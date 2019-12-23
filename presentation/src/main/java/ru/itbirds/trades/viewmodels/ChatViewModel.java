package ru.itbirds.trades.viewmodels;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.Query;

import androidx.lifecycle.ViewModel;
import ru.itbirds.data.model.Message;
import ru.itbirds.domain.interactor.ChatInteractor;

public class ChatViewModel extends ViewModel {
    private ChatInteractor mChatInteractor;

    ChatViewModel(ChatInteractor chatInteractor) {
        mChatInteractor = chatInteractor;
    }
    public FirebaseUser getUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public FirestoreRecyclerOptions<Message> getMessages(String symbol) {
        Query query = mChatInteractor.getMessages(symbol);
        return new FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(query, Message.class)
                .build();

    }

    public void sendMessage(String symbol, String message) {
        mChatInteractor.sendMessage(symbol, message);
    }

}
