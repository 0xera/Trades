package ru.itbirds.trades.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import ru.itbirds.trades.repository.RemoteRepository;

public class TopTenViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    @Inject
    RemoteRepository remoteRepository;


    @Inject
    TopTenViewModelFactory() {
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new TopTenViewModel(remoteRepository);
    }
}
