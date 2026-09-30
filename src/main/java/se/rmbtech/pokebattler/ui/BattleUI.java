package se.rmbtech.pokebattler.ui;

import se.rmbtech.pokebattler.model.Pokemon;
import se.rmbtech.pokebattler.service.BattleEngine;
import se.rmbtech.pokebattler.service.PokedexService;
import se.rmbtech.pokebattler.util.InputHelper;

import java.util.List;
import java.util.Scanner;

public class BattleUI {
    private final Scanner scanner;
    private final PokedexService manager;

    public BattleUI(Scanner scanner, PokedexService manager) {
        this.scanner = scanner;
        this.manager = manager;
    }

    public void startBattle() {
        List<Pokemon> list = manager.getPokemons();
        if (list.size() < 2) {
            System.out.println("❌ Du behöver minst 2 Pokémon i ditt Pokédex för att starta en strid!");
            return;
        }

        System.out.println("\n============= ⚔️ VÄLJ KÄMPAR FÖR STRID ⚔️ =============");
        printPokemonChoices(list);

        int choice1 = InputHelper.readInt(scanner, "Välj Pokémon 1 (#): ", 1, list.size());
        int choice2 = InputHelper.readInt(scanner, "Välj Pokémon 2 (#): ", 1, list.size());

        if (choice1 == choice2) {
            System.out.println("❌ En Pokémon kan inte strida mot sig själv!");
            return;
        }

        Pokemon p1 = list.get(choice1 - 1);
        Pokemon p2 = list.get(choice2 - 1);

        p1.setCurrentHp(p1.getMaxHp());
        p2.setCurrentHp(p2.getMaxHp());

        BattleEngine engine = new BattleEngine(scanner);
        engine.startBattle(p1, p2);

        p1.setCurrentHp(p1.getMaxHp());
        p2.setCurrentHp(p2.getMaxHp());
    }

    private void printPokemonChoices(List<Pokemon> list) {
        for (int i = 0; i < list.size(); i++) {
            Pokemon p = list.get(i);
            System.out.printf("#%-2d %-12s │ HP: %d │ Försvar: %d │ Speed: %d%n",
                    (i + 1), p.getName(), p.getMaxHp(),p.getDefense(), p.getSpeed());
        }
    }
}