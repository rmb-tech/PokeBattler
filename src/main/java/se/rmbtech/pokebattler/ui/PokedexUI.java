package se.rmbtech.pokebattler.ui;

import se.rmbtech.pokebattler.exception.InputCancelledException;
import se.rmbtech.pokebattler.exception.InvalidPokemonException;
import se.rmbtech.pokebattler.exception.PokemonNotFoundException;
import se.rmbtech.pokebattler.model.Attack;
import se.rmbtech.pokebattler.model.DamageAttack;
import se.rmbtech.pokebattler.model.Pokemon;
import se.rmbtech.pokebattler.model.PokemonType;
import se.rmbtech.pokebattler.model.StatusAttack;
import se.rmbtech.pokebattler.service.PokedexService;
import se.rmbtech.pokebattler.service.PokemonDataSeeder;
import se.rmbtech.pokebattler.service.StatsService;
import se.rmbtech.pokebattler.util.InputHelper;
import se.rmbtech.pokebattler.util.UIHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PokedexUI {
    private final Scanner scanner;
    private final PokedexService service;
    private final BattleUI battleUI;
    private final StatsUI statsUI;

    public PokedexUI(PokedexService service, StatsService statsService) {
        this.scanner = new Scanner(System.in);
        this.service = service;
        this.battleUI = new BattleUI(this.scanner, service, statsService);
        this.statsUI = new StatsUI(statsService);
    }

    public void start() {
        UIHelper.printWelcomeHeader();
        UIHelper.printPikachuArt();
        try {
            runMainMenu();
        } catch (InputCancelledException e) {
            System.out.println("\nInmatningen avbröts. Sparar och avslutar...");
        } finally {
            service.save();
            scanner.close();
        }
    }

    private void runMainMenu() {
        boolean running = true;
        while (running) {
            UIHelper.printMainMenu();
            int choice = InputHelper.readInt(scanner, "Val: ", 1, 7);

            try {
                switch (choice) {
                    case 1 -> pokedexSubMenu();
                    case 2 -> catchRandomPokemon();
                    case 3 -> battleUI.startBattleMenu();
                    case 4 -> statsUI.showStats();
                    case 5 -> resetToDefaultData();
                    case 6 -> saveToFile();
                    case 7 -> running = false;
                }
            } catch (InputCancelledException e) {
                throw e;
            } catch (InvalidPokemonException | PokemonNotFoundException e) {
                System.out.println("Felmeddelande: " + e.getMessage());
            } catch (RuntimeException e) {
                System.out.println("❗ Ett oväntat fel inträffade (" + e.getClass().getSimpleName() + "): " + e.getMessage());
            }
            System.out.println();
        }
        System.out.println("Sparar och avslutar...");
    }

    private void pokedexSubMenu() {
        boolean inSubMenu = true;
        while (inSubMenu) {
            UIHelper.printPokedexSubMenu();
            int subChoice = InputHelper.readInt(scanner, "Val: ", 1, 7);

            switch (subChoice) {
                case 1 -> listPokemons();
                case 2 -> addPokemon();
                case 3 -> editPokemon();
                case 4 -> removePokemon();
                case 5 -> searchPokemon();
                case 6 -> healAllPokemons();
                case 7 -> inSubMenu = false;
            }
        }
    }

    private void catchRandomPokemon() {
        Pokemon newPokemon = PokemonDataSeeder.getRandomSeedPokemon();
        service.addPokemon(newPokemon);
        System.out.println("\n🎉 GRATTIS! Du fångade en vild " + newPokemon.getDisplayName() + " (" + newPokemon.getType() + ")!");
    }

    private void healAllPokemons() {
        service.healAll();
        System.out.println("\n💖 Alla dina Pokémon har blivit fullständigt läkta!");
    }

    private void resetToDefaultData() {
        service.seedDefaultData();
        System.out.println("✅ Pokédex återställt till seedat läge!");
    }

    private void saveToFile() {
        service.save();
        System.out.println("✅ Data sparad till fil.");
    }

    private void listPokemons() {
        List<Pokemon> list = service.getPokemons();
        if (list.isEmpty()) {
            System.out.println("Pokédexet är tomt.");
            return;
        }
        UIHelper.printHeader();
        for (int i = 0; i < list.size(); i++) {
            Pokemon p = list.get(i);
            System.out.printf("#%-2d %-22s │ Typ: %-10s │ HP: %3d/%-3d │ Försvar: %-3d │ Snabbhet: %-3d%n",
                    (i + 1), p.getDisplayName(), p.getType(), p.getCurrentHp(), p.getMaxHp(), p.getDefense(), p.getSpeed());
            for (Attack a : p.getAttacks()) {
                System.out.printf("     └─ %-15s [%-6s] %-22s │ Träff: %3d%% │ Typ: %s%n",
                        a.getName(), a.categoryLabel(), a.effectSummary(), a.getAccuracy(), a.getType());
            }
            UIHelper.printDivider();
        }
    }

    private void addPokemon() {
        System.out.println("\n--- LÄGG TILL NY POKÉMON ---");
        String name = InputHelper.readString(scanner, "Namn: ");

        PokemonType type = selectType("Välj Pokémon-typ:");

        int maxHp = InputHelper.readIntInRange(scanner, "Max HP", Pokemon.MIN_HP, Pokemon.MAX_HP);
        int defense = InputHelper.readIntInRange(scanner, "Försvar", Pokemon.MIN_DEFENSE, Pokemon.MAX_DEFENSE);
        int speed = InputHelper.readIntInRange(scanner, "Snabbhet", Pokemon.MIN_SPEED, Pokemon.MAX_SPEED);

        List<Attack> attacks = new ArrayList<>();
        int attackCount = InputHelper.readIntInRange(scanner, "Antal attacker", Pokemon.MIN_ATTACKS, Pokemon.MAX_ATTACKS);

        for (int i = 1; i <= attackCount; i++) {
            System.out.println("\n--- Attack " + i + " ---");
            attacks.add(readNewAttack("Välj typ för attacken:"));
        }

        Pokemon newPokemon = new Pokemon(name, type, maxHp, defense, speed, attacks);
        service.addPokemon(newPokemon);
        System.out.println("✅ Pokémon tillagd och sparad till JSON!");
    }

    private void editPokemon() {
        listPokemons();
        if (service.getPokemons().isEmpty()) return;

        int userChoice = InputHelper.readInt(scanner, "Välj nummer (#) på Pokémon du vill redigera: ", 1, service.getPokemons().size());

        int internalIndex = userChoice - 1;
        Pokemon p = service.getPokemon(internalIndex);

        boolean editing = true;
        while (editing) {
            System.out.println("\n--- REDIGERAR " + p.getName().toUpperCase() + " ---");
            System.out.println("1) Ändra namn (" + p.getName() + ")");
            System.out.println("2) Ändra typ (" + p.getType() + ")");
            System.out.println("3) Ändra Max HP (" + p.getMaxHp() + ")");
            System.out.println("4) Ändra Försvar (" + p.getDefense() + ")");
            System.out.println("5) Ändra Snabbhet (" + p.getSpeed() + ")");
            System.out.println("6) Hantera/Redigera attacker (" + p.getAttacks().size() + "/" + Pokemon.MAX_ATTACKS + " st)");
            System.out.println("7) Klar / Tillbaka till huvudmenyn");

            int subChoice = InputHelper.readInt(scanner, "Val: ", 1, 7);

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
                    int newHp = InputHelper.readIntInRange(scanner, "Nytt Max HP", Pokemon.MIN_HP, Pokemon.MAX_HP);
                    p.setMaxHp(newHp);
                    System.out.println("✅ Max HP uppdaterat!");
                }
                case 4 -> {
                    int newDefense = InputHelper.readIntInRange(scanner, "Nytt Försvar", Pokemon.MIN_DEFENSE, Pokemon.MAX_DEFENSE);
                    p.setDefense(newDefense);
                    System.out.println("✅ Försvar uppdaterat!");
                }
                case 5 -> {
                    int newSpeed = InputHelper.readIntInRange(scanner, "Ny Snabbhet", Pokemon.MIN_SPEED, Pokemon.MAX_SPEED);
                    p.setSpeed(newSpeed);
                    System.out.println("✅ Snabbhet uppdaterat!");
                }
                case 6 -> editAttacksMenu(p);
                case 7 -> editing = false;
            }
            service.save();
        }
    }

    private void editAttacksMenu(Pokemon p) {
        System.out.println("\n--- HANTERA ATTACKER FÖR " + p.getName().toUpperCase() + " ---");
        List<Attack> attacks = p.getAttacks();

        for (int i = 0; i < attacks.size(); i++) {
            System.out.printf("%d) %s%n", (i + 1), attacks.get(i));
        }

        System.out.println("\nVad vill du göra?");
        System.out.println("1) Redigera en befintlig attack");
        System.out.println("2) Lägga till en ny attack");
        System.out.println("3) Ta bort en attack");
        System.out.println("4) Tillbaka");

        int choice = InputHelper.readInt(scanner, "Val: ", 1, 4);

        if (choice == 1) {
            int attackIndex = InputHelper.readInt(scanner, "Välj nummer på attacken du vill ändra: ", 1, attacks.size()) - 1;
            Attack target = attacks.get(attackIndex);

            System.out.println("Redigerar attack: " + target.getName());
            String newName = InputHelper.readString(scanner, "Nytt attacknamn: ");
            int newAcc = InputHelper.readIntInRange(scanner, "Ny träffsäkerhet", Attack.MIN_ACCURACY, Attack.MAX_ACCURACY);
            PokemonType newType = selectType("Välj ny typ för attacken:");
            int newPower = InputHelper.readIntInRange(scanner, "Ny " + target.powerLabel(), target.minPower(), target.maxPower());

            target.setName(newName);
            target.setAccuracy(newAcc);
            target.setType(newType);
            target.updatePower(newPower);

            p.setAttacks(attacks);
            System.out.println("✅ Attacken har uppdaterats!");

        } else if (choice == 2) {
            if (attacks.size() >= Pokemon.MAX_ATTACKS) {
                System.out.println("❌ Denna Pokémon har redan max antal attacker (" + Pokemon.MAX_ATTACKS + " st). Redigera eller ta bort en befintlig istället.");
                return;
            }
            System.out.println("--- LÄGG TILL NY ATTACK ---");
            Attack newAttack = readNewAttack("Välj typ för den nya attacken:");
            try {
                p.addAttack(newAttack);
                System.out.println("✅ Ny attack tillagd!");
            } catch (InvalidPokemonException e) {
                System.out.println("❌ " + e.getMessage());
            }

        } else if (choice == 3) {
            if (attacks.size() <= Pokemon.MIN_ATTACKS) {
                System.out.println("❌ En Pokémon måste ha minst 1 attack! Det går inte att ta bort den sista.");
                return;
            }
            int attackIndex = InputHelper.readInt(scanner, "Välj nummer på attacken du vill ta bort: ", 1, attacks.size()) - 1;
            Attack removed = attacks.remove(attackIndex);
            p.setAttacks(attacks);
            System.out.println("✅ Attacken '" + removed.getName() + "' har tagits bort!");
        }
    }

    private Attack readNewAttack(String typePrompt) {
        System.out.println("Välj attacktyp: 1) Skadeattack  2) Statusattack");
        int kind = InputHelper.readInt(scanner, "Val: ", 1, 2);

        String name = InputHelper.readString(scanner, "Attacknamn: ");
        int accuracy = InputHelper.readIntInRange(scanner, "Träffsäkerhet", Attack.MIN_ACCURACY, Attack.MAX_ACCURACY);
        PokemonType type = selectType(typePrompt);

        if (kind == 1) {
            int damage = InputHelper.readIntInRange(scanner, "Basskada", DamageAttack.MIN_DAMAGE, DamageAttack.MAX_DAMAGE);
            return new DamageAttack(name, damage, accuracy, type);
        }
        int reduction = InputHelper.readIntInRange(scanner, "Försvarssänkning", StatusAttack.MIN_REDUCTION, StatusAttack.MAX_REDUCTION);
        return new StatusAttack(name, accuracy, type, reduction);
    }

    private void removePokemon() {
        listPokemons();
        if (service.getPokemons().isEmpty()) return;

        int userChoice = InputHelper.readInt(scanner, "Välj nummer (#) på Pokémon du vill ta bort: ", 1, service.getPokemons().size());
        int internalIndex = userChoice - 1;

        Pokemon removed = service.getPokemon(internalIndex);
        service.removePokemon(internalIndex);

        System.out.println("Tog bort: " + removed.getName());
    }

    private void searchPokemon() {
        String query = InputHelper.readString(scanner, "Sök efter namn: ").toLowerCase();
        boolean found = false;
        for (Pokemon p : service.getPokemons()) {
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