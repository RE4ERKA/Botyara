package me.re4erka.botyara.command.types;

import me.re4erka.botyara.api.command.Command;
import me.re4erka.botyara.Botyara;
import me.re4erka.botyara.api.command.logger.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class ListenersCommand implements Command {
    @Override
    public void execute(@NotNull Logger logger, @NotNull String[] args) {
        if (args.length < 1) {
            logger.info("Недостаточно аргументов!");
            logger.info("Подкоманды: reload|cleanup|list|unregister");
            return;
        }

        switch (args[0]) {
            case "reload" -> {
                logger.info("Перезагрузка...");

                Botyara.INSTANCE.getDiscordManager().getBot()
                        .cleanUp()
                        .unregisterAll();

                Botyara.INSTANCE.getDiscordManager().loadListeners();
            }

            case "cleanup" -> {
                logger.info("Очистка ожидающий и вопросительных слушателей...");

                Botyara.INSTANCE.getDiscordManager().getBot().cleanUp();
            }

            case "list" -> {
                logger.info("Список слушателей:");

                Botyara.INSTANCE.getDiscordManager()
                        .getBot()
                        .getListeners()
                        .forEach(listener -> logger.info(
                                "Имя: " + listener.getName() + " | PostOrder: " + listener.getPostOrder()
                        )
                );
            }

            case "unregister" -> {
                final boolean hasUnregistered = Botyara.INSTANCE.getDiscordManager()
                        .getBot()
                        .unregister(args[1]);

                if (hasUnregistered) {
                    logger.info("Слушатель '%s' больше не зарегистрирован!", args[1].toUpperCase(Locale.ROOT));
                } else {
                    logger.info("Слушатель не был найден!");
                }
            }

            default -> logger.info("Подкоманда не найдена!");
        }
    }
}
