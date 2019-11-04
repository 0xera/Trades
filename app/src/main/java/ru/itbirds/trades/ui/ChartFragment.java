package ru.itbirds.trades.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.github.tifezh.kchartlib.chart.KChartView;
import com.github.tifezh.kchartlib.chart.formatter.TimeFormatter;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import javax.inject.Inject;

import ru.itbirds.trades.App;
import ru.itbirds.trades.R;
import ru.itbirds.trades.adapter.KChartAdapter;
import ru.itbirds.trades.databinding.ChartBinding;
import ru.itbirds.trades.model.Company;
import ru.itbirds.trades.util.LiveConnectUtil;
import ru.itbirds.trades.viewmodels.ChartViewModel;
import ru.itbirds.trades.viewmodels.ChartViewModelFactory;

import static ru.itbirds.trades.util.Constants.COMPANY;

public class ChartFragment extends Fragment {

    private ChartViewModel mViewModel;
    private ChartBinding mBinding;
    private Toolbar mToolbar;
    @Inject
    ChartViewModelFactory chartViewModelFactory;
    private Snackbar snackbar;


//    public ChartFragment() {
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getAppComponent().inject(this);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        mViewModel = ViewModelProviders.of(this, chartViewModelFactory).get(ChartViewModel.class);
        Company company;
        if (getArguments() != null) {
            company = (Company) (getArguments().getSerializable(COMPANY));
            if (company != null) {
                LiveConnectUtil.getInstance().observe(this, aBoolean -> {
                    if (aBoolean) {
                        loadData(company);
                        mViewModel.setProgress(true);
                        snackbar.dismiss();
                    } else {
                        mViewModel.dispatchDetach();
                        mViewModel.setProgress(false);
                        snackbar.show();
                    }
                });
                mViewModel.getkLineEntitiesLive(company.getSymbol()).observe(this, companyChart -> {
                    if (companyChart != null)
                        mViewModel.setData(companyChart.getEntities());
                });
                mViewModel.getCompanyLive(company.getSymbol()).observe(this, comp -> {
                    if (comp != null)
                        mViewModel.setCompany(comp);
                });
            }
        }


    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = ChartBinding.inflate(inflater, container, false);
        mToolbar = mBinding.toolbar;
        snackbar = Snackbar.make(Objects.requireNonNull(getActivity()).findViewById(android.R.id.content), getResources().getString(R.string.no_connect), Snackbar.LENGTH_LONG);
        KChartView mKChartView = mBinding.kchartView;
        mKChartView.showLoading();
        KChartAdapter mAdapter = new KChartAdapter();
        mKChartView.setAdapter(mAdapter);
        mKChartView.setDateTimeFormatter(new TimeFormatter());
        mKChartView.setGridRows(getResources().getInteger(R.integer.rows));
        mKChartView.setGridColumns(getResources().getInteger(R.integer.columns));
        mBinding.setLifecycleOwner(this);
        mBinding.setVm(mViewModel);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(mToolbar);
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    private void loadData(Company company) {
        if (LiveConnectUtil.getInstance().isInternetOn()) {
            mViewModel.loadData(company);
        }

    }

    @Override
    public void onDetach() {
        mViewModel.dispatchDetach();
        super.onDetach();
    }
}
