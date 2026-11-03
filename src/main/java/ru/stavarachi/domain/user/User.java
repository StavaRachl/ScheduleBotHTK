package ru.stavarachi.domain.user;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {
    private long chatId;
    private String group;
}
