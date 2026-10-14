package se.rmbtech.pokebattler.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import se.rmbtech.pokebattler.exception.InvalidPokemonException;

import java.util.function.IntUnaryOperator;

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
    public static final int MIN_ACCURACY = 0;
    public static final int MAX_ACCURACY = 100;

    @JsonCreator
    protected Attack() {}

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
        if (accuracy < MIN_ACCURACY || accuracy > MAX_ACCURACY) {
            throw new InvalidPokemonException("Träffsäkerhet måste vara mellan " + MIN_ACCURACY +" och " + MAX_ACCURACY + ".");
        }
        this.accuracy = accuracy;
    }

    public String getName() { return name; }
    public int getAccuracy() { return accuracy; }
    public PokemonType getType() { return type; }


    @Override
    public abstract void execute(Pokemon attacker, Pokemon defender);

    public boolean isUsefulAgainst(Pokemon defender) {
        return true;
    }

    public abstract String categoryLabel();
    public abstract String effectSummary();
    public abstract String powerLabel();
    public abstract int minPower();
    public abstract int maxPower();
    public abstract void updatePower(int newPower);
    public abstract Attack scaledCopy(IntUnaryOperator scaler);

    @Override
    public String toString() {
        return String.format("%s (%s, Träff: %d%%, Typ: %s)", name, effectSummary(), accuracy, type);
    }
}