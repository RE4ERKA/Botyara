package me.re4erka.botyara.api.config.types;

import me.re4erka.botyara.api.bot.friendship.FriendshipType;
import me.re4erka.botyara.api.config.exception.ConfigLoadException;
import me.re4erka.botyara.api.config.message.ConfigMessage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.simpleyaml.configuration.ConfigurationSection;

public class ConfigRespond {
    private final ConfigMessage strangerMessage;
    private final ConfigMessage familiarMessage;
    private final ConfigMessage friendMessage;
    private final ConfigMessage bestFriendMessage;

    public ConfigRespond(@Nullable ConfigurationSection section) throws ConfigLoadException {
        if (section == null) {
            throw new ConfigLoadException("The 'Response' list cannot be empty!");
        }

        this.strangerMessage = create(section, FriendshipType.STRANGER);
        this.familiarMessage = create(section, FriendshipType.FAMILIAR);
        this.friendMessage = create(section, FriendshipType.FRIEND);
        this.bestFriendMessage = create(section, FriendshipType.BEST_FRIEND);

        if (getMessageForBestFriend() == null) {
            throw new ConfigLoadException("There must be at least one response!");
        }
    }

    public ConfigMessage getMessageForStranger() {
        return strangerMessage;
    }

    public ConfigMessage getMessageForFamiliar() {
        return familiarMessage == null ? getMessageForStranger() : familiarMessage;
    }

    public ConfigMessage getMessageForFriend() {
        return friendMessage == null ? getMessageForFamiliar() : friendMessage;
    }

    public ConfigMessage getMessageForBestFriend() {
        return bestFriendMessage == null ? getMessageForFriend() : bestFriendMessage;
    }

    @Nullable
    private ConfigMessage create(@NotNull ConfigurationSection section, @NotNull FriendshipType type) throws ConfigLoadException {
        return section.contains(type.name())
                ? new ConfigMessage(section.getConfigurationSection(type.name()))
                : null;
    }
}
