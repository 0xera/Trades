package ru.itbirds.trades.viewmodels;


import com.github.tifezh.kchartlib.chart.entity.KLineEntity;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.disposables.Disposable;
import ru.itbirds.data.model.Company;
import ru.itbirds.data.model.CompanyChart;
import ru.itbirds.domain.DataHelper;
import ru.itbirds.domain.usecase.CompanyChartUseCase;
import ru.itbirds.domain.usecase.CompanyUseCase;

public class ChartViewModel extends ViewModel {
    private MutableLiveData<KLineEntity> entity = new MutableLiveData<>();
    private MutableLiveData<Boolean> progress = new MutableLiveData<>(true);


    private MutableLiveData<Company> company = new MutableLiveData<>();
    private MutableLiveData<List<KLineEntity>> data = new MutableLiveData<>();

    private Disposable mDisposableChart;

    private Disposable mDisposableCompany;
    private LiveData<CompanyChart> kLineEntitiesLive;
    private LiveData<Company> companyLive;
    private CompanyChartUseCase mCompanyChartUseCase;
    private CompanyUseCase mCompanyUseCase;


    public ChartViewModel(CompanyChartUseCase companyChartUseCase, CompanyUseCase companyUseCase) {

        mCompanyChartUseCase = companyChartUseCase;
        mCompanyUseCase = companyUseCase;
    }

    public LiveData<CompanyChart> getkLineEntitiesLive(String symbol) {
        if (kLineEntitiesLive == null) {
            kLineEntitiesLive = mCompanyChartUseCase.getKLineEntities(symbol);
        }
        return kLineEntitiesLive;
    }

    public LiveData<Company> getCompanyLive(String symbol) {
        if (companyLive == null) {
            companyLive = mCompanyUseCase.getCompany(symbol);
        }
        return companyLive;
    }

    public void loadData(String symbol) {
        mDisposableCompany = mCompanyUseCase.downloadCompany(symbol, progress);
        mDisposableChart = mCompanyChartUseCase.downloadCompanyChart(symbol, progress);
    }

    public MutableLiveData<Boolean> getProgress() {
        return progress;
    }

    public void setCompany(Company c) {

        company.setValue(c);

    }

    public MutableLiveData<Company> getCompany() {
        return company;
    }

    public void setData(List<KLineEntity> kLineEntities) {
        DataHelper.removeNullEntity(kLineEntities);
        DataHelper.calculate(kLineEntities);
        data.postValue(kLineEntities);
        setEntity(kLineEntities.get(kLineEntities.size() - 1));
    }


    public MutableLiveData<KLineEntity> getEntity() {
        return entity;
    }

    private void setEntity(KLineEntity kLineEntity) {
        entity.postValue(kLineEntity);
    }

    public MutableLiveData<List<KLineEntity>> getData() {
        return data;
    }

    public void dispatchDetach() {
        if (mDisposableChart != null) {
            mDisposableChart.dispose();
        }
        if (mDisposableCompany != null) {
            mDisposableCompany.dispose();
        }
    }

    public void setProgress(boolean value) {
        progress.postValue(value);
    }
}
