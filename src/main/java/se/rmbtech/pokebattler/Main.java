package se.rmbtech.pokebattler;

import se.rmbtech.pokebattler.model.Pokemon;
import se.rmbtech.pokebattler.repository.FileManager;
import se.rmbtech.pokebattler.repository.StatsRepository;
import se.rmbtech.pokebattler.service.PokedexService;
import se.rmbtech.pokebattler.service.StatsService;
import se.rmbtech.pokebattler.ui.PokedexUI;
import se.rmbtech.pokebattler.util.UIHelper;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        PokedexService service = new PokedexService();

        List<Pokemon> loadedData = FileManager.loadPokemons();

        if (loadedData != null && !loadedData.isEmpty()) {
            UIHelper.simulateLoading("Laddar sparad data från 'pokebattler.json'", 1500);
            service .setPokemons(loadedData);
            System.out.println("Data har laddats framgångsrikt!");
        } else {
            UIHelper.simulateLoading("Ingen användbar sparfil hittades. Genererar en starttrupp med 6 slumpade Pokémon", 2000);
            service .seedDefaultData();
            FileManager.savePokemons(service .getPokemons());
            System.out.println("Starttrupp skapad och sparad!");
        }
        StatsService statsService = new StatsService(new StatsRepository("battle-stats.json"));
        PokedexUI ui = new PokedexUI(service, statsService);
        ui.start();
    }
}