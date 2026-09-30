package se.rmbtech.pokebattler.service;

import se.rmbtech.pokebattler.model.Attack;
import se.rmbtech.pokebattler.model.Pokemon;
import se.rmbtech.pokebattler.util.InputHelper;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class BattleEngine {
    private final Scanner scanner;
    private final Random random;

    public BattleEngine(Scanner scanner) {
        this.scanner = scanner;
        this.random = new Random();
    }

    public boolean startBattle(Pokemon player, Pokemon cpu) {
        System.out.println("\n====================================================");
        System.out.printf("⚔️ STRID BÖRJAR: %s möter vild %s! ⚔️%n", player.getName(), cpu.getName());
        System.out.println("====================================================\n");

        int turn = 1;

        while (!player.isFainted() && !cpu.isFainted()) {
            System.out.println("--- RUNDA " + turn + " ---");
            System.out.printf("%s (HP: %d/%d | Def: %d)  vs  Vild %s (HP: %d/%d | Def: %d)%n%n",
                    player.getName(), player.getCurrentHp(), player.getMaxHp(), player.getDefense(),
                    cpu.getName(), cpu.getCurrentHp(), cpu.getMaxHp(), cpu.getDefense());

            boolean playerGoesFirst = determineTurnOrder(player, cpu);

            if (playerGoesFirst) {
                System.out.println("⚡ " + player.getName() + " är snabbare och attackerar först!");
                if (executePlayerTurn(player, cpu)) break;
                if (executeCpuTurn(cpu, player)) break;
            } else {
                System.out.println("⚡ Vild " + cpu.getName() + " är snabbare och attackerar först!");
                if (executeCpuTurn(cpu, player)) break;
                if (executePlayerTurn(player, cpu)) break;
            }

            turn++;
            System.out.println("\n----------------------------------------\n");
        }
        return !player.isFainted();
    }

    private boolean determineTurnOrder(Pokemon player, Pokemon cpu) {
        if (player.getSpeed() > cpu.getSpeed()) {
            return true;
        } else if (cpu.getSpeed() > player.getSpeed()) {
            return false;
        } else {
            return random.nextBoolean();
        }
    }

    private boolean executePlayerTurn(Pokemon player, Pokemon cpu) {
        Attack attack = choosePlayerAttack(player);
        executeAttack(player, attack, cpu);

        if (cpu.isFainted()) {
            System.out.printf("%n🎉 Vild %s svimmade! Du vann striden! 🎉%n", cpu.getName());
            return true;
        }
        return false;
    }

    private boolean executeCpuTurn(Pokemon cpu, Pokemon player) {
        System.out.println();
        Attack attack = chooseCpuAttack(cpu);
        executeAttack(cpu, attack, player);

        if (player.isFainted()) {
            System.out.printf("%n☠️ Din %s svimmade! Du förlorade striden... ☠️%n", player.getName());
            return true;
        }
        return false;
    }

    private Attack choosePlayerAttack(Pokemon player) {
        List<Attack> attacks = player.getAttacks();
        System.out.println("Välj din attack:");
        for (int i = 0; i < attacks.size(); i++) {
            Attack a = attacks.get(i);
            System.out.printf("%d) %s (Skada: %d, Träff: %d%%, Typ: %s)%n",
                    (i + 1), a.getName(), a.getBaseDamage(), a.getAccuracy(), a.getType());
        }

        int choice = InputHelper.readInt(scanner, "Ditt val: ", 1, attacks.size());
        return attacks.get(choice - 1);
    }

    private Attack chooseCpuAttack(Pokemon cpu) {
        List<Attack> attacks = cpu.getAttacks();
        return attacks.get(random.nextInt(attacks.size()));
    }

    private void executeAttack(Pokemon attacker, Attack attack, Pokemon defender) {
        System.out.printf("💥 %s använder %s!%n", attacker.getName(), attack.getName());

        int rawDamage = attack.getBaseDamage();
        int defenseBlock = defender.getDefense() / 5;
        int finalDamage = Math.max(1, rawDamage - defenseBlock);

        if (defenseBlock > 0) {
            System.out.printf("   🛡️ %s försvar (%d) minskade skadan med %d!%n",
                    defender.getName(), defender.getDefense(), defenseBlock);
        }

        defender.takeDamage(finalDamage);

        System.out.printf("   -> %s tog %d skada! (%d/%d HP kvar)%n",
                defender.getName(), finalDamage, defender.getCurrentHp(), defender.getMaxHp());
    }
}