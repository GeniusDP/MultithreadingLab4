package com.kpi.lab4.task1.dto;

public record FileContent(long wordsLength, long wordsNumber) {

    public FileContent merge(FileContent fileContent) {
        return new FileContent(
            this.wordsLength + fileContent.wordsLength,
            this.wordsNumber + fileContent.wordsNumber
        );
    }

    public static FileContent merge(FileContent fileContent1, FileContent fileContent2) {
        if (fileContent1 == null) {
            return fileContent2;
        }
        if (fileContent2 == null) {
            return fileContent1;
        }
        return new FileContent(
            fileContent1.wordsLength + fileContent2.wordsLength,
            fileContent1.wordsNumber + fileContent2.wordsNumber
        );
    }

    @Override
    public String toString() {
        return "FileContent{" +
            "wordsLength=" + wordsLength +
            ", wordsNumber=" + wordsNumber +
            '}';
    }
}
