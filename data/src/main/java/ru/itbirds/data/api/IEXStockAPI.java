package ru.itbirds.data.api;


import com.github.tifezh.kchartlib.chart.entity.KLineEntity;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import ru.itbirds.data.model.Company;


public interface IEXStockAPI {
    String KEY_API = "?token=pk_f7a8c71a8d404542ab7d08028ca8f208";

    @GET("stock/{symbol}/quote" + KEY_API)
    Single<Company> getCompany(@Path("symbol") String symbol);


    @GET("stock/{symbol}/intraday-prices" + KEY_API)
    Single<List<KLineEntity>> getCompanyChart(@Path("symbol") String symbol);


    @GET("stock/market/list/mostactive" + KEY_API)
    Single<List<Company>> getMostActive();

    @GET("stock/market/list/gainers" + KEY_API)
    Single<List<Company>> getGainers();


    @GET("stock/market/list/losers" + KEY_API)
    Single<List<Company>> getLosers();

}
