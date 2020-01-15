package ru.itbirds.trades.di;

import javax.inject.Singleton;

import dagger.Component;
import ru.itbirds.trades.ui.ChartFragment;
import ru.itbirds.trades.ui.ChatFragment;
import ru.itbirds.trades.ui.LoginFragment;
import ru.itbirds.trades.ui.PageFragment;
import ru.itbirds.trades.ui.RegFragment;
import ru.itbirds.trades.ui.ResetFragment;
import ru.itbirds.trades.ui.TopTenFragment;

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class, RepositoryModule.class, UseCaseModule.class})
public interface AppComponent {
    void inject(TopTenFragment fragment);

    void inject(RegFragment fragment);

    void inject(LoginFragment fragment);

    void inject(ResetFragment fragment);

    void inject(ChartFragment fragment);

    void inject(PageFragment fragment);

    void inject(ChatFragment fragment);
}
