package ru.itbirds.trades.common;


public interface INavigator {

    void clickForNavigate(String symbol);

    void clickForNavigate(Throwable throwable);

}

