package ru.itbirds.data.repositories;

import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ResetRepository {
    private FirebaseAuth mAuth;


    public ResetRepository(FirebaseAuth auth) {
        mAuth = auth;
    }



    private MutableLiveData<ResetProgress> mResetProgress;

    public LiveData<ResetProgress> resetPassword(@NonNull String login) {
        if (mResetProgress != null && mResetProgress.getValue() == ResetProgress.IN_PROGRESS) {
            return mResetProgress;
        }
        mResetProgress = new MutableLiveData<>(ResetProgress.IN_PROGRESS);
        reset(login);
        return mResetProgress;
    }

    private void reset(@NonNull final String login) {
        mAuth.sendPasswordResetEmail(login)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        mResetProgress.postValue(ResetProgress.SUCCESS);
                    } else {
                        mResetProgress.postValue(ResetProgress.FAILED);

                    }
                });
    }

    public enum ResetProgress {
        IN_PROGRESS,
        SUCCESS,
        FAILED
    }
}
