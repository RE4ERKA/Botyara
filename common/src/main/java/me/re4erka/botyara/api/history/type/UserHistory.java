package me.re4erka.botyara.api.history.type;
import me.re4erka.botyara.api.bot.receiver.Receiver;
import me.re4erka.botyara.api.history.History;
import me.re4erka.botyara.api.history.logging.HistoryLogging;
import me.re4erka.botyara.api.history.logging.HistoryMessage;
import org.jetbrains.annotations.NotNull;

public class UserHistory extends History {
    private HistoryLogging<HistoryMessage, Receiver> logging;

    public UserHistory(@NotNull String name) {
        super(name);
    }

    public UserHistory logging(@NotNull HistoryLogging<HistoryMessage, Receiver> logging) {
        this.logging = logging;

        return this;
    }

    public void log(@NotNull String message, @NotNull Receiver receiver) {
        final HistoryMessage historyMessage = HistoryMessage.create(message);
        logging.accept(historyMessage, receiver);

        super.log(
                historyMessage.getMessage()
        );
    }
}
