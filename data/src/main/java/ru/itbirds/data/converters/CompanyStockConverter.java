package ru.itbirds.data.converters;





import androidx.room.TypeConverter;
import ru.itbirds.data.model.Company;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;




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
