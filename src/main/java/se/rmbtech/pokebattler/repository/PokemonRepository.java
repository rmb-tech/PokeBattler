package se.rmbtech.pokebattler.repository;

import se.rmbtech.pokebattler.model.Pokemon;

import java.util.List;

public interface PokemonRepository {
    List<Pokemon> load();

    void save(List<Pokemon> pokemons);
}