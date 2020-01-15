package ru.itbirds.domain.interactor;

import androidx.lifecycle.LiveData;
import ru.itbirds.data.repositories.LoginRepository;
import ru.itbirds.domain.usecase.LoginUseCase;

public class LoginInteractor implements LoginUseCase {

    private LoginRepository mLoginRepository;

    public LoginInteractor(LoginRepository loginRepository) {

        mLoginRepository = loginRepository;
    }


    @Override
    public LiveData<LoginRepository.AuthProgress> loginAccount(String login, String password) {
        return mLoginRepository.loginAccount(login, password);
    }
}
