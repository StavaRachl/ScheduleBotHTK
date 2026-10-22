package ru.stavarachi.model;

import org.jetbrains.annotations.Contract;

public class Break {
    private String breaks;
    private String time;

    @Contract(pure = true)
    public Break(String breaks, String time) {
        this.breaks = breaks;
        this.time = time;
    }

    public String getBreaks() {
        return breaks;
    }

    public void setBreaks(String breaks) {
        this.breaks = breaks;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Break{" +
                "breaks='" + breaks + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
