package se.rmbtech.pokebattler.service;

import se.rmbtech.pokebattler.exception.PokemonNotFoundException;
import se.rmbtech.pokebattler.model.Pokemon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PokedexService {
    private List<Pokemon> pokemons = new ArrayList<>();

    public List<Pokemon> getPokemons() {
        return new ArrayList<>(pokemons);
    }

    public void setPokemons(List<Pokemon> pokemons) {
        this.pokemons = pokemons;
    }

    public void addPokemon(Pokemon p) {
        pokemons.add(p);
    }

    public void removePokemon(int index) {
        if (index < 0 || index >= pokemons.size()) {
            throw new PokemonNotFoundException("Index " + index + " finns inte i Pokédexet.");
        }
        pokemons.remove(index);
    }

    public Pokemon getPokemon(int index) {
        if (index < 0 || index >= pokemons.size()) {
            throw new PokemonNotFoundException("Index " + index + " finns inte i Pokédexet.");
        }
        return pokemons.get(index);
    }

    public void seedDefaultData() {
        List<Pokemon> masterList = PokemonDataSeeder.getSeedPokemon();

        Collections.shuffle(masterList);
        this.pokemons = new ArrayList<>(masterList.subList(0, 6));
    }
}