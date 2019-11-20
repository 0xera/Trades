package ru.itbirds.domain.interactor;

import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.lifecycle.LiveData;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import ru.itbirds.data.model.Company;
import ru.itbirds.data.model.CompanyStock;
import ru.itbirds.data.repositories.LocalRepository;
import ru.itbirds.data.repositories.RemoteRepository;
import ru.itbirds.domain.usecase.CompanyStockUseCase;

public class CompanyStockInteractor implements CompanyStockUseCase {
    private LocalRepository mLocalRepository;
    private RemoteRepository mRemoteRepository;

    public CompanyStockInteractor(LocalRepository localRepository, RemoteRepository remoteRepository) {

        mLocalRepository = localRepository;
        mRemoteRepository = remoteRepository;
    }

    @Override
    public void insertCompanyStock(List<Company> companies, String type) {
        CompanyStock companyStock = new CompanyStock();
        companyStock.setCompanies(companies);
        companyStock.setType(type);
        mLocalRepository.insertCompanyStock(companyStock);
    }

    @Override
    public LiveData<CompanyStock> getCompanyStock(String type) {
        return mLocalRepository.getCompanyStock(type);
    }

    @Override
    public Flowable<List<Company>> getMostActive() {
        return mRemoteRepository.getMostActive()
                .subscribeOn(Schedulers.io())
                .repeatWhen(objectFlowable -> objectFlowable.delay(2, TimeUnit.SECONDS))
                .observeOn(Schedulers.io());
    }

    @Override
    public Flowable<List<Company>> getGainers() {
        return mRemoteRepository.getGainers()
                .subscribeOn(Schedulers.io())
                .repeatWhen(objectFlowable -> objectFlowable.delay(2, TimeUnit.SECONDS))
                .observeOn(Schedulers.io());
    }

    @Override
    public Flowable<List<Company>> getLosers() {
        return mRemoteRepository.getLosers()
                .subscribeOn(Schedulers.io())
                .repeatWhen(objectFlowable -> objectFlowable.delay(2, TimeUnit.SECONDS))
                .observeOn(Schedulers.io());
    }


}
