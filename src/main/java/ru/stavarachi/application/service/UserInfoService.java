package ru.stavarachi.application.service;

import ru.stavarachi.domain.user.UserInfo;
import ru.stavarachi.port.Repository;

import java.util.List;
import java.util.Optional;

public class UserInfoService {
    private final Repository<UserInfo, Long> repository;

    public UserInfoService(Repository<UserInfo, Long> repository) {
        this.repository = repository;
    }

    public List<UserInfo> getAll() {
        return repository.findAll();
    }

    public Optional<UserInfo> getInfo(Long id) {
        return repository.findById(id);
    }

    public void saveUserInfo(UserInfo userInfo) {
        if (userInfo == null) {
            throw new NullPointerException("User info is null");
        }
        repository.save(userInfo);
    }

    public void updateUserInfo(UserInfo userInfo) {
        if (userInfo == null) {
            throw new NullPointerException("User info is null");
        }
        repository.update(userInfo);
    }
}
