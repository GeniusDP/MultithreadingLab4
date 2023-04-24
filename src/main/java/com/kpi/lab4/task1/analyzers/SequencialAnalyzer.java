package com.kpi.lab4.task1.analyzers;

import com.kpi.lab4.task1.utils.FileUtil;
import com.kpi.lab4.task1.dto.FileContent;
import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SequencialAnalyzer implements WordsLengthAnalyzer {

    public Map<Integer, Integer> wordLengthsMap = new ConcurrentHashMap<>();
    public Set<String> commonWords = new HashSet<>();

    @Override
    public FileContent analyze(File file) {
        if (file.isFile()) {
            return FileUtil.analyzeFileSequential(file, wordLengthsMap, commonWords);
        }

        FileContent result = null;
        for (File fsItem : file.listFiles()) {
            FileContent analyze = analyze(fsItem);
            result = FileContent.merge(result, analyze);
        }
        return result;
    }
}
