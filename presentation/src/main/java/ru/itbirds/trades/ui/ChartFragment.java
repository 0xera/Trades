package ru.itbirds.trades.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.github.tifezh.kchartlib.chart.KChartView;
import com.github.tifezh.kchartlib.chart.formatter.TimeFormatter;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import ru.itbirds.trades.R;
import ru.itbirds.trades.adapter.KChartAdapter;
import ru.itbirds.trades.common.App;
import ru.itbirds.trades.common.SingleActivity;
import ru.itbirds.trades.databinding.ChartBinding;
import ru.itbirds.trades.util.LiveConnectUtil;
import ru.itbirds.trades.viewmodel_factories.ChartViewModelFactory;
import ru.itbirds.trades.viewmodels.ChartViewModel;

import static ru.itbirds.data.Constants.COMPANY_SYMBOL;


public class ChartFragment extends Fragment {

    private ChartViewModel mViewModel;
    private ChartBinding mBinding;
    private Toolbar mToolbar;
    @Inject
    ChartViewModelFactory chartViewModelFactory;
    private Snackbar snackbar;
    private KChartView mKChartView;
    private String mSymbol;

    static ChartFragment newInstance(Bundle bundle) {
        ChartFragment chartFragment = new ChartFragment();
        chartFragment.setArguments(bundle);
        return chartFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getAppComponent().inject(this);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        if (getArguments() != null)
            mSymbol = getArguments().getString(COMPANY_SYMBOL);
        mViewModel = ViewModelProviders.of(this, chartViewModelFactory).get(ChartViewModel.class);

    }

    @Override
    public void onStart() {
        viewModelConfig();
        super.onStart();
    }

    private void viewModelConfig() {
        if (!TextUtils.isEmpty(mSymbol)) {
            LiveConnectUtil.getInstance().observe(this, aBoolean -> {
                if (aBoolean) {
                    loadData(mSymbol);
                    mViewModel.setProgress(true);
                    snackbar.dismiss();
                } else {
                    mViewModel.dispatchDetach();
                    mViewModel.setProgress(false);
                    snackbar.show();
                }
            });
            mViewModel.getkLineEntitiesLive(mSymbol).observe(this, companyChart -> {
                if (companyChart != null && companyChart.getEntities().size() != 0)
                    mViewModel.setData(companyChart.getEntities());
            });
            mViewModel.getCompanyLive(mSymbol).observe(this, comp -> {
                if (comp != null)
                    mViewModel.setCompany(comp);
            });
        }

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = ChartBinding.inflate(inflater, container, false);
        mBinding.setLifecycleOwner(this);
        mBinding.setVm(mViewModel);
        mToolbar = mBinding.toolbar;
        configToolbar();
        mKChartView = mBinding.kchartView;
        kChartViewConfig();
        snackbar = Snackbar.make(Objects.requireNonNull(getActivity()).findViewById(android.R.id.content), this.getString(R.string.no_connect), Snackbar.LENGTH_LONG);
        return mBinding.getRoot();
    }

    private void configToolbar() {
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(mToolbar);
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setDisplayShowTitleEnabled(false);
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(view -> popUp());
        mToolbar.setTitle(mSymbol);
    }

    private void popUp() {
        ((SingleActivity) Objects.requireNonNull(getActivity())).popBackStack(false);
    }

    private void kChartViewConfig() {
        mKChartView.showLoading();
        KChartAdapter mAdapter = new KChartAdapter();
        mKChartView.setAdapter(mAdapter);
        mKChartView.setDateTimeFormatter(new TimeFormatter());
        mKChartView.setGridRows(getResources().getInteger(R.integer.rows));
        mKChartView.setGridColumns(getResources().getInteger(R.integer.columns));
    }


    private void loadData(String symbol) {
        if (LiveConnectUtil.getInstance().isInternetOn()) {
            mViewModel.loadData(symbol);
        }

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_chat, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.chat_item) {
            snackbar.dismiss();
            Bundle bundle = new Bundle();
            bundle.putString(COMPANY_SYMBOL, mSymbol);
            ((SingleActivity) Objects.requireNonNull(getActivity())).changeFragment(ChatFragment.newInstance(bundle), true);
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop() {
        mViewModel.dispatchDetach();
        LiveConnectUtil.getInstance().removeObservers(this);
        super.onStop();
    }
}
