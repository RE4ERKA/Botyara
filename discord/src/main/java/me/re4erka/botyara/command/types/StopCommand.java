package me.re4erka.botyara.command.types;

import me.re4erka.botyara.api.command.Command;
import me.re4erka.botyara.Botyara;
import me.re4erka.botyara.api.command.logger.Logger;
import org.jetbrains.annotations.NotNull;

public class StopCommand implements Command {
    @Override
    public void execute(@NotNull Logger logger, @NotNull String[] args) {
        Botyara.INSTANCE.shutdown();
    }
}
