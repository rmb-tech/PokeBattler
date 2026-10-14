package se.rmbtech.pokebattler.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import se.rmbtech.pokebattler.model.Pokemon;
import se.rmbtech.pokebattler.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public final class FileManager {
    private static final String FILE_PATH = "pokebattler.json";
    private static final ObjectMapper mapper = new ObjectMapper();

    private FileManager() {}

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
            List<Pokemon> loaded = mapper.readValue(file, new TypeReference<List<Pokemon>>() {
            });
            if (loaded == null) {
                return null;
            }
            List<Pokemon> valid = new ArrayList<>(
                    loaded.stream().filter(Objects::nonNull).filter(Pokemon::isComplete).toList());
            int skipped = loaded.size() - valid.size();
            if (skipped > 0) {
                System.out.println("❌ " + skipped + " ofullständig(a) Pokémon hoppades över.");
            }

            if (!loaded.isEmpty() && valid.isEmpty()) {
                System.out.println("❗Alla Pokémon i filen var ofullständiga. Skapar backup.");
                FileUtils.backupCorruptFile(file);
                return null;
            }
            return valid;
        } catch (IOException e) {
            System.out.println("Kunde inte läsa JSON-filen: " + e.getMessage());
            FileUtils.backupCorruptFile(file);
            return null;
        }
    }
}