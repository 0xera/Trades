package ru.itbirds.data.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import ru.itbirds.data.converters.CompanyChartConverter;
import ru.itbirds.data.converters.CompanyStockConverter;
import ru.itbirds.data.model.Company;
import ru.itbirds.data.model.CompanyChart;
import ru.itbirds.data.model.CompanyStock;


@Database(entities = {CompanyStock.class, CompanyChart.class, Company.class}, version = 1)
@TypeConverters({CompanyStockConverter.class, CompanyChartConverter.class})
public abstract class DataBase extends RoomDatabase {
    public abstract TradesDao getTradesDao();

}
