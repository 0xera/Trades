package ru.itbirds.domain.use_case;

import com.github.tifezh.kchartlib.chart.entity.KLineEntity;

import java.util.List;

import androidx.lifecycle.LiveData;
import io.reactivex.Flowable;
import ru.itbirds.data.model.CompanyChart;


public interface CompanyChartUseCase {

    void insertKLineEntities(List<KLineEntity> kLineEntities, String ticker);

    public LiveData<CompanyChart> getKLineEntities(String ticker);


    public Flowable<List<KLineEntity>> downloadCompanyChart(String ticker);
}
