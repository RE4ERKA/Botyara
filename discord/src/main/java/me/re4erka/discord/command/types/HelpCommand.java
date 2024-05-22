package me.re4erka.discord.command.types;

import me.re4erka.api.command.Command;
import me.re4erka.discord.Botyara;

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
