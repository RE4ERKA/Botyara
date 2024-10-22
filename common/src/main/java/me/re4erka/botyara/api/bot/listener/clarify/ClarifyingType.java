package me.re4erka.botyara.api.bot.listener.clarify;

import lombok.RequiredArgsConstructor;
import me.re4erka.botyara.api.bot.word.search.SearchWords;

@RequiredArgsConstructor
public enum ClarifyingType {
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
