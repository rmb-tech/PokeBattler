package se.rmbtech.pokebattler.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import se.rmbtech.pokebattler.exception.InvalidPokemonException;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "attackType",
        defaultImpl = DamageAttack.class
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DamageAttack.class, name = "damage"),
        @JsonSubTypes.Type(value = StatusAttack.class, name = "status")
})
public abstract class Attack implements BattleAction {
    private String name;
    private int accuracy;
    private PokemonType type;

    @JsonCreator
    protected Attack(){}

    public Attack(String name, int accuracy, PokemonType type) {
        setName(name);
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

    public void setAccuracy(int accuracy) {
        if (accuracy < 0 || accuracy > 100) {
            throw new InvalidPokemonException("Träffsäkerhet måste vara mellan 0 och 100.");
        }
        this.accuracy = accuracy;
    }

    public String getName() {
        return name;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public PokemonType getType() {
        return type;
    }

    @Override
    public abstract void execute(Pokemon attacker, Pokemon defender);
}