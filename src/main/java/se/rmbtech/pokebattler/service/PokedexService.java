package se.rmbtech.pokebattler.service;

import se.rmbtech.pokebattler.exception.PokemonNotFoundException;
import se.rmbtech.pokebattler.model.Pokemon;
import se.rmbtech.pokebattler.repository.PokemonRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class PokedexService {
    private static final int DEFAULT_TEAM_SIZE = 6;
    private final PokemonRepository repository;
    private List<Pokemon> pokemons = new ArrayList<>();

    public PokedexService(PokemonRepository repository) {
        this.repository = Objects.requireNonNull(repository, "repository");
    }

    public List<Pokemon> getPokemons() {
        return new ArrayList<>(pokemons);
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
        repository.save(pokemons);
    }

    public boolean loadFromFile() {
        List<Pokemon> loadedData = repository.load();
        if (loadedData != null && !loadedData.isEmpty()) {
            this.pokemons = new ArrayList<>(loadedData);
            return true;
        }
        return false;
    }
}