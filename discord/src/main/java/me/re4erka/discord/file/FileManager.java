package me.re4erka.discord.file;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import me.re4erka.api.util.file.CustomFile;
import me.re4erka.api.manager.Manager;
import me.re4erka.discord.Botyara;
import org.simpleyaml.configuration.file.YamlConfiguration;

import java.io.IOException;

@Log4j2
public class FileManager extends Manager {
    @Getter
    private YamlConfiguration properties;

    private static final String PROPERTIES_FILE = "properties.yml";

    @Override
    public boolean start() {
        log.info("Loading configurations...");

        final CustomFile file = Botyara.INSTANCE.getJarDirectory().newFile(PROPERTIES_FILE);

        try {
            properties = file.saveOrLoad(PROPERTIES_FILE);
        } catch (IOException e) {
            log.error("Failed to load the configuration!", e);
            return false;
        }

        return true;
    }
}
