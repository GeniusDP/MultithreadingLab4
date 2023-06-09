package com.kpi.lab4.task1.analyzers;

import com.kpi.lab4.task1.utils.FileUtil;
import com.kpi.lab4.task1.dto.FileContent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import lombok.AllArgsConstructor;

public class ParallelAnalyzer implements WordsLengthAnalyzer {

    public Map<Integer, Integer> wordLengthsMap = new ConcurrentHashMap<>();
    public Set<String> commonWords = new HashSet<>();

    @Override
    public FileContent analyze(File file) {
        return new AnalyzeWorker(file, wordLengthsMap, commonWords).invoke();
    }

    @AllArgsConstructor
    static class AnalyzeWorker extends RecursiveTask<FileContent> {

        private File file;
        private Map<Integer, Integer> wordLengths;
        private Set<String> commonWords;

        @Override
        public FileContent compute() {
            if (file.isFile()) {
                return FileUtil.analyzeFileParallel(file, wordLengths, commonWords);
            }

            List<ForkJoinTask<FileContent>> tasks = new ArrayList<>();
            for (var fsItem : file.listFiles()) {
                AnalyzeWorker worker = new AnalyzeWorker(fsItem, wordLengths, commonWords);
                tasks.add(worker.fork());
            }

            FileContent result = null;
            for (var task : tasks) {
                FileContent fileContent = task.join();
                result = FileContent.merge(result, fileContent);
            }
            return result;
        }
    }

}
