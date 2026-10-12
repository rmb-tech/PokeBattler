package se.rmbtech.pokebattler.model;

import java.util.List;

public record BattleResult(boolean playerWon, List<DuelResult> duels, List<String> playerAttacksUsed) {

    public BattleResult {
        duels = List.copyOf(duels);
        playerAttacksUsed = List.copyOf(playerAttacksUsed);
    }

    public record DuelResult(String pokemonName, boolean won) {}
}