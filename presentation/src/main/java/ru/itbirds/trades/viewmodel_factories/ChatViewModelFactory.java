package ru.itbirds.trades.viewmodel_factories;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import ru.itbirds.domain.usecase.ChatUseCase;
import ru.itbirds.trades.viewmodels.ChatViewModel;

public class ChatViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    @Inject
    ChatUseCase chatUseCase;

    @Inject
    ChatViewModelFactory() {
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ChatViewModel(chatUseCase);
    }
}
