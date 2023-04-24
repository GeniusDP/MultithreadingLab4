package com.kpi.lab4.task1.analyzers;

import com.kpi.lab4.task1.utils.FileUtil;
import com.kpi.lab4.task1.dto.FileContent;
import java.io.File;

public class SequencialAnalyzer implements WordsLengthAnalyzer {

    @Override
    public FileContent analyze(File file) {
        if (file.isFile()) {
            return FileUtil.analyzeFileSequential(file);
        }

        FileContent result = null;
        for (File fsItem : file.listFiles()) {
            FileContent analyze = analyze(fsItem);
            result = FileContent.merge(result, analyze);
        }
        return result;
    }
}
