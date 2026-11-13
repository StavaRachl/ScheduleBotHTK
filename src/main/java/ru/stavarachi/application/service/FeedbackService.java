package ru.stavarachi.application.service;

import ru.stavarachi.domain.feedback.Feedback;
import ru.stavarachi.domain.feedback.RequestStatus;
import ru.stavarachi.port.Repository;

import java.util.List;
import java.util.Optional;

public class FeedbackService {
    private final Repository<Feedback, Long> repository;

    public FeedbackService(Repository<Feedback, Long> repository) {
        this.repository = repository;
    }

    public List<Feedback> getAll() {
        return repository.findAll();
    }

    public Optional<Feedback> getFeedback(Long chatId) {
        return repository.findById(chatId);
    }

    public void setRequestStatus(Long chatId, RequestStatus status) {
        repository.findById(chatId)
                .ifPresent(feedback -> {
                    feedback.setRequestStatus(status);
                    repository.update(feedback);
                });
    }

    public void save(Feedback feedback) {
        if (feedback == null) {
            throw new NullPointerException("feedback is null");
        }
        repository.save(feedback);
    }
}
