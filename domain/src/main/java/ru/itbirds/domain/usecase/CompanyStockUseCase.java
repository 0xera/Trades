package ru.itbirds.domain.usecase;

import java.util.List;

import androidx.lifecycle.LiveData;
import io.reactivex.Flowable;
import ru.itbirds.data.model.Company;
import ru.itbirds.data.model.CompanyStock;


public interface CompanyStockUseCase {
    void insertCompanyStock(List<Company> companies, String type);


    LiveData<CompanyStock> getCompanyStock(String type);

    Flowable<List<Company>> getMostActive();

    Flowable<List<Company>> getGainers();

    Flowable<List<Company>> getLosers();
}
