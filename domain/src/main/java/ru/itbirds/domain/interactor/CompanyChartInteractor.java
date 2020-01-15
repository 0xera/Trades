package ru.itbirds.domain.interactor;


import com.github.tifezh.kchartlib.chart.entity.KLineEntity;

import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.itbirds.data.model.CompanyChart;
import ru.itbirds.data.repositories.LocalRepository;
import ru.itbirds.data.repositories.RemoteRepository;
import ru.itbirds.domain.usecase.CompanyChartUseCase;

public class CompanyChartInteractor implements CompanyChartUseCase {
    private LocalRepository mLocalRepository;
    private RemoteRepository mRemoteRepository;

    public CompanyChartInteractor(LocalRepository localRepository, RemoteRepository remoteRepository) {

        mLocalRepository = localRepository;
        mRemoteRepository = remoteRepository;
    }

    @Override
    public void insertKLineEntities(List<KLineEntity> kLineEntities, String symbol) {
        CompanyChart companyChart = new CompanyChart();
        companyChart.setEntities(kLineEntities);
        companyChart.setSymbol(symbol);
        mLocalRepository.insertKLineEntities(companyChart);
    }


    @Override
    public LiveData<CompanyChart> getKLineEntities(String symbol) {
        return mLocalRepository.getKLineEntities(symbol);
    }

    @Override
    public Disposable downloadCompanyChart(String symbol, MutableLiveData<Boolean> progress) {
        return mRemoteRepository.getCompanyChart(symbol)
                .subscribeOn(Schedulers.io())
                .repeatWhen(objectFlowable -> objectFlowable.delay(2, TimeUnit.MINUTES))
                .observeOn(Schedulers.io())
                .subscribe(kLineEntities -> {
                            insertKLineEntities(kLineEntities, symbol);
                            progress.postValue(false);
                        },
                        throwable -> progress.postValue(false));

    }

}
