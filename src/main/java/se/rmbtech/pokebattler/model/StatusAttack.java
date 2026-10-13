package se.rmbtech.pokebattler.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import se.rmbtech.pokebattler.exception.InvalidPokemonException;

import java.util.Random;
import java.util.function.IntUnaryOperator;

public class StatusAttack extends Attack {
    public static final int MIN_REDUCTION = 5;
    public static final int MAX_REDUCTION = 50;
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
        if (statReduction < MIN_REDUCTION || statReduction > MAX_REDUCTION) {
            throw new InvalidPokemonException(
                    "Stat-sänkning måste vara mellan " + MIN_REDUCTION + " och " + MAX_REDUCTION + ".");
        }
        this.statReduction = statReduction;
    }

    public int getStatReduction() {
        return statReduction;
    }

    @Override
    public boolean isUsefulAgainst(Pokemon defender) {
        return !defender.isStatLowered() && defender.getDefense() > 0;
    }

    @Override
    public void execute(Pokemon attacker, Pokemon defender) {
        if (random.nextInt(100) >= getAccuracy()) {
            System.out.println("💨 " + attacker.getName() + "s statusattack missade!");
            return;
        }
        if (!isUsefulAgainst(defender)) {
            System.out.printf("✨ %s använde %s, men %ss försvar kan inte sjunka lägre!%n",
                    attacker.getName(), getName(), defender.getName());
            return;
        }

        defender.lowerDefense(statReduction);

        System.out.printf("✨ %s sänkte %ss försvar till %d!%n",
                attacker.getName(), defender.getName(), defender.getDefense());
    }

    @Override public String categoryLabel() { return "Status"; }
    @Override public String effectSummary() { return "Sänker försvar: -" + statReduction; }
    @Override public String powerLabel() { return "försvarssänkning"; }
    @Override public int minPower() { return MIN_REDUCTION; }
    @Override public int maxPower() { return MAX_REDUCTION; }
    @Override public void updatePower(int newPower) { setStatReduction(newPower); }

    @Override
    public Attack scaledCopy(IntUnaryOperator scaler) {
        int scaled = Math.clamp(scaler.applyAsInt(statReduction), MIN_REDUCTION, MAX_REDUCTION);
        return new StatusAttack(getName(), getAccuracy(), getType(), scaled);
    }
}