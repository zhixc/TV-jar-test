package com.github.catvod.crawler;

import com.github.catvod.utils.FileUtil;

public class SpiderDebug {
    /*
        public static void log(Throwable th) {
        try {
            System.out.println("=====SpiderLog=====\n");
            System.out.println("th.getMessage:");
            System.out.print("      ");
            System.out.print(th.getMessage());
            System.out.println("th:");
            System.out.print("      ");
            System.out.print(th);
            System.out.println("\n\n=====SpiderLog=====");
        } catch (Throwable th1) {

        }
    }

    public static void log(String msg) {
        try {
            System.out.println("=====SpiderLog=====\n");
            System.out.println("msg:");
            System.out.print("      ");
            System.out.println(msg);
            System.out.println("\n\n=====SpiderLog=====");
        } catch (Throwable th1) {

        }
    }
     */
    public static void log(Throwable throwable) {
        System.out.println("=====SpiderLog start=====");
        FileUtil.writeExceptionToFile(throwable, "app/build/throwable.txt");
        System.out.println("\n=====SpiderLog end=====");
    }

    public static void log(String msg) {
        System.out.println("=====SpiderLog start=====");
        FileUtil.writeToFile(msg, "app/build/msg.json");
        System.out.println("\n=====SpiderLog end=====");
    }
}
