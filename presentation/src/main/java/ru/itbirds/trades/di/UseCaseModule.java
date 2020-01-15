package ru.itbirds.trades.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.itbirds.data.repositories.ChatRepository;
import ru.itbirds.data.repositories.LocalRepository;
import ru.itbirds.data.repositories.LoginRepository;
import ru.itbirds.data.repositories.RegRepository;
import ru.itbirds.data.repositories.RemoteRepository;
import ru.itbirds.data.repositories.ResetRepository;
import ru.itbirds.domain.interactor.ChatInteractor;
import ru.itbirds.domain.interactor.CleanInteractor;
import ru.itbirds.domain.interactor.CompanyChartInteractor;
import ru.itbirds.domain.interactor.CompanyInteractor;
import ru.itbirds.domain.interactor.CompanyStockInteractor;
import ru.itbirds.domain.interactor.LoginInteractor;
import ru.itbirds.domain.interactor.RegInteractor;
import ru.itbirds.domain.interactor.ResetInteractor;
import ru.itbirds.domain.usecase.ChatUseCase;
import ru.itbirds.domain.usecase.CleanUseCase;
import ru.itbirds.domain.usecase.CompanyChartUseCase;
import ru.itbirds.domain.usecase.CompanyStockUseCase;
import ru.itbirds.domain.usecase.CompanyUseCase;
import ru.itbirds.domain.usecase.LoginUseCase;
import ru.itbirds.domain.usecase.RegUseCase;
import ru.itbirds.domain.usecase.ResetUseCase;

@Module
public class UseCaseModule {
    @Provides
    @Singleton
    CompanyUseCase provideCompanyUseCase(LocalRepository localRepository, RemoteRepository remoteRepository) {
        return new CompanyInteractor(localRepository, remoteRepository);
    }


    @Provides
    @Singleton
    CompanyChartUseCase provideCompanyChartUseCase(LocalRepository localRepository, RemoteRepository remoteRepository) {
        return new CompanyChartInteractor(localRepository, remoteRepository);
    }

    @Provides
    @Singleton
    CompanyStockUseCase provideCompanyStockUseCase(LocalRepository localRepository, RemoteRepository remoteRepository) {
        return new CompanyStockInteractor(localRepository, remoteRepository);
    }

    @Provides
    @Singleton
    ChatUseCase provideChatUseCase(ChatRepository chatRepository) {
        return new ChatInteractor(chatRepository);
    }

    @Provides
    @Singleton
    CleanUseCase provideCleanUseCase(LocalRepository localRepository) {
        return new CleanInteractor(localRepository);
    }

    @Provides
    @Singleton
    ResetUseCase provideResetUseCase(ResetRepository resetRepository) {
        return new ResetInteractor(resetRepository);
    }

    @Provides
    @Singleton
    RegUseCase provideRegUseCase(RegRepository regRepository) {
        return new RegInteractor(regRepository);
    }

    @Provides
    @Singleton
    LoginUseCase provideLoginUseCase(LoginRepository loginRepository) {
        return new LoginInteractor(loginRepository);
    }
}
