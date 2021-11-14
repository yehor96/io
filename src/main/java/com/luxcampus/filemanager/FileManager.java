package com.luxcampus.filemanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class FileManager {

    static final String INCORRECT_ARGUMENT_MESSAGE = "Please make sure to pass a directory (not a file). %s is not valid argument";
    static final String INCORRECT_DESTINATION_MESSAGE = "Please make sure destination directory is not a file and source exists.";
    static final String NO_ACCESS_TO_FILES_MESSAGE = "Do not have permissions to access some of the files/folders in path: ";

    public static int countFiles(String path) {
        File file = getFileIfValidDirectory(path);
        return countFiles(file);
    }

    public static int countDirs(String path) {
        File file = getFileIfValidDirectory(path);
        return countDirs(file);
    }

    public static void move(String from, String to) throws IOException {
        copy(from, to);
        delete(new File(from));
    }

    public static void copy(String from, String to) throws IOException {
        File fromFile = new File(from);
        File toFile = new File(to);
        if (!fromFile.exists() || toFile.isFile()) {
            throw new IllegalArgumentException(INCORRECT_DESTINATION_MESSAGE);
        } else {
            copy(fromFile, toFile);
        }
    }

    static void delete(File directory) {
        File[] files = getFilesOf(directory);

        for (File file : files) {
            if (file.isFile()) {
                file.delete();
            } else {
                file.deleteOnExit();
                delete(file);
            }
        }
        directory.delete();
    }

    private static void copy(File fromDestination, File toDestination) throws IOException {
        if (fromDestination.isFile()) {
            copyFile(fromDestination, toDestination);
        } else {
            copyDirectory(fromDestination, toDestination);
        }
    }

    private static void copyDirectory(File fromDestination, File toDestination) throws IOException {
        if (!toDestination.exists()) {
            toDestination.mkdir();
        }

        for (String fileName : getFileNamesOf(fromDestination)) {
            File fromFileChild = new File(fromDestination, fileName);
            File toFileChild = new File(toDestination, fileName);
            copy(fromFileChild, toFileChild);
        }
    }

    private static void copyFile(File fromFile, File toFile) throws IOException {
        try (FileInputStream inputStream = new FileInputStream(fromFile);
             FileOutputStream outputStream = new FileOutputStream(toFile)) {
            inputStream.transferTo(outputStream);
        }
    }

    private static int countFiles(File file) {
        int count = 0;
        File[] files = getFilesOf(file);

        for (File innerFile : files) {
            if (innerFile.isFile()) {
                count++;
            } else {
                count += countFiles(innerFile);
            }
        }

        return count;
    }

    private static int countDirs(File file) {
        int count = 0;
        File[] files = getFilesOf(file);

        for (File innerFile : files) {
            if (innerFile.isDirectory()) {
                count++;
                count += countDirs(innerFile);
            }
        }

        return count;
    }

    private static File getFileIfValidDirectory(String path) {
        File file = new File(path);

        if (!file.isDirectory()) {
            String errorMessage = String.format(INCORRECT_ARGUMENT_MESSAGE, path);
            throw new IllegalArgumentException(errorMessage);
        }

        return file;
    }

    private static File[] getFilesOf(File file) {
        File[] files = file.listFiles();

        if (Objects.isNull(files)) {
            throw new IllegalArgumentException(NO_ACCESS_TO_FILES_MESSAGE + file.getPath());
        }

        return files;
    }

    private static String[] getFileNamesOf(File file) {
        String[] files = file.list();

        if (Objects.isNull(files)) {
            throw new IllegalArgumentException(NO_ACCESS_TO_FILES_MESSAGE + file.getPath());
        }

        return files;
    }

}
