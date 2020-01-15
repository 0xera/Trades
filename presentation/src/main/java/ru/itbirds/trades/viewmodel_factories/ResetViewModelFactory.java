package ru.itbirds.trades.viewmodel_factories;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import ru.itbirds.domain.usecase.ResetUseCase;
import ru.itbirds.trades.viewmodels.ResetViewModel;


public class ResetViewModelFactory extends ViewModelProvider.NewInstanceFactory {


    @Inject
     ResetUseCase resetUseCase;

    @Inject
    ResetViewModelFactory() {
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ResetViewModel(resetUseCase);
    }
}
