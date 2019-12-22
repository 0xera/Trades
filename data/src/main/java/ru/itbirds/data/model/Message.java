package ru.itbirds.data.model;

import java.util.Date;

public class Message {
    private String id;
    private String name;
    private String text;
    private long date;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Message() {
    }

    public Message(String id, String name, String text) {
        this.id = id;
        this.name = name;
        this.text = text;
        this.date = new Date().getTime();
    }

    public Message(String id, String name, String text, long date) {
        this.id = id;
        this.name = name;
        this.text = text;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public long getDate() {
        return date;
    }

}
