package se.rmbtech.pokebattler.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PokemonRecord extends WinLossRecord {
    private String pokemonName;
    private int potential;

    public String getPokemonName() {
        return pokemonName;
    }

    @JsonProperty
    public int getPotential() {
        return potential;
    }

    public void setPokemonName(String pokemonName) {
        this.pokemonName = pokemonName;
    }

    public void setPotential(int potential) {
        this.potential = Math.clamp(potential, Pokemon.MIN_POTENTIAL, Pokemon.MAX_POTENTIAL);
    }

    public String displayName() {
        return pokemonName + " (IV " + potential + "%)";
    }
}