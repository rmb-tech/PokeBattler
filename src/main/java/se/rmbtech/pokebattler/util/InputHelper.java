package se.rmbtech.pokebattler.util;

import se.rmbtech.pokebattler.exception.InputCancelledException;

import java.util.NoSuchElementException;
import java.util.Scanner;

public final class InputHelper {

    private InputHelper() {
    }

    private static String nextLine(Scanner scanner) {
        try {
            return scanner.nextLine().trim();
        } catch (NoSuchElementException | IllegalStateException e) {
            throw new InputCancelledException();
        }
    }

    public static int readInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = nextLine(scanner);

            if (input.isEmpty()) {
                System.out.println("❗Ogiltig input. Skriv ett heltal");
                continue;
            }
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("❗Ogiltig input. Skriv ett heltal");
            }
        }
    }

    public static int readInt(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            int value = readInt(scanner, prompt);
            if (value >= min && value <= max) {
                return value;
            }
            System.out.printf("❗Värdet måste vara mellan %d och %d.%n", min, max);
        }
    }

    public static int readIntInRange(Scanner scanner, String label, int min, int max) {
        return readInt(scanner, label + " (" + min + "-" + max + "): ", min, max);
    }

    public static String readString(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = nextLine(scanner);

            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("❗Input får inte vara tom. Försök igen.");
        }
    }
}
