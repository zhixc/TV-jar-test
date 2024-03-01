package com.github.catvod.utils;

import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {

    public static void writeByteArrayToFile(byte[] byteArray, String filePath) {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(byteArray);
            System.out.println("Byte array has been written to the file successfully.");
        } catch (IOException e) {
            System.err.println("An error occurred while writing the byte array to the file: " + e.getMessage());
        }
    }
}
