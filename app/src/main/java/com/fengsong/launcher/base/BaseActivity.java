package com.fengsong.launcher.base;

import android.os.Bundle;
import android.os.SystemProperties;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.fengsong.launcher.R;
import com.fengsong.launcher.control.ControlManager;
import com.fengsong.launcher.net.NetworkMonitor;
import com.fengsong.launcher.view.TopBar;
import com.mstar.android.tv.TvCommonManager;

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
        SystemProperties.set("persist.sys.intvmode", "0");
        goToStorageSource();
    }

    private void goToStorageSource() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                ControlManager.getInstance().setInputSource(TvCommonManager.INPUT_SOURCE_STORAGE);
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mNetworkMonitor != null) {
            mNetworkMonitor.stopMonitor();
        }
    }
}
