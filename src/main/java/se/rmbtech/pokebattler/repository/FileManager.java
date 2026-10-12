package se.rmbtech.pokebattler.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import se.rmbtech.pokebattler.model.Pokemon;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
            List<Pokemon> loaded = mapper.readValue(file, new TypeReference<List<Pokemon>>() {
            });
            if (loaded == null) {
                return null;
            }
            List<Pokemon> valid = new ArrayList<>(
                    loaded.stream().filter(Objects::nonNull).filter(Pokemon::isComplete).toList());
            if (valid.size() != loaded.size()) {
                System.out.println("⚠️ " + (loaded.size() - valid.size()) + " ofullständig(a) Pokémon hoppades över.");
            }
            return valid;
        } catch (IOException e) {
            System.out.println("Kunde inte läsa JSON-filen: " + e.getMessage());
            backupCorruptFile(file);
            return null;
        }
    }

    private static void backupCorruptFile(File file) {
        try {
            Files.move(file.toPath(), new File(FILE_PATH + ".bak").toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Den trasiga filen sparades som " + FILE_PATH + ".bak");
        } catch (IOException e) {
            System.out.println("Kunde inte göra backup av den trasiga filen.");
        }
    }
}