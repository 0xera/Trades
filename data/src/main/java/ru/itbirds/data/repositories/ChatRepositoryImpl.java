package ru.itbirds.data.repositories;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import ru.itbirds.data.model.Message;

import static ru.itbirds.data.Constants.DATE_SORT_FIELD;

public class ChatRepositoryImpl implements ChatRepository {
    private FirebaseFirestore mDatabase;
    private FirebaseAuth mFirebaseAuth;

    public ChatRepositoryImpl() {
        mDatabase = FirebaseFirestore.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    public FirebaseUser getUser() {
        return mFirebaseAuth.getCurrentUser();
    }

    public Query getMessages(String symbol) {
        return mDatabase.collection(symbol).orderBy(DATE_SORT_FIELD, Query.Direction.ASCENDING);

    }

    public void sendMessage(String symbol, String message) {
        String name = getUser().getDisplayName();
        mDatabase.collection(symbol).add(new Message(getUser().getUid(), name, message));

    }
}
