package com.project.chat;

public class MessageItem {

    private String date, message, name, time, type;

    public MessageItem() {}

    public MessageItem(String date, String message, String name, String time, String type) {
        this.date = date;
        this.message = message;
        this.name = name;
        this.time = time;
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
