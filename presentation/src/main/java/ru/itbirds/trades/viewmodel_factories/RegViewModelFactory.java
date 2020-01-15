package ru.itbirds.trades.viewmodel_factories;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import ru.itbirds.domain.usecase.RegUseCase;
import ru.itbirds.trades.viewmodels.RegViewModel;


public class RegViewModelFactory extends ViewModelProvider.NewInstanceFactory {


    @Inject
    RegUseCase regUseCase;

    @Inject
    RegViewModelFactory() {
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new RegViewModel(regUseCase);
    }
}
