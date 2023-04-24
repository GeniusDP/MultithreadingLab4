package com.kpi.lab4.task1;

import com.kpi.lab4.task1.analyzers.ParallelAnalyzer;
import com.kpi.lab4.task1.analyzers.SequencialAnalyzer;
import com.kpi.lab4.task1.dto.FileContent;
import com.kpi.lab4.task1.utils.FileUtil;
import java.io.File;

public class Task1Main {

    private final static String ROOT_DIRECTORY = "src/main/resources/texts";

    public static void main(String[] args) {
        long currentTimeMillis = System.currentTimeMillis();

        var analyzer = new SequencialAnalyzer();
        FileContent fileContent = analyzer.analyze(new File(ROOT_DIRECTORY));
        System.out.println("fileContent = " + fileContent);

        double averageWordLength = FileUtil.getAverageWordLength(fileContent);
        System.out.println("averageWordLength = " + averageWordLength);

        System.out.println("time millis = " + (System.currentTimeMillis() - currentTimeMillis) );

        double standardDeviation = FileUtil.getStandardDeviation(fileContent, analyzer.wordLengthsMap);
        System.out.println("standardDeviation = " + standardDeviation);

        double dispersion = FileUtil.getDispersion(fileContent, analyzer.wordLengthsMap);
        System.out.println("dispersion = " + dispersion);

    }

}
