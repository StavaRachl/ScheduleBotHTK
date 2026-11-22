package ru.stavarachi.domain.feedback;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class Feedback {
    private UUID id;
    private final Long chatId;
    private final String message;
    private RequestStatus requestStatus;
}
