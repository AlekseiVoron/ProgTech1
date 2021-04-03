package ru.ssu;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class FileWorker {
    public static void writeDataToTempFile (ArrayList<String> data, int tempFileNumber) throws IOException {
        File newTempFile = new File(Constants.TEMP_DIR_NAME + "/" + tempFileNumber + ".txt");
        newTempFile.createNewFile();
        FileWriter fw = new FileWriter(newTempFile);
        BufferedWriter bw = new BufferedWriter(fw);
        data.forEach(x -> {
            try {
                bw.write(x);
                bw.newLine();
                bw.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        fw.close();
    }

    public static void makeRandomInputFile() {
        try {
            FileWriter fw = new FileWriter(Constants.FILE_INPUT);
            BufferedWriter bw = new BufferedWriter(fw);
            Random rng = new Random();
            for (int strNum = 0; strNum < 10000000; strNum++) {
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

    public static void recursiveDelete(File file) {
        if (!file.exists())
            return;

        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                recursiveDelete(f);
            }
        }
        file.delete();
    }

    public static void makeTempDir() {
        File tempDir = new File(Constants.TEMP_DIR_NAME);
        recursiveDelete(tempDir);
        tempDir.mkdir();
    }
}
