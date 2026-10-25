package ru.stavarachi.domain.schedule;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Change {
    private Object count;
    private String pair;
    private String teacher;
}
