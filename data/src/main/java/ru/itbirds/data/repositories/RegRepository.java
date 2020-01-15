package ru.itbirds.data.repositories;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


public class RegRepository {
    private static FirebaseAuth mAuth;
    private static FirebaseStorage mStorage;
    private FirebaseUser mUser;


    public RegRepository(FirebaseAuth auth, FirebaseStorage storage) {
        mStorage = storage;
        mAuth = auth;
    }

    private MutableLiveData<RegProgress> mRegProgress;

    public LiveData<RegProgress> createAccount(@NonNull String name, @NonNull String login, @NonNull String password, byte[] imageBytes) {
        if (mRegProgress != null && mRegProgress.getValue() == RegRepository.RegProgress.IN_PROGRESS) {
            return mRegProgress;
        }
        mRegProgress = new MutableLiveData<>(RegProgress.IN_PROGRESS);
        creUser(name, login, password, imageBytes);
        return mRegProgress;
    }

    private void creUser(@NonNull final String name, @NonNull final String login, @NonNull final String password, byte[] imageBytes) {
        mAuth.createUserWithEmailAndPassword(login, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        mUser = mAuth.getCurrentUser();
                        if (mUser != null) {
                            mUser.sendEmailVerification()
                                    .addOnCompleteListener(task1 -> uploadImage(name, imageBytes))
                                    .addOnFailureListener(e -> mRegProgress.postValue(RegProgress.FAILED));
                        }
                    } else {
                        mRegProgress.postValue(RegProgress.FAILED);
                    }
                });
    }

    private void uploadImage(@NonNull String name, byte[] imageBytes) {
        StorageReference child = mStorage.getReference("pics").child(mUser.getUid());
        child.putBytes(imageBytes)
                .addOnCompleteListener(task2 ->
                        addUserInfo(name, child))
                .addOnFailureListener(e -> mRegProgress.postValue(RegProgress.FAILED));
    }

    private void addUserInfo(@NonNull String name, StorageReference child) {
        child.getDownloadUrl()
                .addOnCompleteListener(task21 -> {
                    UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                            .setDisplayName(name)
                            .setPhotoUri(task21.getResult())
                            .build();
                    mUser.updateProfile(profileUpdate);
                    mRegProgress.postValue(RegProgress.SUCCESS);
                }).addOnFailureListener(e -> mRegProgress.postValue(RegProgress.FAILED));
    }


    public enum RegProgress {
        IN_PROGRESS,
        SUCCESS,
        FAILED
    }
}
