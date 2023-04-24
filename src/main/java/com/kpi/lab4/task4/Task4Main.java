package com.kpi.lab4.task4;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Task4Main {

    private final static String ROOT_DIRECTORY = "src/main/resources/texts";

    public static void main(String[] args) {
        long currentTimeMillis = System.currentTimeMillis();

// non-exact match
//        Set<String> keywords = new HashSet<>(Arrays.asList(
//            "program", "code", "language", "intelligence",
//            "artificial", "virtual", "test", "automatic"
//        ));
        // exact match
        Set<String> keywords = new HashSet<>(Arrays.asList(
            "program", "code", "language"
        ));

        var searcher = new KeywordSearcher(keywords, true);
        List<String> filePaths = searcher.search(new File(ROOT_DIRECTORY));

        if (filePaths.isEmpty()) {
            System.out.println("No such files!");
            return;
        }

        filePaths.forEach(System.out::println);

        System.out.println("time millis = " + (System.currentTimeMillis() - currentTimeMillis));
    }

}
