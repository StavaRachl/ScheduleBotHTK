package ru.stavarachi.domain.user;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserInfo {
    private long chatId;
    private String username;
    private String firstName;
    private String lastName;
    private boolean sponsor;
}
