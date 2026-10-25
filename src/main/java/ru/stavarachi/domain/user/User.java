package ru.stavarachi.domain.user;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {
    private long id;
    private String group;
    private boolean darkTheme;
}
