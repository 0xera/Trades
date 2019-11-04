package ru.itbirds.trades.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity
public class CompanyStock {

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @PrimaryKey
    @NonNull
    private String type;

    private List<Company> companies;

    public List<Company> getCompanies() {
        return companies;
    }

    public void setCompanies(List<Company> companies) {
        this.companies = companies;
    }
}
