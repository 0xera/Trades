package ru.itbirds.trades.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.itbirds.data.api.IEXStockAPI;
import ru.itbirds.data.db.TradesDao;
import ru.itbirds.data.repositories.ChatRepository;
import ru.itbirds.data.repositories.ChatRepositoryImpl;
import ru.itbirds.data.repositories.LocalRepository;
import ru.itbirds.data.repositories.LocalRepositoryImpl;
import ru.itbirds.data.repositories.RemoteRepository;
import ru.itbirds.data.repositories.RemoteRepositoryImpl;

@Module
class RepositoryModule {

    @Provides
    @Singleton
    RemoteRepository provideRemoteRepository(IEXStockAPI stockAPI) {
        return new RemoteRepositoryImpl(stockAPI);
    }

    @Provides
    @Singleton
    ChatRepository provideChatRepository() {
        return new ChatRepositoryImpl();
    }


    @Provides
    @Singleton
    LocalRepository provideLocalRepository(TradesDao tradesDao) {
        return new LocalRepositoryImpl(tradesDao);
    }
}
