package org.robolectric.res;

import java.io.File;

public class RawResourceLoader {
    private final ResBundle<File> rawResourceFiles;

    public RawResourceLoader(ResBundle<File> rawResourceFiles) {
        this.rawResourceFiles = rawResourceFiles;
    }

    public void loadFrom(ResourcePath resourcePath) {
        if (resourcePath.rawDir != null) {
            File[] files = resourcePath.rawDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    String name = file.getName();
                    int dotIndex = name.indexOf(".");
                    String fileBaseName = dotIndex >= 0 ? name.substring(0, dotIndex) : name;
                    rawResourceFiles.put("raw", fileBaseName, file, new XmlLoader.XmlContext(resourcePath.getPackageName(), file));
                }
            }
        }
    }
}
