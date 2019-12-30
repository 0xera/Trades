package ru.itbirds.data.model;

import com.google.firebase.firestore.DocumentId;

import java.util.Calendar;
import java.util.TimeZone;

public class Message {
    @DocumentId
    private String documentId;

    private String id;
    private String name;
    private String text;
    private long date;
    private String uri;
    private boolean edit;

    public Message() {
    }

    public Message(String id, String name, String text, String uri) {
        this.id = id;
        this.name = name;
        this.text = text;
        this.date = Calendar.getInstance(TimeZone.getTimeZone("UTC+3")).getTimeInMillis();
        this.uri = uri;
        this.edit = false;

    }

    public Message(String id, String name, String text, long date, String uri, boolean edit) {
        this.id = id;
        this.name = name;
        this.text = text;
        this.date = date;
        this.uri = uri;
        this.edit = edit;
    }

    public String getDocumentId() {
        return documentId;
    }

    public String getId() {
        return id;
    }

    public String getUri() {
        return uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public long getDate() {
        return date;
    }

    public boolean isEdit() {
        return edit;
    }
}
