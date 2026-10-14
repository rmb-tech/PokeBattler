package se.rmbtech.pokebattler.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import se.rmbtech.pokebattler.exception.InvalidPokemonException;

import java.util.Random;
import java.util.function.IntUnaryOperator;

public class DamageAttack extends Attack {
    public static final int MIN_DAMAGE = 5;
    public static final int MAX_DAMAGE = 250;
    private static final int CRIT_CHANCE_PERCENT = 10;
    private static final double CRIT_MULTIPLIER = 1.5;
    private static final Random random = new Random();

    private int baseDamage;

    @JsonCreator
    protected DamageAttack() {
        super();
    }

    public DamageAttack(
            @JsonProperty("name") String name,
            @JsonProperty("baseDamage") int baseDamage,
            @JsonProperty("accuracy") int accuracy,
            @JsonProperty("type") PokemonType type) {
        super(name, accuracy, type);
        setBaseDamage(baseDamage);
    }

    public void setBaseDamage(int baseDamage) {
        if (baseDamage < MIN_DAMAGE || baseDamage > MAX_DAMAGE) {
            throw new InvalidPokemonException(
                    "Basskada måste vara mellan " + MIN_DAMAGE + " och " + MAX_DAMAGE + ".");
        }
        this.baseDamage = baseDamage;
    }

    public int getBaseDamage() {
        return baseDamage;
    }

    public int calculateDamage(Pokemon defender, double typeMultiplier, boolean isCritical) {
        int blockedDamage = defender.getDefense() / 2;
        double critMultiplier = isCritical ? CRIT_MULTIPLIER : 1.0;

        long rawDamage = Math.round((baseDamage - blockedDamage) * typeMultiplier * critMultiplier);
        return Math.max(1, (int) rawDamage);
    }

    @Override
    public void execute(Pokemon attacker, Pokemon defender) {
        if (random.nextInt(100) >= getAccuracy()) {
            System.out.println("💨 " + attacker.getName() + "s attack missade!");
            return;
        }

        double typeMultiplier = getType().getMultiplierAgainst(defender.getType());
        boolean isCritical = random.nextInt(100) < CRIT_CHANCE_PERCENT;

        int finalDamage = calculateDamage(defender, typeMultiplier, isCritical);
        defender.takeDamage(finalDamage);

        StringBuilder log = new StringBuilder(String.format("💥 Träff! %s tog %d skada! (HP: %d/%d)",
                defender.getName(), finalDamage, defender.getCurrentHp(), defender.getMaxHp()));

        if (isCritical) log.append(" 🎯 KRITISK TRÄFF!");
        if (typeMultiplier > 1.0) log.append(" 🔥 Super effektivt!");
        else if (typeMultiplier < 1.0) log.append(" 🛡️ Inte så effektivt...");

        System.out.println(log);
    }

    @Override
    public String categoryLabel() {
        return "Damage";
    }

    @Override
    public String effectSummary() {
        return "Skada: " + baseDamage;
    }

    @Override
    public String powerLabel() {
        return "basskada";
    }

    @Override
    public int minPower() {
        return MIN_DAMAGE;
    }

    @Override
    public int maxPower() {
        return MAX_DAMAGE;
    }

    @Override
    public void updatePower(int newPower) {
        setBaseDamage(newPower);
    }

    @Override
    public Attack scaledCopy(IntUnaryOperator scaler) {
        int scaled = Math.clamp(scaler.applyAsInt(baseDamage), MIN_DAMAGE, MAX_DAMAGE);
        return new DamageAttack(getName(), scaled, getAccuracy(), getType());
    }
}