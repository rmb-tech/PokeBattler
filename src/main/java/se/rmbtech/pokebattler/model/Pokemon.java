package se.rmbtech.pokebattler.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import se.rmbtech.pokebattler.exception.InvalidPokemonException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"name", "type", "maxHp", "currentHp", "defense", "speed", "potential", "attacks"})

public class Pokemon {
    public static final int MIN_HP = 10;
    public static final int MAX_HP = 1000;
    public static final int MIN_DEFENSE = 0;
    public static final int MAX_DEFENSE = 100;
    public static final int MIN_SPEED = 1;
    public static final int MAX_SPEED = 200;
    public static final int MIN_ATTACKS = 1;
    public static final int MAX_ATTACKS = 4;
    public static final int MIN_POTENTIAL = 0;
    public static final int MAX_POTENTIAL = 100;

    private String id = UUID.randomUUID().toString();
    private String name;
    private PokemonType type;
    private int maxHp;
    private int currentHp;
    private int defense; // aktuellt försvar (kan sänkas i strid)
    private int baseDefense; // permanent försvar, återställs efter strid
    private int speed;
    private int potential;
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

    public void setId(String id) {
        if (id != null && !id.trim().isEmpty()) {
            this.id = id;
        }
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
        if (maxHp < MIN_HP || maxHp > MAX_HP) {
            throw new InvalidPokemonException("Max HP måste vara mellan " + MIN_HP + " och " + MAX_HP + ".");
        }
        this.maxHp = maxHp;
        this.currentHp = Math.min(this.currentHp, maxHp);
    }

    public void setCurrentHp(int currentHp) {
        this.currentHp = Math.clamp(currentHp, 0, this.maxHp);
    }

    public void setDefense(int defense) {
        if (defense < MIN_DEFENSE || defense > MAX_DEFENSE) {
            throw new InvalidPokemonException("Försvar (Defense) måste vara mellan " + MIN_DEFENSE + " och " + MAX_DEFENSE + ".");
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
        if (speed < MIN_SPEED || speed > MAX_SPEED) {
            throw new InvalidPokemonException("Snabbhet (Speed) måste vara mellan " + MIN_SPEED + " och " + MAX_SPEED + ".");
        }
        this.speed = speed;
    }

    public void setAttacks(List<Attack> attacks) {
        if (attacks == null || attacks.size() < MIN_ATTACKS || attacks.size() > MAX_ATTACKS) {
            throw new InvalidPokemonException("En Pokémon måste ha mellan " + MIN_ATTACKS + " och " + MAX_ATTACKS + " attacker.");
        }
        this.attacks = new ArrayList<>(attacks);
    }

    public void addAttack(Attack attack) {
        if (attack == null) {
            throw new InvalidPokemonException("Attacken kan inte vara null.");
        }
        if (this.attacks.size() >= MAX_ATTACKS) {
            throw new InvalidPokemonException("En Pokémon kan inte ha fler än " + MAX_ATTACKS + " attacker!");
        }
        this.attacks.add(attack);
    }

    public void takeDamage(int damage) {
        if (damage <= 0) return;
        this.currentHp = Math.max(0, this.currentHp - damage);
    }

    public void setPotential(int potential) {
        this.potential = Math.clamp(potential, MIN_POTENTIAL, MAX_POTENTIAL);
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
    public boolean isComplete() {
        return name != null && !name.trim().isEmpty()
                && type != null
                && maxHp >= MIN_HP && maxHp <= MAX_HP
                && speed >= MIN_SPEED && speed <= MAX_SPEED
                && defense >= MIN_DEFENSE && defense <= MAX_DEFENSE
                && attacks != null && attacks.size() >= MIN_ATTACKS && attacks.size() <= MAX_ATTACKS;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPotential() {
        return potential;
    }

    @JsonIgnore
    public String getDisplayName() {
        return name + " (IV " + potential + "%)";
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
        return String.format("%s [%s] - HP: %d/%d | Försvar: %d | Snabbhet: %d | Attacker: %d/" + MAX_ATTACKS,
                name, type, currentHp, maxHp, defense, speed, attacks.size());
    }
}