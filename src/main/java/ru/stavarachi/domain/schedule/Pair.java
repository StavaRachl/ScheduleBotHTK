package ru.stavarachi.domain.schedule;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Pair {
    private int count;
    private String pair;
    private String time;
}
