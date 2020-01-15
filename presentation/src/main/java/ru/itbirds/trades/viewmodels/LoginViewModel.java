package ru.itbirds.trades.viewmodels;

import android.text.Editable;
import android.text.TextUtils;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import ru.itbirds.data.repositories.LoginRepository;
import ru.itbirds.domain.usecase.LoginUseCase;
import ru.itbirds.trades.R;

public class LoginViewModel extends ViewModel {

    private MediatorLiveData<LoginState> mLoginState = new MediatorLiveData<>();
    private LoginUseCase mLoginUseCase;
    private ObservableBoolean progress = new ObservableBoolean();

    public LoginViewModel(LoginUseCase loginUseCase) {
        mLoginState.setValue(LoginState.NONE);
        mLoginUseCase = loginUseCase;
    }

    public LiveData<LoginState> getProgressState() {
        return mLoginState;
    }

    public void login(Editable login, Editable password) {
        if (TextUtils.isEmpty(login) || TextUtils.isEmpty(password)) {
            mLoginState.setValue(LoginState.ERROR);
        } else if (mLoginState.getValue() != LoginState.IN_PROGRESS) {
            requestLogin(login.toString(), password.toString());
        }
    }

    private void requestLogin(String login, String password) {
        mLoginState.postValue(LoginState.IN_PROGRESS);
        setProgress(true);
        LiveData<LoginRepository.AuthProgress> source = mLoginUseCase.loginAccount(login, password);
        mLoginState.addSource(source, authProgress -> {
            if (authProgress == LoginRepository.AuthProgress.SUCCESS) {
                mLoginState.postValue(LoginState.SUCCESS);
                mLoginState.removeSource(source);
                setProgress(false);
            } else if (authProgress == LoginRepository.AuthProgress.FAILED) {
                mLoginState.postValue(LoginState.FAILED);
                mLoginState.removeSource(source);
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

    public void getRemoteConfig() {
        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(0)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.configs);
        mFirebaseRemoteConfig.fetchAndActivate();
    }

    public enum LoginState {
        NONE,
        ERROR,
        IN_PROGRESS,
        SUCCESS,
        FAILED
    }

}

