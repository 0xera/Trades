package ru.itbirds.domain.usecase;

import androidx.lifecycle.LiveData;
import ru.itbirds.data.repositories.RegRepository;

public interface RegUseCase {
    LiveData<RegRepository.RegProgress> createAccount(String name, String login, String password, byte[] imageBytes);
}
