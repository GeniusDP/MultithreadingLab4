package com.kpi.lab4.task1.analyzers;

import com.kpi.lab4.task1.dto.FileContent;
import java.io.File;

public interface WordsLengthAnalyzer {

    FileContent analyze(File file);

}
