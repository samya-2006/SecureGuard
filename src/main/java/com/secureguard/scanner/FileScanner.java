package com.secureguard.scanner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FileScanner {

    private static final Set<String> IGNORED_DIRECTORIES = Set.of(
            "target",
            "build",
            "dist",
            ".git",
            ".idea",
            "node_modules",
            "venv"
    );

    private static final Set<String> IGNORED_FILES = Set.of(
            "SG009.java"
    );

    public List<File> scanProject(File folder) {

        List<File> files = new ArrayList<>();

        collectFiles(folder, files);

        return files;
    }

    private void collectFiles(File folder, List<File> files) {

        File[] folderContents = folder.listFiles();

        if (folderContents == null) {
            return;
        }

        for (File file : folderContents) {

            if (file.isDirectory()) {

                if (IGNORED_DIRECTORIES.contains(file.getName())) {
                    continue;
                }

                collectFiles(file, files);

            } else {

                if (isSupportedSourceFile(file)) {
                    files.add(file);
                }
            }
        }
    }

    private boolean isSupportedSourceFile(File file) {

        String name = file.getName().toLowerCase();

        return name.endsWith(".java")
                || name.endsWith(".py")
                || name.endsWith(".js")
                || name.endsWith(".ts")
                || name.endsWith(".c")
                || name.endsWith(".cpp")
                || name.endsWith(".cs")
                || name.endsWith(".go")
                || name.endsWith(".php");
    }
}