package me.re4erka.botyara.command.types;

import me.re4erka.botyara.api.bot.user.UserData;
import me.re4erka.botyara.api.command.Command;
import me.re4erka.botyara.Botyara;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

public class UsersCommand implements Command {
    @Override
    public void execute(@NotNull String[] args) {
        if (args.length < 1) {
            info("Недостаточно аргументов!");
            info("Подкоманды: get|saveAll|setName|setReputation");

            return;
        }

        final long id = args.length > 1 ? Long.parseLong(args[1]) : 0;

        switch (args[0].toLowerCase()) {
            case "get" -> {
                UserData data = Botyara.INSTANCE.getDiscordManager()
                        .getUsers()
                        .getCache()
                        .getIfPresent(id);

                if (data == null) {
                    Botyara.INSTANCE.getDatabaseManager()
                            .getUserData(id).thenAccept(optionalData ->
                                    optionalData.ifPresentOrElse(
                                            this::infoUserData, this::infoUserNotFound
                                    )
                            );
                } else {
                    infoUserData(data);
                }
            }

            case "setname" -> {
                UserData data = Botyara.INSTANCE.getDiscordManager()
                        .getUsers()
                        .getCache()
                        .getIfPresent(id);

                if (data == null) {
                    Botyara.INSTANCE.getDatabaseManager()
                            .getUserData(id).thenAccept(optionalData -> optionalData.ifPresentOrElse(
                                    userData -> {
                                        Botyara.INSTANCE.getDatabaseManager().addUserOrUpdate(
                                                args[1],
                                                StringUtils.capitalize(args[2]),
                                                userData.getFriendshipType(),
                                                userData.getReputation()
                                        );
                                        info("Имя пользователя %s изменено на %s в базе-данных", args[1], StringUtils.capitalize(args[2]));
                                    },
                                    this::infoUserNotFound
                            ));
                } else {
                    data.setName(StringUtils.capitalize(args[2]));
                    info("Имя пользователя %s изменено на %s в кеше", args[1], StringUtils.capitalize(args[2]));
                }
            }

            case "setreputation" -> {
                UserData data = Botyara.INSTANCE.getDiscordManager()
                        .getUsers()
                        .getCache()
                        .getIfPresent(id);

                if (data == null) {
                    Botyara.INSTANCE.getDatabaseManager()
                            .getUserData(id).thenAccept(optionalData -> optionalData.ifPresentOrElse(
                                    userData -> {
                                        Botyara.INSTANCE.getDatabaseManager().addUserOrUpdate(
                                                args[1],
                                                userData.getName(),
                                                userData.getFriendshipType(),
                                                Integer.parseInt(args[2])
                                        );
                                        info("Репутация пользователя %s изменено на %s в базе-данных", args[1], args[2]);
                                    },
                                    this::infoUserNotFound
                            ));
                } else {
                    data.setReputation(
                            Integer.parseInt(args[2])
                    );
                    info("Репутация пользователя %s изменено на %s в кеше", args[1], args[2]);
                }
            }

            case "saveall" -> {
                Botyara.INSTANCE.getDiscordManager().getUsers().saveAll();
                info("Все пользователи из кеша были сохранены!");
            }

            default -> info("Подкоманда не найдена!");
        }
    }

    private void infoUserData(UserData data) {
        info("ID: " + data.getId());
        info("Name: " + data.getName());
        info("FriendshipType: " + data.getFriendshipType().toString());
        info("Reputation: " + data.getReputation());
        info("LastDialog: " + data.getLastDialog().toString());
    }

    private void infoUserNotFound() {
        info("Пользователь с таким айди не был найден!");
    }
}
