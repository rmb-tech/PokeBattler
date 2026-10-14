package se.rmbtech.pokebattler.service;

import se.rmbtech.pokebattler.model.Attack;
import se.rmbtech.pokebattler.model.BattleMode;
import se.rmbtech.pokebattler.model.BattleResult;
import se.rmbtech.pokebattler.model.BattleResult.DuelResult;
import se.rmbtech.pokebattler.model.Pokemon;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BattleEngine {
    private static final int MAX_ROUNDS = 30;

    private final AttackChooser playerChooser;
    private final AttackChooser cpuChooser;
    private final Random random;

    public BattleEngine(AttackChooser playerChooser, AttackChooser cpuChooser) {
        this(playerChooser, cpuChooser, new Random());
    }

    public BattleEngine(AttackChooser playerChooser, AttackChooser cpuChooser, Random random) {
        this.playerChooser = playerChooser;
        this.cpuChooser = cpuChooser;
        this.random = random;
    }

    public BattleResult startBattle(BattleMode mode, List<Pokemon> playerTeam, List<Pokemon> cpuTeam) {
        try {
            return runBattle(mode, playerTeam, cpuTeam);
        } finally {
            playerTeam.forEach(Pokemon::resetBattleStats);
            cpuTeam.forEach(Pokemon::resetBattleStats);
        }
    }

    private BattleResult runBattle(BattleMode mode, List<Pokemon> playerTeam, List<Pokemon> cpuTeam) {
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
            duels.add(new DuelResult(activePlayer.getId(), activePlayer.getName(),activePlayer.getPotential(), playerWonDuel));

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

        return new BattleResult(mode, playerWon, duels, attacksUsed);
    }

    /** Returnerar true om spelarens Pokémon vann duellen. */
    private boolean fightDuel(Pokemon player, Pokemon cpu, List<String> attacksUsed) {
        int round = 0;
        while (!player.isFainted() && !cpu.isFainted()) {
            if (round++ >= MAX_ROUNDS) {
                resolveStalemate(player, cpu);
                break;
            }

            if (determineTurnOrder(player, cpu)) {
                attacksUsed.add(takeTurn(player, cpu, playerChooser).getName());
                if (!cpu.isFainted()) {
                    takeTurn(cpu, player, cpuChooser);
                }
            } else {
                System.out.println("⚡ Vild " + cpu.getName() + " var snabbare och attackerar först!");
                takeTurn(cpu, player, cpuChooser);
                if (!player.isFainted()) {
                    attacksUsed.add(takeTurn(player, cpu, playerChooser).getName());
                }
            }
        }
        return cpu.isFainted();
    }

    /** En tur för vem som helst: valet delegeras till rätt AttackChooser. */
    private Attack takeTurn(Pokemon attacker, Pokemon defender, AttackChooser chooser) {
        Attack attack = chooser.chooseAttack(attacker, defender);
        System.out.println(attacker.getName() + " använder " + attack.getName() + "!");
        attack.execute(attacker, defender);
        return attack;
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
}