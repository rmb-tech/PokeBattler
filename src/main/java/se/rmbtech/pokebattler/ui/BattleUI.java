package se.rmbtech.pokebattler.ui;

import se.rmbtech.pokebattler.model.BattleResult;
import se.rmbtech.pokebattler.model.Pokemon;
import se.rmbtech.pokebattler.service.*;
import se.rmbtech.pokebattler.util.InputHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BattleUI {
    private final Scanner scanner;
    private final PokedexService manager;
    private final StatsService statsService;

    public BattleUI(Scanner scanner, PokedexService manager, StatsService statsService) {
        this.scanner = scanner;
        this.manager = manager;
        this.statsService = statsService;
    }

    public void startBattleMenu() {
        List<Pokemon> allPokemons = manager.getPokemons();
        if (allPokemons.isEmpty()) {
            System.out.println("❌ Du äger inga Pokémon! Fånga eller lägg till en först.");
            return;
        }

        List<Pokemon> readyPokemons = allPokemons.stream()
                .filter(p -> !p.isFainted())
                .filter(p -> !p.getAttacks().isEmpty())
                .toList();

        if (readyPokemons.isEmpty()) {
            System.out.println("❌ Inga stridsdugliga Pokémon (svimmade eller utan attacker). Läk dem i Pokédex-menyn.");
            return;
        }

        System.out.println("\n=== ⚔️ STRIDSLÄGE ⚔️ ===");
        System.out.println("1) 1v1 Strid");
        System.out.println("2) 3v3 Lagstrid");
        System.out.println("3) RAID (5v1 mot Raid-boss)");
        int mode = InputHelper.readInt(scanner, "Välj typ av strid: ", 1, 3);

        int teamSize;
        if (mode == 1) {
            teamSize = 1;
        } else if (mode == 2) {
            teamSize = 3;
        } else {
            teamSize = 5;
        }

        if (readyPokemons.size() < teamSize) {
            System.out.println("❌ Du behöver minst " + teamSize + " frisk(a) Pokémon i ditt Pokédex för detta läge!");
            return;
        }

        List<Pokemon> playerTeam = selectPlayerTeam(readyPokemons, teamSize);

        List<Pokemon> cpuTeam;
        if (mode == 3) {
            Pokemon boss = RaidDataSeeder.getRandomRaidBoss();
            System.out.printf("\n🚨 EN EPISK RAID-BOSS DYKER UPP: %s (HP: %d)!\n", boss.getName(), boss.getMaxHp());
            cpuTeam = List.of(boss);
        } else {
            cpuTeam = new ArrayList<>();
            for (int i = 0; i < teamSize; i++) {
                cpuTeam.add(PokemonDataSeeder.getRandomSeedPokemon());
            }
        }

        BattleEngine engine = new BattleEngine(scanner);
        BattleResult result = engine.startTeamBattle(playerTeam, cpuTeam);
        statsService.recordBattle(result);
        System.out.println("📊 Resultatet har sparats i statistiken.");
    }

    private List<Pokemon> selectPlayerTeam(List<Pokemon> readyPokemons, int teamSize) {
        List<Pokemon> team = new ArrayList<>();
        System.out.println("\n--- Välj dina " + teamSize + " Pokémon ---");

        for (int i = 0; i < readyPokemons.size(); i++) {
            Pokemon p = readyPokemons.get(i);
            System.out.printf("#%-2d %-12s │ HP: %3d/%-3d │ Defense: %-3d │ Speed: %-3d%n",
                    (i + 1), p.getName(), p.getCurrentHp(), p.getMaxHp(), p.getDefense(), p.getSpeed());
        }

        for (int count = 1; count <= teamSize; count++) {
            while (true) {
                int choice = InputHelper.readInt(scanner, "Välj Pokémon #" + count + " (#): ", 1, readyPokemons.size());
                Pokemon selected = readyPokemons.get(choice - 1);

                if (team.contains(selected)) {
                    System.out.println("❌ Denna Pokémon är redan vald i ditt lag! Välj en annan.");
                } else {
                    team.add(selected);
                    break;
                }
            }
        }
        return team;
    }
}