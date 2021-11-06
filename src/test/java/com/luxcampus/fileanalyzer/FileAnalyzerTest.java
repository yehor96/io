package com.luxcampus.fileanalyzer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.luxcampus.fileanalyzer.FileAnalyzer.INCORRECT_ARGUMENT_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@DisplayName("FileAnalyzer tests")
class FileAnalyzerTest {

    final static String RESOURCES = "src/main/resources";

    @Test
    @DisplayName("Test large file")
    void testLargeFile() throws IOException {
        String searchedWord = "tree";
        String[] args = {RESOURCES + "\\test1.txt", searchedWord};

        Result result = FileAnalyzer.search(args);
        var sentences = result.sentences();

        assertEquals(2, result.count());
        assertEquals(2, sentences.size());
        assertEquals("Tree at my window, window tree.", sentences.get(0));
        assertEquals("But tree, I have seen you taken and tossed.", sentences.get(1));
    }

    @Test
    @DisplayName("Test file with searched word")
    void testFileWithSearchedWord() throws IOException {
        String keyword = "Hello";
        String[] args = {RESOURCES + "\\test2.txt", keyword};

        Result result = FileAnalyzer.search(args);
        var sentences = result.sentences();

        assertEquals(4, result.count());
        assertEquals(4, sentences.size());
        assertEquals("Hello World!", sentences.get(0));
        assertEquals("Yes, Hello!", sentences.get(sentences.size() - 1));
    }

    @Test
    @DisplayName("Test file with searched word and different end symbols")
    void testFileWithSearchedWordAndDifferentEndSymbols() throws IOException {
        String keyword = "sentence";
        String[] args = {RESOURCES + "\\test3.txt", keyword};

        Result result = FileAnalyzer.search(args);
        var sentences = result.sentences();

        assertEquals(3, result.count());
        assertEquals(3, sentences.size());
        assertEquals("sentence one.", sentences.get(0));
        assertEquals("sentence two!", sentences.get(1));
        assertEquals("sentence three?", sentences.get(2));
    }

    @Test
    @DisplayName("Test file with empty content")
    void testFileWithEmptyContent() throws IOException {
        String keyword = "word";
        String[] args = {RESOURCES + "\\test4.txt", keyword};

        Result result = FileAnalyzer.search(args);
        var sentences = result.sentences();

        assertEquals(0, result.count());
        assertEquals(0, sentences.size());
    }

    @Test
    @DisplayName("Test file without any chars")
    void testFileWithoutChars() throws IOException {
        String keyword = "without";
        String[] args = {RESOURCES + "\\test5.txt", keyword};

        Result result = FileAnalyzer.search(args);
        var sentences = result.sentences();

        assertEquals(1, result.count());
        assertEquals(1, sentences.size());
        assertEquals("Only one sentence here without any chars", sentences.get(0));
    }

    @Test
    @DisplayName("Test file with not matching count of searched word and sentences")
    void testFileWithNotMatchingCountOfSearchedWordAndSentences() throws IOException {
        String keyword = "word";
        String[] args = {RESOURCES + "\\test6.txt", keyword};

        Result result = FileAnalyzer.search(args);
        var sentences = result.sentences();

        assertEquals(7, result.count());
        assertEquals(4, sentences.size());
        assertEquals("This file will not have matching searched word result count to the number of sentences.", sentences.get(0));
        assertEquals("Empty sentence ^_^ word.", sentences.get(sentences.size() - 1));
    }

    @Test
    @DisplayName("Test exception is thrown when passing only directory")
    void testExceptionWhenPassingDirectory() {
        String[] args = {RESOURCES};

        try {
            FileAnalyzer.search(args);
            fail("Exception was not thrown");
        } catch (Exception e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
            assertEquals(INCORRECT_ARGUMENT_MESSAGE, e.getMessage());
        }
    }

    @Test
    @DisplayName("Test exception is thrown when passing one argument")
    void testExceptionWhenPassingOneArgument() {
        String[] args = {RESOURCES + "\\test6.txt"};

        try {
            FileAnalyzer.search(args);
            fail("Exception was not thrown");
        } catch (Exception e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
            assertEquals(INCORRECT_ARGUMENT_MESSAGE, e.getMessage());
        }
    }

    @Test
    @DisplayName("Test analyzing image file")
    void testImageFile() throws IOException {
        String keyword = "word";
        String[] args = {RESOURCES + "\\img.png", keyword};

        Result result = FileAnalyzer.search(args);
        var sentences = result.sentences();

        assertEquals(0, result.count());
        assertEquals(0, sentences.size());
    }

}
