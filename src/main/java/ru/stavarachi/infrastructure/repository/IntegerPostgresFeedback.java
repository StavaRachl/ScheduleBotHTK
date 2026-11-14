package ru.stavarachi.infrastructure.repository;

import lombok.extern.slf4j.Slf4j;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ru.stavarachi.database.DatabaseManager;
import ru.stavarachi.domain.feedback.Feedback;
import ru.stavarachi.domain.feedback.RequestStatus;
import ru.stavarachi.port.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class IntegerPostgresFeedback implements Repository<Feedback, Integer> {
    private final DatabaseManager databaseManager;

    public IntegerPostgresFeedback(DatabaseManager databaseManager) {
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
    public Optional<Feedback> findById(Integer id) {
        @Language("SQL")
        String sql = """
                SELECT * FROM requests WHERE id = ?
                """;

        try (Connection connection = databaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery();) {
                if (resultSet.next()) {
                    return Optional.of(mapRequest(resultSet));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            log.error("SQLException: {}", e, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Feedback entity) {
        @Language("SQL")
        String sql = "INSERT INTO requests (chat_id, message)  VALUES (?, ?) RETURNING id";
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, entity.getChatId());
            statement.setString(2, entity.getMessage());

            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("SQLException: {}", e, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Feedback entity, Connection connection) {

    }

    @Override
    public void update(Feedback entity) {
        @Language("SQL")
        String sql = """
                UPDATE requests
                SET feedback_status = ?
                WHERE id = ?
                """;
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, entity.getRequestStatus());
            statement.setInt(2, entity.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("SQLException: {}", e, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Integer chatId) {

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
