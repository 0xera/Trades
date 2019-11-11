package ru.itbirds.domain.use_case;

import java.util.List;

import androidx.lifecycle.LiveData;
import io.reactivex.Flowable;
import ru.itbirds.data.model.Company;
import ru.itbirds.data.model.CompanyStock;


public interface CompanyStockUseCase {
    void insertCompanyStock(List<Company> companies, String type);


    public LiveData<CompanyStock> getCompanyStock(String type);

    public Flowable<List<Company>> getMostActive();

    public Flowable<List<Company>> getGainers();

    public Flowable<List<Company>> getLosers();
}
