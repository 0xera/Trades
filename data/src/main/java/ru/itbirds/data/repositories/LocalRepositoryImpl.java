package ru.itbirds.data.repositories;

import androidx.lifecycle.LiveData;
import io.reactivex.Flowable;
import ru.itbirds.data.db.TradesDao;
import ru.itbirds.data.model.Company;
import ru.itbirds.data.model.CompanyChart;
import ru.itbirds.data.model.CompanyStock;


public class LocalRepositoryImpl implements LocalRepository {


    private final TradesDao tradesDao;

    public LocalRepositoryImpl(TradesDao tradesDao) {
        this.tradesDao = tradesDao;
    }

    @Override
    public void insertCompanyStock(CompanyStock companyStock) {
        tradesDao.insertCompanyStock(companyStock);
    }

    @Override
    public LiveData<CompanyStock> getCompanyStock(String type) {
        return tradesDao.getCompanyStock(type);
    }

    @Override
    public void insertKLineEntities(CompanyChart companyChart) {
        tradesDao.insertCompanyChart(companyChart);
    }

    @Override
    public LiveData<CompanyChart> getKLineEntities(String symbol) {
        return tradesDao.getCompanyChart(symbol);
    }

    @Override
    public void insertCompany(Company company) {
        tradesDao.insertCompany(company);
    }

    @Override
    public LiveData<Company> getCompany(String symbol) {
        return tradesDao.getCompany(symbol);
    }

}
