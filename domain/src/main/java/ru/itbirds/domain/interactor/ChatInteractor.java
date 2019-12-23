package ru.itbirds.domain.interactor;

import android.text.TextUtils;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.Query;

import ru.itbirds.data.repositories.ChatRepository;
import ru.itbirds.domain.usecase.ChatUseCase;

public class ChatInteractor implements ChatUseCase {
    private ChatRepository mChatRepository;

    public ChatInteractor(ChatRepository chatRepository) {
        mChatRepository = chatRepository;

    }

    public FirebaseUser getUser() {
        return mChatRepository.getUser();
    }

    public Query getMessages(String symbol) {
        return mChatRepository.getMessages(symbol);
    }

    public void sendMessage(String symbol, String message) {
        if (!TextUtils.isEmpty(message)) {
            mChatRepository.sendMessage(symbol, message);
        }
    }
}
