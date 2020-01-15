package ru.itbirds.domain.usecase;

import com.github.tifezh.kchartlib.chart.entity.KLineEntity;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.disposables.Disposable;
import ru.itbirds.data.model.CompanyChart;


public interface CompanyChartUseCase {

    void insertKLineEntities(List<KLineEntity> kLineEntities, String ticker);

    LiveData<CompanyChart> getKLineEntities(String ticker);


    Disposable downloadCompanyChart(String ticker, MutableLiveData<Boolean> progress);
}
