package ru.itbirds.domain.usecase;

import com.github.tifezh.kchartlib.chart.entity.KLineEntity;

import java.util.List;

import androidx.lifecycle.LiveData;
import io.reactivex.Flowable;
import ru.itbirds.data.model.CompanyChart;


public interface CompanyChartUseCase {

    void insertKLineEntities(List<KLineEntity> kLineEntities, String ticker);

    LiveData<CompanyChart> getKLineEntities(String ticker);


    Flowable<List<KLineEntity>> downloadCompanyChart(String ticker);
}
