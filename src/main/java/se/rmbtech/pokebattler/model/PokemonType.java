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

    // Datan är hämtade från PokemonGo's wiki sida.
    private static final double SUPER_EFFECTIVE = 1.6;
    private static final double NOT_VERY_EFFECTIVE = 0.625;
    private static final double DOUBLE_RESISTED = 0.390625;
    private static final double NEUTRAL = 1.0;

    public double getMultiplierAgainst(PokemonType defenderType) {
        return switch (this) {
            case NORMAL -> switch (defenderType) {
                case ROCK, STEEL -> NOT_VERY_EFFECTIVE;
                case GHOST -> DOUBLE_RESISTED;
                default -> NEUTRAL;
            };
            case FIGHTING -> switch (defenderType) {
                case NORMAL, ROCK, STEEL, ICE, DARK -> SUPER_EFFECTIVE;
                case FLYING, POISON, BUG, PSYCHIC, FAIRY -> NOT_VERY_EFFECTIVE;
                case GHOST -> DOUBLE_RESISTED;
                default -> NEUTRAL;
            };
            case FLYING -> switch (defenderType) {
                case FIGHTING, BUG, GRASS -> SUPER_EFFECTIVE;
                case ROCK, STEEL, ELECTRIC -> NOT_VERY_EFFECTIVE;
                default -> NEUTRAL;
            };
            case POISON -> switch (defenderType) {
                case GRASS, FAIRY -> SUPER_EFFECTIVE;
                case POISON, GROUND, ROCK, GHOST -> NOT_VERY_EFFECTIVE;
                case STEEL -> DOUBLE_RESISTED;
                default -> NEUTRAL;
            };
            case GROUND -> switch (defenderType) {
                case POISON, ROCK, STEEL, FIRE, ELECTRIC -> SUPER_EFFECTIVE;
                case BUG, GRASS -> NOT_VERY_EFFECTIVE;
                case FLYING -> DOUBLE_RESISTED;
                default -> NEUTRAL;
            };
            case ROCK -> switch (defenderType) {
                case FLYING, BUG, FIRE, ICE -> SUPER_EFFECTIVE;
                case FIGHTING, GROUND, STEEL -> NOT_VERY_EFFECTIVE;
                default -> NEUTRAL;
            };
            case BUG -> switch (defenderType) {
                case GRASS, PSYCHIC, DARK -> SUPER_EFFECTIVE;
                case FIGHTING, FLYING, POISON, GHOST, STEEL, FIRE, FAIRY -> NOT_VERY_EFFECTIVE;
                default -> NEUTRAL;
            };
            case GHOST -> switch (defenderType) {
                case GHOST, PSYCHIC -> SUPER_EFFECTIVE;
                case DARK -> NOT_VERY_EFFECTIVE;
                case NORMAL -> DOUBLE_RESISTED;
                default -> NEUTRAL;
            };
            case STEEL -> switch (defenderType) {
                case ROCK, ICE, FAIRY -> SUPER_EFFECTIVE;
                case STEEL, FIRE, WATER, ELECTRIC -> NOT_VERY_EFFECTIVE;
                default -> NEUTRAL;
            };
            case FIRE -> switch (defenderType) {
                case BUG, STEEL, GRASS, ICE -> SUPER_EFFECTIVE;
                case ROCK, FIRE, WATER, DRAGON -> NOT_VERY_EFFECTIVE;
                default -> NEUTRAL;
            };
            case WATER -> switch (defenderType) {
                case GROUND, ROCK, FIRE -> SUPER_EFFECTIVE;
                case WATER, GRASS, DRAGON -> NOT_VERY_EFFECTIVE;
                default -> NEUTRAL;
            };
            case GRASS -> switch (defenderType) {
                case GROUND, ROCK, WATER -> SUPER_EFFECTIVE;
                case FLYING, POISON, BUG, STEEL, FIRE, GRASS, DRAGON -> NOT_VERY_EFFECTIVE;
                default -> NEUTRAL;
            };
            case ELECTRIC -> switch (defenderType) {
                case FLYING, WATER -> SUPER_EFFECTIVE;
                case GRASS, ELECTRIC, DRAGON -> NOT_VERY_EFFECTIVE;
                case GROUND -> DOUBLE_RESISTED;
                default -> NEUTRAL;
            };
            case PSYCHIC -> switch (defenderType) {
                case FIGHTING, POISON -> SUPER_EFFECTIVE;
                case STEEL, PSYCHIC -> NOT_VERY_EFFECTIVE;
                case DARK -> DOUBLE_RESISTED;
                default -> NEUTRAL;
            };
            case ICE -> switch (defenderType) {
                case FLYING, GROUND, GRASS, DRAGON -> SUPER_EFFECTIVE;
                case STEEL, FIRE, WATER, ICE -> NOT_VERY_EFFECTIVE;
                default -> NEUTRAL;
            };
            case DRAGON -> switch (defenderType) {
                case DRAGON -> SUPER_EFFECTIVE;
                case STEEL -> NOT_VERY_EFFECTIVE;
                case FAIRY -> DOUBLE_RESISTED;
                default -> NEUTRAL;
            };
            case DARK -> switch (defenderType) {
                case GHOST, PSYCHIC -> SUPER_EFFECTIVE;
                case FIGHTING, DARK, FAIRY -> NOT_VERY_EFFECTIVE;
                default -> NEUTRAL;
            };
            case FAIRY -> switch (defenderType) {
                case FIGHTING, DRAGON, DARK -> SUPER_EFFECTIVE;
                case POISON, STEEL, FIRE -> NOT_VERY_EFFECTIVE;
                default -> NEUTRAL;
            };
        };
    }
}