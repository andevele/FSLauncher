package com.fengsong.launcher.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.fengsong.launcher.R;
import com.fengsong.launcher.net.NetworkMonitor;
import com.fengsong.launcher.view.TopBar;

public class BaseActivity extends AppCompatActivity {
    protected NetworkMonitor mNetworkMonitor;
    private NetworkMonitor.INetworkUpdateListener mNetworkUpdateListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TopBar topBar = (TopBar) findViewById(R.id.topbar_container);
        mNetworkUpdateListener = (NetworkMonitor.INetworkUpdateListener) topBar;
        if(mNetworkMonitor == null) {
            mNetworkMonitor = new NetworkMonitor(this, mNetworkUpdateListener);
        }
        mNetworkMonitor.startMonitor();
        mNetworkMonitor.checkUsb();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mNetworkMonitor != null) {
            mNetworkMonitor.stopMonitor();
        }
    }
}
