package ru.ssu;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Splitter {
    public static Runtime runtime = Runtime.getRuntime();

    public static long freeMemory(Runtime r) {
        return r.maxMemory() - r.totalMemory() + r.freeMemory();
    }

    public static int splitInputFile() {
        try (FileReader fr = new FileReader(Constants.FILE_INPUT)) {
            int tempFileNumber = 0;
            BufferedReader reader = new BufferedReader(fr);
            String line;
            ArrayList<String> list = new ArrayList<>();
            while(true) {
                line = reader.readLine();
                if (line == null) {
                    break;
                }
                list.add(line);
                long fm = freeMemory(runtime);
                fm = fm / 1024 / 1024;
                if (freeMemory(runtime) < Constants.RECOMMENDED_FREE_MEMORY) {
                    Collections.sort(list);

                    FileWorker.writeDataToTempFile(list, tempFileNumber++);

                    list.clear();
                    runtime.gc();
                }
            }
            if (!list.isEmpty()) {
                Collections.sort(list);
                FileWorker.writeDataToTempFile(list, tempFileNumber++);
                list.clear();
                runtime.gc();
            }
            return tempFileNumber;

        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }

    }
}
