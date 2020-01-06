package ru.itbirds.domain.interactor;

import android.net.Uri;
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

    @Override
    public void editMessage(String symbol, String message, String documentId) {
        if (!TextUtils.isEmpty(message)) {
            mChatRepository.editMessage(symbol, message, documentId);
        }
    }

    @Override
    public void deleteMessage(String symbol, String documentId) {
        mChatRepository.deleteMessage(symbol, documentId);
    }

    @Override
    public Query getStickers() {
        return mChatRepository.getStickers();
    }

    @Override
    public void uploadSticker(Uri uri) {
        mChatRepository.uploadSticker(uri);
    }

    @Override
    public void sendSticker(String url, String symbol) {
        mChatRepository.sendSticker(url, symbol);
    }
}
