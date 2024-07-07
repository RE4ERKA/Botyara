package me.re4erka.botyara.file;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import me.re4erka.botyara.api.util.file.CustomFile;
import me.re4erka.botyara.api.manager.Manager;
import me.re4erka.botyara.Botyara;
import org.simpleyaml.configuration.file.YamlConfiguration;

import java.io.IOException;

@Getter
@Log4j2
public class FileManager extends Manager {
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
