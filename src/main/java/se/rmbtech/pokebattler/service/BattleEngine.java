package se.rmbtech.pokebattler.service;

import se.rmbtech.pokebattler.model.Attack;
import se.rmbtech.pokebattler.model.Pokemon;
import se.rmbtech.pokebattler.model.StatusAttack;
import se.rmbtech.pokebattler.util.InputHelper;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class BattleEngine {
    private final Scanner scanner;
    private final Random random = new Random();

    public BattleEngine(Scanner scanner) {
        this.scanner = scanner;
    }

    public void startTeamBattle(List<Pokemon> playerTeam, List<Pokemon> cpuTeam) {
        int playerIdx = 0;
        int cpuIdx = 0;

        System.out.println("\n==================================================");
        System.out.println("⚔️ STRIDEN STARTAR! (" + playerTeam.size() + "v" + cpuTeam.size() + ")");
        System.out.println("==================================================");

        while (playerIdx < playerTeam.size() && cpuIdx < cpuTeam.size()) {
            Pokemon activePlayer = playerTeam.get(playerIdx);
            Pokemon activeCpu = cpuTeam.get(cpuIdx);

            System.out.println("\n--------------------------------------------------");
            System.out.printf("🔥 AVSNITT: %s (HP: %d/%d) VS Vild %s (HP: %d/%d)%n",
                    activePlayer.getName(), activePlayer.getCurrentHp(), activePlayer.getMaxHp(),
                    activeCpu.getName(), activeCpu.getCurrentHp(), activeCpu.getMaxHp());
            System.out.println("--------------------------------------------------");

            fightDuel(activePlayer, activeCpu);

            if (activePlayer.isFainted()) {
                System.out.println("\n💀 Din " + activePlayer.getName() + " svimmade!");
                playerIdx++;
                if (playerIdx < playerTeam.size()) {
                    System.out.println("🔄 Du skickar ut nästa Pokémon: " + playerTeam.get(playerIdx).getName() + "!");
                }
            }

            if (activeCpu.isFainted()) {
                System.out.println("\n💀 Vild " + activeCpu.getName() + " svimmade!");
                cpuIdx++;
                if (cpuIdx < cpuTeam.size()) {
                    System.out.println("🔄 Motståndaren skickar ut sin nästa Pokémon: " + cpuTeam.get(cpuIdx).getName() + "!");
                }
            }
        }

        System.out.println("\n==================================================");
        if (playerIdx < playerTeam.size()) {
            System.out.println("🏆 DU VANN STRIDEN!");
        } else {
            System.out.println("☠️ ALLA DINA POKÉMON HAR SVIMMAT. DU FÖRLORADE!");
        }
        System.out.println("==================================================");
        playerTeam.forEach(Pokemon::resetBattleStats);
        cpuTeam.forEach(Pokemon::resetBattleStats);
    }

    private void fightDuel(Pokemon player, Pokemon cpu) {
        while (!player.isFainted() && !cpu.isFainted()) {
            boolean playerFirst = determineTurnOrder(player, cpu);

            if (playerFirst) {
                executePlayerTurn(player, cpu);
                if (!cpu.isFainted()) {
                    executeCpuTurn(cpu, player);
                }
            } else {
                System.out.println("⚡ Vild " + cpu.getName() + " var snabbare och attackerar först!");
                executeCpuTurn(cpu, player);
                if (!player.isFainted()) {
                    executePlayerTurn(player, cpu);
                }
            }
        }
    }

    private boolean determineTurnOrder(Pokemon player, Pokemon cpu) {
        if (player.getSpeed() > cpu.getSpeed()) return true;
        if (cpu.getSpeed() > player.getSpeed()) return false;
        return random.nextBoolean();
    }

    private void executePlayerTurn(Pokemon attacker, Pokemon defender) {
        System.out.println("\n--- DIN TUR (" + attacker.getName() + ") ---");
        List<Attack> attacks = attacker.getAttacks();

        for (int i = 0; i < attacks.size(); i++) {
            Attack a = attacks.get(i);
            System.out.printf("%d) %s%n", (i + 1), a.toString());
        }

        int choice = InputHelper.readInt(scanner, "Välj attack: ", 1, attacks.size()) - 1;
        Attack attack = attacks.get(choice);

        performAttack(attacker, defender, attack);
    }

    private void executeCpuTurn(Pokemon attacker, Pokemon defender) {
        List<Attack> attacks = attacker.getAttacks();
        // Välj bara statusattacker om motståndaren INTE redan har blivit påverkad
        List<Attack> validAttacks = attacks.stream()
                .filter(a -> !(a instanceof StatusAttack && defender.isStatLowered()))
                .toList();

        // Fallback om Pokémonen enbart har statusattacker
        if (validAttacks.isEmpty()) {
            validAttacks = attacks;
        }

        Attack attack = validAttacks.get(random.nextInt(validAttacks.size()));

        System.out.println("\n--- MOTSTÅNDARENS TUR ---");
        performAttack(attacker, defender, attack);
    }

    private void performAttack(Pokemon attacker, Pokemon defender, Attack attack) {
        System.out.println(attacker.getName() + " använder " + attack.getName() + "!");
        attack.execute(attacker, defender);
    }
}