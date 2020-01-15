package ru.itbirds.trades.viewmodels;

import android.text.Editable;
import android.text.TextUtils;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import ru.itbirds.data.repositories.ResetRepository;
import ru.itbirds.domain.usecase.ResetUseCase;

public class ResetViewModel extends ViewModel {
    private ObservableBoolean progress = new ObservableBoolean();

    private ResetUseCase mResetUseCase;
    private MediatorLiveData<ResetState> mResetState = new MediatorLiveData<>();

    public ResetViewModel(ResetUseCase resetUseCase) {
        mResetState.setValue(ResetState.NONE);
        mResetUseCase = resetUseCase;
    }

    public MediatorLiveData<ResetState> getProgressLive() {
        return mResetState;
    }

    public void resetPassword(Editable login) {
        if (!TextUtils.isEmpty(login) && mResetState.getValue() != ResetState.IN_PROGRESS) {
            reset(login.toString());
        } else if (mResetState.getValue() != ResetState.IN_PROGRESS) {
            mResetState.postValue(ResetState.ERROR);
        }
    }

    private void reset(String login) {
        mResetState.postValue(ResetState.IN_PROGRESS);
        setProgress(true);
        LiveData<ResetRepository.ResetProgress> source = mResetUseCase.resetPassword(login);
        mResetState.addSource(source, regProgress -> {
            if (regProgress == ResetRepository.ResetProgress.SUCCESS) {
                mResetState.postValue(ResetState.SUCCESS);
                mResetState.removeSource(source);
                setProgress(false);
            } else if (regProgress == ResetRepository.ResetProgress.FAILED) {
                mResetState.postValue(ResetState.FAILED);
                mResetState.removeSource(source);
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
