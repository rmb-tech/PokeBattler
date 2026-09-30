package se.rmbtech.pokebattler.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import se.rmbtech.pokebattler.exception.InvalidPokemonException;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Attack {
    private String name;
    private int baseDamage;
    private int accuracy;
    private PokemonType type;

    @JsonCreator
    protected Attack() {
    }// Krävs för Jackson (JSON)

    public Attack(String name, int baseDamage, int accuracy, PokemonType type) {
        setName(name);
        setBaseDamage(baseDamage);
        setAccuracy(accuracy);
        setType(type);
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidPokemonException("Attackens namn får inte vara tomt.");
        }
        this.name = name.trim();
    }

    public void setType(PokemonType type) {
        if (type == null) {
            throw new InvalidPokemonException("Attacktypen får inte vara tom.");
        }
        this.type = type;
    }
    public void setBaseDamage(int baseDamage) {
        if (baseDamage < 5 || baseDamage > 250) {
            throw new InvalidPokemonException("Basskada måste vara mellan 5 och 250.");
        }
        this.baseDamage = baseDamage;
    }

    public void setAccuracy(int accuracy) {
        if (accuracy < 0 || accuracy > 100) {
            throw new InvalidPokemonException("Träffsäkerhet måste vara mellan 0 och 100.");
        }
        this.accuracy = accuracy;
    }

    public String getName() {
        return name;
    }

    public int getBaseDamage() {
        return baseDamage;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public PokemonType getType() {
        return type;
    }

    @Override
    public String toString() {
        return String.format("%s (Skada: %d, Träff: %d%%, Typ: %s)",
                name, baseDamage, accuracy, type);
    }
}