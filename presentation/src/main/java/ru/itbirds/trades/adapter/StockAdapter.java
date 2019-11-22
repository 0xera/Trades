package ru.itbirds.trades.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import ru.itbirds.data.model.Company;
import ru.itbirds.trades.common.INavigator;
import ru.itbirds.trades.databinding.CompanyItemBinding;
import ru.itbirds.trades.util.CompanyDiffUtils;

import static ru.itbirds.data.Constants.COMPANY;


public class StockAdapter extends ListAdapter<Company, StockAdapter.StockHolder> {


    private INavigator mNavigator;

    public StockAdapter(INavigator navigator) {
        super(new CompanyDiffUtils());
        mNavigator = navigator;
    }

    @NonNull
    @Override
    public StockHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new StockHolder(CompanyItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), mNavigator);
    }

    @Override
    public void onBindViewHolder(@NonNull StockHolder stockHolder, int position) {
        stockHolder.bind(getItem(position));
    }


    @Override
    public void onBindViewHolder(@NonNull StockHolder stockHolder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) stockHolder.bind(getItem(position));
        else {
            Bundle bundle = (Bundle) payloads.get(0);
            Company company = null;
            for (String key : bundle.keySet()) {
                if (key.equals(COMPANY)) {
                    company = (Company) bundle.getSerializable(COMPANY);
                    break;
                }
            }
            if (company != null) stockHolder.bind(company);
            else super.onBindViewHolder(stockHolder, position, payloads);

        }
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        mNavigator = null;
        super.onDetachedFromRecyclerView(recyclerView);
    }

    public class StockHolder extends RecyclerView.ViewHolder {
        private final CompanyItemBinding binding;

        StockHolder(CompanyItemBinding itemBinding, INavigator listener) {
            super(itemBinding.getRoot());
            binding = itemBinding;
            binding.setClickListener(view -> listener.clickForNavigate(binding.getCompany()));

        }

        void bind(Company company) {
            binding.setCompany(company);
            binding.executePendingBindings();

        }
    }
}


