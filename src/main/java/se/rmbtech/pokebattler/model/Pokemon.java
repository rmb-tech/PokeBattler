package se.rmbtech.pokebattler.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import se.rmbtech.pokebattler.exception.InvalidPokemonException;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"name", "type", "maxHp", "currentHp", "defense", "speed", "attacks"})

public class Pokemon {
    private String name;
    private PokemonType type;
    private int maxHp;
    private int currentHp;
    private int defense; // aktuellt försvar (kan sänkas i strid)
    private int baseDefense; // permanent försvar, återställs efter strid
    private int speed;
    private List<Attack> attacks;
    private boolean statLowered = false;

    @JsonCreator
    protected Pokemon() {
        this.attacks = new ArrayList<>();
    }

    public Pokemon(String name, PokemonType type, int maxHp, int defense, int speed, List<Attack> attacks) {
        setName(name);
        setType(type);
        setMaxHp(maxHp);
        this.currentHp = this.maxHp;
        setDefense(defense);
        setSpeed(speed);
        setAttacks(attacks);
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidPokemonException("Pokémon-namnet får inte vara tomt.");
        }
        this.name = name.trim();
    }

    public void setType(PokemonType type) {
        if (type == null) {
            throw new InvalidPokemonException("Pokémon-typen får inte vara null.");
        }
        this.type = type;
    }

    public void setMaxHp(int maxHp) {
        if (maxHp < 10 || maxHp > 1000) {
            throw new InvalidPokemonException("Max HP måste vara mellan 10 och 1000.");
        }
        this.maxHp = maxHp;
        this.currentHp = Math.min(this.currentHp, maxHp);
    }

    public void setCurrentHp(int currentHp) {
        this.currentHp = Math.clamp(currentHp, 0, this.maxHp);
    }

    public void setDefense(int defense) {
        if (defense < 0 || defense > 100) {
            throw new InvalidPokemonException("Försvar (Defense) måste vara mellan 0 och 100.");
        }
        this.defense = defense;
        this.baseDefense = defense;
    }

    public void lowerDefense(int amount) {
        this.defense = Math.max(0, this.defense - amount);
        this.statLowered = true;
    }

    public void resetBattleStats() {
        this.defense = this.baseDefense;
        this.statLowered = false;
    }

    public void setSpeed(int speed) {
        if (speed < 1 || speed > 300) {
            throw new InvalidPokemonException("Snabbhet (Speed) måste vara mellan 1 och 300.");
        }
        this.speed = speed;
    }

    public void setAttacks(List<Attack> attacks) {
        if (attacks == null || attacks.isEmpty() || attacks.size() > 4) {
            throw new InvalidPokemonException("En Pokémon måste ha mellan 1 och 4 attacker.");
        }
        this.attacks = new ArrayList<>(attacks);
    }

    public void addAttack(Attack attack) {
        if (attack == null) {
            throw new InvalidPokemonException("Attacken kan inte vara null.");
        }
        if (this.attacks.size() >= 4) {
            throw new InvalidPokemonException("En Pokémon kan inte ha fler än 4 attacker!");
        }
        this.attacks.add(attack);
    }

    public void takeDamage(int damage) {
        if (damage <= 0) return;
        this.currentHp = Math.max(0, this.currentHp - damage);
    }

    public void healFull() {
        this.currentHp = this.maxHp;
    }

    @JsonIgnore
    public boolean isFainted() {
        return this.currentHp <= 0;
    }

    @JsonIgnore
    public boolean isStatLowered() {
        return statLowered;
    }

    @JsonIgnore
    public int getBaseDefense() {
        return baseDefense;
    }

    @JsonIgnore
    public boolean isComplete() {
        return name != null && type != null && !attacks.isEmpty()
                && attacks.stream().allMatch(a -> a != null && a.getName() != null && a.getType() != null);
    }

    public String getName() {
        return name;
    }

    public PokemonType getType() {
        return type;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public int getDefense() {
        return defense;
    }

    public int getSpeed() {
        return speed;
    }

    public List<Attack> getAttacks() {
        return new ArrayList<>(attacks);
    }

    @Override
    public String toString() {
        return String.format("%s [%s] - HP: %d/%d | Försvar: %d | Snabbhet: %d | Attacker: %d/4",
                name, type, currentHp, maxHp, defense, speed, attacks.size());
    }
}