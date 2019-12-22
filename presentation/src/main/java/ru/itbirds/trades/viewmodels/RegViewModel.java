package ru.itbirds.trades.viewmodels;

import android.text.TextUtils;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import ru.itbirds.data.repositories.RegRepository;

public class RegViewModel extends ViewModel {

    private ObservableBoolean progress = new ObservableBoolean();

    private RegRepository mRegRepository;
    private MediatorLiveData<RegState> mRegState = new MediatorLiveData<>();

    public RegViewModel() {
        mRegState.setValue(RegState.NONE);
        mRegRepository = RegRepository.getInstance();
    }

    public MediatorLiveData<RegState> getProgressLive() {
        return mRegState;
    }

    public void createAccount(String name, String login, String password) {
        if (!(TextUtils.isEmpty(name) || TextUtils.isEmpty(login) || TextUtils.isEmpty(password)) && mRegState.getValue() != RegState.IN_PROGRESS) {
            requestReg(name, login, password);
        } else if (mRegState.getValue() != RegState.IN_PROGRESS) {
            mRegState.postValue(RegState.ERROR);
        }
    }

    private void requestReg(String name, String login, String password) {
        mRegState.postValue(RegState.IN_PROGRESS);
        setProgress(true);
        final LiveData<RegRepository.RegProgress> progressLiveData = mRegRepository.createAccount(name, login, password);
        mRegState.addSource(progressLiveData, regProgress -> {
            if (regProgress == RegRepository.RegProgress.SUCCESS) {
                mRegState.postValue(RegState.SUCCESS);
                mRegState.removeSource(progressLiveData);
                setProgress(false);
            } else if (regProgress == RegRepository.RegProgress.FAILED) {
                mRegState.postValue(RegState.FAILED);
                mRegState.removeSource(progressLiveData);
                setProgress(false);
            }
        });
    }


    public ObservableBoolean getProgress() {
        return progress;
    }

    public void setProgress(boolean p) {
        progress.set(p);
    }

    public enum RegState {
        NONE,
        ERROR,
        IN_PROGRESS,
        SUCCESS,
        FAILED
    }
}