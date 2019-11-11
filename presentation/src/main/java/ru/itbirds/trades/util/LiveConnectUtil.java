package ru.itbirds.trades.util;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.lifecycle.LiveData;

import java.util.Objects;


public class LiveConnectUtil extends LiveData<Boolean> {

    private static volatile LiveConnectUtil instance;
    private Application application;
    private BroadcastReceiver broadcastReceiver;

    public static LiveConnectUtil getInstance() {
        LiveConnectUtil localInstance = instance;
        if (localInstance == null) {
            synchronized (LiveConnectUtil.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new LiveConnectUtil();
                }
            }
        }
        return localInstance;
    }

    private LiveConnectUtil() {
    }


    public void init(Application app) {
        application = app;
    }

    public boolean isInternetOn() {
        ConnectivityManager connectivityManager = (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = Objects.requireNonNull(connectivityManager).getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    protected void onActive() {
        registerBroadCastReceiver();
    }

    @Override
    protected void onInactive() {
        unRegisterBroadCastReceiver();
    }


    private void registerBroadCastReceiver() {
        if (broadcastReceiver == null) {
            IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    Bundle extra = intent.getExtras();
                    NetworkInfo info = Objects.requireNonNull(extra).getParcelable("networkInfo");
                    setValue(Objects.requireNonNull(info).getState() == NetworkInfo.State.CONNECTED);
                }
            };
            application.registerReceiver(broadcastReceiver, filter);
        }
    }

    private void unRegisterBroadCastReceiver() {
        if (broadcastReceiver != null) {
            application.unregisterReceiver(broadcastReceiver);
            broadcastReceiver = null;
        }
    }
}
