package ru.itbirds.domain.interactor;

import androidx.lifecycle.LiveData;
import ru.itbirds.data.repositories.ResetRepository;
import ru.itbirds.domain.usecase.ResetUseCase;

public class ResetInteractor implements ResetUseCase {

    private ResetRepository mResetRepository;

    public ResetInteractor(ResetRepository resetRepository) {

        mResetRepository = resetRepository;
    }

    @Override
    public LiveData<ResetRepository.ResetProgress> resetPassword(String login) {
        return mResetRepository.resetPassword(login);
    }
}
