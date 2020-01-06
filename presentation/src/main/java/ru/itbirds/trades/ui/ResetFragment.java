package ru.itbirds.trades.ui;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import ru.itbirds.trades.databinding.ResetBinding;
import ru.itbirds.trades.util.LiveConnectUtil;
import ru.itbirds.trades.viewmodels.ResetViewModel;

import static ru.itbirds.data.Constants.LOGIN_RESET;


public class ResetFragment extends Fragment {
    private ResetViewModel mResetViewModel;
    private ResetBinding mBinding;
    private String mLogin;
    private AlertDialog mAlertErrorDialog;
    private AlertDialog mAlertResetDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mResetViewModel = ViewModelProviders.of(this).get(ResetViewModel.class);
        if (getArguments() != null) {
            mLogin = getArguments().getString(LOGIN_RESET);
        }
        if (savedInstanceState != null) {
            if (!TextUtils.isEmpty(savedInstanceState.getString(LOGIN_RESET)))
                mLogin = savedInstanceState.getString(LOGIN_RESET);
        }
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
        mBinding = ResetBinding.inflate(inflater, container, false);
        mBinding.btnReset.setOnClickListener(view -> {
            if (TextUtils.isEmpty(mBinding.loginResetLayout.getError()) && !TextUtils.isEmpty(mBinding.loginReset.getText()))
                mResetViewModel.resetPassword(mBinding.loginReset.getText().toString());
            else createAlertDialogError(R.string.fill_field);
        });
        textWatcherForEditText();
        mBinding.loginReset.setText(mLogin);
        mBinding.setVm(mResetViewModel);
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
        mResetViewModel.getProgressLive().observe(this, resetState -> {
            if (resetState == ResetViewModel.ResetState.FAILED) {
                createAlertDialogError(R.string.email_not_found);
            } else if (resetState == ResetViewModel.ResetState.ERROR) {
                createAlertDialogError(R.string.fill_field);
            } else if (resetState == ResetViewModel.ResetState.SUCCESS) {
                createAlertDialogReset();
                if (!mAlertResetDialog.isShowing())
                    mAlertResetDialog.show();
            }
        });
    }

    private void createAlertDialogReset() {
        if (mAlertResetDialog == null) {
            mAlertResetDialog = new AlertDialog.Builder(Objects.requireNonNull(getActivity()), R.style.DialogTheme)
                    .setPositiveButton(R.string.positive_ok, (dialog, which) -> Navigation.findNavController(mBinding.getRoot()).popBackStack())
                    .setTitle(R.string.check_email_message)
                    .create();
        }
        mAlertResetDialog.setMessage(getString(R.string.instructions_message) + "\n" + Objects.requireNonNull(mBinding.loginReset.getText()).toString());


    }

    @Override
    public void onStop() {
        LiveConnectUtil.getInstance().removeObservers(this);
        super.onStop();
    }

    private void textWatcherForEditText() {
        mBinding.loginReset.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(" ")) {
                    mBinding.loginResetLayout.setError(getString(R.string.space_error));
                } else if (s.length() > 0 && !Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                    mBinding.loginResetLayout.setError(getString(R.string.is_not_email));
                } else {
                    mBinding.loginResetLayout.setErrorEnabled(false);
                }
                mLogin = s.toString();

            }

            @Override
            public void afterTextChanged(Editable s) {
                String result = s.toString().replaceAll(" ", "");
                if (!s.toString().equals(result)) {
                    mBinding.loginReset.setText(result);
                    mBinding.loginReset.setSelection(result.length());
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(LOGIN_RESET, mLogin);
        super.onSaveInstanceState(outState);
    }
}
