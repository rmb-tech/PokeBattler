package se.rmbtech.pokebattler.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import se.rmbtech.pokebattler.exception.InvalidPokemonException;

import java.util.Random;

public class StatusAttack extends Attack {
    private static final Random random = new Random();
    private int statReduction;

    @JsonCreator
    protected StatusAttack() {
        super();
    }

    public StatusAttack(
            @JsonProperty("name") String name,
            @JsonProperty("accuracy") int accuracy,
            @JsonProperty("type") PokemonType type,
            @JsonProperty("statReduction") int statReduction) {
        super(name, accuracy, type);
        setStatReduction(statReduction);
    }

    public void setStatReduction(int statReduction) {
        if (statReduction < 5 || statReduction > 50) {
            throw new InvalidPokemonException("Stat-sänkning måste vara mellan 5 och 50.");
        }
        this.statReduction = statReduction;
    }

    public int getStatReduction() {
        return statReduction;
    }

    @Override
    public void execute(Pokemon attacker, Pokemon defender) {
        if (random.nextInt(100) >= getAccuracy()) {
            System.out.println("💨 " + attacker.getName() + "s statusattack missade!");
            return;
        }
        if (defender.isStatLowered() || defender.getDefense() <= 0) {
            System.out.printf("✨ %s använde %s, men %s:s försvar kan inte sjunka lägre!%n", attacker.getName(), getName(), defender.getName());
            return;
        }

        int newDefense = Math.max(0, defender.getDefense() - statReduction);
        defender.setDefense(newDefense);
        defender.setStatLowered(true);

        System.out.printf("✨ %s sänkte %s försvarsvärde till %d!%n", attacker.getName(), defender.getName(), newDefense);
    }

    @Override
    public String toString() {
        return String.format("%s (Sänker försvar: -%d, Träff: %d%%, Typ: %s)",
                getName(), statReduction, getAccuracy(), getType());
    }
}