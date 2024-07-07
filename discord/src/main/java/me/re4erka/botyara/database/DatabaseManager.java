package me.re4erka.botyara.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.log4j.Log4j2;
import me.re4erka.botyara.api.bot.friendship.FriendshipType;
import me.re4erka.botyara.api.util.time.DateUtil;
import me.re4erka.botyara.api.manager.Manager;
import me.re4erka.botyara.api.bot.user.UserData;
import me.re4erka.botyara.file.type.Properties;
import org.intellij.lang.annotations.Language;

import java.sql.*;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Log4j2
public class DatabaseManager extends Manager {
    private final HikariConfig config = new HikariConfig();

    private HikariDataSource dataSource;

    @Language("SQL")
    private static final String CREATE_FRIENDSHIP_TYPES_TABLE = """
    CREATE TABLE IF NOT EXISTS `friendship_types` (
    `name` VARCHAR(16) NOT NULL,
    PRIMARY KEY (`name`)
    )
    """;

    @Language("SQL")
    private static final String INSERT_ALL_FRIENDSHIP_TYPES = """
    INSERT OR IGNORE INTO friendship_types(`name`)
    VALUES
    ('STRANGER'),
    ('FAMILIAR'),
    ('FRIEND'),
    ('BEST_FRIEND')
    """;

    @Language("SQL")
    private static final String CREATE_USERS_TABLE = """
    CREATE TABLE IF NOT EXISTS `users` (
    `id` VARCHAR(18) NOT NULL,
    `name` VARCHAR(16) NOT NULL,
    `friendship_type` VARCHAR(16) DEFAULT "STRANGER" NOT NULL,
    `reputation` SMALLINT NOT NULL,
    `last_dialog` DATE NOT NULL,
    FOREIGN KEY (`friendship_type`) REFERENCES `friendship_types`(`name`) ON DELETE CASCADE,
    PRIMARY KEY (`id`)
    )
    """;

    @Language("SQL")
    private static final String INSERT_USER_DATA = """
    INSERT INTO users(`id`, `name`, `friendship_type`, `reputation`, `last_dialog`) VALUES(?, ?, ?, ?, DATE('now'))
    ON CONFLICT DO UPDATE SET `name`=?, `friendship_type`=?, `reputation`=?, `last_dialog`=DATE('now');
    """;

    @Language("SQL")
    private static final String SELECT_USER_DATA = "SELECT `name`, `friendship_type`, `reputation`, `last_dialog` FROM `users` WHERE `id`=?";

    @Override
    public boolean start() {
        log.info("Running databases...");

        config.setDriverClassName("org.sqlite.JDBC");

        try {
            Class.forName(config.getDriverClassName())
                    .getConstructor()
                    .newInstance();
        } catch (Exception e) {
            log.error("An error occurred while initializing a database class!", e);
            return false;
        }

        config.setJdbcUrl(
                String.format("jdbc:sqlite:%s.db", Properties.DATABASE_SQLITE_FILE_NAME.asString())
        );

        dataSource = new HikariDataSource(config);

        this.createTables().join();

        return true;
    }

    private CompletableFuture<Void> createTables() {
        return CompletableFuture.runAsync(() -> {
            try (Connection connection = dataSource.getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement(CREATE_FRIENDSHIP_TYPES_TABLE)) {
                    statement.execute();
                } catch (SQLException e) {
                    log.error("Error when creating the table 'friendship_types' in the database!", e);
                }

                try (PreparedStatement statement = connection.prepareStatement(INSERT_ALL_FRIENDSHIP_TYPES)) {
                    statement.execute();
                } catch (SQLException e) {
                    log.error("Error when inserting friendship types into the 'friendship_types' table in the database!", e);
                }

                try (PreparedStatement statement = connection.prepareStatement(CREATE_USERS_TABLE)) {
                    statement.execute();
                } catch (SQLException e) {
                    log.error("Error when creating the 'users' table in database!", e);
                }
            } catch (SQLException e) {
                log.error("An error occurred while connecting to the database!", e);
            }
        });
    }

    public CompletableFuture<Void> addUserOrUpdate(String id, String name, FriendshipType type, int reputation) {
        return CompletableFuture.runAsync(() -> {
            try (Connection connection = dataSource.getConnection();
                    PreparedStatement statement = connection.prepareStatement(INSERT_USER_DATA)) {
                statement.setString(1, id);
                statement.setString(2, name);
                statement.setString(3, type.name());
                statement.setInt(4, reputation);

                statement.setString(5, name);
                statement.setString(6, type.name());
                statement.setInt(7, reputation);

                statement.execute();
            } catch (SQLException e) {
                log.error("Error when inserting a value in the 'users' table from database!", e);
            }
        });
    }

    public CompletableFuture<Optional<UserData>> getUserData(long id) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = dataSource.getConnection();
                    PreparedStatement statement = connection.prepareStatement(SELECT_USER_DATA)) {
                statement.setString(1, Long.toString(id));

                try (ResultSet result = statement.executeQuery()) {
                    if (result.next()) {
                        return Optional.of(
                                new UserData(
                                        id,
                                        FriendshipType.valueOf(result.getString(2)),
                                        result.getInt(3),
                                        result.getString(1),
                                        DateUtil.parse(result.getString(4))
                                )
                        );
                    }
                }
            } catch (SQLException e) {
                log.error("Error when selecting a value in the 'users' table from the database!", e);
            }

            return Optional.empty();
        });
    }

    @Override
    public void stop() {
        log.info("Shutting down the database...");

        if (dataSource != null
                && !dataSource.isClosed()) {
            dataSource.close();
        }

        log.info("The database was successfully shut down!");
    }
}
