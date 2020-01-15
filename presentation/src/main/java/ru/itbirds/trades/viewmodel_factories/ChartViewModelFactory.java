package ru.itbirds.trades.viewmodel_factories;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import ru.itbirds.domain.usecase.CompanyChartUseCase;
import ru.itbirds.domain.usecase.CompanyUseCase;
import ru.itbirds.trades.viewmodels.ChartViewModel;


public class ChartViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    @Inject
    CompanyChartUseCase companyChartUseCase;
    @Inject
    CompanyUseCase companyUseCase;

    @Inject
    ChartViewModelFactory() {
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ChartViewModel(companyChartUseCase, companyUseCase);
    }
}
