package me.re4erka.botyara.command.types;

import me.re4erka.botyara.api.bot.user.UserData;
import me.re4erka.botyara.api.command.Command;
import me.re4erka.botyara.Botyara;
import me.re4erka.botyara.api.command.logger.Logger;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

public class UsersCommand implements Command {
    @Override
    public void execute(@NotNull Logger logger, @NotNull String[] args) {
        if (args.length < 1) {
            logger.info("Недостаточно аргументов!");
            logger.info("Подкоманды: get|saveAll|setName|setReputation");

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
                                            userData -> infoUserData(logger, userData),
                                            () -> infoUserNotFound(logger)
                                    )
                            );
                } else {
                    infoUserData(logger, data);
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
                                        logger.info(
                                                "Имя пользователя %s изменено на %s в базе-данных",
                                                args[1],
                                                StringUtils.capitalize(args[2])
                                        );
                                    },
                                    () -> infoUserNotFound(logger)
                            ));
                } else {
                    data.setName(StringUtils.capitalize(args[2]));
                    logger.info("Имя пользователя %s изменено на %s в кеше", args[1], StringUtils.capitalize(args[2]));
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
                                        logger.info("Репутация пользователя %s изменено на %s в базе-данных", args[1], args[2]);
                                    },
                                    () -> infoUserNotFound(logger)
                            ));
                } else {
                    data.setReputation(
                            Integer.parseInt(args[2])
                    );
                    logger.info("Репутация пользователя %s изменено на %s в кеше", args[1], args[2]);
                }
            }

            case "saveall" -> {
                Botyara.INSTANCE.getDiscordManager().getUsers().saveAll();
                logger.info("Все пользователи из кеша были сохранены!");
            }

            default -> logger.info("Подкоманда не найдена!");
        }
    }

    private void infoUserData(@NotNull Logger logger, @NotNull UserData data) {
        logger.info("ID: " + data.getId());
        logger.info("Name: ".concat(data.getName()));
        logger.info("FriendshipType: " + data.getFriendshipType().toString());
        logger.info("Reputation: " + data.getReputation());
        logger.info("LastDialog: ".concat(data.getLastDialog().toString()));
    }

    private void infoUserNotFound(@NotNull Logger logger) {
        logger.info("Пользователь с таким айди не был найден!");
    }
}
