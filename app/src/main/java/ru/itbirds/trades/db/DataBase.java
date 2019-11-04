package ru.itbirds.trades.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import ru.itbirds.trades.model.CompanyChart;
import ru.itbirds.trades.model.CompanyStock;
import ru.itbirds.trades.util.CompanyChartConverter;
import ru.itbirds.trades.util.CompanyStockConverter;

@Database(entities = {CompanyStock.class, CompanyChart.class}, version = 1)
@TypeConverters({CompanyStockConverter.class, CompanyChartConverter.class})
public abstract class DataBase extends RoomDatabase {
    public abstract TradesDao getTradesDao();

}
