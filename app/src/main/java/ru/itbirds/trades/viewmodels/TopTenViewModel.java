package ru.itbirds.trades.viewmodels;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import ru.itbirds.trades.common.INavigator;
import ru.itbirds.trades.repository.RemoteRepository;

public class TopTenViewModel extends ViewModel {
    private INavigator mNavigator;
    private Disposable mDisposableSearch;
    private final RemoteRepository mRemoteRepository;

    public TopTenViewModel(RemoteRepository remoteRepository) {
        mRemoteRepository = remoteRepository;

    }

    public void checkSearchInput(final String query) {
        mDisposableSearch = mRemoteRepository.getCompany(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> mNavigator.clickForNavigate(null))
                .subscribe(company -> mNavigator.clickForNavigate(company),
                        throwable -> Log.d("ViewModelTop", "checkSearchInput: nu takoe"));

    }


    public void dispatchDetach() {
        if (mDisposableSearch != null) {
            mDisposableSearch.dispose();
        }
    }

    public void setOnclickListener(INavigator navigator) {
        mNavigator = navigator;
    }
}
