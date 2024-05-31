package me.re4erka.botyara.command.types;

import me.re4erka.botyara.api.command.Command;
import me.re4erka.botyara.Botyara;

public class StopCommand implements Command {
    @Override
    public boolean execute(String[] args) {
        Botyara.INSTANCE.shutdown();

        return true;
    }
}
