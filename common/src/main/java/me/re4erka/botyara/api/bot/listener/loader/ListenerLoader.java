package me.re4erka.botyara.api.bot.listener.loader;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.Files;
import com.google.common.reflect.ClassPath;
import lombok.extern.log4j.Log4j2;
import me.re4erka.botyara.api.bot.listener.ListeningBot;
import me.re4erka.botyara.api.bot.listener.common.Listener;
import me.re4erka.botyara.api.config.exception.ConfigLoadException;
import me.re4erka.botyara.api.config.handler.ConfigHandler;
import me.re4erka.botyara.api.util.file.JarDirectory;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.message.ParameterizedMessage;
import org.simpleyaml.configuration.file.YamlConfiguration;
import org.simpleyaml.utils.SupplierIO;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;

@Log4j2
public class ListenerLoader {
    public static ImmutableSet<ConfigHandler> fromDirectory(JarDirectory directory, boolean ignoreEmptyFolder) {
        if (directory.notExists("listeners")) {
            final String exampleListenerPath = "listeners/ExampleListener.yml";

            try {
                directory.newFile(exampleListenerPath)
                        .createNewCustomFile()
                        .saveOrLoad(exampleListenerPath);
            } catch (IOException e) {
                log.error("Failed to create path or file!", e);
                return ImmutableSet.of();
            }
        }

        final File[] files = directory.filesInDirectory("listeners");

        if (files == null || files.length == 0) {
            if (!ignoreEmptyFolder) {
                log.warn("No listeners found in the 'listeners' folder!");
            }

            return ImmutableSet.of();
        }

        final ImmutableSet.Builder<ConfigHandler> builder = new ImmutableSet.Builder<>();

        for (File file : files) {
            if (file.isDirectory()) {
                loadDirectory(
                        builder,
                        file.getName().toUpperCase(Locale.ROOT),
                        file
                );
            } else if (file.getName().endsWith(".yml")) {
                final ConfigHandler handler = create(formatName(file.getName()), file);

                if (handler != null) {
                    builder.add(handler);
                }
            }
        }

        return builder.build();
    }

    public static ImmutableSet<Listener> fromPackage(String packageName, ListeningBot bot) {
        try {
            final ImmutableSet.Builder<Listener> builder = new ImmutableSet.Builder<>();

            ClassPath.from(Thread.currentThread().getContextClassLoader())
                    .getTopLevelClasses(packageName)
                    .forEach(classInfo -> builder.add(
                            loadListener(
                                    classInfo.load().asSubclass(Listener.class),
                                    bot
                            )
                    ));

            return builder.build();
        } catch (IOException e) {
            log.error("Failed to find classes in the package!", e);
        }

        return ImmutableSet.of();
    }

    private static Listener loadListener(Class<? extends Listener> clazz, ListeningBot bot) {
        try {
            try {
                return clazz.getConstructor().newInstance();
            } catch (NoSuchMethodException ignored) {
                return clazz.getConstructor(ListeningBot.class).newInstance(bot);
            }
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private static void loadDirectory(ImmutableSet.Builder<ConfigHandler> builder, String directoryName, File directory) {
        final File[] files = directory.listFiles(pathname -> pathname.getName().endsWith(".yml"));

        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.isFile()) {
                final ConfigHandler handler = create(
                        directoryName + "/" + formatName(file.getName()),
                        file
                );

                if (handler != null) {
                    builder.add(handler);
                }
            }
        }
    }

    private static ConfigHandler create(String name, File file) {
        final YamlConfiguration config = new YamlConfiguration();

        try (BufferedReader reader = Files.newReader(file, Charsets.UTF_8)) {
            try {
                config.load(reader);
            } catch (IOException e) {
                log.error(new ParameterizedMessage("Failed to load listener named '{}'!", name), e);
                return null;
            }
        } catch (IOException e) {
            log.error(new ParameterizedMessage("Failed to read listener named '{}'!", name), e);
            return null;
        }

        try {
            return new ConfigHandler(name, config);
        } catch (ConfigLoadException exception) {
            log.error(
                    new ParameterizedMessage(
                            "Failed to create a listener named '{}'!",
                            exception.getListenerName()
                    ),
                    exception
            );
        }

        return null;
    }

    private static String formatName(String name) {
        return StringUtils.removeEnd(name, ".yml").toUpperCase(Locale.ROOT);
    }
}
