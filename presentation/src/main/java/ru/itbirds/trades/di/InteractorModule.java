package ru.itbirds.trades.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.itbirds.domain.interactor.CompanyChartInteractor;
import ru.itbirds.domain.interactor.CompanyInteractor;
import ru.itbirds.domain.interactor.CompanyStockInteractor;
import ru.itbirds.data.repositories.LocalRepository;
import ru.itbirds.data.repositories.RemoteRepository;

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
}
