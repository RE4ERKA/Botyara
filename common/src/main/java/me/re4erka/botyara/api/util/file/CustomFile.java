package me.re4erka.botyara.api.util.file;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.configuration.file.YamlConfiguration;
import org.simpleyaml.configuration.implementation.snakeyaml.lib.error.YAMLException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Log4j2
public class CustomFile extends File {
    public CustomFile(@NotNull String parent, @NotNull String child) {
        super(parent, child);
    }

    public CustomFile createNewCustomFile() throws IOException {
        final File file = getCanonicalFile().getParentFile();
        if (file != null && !file.exists()) {
            Files.createParentDirs(this);
        }

        if (!exists()) {
            Files.touch(this);
        }

        return this;
    }

    public YamlConfiguration saveOrLoad(@NotNull String resource) throws IOException {
        final YamlConfiguration config = new YamlConfiguration();

        config.options().useComments(true);
        config.options().charset(Charsets.UTF_8);

        if (exists()) {
            loadFromFile(config);
        } else {
            loadFromResource(config, resource);
            config.save(this);
        }

        return config;
    }

    private void loadFromFile(@NotNull YamlConfiguration config) throws IOException {
        try {
            config.load(this);
        } catch (YAMLException e) {
            log.error("Failed to load configuration from file: {}", getAbsolutePath(), e);
            throw new IOException("Failed to load configuration from file: " + getAbsolutePath(), e);
        }
    }

    private void loadFromResource(@NotNull YamlConfiguration config, @NotNull String resource) throws IOException {
        try (InputStream inputStream = Resources.getResource(resource).openStream()) {
            config.load(inputStream);
        } catch (IOException e) {
            log.error("Failed to load file from resources: {}", resource, e);
            throw new IOException("Failed to load file from resources: " + resource, e);
        }
    }
}
