package me.re4erka.botyara.command.types;

import me.re4erka.botyara.api.command.Command;
import me.re4erka.botyara.Botyara;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class ListenersCommand implements Command {
    @Override
    public void execute(@NotNull String[] args) {
        if (args.length < 1) {
            info("Недостаточно аргументов!");
            info("Подкоманды: reload|cleanup|list|unregister");
            return;
        }

        switch (args[0]) {
            case "reload" -> {
                info("Перезагрузка...");

                Botyara.INSTANCE.getDiscordManager().getBot()
                        .cleanUp()
                        .unregisterAll();

                Botyara.INSTANCE.getDiscordManager().loadListeners();
            }

            case "cleanup" -> {
                info("Очистка ожидающий и вопросительных слушателей...");

                Botyara.INSTANCE.getDiscordManager().getBot().cleanUp();
            }

            case "list" -> {
                info("Список слушателей:");

                Botyara.INSTANCE.getDiscordManager()
                        .getBot()
                        .getListeners()
                        .forEach(listener -> info(
                                "Имя: " + listener.getName() + " | PostOrder: " + listener.getPostOrder()
                        )
                );
            }

            case "unregister" -> {
                final boolean hasUnregistered = Botyara.INSTANCE.getDiscordManager()
                        .getBot()
                        .unregister(args[1]);

                if (hasUnregistered) {
                    info("Слушатель '%s' больше не зарегистрирован!", args[1].toUpperCase(Locale.ROOT));
                } else {
                    info("Слушатель не был найден!");
                }
            }

            default -> info("Подкоманда не найдена!");
        }
    }
}
