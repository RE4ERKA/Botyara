package me.re4erka.botyara.command.types;

import me.re4erka.botyara.api.command.Command;
import me.re4erka.botyara.Botyara;

public class HelpCommand implements Command {
    @Override
    public boolean execute(String[] args) {
        info("Список команд: ");

        Botyara.INSTANCE
                .getCommandManager()
                .getCommands()
                .forEach(command ->
                    info(" - /" + command.toLowerCase())
                );

        return false;
    }
}
