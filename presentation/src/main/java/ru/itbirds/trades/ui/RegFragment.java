package ru.itbirds.trades.ui;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import ru.itbirds.trades.R;
import ru.itbirds.trades.databinding.RegBinding;
import ru.itbirds.trades.util.LiveConnectUtil;
import ru.itbirds.trades.viewmodels.RegViewModel;


public class RegFragment extends Fragment {
    private RegViewModel mRegViewModel;
    private RegBinding mBinding;

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
        mBinding.btnRegistration.setOnClickListener(view -> {
            if (TextUtils.isEmpty(mBinding.textInputPasswordAgainToggle.getError()) && TextUtils.isEmpty(mBinding.nameRegLayout.getError()) && TextUtils.isEmpty(mBinding.loginRegLayout.getError()))
                mRegViewModel.createAccount(mBinding.nameReg.getText().toString(), mBinding.loginReg.getText().toString(), mBinding.passwordReg.getText().toString());
            else createSnackbar(R.string.fill_fields);
        });
        textWatcherForEditTexts();
        mBinding.setVm(mRegViewModel);
        configToolbar();
        return mBinding.getRoot();
    }

    private void configToolbar() {
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(mBinding.toolbar);
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setDisplayShowTitleEnabled(false);
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        mBinding.toolbar.setNavigationOnClickListener(view -> Navigation.findNavController(view).navigateUp());
    }


    private void createSnackbar(@StringRes int message) {
        Snackbar.make(Objects.requireNonNull(getActivity()).findViewById(android.R.id.content), this.getString(message), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onResume() {
        progressObserve();
        connectObserve();
        super.onResume();
    }

    private void connectObserve() {
        Snackbar snackbar = Snackbar.make(Objects.requireNonNull(getActivity()).findViewById(android.R.id.content), this.getString(R.string.no_connect), Snackbar.LENGTH_LONG);
        LiveConnectUtil.getInstance().observe(this, aBoolean -> {
            if (aBoolean) {
                snackbar.dismiss();
            } else {
                if (!snackbar.isShown())
                    snackbar.show();

            }
        });
    }

    private void progressObserve() {
        mRegViewModel.getProgressLive().observe(this, regState -> {
            if (regState == RegViewModel.RegState.FAILED) {
                createSnackbar(R.string.reg_failed);
            } else if (regState == RegViewModel.RegState.ERROR) {
                createSnackbar(R.string.fill_fields);
            } else if (regState == RegViewModel.RegState.SUCCESS) {
                createSnackbar(R.string.success);
                new AlertDialog.Builder(Objects.requireNonNull(getActivity()))
                        .setPositiveButton("Ok", (dialog, which) -> Navigation.findNavController(mBinding.getRoot()).popBackStack())
                        .setMessage(getString(R.string.click_link_message) + " " + mBinding.loginReg.getText().toString())
                        .setTitle(R.string.verify_email_message)
                        .create().show();
            }
        });
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
                if (s.toString().contains(" ")) {
                    mBinding.nameRegLayout.setError(getString(R.string.space_error));
                } else {
                    mBinding.nameRegLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

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

            }
        });
        mBinding.passwordRegAgain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(mBinding.passwordReg.getText().toString())) {
                    mBinding.textInputPasswordAgainToggle.setError(getString(R.string.passwords_are_not_same));
                } else {
                    mBinding.textInputPasswordAgainToggle.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mBinding.passwordReg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(mBinding.passwordReg.getText().toString())) {
                    mBinding.textInputPasswordAgainToggle.setError(getString(R.string.passwords_are_not_same));
                } else {
                    mBinding.textInputPasswordAgainToggle.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
