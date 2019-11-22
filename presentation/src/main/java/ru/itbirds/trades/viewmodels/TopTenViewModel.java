package ru.itbirds.trades.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import ru.itbirds.data.model.Company;
import ru.itbirds.domain.interactor.CleanInteractor;
import ru.itbirds.domain.interactor.CompanyInteractor;
import ru.itbirds.trades.common.INavigator;
import ru.itbirds.trades.util.LiveConnectUtil;

import static io.fabric.sdk.android.Fabric.TAG;


public class TopTenViewModel extends ViewModel {
    private INavigator mNavigator;
    private Disposable mDisposableSearch;
    private CompanyInteractor mCompanyInteractor;
    private CleanInteractor mCleanInteractor;
    private MediatorLiveData<Company> mCompanyMediatorLive = new MediatorLiveData<>();

    TopTenViewModel(CompanyInteractor companyInteractor, CleanInteractor cleanInteractor) {

        mCompanyInteractor = companyInteractor;
        mCleanInteractor = cleanInteractor;
        mCompanyMediatorLive.observeForever(company -> mCompany = company);
    }

    private Company mCompany = null;


    public void cleanOldData() {
        mCleanInteractor.clean();
    }

    public void searchCompanyLive(String symbol) {
        LiveData<Company> companyLiveData = mCompanyInteractor.getCompany(symbol);
        mCompanyMediatorLive.addSource(companyLiveData, company -> {
            mCompany = company;
            if (company == null) mCompanyMediatorLive.removeSource(companyLiveData);
            else mCompanyMediatorLive.setValue(company);
        });


    }

    public void checkSearchInput(final String query) {
        if (mCompany == null) {
            if (LiveConnectUtil.getInstance().isInternetOn()) {
                mDisposableSearch = mCompanyInteractor.downloadCompany(query)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(company1 -> mNavigator.clickForNavigate(company1.getSymbol()), throwable -> Log.d(TAG, "checkSearchInput: boom" + throwable.getMessage()));
            } else mNavigator.clickForNavigate(null);
        } else {
            mNavigator.clickForNavigate(mCompany.getSymbol());
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
