package ru.itbirds.domain.usecase;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.disposables.Disposable;
import ru.itbirds.data.model.Company;
import ru.itbirds.data.model.CompanyStock;


public interface CompanyStockUseCase {
    void insertCompanyStock(List<Company> companies, String type);


    LiveData<CompanyStock> getCompanyStock(String type);

    Disposable getMostActive(MutableLiveData<Boolean> progress, MutableLiveData<Boolean> noInternet);

    Disposable getGainers(MutableLiveData<Boolean> progress, MutableLiveData<Boolean> booleanMutableLiveData);

    Disposable getLosers(MutableLiveData<Boolean> progress, MutableLiveData<Boolean> noInternet);
}
