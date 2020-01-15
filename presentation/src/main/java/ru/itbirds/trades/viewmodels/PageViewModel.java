package ru.itbirds.trades.viewmodels;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.disposables.Disposable;
import ru.itbirds.data.model.Company;
import ru.itbirds.data.model.CompanyStock;
import ru.itbirds.domain.usecase.CompanyStockUseCase;

import static ru.itbirds.data.Constants.ACTIVE_FR;
import static ru.itbirds.data.Constants.GAINERS_FR;
import static ru.itbirds.data.Constants.LOSERS_FR;


public class PageViewModel extends ViewModel {
    private Disposable mDisposableGainer;
    private Disposable mDisposableActive;
    private Disposable mDisposableLosers;
    private CompanyStockUseCase mCompanyStockUseCase;

    public PageViewModel(CompanyStockUseCase companyStockUseCase) {

        mCompanyStockUseCase = companyStockUseCase;
    }

    public void setCompanyStock(List<Company> companyStock) {
        this.companyStock.postValue(companyStock);
    }

    private MutableLiveData<List<Company>> companyStock = new MutableLiveData<>();
    private MutableLiveData<Boolean> progress = new MutableLiveData<>(true);
    private MutableLiveData<Boolean> noInternet = new MutableLiveData<>(false);

    private String mType;

    private LiveData<CompanyStock> companyStockLive;

    public LiveData<CompanyStock> getCompanyStockLive(String type) {
        if (companyStockLive == null) {
            companyStockLive = mCompanyStockUseCase.getCompanyStock(type);
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
        mDisposableGainer = mCompanyStockUseCase.getGainers(progress, progress);
    }


    private void loadActive() {
        mDisposableActive = mCompanyStockUseCase.getMostActive(progress, noInternet);
    }

    private void loadLosers() {
        mDisposableLosers = mCompanyStockUseCase.getLosers(progress, noInternet);
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

    public MutableLiveData<Boolean> getProgress() {
        return progress;
    }

    public void setProgress(boolean value) {
        progress.postValue(value);
    }

    public MutableLiveData<Boolean> getNoInternet() {
        return noInternet;
    }

    public void setNoInternet(Boolean ni) {
        noInternet.postValue(ni);
    }
}
