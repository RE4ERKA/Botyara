package me.re4erka.botyara.api.bot.user;

import lombok.Data;
import me.re4erka.botyara.api.bot.friendship.FriendshipType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.util.function.Consumer;

import static java.time.temporal.ChronoUnit.DAYS;

@Data
public class UserData {
    private final long id;

    private FriendshipType friendshipType;
    private int reputation;

    private String name;

    private LocalDate lastDialog;

    private static final UserData STRANGER = new UserData(
            Long.MIN_VALUE, FriendshipType.STRANGER, Integer.MIN_VALUE, "", null
    );

    public UserData(final long id,
                    @NotNull final FriendshipType friendshipType,
                    final int reputation,
                    @NotNull final String name,
                    @Nullable final LocalDate lastDialog) {
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

        setReputation(reputation + ((int) -(DAYS.between(lastDialog, LocalDate.now()))));
    }

    public void ifFamiliarOrElse(Consumer<UserData> action, final Runnable runnable) {
        if (isStranger()) {
            runnable.run();
        } else {
            action.accept(this);
        }
    }

    public boolean isStranger() {
        return friendshipType == FriendshipType.STRANGER;
    }

    public static UserData newFamiliar(long id, String name) {
        return new UserData(id, FriendshipType.FAMILIAR, 0, name, LocalDate.now());
    }

    public static UserData newStranger() {
        return UserData.STRANGER;
    }
}
