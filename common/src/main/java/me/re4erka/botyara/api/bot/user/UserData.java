package me.re4erka.botyara.api.bot.user;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import me.re4erka.botyara.api.bot.friendship.FriendshipType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.util.function.Consumer;

import static java.time.temporal.ChronoUnit.DAYS;

@Getter
@Setter
public class UserData {
    private final long id;

    private FriendshipType friendshipType;
    private int reputation;

    private String name;

    private LocalDate lastDialog;

    private static final UserData STRANGER = new UserData(
            Long.MIN_VALUE, FriendshipType.STRANGER, Integer.MIN_VALUE, "", null
    );

    public UserData(long id,
                    @NotNull FriendshipType friendshipType,
                    int reputation,
                    @NotNull String name,
                    @Nullable LocalDate lastDialog) {
        this.id = id;

        this.friendshipType = friendshipType;
        this.reputation = reputation;

        this.name = name;

        this.lastDialog = lastDialog;
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

        setReputation(reputation + getNegativeDaysBetween());
    }

    public boolean isStranger() {
        return friendshipType == FriendshipType.STRANGER;
    }

    public static UserData newFamiliar(long id, @NotNull String name) {
        return new UserData(id, FriendshipType.FAMILIAR, 0, name, LocalDate.now());
    }

    public static UserData newStranger() {
        return UserData.STRANGER;
    }

    private int getNegativeDaysBetween() {
        return (int) -DAYS.between(lastDialog, LocalDate.now());
    }
}
