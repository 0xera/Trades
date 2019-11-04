package ru.itbirds.trades.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

import javax.inject.Inject;

import ru.itbirds.trades.App;
import ru.itbirds.trades.R;
import ru.itbirds.trades.adapter.ViewPagerAdapter;
import ru.itbirds.trades.common.INavigator;
import ru.itbirds.trades.databinding.TopBinding;
import ru.itbirds.trades.model.Company;
import ru.itbirds.trades.util.LiveConnectUtil;
import ru.itbirds.trades.viewmodels.TopTenViewModel;
import ru.itbirds.trades.viewmodels.TopTenViewModelFactory;

import static ru.itbirds.trades.util.Constants.ACTIVE_FR;
import static ru.itbirds.trades.util.Constants.COMPANY;
import static ru.itbirds.trades.util.Constants.GAINERS_FR;
import static ru.itbirds.trades.util.Constants.LOSERS_FR;
import static ru.itbirds.trades.util.Constants.SEARCH_QUERY;

public class TopTenFragment extends Fragment implements INavigator {
    private TopTenViewModel mViewModel;
    private TopBinding mBinding;
    private Toolbar mToolbar;
    private String mQuery;
    @Inject
    TopTenViewModelFactory topTenViewModelFactory;
    private Snackbar snackbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getAppComponent().inject(this);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        mViewModel = ViewModelProviders.of(this, topTenViewModelFactory).get(TopTenViewModel.class);
        if (savedInstanceState != null) {
            if (!TextUtils.isEmpty(savedInstanceState.getString(SEARCH_QUERY))) {
                mQuery = savedInstanceState.getString(SEARCH_QUERY);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = TopBinding.inflate(inflater, container, false);
        TabLayout tabLayout = mBinding.tabs;
        ViewPager viewPager = mBinding.viewPager;
        snackbar = Snackbar.make(Objects.requireNonNull(getActivity()).findViewById(android.R.id.content), getResources().getString(R.string.no_connect), Snackbar.LENGTH_LONG);
        LiveConnectUtil.getInstance().observe(this, aBoolean -> {
            if (aBoolean) {
                snackbar.dismiss();

            } else {
                snackbar.show();
            }
        });
        mToolbar = mBinding.toolbar;
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFragment(PageFragment.newInstance(ACTIVE_FR), ACTIVE_FR);
        viewPagerAdapter.addFragment(PageFragment.newInstance(GAINERS_FR), GAINERS_FR);
        viewPagerAdapter.addFragment(PageFragment.newInstance(LOSERS_FR), LOSERS_FR);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(3);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel.setOnclickListener(this);
        mBinding.setLifecycleOwner(this);
        mBinding.setVm(mViewModel);

        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(mToolbar);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.search_item);
        SearchView searchView = (SearchView) item.getActionView();
        if (!TextUtils.isEmpty(mQuery)) {
            item.expandActionView();
            searchView.setQuery(mQuery, false);
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (LiveConnectUtil.getInstance().isInternetOn())
                    mViewModel.checkSearchInput(query);
                else createToast("No internet connection");
                return true;

            }

            @Override
            public boolean onQueryTextChange(String query) {
                mQuery = query;
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
    public void onDetach() {
        mViewModel.dispatchDetach();
        super.onDetach();
    }


    @Override
    public void clickForNavigate(Company company) {
        if (company != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(COMPANY, company);
            Navigation.findNavController(mBinding.getRoot()).navigate(R.id.chartFragment, bundle);
        } else {
            createToast("Not found");
        }

    }

    void createToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
