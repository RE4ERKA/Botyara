package me.re4erka.botyara.command.types;

import me.re4erka.botyara.Botyara;
import me.re4erka.botyara.api.command.Command;
import me.re4erka.botyara.api.command.logger.Logger;
import org.jetbrains.annotations.NotNull;

public class ActivityCommand implements Command {

    @Override
    public void execute(@NotNull Logger logger, @NotNull String[] args) {
        if (args.length < 1) {
            logger.info("Недостаточно аргументов!");
            logger.info("Подкоманды: update");
            return;
        }

        if (args[0].equals("update")) {
            Botyara.INSTANCE.getDiscordManager().getBot().updateActivities();
            logger.info(String.format("Активность обновлена на %s", Botyara.INSTANCE.getDiscordManager().getBot().getActivityType()));
        } else {
            logger.info("Подкоманда не найдена!");
        }
    }
}
