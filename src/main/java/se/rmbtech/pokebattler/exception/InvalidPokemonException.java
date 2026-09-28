package se.rmbtech.pokebattler.exception;

public class InvalidPokemonException extends RuntimeException {
    public InvalidPokemonException(String message) {
        super(message);
    }
}