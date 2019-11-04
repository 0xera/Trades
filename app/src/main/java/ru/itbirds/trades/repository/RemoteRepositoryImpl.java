package ru.itbirds.trades.repository;

import java.util.List;

import io.reactivex.Single;
import ru.itbirds.trades.api.IEXStockAPI;
import ru.itbirds.trades.model.Company;
import ru.itbirds.trades.model.KLineEntity;

public class RemoteRepositoryImpl implements RemoteRepository {


    private final IEXStockAPI stockAPI;

    public RemoteRepositoryImpl(IEXStockAPI stockAPI) {
        this.stockAPI = stockAPI;
    }

    @Override
    public Single<Company> getCompany(String company) {
        return stockAPI.getCompany(company);
    }

    @Override
    public Single<List<KLineEntity>> getCompanyChart(String company) {
        return stockAPI.getCompanyChart(company);
    }

    @Override
    public Single<List<Company>> getMostActive() {
        return stockAPI.getMostActive();
    }

    @Override
    public Single<List<Company>> getGainers() {
        return stockAPI.getGainers();
    }

    @Override
    public Single<List<Company>> getLosers() {
        return stockAPI.getLosers();
    }
}
