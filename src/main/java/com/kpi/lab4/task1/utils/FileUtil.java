package com.kpi.lab4.task1.utils;

import com.kpi.lab4.task1.dto.FileContent;
import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

public class FileUtil {

    @SneakyThrows
    public static FileContent analyzeFileSequential(File file, Map<Integer, Integer> wordLengthsMap, Set<String> commonWords) {
        List<String> list = Files.lines(file.toPath())
            .flatMap(line -> Arrays.stream(line.split(" ")))
            .toList();
        processCommonWords(commonWords, list);
        synchronized (wordLengthsMap) {
            list.forEach(string -> {
                int length = string.length();
                int currentValue = wordLengthsMap.getOrDefault(length, 0);
                wordLengthsMap.put(length, currentValue + 1);
            });
        }

        int size = list.size();
        int summaryWordsLength = String.join("", list).length();
        return new FileContent(summaryWordsLength, size);
    }

    @SneakyThrows
    public static FileContent analyzeFileParallel(File file, Map<Integer, Integer> wordLengthsMap, Set<String> commonWords) {
        List<String> list = Files.lines(file.toPath())
            .flatMap(line -> Arrays.stream(line.split(" ")))
            .toList();
        processCommonWords(commonWords, list);
        var task = new WordCounterTask(list, wordLengthsMap);
        return task.invoke();
    }

    private static void processCommonWords(Set<String> commonWords, List<String> list) {
        synchronized (commonWords) {
            Set<String> tokenizedWords = list.stream()
                .map(String::toLowerCase)
                .map(w -> {
                    String result = w;
                    Set<String> signs = Set.of(",", ".", "?", "!", ";", ":", "-");
                    for(var s: signs) {
                        if(result.endsWith(s)) {
                            result = result.substring(0, result.length() - 1);
                        }
                    }
                    return result;
                })
                .collect(Collectors.toSet());

            if (commonWords.isEmpty()) {
                commonWords.addAll(tokenizedWords);
            } else {
                commonWords.retainAll(tokenizedWords);
            }
        }
    }

    @RequiredArgsConstructor
    static class WordCounterTask extends RecursiveTask<FileContent> {

        private final List<String> list;
        private final Map<Integer, Integer> wordLengthsMap;

        @Override
        public FileContent compute() {
            if (list.size() < 100_000) {
                synchronized (wordLengthsMap) {
                    list.forEach(string -> {
                        int length = string.length();
                        int currentValue = wordLengthsMap.getOrDefault(length, 0);
                        wordLengthsMap.put(length, currentValue + 1);
                    });
                }
                return new FileContent(String.join("", list).length(), list.size());
            }
            int n = list.size();
            List<String> listLeft = list.stream().limit(n / 2).toList();
            List<String> listRight = list.stream().skip(n / 2).toList();
            var taskLeft = new WordCounterTask(listLeft, wordLengthsMap);
            var taskRight = new WordCounterTask(listRight, wordLengthsMap);

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

    public static double getDispersion(FileContent fileContent, Map<Integer, Integer> wordLengths) {
        double average = getAverageWordLength(fileContent);
        double sumOfSquaredDeviations = 0;
        for (Map.Entry<Integer, Integer> entry : wordLengths.entrySet()) {
            sumOfSquaredDeviations += Math.pow(entry.getKey() - average, 2) * entry.getValue();
        }
        return sumOfSquaredDeviations / fileContent.wordsNumber();
    }

    public static double getStandardDeviation(FileContent fileContent, Map<Integer, Integer> wordLengths) {
        double dispersion = getDispersion(fileContent, wordLengths);
        return Math.sqrt(dispersion);
    }

}
