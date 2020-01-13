package ru.itbirds.data.repositories;

import com.github.tifezh.kchartlib.chart.entity.KLineEntity;

import java.util.List;

import io.reactivex.Flowable;
import ru.itbirds.data.model.Company;


public interface RemoteRepository {

    Flowable<Company> getCompany(String symbol);


    Flowable<List<KLineEntity>> getCompanyChart(String symbol);


    Flowable<List<Company>> getMostActive();


    Flowable<List<Company>> getGainers();


    Flowable<List<Company>> getLosers();

}
