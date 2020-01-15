package ru.itbirds.data.repositories;

import android.text.TextUtils;

import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


public class LoginRepository {
    private FirebaseAuth mAuth;


    public LoginRepository(FirebaseAuth auth) {
        mAuth = auth;
    }


    private MutableLiveData<AuthProgress> mAuthProgress;

    public LiveData<AuthProgress> loginAccount(@NonNull String login, @NonNull String password) {
        if (mAuth.getCurrentUser() != null && mAuth.getCurrentUser().getEmail() != null && mAuthProgress != null) {
            if (TextUtils.equals(login, mAuth.getCurrentUser().getEmail()) && mAuthProgress.getValue() == AuthProgress.IN_PROGRESS) {
                return mAuthProgress;
            } else {
                mAuthProgress.postValue(AuthProgress.FAILED);
            }
        }
        mAuthProgress = new MutableLiveData<>(AuthProgress.IN_PROGRESS);
        login(login, password);
        return mAuthProgress;
    }

    private void login(@NonNull final String login, @NonNull final String password) {
        mAuth.signInWithEmailAndPassword(login, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && mAuth.getCurrentUser() != null && mAuth.getCurrentUser().isEmailVerified()) {
                        mAuthProgress.postValue(AuthProgress.SUCCESS);
                    } else {
                        mAuthProgress.postValue(AuthProgress.FAILED);

                    }
                });
    }

    public enum AuthProgress {
        IN_PROGRESS,
        SUCCESS,
        FAILED
    }
}