package ru.itbirds.domain.usecase;

import androidx.lifecycle.LiveData;
import ru.itbirds.data.repositories.ResetRepository;

public interface ResetUseCase {
    LiveData<ResetRepository.ResetProgress> resetPassword(String login);
}
