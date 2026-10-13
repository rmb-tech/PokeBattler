package se.rmbtech.pokebattler.service;

import se.rmbtech.pokebattler.model.Attack;
import se.rmbtech.pokebattler.model.Pokemon;

public interface AttackChooser {
    Attack chooseAttack(Pokemon attacker, Pokemon defender);
}