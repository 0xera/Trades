package ru.itbirds.data.repositories;

import android.util.Log;

import com.github.tifezh.kchartlib.chart.entity.KLineEntity;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.List;

import io.reactivex.Flowable;
import ru.itbirds.data.api.IEXStockAPI;
import ru.itbirds.data.model.Company;

import static android.content.ContentValues.TAG;
import static ru.itbirds.data.Constants.API_KEY;

public class RemoteRepositoryImpl implements RemoteRepository {

    private String KEY_API;

    public RemoteRepositoryImpl(IEXStockAPI stockAPI) {
        this.stockAPI = stockAPI;
        KEY_API = FirebaseRemoteConfig.getInstance().getString(API_KEY);
    }

    private final IEXStockAPI stockAPI;

    @Override
    public Flowable<Company> getCompany(String symbol) {
        return stockAPI.getCompany(symbol, KEY_API);
    }

    @Override
    public Flowable<List<KLineEntity>> getCompanyChart(String symbol) {
        return stockAPI.getCompanyChart(symbol, KEY_API);
    }

    @Override
    public Flowable<List<Company>> getMostActive() {
        Log.d(TAG, "getMostActive() called" + KEY_API);
        return stockAPI.getMostActive(KEY_API);
    }

    @Override
    public Flowable<List<Company>> getGainers() {
        Log.d(TAG, "getGainers() called" + KEY_API);
        return stockAPI.getGainers(KEY_API);
    }

    @Override
    public Flowable<List<Company>> getLosers() {
        Log.d(TAG, "getLosers() called" + KEY_API);
        return stockAPI.getLosers(KEY_API);
    }
}
