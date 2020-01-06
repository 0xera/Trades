package ru.itbirds.data.repositories;

import android.net.Uri;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.Query;

public interface ChatRepository {
    FirebaseUser getUser();

    Query getMessages(String symbol);

    void sendMessage(String symbol, String message);

    void editMessage(String symbol, String message, String documentId);

    void deleteMessage(String symbol, String documentId);

    Query getStickers();

    void uploadSticker(Uri uri);

    void sendSticker(String url, String symbol);
}
