package se.rmbtech.pokebattler.ui;

import se.rmbtech.pokebattler.model.BattleResult;
import se.rmbtech.pokebattler.model.Pokemon;
import se.rmbtech.pokebattler.service.BattleEngine;
import se.rmbtech.pokebattler.service.PokedexService;
import se.rmbtech.pokebattler.service.PokemonDataSeeder;
import se.rmbtech.pokebattler.service.StatsService;
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
        int mode = InputHelper.readInt(scanner, "Välj typ av strid: ", 1, 2);

        int teamSize = (mode == 1) ? 1 : 3;

        if (readyPokemons.size() < teamSize) {
            System.out.println("❌ Du behöver minst " + teamSize + " frisk(a) Pokémon i ditt Pokédex för detta läge!");
            return;
        }

        List<Pokemon> playerTeam = selectPlayerTeam(readyPokemons, teamSize);

        List<Pokemon> cpuTeam = new ArrayList<>();
        for (int i = 0; i < teamSize; i++) {
            cpuTeam.add(PokemonDataSeeder.getRandomSeedPokemon());
        }

        BattleEngine engine = new BattleEngine(scanner);
        BattleResult result = engine.startTeamBattle(playerTeam, cpuTeam);
        statsService.recordBattle(result);
        System.out.println("📊 Resultatet har sparats i statistiken.");
    }

    private List<Pokemon> selectPlayerTeam(List<Pokemon> readyPokemons, int teamSize) {
        List<Pokemon> team = new ArrayList<>();
        System.out.println("\n--- Välj dina " + teamSize + " fighters ---");

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