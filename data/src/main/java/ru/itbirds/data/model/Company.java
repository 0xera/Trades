package ru.itbirds.data.model;


import java.io.Serializable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import ru.itbirds.data.Constants;


@Entity
public class Company implements Serializable {
    @PrimaryKey
    @NonNull
    private String symbol;
    private String companyName;
    private double change;
    private double changePercent;
    private double latestPrice;
    private String url = "";

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public double getChangePercent() {
        return changePercent;
    }

    public void setChangePercent(double changePercent) {
        this.changePercent = changePercent;
    }

    public double getLatestPrice() {
        return latestPrice;
    }

    public void setLatestPrice(double latestPrice) {
        this.latestPrice = latestPrice;
    }

    @NonNull
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(@NonNull String symbol) {
        this.symbol = symbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getUrl() {
        return url = Constants.URL_IMAGE + symbol.toUpperCase() + Constants.IMAGE_EXTENSION;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
