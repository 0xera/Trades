package ru.itbirds.domain.usecase;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.Query;

public interface ChatUseCase {
    FirebaseUser getUser();

    public Query getMessages(String symbol);

    public void sendMessage(String symbol, String message);

    void editMessage(String symbol, String message, String documentId);

    void deleteMessage(String symbol, String documentId);
}
