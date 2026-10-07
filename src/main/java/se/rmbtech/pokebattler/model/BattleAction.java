package se.rmbtech.pokebattler.model;

public interface BattleAction {
    void execute(Pokemon attack, Pokemon defender);
}
