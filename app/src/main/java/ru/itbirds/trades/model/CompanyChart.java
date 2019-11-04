package ru.itbirds.trades.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity
public class CompanyChart {
    @PrimaryKey
    @NonNull
    private String symbol;

    public List<KLineEntity> getEntities() {
        return entities;
    }

    public void setEntities(List<KLineEntity> entities) {
        this.entities = entities;
    }

    private List<KLineEntity> entities;

    public void setSymbol(@NonNull String symbol) {
        this.symbol = symbol;
    }

    @NonNull
    public String getSymbol() {
        return symbol;
    }


}
