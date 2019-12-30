package ru.itbirds.trades.ui;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import ru.itbirds.trades.R;
import ru.itbirds.trades.databinding.LoginBinding;
import ru.itbirds.trades.util.LiveConnectUtil;
import ru.itbirds.trades.viewmodels.LoginViewModel;

import static ru.itbirds.data.Constants.LOGIN_RESET;


public class LoginFragment extends Fragment {

    private LoginViewModel mLoginViewModel;
    private LoginBinding mBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = LoginBinding.inflate(inflater, container, false);
        mBinding.setVm(mLoginViewModel);
        mBinding.loginBtn.setOnClickListener(v -> {
            if (!(TextUtils.isEmpty(mBinding.etLogin.getText()) || TextUtils.isEmpty(mBinding.etPassword.getText()))) {
                InputMethodManager imm = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
                Objects.requireNonNull(imm).hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                mLoginViewModel.login(mBinding.etLogin.getText().toString(), mBinding.etPassword.getText().toString());
            } else {
                createSnackbar(R.string.empty_login_password);
            }
        });
        mBinding.tvForgot.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(mBinding.etLogin.getText())) {
                navigateToReset(mBinding.etLogin.getText().toString());
            } else {
                navigateToReset(null);
            }
        });
        textWatcherForEditTexts();
        mBinding.btnToRegistration.setOnClickListener(v -> navigateToReg());
        return mBinding.getRoot();
    }

    private void textWatcherForEditTexts() {
        mBinding.etLogin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(" ")) {
                    mBinding.loginLayout.setError(getString(R.string.space_error));
                } else if (s.length() > 0 && !Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                    mBinding.loginLayout.setError(getString(R.string.is_not_email));
                } else {
                    mBinding.loginLayout.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                String result = s.toString().replaceAll(" ", "");
                if (!s.toString().equals(result)) {
                    mBinding.etLogin.setText(result);
                    mBinding.etLogin.setSelection(result.length());
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Objects.requireNonNull(getActivity()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        progressObserve();
        connectObserve();
    }

    private void progressObserve() {
        mLoginViewModel.getProgressState().observe(this, loginState -> {
            if (loginState == LoginViewModel.LoginState.FAILED) {
                createSnackbar(R.string.failed_login);
            } else if (loginState == LoginViewModel.LoginState.ERROR) {
                createSnackbar(R.string.empty_login_password);
            } else if (loginState == LoginViewModel.LoginState.SUCCESS) {
                createSnackbar(R.string.success);
                navigateToTop();

            }
        });
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

    private void navigateToTop() {
        Navigation.findNavController(mBinding.getRoot()).navigate(R.id.action_loginFragment_to_topTenFragment);
    }

    private void navigateToReset(@Nullable String login) {
        Bundle bundle = new Bundle();
        bundle.putString(LOGIN_RESET, login);
        Navigation.findNavController(mBinding.getRoot()).navigate(R.id.action_loginFragment_to_resetFragment, bundle);
    }

    private void createSnackbar(@StringRes int message) {
        Snackbar.make(Objects.requireNonNull(getActivity()).findViewById(android.R.id.content), this.getString(message), Snackbar.LENGTH_LONG).show();
    }


    private void navigateToReg() {
        Navigation.findNavController(mBinding.getRoot()).navigate(R.id.action_loginFragment_to_regFragment);
    }

    @Override
    public void onStop() {
        mLoginViewModel.getProgressState().removeObservers(this);
        LiveConnectUtil.getInstance().removeObservers(this);
        super.onStop();
    }
}
