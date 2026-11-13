package ru.stavarachi.port;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface Repository<T, ID> {
    List<T> findAll();

    Optional<T> findById(ID chatId);

    void save(T entity);

    void save(T entity, Connection connection);

    void update(T entity);

    void delete(ID chatId);
}
