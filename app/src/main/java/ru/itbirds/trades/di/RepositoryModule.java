package ru.itbirds.trades.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.itbirds.trades.api.IEXStockAPI;
import ru.itbirds.trades.db.TradesDao;
import ru.itbirds.trades.repository.LocalRepository;
import ru.itbirds.trades.repository.LocalRepositoryImpl;
import ru.itbirds.trades.repository.RemoteRepository;
import ru.itbirds.trades.repository.RemoteRepositoryImpl;

@Module
public class RepositoryModule {

    @Provides
    @Singleton
    public RemoteRepository provideRemoteRepository(IEXStockAPI stockAPI) {
        return new RemoteRepositoryImpl(stockAPI);
    }


    @Provides
    @Singleton
    public LocalRepository provideLocalRepository(TradesDao tradesDao) {
        return new LocalRepositoryImpl(tradesDao);
    }
}
