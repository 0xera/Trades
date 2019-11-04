package ru.itbirds.trades.util;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import ru.itbirds.trades.model.Company;

public class CompanyStockConverter {

    private Type listType = new TypeToken<List<Company>>(){}.getType();
    private Gson gson = new Gson();

    @TypeConverter
    public String saveList(List<Company> companyList) {
        return gson.toJson(companyList);
    }

    @TypeConverter
    public List<Company> restoreList(String data) {
        return gson.fromJson(data, listType);

    }

}
