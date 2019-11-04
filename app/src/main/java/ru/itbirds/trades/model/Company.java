package ru.itbirds.trades.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import static ru.itbirds.trades.util.Constants.IMAGE_EXTENSION;
import static ru.itbirds.trades.util.Constants.URL_IMAGE;

@Entity
public class Company implements Serializable {
    @PrimaryKey
    @NonNull
    private String symbol;

    public double getChange() {
        return change;
    }

    public double getChangePercent() {
        return changePercent;
    }

    public double getLatestPrice() {
        return latestPrice;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public void setChangePercent(double changePercent) {
        this.changePercent = changePercent;
    }

    public void setLatestPrice(double latestPrice) {
        this.latestPrice = latestPrice;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String companyName;

    private double change;
    private double changePercent;
    private double latestPrice;
    private String url = "";

    public String getSymbol() {
        return symbol;
    }

    public String getCompanyName() {
        return companyName;
    }


    public String getUrl() {
        return url = URL_IMAGE + symbol.toUpperCase() + IMAGE_EXTENSION;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
