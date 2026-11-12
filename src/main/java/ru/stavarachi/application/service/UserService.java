package ru.stavarachi.application.service;

import ru.stavarachi.domain.user.User;
import ru.stavarachi.port.Repository;

import java.util.List;
import java.util.Optional;

public class UserService {
    private final Repository<User, Long> repository;

    public UserService(Repository<User, Long> repository) {
        this.repository = repository;
    }

    public Optional<User> getUser(Long id) {
        return repository.findById(id);
    }

    public List<User> getAllUser() {
        return repository.findAll();
    }


    public Optional<String> getDefaultGroup(Long id) {
        return repository.findById(id)
                .map(User::getGroup);
    }

    public boolean hasDefaultGroup(Long id) {
        return repository.findById(id)
                .map(User::getGroup)
                .filter(group -> group != null &&  !group.isBlank())
                .isPresent();
    }

    public void setDefaultGroup(Long id, String group) {
        repository.findById(id)
                .ifPresent(user -> {
                    user.setGroup(group);
                    repository.update(user);
                });
    }
}
