package ru.stavarachi.model;

public class Pair {
    private int count;
    private String pair;
    private String time;

    public Pair(int count, String pair, String time) {
        this.count = count;
        this.pair = pair;
        this.time = time;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getPair() {
        return pair;
    }

    public void setPair(String pair) {
        this.pair = pair;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
