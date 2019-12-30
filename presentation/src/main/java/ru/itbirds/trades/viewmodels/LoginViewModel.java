package ru.itbirds.trades.viewmodels;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import ru.itbirds.data.repositories.AuthRepository;

public class LoginViewModel extends ViewModel {

    private MediatorLiveData<LoginState> mLoginState = new MediatorLiveData<>();
    private AuthRepository authRepository;
    private ObservableBoolean progress = new ObservableBoolean();

    public LoginViewModel() {
        mLoginState.setValue(LoginState.NONE);
        authRepository = AuthRepository.getInstance();
    }

    public LiveData<LoginState> getProgressState() {
        return mLoginState;
    }

    public void login(String login, String password) {
        if (mLoginState.getValue() != LoginState.IN_PROGRESS) {
            requestLogin(login, password);
        }
    }

    private void requestLogin(String login, String password) {
        mLoginState.postValue(LoginState.IN_PROGRESS);
        setProgress(true);
        final LiveData<AuthRepository.AuthProgress> progressLiveData = authRepository.loginAccount(login, password);
        mLoginState.addSource(progressLiveData, authProgress -> {
            if (authProgress == AuthRepository.AuthProgress.SUCCESS) {
                mLoginState.postValue(LoginState.SUCCESS);
                mLoginState.removeSource(progressLiveData);
                setProgress(false);
            } else if (authProgress == AuthRepository.AuthProgress.FAILED) {
                mLoginState.postValue(LoginState.FAILED);
                mLoginState.removeSource(progressLiveData);
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

    public enum LoginState {
        NONE,
        ERROR,
        IN_PROGRESS,
        SUCCESS,
        FAILED
    }

}

