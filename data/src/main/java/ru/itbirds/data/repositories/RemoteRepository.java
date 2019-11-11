package ru.itbirds.data.repositories;

import com.github.tifezh.kchartlib.chart.entity.KLineEntity;

import java.util.List;

import io.reactivex.Single;
import ru.itbirds.data.model.Company;


public interface RemoteRepository {

    Single<Company> getCompany(String symbol);


    Single<List<KLineEntity>> getCompanyChart(String symbol);


    Single<List<Company>> getMostActive();


    Single<List<Company>> getGainers();


    Single<List<Company>> getLosers();

}
