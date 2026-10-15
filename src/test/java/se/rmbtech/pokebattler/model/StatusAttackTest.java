package se.rmbtech.pokebattler.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StatusAttackTest {

    @Test
    void testStatusAttackLowersDefenseOnce() {
        StatusAttack statusAttack = new StatusAttack("Screech", 100, PokemonType.NORMAL, 15);

        Pokemon attacker = new Pokemon("Ekans", PokemonType.POISON, 100, 50, 50,
                List.of(new DamageAttack("Wrap", 30, 100, PokemonType.POISON)));

        Pokemon defender = new Pokemon("Charmander", PokemonType.FIRE, 100, 50, 50,
                List.of(new DamageAttack("Scratch", 40, 100, PokemonType.NORMAL)));

        statusAttack.execute(attacker, defender);

        // 35 (50 - 15)
        assertEquals(35, defender.getDefense());

        // Kolla så att statusattack inte går att använda flera gånger.
        assertFalse(statusAttack.isUsefulAgainst(defender));
    }
}