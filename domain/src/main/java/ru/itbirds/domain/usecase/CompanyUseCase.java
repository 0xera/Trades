package ru.itbirds.domain.usecase;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import ru.itbirds.data.model.Company;


public interface CompanyUseCase {

    void insertCompany(Company company);

    LiveData<Company> getCompany(String ticker);

    Disposable downloadCompany(String ticker, MutableLiveData<Boolean> progress);

    Flowable<Company> downloadCompany(String ticker);
}

