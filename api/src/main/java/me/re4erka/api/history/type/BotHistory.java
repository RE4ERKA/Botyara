package me.re4erka.api.history.type;

import me.re4erka.api.bot.Bot;
import me.re4erka.api.history.History;
import me.re4erka.api.history.logging.HistoryLogging;
import me.re4erka.api.history.logging.HistoryMessage;

public class BotHistory extends History {
    private final Bot bot;

    private HistoryLogging<HistoryMessage, Bot> logging;

    public BotHistory(String name, Bot bot) {
        super(name);

        this.bot = bot;
    }

    public BotHistory logging(HistoryLogging<HistoryMessage, Bot> logging) {
        this.logging = logging;

        return this;
    }

    public void log(String message) {
        final HistoryMessage historyMessage = HistoryMessage.create(message);
        logging.accept(historyMessage, bot);

        super.log(
                historyMessage.get()
        );
    }
}
