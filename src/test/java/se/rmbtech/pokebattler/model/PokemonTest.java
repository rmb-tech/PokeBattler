package se.rmbtech.pokebattler.model;

import org.junit.jupiter.api.Test;
import se.rmbtech.pokebattler.exception.InvalidPokemonException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PokemonTest {

    @Test
    void addingFifthAttackThrows() {
        Pokemon p = new Pokemon("Mew", PokemonType.PSYCHIC, 100, 50, 50, List.of(
                new DamageAttack("A", 40, 100, PokemonType.PSYCHIC),
                new DamageAttack("B", 40, 100, PokemonType.PSYCHIC),
                new DamageAttack("C", 40, 100, PokemonType.PSYCHIC),
                new DamageAttack("D", 40, 100, PokemonType.PSYCHIC)));

        assertThrows(InvalidPokemonException.class,
                () -> p.addAttack(new DamageAttack("E", 40, 100, PokemonType.PSYCHIC)));
    }

    @Test
    void tooLongNameThrows() {
        assertThrows(InvalidPokemonException.class, () ->
                new Pokemon("x".repeat(Pokemon.MAX_NAME_LENGTH + 1), PokemonType.NORMAL, 100, 50, 50,
                        List.of(new DamageAttack("Hit", 40, 100, PokemonType.NORMAL))));
    }

    @Test
    void testCreatingPokemonWithEmptyNameThrowsException() {
        assertThrows(InvalidPokemonException.class, () -> {
            new Pokemon("", PokemonType.NORMAL, 100, 50, 50,
                    List.of(new DamageAttack("Scratch", 40, 100, PokemonType.NORMAL)));
        });
    }

    @Test
    void testCreatingPokemonWithInvalidHpThrowsException() {
        assertThrows(InvalidPokemonException.class, () -> {
            new Pokemon("Pikachu", PokemonType.ELECTRIC, -10, 50, 50,
                    List.of(new DamageAttack("Thunder Shock", 40, 100, PokemonType.ELECTRIC)));
        });
    }

    @Test
    void testValidPokemonIsCreatedSuccessfully() {
        Pokemon p = new Pokemon("Pikachu", PokemonType.ELECTRIC, 100, 50, 50,
                List.of(new DamageAttack("Thunderbolt", 90, 100, PokemonType.ELECTRIC)));

        assertEquals("Pikachu", p.getName());
        assertEquals(100, p.getMaxHp());
        assertEquals(100, p.getCurrentHp());
        assertEquals(1, p.getAttacks().size());
    }
}