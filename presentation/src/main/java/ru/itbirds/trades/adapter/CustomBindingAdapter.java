package ru.itbirds.trades.adapter;


import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.tifezh.kchartlib.chart.KChartView;

import java.util.List;
import java.util.Objects;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;
import ru.itbirds.data.model.Company;

import com.github.tifezh.kchartlib.chart.entity.KLineEntity;


public class CustomBindingAdapter {

    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView imageView, String url) {
        Glide.with(imageView.getContext()).load(Uri.parse(url)).into(imageView);
    }

    @BindingAdapter({"datarv"})
    public static void dataToRecyclerView(RecyclerView recyclerView, List<Company> companyStock) {
        ((StockAdapter) Objects.requireNonNull(recyclerView.getAdapter())).submitList(companyStock);
    }

    @BindingAdapter({"datakcv"})
    public static void dataToKChartView(KChartView kChartView, List<KLineEntity> kLineEntities) {
        ((KChartAdapter) kChartView.getAdapter()).setNewData(kLineEntities);
        kChartView.refreshEnd();
    }
}
