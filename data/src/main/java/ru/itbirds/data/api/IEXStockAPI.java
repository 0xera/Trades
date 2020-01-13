package ru.itbirds.data.api;


import com.github.tifezh.kchartlib.chart.entity.KLineEntity;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.itbirds.data.model.Company;


public interface IEXStockAPI {


    @GET("stock/{symbol}/quote")
    Flowable<Company> getCompany(@Path("symbol") String symbol, @Query("token") String KEY_API);


    @GET("stock/{symbol}/intraday-prices")
    Flowable<List<KLineEntity>> getCompanyChart(@Path("symbol") String symbol, @Query("token") String KEY_API);


    @GET("stock/market/list/mostactive")
    Flowable<List<Company>> getMostActive(@Query("token") String KEY_API);

    @GET("stock/market/list/gainers")
    Flowable<List<Company>> getGainers(@Query("token") String KEY_API);


    @GET("stock/market/list/losers")
    Flowable<List<Company>> getLosers(@Query("token") String KEY_API);

}
