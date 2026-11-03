package ru.stavarachi.factory.components;

import ru.stavarachi.database.DatabaseInitializer;
import ru.stavarachi.database.DatabaseManager;
import ru.stavarachi.infrastructure.repository.PostgresUserInfoRepository;
import ru.stavarachi.infrastructure.repository.PostgresUserRepository;
import ru.stavarachi.infrastructure.repository.PostgresUserSettingRepository;

public record DatabaseComponent(
        DatabaseManager databaseManager,
        DatabaseInitializer databaseInitializer,
        PostgresUserRepository postgresUserRepository,
        PostgresUserInfoRepository postgresUserInfoRepository,
        PostgresUserSettingRepository postgresUserSettingRepository
) {
}
