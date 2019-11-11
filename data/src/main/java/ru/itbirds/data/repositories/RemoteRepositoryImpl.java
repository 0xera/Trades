package ru.itbirds.data.repositories;

import java.util.List;

import io.reactivex.Single;
import ru.itbirds.data.api.IEXStockAPI;

import ru.itbirds.data.model.Company;
import com.github.tifezh.kchartlib.chart.entity.KLineEntity;

public class RemoteRepositoryImpl implements RemoteRepository {


    private final IEXStockAPI stockAPI;

    public RemoteRepositoryImpl(IEXStockAPI stockAPI) {
        this.stockAPI = stockAPI;
    }

    @Override
    public Single<Company> getCompany(String symbol) {
        return stockAPI.getCompany(symbol);
    }

    @Override
    public Single<List<KLineEntity>> getCompanyChart(String symbol) {
        return stockAPI.getCompanyChart(symbol);
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
