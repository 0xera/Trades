package ru.itbirds.trades.viewmodels;

import android.text.Editable;
import android.text.TextUtils;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import ru.itbirds.data.repositories.RegRepository;
import ru.itbirds.domain.usecase.RegUseCase;

public class RegViewModel extends ViewModel {

    private ObservableBoolean progress = new ObservableBoolean();

    private RegUseCase mRegUseCase;
    private MediatorLiveData<RegState> mRegState = new MediatorLiveData<>();

    public RegViewModel(RegUseCase regUseCase) {
        mRegUseCase = regUseCase;
        mRegState.setValue(RegState.NONE);
    }

    public MediatorLiveData<RegState> getProgressLive() {
        return mRegState;
    }

    public void createAccount(Editable name, Editable login, Editable password, byte[] imageBytes) {
        if (!(TextUtils.isEmpty(name) || TextUtils.isEmpty(login) || TextUtils.isEmpty(password)) && mRegState.getValue() != RegState.IN_PROGRESS) {
            requestReg(name.toString(), login.toString(), password.toString(), imageBytes);
        } else if (mRegState.getValue() != RegState.IN_PROGRESS) {
            mRegState.postValue(RegState.ERROR);
        }
    }

    private void requestReg(String name, String login, String password, byte[] imageBytes) {
        mRegState.postValue(RegState.IN_PROGRESS);
        setProgress(true);
        LiveData<RegRepository.RegProgress> source = mRegUseCase.createAccount(name, login, password, imageBytes);
        mRegState.addSource(source, regProgress -> {
            if (regProgress == RegRepository.RegProgress.SUCCESS) {
                mRegState.postValue(RegState.SUCCESS);
                mRegState.removeSource(source);
                setProgress(false);
            } else if (regProgress == RegRepository.RegProgress.FAILED) {
                mRegState.postValue(RegState.FAILED);
                mRegState.removeSource(source);
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