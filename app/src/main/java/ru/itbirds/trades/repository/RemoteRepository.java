package ru.itbirds.trades.repository;

import java.util.List;

import io.reactivex.Single;
import ru.itbirds.trades.model.Company;
import ru.itbirds.trades.model.KLineEntity;

public interface RemoteRepository {

    Single<Company> getCompany(String company);


    Single<List<KLineEntity>> getCompanyChart(String company);


    Single<List<Company>> getMostActive();


    Single<List<Company>> getGainers();


    Single<List<Company>> getLosers();

}
