package ru.itbirds.trades.common;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import ru.itbirds.trades.R;
import ru.itbirds.trades.ui.LoginFragment;
import ru.itbirds.trades.ui.TopTenFragment;


public class SingleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);
        findViewById(android.R.id.content).getRootView().setBackgroundColor(ContextCompat.getColor(this, R.color.chart_background));
        if (FirebaseAuth.getInstance().getCurrentUser() != null && FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
            changeFragment(TopTenFragment.newInstance(), false);
        } else {
            changeFragment(LoginFragment.newInstance(), false);
        }
    }

    public void changeFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
        transaction.replace(R.id.container, fragment);
        if (addToBackStack) {
            transaction.addToBackStack(fragment.getClass().getSimpleName());
        }

        transaction.commit();
    }

    public void popBackStack(boolean inclusive) {
        if (inclusive) getSupportFragmentManager()
                .popBackStack
                        (getSupportFragmentManager().getBackStackEntryAt(
                                getSupportFragmentManager().getBackStackEntryCount() - 1).getName(),
                                FragmentManager.POP_BACK_STACK_INCLUSIVE);
        else getSupportFragmentManager()
                .popBackStack();
    }

}


