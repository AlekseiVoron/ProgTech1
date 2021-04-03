package ru.ssu;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class Merger {

    public static void mergeFiles(int tempFileNumber) {
        ArrayList<LinkedList<String>> listOfBuffers = new ArrayList<>();
        for (int i = 0; i < tempFileNumber; i++) {
            listOfBuffers.add(new LinkedList<>());
        }

        try(FileWriter fw = new FileWriter(Constants.FILE_OUTPUT)) {
            BufferedWriter bw = new BufferedWriter(fw);
            ArrayList<FileReader> tempFileReaders = new ArrayList<>();
            ArrayList<BufferedReader> tempBufferReaders = new ArrayList<>();
            for (int i = 0; i < tempFileNumber; i++) {
                FileReader fr = new FileReader(Constants.TEMP_DIR_NAME + "/" + i + ".txt");
                tempFileReaders.add(fr);
                tempBufferReaders.add(new BufferedReader(fr));
            }
            int emptyFiles = 0;

            do {
                emptyFiles = 0;
                String minStr = null;
                int minStrNum = 0;
                for (int i = 0; i < tempFileNumber; i++) {
                    String t = null;
                    if (listOfBuffers.get(i).isEmpty()) {
                        for (int j = 0; j < Constants.STR_FROM_ONE_FILE; j++) {
                            t = tempBufferReaders.get(i).readLine();
                            if (t == null) {
                                break;
                            }
                            listOfBuffers.get(i).add(t);
                        }
                        if (t == null) {
                            emptyFiles++;
                            continue;
                        }
                    }
                    t = listOfBuffers.get(i).getFirst();
                    if (t != null && minStr == null) {
                        minStr = t;
                        minStrNum = i;
                    }
                    else if (t != null && t.compareTo(minStr) < 0) {
                        minStr = t;
                        minStrNum = i;
                    }
                }
                if (emptyFiles == tempFileNumber) {
                    break;
                }
                bw.write(listOfBuffers.get(minStrNum).pollFirst());
                bw.newLine();
                bw.flush();
            } while (true);
            for (FileReader fr : tempFileReaders) {
                fr.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
