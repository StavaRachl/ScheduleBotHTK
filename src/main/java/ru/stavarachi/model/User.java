package ru.stavarachi.model;

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
