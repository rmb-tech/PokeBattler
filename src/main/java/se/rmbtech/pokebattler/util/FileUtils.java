package se.rmbtech.pokebattler.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class FileUtils {

    private FileUtils() {}

    public static void backupCorruptFile(File file) {
        File backup = new File(file.getPath() + ".bak");
        try {
            Files.move(file.toPath(), backup.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Den trasiga filen sparades som " + backup.getName());
        } catch (IOException e) {
            System.out.println("Kunde inte göra backup av den trasiga filen.");
        }
    }
}