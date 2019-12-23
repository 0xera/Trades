package ru.itbirds.trades.viewmodels;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import ru.itbirds.domain.interactor.ChatInteractor;

public class ChatViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    @Inject
    ChatInteractor chatInteractor;

    @Inject
    ChatViewModelFactory() {
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ChatViewModel(chatInteractor);
    }
}
