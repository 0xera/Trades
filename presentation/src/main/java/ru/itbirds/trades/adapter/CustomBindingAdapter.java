package ru.itbirds.trades.adapter;


import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.tifezh.kchartlib.chart.KChartView;
import com.github.tifezh.kchartlib.chart.entity.KLineEntity;

import java.util.List;
import java.util.Objects;

import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;
import ru.itbirds.data.model.Company;
import ru.itbirds.trades.R;


public class CustomBindingAdapter {

    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView imageView, String url) {
        if (!TextUtils.isEmpty(url))
            Glide.with(imageView.getContext()).load(Uri.parse(url)).into(imageView);
        else
            Glide.with(imageView.getContext()).load(R.drawable.ic_person_black_24dp).into(imageView);

    }


    @BindingAdapter({"datarv"})
    public static void dataToRecyclerView(RecyclerView recyclerView, List<Company> companyStock) {
        ((StockAdapter) Objects.requireNonNull(recyclerView.getAdapter())).submitList(companyStock);
    }


    @BindingAdapter({"datakcv"})
    public static void dataToKChartView(KChartView kChartView, List<KLineEntity> kLineEntities) {
        kChartView.getAdapter().setNewData(kLineEntities);
        kChartView.refreshEnd();
    }

    @BindingAdapter({"animateText"})
    public static void animateTextView(TextView textView, String text) {
        ObjectAnimator objectAnimator = null;
        if (!TextUtils.isEmpty(textView.getText()) && !textView.getText().equals("0.00") && Double.parseDouble(String.valueOf(textView.getText())) != Double.parseDouble(String.valueOf(text))) {
            if (Double.parseDouble(String.valueOf(textView.getText())) > Double.parseDouble(String.valueOf(text))) {
                objectAnimator = ObjectAnimator.ofObject(textView, "backgroundColor",
                        new ArgbEvaluator(),
                        ContextCompat.getColor(textView.getContext(), R.color.red),
                        ContextCompat.getColor(textView.getContext(), android.R.color.transparent));
            } else if (Double.parseDouble(String.valueOf(textView.getText())) < Double.parseDouble(String.valueOf(text))) {
                objectAnimator = ObjectAnimator.ofObject(textView, "backgroundColor",
                        new ArgbEvaluator(),
                        ContextCompat.getColor(textView.getContext(), R.color.green),
                        ContextCompat.getColor(textView.getContext(), android.R.color.transparent));
            }
            if (objectAnimator != null) {
                objectAnimator.setDuration(2000L);
                objectAnimator.start();
            }
        }
        textView.setText(text);
    }
}
