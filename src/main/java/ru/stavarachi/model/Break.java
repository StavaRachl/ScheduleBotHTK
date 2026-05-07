package ru.stavarachi.model;

public class Break {
    private String breaks;
    private String time;

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
}
