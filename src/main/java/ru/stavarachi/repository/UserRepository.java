package ru.stavarachi.repository;

import java.util.List;
import java.util.Optional;

public interface UserRepository<T, ID> {
    List<T> findAll();

    Optional<T> findById(ID id);

    void save(T t);

    void update(T t);

    void delete(ID id);
}
