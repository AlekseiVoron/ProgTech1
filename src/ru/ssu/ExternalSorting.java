package ru.ssu;

public class ExternalSorting {
    public static void externalSort() {
        FileWorker.makeTempDir();

        int tempFileNumber = Splitter.splitInputFile();
        Merger.mergeFiles(tempFileNumber);
    }
}
