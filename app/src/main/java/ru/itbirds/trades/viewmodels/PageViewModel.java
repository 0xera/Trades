package ru.itbirds.trades.viewmodels;

import android.util.Log;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.itbirds.trades.model.Company;
import ru.itbirds.trades.model.CompanyStock;
import ru.itbirds.trades.repository.LocalRepository;
import ru.itbirds.trades.repository.RemoteRepository;

import static ru.itbirds.trades.util.Constants.ACTIVE_FR;
import static ru.itbirds.trades.util.Constants.GAINERS_FR;
import static ru.itbirds.trades.util.Constants.LOSERS_FR;


public class PageViewModel extends ViewModel {
    private final RemoteRepository mRemoteRepository;
    private final LocalRepository mLocalRepository;
    private Disposable mDisposableGainer;
    private Disposable mDisposableActive;
    private Disposable mDisposableLosers;

    public void setCompanyStock(List<Company> companyStock) {
        this.companyStock.postValue(companyStock);
    }

    private MutableLiveData<List<Company>> companyStock = new MutableLiveData<>();
    private ObservableBoolean progress = new ObservableBoolean();

    private ObservableBoolean noInternet = new ObservableBoolean();
    private String mType;

    public LiveData<CompanyStock> getCompanyStockLive(String type) {
        if(companyStockLive == null){
            companyStockLive = mLocalRepository.getCompanyList(type);
        }
        return companyStockLive;
    }

    private LiveData<CompanyStock> companyStockLive;

    public PageViewModel(RemoteRepository remoteRepository, LocalRepository localRepository) {
        mRemoteRepository = remoteRepository;
        mLocalRepository = localRepository;
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
                gainersUI();
                break;
            case ACTIVE_FR:
                activeUI();
                break;
            case LOSERS_FR:
                losersUI();
                break;
        }
    }

    private void gainersUI() {
        mDisposableGainer = mRemoteRepository.getGainers()
                .doAfterSuccess(companies -> mLocalRepository.insertCompanyList(companies, mType))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .repeatWhen(objectFlowable -> objectFlowable.delay(2, TimeUnit.SECONDS))
                .subscribe(companies -> {
                            mLocalRepository.insertCompanyList(companies, mType);
                            setProgress(false);
                        },
                        throwable -> {
                            setProgress(false);
                            setNoInternet(true);
                            Log.d("gainers", "gainersUI: " + throwable.getMessage());
                        });
    }


    private void activeUI() {
        mDisposableActive = mRemoteRepository.getMostActive()
                .doAfterSuccess(companies -> mLocalRepository.insertCompanyList(companies, mType))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    Log.d("active", "activeUI: doonsub");
                })
                .repeatWhen(objectFlowable -> objectFlowable.delay(2, TimeUnit.SECONDS))
                .subscribe(companies -> {
                            mLocalRepository.insertCompanyList(companies, mType);
                            setProgress(false);
                        },
                        throwable -> {
                            setProgress(false);
                            setNoInternet(true);
                            Log.d("active", "activeUI: error");
                        });
    }

    private void losersUI() {
        mDisposableLosers = mRemoteRepository.getLosers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .repeatWhen(objectFlowable -> objectFlowable.delay(2, TimeUnit.SECONDS))
                .subscribe(companies -> {
                            mLocalRepository.insertCompanyList(companies, mType);
                            setProgress(false);
                        },
                        throwable -> {
                            setProgress(false);
                            setNoInternet(true);
                            Log.d("loser", "losersUI: error");
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
