package ru.itbirds.trades.viewmodel_factories;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import ru.itbirds.domain.usecase.LoginUseCase;
import ru.itbirds.trades.viewmodels.LoginViewModel;


public class LoginViewModelFactory extends ViewModelProvider.NewInstanceFactory {


    @Inject
    LoginUseCase loginUseCase;

    @Inject
    LoginViewModelFactory() {
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new LoginViewModel(loginUseCase);
    }
}
