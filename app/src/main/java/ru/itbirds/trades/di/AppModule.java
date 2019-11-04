package ru.itbirds.trades.di;

import androidx.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.itbirds.trades.App;
import ru.itbirds.trades.db.DataBase;
import ru.itbirds.trades.db.TradesDao;

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
    public TradesDao getCouponDAO(DataBase dataBase) {
        return dataBase.getTradesDao();
    }


}
