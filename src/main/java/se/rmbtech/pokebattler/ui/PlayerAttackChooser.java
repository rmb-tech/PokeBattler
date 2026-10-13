package se.rmbtech.pokebattler.ui;

import se.rmbtech.pokebattler.exception.InvalidPokemonException;
import se.rmbtech.pokebattler.model.Attack;
import se.rmbtech.pokebattler.model.Pokemon;
import se.rmbtech.pokebattler.service.AttackChooser;
import se.rmbtech.pokebattler.util.InputHelper;

import java.util.List;
import java.util.Scanner;

public class PlayerAttackChooser implements AttackChooser {
    private final Scanner scanner;

    public PlayerAttackChooser(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public Attack chooseAttack(Pokemon attacker, Pokemon defender) {
        List<Attack> attacks = attacker.getAttacks();
        if (attacks.isEmpty()) {
            throw new InvalidPokemonException(attacker.getName() + " har inga attacker.");
        }

        System.out.println("\n--- DIN TUR (" + attacker.getName() + ") ---");
        for (int i = 0; i < attacks.size(); i++) {
            System.out.printf("%d) %s%n", i + 1, attacks.get(i));
        }

        int choice = InputHelper.readInt(scanner, "Välj attack: ", 1, attacks.size());
        return attacks.get(choice - 1);
    }
}