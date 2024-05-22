package me.re4erka.api.util.file;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

public class JarDirectory {
    private final String parent;

    public JarDirectory() {
        final URL jarPath = getClass().getProtectionDomain().getCodeSource().getLocation();

        try {
            this.parent = Paths.get(jarPath.toURI()).toFile().getParent();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public CustomFile newFile(String path) {
        return new CustomFile(parent, path);
    }

    public File[] filesInDirectory(String directory) {
        return new File(parent, directory).listFiles();
    }

    public boolean notExists(String path) {
        return !new File(parent, path).exists();
    }
}
