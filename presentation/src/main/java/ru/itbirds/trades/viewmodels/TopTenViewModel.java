package ru.itbirds.trades.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import ru.itbirds.data.model.Company;
import ru.itbirds.domain.usecase.CleanUseCase;
import ru.itbirds.domain.usecase.CompanyUseCase;
import ru.itbirds.trades.common.INavigator;
import ru.itbirds.trades.util.LiveConnectUtil;


public class TopTenViewModel extends ViewModel {
    private INavigator mNavigator;
    private Disposable mDisposableSearch;
    private Disposable mDisposableClean;
    private CompanyUseCase mCompanyUseCase;
    private CleanUseCase mCleanUseCase;
    private MediatorLiveData<Company> mCompanyMediatorLive = new MediatorLiveData<>();

    public TopTenViewModel(CompanyUseCase companyInteractor, CleanUseCase cleanInteractor) {

        mCompanyUseCase = companyInteractor;
        mCleanUseCase = cleanInteractor;
        mCompanyMediatorLive.observeForever(company -> mCompany = company);
    }

    private Company mCompany = null;


    public void cleanOldData() {
        mDisposableClean = mCleanUseCase.clean();
    }

    public void searchCompanyLive(String symbol) {
        LiveData<Company> companyLiveData = mCompanyUseCase.getCompany(symbol);
        mCompanyMediatorLive.addSource(companyLiveData, company -> {
            mCompany = company;
            if (company == null) mCompanyMediatorLive.removeSource(companyLiveData);
            else mCompanyMediatorLive.setValue(company);
        });


    }

    public void checkSearchInput(final String query) {
        if (mCompany == null) {
            if (LiveConnectUtil.getInstance().isInternetOn()) {
                mDisposableSearch = mCompanyUseCase.downloadCompany(query)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(company1 -> mNavigator.clickForNavigate(company1.getSymbol()), throwable -> mNavigator.clickForNavigate(throwable));
            } else mNavigator.clickForNavigate("");
        } else {
            mNavigator.clickForNavigate(mCompany.getSymbol());
        }

    }


    public void dispatchDetach() {
        if (mDisposableSearch != null) {
            mDisposableSearch.dispose();
        }
        if (mDisposableClean != null) {
            mDisposableClean.dispose();
        }
    }

    public void setOnclickListener(INavigator navigator) {
        mNavigator = navigator;
    }

}
