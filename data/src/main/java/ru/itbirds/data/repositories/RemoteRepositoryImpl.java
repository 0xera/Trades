package ru.itbirds.data.repositories;

import com.github.tifezh.kchartlib.chart.entity.KLineEntity;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.List;

import io.reactivex.Single;
import ru.itbirds.data.api.IEXStockAPI;
import ru.itbirds.data.model.Company;
import ru.itbirds.date.R;

import static ru.itbirds.data.Constants.API_KEY;

public class RemoteRepositoryImpl implements RemoteRepository {

    private String KEY_API;
    private final IEXStockAPI stockAPI;

    public RemoteRepositoryImpl(IEXStockAPI stockAPI) {

        this.stockAPI = stockAPI;
        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(0)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.configs);
        KEY_API = mFirebaseRemoteConfig.getString(API_KEY);
        mFirebaseRemoteConfig.fetchAndActivate();
    }

    @Override
    public Single<Company> getCompany(String symbol) {
        return stockAPI.getCompany(symbol, KEY_API);
    }

    @Override
    public Single<List<KLineEntity>> getCompanyChart(String symbol) {
        return stockAPI.getCompanyChart(symbol, KEY_API);
    }

    @Override
    public Single<List<Company>> getMostActive() {
        return stockAPI.getMostActive(KEY_API);
    }

    @Override
    public Single<List<Company>> getGainers() {
        return stockAPI.getGainers(KEY_API);
    }

    @Override
    public Single<List<Company>> getLosers() {
        return stockAPI.getLosers(KEY_API);
    }
}
