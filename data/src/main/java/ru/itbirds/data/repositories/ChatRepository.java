package ru.itbirds.data.repositories;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.Query;

public interface ChatRepository {
    FirebaseUser getUser();

    public Query getMessages(String symbol);

    public void sendMessage(String symbol, String message);
}
