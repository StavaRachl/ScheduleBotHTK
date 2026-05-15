package ru.stavarachi.model;

public class User {
    private long id;
    private String group;
    private boolean darkTheme;

    public User() {}

    public User(long id, String group, boolean darkTheme) {
        this.id = id;
        this.group = group;
        this.darkTheme = darkTheme;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public boolean isDarkTheme() {
        return darkTheme;
    }

    public void setDarkTheme(boolean darkTheme) {
        this.darkTheme = darkTheme;
    }
}
