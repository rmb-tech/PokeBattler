package se.rmbtech.pokebattler.manager;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import se.rmbtech.pokebattler.model.Pokemon;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FileManager {
    private static final String FILE_PATH = "pokebattler.json";
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void savePokemons(List<Pokemon> pokemons) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_PATH), pokemons);
        } catch (IOException e) {
            System.out.println("Kunde inte spara till JSON fil: " + e.getMessage());
        }
    }

    public static List<Pokemon> loadPokemons() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return null;
        }
        try {
            return mapper.readValue(file, new TypeReference<>() {
            });
        } catch (IOException e) {
            System.out.println("Kunde inte läsa från JSON fil: " + e.getMessage());
            return null;
        }
    }
}