package ru.itbirds.trades.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import javax.inject.Inject;

import ru.itbirds.trades.common.App;
import ru.itbirds.trades.R;
import ru.itbirds.trades.adapter.StockAdapter;
import ru.itbirds.trades.common.INavigator;
import ru.itbirds.trades.databinding.PageBinding;
import ru.itbirds.trades.util.LiveConnectUtil;
import ru.itbirds.trades.viewmodels.PageViewModel;
import ru.itbirds.trades.viewmodels.PageViewModelFactory;

import static ru.itbirds.trades.util.Constants.TYPE_KEY;


public class PageFragment extends Fragment {

    private String mType;
    private PageViewModel mViewModel;

    @Inject
    PageViewModelFactory pageViewModelFactory;


    public static PageFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString(TYPE_KEY, type);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getAppComponent().inject(this);
        setRetainInstance(true);
        mViewModel = ViewModelProviders.of(this, pageViewModelFactory).get(PageViewModel.class);
        if (getArguments() != null) {
            mType = getArguments().getString(TYPE_KEY);
        }


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        PageBinding mBinding = PageBinding.inflate(inflater, container, false);
        RecyclerView mRecyclerView = mBinding.recyclerview;
        StockAdapter mAdapter = new StockAdapter(((INavigator) getParentFragment()));
        mRecyclerView.setAdapter(mAdapter);
        mBinding.setLifecycleOwner(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(null);
        mBinding.setVm(mViewModel);
        LiveConnectUtil.getInstance().observe(this, aBoolean -> {
            if (aBoolean) {
                loadData();
                mViewModel.setProgress(true);
                mViewModel.setNoInternet(false);
            } else {
                mViewModel.setProgress(false);
                mViewModel.setNoInternet(true);
                mViewModel.dispatchDetach();
            }
        });
        mViewModel.getCompanyStockLive(mType).observe(this, companyStock -> {
            if (companyStock != null)
                mViewModel.setCompanyStock(companyStock.getCompanies());
        });
        return mBinding.getRoot();
    }

    void loadData() {
        mViewModel.loadData(mType);
        if (!LiveConnectUtil.getInstance().isInternetOn())
            Snackbar.make(Objects.requireNonNull(getActivity()).findViewById(android.R.id.content), getResources().getString(R.string.no_connect), Snackbar.LENGTH_LONG).show();


    }


    @Override
    public void onDetach() {
        mViewModel.dispatchDetach();
        super.onDetach();
    }

}
