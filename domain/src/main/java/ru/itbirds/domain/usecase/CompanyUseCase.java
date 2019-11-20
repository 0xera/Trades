package ru.itbirds.domain.usecase;

import androidx.lifecycle.LiveData;
import io.reactivex.Flowable;
import ru.itbirds.data.model.Company;


public interface CompanyUseCase {

    void insertCompany(Company company);

    LiveData<Company> getCompany(String ticker);

    Flowable<Company> downloadCompany(String ticker);
}

