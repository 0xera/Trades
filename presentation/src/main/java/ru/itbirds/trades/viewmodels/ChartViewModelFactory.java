package ru.itbirds.trades.viewmodels;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import ru.itbirds.domain.interactor.CompanyChartInteractor;
import ru.itbirds.domain.interactor.CompanyInteractor;


public class ChartViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    @Inject
    CompanyChartInteractor companyChartInteractor;
    @Inject
    CompanyInteractor companyInteractor;

    @Inject
    ChartViewModelFactory() {
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ChartViewModel(companyChartInteractor, companyInteractor);
    }
}
