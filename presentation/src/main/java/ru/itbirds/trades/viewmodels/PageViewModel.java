package ru.itbirds.trades.viewmodels;

import android.util.Log;

import java.util.Arrays;
import java.util.List;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.disposables.Disposable;
import ru.itbirds.data.model.Company;
import ru.itbirds.data.model.CompanyStock;
import ru.itbirds.domain.interactor.CompanyStockInteractor;

import static ru.itbirds.trades.util.Constants.ACTIVE_FR;
import static ru.itbirds.trades.util.Constants.GAINERS_FR;
import static ru.itbirds.trades.util.Constants.LOSERS_FR;


public class PageViewModel extends ViewModel {
    private Disposable mDisposableGainer;
    private Disposable mDisposableActive;
    private Disposable mDisposableLosers;
    private CompanyStockInteractor mCompanyStockInteractor;

    public PageViewModel(CompanyStockInteractor companyStockInteractor) {

        mCompanyStockInteractor = companyStockInteractor;
    }

    public void setCompanyStock(List<Company> companyStock) {
        this.companyStock.postValue(companyStock);
    }

    private MutableLiveData<List<Company>> companyStock = new MutableLiveData<>();
    private ObservableBoolean progress = new ObservableBoolean();

    private ObservableBoolean noInternet = new ObservableBoolean();
    private String mType;

    private LiveData<CompanyStock> companyStockLive;

    public LiveData<CompanyStock> getCompanyStockLive(String type) {
        if (companyStockLive == null) {
            companyStockLive = mCompanyStockInteractor.getCompanyStock(type);
        }
        return companyStockLive;
    }

    public MutableLiveData<List<Company>> getCompanyStock() {
        return companyStock;
    }


    public void loadData(String type) {
        mType = type;
        setProgress(true);
        setNoInternet(false);
        switch (type) {
            case GAINERS_FR:
                loadGainers();
                break;
            case ACTIVE_FR:
                loadActive();
                break;
            case LOSERS_FR:
                loadLosers();
                break;
        }
    }

    private void loadGainers() {
        mDisposableGainer = mCompanyStockInteractor.getGainers()
                .subscribe(companies -> {
                            mCompanyStockInteractor.insertCompanyStock(companies, mType);
                            setProgress(false);
                        },
                        throwable -> {
                            setProgress(false);
                            setNoInternet(true);
                        });
    }


    private void loadActive() {
        mDisposableActive = mCompanyStockInteractor.getMostActive()
                .subscribe(companies -> {
                            mCompanyStockInteractor.insertCompanyStock(companies, mType);
                            setProgress(false);
                        },
                        throwable -> {
                            setProgress(false);
                            setNoInternet(true);
                        });
    }

    private void loadLosers() {
        mDisposableLosers = mCompanyStockInteractor.getLosers()
                .subscribe(companies -> {
                            mCompanyStockInteractor.insertCompanyStock(companies, mType);
                            setProgress(false);
                        },
                        throwable -> {
                            setProgress(false);
                            setNoInternet(true);
                        });
    }

    public void dispatchDetach() {
        if (mDisposableGainer != null) {
            mDisposableGainer.dispose();
        }
        if (mDisposableActive != null) {
            mDisposableActive.dispose();
        }
        if (mDisposableLosers != null) {
            mDisposableLosers.dispose();
        }
    }

    public ObservableBoolean getProgress() {
        return progress;
    }

    public void setProgress(boolean p) {
        progress.set(p);
    }

    public ObservableBoolean getNoInternet() {
        return noInternet;
    }

    public void setNoInternet(Boolean ni) {
        noInternet.set(ni);
    }
}
