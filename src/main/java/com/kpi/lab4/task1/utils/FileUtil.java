package com.kpi.lab4.task1.utils;

import com.kpi.lab4.task1.dto.FileContent;
import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.RecursiveTask;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

public class FileUtil {

    @SneakyThrows
    public static FileContent analyzeFileSequential(File file) {
        List<String> list = Files.lines(file.toPath())
            .flatMap(line -> Arrays.stream(line.split(" ")))
            .toList();
        int size = list.size();
        int summaryWordsLength = String.join("", list).length();
        return new FileContent(summaryWordsLength, size);
    }

    @SneakyThrows
    public static FileContent analyzeFileParallel(File file) {
        List<String> list = Files.lines(file.toPath())
            .parallel()
            .flatMap(line -> Arrays.stream(line.split(" ")))
            .toList();
        var task = new WordCounterTask(list);
        return task.invoke();
    }

    @AllArgsConstructor
    static class WordCounterTask extends RecursiveTask<FileContent> {

        private List<String> list;

        @Override
        public FileContent compute() {
            if (list.size() < 100_000) {
                return new FileContent(String.join("", list).length(), list.size());
            }
            int n = list.size();
            List<String> listLeft = list.stream().limit(n / 2).toList();
            List<String> listRight = list.stream().skip(n / 2).toList();
            var taskLeft = new WordCounterTask(listLeft);
            var taskRight = new WordCounterTask(listRight);

            taskLeft.fork();
            taskRight.fork();

            FileContent fileContentRight = taskRight.join();
            FileContent fileContentLeft = taskLeft.join();

            return fileContentLeft.merge(fileContentRight);
        }
    }

    public static double getAverageWordLength(FileContent fileContent) {
        double average = ((double) fileContent.wordsLength()) / fileContent.wordsNumber();
        return Math.round(average * Math.pow(10, 3)) / Math.pow(10, 3);
    }

}
