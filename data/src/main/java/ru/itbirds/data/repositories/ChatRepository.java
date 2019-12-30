package ru.itbirds.data.repositories;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.Query;

public interface ChatRepository {
    FirebaseUser getUser();

    Query getMessages(String symbol);

    void sendMessage(String symbol, String message);

    void editMessage(String symbol, String message, String documentId);

    void deleteMessage(String symbol, String documentId);
}
