package se.rmbtech.pokebattler.ui;

import se.rmbtech.pokebattler.model.BattleStats;
import se.rmbtech.pokebattler.model.PokemonRecord;
import se.rmbtech.pokebattler.service.StatsService;

import java.util.Comparator;

public class StatsUI {
    private final StatsService statsService;

    public StatsUI(StatsService statsService) {
        this.statsService = statsService;
    }

    public void showStats() {
        BattleStats stats = statsService.getStats();
        System.out.println("\n=== 📊 STRIDSSTATISTIK ===");

        if (stats.totalBattles() == 0) {
            System.out.println("Inga strider har spelats än. Starta en strid så dyker statistiken upp här!");
            return;
        }

        System.out.printf("Strider: %d | Vinster: %d | Förluster: %d | Vinstprocent: %.0f%%%n",
                stats.totalBattles(), stats.getTotalWins(), stats.getTotalLosses(), stats.winPercentage());
        System.out.println("Mest använda Pokémon: " + stats.findMostUsedPokemon().orElse("-"));
        System.out.println("Mest använda attack:  " + stats.findMostUsedAttack().orElse("-"));

        System.out.println("\nLäge (vunna/förlorade strider):");
        stats.getModeRecords().forEach((mode, record) ->
                System.out.printf("  %-26s %2d V / %2d F  (%3.0f%% vinster)%n",
                        mode.getLabel(), record.getWins(), record.getLosses(), record.winPercentage()));

        System.out.println("\nPokémon (vunna/förlorade dueller):");
        stats.getPokemonRecords().values().stream()
                .sorted(Comparator.comparingInt(PokemonRecord::timesUsed).reversed()
                        .thenComparing(PokemonRecord::getPokemonName,
                                Comparator.nullsFirst(Comparator.naturalOrder())))
                .forEach(r -> System.out.printf("  %-22s %2d V / %2d F  (%3.0f%% vinster)%n",
                        r.displayName(), r.getWins(), r.getLosses(), r.winPercentage()));
    }
}