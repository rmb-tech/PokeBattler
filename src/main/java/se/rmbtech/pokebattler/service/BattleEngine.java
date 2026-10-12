package se.rmbtech.pokebattler.service;

import se.rmbtech.pokebattler.model.Attack;
import se.rmbtech.pokebattler.model.BattleResult;
import se.rmbtech.pokebattler.model.BattleResult.DuelResult;
import se.rmbtech.pokebattler.model.Pokemon;
import se.rmbtech.pokebattler.model.StatusAttack;
import se.rmbtech.pokebattler.util.InputHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class BattleEngine {
    private static final int MAX_ROUNDS = 50;

    private final Scanner scanner;
    private final Random random = new Random();

    public BattleEngine(Scanner scanner) {
        this.scanner = scanner;
    }

    public BattleResult startTeamBattle(List<Pokemon> playerTeam, List<Pokemon> cpuTeam) {
        try {
            return runTeamBattle(playerTeam, cpuTeam);
        } finally {
            playerTeam.forEach(Pokemon::resetBattleStats);
            cpuTeam.forEach(Pokemon::resetBattleStats);
        }
    }

    private BattleResult runTeamBattle(List<Pokemon> playerTeam, List<Pokemon> cpuTeam) {
        List<DuelResult> duels = new ArrayList<>();
        List<String> attacksUsed = new ArrayList<>();
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

            boolean playerWonDuel = fightDuel(activePlayer, activeCpu, attacksUsed);
            duels.add(new DuelResult(activePlayer.getName(), playerWonDuel));

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

        boolean playerWon = playerIdx < playerTeam.size();

        System.out.println("\n==================================================");
        System.out.println(playerWon
                ? "🏆 DU VANN STRIDEN!"
                : "☠️ ALLA DINA POKÉMON HAR SVIMMAT. DU FÖRLORADE!");
        System.out.println("==================================================");

        return new BattleResult(playerWon, duels, attacksUsed);
    }

    /** Returnerar true om spelarens Pokémon vann duellen. */
    private boolean fightDuel(Pokemon player, Pokemon cpu, List<String> attacksUsed) {
        int round = 0;
        while (!player.isFainted() && !cpu.isFainted()) {
            if (round++ >= MAX_ROUNDS) {
                resolveStalemate(player, cpu);
                break;
            }

            boolean playerFirst = determineTurnOrder(player, cpu);

            if (playerFirst) {
                executePlayerTurn(player, cpu, attacksUsed);
                if (!cpu.isFainted()) {
                    executeCpuTurn(cpu, player);
                }
            } else {
                System.out.println("⚡ Vild " + cpu.getName() + " var snabbare och attackerar först!");
                executeCpuTurn(cpu, player);
                if (!player.isFainted()) {
                    executePlayerTurn(player, cpu, attacksUsed);
                }
            }
        }
        return cpu.isFainted();
    }

    private void resolveStalemate(Pokemon player, Pokemon cpu) {
        System.out.printf("%n⏱️ Dödläge efter %d rundor! Den med högst HP-procent vinner duellen.%n", MAX_ROUNDS);
        double playerRatio = (double) player.getCurrentHp() / player.getMaxHp();
        double cpuRatio = (double) cpu.getCurrentHp() / cpu.getMaxHp();
        if (playerRatio > cpuRatio) {
            cpu.takeDamage(cpu.getCurrentHp());
        } else {
            player.takeDamage(player.getCurrentHp());
        }
    }

    private boolean determineTurnOrder(Pokemon player, Pokemon cpu) {
        if (player.getSpeed() > cpu.getSpeed()) return true;
        if (cpu.getSpeed() > player.getSpeed()) return false;
        return random.nextBoolean();
    }

    private void executePlayerTurn(Pokemon attacker, Pokemon defender, List<String> attacksUsed) {
        System.out.println("\n--- DIN TUR (" + attacker.getName() + ") ---");
        List<Attack> attacks = attacker.getAttacks();

        for (int i = 0; i < attacks.size(); i++) {
            System.out.printf("%d) %s%n", (i + 1), attacks.get(i));
        }

        int choice = InputHelper.readInt(scanner, "Välj attack: ", 1, attacks.size()) - 1;
        Attack attack = attacks.get(choice);

        attacksUsed.add(attack.getName());
        performAttack(attacker, defender, attack);
    }

    private void executeCpuTurn(Pokemon attacker, Pokemon defender) {
        List<Attack> attacks = attacker.getAttacks();
        List<Attack> validAttacks = attacks.stream()
                .filter(a -> !(a instanceof StatusAttack && defender.isStatLowered()))
                .toList();

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