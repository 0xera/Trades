package ru.itbirds.trades.viewmodels;

import android.net.Uri;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.Query;

import androidx.lifecycle.ViewModel;
import ru.itbirds.data.model.Message;
import ru.itbirds.data.model.Sticker;
import ru.itbirds.domain.interactor.ChatInteractor;

public class ChatViewModel extends ViewModel {
    private ChatInteractor mChatInteractor;

    ChatViewModel(ChatInteractor chatInteractor) {
        mChatInteractor = chatInteractor;
    }

    public FirebaseUser getUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public FirestoreRecyclerOptions.Builder<Message> getMessages(String symbol) {
        Query query = mChatInteractor.getMessages(symbol);
        return new FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(query, Message.class);

    }

    public void sendMessage(String symbol, String message) {
        mChatInteractor.sendMessage(symbol, message);
    }

    public void sendSticker(String url, String symbol) {
        mChatInteractor.sendSticker(url, symbol);
    }

    public void editMessage(String symbol, String message, String documentId) {
        mChatInteractor.editMessage(symbol, message, documentId);

    }

    public void deleteMessage(String symbol, String documentId) {
        mChatInteractor.deleteMessage(symbol, documentId);
    }

    public FirestoreRecyclerOptions.Builder<Sticker> getStickers() {
        Query query = mChatInteractor.getStickers();
        return new FirestoreRecyclerOptions.Builder<Sticker>()
                .setQuery(query, Sticker.class);

    }

    public void uploadSticker(Uri uri) {
        mChatInteractor.uploadSticker(uri);
    }
}
