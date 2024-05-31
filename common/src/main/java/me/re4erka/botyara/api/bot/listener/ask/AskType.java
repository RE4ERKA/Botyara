package me.re4erka.botyara.api.bot.listener.ask;

import lombok.RequiredArgsConstructor;
import me.re4erka.botyara.api.bot.word.search.SearchWords;

@RequiredArgsConstructor
public enum AskType {
    WHY(
            SearchWords.builder()
                    .words("почему")
                    .words("а что так")
                    .words("зачем")
                    .words("объясни")
                    .words("поясни")
                    .build()
    ),
    SURE(
            SearchWords.builder()
                    .words("точно")
                    .words("не врешь")
                    .words("правда")
                    .build()
    );

    private final SearchWords questions;

    public SearchWords getSearchWords() {
        return questions;
    }
}
