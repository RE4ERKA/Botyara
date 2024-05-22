package me.re4erka.discord.command.types;

import me.re4erka.api.command.Command;
import me.re4erka.discord.Botyara;

public class StopCommand implements Command {
    @Override
    public boolean execute(String[] args) {
        Botyara.INSTANCE.onDisable();

        return true;
    }
}
