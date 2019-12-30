package ru.itbirds.trades.viewmodels;

import android.text.TextUtils;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import ru.itbirds.data.repositories.ResetRepository;

public class ResetViewModel extends ViewModel {
    private ObservableBoolean progress = new ObservableBoolean();

    private ResetRepository mResetRepository;
    private MediatorLiveData<ResetState> mResetState = new MediatorLiveData<>();

    public ResetViewModel() {
        mResetState.setValue(ResetState.NONE);
        mResetRepository = ResetRepository.getInstance();
    }

    public MediatorLiveData<ResetState> getProgressLive() {
        return mResetState;
    }

    public void resetPassword(String login) {
        if (!TextUtils.isEmpty(login) && mResetState.getValue() != ResetState.IN_PROGRESS) {
            reset(login);
        } else if (mResetState.getValue() != ResetState.IN_PROGRESS) {
            mResetState.postValue(ResetState.ERROR);
        }
    }

    private void reset(String login) {
        mResetState.postValue(ResetState.IN_PROGRESS);
        setProgress(true);
        final LiveData<ResetRepository.ResetProgress> progressLiveData = mResetRepository.resetPassword(login);
        mResetState.addSource(progressLiveData, regProgress -> {
            if (regProgress == ResetRepository.ResetProgress.SUCCESS) {
                mResetState.postValue(ResetState.SUCCESS);
                mResetState.removeSource(progressLiveData);
                setProgress(false);
            } else if (regProgress == ResetRepository.ResetProgress.FAILED) {
                mResetState.postValue(ResetState.FAILED);
                mResetState.removeSource(progressLiveData);
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

    public enum ResetState {
        NONE,
        ERROR,
        IN_PROGRESS,
        SUCCESS,
        FAILED
    }
}
