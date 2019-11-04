package ru.itbirds.trades.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import ru.itbirds.trades.repository.LocalRepository;
import ru.itbirds.trades.repository.RemoteRepository;

public class ChartViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    @Inject
    RemoteRepository remoteRepository;
    @Inject
    LocalRepository localRepository;

    @Inject
    ChartViewModelFactory() {
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ChartViewModel(remoteRepository, localRepository);
    }
}
