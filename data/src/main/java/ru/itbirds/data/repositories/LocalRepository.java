package ru.itbirds.data.repositories;


import androidx.lifecycle.LiveData;
import ru.itbirds.data.model.Company;
import ru.itbirds.data.model.CompanyChart;
import ru.itbirds.data.model.CompanyStock;


public interface LocalRepository {
    void insertCompanyStock(CompanyStock companyStock);

    LiveData<CompanyStock> getCompanyStock(String type);

    void insertKLineEntities(CompanyChart companyChart);

    CompanyChart getFirstCompanyChart();

    LiveData<CompanyChart> getKLineEntities(String symbol);

    void insertCompany(Company company);

    LiveData<Company> getCompany(String symbol);

    void deleteChartsAndCompanies();
}
