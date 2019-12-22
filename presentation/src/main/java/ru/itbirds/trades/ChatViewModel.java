package ru.itbirds.trades;

import android.text.TextUtils;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import androidx.lifecycle.ViewModel;
import ru.itbirds.data.model.Message;

public class ChatViewModel extends ViewModel {
    private FirebaseFirestore mDatabase;

    public FirebaseUser getUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public FirestoreRecyclerOptions<Message> getMessages(String symbol) {
        mDatabase = FirebaseFirestore.getInstance();
        Query query = mDatabase.collection(symbol).orderBy("date", Query.Direction.ASCENDING);
        return new FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(query, Message.class)
                .build();

    }

    void sendMessage(String symbol, String message) {
        String name = getUser().getDisplayName();
        if (!TextUtils.isEmpty(message)) {
            mDatabase.collection(symbol).add(new Message(getUser().getUid(), name, message));
        }
    }

}
