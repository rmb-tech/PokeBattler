package se.rmbtech.pokebattler.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import se.rmbtech.pokebattler.exception.InvalidPokemonException;

import java.util.Random;

public class DamageAttack extends Attack {
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
        if (baseDamage < 5 || baseDamage > 250) {
            throw new InvalidPokemonException("Basskada måste vara mellan 5 och 250.");
        }
        this.baseDamage = baseDamage;
    }

    public int getBaseDamage() {
        return baseDamage;
    }

    @Override
    public void execute(Pokemon attacker, Pokemon defender) {
        if (random.nextInt(100) >= getAccuracy()) {
            System.out.println("💨 " + attacker.getName() + "s attack missade!");
            return;
        }

        boolean isCritical = random.nextInt(100) < 10;
        double critMultiplier = isCritical ? 1.5 : 1.0;
        double typeMultiplier = getType().getMultiplierAgainst(defender.getType());

        int blockedDamage = defender.getDefense() / 2;
        int rawDamage = (int) Math.round((baseDamage - blockedDamage) * typeMultiplier * critMultiplier);
        int finalDamage = Math.max(1, rawDamage);

        defender.takeDamage(finalDamage);

        StringBuilder log = new StringBuilder(String.format("💥 Träff! %s tog %d skada! (HP: %d/%d)",
                defender.getName(), finalDamage, defender.getCurrentHp(), defender.getMaxHp()));

        if (isCritical) log.append(" 🎯 KRITISK TRÄFF!");
        if (typeMultiplier > 1.0) log.append(" 🔥 Super effektivt!");
        else if (typeMultiplier < 1.0) log.append(" 🛡️ Inte så effektivt...");

        System.out.println(log);
    }

    @Override
    public String toString() {
        return String.format("%s (Skada: %d, Träff: %d%%, Typ: %s)",
                getName(), baseDamage, getAccuracy(), getType());
    }
}