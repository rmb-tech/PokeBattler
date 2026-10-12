package se.rmbtech.pokebattler.exception;

public class InputCancelledException extends RuntimeException {
    public InputCancelledException() {
        super("Inmatningen avbröts");
    }
}
