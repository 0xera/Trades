package ru.itbirds.domain.interactor;

import androidx.lifecycle.LiveData;
import ru.itbirds.data.repositories.RegRepository;
import ru.itbirds.domain.usecase.RegUseCase;

public class RegInteractor implements RegUseCase {

    private RegRepository mRegRepository;

    public RegInteractor(RegRepository regRepository) {

        mRegRepository = regRepository;
    }


    @Override
    public LiveData<RegRepository.RegProgress> createAccount(String name, String login, String password, byte[] imageBytes) {
        return mRegRepository.createAccount(name, login, password, imageBytes);
    }
}
