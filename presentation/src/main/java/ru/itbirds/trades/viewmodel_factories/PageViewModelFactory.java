package ru.itbirds.trades.viewmodel_factories;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import ru.itbirds.domain.usecase.CompanyStockUseCase;
import ru.itbirds.trades.viewmodels.PageViewModel;


public class PageViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    @Inject
    CompanyStockUseCase companyStockUseCase;

    @Inject
    PageViewModelFactory() {
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new PageViewModel(companyStockUseCase);
    }
}
