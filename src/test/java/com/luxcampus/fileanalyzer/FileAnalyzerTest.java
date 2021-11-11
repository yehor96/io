package com.luxcampus.fileanalyzer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("FileAnalyzer unit tests")
class FileAnalyzerTest {

    @DisplayName("Test breakIntoSentences() method with all types of separators")
    @Test
    void testBreakIntoSentencesWithValidSeparators() {
        String content = "word 1. Another word! And one more word?";
        List<String> expectedResult = List.of("word 1.", "Another word!", "And one more word?");

        List<String> resultSentences = FileAnalyzer.breakIntoSentences(content);

        assertEquals(expectedResult, resultSentences);
    }

    @DisplayName("Test breakIntoSentences() method with invalid separators")
    @Test
    void testBreakIntoSentencesWithInvalidSeparators() {
        String content = "word 1; Another word\n And one more word<>";
        List<String> expectedResult = List.of(content);

        List<String> resultSentences = FileAnalyzer.breakIntoSentences(content);

        assertEquals(expectedResult, resultSentences);
    }

    @DisplayName("Test breakIntoSentences() method with content that does not have separators")
    @Test
    void testBreakIntoSentencesWithoutSeparators() {
        String content = "Sentence without separators";
        List<String> expectedResult = List.of(content);

        List<String> resultSentences = FileAnalyzer.breakIntoSentences(content);

        assertEquals(expectedResult, resultSentences);
    }

    @DisplayName("Test getSearchedSentences() method when all sentences match")
    @Test
    void testGetSearchedSentencesWhenAllSentencesMatch() {
        List<String> inputSentences = List.of("word 1.", "Another word!", "And one more word?");
        String word = "word";

        List<String> resultSentences = FileAnalyzer.getSearchedSentences(inputSentences, word);

        assertEquals(inputSentences, resultSentences);
    }

    @DisplayName("Test getSearchedSentences() method with matching and not matching entries")
    @Test
    void testGetSearchedSentencesWhenSomeSentencesMatchAndSomeNot() {
        List<String> inputSentences = List.of("word 1.", "More random text!", "Another word!", "And one more word?", "Something else.", "The end.");
        List<String> expectedResult = List.of("word 1.", "Another word!", "And one more word?");
        String word = "word";

        List<String> resultSentences = FileAnalyzer.getSearchedSentences(inputSentences, word);

        assertEquals(expectedResult, resultSentences);
    }

    @DisplayName("Test getSearchedSentences() method with empty input")
    @Test
    void testGetSearchedSentencesWithEmptyInput() {
        List<String> inputSentences = Collections.emptyList();
        String word = "word";

        List<String> resultSentences = FileAnalyzer.getSearchedSentences(inputSentences, word);

        assertEquals(inputSentences, resultSentences);
    }

    @DisplayName("Test getSearchedSentences() method when none of sentences contains searched word")
    @Test
    void testGetSearchedSentencesWithoutTheSearchedWord() {
        List<String> inputSentences = List.of("word 1.", "More random text!", "Another word!", "And one more word?", "Something else.", "The end.");
        List<String> expectedResult = Collections.emptyList();
        String word = "information";

        List<String> resultSentences = FileAnalyzer.getSearchedSentences(inputSentences, word);

        assertEquals(expectedResult, resultSentences);
    }

    @DisplayName("Test getWordOccurrences() method with one searched word")
    @Test
    void testGetWordOccurrencesWithOneSearchedWord() {
        List<String> inputSentences = List.of("word 1.", "More random text!", "Another information!", "And one more question?", "Something else.", "The end.");
        String word = "word";
        int expectedResult = 1;

        int actualResult = FileAnalyzer.getWordOccurrences(inputSentences, word);

        assertEquals(expectedResult, actualResult);
    }

    @DisplayName("Test getWordOccurrences() method with one searched word per sentence")
    @Test
    void testGetWordOccurrencesWithOneSearchedWordPerSentence() {
        List<String> inputSentences = List.of("word 1.", "Another word!", "And one more word?");
        String word = "word";
        int expectedResult = 3;

        int actualResult = FileAnalyzer.getWordOccurrences(inputSentences, word);

        assertEquals(expectedResult, actualResult);
    }

    @DisplayName("Test getWordOccurrences() method without searched word")
    @Test
    void testGetWordOccurrencesWithoutSearchedWord() {
        List<String> inputSentences = List.of("word 1.", "Another word!", "And one more word?");
        String word = "information";
        int expectedResult = 0;

        int actualResult = FileAnalyzer.getWordOccurrences(inputSentences, word);

        assertEquals(expectedResult, actualResult);
    }

    @DisplayName("Test getWordOccurrences() method with many searched words per one sentence")
    @Test
    void testGetWordOccurrencesWithManySearchedWordsPerSentence() {
        List<String> inputSentences = List.of("Long sentence that contains one word and another word.", "Here, also a few: word, word, word.", "Another sentence");
        String word = "word";
        int expectedResult = 5;

        int actualResult = FileAnalyzer.getWordOccurrences(inputSentences, word);

        assertEquals(expectedResult, actualResult);
    }

    @DisplayName("Test getWordOccurrences() method with word that contains all the symbols from searched word + 1 more symbol")
    @Test
    void testGetWordOccurrencesWithMatchingSymbolsButNotMatchingWord() {
        List<String> inputSentences = List.of("A lot of words here.", "These are words, but not the ones you are looking for!");
        String word = "word";
        int expectedResult = 0;

        int actualResult = FileAnalyzer.getWordOccurrences(inputSentences, word);

        assertEquals(expectedResult, actualResult);
    }

    @DisplayName("Test getWordOccurrences() method with searched word in different case")
    @Test
    void testGetWordOccurrencesWithWordInDifferentCase() {
        List<String> inputSentences = List.of("Capitalization of Words Matter.", "Words!");
        String word = "words";
        int expectedResult = 0;

        int actualResult = FileAnalyzer.getWordOccurrences(inputSentences, word);

        assertEquals(expectedResult, actualResult);
    }
}
