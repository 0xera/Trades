package ru.itbirds.domain.usecase;

import androidx.lifecycle.LiveData;
import ru.itbirds.data.repositories.LoginRepository;

public interface LoginUseCase {
     LiveData<LoginRepository.AuthProgress> loginAccount(String login, String password);
}
