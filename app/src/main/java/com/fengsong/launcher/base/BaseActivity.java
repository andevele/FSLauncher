package com.fengsong.launcher.base;

import android.os.Bundle;
import android.os.SystemProperties;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.fengsong.launcher.R;
import com.fengsong.launcher.control.ControlManager;
import com.fengsong.launcher.net.NetworkMonitor;
import com.fengsong.launcher.util.LogUtils;
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
        LogUtils.d("zhulf","===this22: " + this.toString());
        if(this.toString().contains("FsMainActivity")) {
            updateView(R.string.page_home,R.drawable.ic_home);
        } else if(this.toString().contains("AppsActivity")) {
            updateView(R.string.page_apps,R.drawable.ic_apps);
        }
        SystemProperties.set("persist.sys.intvmode", "0");
        goToStorageSource();
        super.onResume();
    }

    private void updateView(int stringId,int imageId) {
        TextView pageLabel = (TextView) findViewById(R.id.page_label);
        ImageView pageIcon = (ImageView)findViewById(R.id.page_icon);
        pageLabel.setText(getResources().getString(stringId));
        pageIcon.setImageResource(imageId);
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
