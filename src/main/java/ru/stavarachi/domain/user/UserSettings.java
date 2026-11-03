package ru.stavarachi.domain.user;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserSettings {
    private long chatId;
    private boolean darkTheme;
    private boolean notification;
}
