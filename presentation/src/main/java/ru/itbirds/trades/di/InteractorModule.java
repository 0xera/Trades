package ru.itbirds.trades.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.itbirds.data.repositories.ChatRepository;
import ru.itbirds.data.repositories.LocalRepository;
import ru.itbirds.data.repositories.RemoteRepository;
import ru.itbirds.domain.interactor.ChatInteractor;
import ru.itbirds.domain.interactor.CleanInteractor;
import ru.itbirds.domain.interactor.CompanyChartInteractor;
import ru.itbirds.domain.interactor.CompanyInteractor;
import ru.itbirds.domain.interactor.CompanyStockInteractor;

@Module
public class InteractorModule {
    @Provides
    @Singleton
    CompanyInteractor provideCompanyInteractor(LocalRepository localRepository, RemoteRepository remoteRepository) {
        return new CompanyInteractor(localRepository, remoteRepository);
    }


    @Provides
    @Singleton
    CompanyChartInteractor provideCompanyChartInteractor(LocalRepository localRepository, RemoteRepository remoteRepository) {
        return new CompanyChartInteractor(localRepository, remoteRepository);
    }

    @Provides
    @Singleton
    CompanyStockInteractor provideCompanyStockInteractor(LocalRepository localRepository, RemoteRepository remoteRepository) {
        return new CompanyStockInteractor(localRepository, remoteRepository);
    }

    @Provides
    @Singleton
    ChatInteractor provideChatInteractor(ChatRepository chatRepository) {
        return new ChatInteractor(chatRepository);
    }

    @Provides
    @Singleton
    CleanInteractor provideCleanInteractor(LocalRepository localRepository) {
        return new CleanInteractor(localRepository);
    }
}
