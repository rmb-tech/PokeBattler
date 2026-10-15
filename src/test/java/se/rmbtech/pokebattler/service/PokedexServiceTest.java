package se.rmbtech.pokebattler.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.rmbtech.pokebattler.exception.PokemonNotFoundException;
import se.rmbtech.pokebattler.model.DamageAttack;
import se.rmbtech.pokebattler.model.Pokemon;
import se.rmbtech.pokebattler.model.PokemonType;
import se.rmbtech.pokebattler.repository.PokemonRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PokedexServiceTest {

    private PokedexService service;
    private List<Pokemon> memoryStorage;

    @BeforeEach
    void setUp() {
        memoryStorage = new ArrayList<>();

        // memoryStorage är för att slipper spara till fil under testerna
        PokemonRepository memoryRepo = new PokemonRepository() {
            @Override
            public List<Pokemon> load() {
                return memoryStorage;
            }

            @Override
            public void save(List<Pokemon> pokemons) {
                memoryStorage = new ArrayList<>(pokemons);
            }
        };

        service = new PokedexService(memoryRepo);
    }

    @Test
    void testAddAndGetPokemon() {
        Pokemon p = new Pokemon("Bulbasaur", PokemonType.GRASS, 120, 45, 40,
                List.of(new DamageAttack("Vine Whip", 45, 100, PokemonType.GRASS)));
        service.addPokemon(p);

        assertEquals(1, service.getPokemons().size());
        assertEquals("Bulbasaur", service.getPokemon(0).getName());
    }

    @Test
    void testRemovePokemonOutOfBoundsThrowsException() {
        assertThrows(PokemonNotFoundException.class, () -> {
            service.removePokemon(0);
        });
    }
}