package se.rmbtech.pokebattler.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BattleStats {
    private int totalWins;
    private int totalLosses;
    private final Map<String, PokemonRecord> pokemonRecords = new TreeMap<>();
    private final Map<String, Integer> attackUsage = new TreeMap<>();
    private final Map<BattleMode, WinLossRecord> modeRecords = new EnumMap<>(BattleMode.class);

    public void registerBattle(BattleResult result) {
        modeRecords.computeIfAbsent(result.mode(), mode -> new WinLossRecord()).add(result.playerWon());
        if (result.playerWon()) {
            totalWins++;
        } else {
            totalLosses++;
        }
        for (BattleResult.DuelResult duel : result.duels()) {
            PokemonRecord record = pokemonRecords.computeIfAbsent(duel.pokemonId(), id -> new PokemonRecord());
            record.setPokemonName(duel.pokemonName());
            record.setPotential(duel.potential());
            record.add(duel.won());
        }
        for (String attackName : result.playerAttacksUsed()) {
            attackUsage.merge(attackName, 1, Integer::sum);
        }
    }

    public int totalBattles() {
        return totalWins + totalLosses;
    }

    public double winPercentage() {
        return totalBattles() == 0 ? 0 : (totalWins * 100.0) / totalBattles();
    }

    public Optional<String> findMostUsedAttack() {
        return attackUsage.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);
    }

    public Optional<String> findMostUsedPokemon() {
        return pokemonRecords.values().stream()
                .max(Comparator.comparingInt(PokemonRecord::timesUsed)
                        .thenComparing(PokemonRecord::getPokemonName,
                                Comparator.nullsFirst(Comparator.reverseOrder())))
                .map(PokemonRecord::displayName);
    }

    public int getTotalWins() {
        return totalWins;
    }

    public int getTotalLosses() {
        return totalLosses;
    }

    @JsonProperty
    public void setTotalWins(int totalWins) {
        this.totalWins = Math.max(0, totalWins);
    }
    @JsonProperty
    public void setTotalLosses(int totalLosses) {
        this.totalLosses = Math.max(0, totalLosses);
    }

    public Map<String, PokemonRecord> getPokemonRecords() {
        return Collections.unmodifiableMap(pokemonRecords);
    }

    @JsonProperty
    public void setPokemonRecords(Map<String, PokemonRecord> records) {
        pokemonRecords.clear();
        if (records == null) return;
        records.forEach((id, record) -> {
            if (id != null && record != null) {
                pokemonRecords.put(id, record);
            }
        });
    }

    public Map<BattleMode, WinLossRecord> getModeRecords() {
        return Collections.unmodifiableMap(modeRecords);
    }

    @JsonProperty
    public void setModeRecords(Map<BattleMode, WinLossRecord> records) {
        modeRecords.clear();
        if (records == null) return;
        records.forEach((mode, record) -> {
            if (mode != null && record != null) {
                modeRecords.put(mode, record);
            }
        });
    }

    @JsonProperty
    public Map<String, Integer> getAttackUsage() {
        return Collections.unmodifiableMap(attackUsage);
    }

    @JsonProperty
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