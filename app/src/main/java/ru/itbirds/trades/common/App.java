package ru.itbirds.trades.common;

import android.app.Application;

import ru.itbirds.trades.di.AppComponent;
import ru.itbirds.trades.di.AppModule;
import ru.itbirds.trades.di.DaggerAppComponent;
import ru.itbirds.trades.di.NetworkModule;
import ru.itbirds.trades.util.LiveConnectUtil;

public class App extends Application {

    private static AppComponent sAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        LiveConnectUtil.getInstance().init(this);
        sAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .networkModule(new NetworkModule())
                .build();
    }

    public static AppComponent getAppComponent() {
        return sAppComponent;
    }
}
