package ru.stavarachi.domain.feedback;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class Feedback {
    private int id;
    private final Long chatId;
    private final String message;
    private RequestStatus requestStatus;
}
