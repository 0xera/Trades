package ru.itbirds.trades.api;


import android.database.Observable;

import com.google.gson.JsonObject;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import ru.itbirds.trades.model.Company;
import ru.itbirds.trades.model.KLineEntity;

public interface IEXStockAPI {
    String KEY_API = "?token=pk_60795a66efdd4d1fbccab24ff8fdd7be";

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

//    @GET("stock/{ticker}/logo" + KEY_API)
//    Single<JsonObject> getCompanyImg(@Path("ticker") String company);
}
