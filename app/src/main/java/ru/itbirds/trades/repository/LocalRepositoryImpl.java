package ru.itbirds.trades.repository;

import androidx.lifecycle.LiveData;

import java.util.List;

import io.fabric.sdk.android.services.concurrency.AsyncTask;
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
        new InsertCompanyListAsyncTask(tradesDao, type).execute(companies);
    }

    @Override
    public LiveData<CompanyStock> getCompanyList(String type) {
        return tradesDao.getCompanyStock(type);
    }

    @Override
    public void insertKLineEntities(List<KLineEntity> kLineEntities, String symbol) {

        new InsertKLineEntitiesAsyncTask(tradesDao, symbol).execute(kLineEntities);
    }

    @Override
    public LiveData<CompanyChart> getKLineEntities(String symbol) {
        return tradesDao.getCompanyChart(symbol);
    }

    private static class InsertCompanyListAsyncTask extends AsyncTask<List<Company>, Void, Void> {

        private final TradesDao tradesDao;
        private final String type;

        InsertCompanyListAsyncTask(TradesDao tradesDao, String type) {
            this.tradesDao = tradesDao;
            this.type = type;
        }


        @Override
        protected Void doInBackground(List<Company>... companies) {
            CompanyStock companyStock = new CompanyStock();
            companyStock.setCompanies(companies[0]);
            companyStock.setType(type);
            tradesDao.insertCompanyStock(companyStock);
            return null;
        }

    }


    private static class InsertKLineEntitiesAsyncTask extends AsyncTask<List<KLineEntity>, Void, Void> {

        private final TradesDao tradesDao;
        private final String symbol;

        InsertKLineEntitiesAsyncTask(TradesDao tradesDao, String symbol) {
            this.tradesDao = tradesDao;
            this.symbol = symbol;
        }


        @Override
        protected Void doInBackground(List<KLineEntity>... kLineEntities) {
            CompanyChart companyChart = new CompanyChart();
            companyChart.setEntities(kLineEntities[0]);
            companyChart.setSymbol(symbol);
            tradesDao.insertCompanyChart(companyChart);
            return null;
        }
    }

}
