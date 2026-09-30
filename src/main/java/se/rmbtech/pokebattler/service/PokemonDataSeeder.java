package se.rmbtech.pokebattler.service;

import se.rmbtech.pokebattler.model.Attack;
import se.rmbtech.pokebattler.model.Pokemon;
import se.rmbtech.pokebattler.model.PokemonType;

import java.util.ArrayList;
import java.util.List;

public class PokemonDataSeeder {

    public static List<Pokemon> getDefaultPokemon() {
        List<Pokemon> masterList = new ArrayList<>();

        masterList.add(new Pokemon("Pikachu", PokemonType.ELECTRIC, 110, 40, 90, List.of(
                new Attack("Thunderbolt", 90, 100, PokemonType.ELECTRIC),
                new Attack("Quick Attack", 40, 100, PokemonType.NORMAL)
        )));

        masterList.add(new Pokemon("Charmander", PokemonType.FIRE, 100, 35, 65, List.of(
                new Attack("Ember", 45, 100, PokemonType.FIRE),
                new Attack("Flame Burst", 70, 95, PokemonType.FIRE)
        )));

        masterList.add(new Pokemon("Squirtle", PokemonType.WATER, 120, 55, 43, List.of(
                new Attack("Water Gun", 40, 100, PokemonType.WATER),
                new Attack("Aqua Tail", 65, 95, PokemonType.WATER)
        )));

        masterList.add(new Pokemon("Bulbasaur", PokemonType.GRASS, 125, 50, 45, List.of(
                new Attack("Vine Whip", 45, 100, PokemonType.GRASS),
                new Attack("Seed Bomb", 60, 95, PokemonType.GRASS)
        )));

        masterList.add(new Pokemon("Gastly", PokemonType.GHOST, 90, 25, 80, List.of(
                new Attack("Lick", 30, 100, PokemonType.GHOST),
                new Attack("Shadow Ball", 100, 90, PokemonType.GHOST)
        )));

        masterList.add(new Pokemon("Machop", PokemonType.FIGHTING, 140, 45, 35, List.of(
                new Attack("Karate Chop", 50, 100, PokemonType.FIGHTING),
                new Attack("Cross Chop", 85, 90, PokemonType.FIGHTING)
        )));

        masterList.add(new Pokemon("Onix", PokemonType.ROCK, 110, 95, 70, List.of(
                new Attack("Rock Throw", 50, 90, PokemonType.ROCK),
                new Attack("Iron Tail", 75, 85, PokemonType.STEEL)
        )));

        masterList.add(new Pokemon("Jigglypuff", PokemonType.NORMAL, 220, 20, 20, List.of(
                new Attack("Pound", 40, 100, PokemonType.NORMAL),
                new Attack("Dazzling Gleam", 80, 95, PokemonType.FAIRY)
        )));

        masterList.add(new Pokemon("Abra", PokemonType.PSYCHIC, 70, 15, 90, List.of(
                new Attack("Zen Headbutt", 45, 100, PokemonType.PSYCHIC),
                new Attack("Psyshock", 110, 85, PokemonType.PSYCHIC)
        )));

        masterList.add(new Pokemon("Snorlax", PokemonType.NORMAL, 330, 65, 30, List.of(
                new Attack("Lick", 35, 100, PokemonType.GHOST),
                new Attack("Body Slam", 95, 95, PokemonType.NORMAL)
        )));

        masterList.add(new Pokemon("Mewtwo", PokemonType.PSYCHIC, 250, 75, 130, List.of(
                new Attack("Psycho Cut", 60, 100, PokemonType.PSYCHIC),
                new Attack("Psystrike", 150, 90, PokemonType.PSYCHIC)
        )));

        masterList.add(new Pokemon("Arcanine", PokemonType.FIRE, 180, 60, 95, List.of(
                new Attack("Flamethrower", 90, 100, PokemonType.FIRE),
                new Attack("Extreme Speed", 80, 100, PokemonType.NORMAL)
        )));

        masterList.add(new Pokemon("Gyarados", PokemonType.WATER, 210, 70, 81, List.of(
                new Attack("Waterfall", 80, 100, PokemonType.WATER),
                new Attack("Bite", 60, 100, PokemonType.DARK)
        )));

        masterList.add(new Pokemon("Gengar", PokemonType.GHOST, 130, 45, 110, List.of(
                new Attack("Shadow Ball", 90, 100, PokemonType.GHOST),
                new Attack("Dark Pulse", 80, 100, PokemonType.DARK)
        )));

        masterList.add(new Pokemon("Dragonite", PokemonType.DRAGON, 220, 75, 80, List.of(
                new Attack("Dragon Claw", 80, 100, PokemonType.DRAGON),
                new Attack("Hyper Beam", 120, 85, PokemonType.NORMAL)
        )));

        masterList.add(new Pokemon("Scyther", PokemonType.BUG, 150, 55, 105, List.of(
                new Attack("Wing Attack", 60, 100, PokemonType.FLYING),
                new Attack("X-Scissor", 80, 100, PokemonType.BUG)
        )));

        masterList.add(new Pokemon("Eevee", PokemonType.NORMAL, 120, 40, 55, List.of(
                new Attack("Tackle", 40, 100, PokemonType.NORMAL),
                new Attack("Bite", 60, 100, PokemonType.DARK)
        )));

        masterList.add(new Pokemon("Lucario", PokemonType.FIGHTING, 150, 50, 90, List.of(
                new Attack("Aura Sphere", 80, 100, PokemonType.FIGHTING),
                new Attack("Metal Claw", 50, 95, PokemonType.STEEL)
        )));

        masterList.add(new Pokemon("Lapras", PokemonType.WATER, 250, 65, 60, List.of(
                new Attack("Ice Beam", 90, 100, PokemonType.ICE),
                new Attack("Surf", 90, 100, PokemonType.WATER)
        )));

        masterList.add(new Pokemon("Gardevoir", PokemonType.PSYCHIC, 140, 45, 80, List.of(
                new Attack("Psychic", 90, 100, PokemonType.PSYCHIC),
                new Attack("Moonblast", 95, 100, PokemonType.FAIRY)
        )));

        masterList.add(new Pokemon("Tyranitar", PokemonType.ROCK, 230, 85, 61, List.of(
                new Attack("Stone Edge", 100, 80, PokemonType.ROCK),
                new Attack("Crunch", 80, 100, PokemonType.DARK)
        )));

        return masterList;
    }
}