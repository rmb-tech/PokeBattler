package se.rmbtech.pokebattler.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BattleStats {
    private int totalWins;
    private int totalLosses;
    private final Map<String, WinLossRecord> pokemonRecords = new TreeMap<>();
    private final Map<String, Integer> attackUsage = new TreeMap<>();

    public void registerBattle(BattleResult result) {
        if (result.playerWon()) {
            totalWins++;
        } else {
            totalLosses++;
        }
        for (BattleResult.DuelResult duel : result.duels()) {
            pokemonRecords.computeIfAbsent(duel.pokemonName(), name -> new WinLossRecord())
                    .add(duel.won());
        }
        for (String attackName : result.playerAttacksUsed()) {
            attackUsage.merge(attackName, 1, Integer::sum);
        }
    }

    public int totalBattles() {
        return totalWins + totalLosses;
    }

    public double winPercentage() {
        return totalBattles() == 0 ? 0 : totalWins * 100.0 / totalBattles();
    }

    public Optional<String> findMostUsedAttack() {
        return attackUsage.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);
    }

    public Optional<String> findMostUsedPokemon() {
        return pokemonRecords.entrySet().stream()
                .max(Comparator.comparingInt(entry -> entry.getValue().timesUsed()))
                .map(Map.Entry::getKey);
    }

    public int getTotalWins() { return totalWins; }
    public int getTotalLosses() { return totalLosses; }
    public void setTotalWins(int totalWins) { this.totalWins = Math.max(0, totalWins); }
    public void setTotalLosses(int totalLosses) { this.totalLosses = Math.max(0, totalLosses); }

    public Map<String, WinLossRecord> getPokemonRecords() {
        return Collections.unmodifiableMap(pokemonRecords);
    }

    public void setPokemonRecords(Map<String, WinLossRecord> records) {
        pokemonRecords.clear();
        if (records == null) return;
        records.forEach((name, record) -> {
            if (name != null && record != null) {
                pokemonRecords.put(name, record);
            }
        });
    }

    public Map<String, Integer> getAttackUsage() {
        return Collections.unmodifiableMap(attackUsage);
    }

    public void setAttackUsage(Map<String, Integer> usage) {
        attackUsage.clear();
        if (usage == null) return;
        usage.forEach((name, count) -> {
            if (name != null && count != null && count > 0) {
                attackUsage.put(name, count);
            }
        });
    }
}