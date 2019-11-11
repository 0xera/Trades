package ru.itbirds.trades.util;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import ru.itbirds.data.model.Company;


public class CompanyDiffUtils extends DiffUtil.ItemCallback<Company> {


    @Override
    public boolean areItemsTheSame(@NonNull Company oldItem, @NonNull Company newItem) {
        return oldItem.getSymbol().equals(newItem.getSymbol());
    }

    @Override
    public boolean areContentsTheSame(@NonNull Company oldItem, @NonNull Company newItem) {
        return oldItem.getLatestPrice() == newItem.getLatestPrice() && oldItem.getChangePercent() == newItem.getChangePercent() && oldItem.getChange() == newItem.getChange();
    }
}
