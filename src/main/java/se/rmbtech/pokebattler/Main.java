package se.rmbtech.pokebattler;

import se.rmbtech.pokebattler.repository.StatsRepository;
import se.rmbtech.pokebattler.service.PokedexService;
import se.rmbtech.pokebattler.service.StatsService;
import se.rmbtech.pokebattler.ui.PokedexUI;

public class Main {
    public static void main(String[] args) {
        PokedexService service = new PokedexService();
        service.loadOrSeedData();

        StatsService statsService = new StatsService(new StatsRepository("battle-stats.json"));
        PokedexUI ui = new PokedexUI(service, statsService);
        ui.start();
    }
}