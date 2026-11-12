package ru.stavarachi.infrastructure.repository;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.sl.draw.geom.GuideIf;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ru.stavarachi.database.DatabaseManager;
import ru.stavarachi.domain.feedback.Feedback;
import ru.stavarachi.domain.feedback.RequestStatus;
import ru.stavarachi.domain.user.UserInfo;
import ru.stavarachi.port.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class PostgresFeedbackRepository implements Repository<Feedback, Long> {
    private final DatabaseManager databaseManager;

    public PostgresFeedbackRepository(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @Override
    public List<Feedback> findAll() {
        List<Feedback> feedbacks = new ArrayList<>();

        @Language("SQL")
        String sql = """
                SELECT * FROM requests
                """;

        try (Connection connection = databaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                feedbacks.add(mapRequest(resultSet));
            }

        } catch (SQLException e) {
            log.error("SQLException: {}", e, e);
            throw new RuntimeException(e);
        }

        return feedbacks;
    }

    @Override
    public Optional<Feedback> findById(Long chatId) {
        @Language("SQL")
        String sql = """
                SELECT * FROM requests WHERE chat_id = ?
                """;

        try (Connection connection = databaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setLong(1, chatId);

            ResultSet resultSet = statement.executeQuery();

            return Optional.of(mapRequest(resultSet));

        } catch (SQLException e) {
            log.error("SQLException: {}", e, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Feedback entity) {

    }

    @Override
    public void save(Feedback entity, Connection connection) {

    }

    @Override
    public void update(Feedback entity) {

    }

    @Override
    public void delete(Long chatId) {

    }

    @NotNull
    @Contract("_ -> new")
    private Feedback mapRequest(@NotNull ResultSet resultSet) throws SQLException {
        return new Feedback(
                resultSet.getInt("id"),
                resultSet.getLong("chat_id"),
                resultSet.getString("message"),
                RequestStatus.valueOf(resultSet.getString("feedback_status"))
        );
    }
}
