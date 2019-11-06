package ru.itbirds.trades.repository;

import androidx.lifecycle.LiveData;

import java.util.List;

import ru.itbirds.trades.db.TradesDao;
import ru.itbirds.trades.model.Company;
import ru.itbirds.trades.model.CompanyChart;
import ru.itbirds.trades.model.CompanyStock;
import ru.itbirds.trades.model.KLineEntity;

public class LocalRepositoryImpl implements LocalRepository {


    private final TradesDao tradesDao;

    public LocalRepositoryImpl(TradesDao tradesDao) {
        this.tradesDao = tradesDao;
    }

    @Override
    public void insertCompanyList(List<Company> companies, String type) {
        CompanyStock companyStock = new CompanyStock();
        companyStock.setCompanies(companies);
        companyStock.setType(type);
        tradesDao.insertCompanyStock(companyStock);
    }

    @Override
    public LiveData<CompanyStock> getCompanyList(String type) {
        return tradesDao.getCompanyStock(type);
    }

    @Override
    public void insertKLineEntities(List<KLineEntity> kLineEntities, String symbol) {

        CompanyChart companyChart = new CompanyChart();
        companyChart.setEntities(kLineEntities);
        companyChart.setSymbol(symbol);
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
