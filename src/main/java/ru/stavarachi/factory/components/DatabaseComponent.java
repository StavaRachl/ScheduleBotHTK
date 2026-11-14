package ru.stavarachi.factory.components;

import ru.stavarachi.database.DatabaseInitializer;
import ru.stavarachi.database.DatabaseManager;
import ru.stavarachi.infrastructure.repository.*;

public record DatabaseComponent(
        DatabaseManager databaseManager,
        DatabaseInitializer databaseInitializer,
        PostgresUserRepository postgresUserRepository,
        PostgresUserInfoRepository postgresUserInfoRepository,
        PostgresUserSettingRepository postgresUserSettingRepository,
        PostgresFeedbackRepository postgresFeedbackRepository,
        IntegerPostgresFeedback integerPostgresFeedback
) {
}
