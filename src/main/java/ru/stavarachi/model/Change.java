package ru.stavarachi.model;

import org.jetbrains.annotations.Contract;

public class Change {
    private Object count;
    private String pair;
    private String teacher;

    @Contract(pure = true)
    public Change(Object count, String pair, String teacher) {
        this.count = count;
        this.pair = pair;
        this.teacher = teacher;
    }

    public Object getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getPair() {
        return pair;
    }

    public void setPair(String pair) {
        this.pair = pair;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    @Override
    public String toString() {
        return "Change{" +
                "count=" + count +
                ", pair='" + pair + '\'' +
                ", teacher='" + teacher + '\'' +
                '}';
    }
}
