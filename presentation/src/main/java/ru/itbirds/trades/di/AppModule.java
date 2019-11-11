package ru.itbirds.trades.di;

import androidx.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.itbirds.data.db.DataBase;
import ru.itbirds.data.db.TradesDao;
import ru.itbirds.trades.common.App;


@Module
public class AppModule {
    private final App mApp;

    public AppModule(App mApp) {
        this.mApp = mApp;
    }

    @Provides
    @Singleton
    App provideApp() {
        return mApp;
    }

    @Provides
    @Singleton
    DataBase provideDatabase(App app) {
        return Room.databaseBuilder(app, DataBase.class, "database")
                .fallbackToDestructiveMigration()
                .build();
    }

    @Provides
    @Singleton
    TradesDao getCouponDAO(DataBase dataBase) {
        return dataBase.getTradesDao();
    }


}
