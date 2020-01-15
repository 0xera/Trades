package ru.itbirds.trades.di;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.itbirds.data.api.IEXStockAPI;
import ru.itbirds.data.db.TradesDao;
import ru.itbirds.data.repositories.ChatRepository;
import ru.itbirds.data.repositories.ChatRepositoryImpl;
import ru.itbirds.data.repositories.LocalRepository;
import ru.itbirds.data.repositories.LocalRepositoryImpl;
import ru.itbirds.data.repositories.LoginRepository;
import ru.itbirds.data.repositories.RegRepository;
import ru.itbirds.data.repositories.RemoteRepository;
import ru.itbirds.data.repositories.RemoteRepositoryImpl;
import ru.itbirds.data.repositories.ResetRepository;

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

    @Provides
    @Singleton
    ResetRepository provideResetRepository(FirebaseAuth auth) {
        return new ResetRepository(auth);
    }

    @Provides
    @Singleton
    LoginRepository provideLoginRepository(FirebaseAuth auth) {
        return new LoginRepository(auth);
    }

    @Provides
    @Singleton
    RegRepository provideRegRepository(FirebaseAuth auth, FirebaseStorage storage) {
        return new RegRepository(auth, storage);
    }
}
