package se.rmbtech.pokebattler.service;

import se.rmbtech.pokebattler.model.DamageAttack;
import se.rmbtech.pokebattler.model.Pokemon;
import se.rmbtech.pokebattler.model.PokemonType;
import se.rmbtech.pokebattler.model.StatusAttack;

import java.util.List;
import java.util.Random;

public class RaidDataSeeder {
    private static final Random random = new Random();

    public static List<Pokemon> getRaidBosses() {
        return List.of(
                new Pokemon("MEGA Mewtwo", PokemonType.PSYCHIC, 600, 65, 130, List.of(
                        new DamageAttack("Psystrike", 140, 95, PokemonType.PSYCHIC),
                        new StatusAttack("Screech", 100, PokemonType.NORMAL, 25)
                )),
                new Pokemon("MEGA Charizard", PokemonType.FIRE, 500, 60, 110, List.of(
                        new DamageAttack("Blast Burn", 150, 90, PokemonType.FIRE),
                        new DamageAttack("Air Slash", 75, 95, PokemonType.FLYING)
                )),
                new Pokemon("MEGA Tyranitar", PokemonType.ROCK, 600, 85, 75, List.of(
                        new DamageAttack("Stone Edge", 120, 90, PokemonType.ROCK),
                        new StatusAttack("Scary Face", 95, PokemonType.NORMAL, 20)
                ))
        );
    }

    public static Pokemon getRandomRaidBoss() {
        List<Pokemon> bosses = getRaidBosses();
        return bosses.get(random.nextInt(bosses.size()));
    }
}