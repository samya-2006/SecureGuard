package com.secureguard.scanner;

import com.secureguard.model.Language;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class LanguageDetector {

    private static final Map<String, Language> EXTENSIONS = new HashMap<>();

    static {
        EXTENSIONS.put("java", Language.JAVA);
        EXTENSIONS.put("py", Language.PYTHON);
        EXTENSIONS.put("js", Language.JAVASCRIPT);
        EXTENSIONS.put("ts", Language.TYPESCRIPT);

        EXTENSIONS.put("c", Language.C);
        EXTENSIONS.put("cpp", Language.CPP);
        EXTENSIONS.put("cc", Language.CPP);
        EXTENSIONS.put("cxx", Language.CPP);

        EXTENSIONS.put("cs", Language.CSHARP);
        EXTENSIONS.put("go", Language.GO);
        EXTENSIONS.put("php", Language.PHP);
    }


    public Language detect(File file) {

        String fileName = file.getName();

        int dot = fileName.lastIndexOf('.');

        if (dot == -1)
            return Language.UNKNOWN;

        String extension = fileName.substring(dot + 1).toLowerCase();

        return EXTENSIONS.getOrDefault(extension, Language.UNKNOWN);
    }
}