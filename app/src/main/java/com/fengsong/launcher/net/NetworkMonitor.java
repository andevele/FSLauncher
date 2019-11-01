package com.fengsong.launcher.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;

import com.fengsong.launcher.base.BaseActivity;

import java.lang.ref.WeakReference;

public class NetworkMonitor extends BroadcastReceiver {
    private static final String TAG = "NetworkMonitor";
    // Network Kind and value
    public static final String KEY_NET_INTERFACE_ID = "NetworkMonitor.interface";
    public static final int ID_INTERFACE_ETHERNET = 0;
    public static final int ID_INTERFACE_WIFI = 1;

    // Network status and value
    public static final String KEY_NET_STATUS_ID = "NetworkMonitor.Status";
    public static final int ID_STATUS_DISCONNECTED = 0;
    public static final int ID_STATUS_UNREACHABLE = 1;
    public static final int ID_STATUS_CONNECTED = 2;

    // WIFI RSSI
    public static final String KEY_NET_WIFI_LEVEL = "NetworkMonitor.wifi.level";
    public static final int WIFI_LEVEL_COUNT = 4;
    private INetworkUpdateListener mListener;
    private WeakReference<Context> mContextRef;
    private boolean mWifiEnabled, mWifiConnected;
    private boolean mEthHWConnected, mEthReachable;
    private int mWifiLevel = 0;
    private int mWifiRssi;
    private int mActiveInterface = ID_INTERFACE_ETHERNET;
    private int mActiveStatus = ID_STATUS_DISCONNECTED;
    // flag:0 indicate unregister; 1 register
    private boolean flag = false;

    public NetworkMonitor(BaseActivity activity, INetworkUpdateListener mNetworkUpdateListener) {
        mContextRef = new WeakReference<Context>(activity);
        this.mListener = mNetworkUpdateListener;
    }

    public interface INetworkUpdateListener {
        void onUpdateNetworkConnectivity(Bundle newConnectivity);
        void onUpdateUSBConnectivity(String action);
    }

    public void startMonitor() {
        IntentFilter netWorkFilter = new IntentFilter();
        netWorkFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        netWorkFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        netWorkFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        netWorkFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);

        Context context = mContextRef.get();
        if (context != null && flag == false) {
            context.registerReceiver(this, netWorkFilter);
            flag = true;
        }
    }

    public void stopMonitor() {
        Context context = mContextRef.get();
        if (context != null && flag == true) {
            flag = false;
            context.unregisterReceiver(this);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String netWorkAction = intent.getAction();
        Log.d("zhujj","==netWorkAction: " + netWorkAction);
        switch (netWorkAction) {
            case ConnectivityManager.CONNECTIVITY_ACTION:
                mWifiEnabled = intent.getIntExtra(ConnectivityManager.EXTRA_NETWORK_TYPE, ConnectivityManager.TYPE_ETHERNET) == ConnectivityManager.TYPE_WIFI;
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
                if (isConnected) {
                    mEthHWConnected = true;
                    boolean isWifi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
                    if (isWifi) {
                        mWifiEnabled = true;
                    }
                } else {
                    mEthHWConnected = false;
                }
                break;
            case WifiManager.WIFI_STATE_CHANGED_ACTION:
                mWifiEnabled = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN) == WifiManager.WIFI_STATE_ENABLED;
                break;
            case WifiManager.NETWORK_STATE_CHANGED_ACTION:
                final NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                mWifiConnected = networkInfo != null && networkInfo.isConnected();
                mWifiLevel = 0;
                mWifiRssi = -200;
                break;
            case WifiManager.RSSI_CHANGED_ACTION:
                if (mWifiConnected) {
                    mWifiRssi = intent.getIntExtra(WifiManager.EXTRA_NEW_RSSI, -200);
                    mWifiLevel = WifiManager.calculateSignalLevel(mWifiRssi, WIFI_LEVEL_COUNT);
                }
                break;
                default:
                    break;
        }
        updateActiveNetwork();
        if (mListener != null) {
            Log.d("zhujj","==500==");
            mListener.onUpdateNetworkConnectivity(getCurrentConnectivityInfo());
        } else {
            Log.d("zhujj","====501==");
        }
    }

    private Bundle getCurrentConnectivityInfo() {
        Bundle mCurrentConnectivity = new Bundle();
        mCurrentConnectivity.putInt(KEY_NET_INTERFACE_ID, mActiveInterface);
        mCurrentConnectivity.putInt(KEY_NET_STATUS_ID, mActiveStatus);
        if (mActiveInterface == ID_INTERFACE_WIFI) {
            mCurrentConnectivity.putInt(KEY_NET_WIFI_LEVEL, mWifiLevel);
        }

        Log.d("zhujj", "NetworkMonitor.interface:" + mActiveInterface + "NetworkMonitor.Status:" + mActiveStatus + "NetworkMonitor.wifi.level:" + mWifiLevel);

        return mCurrentConnectivity;
    }

    private void updateActiveNetwork() {
        if (mWifiEnabled) {
            mActiveInterface = ID_INTERFACE_WIFI;
            if (mWifiConnected) {
                mActiveStatus = ID_STATUS_CONNECTED;
            } else {
                mActiveStatus = ID_STATUS_UNREACHABLE;
            }
        } else {
            mActiveInterface = ID_INTERFACE_ETHERNET;
            if (mEthHWConnected) {
                mActiveStatus = ID_STATUS_CONNECTED;
//                if (mEthReachable) {
//                    mActiveStatus = ID_STATUS_CONNECTED;
//                }
//                else {
//                    mActiveStatus = ID_STATUS_UNREACHABLE;
//                }
            } else {
                mActiveStatus = ID_STATUS_DISCONNECTED;
            }
        }
    }
}
