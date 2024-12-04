package com.github.catvod.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtil {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static void checkPath(String filePath) {
        File file = new File(filePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
    }

    public static void writeExceptionToFile(Throwable throwable, String filePath) {
        checkPath(filePath);
        try (FileWriter writer = new FileWriter(filePath)) {
            // 写入异常信息
            writer.write(throwable.getMessage());

            // 写入堆栈跟踪信息
            for (StackTraceElement element : throwable.getStackTrace()) {
                writer.write("\n" + element.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 追加写入
     *
     * @param throwable 异常信息
     * @param filePath  写入的文件路径
     */
    public static void appendExceptionToFile(Throwable throwable, String filePath) {
        checkPath(filePath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            // 写入当前时间
            writer.write(DATE_FORMAT.format(new Date()) + "\n");

            // 写入异常信息
            throwable.printStackTrace(new java.io.PrintWriter(writer));

            // 写入结束时间
            writer.write(DATE_FORMAT.format(new Date()) + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeToFile(String content, String filePath) {
        checkPath(filePath);
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 追加写入
     *
     * @param content  要写入的内容字符串
     * @param filePath 写入的文件路径
     */
    public static void appendToFile(String content, String filePath) {
        checkPath(filePath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            // 写入开始时间
            writer.write(DATE_FORMAT.format(new Date()));
            writer.newLine();

            // 写入内容
            writer.write(content);
            writer.newLine();

            // 写入结束时间
            writer.write(DATE_FORMAT.format(new Date()));
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeByteArrayToFile(byte[] byteArray, String filePath) {
        checkPath(filePath);
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(byteArray);
            System.out.println("Byte array has been written to the file successfully.");
        } catch (IOException e) {
            System.err.println("An error occurred while writing the byte array to the file: " + e.getMessage());
        }
    }
}
