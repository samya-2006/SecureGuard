package com.secureguard.scanner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileScanner {

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

                collectFiles(file, files);

            } else {

                files.add(file);

            }

        }

    }

}