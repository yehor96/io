package com.luxcampus.fileanalyzer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileAnalyzer {

    static final String INCORRECT_ARGUMENT_MESSAGE =
            "Make sure to pass two arguments - file path (not a directory) and a keyword";

    public static void main(String[] args) throws IOException {
        Result result = search(args);
        print(args[1], result);
    }

    static Result search(String[] args) throws IOException {
        if (args.length != 2) {
            throw new IllegalArgumentException(INCORRECT_ARGUMENT_MESSAGE);
        }
        File file = new File(args[0]);
        if (!file.isFile()) {
            throw new IllegalArgumentException(INCORRECT_ARGUMENT_MESSAGE);
        }
        String word = args[1];
        return find(word, file);
    }

    private static Result find(String word, File file) throws IOException {
        String content = getContent(file);
        List<String> allSentences = breakIntoSentences(content);
        List<String> searchedSentences = getSearchedSentences(allSentences, word);
        int wordCount = getWordOccurrences(searchedSentences, word);
        return new Result(wordCount, searchedSentences);
    }

    static List<String> breakIntoSentences(String content) {
        List<String> sentences = Arrays.asList(content.split("((?<=[.?!]))"));
        for (int i = 0; i < sentences.size(); i++) {
            var sentence = sentences.get(i);
            sentences.set(i, sentence.trim());
        }
        return sentences;
    }

    static List<String> getSearchedSentences(List<String> sentences, String word) {
        List<String> searchedSentences = new ArrayList<>();
        for (String sentence : sentences) {
            if (sentence.contains(word)) {
                searchedSentences.add(sentence);
            }
        }
        return searchedSentences;
    }

    static int getWordOccurrences(List<String> sentences, String word) {
        Pattern pattern = Pattern.compile("\\b" + word + "\\b");
        int count = 0;
        for (String searchedSentence : sentences) {
            Matcher matcher = pattern.matcher(searchedSentence);
            while (matcher.find()) {
                count++;
            }
        }
        return count;
    }

    private static String getContent(File file) throws IOException {
        String content;
        try (FileInputStream inputStream = new FileInputStream(file)) {
            content = new String(inputStream.readAllBytes());
        }
        return content;
    }

    private static void print(String word, Result result) {
        String times = result.count() == 1 ? " time." : " times.";

        System.out.println("Passed keyword [" + word + "] occurred " + result.count() + times);
        if (result.count() > 0) {
            System.out.println("Sentences:");
        }
        for (String s : result.sentences()) {
            System.out.println(s);
        }
    }

}
