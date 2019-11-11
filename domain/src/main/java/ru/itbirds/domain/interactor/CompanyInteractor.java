package ru.itbirds.domain.interactor;


import java.util.concurrent.TimeUnit;

import androidx.lifecycle.LiveData;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

import ru.itbirds.data.model.Company;
import ru.itbirds.data.repositories.LocalRepository;
import ru.itbirds.data.repositories.RemoteRepository;
import ru.itbirds.domain.use_case.CompanyUseCase;

public class CompanyInteractor implements CompanyUseCase {
    private LocalRepository mLocalRepository;
    private RemoteRepository mRemoteRepository;

    public CompanyInteractor(LocalRepository localRepository, RemoteRepository remoteRepository) {

        mLocalRepository = localRepository;
        mRemoteRepository = remoteRepository;
    }

    @Override
    public void insertCompany(Company company) {
        mLocalRepository.insertCompany(company);
    }

    @Override
    public LiveData<Company> getCompany(String symbol) {
        return mLocalRepository.getCompany(symbol);
    }

    @Override
    public Flowable<Company> downloadCompany(String symbol) {
        return mRemoteRepository.getCompany(symbol)
                .repeatWhen(objectFlowable -> objectFlowable.delay(2, TimeUnit.MINUTES))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());

    }


}
