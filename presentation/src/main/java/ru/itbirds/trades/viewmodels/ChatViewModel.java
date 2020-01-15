package ru.itbirds.trades.viewmodels;

import android.net.Uri;

import com.firebase.ui.common.ChangeEventType;
import com.firebase.ui.firestore.ChangeEventListener;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.ObservableSnapshotArray;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.lifecycle.ViewModel;
import ru.itbirds.data.model.Message;
import ru.itbirds.data.model.Sticker;
import ru.itbirds.domain.usecase.ChatUseCase;

public class ChatViewModel extends ViewModel implements DefaultLifecycleObserver {
    private ChatUseCase mChatUseCase;
    private ObservableSnapshotArray<Message> mMessagesSnapshot;
    private ChangeEventListener mChangeEventListener;

    public ChatViewModel(ChatUseCase chatUseCase) {
        mChatUseCase = chatUseCase;
    }

    public FirebaseUser getUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public FirestoreRecyclerOptions.Builder<Message> getMessages(String symbol) {
        Query query = mChatUseCase.getMessages(symbol);
        return new FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(query, Message.class);

    }

    public void initSnapshot(ObservableSnapshotArray<Message> snapshotArray) {
        mMessagesSnapshot = snapshotArray;
        mChangeEventListener = new ChangeEventListener() {
            @Override
            public void onChildChanged(@NonNull ChangeEventType type, @NonNull DocumentSnapshot snapshot, int newIndex, int oldIndex) {

            }

            @Override
            public void onDataChanged() {

            }

            @Override
            public void onError(@NonNull FirebaseFirestoreException e) {

            }
        };
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        mMessagesSnapshot.addChangeEventListener(mChangeEventListener);
    }

    @Override
    public void onStop(@NonNull LifecycleOwner owner) {
        mMessagesSnapshot.removeChangeEventListener(mChangeEventListener);
    }

    public void sendMessage(String symbol, String message) {
        mChatUseCase.sendMessage(symbol, message);
    }

    public void sendSticker(String url, String symbol) {
        mChatUseCase.sendSticker(url, symbol);
    }

    public void editMessage(String symbol, String message, String documentId) {
        mChatUseCase.editMessage(symbol, message, documentId);

    }

    public void deleteMessage(String symbol, String documentId) {
        mChatUseCase.deleteMessage(symbol, documentId);
    }

    public FirestoreRecyclerOptions.Builder<Sticker> getStickers() {
        Query query = mChatUseCase.getStickers();
        return new FirestoreRecyclerOptions.Builder<Sticker>()
                .setQuery(query, Sticker.class);

    }

    public void uploadSticker(Uri uri) {
        mChatUseCase.uploadSticker(uri);
    }


    @Override
    protected void onCleared() {
        ProcessLifecycleOwner.get().getLifecycle().removeObserver(this);
        onStop(ProcessLifecycleOwner.get());
    }

}
