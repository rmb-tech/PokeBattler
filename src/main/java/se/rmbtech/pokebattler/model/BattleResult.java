package se.rmbtech.pokebattler.model;

import java.util.List;
import java.util.Objects;

public record BattleResult(BattleMode mode, boolean playerWon, List<DuelResult> duels, List<String> playerAttacksUsed) {

    public BattleResult {
        Objects.requireNonNull(mode, "mode");
        duels = List.copyOf(duels);
        playerAttacksUsed = List.copyOf(playerAttacksUsed);
    }

    public record DuelResult(String pokemonId, String pokemonName, int potential, boolean won) {
    }
}