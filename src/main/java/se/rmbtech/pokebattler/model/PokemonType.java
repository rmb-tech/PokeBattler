package se.rmbtech.pokebattler.model;

public enum PokemonType {
    BUG,
    DARK,
    DRAGON,
    ELECTRIC,
    FAIRY,
    FIGHTING,
    FIRE,
    FLYING,
    GHOST,
    GRASS,
    GROUND,
    ICE,
    NORMAL,
    POISON,
    PSYCHIC,
    ROCK,
    STEEL,
    WATER;

    public double getMultiplierAgainst(PokemonType defenderType) {
        return switch (this) {
            case NORMAL -> switch (defenderType) {
                case ROCK, STEEL -> 0.5;
                case GHOST -> 0.0;
                default -> 1.0;
            };
            case FIGHTING -> switch (defenderType) {
                case NORMAL, ROCK, STEEL, ICE, DARK -> 2.0;
                case FLYING, POISON, BUG, PSYCHIC, FAIRY -> 0.5;
                case GHOST -> 0.0;
                default -> 1.0;
            };
            case FLYING -> switch (defenderType) {
                case FIGHTING, BUG, GRASS -> 2.0;
                case ROCK, STEEL, ELECTRIC -> 0.5;
                default -> 1.0;
            };
            case POISON -> switch (defenderType) {
                case GRASS, FAIRY -> 2.0;
                case POISON, GROUND, ROCK, GHOST -> 0.5;
                case STEEL -> 0.0;
                default -> 1.0;
            };
            case GROUND -> switch (defenderType) {
                case POISON, ROCK, STEEL, FIRE, ELECTRIC -> 2.0;
                case BUG, GRASS -> 0.5;
                case FLYING -> 0.0;
                default -> 1.0;
            };
            case ROCK -> switch (defenderType) {
                case FLYING, BUG, FIRE, ICE -> 2.0;
                case FIGHTING, GROUND, STEEL -> 0.5;
                default -> 1.0;
            };
            case BUG -> switch (defenderType) {
                case GRASS, PSYCHIC, DARK -> 2.0;
                case FIGHTING, FLYING, POISON, GHOST, STEEL, FIRE, FAIRY -> 0.5;
                default -> 1.0;
            };
            case GHOST -> switch (defenderType) {
                case GHOST, PSYCHIC -> 2.0;
                case DARK -> 0.5;
                case NORMAL -> 0.0;
                default -> 1.0;
            };
            case STEEL -> switch (defenderType) {
                case ROCK, ICE, FAIRY -> 2.0;
                case STEEL, FIRE, WATER, ELECTRIC -> 0.5;
                default -> 1.0;
            };
            case FIRE -> switch (defenderType) {
                case BUG, STEEL, GRASS, ICE -> 2.0;
                case ROCK, FIRE, WATER, DRAGON -> 0.5;
                default -> 1.0;
            };
            case WATER -> switch (defenderType) {
                case GROUND, ROCK, FIRE -> 2.0;
                case WATER, GRASS, DRAGON -> 0.5;
                default -> 1.0;
            };
            case GRASS -> switch (defenderType) {
                case GROUND, ROCK, WATER -> 2.0;
                case FLYING, POISON, BUG, STEEL, FIRE, GRASS, DRAGON -> 0.5;
                default -> 1.0;
            };
            case ELECTRIC -> switch (defenderType) {
                case FLYING, WATER -> 2.0;
                case GRASS, ELECTRIC, DRAGON -> 0.5;
                case GROUND -> 0.0;
                default -> 1.0;
            };
            case PSYCHIC -> switch (defenderType) {
                case FIGHTING, POISON -> 2.0;
                case STEEL, PSYCHIC -> 0.5;
                case DARK -> 0.0;
                default -> 1.0;
            };
            case ICE -> switch (defenderType) {
                case FLYING, GROUND, GRASS, DRAGON -> 2.0;
                case STEEL, FIRE, WATER, ICE -> 0.5;
                default -> 1.0;
            };
            case DRAGON -> switch (defenderType) {
                case DRAGON -> 2.0;
                case STEEL -> 0.5;
                case FAIRY -> 0.0;
                default -> 1.0;
            };
            case DARK -> switch (defenderType) {
                case GHOST, PSYCHIC -> 2.0;
                case FIGHTING, DARK, FAIRY -> 0.5;
                default -> 1.0;
            };
            case FAIRY -> switch (defenderType) {
                case FIGHTING, DRAGON, DARK -> 2.0;
                case POISON, STEEL, FIRE -> 0.5;
                default -> 1.0;
            };
        };
    }
}