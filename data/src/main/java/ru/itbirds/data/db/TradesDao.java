package ru.itbirds.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import io.reactivex.Flowable;
import ru.itbirds.data.model.Company;
import ru.itbirds.data.model.CompanyChart;
import ru.itbirds.data.model.CompanyStock;


@Dao
public interface TradesDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCompanyStock(CompanyStock companyStock);

    @Query("SELECT * FROM CompanyStock WHERE type = :string")
    LiveData<CompanyStock> getCompanyStock(String string);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCompany(Company company);

    @Query("SELECT * FROM company WHERE symbol = :string")
    LiveData<Company> getCompany(String string);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCompanyChart(CompanyChart companyChart);

    @Query("SELECT * FROM CompanyChart WHERE symbol = :string")
    LiveData<CompanyChart> getCompanyChart(String string);


}
