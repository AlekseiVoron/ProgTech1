package ru.ssu;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class FileGenerator {
    public static final int STR_NUM = 10000000;

    public static void makeRandomInputFile() {
        try {
            FileWriter fw = new FileWriter(Constants.FILE_INPUT);
            BufferedWriter bw = new BufferedWriter(fw);
            Random rng = new Random();
            for (int strNum = 0; strNum < STR_NUM; strNum++) {
                int len = 10 + rng.nextInt(100);
                StringBuilder text = new StringBuilder();
                for (int i = 0; i < len; i++) {
                    text.append(Constants.AVAILABLE_CHARS.charAt(rng.nextInt(Constants.AVAILABLE_CHARS.length())));
                }
                bw.write(String.valueOf(text));
                bw.newLine();
                bw.flush();
            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
