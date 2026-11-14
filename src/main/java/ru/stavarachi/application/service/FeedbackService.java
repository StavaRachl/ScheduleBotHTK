package ru.stavarachi.application.service;

import ru.stavarachi.domain.feedback.Feedback;
import ru.stavarachi.domain.feedback.RequestStatus;
import ru.stavarachi.port.Repository;

import java.util.List;
import java.util.Optional;

public class FeedbackService {
    private final Repository<Feedback, Integer> repository;

    public FeedbackService(Repository<Feedback, Integer> repository) {
        this.repository = repository;
    }

    public List<Feedback> getAll() {
        return repository.findAll();
    }

    public Optional<Feedback> getFeedback(Integer id) {
        return repository.findById(id);
    }

    public void setRequestStatus(Integer id, RequestStatus status) {
        repository.findById(id)
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
