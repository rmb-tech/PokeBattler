package se.rmbtech.pokebattler.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DamageAttackTest {

    @Test
    void testCalculateDamageReturnsAtLeastOne() {
        DamageAttack attack = new DamageAttack("Scratch", 5, 100, PokemonType.NORMAL);
        Pokemon defender = new Pokemon("Onix", PokemonType.ROCK, 100, 200, 50,
                List.of(new DamageAttack("Tackle", 40, 100, PokemonType.NORMAL)));

        int damage = attack.calculateDamage(defender, 1.0, false);

        assertTrue(damage >= 1);
        assertEquals(1, damage);
    }

    @Test
    void testCalculateDamageAppliesTypeAndCrit() {
        DamageAttack attack = new DamageAttack("Thunderbolt", 90, 100, PokemonType.ELECTRIC);
        Pokemon defender = new Pokemon("Squirtle", PokemonType.WATER, 100, 50, 50,
                List.of(new DamageAttack("Water Gun", 40, 100, PokemonType.WATER)));

        int damage = attack.calculateDamage(defender, 1.6, false);

        // defenseFactor = 50 / (50 + 50) = 0.5
        // mitigatedDamage = 90 * 0.5 = 45
        // rawDamage = 45 * 1.6 = 72
        assertEquals(72, damage);
    }
}