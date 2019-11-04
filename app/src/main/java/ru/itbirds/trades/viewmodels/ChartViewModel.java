package ru.itbirds.trades.viewmodels;

import android.util.Log;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.itbirds.trades.model.Company;
import ru.itbirds.trades.model.CompanyChart;
import ru.itbirds.trades.model.KLineEntity;
import ru.itbirds.trades.repository.LocalRepository;
import ru.itbirds.trades.repository.RemoteRepository;
import ru.itbirds.trades.util.DataHelper;

public class ChartViewModel extends ViewModel {
    private final RemoteRepository mRemoteRepository;
    private final LocalRepository mLocalRepository;
    private MutableLiveData<KLineEntity> entity = new MutableLiveData<>();
    private ObservableBoolean progress = new ObservableBoolean();


    private MutableLiveData<Company> company = new MutableLiveData<>();
    private MutableLiveData<List<KLineEntity>> data = new MutableLiveData<>();

    private Disposable mDisposableChart;

    private Disposable mDisposableCompany;
    private LiveData<CompanyChart> kLineEntitiesLive;
    private LiveData<Company> companyLive;


    public ChartViewModel(RemoteRepository remoteRepository, LocalRepository localRepository) {
        mRemoteRepository = remoteRepository;
        mLocalRepository = localRepository;
    }

    public LiveData<CompanyChart> getkLineEntitiesLive(String symbol) {
        if (kLineEntitiesLive == null) {
            kLineEntitiesLive = mLocalRepository.getKLineEntities(symbol);
        }
        return kLineEntitiesLive;
    }

    public LiveData<Company> getCompanyLive(String symbol) {
        if (companyLive == null) {
            companyLive = mLocalRepository.getCompany(symbol);
        }
        return companyLive;
    }

    public void loadData(Company c) {
        mDisposableCompany = mRemoteRepository.getCompany(c.getSymbol())
                .repeatWhen(objectFlowable -> objectFlowable.delay(2, TimeUnit.SECONDS))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(company -> {
                            mLocalRepository.insertCompany(company);
                            setProgress(false);
                        },
                        throwable -> {
                            setProgress(false);
                            Log.d("loadDataCompany", "loadData: " + throwable.getMessage());
                        });
        mDisposableChart = mRemoteRepository.getCompanyChart(c.getSymbol())
                .repeatWhen(objectFlowable -> objectFlowable.delay(2, TimeUnit.MINUTES))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(kLineEntities -> {
                            mLocalRepository.insertKLineEntities(kLineEntities, c.getSymbol());
                            setProgress(false);
                            Log.d("loadDataChart1", "loadData: ");
                        },
                        throwable -> {
                            setProgress(false);
                            Log.d("loadDataChart", "loadData: " + throwable.getMessage());
                        });
    }

    public ObservableBoolean getProgress() {
        return progress;
    }

    public void setProgress(boolean p) {
        progress.set(p);
    }

    public void setCompany(Company c) {

        company.postValue(c);

    }

    public MutableLiveData<Company> getCompany() {
        return company;
    }

    public void setData(List<KLineEntity> kLineEntities) {
        removeNullEntity(kLineEntities);
        DataHelper.calculate(kLineEntities);
        data.postValue(kLineEntities);
        setEntity(kLineEntities.get(kLineEntities.size() - 1));
    }

    private void removeNullEntity(List<KLineEntity> kLineEntities) {
        if (kLineEntities == null || kLineEntities.size() == 0) return;
        ListIterator<KLineEntity> iterator = kLineEntities.listIterator();
        KLineEntity next;
        while (iterator.hasNext()) {

            next = iterator.next();
            if (next.high == 0.0 || next.close == 0.0 || next.low == 0.0 || next.open == 0.0) {
                iterator.remove();
            }
        }
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
