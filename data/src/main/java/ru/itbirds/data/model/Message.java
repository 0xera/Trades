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
    private String sticker;
    private long date;
    private String profile;
    private boolean edit;
    private String type;

    public Message() {
    }

    public Message(String id, String name, String text, String urlProfile, String urlSticker, String type) {
        this.id = id;
        this.name = name;
        this.text = text;
        this.sticker = urlSticker;
        this.date = Calendar.getInstance(TimeZone.getTimeZone("UTC+3")).getTimeInMillis();
        this.profile = urlProfile;
        this.edit = false;
        this.type = type;
        this.profile = urlProfile;

    }

    public Message(String id, String name, String text, long date, String urlProfile, String urlSticker, String type, boolean edit) {
        this.id = id;
        this.name = name;
        this.text = text;
        this.date = date;
        this.sticker = urlSticker;
        this.profile = urlProfile;
        this.edit = edit;
        this.type = type;
    }

    public String getDocumentId() {
        return documentId;
    }

    public String getId() {
        return id;
    }

    public String getProfile() {
        return profile;
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

    public String getSticker() {
        return sticker;
    }

    public void setSticker(String sticker) {
        this.sticker = sticker;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
