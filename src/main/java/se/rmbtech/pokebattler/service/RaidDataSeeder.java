package se.rmbtech.pokebattler.service;

import se.rmbtech.pokebattler.model.DamageAttack;
import se.rmbtech.pokebattler.model.Pokemon;
import se.rmbtech.pokebattler.model.PokemonType;
import se.rmbtech.pokebattler.model.StatusAttack;

import java.util.List;
import java.util.Random;

public final class RaidDataSeeder {
    private static final Random random = new Random();

    private RaidDataSeeder() {}

    public static List<Pokemon> getRaidBosses() {
        return List.of(
                new Pokemon("MEGA Mewtwo", PokemonType.PSYCHIC, 600, 65, 130, List.of(
                        new DamageAttack("Psystrike", 140, 95, PokemonType.PSYCHIC),
                        new DamageAttack("Shadow Ball", 90, 100, PokemonType.GHOST),
                        new StatusAttack("Screech", 100, PokemonType.NORMAL, 25)
                )),
                new Pokemon("MEGA Charizard", PokemonType.FIRE, 500, 60, 110, List.of(
                        new DamageAttack("Blast Burn", 150, 90, PokemonType.FIRE),
                        new DamageAttack("Air Slash", 75, 95, PokemonType.FLYING),
                        new DamageAttack("Dragon Claw", 80, 100, PokemonType.DRAGON)
                )),
                new Pokemon("MEGA Tyranitar", PokemonType.ROCK, 600, 85, 75, List.of(
                        new DamageAttack("Stone Edge", 120, 90, PokemonType.ROCK),
                        new DamageAttack("Crunch", 80, 100, PokemonType.DARK),
                        new StatusAttack("Scary Face", 95, PokemonType.NORMAL, 20)
                )),
                new Pokemon("MEGA Rayquaza", PokemonType.DRAGON, 620, 70, 120, List.of(
                        new DamageAttack("Dragon Ascent", 140, 90, PokemonType.DRAGON),
                        new DamageAttack("Outrage", 110, 85, PokemonType.DRAGON),
                        new DamageAttack("Extreme Speed", 80, 100, PokemonType.NORMAL)
                )),
                new Pokemon("MEGA Blastoise", PokemonType.WATER, 550, 95, 80, List.of(
                        new DamageAttack("Hydro Cannon", 150, 90, PokemonType.WATER),
                        new DamageAttack("Flash Cannon", 80, 100, PokemonType.STEEL),
                        new DamageAttack("Dark Pulse", 80, 100, PokemonType.DARK)
                ))
        );
    }

    public static Pokemon getRandomRaidBoss() {
        List<Pokemon> bosses = getRaidBosses();
        return bosses.get(random.nextInt(bosses.size()));
    }
}