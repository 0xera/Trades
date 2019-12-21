package ru.itbirds.data.repositories;

import android.text.TextUtils;

import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


public class AuthRepository {
    private FirebaseAuth mAuth;

    private static volatile AuthRepository instance;

    private AuthRepository() {
        mAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    public static AuthRepository getInstance() {
        AuthRepository localInstance = instance;
        if (localInstance == null) {
            synchronized (AuthRepository.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new AuthRepository();
                }
            }
        }
        return localInstance;
    }


    private MutableLiveData<AuthProgress> mAuthProgress;

    public LiveData<AuthProgress> login(@NonNull String login, @NonNull String password) {
        if (mAuth.getCurrentUser() != null && mAuth.getCurrentUser().getEmail() != null && mAuthProgress != null) {
            if (TextUtils.equals(login, mAuth.getCurrentUser().getEmail()) && mAuthProgress.getValue() == AuthProgress.IN_PROGRESS) {
                return mAuthProgress;
            } else {
                mAuthProgress.postValue(AuthProgress.FAILED);
            }
        }
        mAuthProgress = new MutableLiveData<>(AuthProgress.IN_PROGRESS);
        login(mAuthProgress, login, password);
        return mAuthProgress;
    }

    private void login(final MutableLiveData<AuthProgress> progress,
                       @NonNull final String login, @NonNull final String password) {
        mAuth.signInWithEmailAndPassword(login, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && mAuth.getCurrentUser() != null && mAuth.getCurrentUser().isEmailVerified()) {
                        progress.postValue(AuthProgress.SUCCESS);
                    } else {
                        progress.postValue(AuthProgress.FAILED);

                    }
                });
    }

    public enum AuthProgress {
        IN_PROGRESS,
        SUCCESS,
        FAILED
    }
}