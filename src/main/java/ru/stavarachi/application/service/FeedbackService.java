package ru.stavarachi.application.service;

import ru.stavarachi.domain.feedback.Feedback;
import ru.stavarachi.domain.feedback.RequestStatus;
import ru.stavarachi.port.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FeedbackService {
    private final Repository<Feedback, UUID> repository;

    public FeedbackService(Repository<Feedback, UUID> repository) {
        this.repository = repository;
    }

    public List<Feedback> getAll() {
        return repository.findAll();
    }

    public Optional<Feedback> getFeedback(UUID id) {
        return repository.findById(id);
    }

    public void setRequestStatus(UUID id, RequestStatus status) {
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

    public void getFeedbackId() {

    }
}
