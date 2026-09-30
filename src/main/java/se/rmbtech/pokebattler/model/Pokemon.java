package se.rmbtech.pokebattler.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import se.rmbtech.pokebattler.exception.InvalidPokemonException;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Pokemon {
    private String name;
    private int maxHp;
    private int defense;

    @JsonProperty("type")
    private PokemonType type;

    @JsonProperty("currentHp")
    private int currentHp;
    private List<Attack> attacks;

    @JsonCreator
    protected Pokemon() {
        this.attacks = new ArrayList<>();
    } // Krävs för Jackson (JSON)

    public Pokemon(String name, PokemonType type, int maxHp, int defense, List<Attack> attacks) {
        setName(name);
        setType(type);
        setMaxHp(maxHp);
        setDefense(defense);
        this.currentHp = maxHp;
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
        this.currentHp = maxHp;
    }

    public void setDefense(int defense) {
        if (defense < 0 || defense > 100) {
            throw new InvalidPokemonException("Försvar (Defense) måste vara mellan 0 och 100.");
        }
        this.defense = defense;
    }

    public void setAttacks(List<Attack> attacks) {
        if (attacks == null || attacks.isEmpty() || attacks.size() > 4) {
            throw new InvalidPokemonException("En Pokémon måste ha mellan 1 och 4 attacker.");
        }
        this.attacks = new ArrayList<>(attacks);
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

    public List<Attack> getAttacks() {
        return new ArrayList<>(attacks);
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

    @Override
    public String toString() {
        return String.format("%s [%s] - HP: %d/%d | Defense: %d | Attacker: %d/4",
                name, type, currentHp, maxHp, defense, attacks.size());
    }
}