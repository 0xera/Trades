package ru.itbirds.trades.api;


import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import ru.itbirds.trades.model.Company;
import ru.itbirds.trades.model.KLineEntity;

public interface IEXStockAPI {
    String KEY_API = "?token=pk_8e6b4710f22e4beea9934058de9384f2";

    @GET("stock/{ticker}/quote" + KEY_API)
    Single<Company> getCompany(@Path("ticker") String company);


    @GET("stock/{ticker}/intraday-prices" + KEY_API)
    Single<List<KLineEntity>> getCompanyChart(@Path("ticker") String company);


    @GET("stock/market/list/mostactive" + KEY_API)
    Single<List<Company>> getMostActive();

    @GET("stock/market/list/gainers" + KEY_API)
    Single<List<Company>> getGainers();


    @GET("stock/market/list/losers" + KEY_API)
    Single<List<Company>> getLosers();

}
