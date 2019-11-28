package ru.itbirds.domain.interactor;

import com.github.tifezh.kchartlib.chart.entity.KLineEntity;
import com.github.tifezh.kchartlib.chart.utils.DateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.itbirds.data.Constants;
import ru.itbirds.data.model.CompanyChart;
import ru.itbirds.data.repositories.LocalRepository;
import ru.itbirds.domain.usecase.CleanUseCase;

public class CleanInteractor implements CleanUseCase {
    private LocalRepository mLocalRepository;

    public CleanInteractor(LocalRepository localRepository) {

        mLocalRepository = localRepository;
    }

    @Override
    public Disposable clean() {
        return Completable.fromAction(this::cleaning)
                .doOnError(Throwable::printStackTrace)
                .subscribeOn(Schedulers.io())
                .subscribe();

    }

    private void cleaning() throws ParseException {
        SimpleDateFormat currentDateFormat = DateUtil.LongTimeFormat;
        String currentDateString = currentDateFormat.format(new Date());
        Date currentDate = currentDateFormat.parse(currentDateString);
        if (currentDate != null) {
            if (isOldDate(currentDate)) {
                mLocalRepository.deleteChartsAndCompanies();
            }
        }
    }

    @Override
    public boolean isOldDate(Date currentDate) throws ParseException {
        CompanyChart companyChart = mLocalRepository.getFirstCompanyChart();
        if (companyChart != null) {
            List<KLineEntity> kLineEntities = companyChart.getEntities();
            SimpleDateFormat currentDateFormat = DateUtil.LongTimeFormat;
            Date oldDate = currentDateFormat.parse(createData(kLineEntities));
            if (oldDate != null) {
                return currentDate.getTime() - oldDate.getTime() >= Constants.DAY_IN_MILLISECONDS;
            }
        }
        return false;

    }

    private String createData(List<KLineEntity> kLineEntities) {
        String[] split = kLineEntities.get(0).minute.split(":");
        return kLineEntities.get(0).date + " " + (Integer.parseInt(split[0]) + 7) + ":" + split[1];
    }
}
