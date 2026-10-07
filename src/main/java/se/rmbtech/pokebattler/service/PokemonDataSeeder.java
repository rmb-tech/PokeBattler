package se.rmbtech.pokebattler.service;

import se.rmbtech.pokebattler.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PokemonDataSeeder {
    private static final Random random = new Random();

    private static int applyVariance(int baseValue) {
        double factor = 0.90 + (random.nextDouble() * 0.20);
        return (int) Math.round(baseValue * factor);
    }

    public static Pokemon createWithVariance(String name, PokemonType type, int baseHp, int baseDefense, int baseSpeed, List<Attack> attacks) {
        int hp = Math.clamp(applyVariance(baseHp), 10, 1000);
        int defense = Math.clamp(applyVariance(baseDefense), 0, 100);
        int speed = Math.clamp(applyVariance(baseSpeed), 1, 300);

        List<Attack> variedAttacks = new ArrayList<>();
        for (Attack a : attacks) {
            if (a instanceof DamageAttack da) {
                int variedDamage = Math.clamp(applyVariance(da.getBaseDamage()), 5, 250);
                variedAttacks.add(new DamageAttack(da.getName(), variedDamage, da.getAccuracy(), da.getType()));
            } else if (a instanceof StatusAttack sa) {
                int variedReduction = Math.clamp(applyVariance(sa.getStatReduction()), 5, 50);
                variedAttacks.add(new StatusAttack(sa.getName(), sa.getAccuracy(), sa.getType(), variedReduction));
            }
        }

        return new Pokemon(name, type, hp, defense, speed, variedAttacks);
    }

    public static List<Pokemon> getSeedPokemon() {
        List<Pokemon> pokemonList = new ArrayList<>();

        pokemonList.add(createWithVariance("Pikachu", PokemonType.ELECTRIC, 110, 40, 90, List.of(
                new DamageAttack("Thunderbolt", 90, 100, PokemonType.ELECTRIC),
                new DamageAttack("Quick Attack", 40, 100, PokemonType.NORMAL),
                new StatusAttack("Tail Whip", 100, PokemonType.NORMAL, 15)
        )));

        pokemonList.add(createWithVariance("Charmander", PokemonType.FIRE, 100, 35, 65, List.of(
                new DamageAttack("Ember", 45, 100, PokemonType.FIRE),
                new DamageAttack("Flame Burst", 70, 95, PokemonType.FIRE)
        )));

        pokemonList.add(createWithVariance("Squirtle", PokemonType.WATER, 120, 55, 43, List.of(
                new DamageAttack("Water Gun", 40, 100, PokemonType.WATER),
                new DamageAttack("Aqua Tail", 65, 95, PokemonType.WATER)
        )));

        pokemonList.add(createWithVariance("Bulbasaur", PokemonType.GRASS, 125, 50, 45, List.of(
                new DamageAttack("Vine Whip", 45, 100, PokemonType.GRASS),
                new DamageAttack("Seed Bomb", 60, 95, PokemonType.GRASS)
        )));

        pokemonList.add(createWithVariance("Gastly", PokemonType.GHOST, 90, 25, 80, List.of(
                new DamageAttack("Lick", 30, 100, PokemonType.GHOST),
                new DamageAttack("Shadow Ball", 100, 90, PokemonType.GHOST),
                new StatusAttack("Screech", 85, PokemonType.NORMAL, 20)
        )));

        pokemonList.add(createWithVariance("Machop", PokemonType.FIGHTING, 140, 45, 35, List.of(
                new DamageAttack("Karate Chop", 50, 100, PokemonType.FIGHTING),
                new DamageAttack("Cross Chop", 85, 90, PokemonType.FIGHTING)
        )));

        pokemonList.add(createWithVariance("Onix", PokemonType.ROCK, 110, 95, 70, List.of(
                new DamageAttack("Rock Throw", 50, 90, PokemonType.ROCK),
                new DamageAttack("Iron Tail", 75, 85, PokemonType.STEEL),
                new StatusAttack("Screech", 85, PokemonType.NORMAL, 25)
        )));

        pokemonList.add(createWithVariance("Jigglypuff", PokemonType.NORMAL, 220, 20, 20, List.of(
                new DamageAttack("Pound", 40, 100, PokemonType.NORMAL),
                new DamageAttack("Dazzling Gleam", 80, 95, PokemonType.FAIRY)
        )));

        pokemonList.add(createWithVariance("Abra", PokemonType.PSYCHIC, 70, 15, 90, List.of(
                new DamageAttack("Zen Headbutt", 45, 100, PokemonType.PSYCHIC),
                new DamageAttack("Psyshock", 110, 85, PokemonType.PSYCHIC)
        )));

        pokemonList.add(createWithVariance("Snorlax", PokemonType.NORMAL, 330, 65, 30, List.of(
                new DamageAttack("Lick", 35, 100, PokemonType.GHOST),
                new DamageAttack("Body Slam", 95, 95, PokemonType.NORMAL)
        )));

        pokemonList.add(createWithVariance("Mewtwo", PokemonType.PSYCHIC, 250, 75, 130, List.of(
                new DamageAttack("Psycho Cut", 60, 100, PokemonType.PSYCHIC),
                new DamageAttack("Psystrike", 150, 90, PokemonType.PSYCHIC)
        )));

        pokemonList.add(createWithVariance("Arcanine", PokemonType.FIRE, 180, 60, 95, List.of(
                new DamageAttack("Flamethrower", 90, 100, PokemonType.FIRE),
                new DamageAttack("Extreme Speed", 80, 100, PokemonType.NORMAL)
        )));

        pokemonList.add(createWithVariance("Gyarados", PokemonType.WATER, 210, 70, 81, List.of(
                new DamageAttack("Waterfall", 80, 100, PokemonType.WATER),
                new DamageAttack("Bite", 60, 100, PokemonType.DARK)
        )));

        pokemonList.add(createWithVariance("Gengar", PokemonType.GHOST, 130, 45, 110, List.of(
                new DamageAttack("Shadow Ball", 90, 100, PokemonType.GHOST),
                new DamageAttack("Dark Pulse", 80, 100, PokemonType.DARK)
        )));

        pokemonList.add(createWithVariance("Dragonite", PokemonType.DRAGON, 220, 75, 80, List.of(
                new DamageAttack("Dragon Claw", 80, 100, PokemonType.DRAGON),
                new DamageAttack("Hyper Beam", 120, 85, PokemonType.NORMAL)
        )));

        pokemonList.add(createWithVariance("Scyther", PokemonType.BUG, 150, 55, 105, List.of(
                new DamageAttack("Wing Attack", 60, 100, PokemonType.FLYING),
                new DamageAttack("X-Scissor", 80, 100, PokemonType.BUG)
        )));

        pokemonList.add(createWithVariance("Eevee", PokemonType.NORMAL, 120, 40, 55, List.of(
                new DamageAttack("Tackle", 40, 100, PokemonType.NORMAL),
                new DamageAttack("Bite", 60, 100, PokemonType.DARK),
                new StatusAttack("Tail Whip", 100, PokemonType.NORMAL, 15)
        )));

        pokemonList.add(createWithVariance("Lucario", PokemonType.FIGHTING, 150, 50, 90, List.of(
                new DamageAttack("Aura Sphere", 80, 100, PokemonType.FIGHTING),
                new DamageAttack("Metal Claw", 50, 95, PokemonType.STEEL)
        )));

        pokemonList.add(createWithVariance("Lapras", PokemonType.WATER, 250, 65, 60, List.of(
                new DamageAttack("Ice Beam", 90, 100, PokemonType.ICE),
                new DamageAttack("Surf", 90, 100, PokemonType.WATER)
        )));

        pokemonList.add(createWithVariance("Gardevoir", PokemonType.PSYCHIC, 140, 45, 80, List.of(
                new DamageAttack("Psychic", 90, 100, PokemonType.PSYCHIC),
                new DamageAttack("Moonblast", 95, 100, PokemonType.FAIRY)
        )));

        pokemonList.add(createWithVariance("Tyranitar", PokemonType.ROCK, 230, 85, 61, List.of(
                new DamageAttack("Stone Edge", 100, 80, PokemonType.ROCK),
                new DamageAttack("Crunch", 80, 100, PokemonType.DARK)
        )));

        return pokemonList;
    }

    public static Pokemon getRandomSeedPokemon() {
        List<Pokemon> seeds = getSeedPokemon();
        return seeds.get(random.nextInt(seeds.size()));
    }
}