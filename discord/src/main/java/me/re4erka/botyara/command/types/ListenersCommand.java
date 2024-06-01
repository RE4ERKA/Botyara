package me.re4erka.botyara.command.types;

import me.re4erka.botyara.api.command.Command;
import me.re4erka.botyara.Botyara;

import java.util.Locale;

public class ListenersCommand implements Command {
    @Override
    public void execute(String[] args) {
        if (args.length < 1) {
            info("Недостаточно аргументов!");
            info("Подкоманды: reload|cleanup|list|unregister");
            return;
        }

        switch (args[0]) {
            case "reload" -> {
                info("Перезагрузка...");

                Botyara.INSTANCE.getBotManager().getBot()
                        .cleanUp()
                        .unregisterAll();

                Botyara.INSTANCE.getBotManager().loadListeners();
            }

            case "cleanup" -> {
                info("Очистка ожидающий и вопросительных слушателей...");

                Botyara.INSTANCE.getBotManager().getBot().cleanUp();
            }

            case "list" -> {
                info("Список слушателей:");

                Botyara.INSTANCE.getBotManager()
                        .getBot()
                        .getListeners()
                        .forEach(listener -> info(
                                "Имя: " + listener.getName() + " | PostOrder: " + listener.getPostOrder()
                        )
                );
            }

            case "unregister" -> {
                Botyara.INSTANCE.getBotManager().getBot().unregister(args[1]);
                info("Слушатель '%s' больше не зарегистрирован!", args[1].toUpperCase(Locale.ROOT));
            }

            default -> info("Подкоманда не найдена!");
        }
    }
}
