package ru.itbirds.data.model;




import java.util.List;

import com.github.tifezh.kchartlib.chart.entity.KLineEntity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CompanyChart {
    @PrimaryKey
    @NonNull
    private String symbol;
    private List<KLineEntity> entities;

    public List<KLineEntity> getEntities() {
        return entities;
    }

    public void setEntities(List<KLineEntity> entities) {
        this.entities = entities;
    }

    @NonNull
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(@NonNull String symbol) {
        this.symbol = symbol;
    }


}
