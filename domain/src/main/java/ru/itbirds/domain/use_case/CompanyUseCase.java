package ru.itbirds.domain.use_case;

import androidx.lifecycle.LiveData;
import io.reactivex.Flowable;
import ru.itbirds.data.model.Company;


public interface CompanyUseCase {

    public void insertCompany(Company company);

    public LiveData<Company> getCompany(String ticker);

    public Flowable<Company> downloadCompany(String ticker);
}

