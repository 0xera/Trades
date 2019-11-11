package ru.itbirds.trades.viewmodels;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import ru.itbirds.domain.interactor.CompanyInteractor;


public class TopTenViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    @Inject
    CompanyInteractor companyInteractor;


    @Inject
    TopTenViewModelFactory() {
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new TopTenViewModel(companyInteractor);
    }
}
