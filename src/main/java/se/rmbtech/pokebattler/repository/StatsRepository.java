package se.rmbtech.pokebattler.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import se.rmbtech.pokebattler.model.BattleStats;
import se.rmbtech.pokebattler.util.FileUtils;

import java.io.File;
import java.io.IOException;

public class StatsRepository {
    private final File file;
    private final ObjectMapper mapper = new ObjectMapper();

    public StatsRepository(String path) {
        this.file = new File(path);
    }

    public BattleStats load() {
        if (!file.exists()) {
            return new BattleStats();
        }
        try {
            BattleStats stats = mapper.readValue(file, BattleStats.class);
            return stats != null ? stats : new BattleStats();
        } catch (IOException e) {
            System.out.println("Kunde inte läsa statistikfilen: " + e.getMessage());
            FileUtils.backupCorruptFile(file);
            return new BattleStats();
        }
    }

    public void save(BattleStats stats) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, stats);
        } catch (IOException e) {
            System.out.println("Kunde inte spara statistiken: " + e.getMessage());
        }
    }
}