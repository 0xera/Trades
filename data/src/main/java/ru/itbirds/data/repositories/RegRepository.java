package ru.itbirds.data.repositories;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


public class RegRepository {
    private static FirebaseAuth mAuth;
    private static volatile RegRepository instance;

    private RegRepository() {
        mAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    public static RegRepository getInstance() {
        RegRepository localInstance = instance;
        if (localInstance == null) {
            synchronized (RegRepository.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new RegRepository();
                }
            }
        }
        return localInstance;
    }

    private MutableLiveData<RegProgress> mRegProgress;

    public LiveData<RegProgress> createAccount(@NonNull String name, @NonNull String login, @NonNull String password) {
        if (mRegProgress != null && mRegProgress.getValue() == RegRepository.RegProgress.IN_PROGRESS) {
            return mRegProgress;
        }
        mRegProgress = new MutableLiveData<>(RegProgress.IN_PROGRESS);
        createAccount(mRegProgress, name, login, password);
        return mRegProgress;
    }

    private void createAccount(final MutableLiveData<RegProgress> progress, @NonNull final String name, @NonNull final String login, @NonNull final String password) {
        mAuth.createUserWithEmailAndPassword(login, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            user.sendEmailVerification()
                                    .addOnCompleteListener(task1 -> progress.postValue(RegProgress.SUCCESS))
                                    .addOnFailureListener(e -> progress.postValue(RegProgress.FAILED));
                            user.updateProfile(new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .build());
                        }
                    } else {
                        progress.postValue(RegProgress.FAILED);
                    }
                });
    }

  public   enum RegProgress {
        IN_PROGRESS,
        SUCCESS,
        FAILED
    }
}
