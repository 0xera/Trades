package ru.itbirds.trades.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;
import ru.itbirds.trades.R;
import ru.itbirds.trades.adapter.ViewPagerAdapter;
import ru.itbirds.trades.common.App;
import ru.itbirds.trades.common.INavigator;
import ru.itbirds.trades.databinding.TopBinding;
import ru.itbirds.trades.util.LiveConnectUtil;
import ru.itbirds.trades.viewmodels.TopTenViewModel;
import ru.itbirds.trades.viewmodels.TopTenViewModelFactory;

import static ru.itbirds.trades.util.Constants.ACTIVE_FR;
import static ru.itbirds.trades.util.Constants.COMPANY_SYMBOL;
import static ru.itbirds.trades.util.Constants.GAINERS_FR;
import static ru.itbirds.trades.util.Constants.LOSERS_FR;
import static ru.itbirds.trades.util.Constants.SEARCH_QUERY;

public class TopTenFragment extends Fragment implements INavigator {
    private TopTenViewModel mViewModel;
    private TopBinding mBinding;
    private String mQuery;
    @Inject
    TopTenViewModelFactory topTenViewModelFactory;
    private SearchView searchView;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getAppComponent().inject(this);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        viewModelConfig();
        if (savedInstanceState != null) {
            if (!TextUtils.isEmpty(savedInstanceState.getString(SEARCH_QUERY))) {
                mQuery = savedInstanceState.getString(SEARCH_QUERY);
            }
        }
    }

    private void viewModelConfig() {
        mViewModel = ViewModelProviders.of(this, topTenViewModelFactory).get(TopTenViewModel.class);
        mViewModel.cleanOldData();
        mViewModel.setOnclickListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = TopBinding.inflate(inflater, container, false);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(mBinding.toolbar);
        mViewPager = mBinding.viewPager;
        mTabLayout = mBinding.tabs;
        mBinding.setLifecycleOwner(this);
        mBinding.setVm(mViewModel);
        viewPagerConfig();
        return mBinding.getRoot();
    }

    @Override
    public void onStart() {
        Snackbar snackbar = Snackbar.make(Objects.requireNonNull(getActivity()).findViewById(android.R.id.content), getResources().getString(R.string.no_connect), Snackbar.LENGTH_LONG);
        LiveConnectUtil.getInstance().observe(this, aBoolean -> {
            if (aBoolean) {
                snackbar.dismiss();
            } else {
                if (!snackbar.isShown())
                    snackbar.show();

            }
        });
        super.onStart();
    }

    private void viewPagerConfig() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFragment(PageFragment.newInstance(ACTIVE_FR), ACTIVE_FR);
        viewPagerAdapter.addFragment(PageFragment.newInstance(GAINERS_FR), GAINERS_FR);
        viewPagerAdapter.addFragment(PageFragment.newInstance(LOSERS_FR), LOSERS_FR);
        mViewPager.setAdapter(viewPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(3);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.search_item);
        searchView = (SearchView) item.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        if (!TextUtils.isEmpty(mQuery)) {
            item.expandActionView();
            searchView.setQuery(mQuery, false);
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mViewModel.checkSearchInput(query.toUpperCase());
                return true;

            }

            @Override
            public boolean onQueryTextChange(String query) {
                mQuery = query;
                mViewModel.searchCompanyLive(mQuery.toUpperCase());
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SEARCH_QUERY, mQuery);
    }

    @Override
    public void onStop() {
        Log.d("myfragments", this.getClass().getSimpleName() + "onStop: ");
        mViewModel.dispatchDetach();
        LiveConnectUtil.getInstance().removeObservers(this);
        super.onStop();
    }

    @Override
    public void clickForNavigate(String symbol) {
        if (!TextUtils.isEmpty(symbol)) {
            searchView.clearFocus();
            Bundle bundle = new Bundle();
            bundle.putString(COMPANY_SYMBOL, symbol);
            Navigation.findNavController(mBinding.getRoot()).navigate(R.id.action_topTenFragment_to_chartFragment, bundle);
        } else {
            if (LiveConnectUtil.getInstance().isInternetOn())
                createToast(getResources().getString(R.string.no_found_ticker));
            else createToast(getResources().getString(R.string.no_connect));
        }

    }

    void createToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
