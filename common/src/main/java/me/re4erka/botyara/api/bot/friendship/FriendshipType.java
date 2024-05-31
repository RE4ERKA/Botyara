package me.re4erka.botyara.api.bot.friendship;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/* Тип дружды с ботом.
*
* Добавление/удаление типа дружбы могут привести к проблемам с базой данной.
* По этому стоит внимательно посмотреть, что содержится в базе данных.
*  */
@RequiredArgsConstructor
public enum FriendshipType {
    STRANGER(-1),
    FAMILIAR(0),
    FRIEND(150),
    BEST_FRIEND(500);

    private static final FriendshipType[] FAMILIARS = new FriendshipType[] { BEST_FRIEND, FRIEND, FAMILIAR };

    @Getter
    private final int requiredReputation;

    public static FriendshipType[] familiars() {
        return FAMILIARS;
    }
}
