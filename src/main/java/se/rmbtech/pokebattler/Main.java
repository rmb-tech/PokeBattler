package se.rmbtech.pokebattler;

import se.rmbtech.pokebattler.repository.FileManager;
import se.rmbtech.pokebattler.service.PokedexService;
import se.rmbtech.pokebattler.model.Pokemon;
import se.rmbtech.pokebattler.ui.PokedexUI;
import se.rmbtech.pokebattler.util.UIHelper;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        PokedexService manager = new PokedexService();

        List<Pokemon> loadedData = FileManager.loadPokemons();

        if (loadedData != null && !loadedData.isEmpty()) {
            UIHelper.simulateLoading("Laddar sparad data från 'pokebattler.json'", 1500);
            manager.setPokemons(loadedData);
            System.out.println("Data har laddats framgångsrikt!");
        } else {
            UIHelper.simulateLoading("Ingen sparfil hittades. Genererar en startrooster med 6 slumpade Pokémon", 2000);
            manager.seedDefaultData();
            FileManager.savePokemons(manager.getPokemons());
            System.out.println("Startrooster skapad och sparad!");
        }
        PokedexUI ui = new PokedexUI(manager);
        ui.start();
    }
}