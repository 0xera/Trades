package ru.itbirds.trades.di;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.itbirds.data.api.IEXStockAPI;


@Module
public class NetworkModule {
    private static final String BASE_URL = "https://cloud.iexapis.com/stable/";

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder().addInterceptor(new HttpLoggingInterceptor()).build();


    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new Gson();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient client, Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    public IEXStockAPI provideApi(Retrofit retrofit) {
        return retrofit.create(IEXStockAPI.class);

    }

    @Provides
    @Singleton
    public FirebaseAuth provideAuth() {
        return FirebaseAuth.getInstance();

    }

    @Provides
    @Singleton
    public FirebaseStorage provideStorage() {
        return FirebaseStorage.getInstance();

    }

}
