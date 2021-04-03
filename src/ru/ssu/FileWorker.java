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
