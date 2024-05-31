package me.re4erka.botyara.api.history.type;
import me.re4erka.botyara.api.bot.receiver.Receiver;
import me.re4erka.botyara.api.history.History;
import me.re4erka.botyara.api.history.logging.HistoryLogging;
import me.re4erka.botyara.api.history.logging.HistoryMessage;

public class UserHistory extends History {
    private HistoryLogging<HistoryMessage, Receiver> logging;

    public UserHistory(String name) {
        super(name);
    }

    public UserHistory logging(HistoryLogging<HistoryMessage, Receiver> logging) {
        this.logging = logging;

        return this;
    }

    public void log(String message, Receiver receiver, boolean isPrivate) {
        final String messagePrefix = isPrivate ? "PM" : "SM";
        this.log("<" + messagePrefix + "> " + message, receiver);
    }

    public void log(String message, Receiver receiver) {
        final HistoryMessage historyMessage = HistoryMessage.create(message);
        logging.accept(historyMessage, receiver);

        super.log(
                historyMessage.get()
        );
    }
}
