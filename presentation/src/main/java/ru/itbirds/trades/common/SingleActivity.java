package ru.itbirds.trades.common;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import ru.itbirds.trades.R;


public class SingleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.single_activity);
        NavController navController = Navigation.findNavController(this, R.id.nav_host);
        if (FirebaseAuth.getInstance().getCurrentUser() != null && FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
            navController.setGraph(R.navigation.nav_graph);
        }

    }

}

