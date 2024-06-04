package me.re4erka.botyara.api.bot.user;

import lombok.Data;
import me.re4erka.botyara.api.bot.friendship.FriendshipType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

@Data
public class UserData {
    private final long id;

    private FriendshipType friendshipType;
    private int reputation;

    private String name;

    private LocalDate lastDialog;

    public UserData(long id,
                    @NotNull FriendshipType friendshipType,
                    @Nullable Integer reputation,
                    @Nullable String name,
                    @Nullable LocalDate lastDialog) {
        this.id = id;

        this.friendshipType = friendshipType;
        this.reputation = reputation == null ? 0 : reputation;

        this.name = name;

        this.lastDialog = lastDialog;
    }

    public void intoFamiliar(String name) {
        this.name = name;
        this.friendshipType = FriendshipType.FAMILIAR;
        this.reputation = 0;
        this.lastDialog = LocalDate.now();
    }

    public boolean setReputation(int reputation) {
        if (reputation < 1) {
            this.reputation = 0;
            return false;
        }

        if (reputation > 1000) {
            this.reputation = 1000;
            return false;
        }

        this.reputation = reputation;
        return true;
    }

    public boolean checkFriendshipStatus() {
        for (FriendshipType type : FriendshipType.familiars()) {
            if (reputation >= type.getRequiredReputation()) {
                if (friendshipType == type) {
                    break;
                }

                setFriendshipType(type);
                return true;
            }
        }

        return false;
    }

    public void checkLastDialog() {
        if (isStranger()) {
            return;
        }

        setReputation(reputation + ((int) -(DAYS.between(lastDialog, LocalDate.now()))));
    }

    public boolean isStranger() {
        return friendshipType == FriendshipType.STRANGER;
    }

    public static UserData newStranger(long id) {
        return new UserData(
                id,
                FriendshipType.STRANGER,
                null,
                null,
                null
        );
    }
}
