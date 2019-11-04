package ru.itbirds.trades.repository;

import androidx.lifecycle.LiveData;

import java.util.List;

import ru.itbirds.trades.model.Company;
import ru.itbirds.trades.model.CompanyChart;
import ru.itbirds.trades.model.CompanyStock;
import ru.itbirds.trades.model.KLineEntity;

public interface LocalRepository {
    void insertCompanyList(List<Company> companies, String type);

    LiveData<CompanyStock> getCompanyList(String type);

    void insertKLineEntities(List<KLineEntity> kLineEntities, String symbol);

    LiveData<CompanyChart> getKLineEntities(String symbol);

    void insertCompany(Company company);

    LiveData<Company> getCompany(String symbol);

}
