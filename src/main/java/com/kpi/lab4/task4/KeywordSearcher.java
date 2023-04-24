package com.kpi.lab4.task4;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@AllArgsConstructor
public class KeywordSearcher {

    private Set<String> keywords;
    private boolean exact;

    public List<String> search(File rootDir) {
        return new SearchWorker(rootDir, keywords, exact).invoke();
    }

    @AllArgsConstructor
    static class SearchWorker extends RecursiveTask<List<String>> {


        private File file;
        private Set<String> keywords;
        private boolean exact;

        @Override
        @SneakyThrows
        public List<String> compute() {
            if (file.isFile()) {
                List<String> list = Files.lines(file.toPath())
                    .flatMap(line -> Arrays.stream(line.split(" ")))
                    .toList();
                list = tokenize(list);

                Set<String> keywordsCopy = new HashSet<>(keywords);
                keywordsCopy.retainAll(list);
                if (exact) {
                    if (keywordsCopy.size() == keywords.size()) {
                        return List.of(file.getAbsolutePath());
                    }
                    return Collections.emptyList();
                } else {
                    if (!keywordsCopy.isEmpty()) {
                        return List.of(file.getAbsolutePath());
                    }
                    return Collections.emptyList();
                }
            }

            List<ForkJoinTask<List<String>>> tasks = new ArrayList<>();
            for (var fsItem : file.listFiles()) {
                var worker = new SearchWorker(fsItem, keywords, exact);
                tasks.add(worker.fork());
            }

            List<String> result = new ArrayList<>();
            for (var task : tasks) {
                result.addAll(task.join());
            }
            return result;
        }
    }

    private static List<String> tokenize(List<String> list) {
        return list.stream()
            .map(String::toLowerCase)
            .map(w -> {
                String result = w;
                Set<String> signs = Set.of(",", ".", "?", "!", ";", ":", "-");
                for (var s : signs) {
                    if (result.endsWith(s)) {
                        result = result.substring(0, result.length() - 1);
                    }
                }
                return result;
            })
            .toList();
    }

}
