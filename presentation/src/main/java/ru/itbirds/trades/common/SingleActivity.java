package ru.itbirds.trades.common;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
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

    @Override
    public boolean onSupportNavigateUp() {
        Log.d("badabum", "onSupportNavigateUp: clicked");
        return navController.navigateUp() || super.onSupportNavigateUp();
    }


}

