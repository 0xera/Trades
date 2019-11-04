package ru.itbirds.trades.viewmodels;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.itbirds.trades.common.INavigator;
import ru.itbirds.trades.model.Company;
import ru.itbirds.trades.repository.LocalRepository;
import ru.itbirds.trades.repository.RemoteRepository;

public class TopTenViewModel extends ViewModel {
    private INavigator mNavigator;
    private Disposable mDisposableSearch;
    private final RemoteRepository mRemoteRepository;

    public LocalRepository getmLocalRepository() {
        return mLocalRepository;
    }

    private final LocalRepository mLocalRepository;

    public void setCompany(Company company) {
        this.mCompany = company;
    }

    private Company mCompany;

    public TopTenViewModel(RemoteRepository remoteRepository, LocalRepository localRepository) {
        mRemoteRepository = remoteRepository;
        mLocalRepository = localRepository;
    }

    public void checkSearchInput(final String query) {
        if (mCompany == null) {
            mDisposableSearch = mRemoteRepository.getCompany(query)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(throwable -> mNavigator.clickForNavigate(null))
                    .subscribe(company1 -> mNavigator.clickForNavigate(company1),
                            throwable -> Log.d("ViewModelTop", "checkSearchInput: nu takoe"));
        } else {
            mNavigator.clickForNavigate(mCompany);
        }

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
