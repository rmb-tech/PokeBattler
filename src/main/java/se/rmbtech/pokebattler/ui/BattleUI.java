package se.rmbtech.pokebattler.ui;

import se.rmbtech.pokebattler.model.Pokemon;
import se.rmbtech.pokebattler.service.BattleEngine;
import se.rmbtech.pokebattler.service.PokedexService;
import se.rmbtech.pokebattler.service.PokemonDataSeeder;
import se.rmbtech.pokebattler.util.InputHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BattleUI {
    private final Scanner scanner;
    private final PokedexService manager;

    public BattleUI(Scanner scanner, PokedexService manager) {
        this.scanner = scanner;
        this.manager = manager;
    }

    public void startBattleMenu() {
        List<Pokemon> availablePokemons = manager.getPokemons();

        // Filtrera ut de som inte har svimmat (HP > 0)
        List<Pokemon> readyPokemons = availablePokemons.stream()
                .filter(p -> !p.isFainted())
                .toList();

        if (readyPokemons.isEmpty()) {
            System.out.println("❌ Alla dina Pokémon har svimmat! Du måste läka dem i Pokédex-menyn innan du kan strida.");
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
        engine.startTeamBattle(playerTeam, cpuTeam);
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