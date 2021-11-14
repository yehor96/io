package com.luxcampus.filemanager;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.File;
import java.io.IOException;

import static com.luxcampus.filemanager.FileManager.INCORRECT_ARGUMENT_MESSAGE;
import static com.luxcampus.filemanager.FileManager.INCORRECT_DESTINATION_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@DisplayName("FileManager integration tests")
class FileManagerITest {

    final static String RESOURCES = "src/test/resources";
    final static String MOVE_FROM = RESOURCES + "\\moveFrom";
    final static String MOVE_TO = RESOURCES + "\\moveTo";
    final static String COPY_FROM = RESOURCES + "\\copyFrom";
    final static String COPY_TO = RESOURCES + "\\copyTo";

    @ParameterizedTest
    @CsvSource({"folder1, 1", "folder2, 5", "folder3, 3", "folder5, 1", "folder9, 6"})
    @DisplayName("Test countFiles() method with valid folder that contains files")
    void testCountFilesWithValidFolderContainingFiles(String folder, int expectedFileCount) {
        String path = RESOURCES + "\\" + folder;

        int actualFileCount = FileManager.countFiles(path);

        assertEquals(expectedFileCount, actualFileCount);
    }

    @ParameterizedTest
    @CsvSource({"folder1, 0", "folder3, 1", "folder5, 3", "folder9, 4"})
    @DisplayName("Test countDirs() method with valid folder that contains files and folders")
    void testCountFilesWithValidFolderContainingFilesAndFolders(String folder, int expectedFileCount) {
        String path = RESOURCES + "\\" + folder;

        int actualDirCount = FileManager.countDirs(path);

        assertEquals(expectedFileCount, actualDirCount);
    }

    @Test
    @DisplayName("Test thrown exception in countFiles() when passing invalid argument")
    void testExceptionInCountFilesWhenPassingInvalidValue() {
        String path = "Hello world";
        String expectedErrorMessage = String.format(INCORRECT_ARGUMENT_MESSAGE, path);

        try {
            FileManager.countFiles(path);
            fail("Exception was not thrown");
        } catch (Exception e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
            assertEquals(expectedErrorMessage, e.getMessage());
        }
    }

    @Test
    @DisplayName("Test thrown exception in countDirs() when passing invalid argument")
    void testExceptionInCountDirsWhenPassingInvalidValue() {
        String path = "Hello world";
        String expectedErrorMessage = String.format(INCORRECT_ARGUMENT_MESSAGE, path);

        try {
            FileManager.countDirs(path);
            fail("Exception was not thrown");
        } catch (Exception e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
            assertEquals(expectedErrorMessage, e.getMessage());
        }
    }

    @Test
    @DisplayName("Test copy method")
    void testCopyFromTo() throws IOException {
        assertFalse(new File(COPY_TO).exists());
        assertTrue(new File(COPY_FROM).exists());

        FileManager.copy(COPY_FROM, COPY_TO);

        assertTrue(new File(COPY_TO).exists());
        assertTrue(new File(COPY_FROM).exists());
    }

    @Test
    @DisplayName("Test move method")
    void testMoveFromTo() throws IOException {
        assertTrue(new File(MOVE_FROM).exists());
        assertFalse(new File(MOVE_TO).exists());

        FileManager.move(MOVE_FROM, MOVE_TO);

        assertTrue(new File(MOVE_TO).exists());
        assertFalse(new File(MOVE_FROM).exists());
    }

    @Test
    @DisplayName("Test move method throws exception when destination is a file")
    void testMoveExceptionWhenDestinationIsFile() {
        String invalidDestination = RESOURCES + "\\test1.txt";
        String from = RESOURCES + "\\folderforinvalidtests";

        try {
            FileManager.move(from, invalidDestination);
            fail("Exception was not thrown");
        } catch (Exception e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
            assertEquals(INCORRECT_DESTINATION_MESSAGE, e.getMessage());
        }
    }

    @Test
    @DisplayName("Test move method throws exception when source does not exist")
    void testMoveExceptionWhenSourceDoesNotExist() {
        String destination = RESOURCES + "\\folderforinvalidtests";
        String invalidSource = RESOURCES + "\\empty";

        try {
            FileManager.move(invalidSource, destination);
            fail("Exception was not thrown");
        } catch (Exception e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
            assertEquals(INCORRECT_DESTINATION_MESSAGE, e.getMessage());
        }
    }

    @Test
    @DisplayName("Test copy method throws exception when destination is a file")
    void testCopyExceptionWhenDestinationIsFile() {
        String invalidDestination = RESOURCES + "\\test1.txt";
        String from = RESOURCES + "\\folderforinvalidtests";

        try {
            FileManager.copy(from, invalidDestination);
            fail("Exception was not thrown");
        } catch (Exception e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
            assertEquals(INCORRECT_DESTINATION_MESSAGE, e.getMessage());
        }
    }

    @Test
    @DisplayName("Test copy method throws exception when source does not exist")
    void testCopyExceptionWhenSourceDoesNotExist() {
        String destination = RESOURCES + "\\folderforinvalidtests";
        String invalidSource = RESOURCES + "\\empty";

        try {
            FileManager.copy(invalidSource, destination);
            fail("Exception was not thrown");
        } catch (Exception e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
            assertEquals(INCORRECT_DESTINATION_MESSAGE, e.getMessage());
        }
    }

    @AfterAll
    static void moveFilesBack() throws IOException {
        FileManager.move(MOVE_TO, MOVE_FROM);
        FileManager.delete(new File(COPY_TO));
    }

}
