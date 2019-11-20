package ru.itbirds.domain.interactor;

import com.github.tifezh.kchartlib.chart.entity.KLineEntity;
import com.github.tifezh.kchartlib.chart.utils.DateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

import ru.itbirds.data.Constants;
import ru.itbirds.data.model.CompanyChart;
import ru.itbirds.data.repositories.LocalRepository;
import ru.itbirds.domain.usecase.CleanUseCase;

public class CleanInteractor implements CleanUseCase {
    private Executor mExecutor;
    private LocalRepository mLocalRepository;

    public CleanInteractor(LocalRepository localRepository, Executor executor) {

        mLocalRepository = localRepository;
        mExecutor = executor;
    }

    @Override
    public void clean() {
        mExecutor.execute(() -> {
            SimpleDateFormat currentDateFormat = DateUtil.LongTimeFormat;
            //
            String currentDateString = currentDateFormat.format(new Date());
            try {
                Date currentDate = currentDateFormat.parse(currentDateString);
                if (currentDate != null) {
                    if (isOldDate(currentDate)) {
                        mLocalRepository.deleteChartsAndCompanies();
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }


        });
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
