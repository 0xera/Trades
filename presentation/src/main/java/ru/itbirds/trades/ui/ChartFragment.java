package ru.itbirds.trades.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import ru.itbirds.data.model.Company;
import ru.itbirds.trades.R;
import ru.itbirds.trades.adapter.KChartAdapter;
import ru.itbirds.trades.common.App;
import ru.itbirds.trades.databinding.ChartBinding;
import ru.itbirds.trades.util.LiveConnectUtil;
import ru.itbirds.trades.viewmodels.ChartViewModel;
import ru.itbirds.trades.viewmodels.ChartViewModelFactory;

import static ru.itbirds.trades.util.Constants.COMPANY_SYMBOL;

public class ChartFragment extends Fragment {

    private ChartViewModel mViewModel;
    private ChartBinding mBinding;
    private Toolbar mToolbar;
    @Inject
    ChartViewModelFactory chartViewModelFactory;
    private Snackbar snackbar;
    private KChartView mKChartView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getAppComponent().inject(this);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        mViewModel = ViewModelProviders.of(this, chartViewModelFactory).get(ChartViewModel.class);



    }

    @Override
    public void onResume() {
        Log.d("myfragments", this.getClass().getSimpleName()+ "onStart: ");

        viewModelConfig();
        super.onResume();
    }

    private void viewModelConfig() {
        Company company;
        if (getArguments() != null) {
            company = (Company) (getArguments().getSerializable(COMPANY_SYMBOL));
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
        mBinding.setLifecycleOwner(this);
        mBinding.setVm(mViewModel);
        mToolbar = mBinding.toolbar;
        mKChartView = mBinding.kchartView;
        kChartViewConfig();
        snackbar = Snackbar.make(Objects.requireNonNull(getActivity()).findViewById(android.R.id.content), getResources().getString(R.string.no_connect), Snackbar.LENGTH_LONG);
        return mBinding.getRoot();
    }

    private void kChartViewConfig() {
        mKChartView.showLoading();
        KChartAdapter mAdapter = new KChartAdapter();
        mKChartView.setAdapter(mAdapter);
        mKChartView.setDateTimeFormatter(new TimeFormatter());
        mKChartView.setGridRows(getResources().getInteger(R.integer.rows));
        mKChartView.setGridColumns(getResources().getInteger(R.integer.columns));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(mToolbar);
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setDisplayShowTitleEnabled(false);
        NavController navController = Navigation.findNavController(mBinding.getRoot());
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.action_topTenFragment_to_chartFragment ).build();
        NavigationUI.setupActionBarWithNavController((AppCompatActivity) getActivity(), navController, appBarConfiguration);
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


    }

    private void loadData(Company company) {
        if (LiveConnectUtil.getInstance().isInternetOn()) {
            mViewModel.loadData(company);
        }

    }

    @Override
    public void onStop() {
        Log.d("myfragments", this.getClass().getSimpleName()+ "onStop: ");
        mViewModel.dispatchDetach();
        super.onStop();
    }
}
