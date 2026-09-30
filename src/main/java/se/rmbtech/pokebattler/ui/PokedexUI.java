package se.rmbtech.pokebattler.ui;

import se.rmbtech.pokebattler.exception.InvalidPokemonException;
import se.rmbtech.pokebattler.exception.PokemonNotFoundException;
import se.rmbtech.pokebattler.repository.FileManager;
import se.rmbtech.pokebattler.service.PokedexService;
import se.rmbtech.pokebattler.model.Attack;
import se.rmbtech.pokebattler.model.Pokemon;
import se.rmbtech.pokebattler.model.PokemonType;
import se.rmbtech.pokebattler.util.InputHelper;
import se.rmbtech.pokebattler.util.UIHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PokedexUI {
    private final Scanner scanner;
    private final PokedexService manager;

    public PokedexUI(PokedexService manager) {
        this.scanner = new Scanner(System.in);
        this.manager = manager;
    }

    public void start() {
        boolean running = true;
        UIHelper.printFirsStartHeader();
        UIHelper.printPikachuArt();

        while (running) {
            UIHelper.printMenu();
            int choice = InputHelper.readInt(scanner, "Val: ", 1, 8);

            try {
                switch (choice) {
                    case 1 -> listPokemons();
                    case 2 -> addPokemon();
                    case 3 -> editPokemon();
                    case 4 -> removePokemon();
                    case 5 -> searchPokemon();
                    case 6 -> {
                        manager.seedDefaultData();
                        FileManager.savePokemons(manager.getPokemons());
                        System.out.println("Pokédex återställt till 6 nya slumpade Pokémon!");
                    }
                    case 7 -> {
                        FileManager.savePokemons(manager.getPokemons());
                        System.out.println("Data sparad till fil.");
                    }
                    case 8 -> {
                        FileManager.savePokemons(manager.getPokemons());
                        System.out.println("Sparar och avslutar...");
                        running = false;
                    }
                }
            } catch (InvalidPokemonException | PokemonNotFoundException e) {
                System.out.println("Felmeddelande: " + e.getMessage());
            }
            System.out.println();
        }

        scanner.close();
    }

    private void listPokemons() {
        List<Pokemon> list = manager.getPokemons();
        if (list.isEmpty()) {
            System.out.println("Pokédexet är tomt.");
            return;
        }
        UIHelper.printHeader();
        for (int i = 0; i < list.size(); i++) {
            Pokemon p = list.get(i);
            System.out.printf("#%-2d %-12s │ Typ: %-10s │ HP: %3d/%-3d │ Defense: %-3d%n",
                    (i + 1), p.getName(), p.getType(), p.getCurrentHp(), p.getMaxHp(), p.getDefense());
            for (Attack a : p.getAttacks()) {
                System.out.printf("     └─ %-15s Skada: %-3d │ Träff: %3d%% │ Typ: %s%n",
                        a.getName(), a.getBaseDamage(), a.getAccuracy(), a.getType());
            }
            UIHelper.printDivider();
        }
    }

    private void addPokemon() {
        System.out.println("\n--- LÄGG TILL NY POKÉMON ---");
        String name = InputHelper.readString(scanner, "Namn: ");

        PokemonType type = selectType("Välj Pokémon-typ:");

        int maxHp = InputHelper.readInt(scanner, "Max HP (10-1000): ", 10, 1000);
        int defense = InputHelper.readInt(scanner, "Försvar (0-100): ", 0, 100);

        List<Attack> attacks = new ArrayList<>();
        int attackCount = InputHelper.readInt(scanner, "Antal attacker (1-4): ", 1, 4);

        for (int i = 1; i <= attackCount; i++) {
            System.out.println("\n--- Attack " + i + " ---");
            String aName = InputHelper.readString(scanner, "Attacknamn: ");
            int aDmg = InputHelper.readInt(scanner, "Basskada (5-250): ", 5, 250);
            int aAcc = InputHelper.readInt(scanner, "Träffsäkerhet (0-100): ", 0, 100);

            PokemonType aType = selectType("Välj typ för attacken:");
            attacks.add(new Attack(aName, aDmg, aAcc, aType));
        }

        Pokemon newPokemon = new Pokemon(name, type, maxHp, defense, attacks);
        manager.addPokemon(newPokemon);
        FileManager.savePokemons(manager.getPokemons());
        System.out.println("✅ Pokémon tillagd och sparad till JSON!");
    }

    private void editPokemon() {
        listPokemons();
        if (manager.getPokemons().isEmpty()) return;

        int userChoice = InputHelper.readInt(scanner, "Välj nummer (#) på Pokémon du vill redigera: ", 1, manager.getPokemons().size());

        int internalIndex = userChoice - 1;
        Pokemon p = manager.getPokemon(internalIndex);

        boolean editing = true;
        while (editing) {
            System.out.println("\n--- REDIGERAR " + p.getName().toUpperCase() + " ---");
            System.out.println("1) Ändra namn (" + p.getName() + ")");
            System.out.println("2) Ändra typ (" + p.getType() + ")");
            System.out.println("3) Ändra Max HP (" + p.getMaxHp() + ")");
            System.out.println("4) Ändra Försvar (" + p.getDefense() + ")");
            System.out.println("5) Hantera/Redigera attacker (" + p.getAttacks().size() + "/4 st)");
            System.out.println("6) Klar / Tillbaka till huvudmenyn");

            int subChoice = InputHelper.readInt(scanner, "Val: ", 1, 6);

            switch (subChoice) {
                case 1 -> {
                    String newName = InputHelper.readString(scanner, "Nytt namn: ");
                    p.setName(newName);
                    System.out.println("✅ Namn uppdaterat!");
                }
                case 2 -> {
                    PokemonType newType = selectType("Välj ny typ:");
                    p.setType(newType);
                    System.out.println("✅ Typ uppdaterad till " + p.getType() + "!");
                }
                case 3 -> {
                    int newHp = InputHelper.readInt(scanner, "Nytt Max HP (10-1000): ", 10, 1000);
                    p.setMaxHp(newHp);
                    System.out.println("✅ Max HP uppdaterat!");
                }
                case 4 -> {
                    int newDefense = InputHelper.readInt(scanner, "Nytt Försvar (0-100): ", 0, 100);
                    p.setDefense(newDefense);
                    System.out.println("✅ Försvar uppdaterat!");
                }
                case 5 -> editAttacksMenu(p);
                case 6 -> editing = false;
            }
            FileManager.savePokemons(manager.getPokemons());
        }
    }

    private void editAttacksMenu(Pokemon p) {
        System.out.println("\n--- HANTERA ATTACKER FÖR " + p.getName().toUpperCase() + " ---");
        List<Attack> attacks = p.getAttacks();

        for (int i = 0; i < attacks.size(); i++) {
            Attack a = attacks.get(i);
            System.out.printf("%d) %s (Skada: %d, Träffsäkerhet: %d%%, Typ: %s)%n",
                    (i + 1), a.getName(), a.getBaseDamage(), a.getAccuracy(), a.getType());
        }

        System.out.println("\nVad vill du göra?");
        System.out.println("1) Redigera en befintlig attack");
        System.out.println("2) Lägga till en ny attack");
        System.out.println("3) Ta bort en attack");
        System.out.println("4) Tillbaka");

        int choice = InputHelper.readInt(scanner, "Val: ", 1, 4);

        if (choice == 1) {
            int attackIndex = InputHelper.readInt(scanner, "Välj nummer på attacken du vill ändra: ", 1, attacks.size()) - 1;
            Attack targetAttack = attacks.get(attackIndex);

            System.out.println("Redigerar attack: " + targetAttack.getName());
            String newName = InputHelper.readString(scanner, "Nytt attacknamn: ");
            int newDmg = InputHelper.readInt(scanner, "Ny basskada (5-250): ", 5, 250);
            int newAcc = InputHelper.readInt(scanner, "Ny träffsäkerhet (0-100): ", 0, 100);

            PokemonType newType = selectType("Välj ny typ för attacken:");

            targetAttack.setName(newName);
            targetAttack.setBaseDamage(newDmg);
            targetAttack.setAccuracy(newAcc);
            targetAttack.setType(newType);

            p.setAttacks(attacks);
            System.out.println("✅ Attacken har uppdaterats!");

        } else if (choice == 2) {
            if (attacks.size() >= 4) {
                System.out.println("❌ Denna Pokémon har redan max antal attacker (4 st). Redigera eller ta bort en befintlig istället.");
                return;
            }

            System.out.println("--- LÄGG TILL NY ATTACK ---");
            String aName = InputHelper.readString(scanner, "Attacknamn: ");
            int aDmg = InputHelper.readInt(scanner, "Basskada (5-250): ", 5, 250);
            int aAcc = InputHelper.readInt(scanner, "Träffsäkerhet (0-100): ", 0, 100);
            PokemonType aType = selectType("Välj typ för den nya attacken:");
            try {
                p.addAttack(new Attack(aName, aDmg, aAcc, aType));
                System.out.println("✅ Ny attack tillagd!");
            } catch (InvalidPokemonException e) {
                System.out.println("❌ " + e.getMessage());
            }

        } else if (choice == 3) {
            if (attacks.size() <= 1) {
                System.out.println("❌ En Pokémon måste ha minst 1 attack! Det går inte att ta bort den sista.");
                return;
            }

            int attackIndex = InputHelper.readInt(scanner, "Välj nummer på attacken du vill ta bort: ", 1, attacks.size()) - 1;
            Attack removedAttack = attacks.remove(attackIndex);

            p.setAttacks(attacks);
            System.out.println("✅ Attacken '" + removedAttack.getName() + "' har tagits bort!");
        }
    }

    private void removePokemon() {
        listPokemons();
        if (manager.getPokemons().isEmpty()) return;

        int userChoice = InputHelper.readInt(scanner, "Välj nummer (#) på Pokémon du vill ta bort: ", 1, manager.getPokemons().size());
        int internalIndex = userChoice - 1;

        Pokemon removed = manager.getPokemon(internalIndex);
        manager.removePokemon(internalIndex);

        FileManager.savePokemons(manager.getPokemons());
        System.out.println("Tog bort: " + removed.getName());
    }

    private void searchPokemon() {
        String query = InputHelper.readString(scanner, "Sök efter namn: ").toLowerCase();
        boolean found = false;
        for (Pokemon p : manager.getPokemons()) {
            if (p.getName().toLowerCase().contains(query)) {
                System.out.printf("Hittad: %s (%s) - HP: %d%n", p.getName(), p.getType(), p.getMaxHp());
                found = true;
            }
        }
        if (!found) {
            System.out.println("❌ Ingen Pokémon hittades med det namnet.");
        }
    }

    private PokemonType selectType(String prompt) {
        System.out.println(prompt);
        PokemonType[] types = PokemonType.values();

        for (int i = 0; i < types.length; i++) {
            System.out.printf("%2d. %-10s ", (i + 1), types[i]);
            if ((i + 1) % 4 == 0) {
                System.out.println();
            }
        }
        System.out.println();

        int choice = InputHelper.readInt(scanner, "Val av typ: ", 1, types.length);
        return types[choice - 1];
    }
}