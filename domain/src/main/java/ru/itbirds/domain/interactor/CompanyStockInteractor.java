package ru.itbirds.domain.interactor;

import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.itbirds.data.model.Company;
import ru.itbirds.data.model.CompanyStock;
import ru.itbirds.data.repositories.LocalRepository;
import ru.itbirds.data.repositories.RemoteRepository;
import ru.itbirds.domain.usecase.CompanyStockUseCase;

import static ru.itbirds.data.Constants.ACTIVE_FR;
import static ru.itbirds.data.Constants.GAINERS_FR;
import static ru.itbirds.data.Constants.LOSERS_FR;

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
    public Disposable getMostActive(MutableLiveData<Boolean> progress, MutableLiveData<Boolean> noInternet) {
        return mRemoteRepository.getMostActive().subscribeOn(Schedulers.io())
                .repeatWhen(objectFlowable -> objectFlowable.delay(2, TimeUnit.SECONDS))
                .observeOn(Schedulers.io())
                .subscribe(companies -> {
                            insertCompanyStock(companies, ACTIVE_FR);
                            progress.postValue(false);
                        },
                        throwable -> {
                            progress.postValue(false);
                            noInternet.postValue(true);
                        });
    }

    @Override
    public Disposable getGainers(MutableLiveData<Boolean> progress, MutableLiveData<Boolean> noInternet) {
        return mRemoteRepository.getGainers()
                .subscribeOn(Schedulers.io())
                .repeatWhen(objectFlowable -> objectFlowable.delay(2, TimeUnit.SECONDS))
                .observeOn(Schedulers.io())
                .subscribe(companies -> {
                            insertCompanyStock(companies, GAINERS_FR);
                            progress.postValue(false);
                        },
                        throwable -> {
                            progress.postValue(false);
                            noInternet.postValue(true);
                        });
    }

    @Override
    public Disposable getLosers(MutableLiveData<Boolean> progress, MutableLiveData<Boolean> noInternet) {
        return mRemoteRepository.getLosers()
                .subscribeOn(Schedulers.io())
                .repeatWhen(objectFlowable -> objectFlowable.delay(2, TimeUnit.SECONDS))
                .observeOn(Schedulers.io())
                .subscribe(companies -> {
                            insertCompanyStock(companies, LOSERS_FR);
                            progress.postValue(false);
                        },
                        throwable -> {
                            progress.postValue(false);
                            noInternet.postValue(true);
                        });
    }


}
