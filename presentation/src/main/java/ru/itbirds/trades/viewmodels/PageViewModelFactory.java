package ru.itbirds.trades.viewmodels;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import ru.itbirds.domain.interactor.CompanyStockInteractor;


public class PageViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    @Inject
    CompanyStockInteractor companyStockInteractor;

    @Inject
    PageViewModelFactory() {
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new PageViewModel(companyStockInteractor);
    }
}
