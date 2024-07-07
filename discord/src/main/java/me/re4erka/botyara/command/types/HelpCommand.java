package me.re4erka.botyara.command.types;

import me.re4erka.botyara.api.command.Command;
import me.re4erka.botyara.Botyara;
import org.jetbrains.annotations.NotNull;

public class HelpCommand implements Command {
    @Override
    public void execute(@NotNull String[] args) {
        info("Список команд: ");

        Botyara.INSTANCE
                .getCommandManager()
                .getCommands()
                .forEach(command ->
                    info(" - /" + command.toLowerCase())
                );
    }
}
