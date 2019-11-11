package ru.itbirds.data.converters;


import androidx.room.TypeConverter;
import com.github.tifezh.kchartlib.chart.entity.KLineEntity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;




public class CompanyChartConverter {

    private Type listType = new TypeToken<List<KLineEntity>>() {
    }.getType();
    private Gson gson = new Gson();

    @TypeConverter
    public String saveList(List<KLineEntity> kLineEntities) {
        return gson.toJson(kLineEntities);

    }

    @TypeConverter
    public List<KLineEntity> restoreList(String data) {
        return gson.fromJson(data, listType);

    }

}
