package ru.stavarachi.factory;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ru.stavarachi.config.DatabaseConfig;
import ru.stavarachi.database.DatabaseInitializer;
import ru.stavarachi.database.DatabaseManager;
import ru.stavarachi.factory.components.DatabaseComponent;
import ru.stavarachi.infrastructure.repository.PostgresUserRepository;

public final class DatabaseFactory {
    private DatabaseFactory() {}

    @NotNull
    @Contract("_ -> new")
    public static DatabaseComponent create(DatabaseConfig databaseConfig) {
        DatabaseManager databaseManager = new DatabaseManager(databaseConfig);
        DatabaseInitializer databaseInitializer = new DatabaseInitializer(databaseManager);
        PostgresUserRepository postgresUserRepository = new PostgresUserRepository(databaseManager);

        return new DatabaseComponent(
                databaseManager,
                databaseInitializer,
                postgresUserRepository
        );
    }
}
