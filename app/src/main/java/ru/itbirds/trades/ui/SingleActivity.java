package ru.itbirds.trades.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import ru.itbirds.trades.R;


public class SingleActivity extends AppCompatActivity {
    private NavController navController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.single_activity);
        navController = Navigation.findNavController(this, R.id.nav_host);
    }

    //    getSupportFragmentManager().addOnBackStackChangedListener(this);
//        if (savedInstanceState == null) {
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            fragmentManager.beginTransaction()
//                    .replace(R.layout.single_activity, getFragment())
//                    .commit();
//        }


//    @NonNull
//    private Fragment getFragment() {
//        return new TopTenFragment();
//    }

//    @Override
//    public void onBackStackChanged() {
//        boolean canGoBack = getSupportFragmentManager().getBackStackEntryCount() > 0;
//        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(canGoBack);
//    }

    @Override
    public boolean onSupportNavigateUp() {
        navController.navigateUp();
        return true;
    }
}

