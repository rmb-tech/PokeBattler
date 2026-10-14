package se.rmbtech.pokebattler.service;

import se.rmbtech.pokebattler.exception.PokemonNotFoundException;
import se.rmbtech.pokebattler.model.Pokemon;
import se.rmbtech.pokebattler.repository.FileManager;
import se.rmbtech.pokebattler.util.UIHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PokedexService {
    private static final int DEFAULT_TEAM_SIZE = 6;
    private List<Pokemon> pokemons = new ArrayList<>();

    public List<Pokemon> getPokemons() {
        return new ArrayList<>(pokemons);
    }

    public void setPokemons(List<Pokemon> pokemons) {
        this.pokemons = (pokemons != null) ? new ArrayList<>(pokemons) : new ArrayList<>();
        save();
    }

    public void addPokemon(Pokemon p) {
        if (p != null) {
            pokemons.add(p);
            save();
        }
    }

    public void removePokemon(int index) {
        if (index < 0 || index >= pokemons.size()) {
            throw new PokemonNotFoundException("Index " + index + " finns inte i Pokédexet.");
        }
        pokemons.remove(index);
        save();
    }

    public Pokemon getPokemon(int index) {
        if (index < 0 || index >= pokemons.size()) {
            throw new PokemonNotFoundException("Index " + index + " finns inte i Pokédexet.");
        }
        return pokemons.get(index);
    }

    public void healAll() {
        for (Pokemon p : pokemons) {
            p.healFull();
        }
        save();
    }

    public void seedDefaultData() {
        List<Pokemon> masterList = PokemonDataSeeder.getSeedPokemon();
        if (masterList != null && !masterList.isEmpty()) {
            Collections.shuffle(masterList);
            int count = Math.min(masterList.size(), DEFAULT_TEAM_SIZE);
            this.pokemons = new ArrayList<>(masterList.subList(0, count));
        } else {
            this.pokemons = new ArrayList<>();
        }
        save();
    }

    public void save() {
        FileManager.savePokemons(pokemons);
    }

    public void loadOrSeedData() {
        List<Pokemon> loadedData = FileManager.loadPokemons();
        if (loadedData != null && !loadedData.isEmpty()) {
            UIHelper.simulateLoading("Laddar sparad data från 'pokebattler.json'", 1500);
            this.pokemons = new ArrayList<>(loadedData);
            System.out.println("Data har laddats framgångsrikt!");
        } else {
            UIHelper.simulateLoading("Ingen användbar sparfil hittades. Genererar en starttrupp med 6 slumpade Pokémon", 2000);
            seedDefaultData();
            System.out.println("Starttrupp skapad och sparad!");
        }
    }
}