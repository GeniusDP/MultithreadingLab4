package com.kpi.lab4.task1.analyzers;

import com.kpi.lab4.task1.utils.FileUtil;
import com.kpi.lab4.task1.dto.FileContent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import lombok.AllArgsConstructor;

public class ParallelAnalyzer implements WordsLengthAnalyzer {

    @Override
    public FileContent analyze(File file) {
        return new AnalyzeWorker(file).invoke();
    }

    @AllArgsConstructor
    static class AnalyzeWorker extends RecursiveTask<FileContent> {

        private File file;

        @Override
        public FileContent compute() {
            if (file.isFile()) {
                return FileUtil.analyzeFileParallel(file);
            }

            List<ForkJoinTask<FileContent>> tasks = new ArrayList<>();
            for (var fsItem : file.listFiles()) {
                AnalyzeWorker worker = new AnalyzeWorker(fsItem);
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
