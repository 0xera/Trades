package ru.itbirds.trades.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import ru.itbirds.trades.R;
import ru.itbirds.trades.common.SingleActivity;
import ru.itbirds.trades.databinding.RegBinding;
import ru.itbirds.trades.util.LiveConnectUtil;
import ru.itbirds.trades.viewmodels.RegViewModel;

import static android.app.Activity.RESULT_OK;


public class RegFragment extends Fragment {
    private static final int RESULT_LOAD_IMAGE = 101;
    private RegViewModel mRegViewModel;
    private RegBinding mBinding;
    private boolean isGeneratedImage = true;
    private Random mRnd = ThreadLocalRandom.current();
    private boolean isDefaultImage = true;
    private byte[] mImageBytes;
    private AlertDialog mAlertVerifyDialog;
    private AlertDialog mAlertErrorDialog;

    public static RegFragment newInstance() {
        return new RegFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mRegViewModel = ViewModelProviders.of(this).get(RegViewModel.class);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Objects.requireNonNull(getActivity()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = RegBinding.inflate(inflater, container, false);
        mBinding.btnRegistration.setOnClickListener(view -> createAccount());
        mBinding.ivProfile.setOnClickListener(v -> openGallery());
        textWatcherForEditTexts();
        mBinding.setVm(mRegViewModel);
        configToolbar();
        mBinding.recreateImage.setOnClickListener(v -> generateImage(String.valueOf(Objects.requireNonNull(mBinding.nameReg.getText()).toString().charAt(0))));
        mBinding.resetImage.setOnClickListener(v -> {
            mBinding.setType(0);
            isGeneratedImage = true;
            isDefaultImage = true;
            mBinding.ivProfile.setImageResource(R.drawable.ic_person_black_24dp);
            createProfileImage(mBinding.nameReg.getText());
        });
        return mBinding.getRoot();
    }

    private void createAccount() {
        if (TextUtils.isEmpty(mBinding.textInputPasswordAgainToggle.getError()) && TextUtils.isEmpty(mBinding.nameRegLayout.getError()) && TextUtils.isEmpty(mBinding.loginRegLayout.getError())) {
            if (!(TextUtils.isEmpty(mBinding.nameReg.getText()) || TextUtils.isEmpty(mBinding.loginReg.getText()) || TextUtils.isEmpty(mBinding.passwordReg.getText())))
                mRegViewModel.createAccount(mBinding.nameReg.getText().toString(), mBinding.loginReg.getText().toString(), mBinding.passwordReg.getText().toString(), mImageBytes);
            else createAlertDialogError(R.string.fill_fields);
        } else createAlertDialogError(R.string.fill_fields);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, RESULT_LOAD_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
                && null != data) {
            isGeneratedImage = false;
            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getContext()).getContentResolver(), data.getData());
                mBinding.ivProfile.setImageBitmap(bitmap);
                mBinding.setType(2);
                uploadImage(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void configToolbar() {
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(mBinding.toolbar);
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setDisplayShowTitleEnabled(false);
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        mBinding.toolbar.setNavigationOnClickListener(view -> popUp());
    }


    @Override
    public void onResume() {
        progressObserve();
        connectObserve();
        super.onResume();
    }

    private void connectObserve() {
//        Snackbar snackbar = Snackbar.make(Objects.requireNonNull(getActivity()).findViewById(android.R.id.content), this.getString(R.string.no_connect), Snackbar.LENGTH_LONG);
        Toast toast = Toast.makeText(getActivity(), this.getString(R.string.no_connect), Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        LiveConnectUtil.getInstance().observe(this, aBoolean -> {
            if (aBoolean) {
                toast.cancel();
            } else {
                toast.show();

            }
        });
    }

    private void progressObserve() {
        mRegViewModel.getProgressLive().observe(this, regState -> {
            if (regState == RegViewModel.RegState.FAILED) {
                createAlertDialogError(R.string.reg_failed);
            } else if (regState == RegViewModel.RegState.ERROR) {
                createAlertDialogError(R.string.fill_fields);
            } else if (regState == RegViewModel.RegState.SUCCESS) {
                createAlertDialogVerify();
                if (!mAlertVerifyDialog.isShowing())
                    mAlertVerifyDialog.show();

            }
        });
    }

    private void popUp() {
        ((SingleActivity) Objects.requireNonNull(getActivity())).popBackStack(false);
    }

    private void createAlertDialogVerify() {
        if (mAlertVerifyDialog == null) {
            mAlertVerifyDialog = new AlertDialog.Builder(Objects.requireNonNull(getActivity()), R.style.DialogTheme)
                    .setPositiveButton("Ok", (dialog, which) -> popUp())
                    .setTitle(R.string.verify_email_message)
                    .create();
        }
        mAlertVerifyDialog.setMessage(getString(R.string.click_link_message) + "\n" + Objects.requireNonNull(mBinding.loginReg.getText()).toString());


    }

    private void createAlertDialogError(@StringRes int message) {
        if (mAlertErrorDialog == null) {
            mAlertErrorDialog = new AlertDialog.Builder(Objects.requireNonNull(getActivity()), R.style.DialogTheme)
                    .setPositiveButton("Ok", null)
                    .setTitle(R.string.error)
                    .create();
        }
        mAlertErrorDialog.setMessage(getString(message));
        if (!mAlertErrorDialog.isShowing())
            mAlertErrorDialog.show();

    }

    @Override
    public void onStop() {
        LiveConnectUtil.getInstance().removeObservers(this);
        super.onStop();
    }

    private void textWatcherForEditTexts() {
        mBinding.nameReg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                createProfileImage(s);
                if (s.toString().contains(" ")) {
                    mBinding.nameRegLayout.setError(getString(R.string.space_error));
                } else {
                    mBinding.nameRegLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String result = s.toString().replaceAll(" ", "");
                if (!s.toString().equals(result)) {
                    mBinding.nameReg.setText(result);
                    mBinding.nameReg.setSelection(result.length());
                }
            }
        });
        mBinding.loginReg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(" ")) {
                    mBinding.loginRegLayout.setError(getString(R.string.space_error));
                } else if (s.length() > 0 && !Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                    mBinding.loginRegLayout.setError(getString(R.string.is_not_email));
                } else {
                    mBinding.loginRegLayout.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                String result = s.toString().replaceAll(" ", "");
                if (!s.toString().equals(result)) {
                    mBinding.loginReg.setText(result);
                    mBinding.loginReg.setSelection(result.length());
                }

            }
        });
        mBinding.passwordReg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mBinding.passwordReg.getText() != null && !s.toString().equals(mBinding.passwordReg.getText().toString())) {
                    mBinding.textInputPasswordAgainToggle.setError(getString(R.string.passwords_are_not_same));
                } else {
                    mBinding.textInputPasswordAgainToggle.setErrorEnabled(false);
                }
                if (s.length() <= 7) {
                    mBinding.textInputPasswordToggle.setError(getString(R.string.password_lenght_error));
                } else if (s.length() > 7) {
                    mBinding.textInputPasswordToggle.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mBinding.passwordRegAgain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mBinding.passwordReg.getText() != null && !s.toString().equals(mBinding.passwordReg.getText().toString())) {
                    mBinding.textInputPasswordAgainToggle.setError(getString(R.string.passwords_are_not_same));
                } else {
                    mBinding.textInputPasswordAgainToggle.setErrorEnabled(false);
                }
                if (mBinding.passwordReg.getText().toString().length() < 7) {
                    mBinding.textInputPasswordToggle.setError(getString(R.string.password_lenght_error));
                } else if (mBinding.passwordReg.getText().toString().length() > 7) {
                    mBinding.textInputPasswordToggle.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void createProfileImage(CharSequence name) {
        if (isGeneratedImage) {
            if (!TextUtils.isEmpty(name) && name.length() > 0 && isDefaultImage) {
                generateImage(String.valueOf(name.charAt(0)));
                isDefaultImage = false;
            } else if (TextUtils.isEmpty(name)) {
                isDefaultImage = true;
                mBinding.setType(0);
                mBinding.ivProfile.setImageResource(R.drawable.ic_person_black_24dp);
            }
        }
    }

    private void generateImage(String name) {
        Bitmap bitmap = Bitmap.createBitmap(mBinding.ivProfile.getWidth(), mBinding.ivProfile.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawARGB(255, mRnd.nextInt(200), mRnd.nextInt(200), mRnd.nextInt(200));
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setARGB(255, mRnd.nextInt(256), mRnd.nextInt(256), mRnd.nextInt(256));
        Rect textBounds = new Rect();
        String text = name.toUpperCase();
        paint.setTextSize(getResources().getDimension(R.dimen.avatar_text_size));
        paint.getTextBounds(text, 0, 1, textBounds);
        canvas.drawText(text, bitmap.getWidth() / 2f - paint.measureText(text) / 2f, bitmap.getHeight() / 2f + textBounds.height() / 2f, paint);
        mBinding.ivProfile.setImageBitmap(bitmap);
        mBinding.setType(1);
        uploadImage(bitmap);
    }

    private void uploadImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        mImageBytes = byteArrayOutputStream.toByteArray();
    }


}
