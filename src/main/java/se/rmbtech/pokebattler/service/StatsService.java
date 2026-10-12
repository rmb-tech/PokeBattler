package se.rmbtech.pokebattler.service;

import se.rmbtech.pokebattler.model.BattleResult;
import se.rmbtech.pokebattler.model.BattleStats;
import se.rmbtech.pokebattler.repository.StatsRepository;

public class StatsService {
    private final StatsRepository repository;
    private final BattleStats stats;

    public StatsService(StatsRepository repository) {
        this.repository = repository;
        this.stats = repository.load();
    }

    public void recordBattle(BattleResult result) {
        stats.registerBattle(result);
        repository.save(stats);
    }

    public BattleStats getStats() {
        return stats;
    }
}