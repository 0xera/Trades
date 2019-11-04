package ru.itbirds.trades.adapter;

import com.github.tifezh.kchartlib.chart.BaseKChartAdapter;

import java.util.Calendar;
import java.util.Date;

import ru.itbirds.trades.model.KLineEntity;

public class KChartAdapter extends BaseKChartAdapter<KLineEntity> {

    @Override
    public Date getDate(int position) {
        try {
            String s = getItem(position).minute;
            String[] split = s.split(":");
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(split[0]) + 7);
            calendar.set(Calendar.MINUTE, Integer.parseInt(split[1]));
//            date.setHours(Integer.parseInt(split[0]) + 7);
//            date.setMinutes(Integer.parseInt(split[1]));
            return calendar.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
