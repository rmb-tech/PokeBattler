package se.rmbtech.pokebattler;

import se.rmbtech.pokebattler.repository.JsonPokemonRepository;
import se.rmbtech.pokebattler.repository.StatsRepository;
import se.rmbtech.pokebattler.service.PokedexService;
import se.rmbtech.pokebattler.service.StatsService;
import se.rmbtech.pokebattler.ui.PokedexUI;
import se.rmbtech.pokebattler.util.UIHelper;

public class Main {
    public static void main(String[] args) {
        PokedexService service = new PokedexService(new JsonPokemonRepository("pokebattler.json"));

        if (service.loadFromFile()) {
            UIHelper.simulateLoading("Laddar sparad data från 'pokebattler.json'", 1500);
            System.out.println("Data har laddats framgångsrikt!");
        } else {
            UIHelper.simulateLoading("Ingen användbar sparfil hittades. Genererar en starttrupp med 6 slumpade Pokémon", 2000);
            service.seedDefaultData();
            System.out.println("Starttrupp skapad och sparad!");
        }

        StatsService statsService = new StatsService(new StatsRepository("battle-stats.json"));
        PokedexUI ui = new PokedexUI(service, statsService);
        ui.start();
    }
}