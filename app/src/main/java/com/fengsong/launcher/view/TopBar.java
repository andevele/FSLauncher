package com.fengsong.launcher.view;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.fengsong.launcher.R;
import com.fengsong.launcher.net.NetworkMonitor;
import com.fengsong.launcher.util.ConstantResource;

public class TopBar extends RelativeLayout implements NetworkMonitor.INetworkUpdateListener {
    private Context mContext;
    private ImageView mNetworkIcon;

    public TopBar(Context context) {
        super(context);
    }

    public TopBar(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public TopBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initViews(context);
    }

    private void initViews(Context context) {
        LayoutInflater.from(context).inflate(R.layout.top_bar, this);
        this.setFocusable(false);
        mNetworkIcon = (ImageView) findViewById(R.id.network_label);
        mNetworkIcon.setFocusable(false);
        checkNetWork();
    }

    private void checkNetWork() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isWifiOn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isAvailable();
        boolean wifiConnect = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
        boolean ethernetConnect = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET).isConnectedOrConnecting();
        Log.d("zhujj", "==isWifiOn: " + isWifiOn +
                " wifiConnect: " + wifiConnect);
        if (!wifiConnect && !ethernetConnect) {
            if (isWifiOn) {
                mNetworkIcon.setImageResource(R.drawable.ic_wifi_disconnected);
            } else {
                mNetworkIcon.setImageResource(R.drawable.ic_eth_disconnected);
            }
        } else if (wifiConnect) {
            mNetworkIcon.setImageResource(R.drawable.ic_wifi_level_4);
        } else if (ethernetConnect) {
            mNetworkIcon.setImageResource(R.drawable.ic_eth_connected);
        }
    }

    @Override
    public void onUpdateNetworkConnectivity(Bundle newConnectivity) {
        int mCurrentInterfaceId = newConnectivity.getInt(NetworkMonitor.KEY_NET_INTERFACE_ID, NetworkMonitor.ID_INTERFACE_ETHERNET);
        final int statusId = newConnectivity.getInt(NetworkMonitor.KEY_NET_STATUS_ID, NetworkMonitor.ID_STATUS_DISCONNECTED);
        Log.d("zhujj", "=====mCurrentInterfaceId:" + mCurrentInterfaceId + " statusId:" + statusId);
        switch (mCurrentInterfaceId) {
            case NetworkMonitor.ID_INTERFACE_ETHERNET:
                switch (statusId) {
                    case NetworkMonitor.ID_STATUS_DISCONNECTED:
                        mNetworkIcon
                                .setImageResource(R.drawable.ic_eth_disconnected);
                        break;
                    case NetworkMonitor.ID_STATUS_CONNECTED:
                        mNetworkIcon
                                .setImageResource(R.drawable.ic_eth_connected);
                        break;
                    case NetworkMonitor.ID_STATUS_UNREACHABLE:
                        mNetworkIcon
                                .setImageResource(R.drawable.ic_eth_unreachable);
                        break;
                }
                break;
            case NetworkMonitor.ID_INTERFACE_WIFI:
                final int wifiLevel = newConnectivity.getInt(NetworkMonitor.KEY_NET_WIFI_LEVEL);
                switch (statusId) {
                    case NetworkMonitor.ID_STATUS_DISCONNECTED:
                    case NetworkMonitor.ID_STATUS_UNREACHABLE:
                        setNetworkStatusImg();
                        break;
                    case NetworkMonitor.ID_STATUS_CONNECTED:
//                	mNetworkIcon.setImageResource(R.drawable.conn_stat_wifi_signal_0);
                        mNetworkIcon.setImageResource(ConstantResource.WIFI_SIGNAL_LEVLES[wifiLevel]);
                        break;
                }
                break;
        }
    }

    @Override
    public void onUpdateUSBConnectivity(String action) {

    }

    private void setNetworkStatusImg() {
        ConnectivityManager con = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean wifi = con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
        boolean internet = con.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET).isConnectedOrConnecting();
        if(internet) {
            mNetworkIcon.setImageResource(R.drawable.ic_eth_connected);
        } else {
            mNetworkIcon.setImageResource(R.drawable.ic_eth_disconnected);
        }
    }
}
