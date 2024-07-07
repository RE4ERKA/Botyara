package me.re4erka.botyara.api.util.file;

import org.jetbrains.annotations.NotNull;

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

    public CustomFile newFile(@NotNull String path) {
        return new CustomFile(parent, path);
    }

    public File[] filesInDirectory(@NotNull String directory) {
        return new File(parent, directory).listFiles();
    }

    public boolean notExists(@NotNull String path) {
        return !new File(parent, path).exists();
    }
}
