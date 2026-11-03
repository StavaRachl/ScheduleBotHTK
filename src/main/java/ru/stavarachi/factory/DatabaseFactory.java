package ru.stavarachi.factory;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ru.stavarachi.config.DatabaseConfig;
import ru.stavarachi.database.DatabaseInitializer;
import ru.stavarachi.database.DatabaseManager;
import ru.stavarachi.factory.components.DatabaseComponent;
import ru.stavarachi.infrastructure.repository.PostgresUserInfoRepository;
import ru.stavarachi.infrastructure.repository.PostgresUserRepository;
import ru.stavarachi.infrastructure.repository.PostgresUserSettingRepository;

public final class DatabaseFactory {
    private DatabaseFactory() {}

    @NotNull
    @Contract("_ -> new")
    public static DatabaseComponent create(DatabaseConfig databaseConfig) {
        DatabaseManager databaseManager = new DatabaseManager(databaseConfig);
        DatabaseInitializer databaseInitializer = new DatabaseInitializer(databaseManager);
        PostgresUserRepository postgresUserRepository = new PostgresUserRepository(databaseManager);
        PostgresUserInfoRepository postgresUserInfoRepository = new PostgresUserInfoRepository(databaseManager);
        PostgresUserSettingRepository postgresUserSettingRepository = new PostgresUserSettingRepository(databaseManager);

        return new DatabaseComponent(
                databaseManager,
                databaseInitializer,
                postgresUserRepository,
                postgresUserInfoRepository,
                postgresUserSettingRepository
        );
    }
}
