package ru.itbirds.trades.util;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import ru.itbirds.data.model.Company;

import static ru.itbirds.data.Constants.COMPANY_SYMBOL;


public class CompanyDiffUtils extends DiffUtil.ItemCallback<Company> {


    @Override
    public boolean areItemsTheSame(@NonNull Company oldItem, @NonNull Company newItem) {
        return oldItem.getSymbol().equals(newItem.getSymbol());
    }

    @Override
    public boolean areContentsTheSame(@NonNull Company oldItem, @NonNull Company newItem) {
        return oldItem.getLatestPrice() == newItem.getLatestPrice() && oldItem.getChangePercent() == newItem.getChangePercent() && oldItem.getChange() == newItem.getChange();
    }

    @Nullable
    @Override
    public Object getChangePayload(@NonNull Company oldItem, @NonNull Company newItem) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(COMPANY_SYMBOL, newItem);
        return bundle;
    }
}
