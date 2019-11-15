package ru.itbirds.trades.viewmodels;


import com.github.tifezh.kchartlib.chart.entity.KLineEntity;

import java.util.List;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.disposables.Disposable;
import ru.itbirds.data.model.Company;
import ru.itbirds.data.model.CompanyChart;
import ru.itbirds.domain.DataHelper;
import ru.itbirds.domain.interactor.CompanyChartInteractor;
import ru.itbirds.domain.interactor.CompanyInteractor;

public class ChartViewModel extends ViewModel {
    private MutableLiveData<KLineEntity> entity = new MutableLiveData<>();
    private ObservableBoolean progress = new ObservableBoolean();


    private MutableLiveData<Company> company = new MutableLiveData<>();
    private MutableLiveData<List<KLineEntity>> data = new MutableLiveData<>();

    private Disposable mDisposableChart;

    private Disposable mDisposableCompany;
    private LiveData<CompanyChart> kLineEntitiesLive;
    private LiveData<Company> companyLive;
    private CompanyChartInteractor mCompanyChartInteractor;
    private CompanyInteractor mCompanyInteractor;


    public ChartViewModel(CompanyChartInteractor companyChartInteractor, CompanyInteractor companyInteractor) {

        mCompanyChartInteractor = companyChartInteractor;
        mCompanyInteractor = companyInteractor;
    }

    public LiveData<CompanyChart> getkLineEntitiesLive(String symbol) {
        if (kLineEntitiesLive == null) {
            kLineEntitiesLive = mCompanyChartInteractor.getKLineEntities(symbol);
        }
        return kLineEntitiesLive;
    }

    public LiveData<Company> getCompanyLive(String symbol) {
        if (companyLive == null) {
            companyLive = mCompanyInteractor.getCompany(symbol);
        }
        return companyLive;
    }

    public void loadData(Company c) {
        mDisposableCompany = mCompanyInteractor.downloadCompany(c.getSymbol())
                .subscribe(company -> {
                            mCompanyInteractor.insertCompany(company);
                            setProgress(false);
                        },
                        throwable -> setProgress(false));
        mDisposableChart = mCompanyChartInteractor.downloadCompanyChart(c.getSymbol())
                .subscribe(kLineEntities -> {
                            mCompanyChartInteractor.insertKLineEntities(kLineEntities, c.getSymbol());
                            setProgress(false);
                        },
                        throwable -> setProgress(false));
    }

    public ObservableBoolean getProgress() {
        return progress;
    }

    public void setProgress(boolean p) {
        progress.set(p);
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
}
