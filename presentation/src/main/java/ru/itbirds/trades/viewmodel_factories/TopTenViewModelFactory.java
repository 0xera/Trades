package ru.itbirds.trades.viewmodel_factories;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import ru.itbirds.domain.usecase.CleanUseCase;
import ru.itbirds.domain.usecase.CompanyUseCase;
import ru.itbirds.trades.viewmodels.TopTenViewModel;


public class TopTenViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    @Inject
    CompanyUseCase companyUseCase;
    @Inject
    CleanUseCase cleanUseCase;


    @Inject
    TopTenViewModelFactory() {
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new TopTenViewModel(companyUseCase, cleanUseCase);
    }
}
