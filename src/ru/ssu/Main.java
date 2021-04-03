package ru.ssu;

import java.io.*;
import java.util.*;

public class Main {

    private static final String FILE_INPUT = "input.txt";
    private static final String FILE_OUTPUT = "output.txt";
    private static final String TEMP_DIR_NAME = "temp";
    private static final long RECOMMENDED_FREE_MEMORY = 16 * 1024 * 1024;   // 16 Mb
    private static final int STR_FROM_ONE_FILE = 10;
    private static final String AVAILABLE_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static void makeRandomInputFile() {
        try {
            FileWriter fw = new FileWriter(FILE_INPUT);
            BufferedWriter bw = new BufferedWriter(fw);
            Random rng = new Random();
            for (int strNum = 0; strNum < 10000000; strNum++) {
                int len = 10 + rng.nextInt(100);
                StringBuilder text = new StringBuilder();
                for (int i = 0; i < len; i++) {
                    text.append(AVAILABLE_CHARS.charAt(rng.nextInt(AVAILABLE_CHARS.length())));
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

    public static long freeMemory(Runtime r) {
        return r.maxMemory() - r.totalMemory() + r.freeMemory();
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

    private static void writeDataToTempFile (ArrayList<String> data, int tempFileNumber) throws IOException {
        File newTempFile = new File(TEMP_DIR_NAME + "/" + tempFileNumber + ".txt");
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

    public static void main(String[] args) {
        makeRandomInputFile();

        File tempDir = new File(TEMP_DIR_NAME);
        recursiveDelete(tempDir);
        tempDir.mkdir();
        Runtime runtime = Runtime.getRuntime();
        int tempFileNumber = 0;

        try (FileReader fr = new FileReader(FILE_INPUT)) {
            BufferedReader reader = new BufferedReader(fr);
            String line;
            ArrayList<String> list = new ArrayList<>();
            line = reader.readLine();
            while(true) {
                line = reader.readLine();
                if (line == null) {
                    break;
                }
                list.add(line);
                long fm = freeMemory(runtime);
                fm = fm / 1024 / 1024;
                if (freeMemory(runtime) < RECOMMENDED_FREE_MEMORY) {
                    Collections.sort(list);

                    writeDataToTempFile(list, tempFileNumber++);

                    list.clear();
                    runtime.gc();
                }
            }
            if (!list.isEmpty()) {
                Collections.sort(list);
                writeDataToTempFile(list, tempFileNumber++);
                list.clear();
                runtime.gc();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<LinkedList<String>> listOfBuffers = new ArrayList<>();
        for (int i = 0; i < tempFileNumber; i++) {
            listOfBuffers.add(new LinkedList<>());
        }

        try(FileWriter fw = new FileWriter(FILE_OUTPUT)) {
            BufferedWriter bw = new BufferedWriter(fw);
            ArrayList<String> buffer = new ArrayList<>();
            ArrayList<FileReader> tempFileReaders = new ArrayList<>();
            ArrayList<BufferedReader> tempBufferReaders = new ArrayList<>();
            for (int i = 0; i < tempFileNumber; i++) {
                FileReader fr = new FileReader(TEMP_DIR_NAME + "/" + i + ".txt");
                tempFileReaders.add(fr);
                tempBufferReaders.add(new BufferedReader(fr));
            }
            int emptyFiles = 0;
            do {
                emptyFiles = 0;
                for (int i = 0; i < tempFileNumber; i++) {
                    String t = null;
                    if (listOfBuffers.get(i).isEmpty()) {
                        for (int j = 0; j < STR_FROM_ONE_FILE; j++) {
                            t = tempBufferReaders.get(i).readLine();
                            if (t == null) {
                                break;
                            }
                            listOfBuffers.get(i).add(t);
                        }
                        if (t == null) {
                            emptyFiles++;
                        }
                    }
                    t = listOfBuffers.get(i).pollFirst();
                    if (t != null) {
                        buffer.add(t);
                    }
                }
                Collections.sort(buffer);
                for (String str : buffer) {
                    bw.write(str);
                    bw.newLine();
                }
                bw.flush();
                buffer.clear();
            } while (emptyFiles != tempFileNumber);
            for (FileReader fr : tempFileReaders) {
                fr.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
