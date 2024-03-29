package com.fengsong.launcher.control;

import android.content.ContentValues;
import android.content.Intent;
import android.database.SQLException;
import android.net.Uri;
import android.support.v4.view.ViewCompat;
import android.view.View;

import com.fengsong.launcher.AppsActivity;
import com.fengsong.launcher.MainApplication;
import com.fengsong.launcher.util.Constant;
import com.fengsong.launcher.util.LogUtils;
import com.mstar.android.tv.TvCommonManager;

import java.util.List;

/**
 * zhulf 20191031
 * andevele@163.com
 * 控制类
 */
public class ControlManager {
    private static final String TAG = ControlManager.class.getSimpleName();
    private static ControlManager INSTANCE = null;
    private final TvCommonManager mTvCommonmanager;

    public ControlManager() {
        mTvCommonmanager = TvCommonManager.getInstance();
    }

    public static ControlManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ControlManager();
        }
        return INSTANCE;
    }

    public void switchSource(int pos, List<String> dataList) {
        String sourceName = dataList.get(pos);
        LogUtils.v(TAG, "sourceName: " + sourceName);
        switch (sourceName) {
            case Constant.INPUT_SOURCE_NAME_ATV:
                switchSource(TvCommonManager.INPUT_SOURCE_ATV);
                break;
            case Constant.INPUT_SOURCE_NAME_DTV:
                switchSource(TvCommonManager.INPUT_SOURCE_DTV);
                break;
            case Constant.INPUT_SOURCE_NAME_AV1:
                switchSource(TvCommonManager.INPUT_SOURCE_CVBS);
                break;
            case Constant.INPUT_SOURCE_NAME_AV2:
                switchSource(TvCommonManager.INPUT_SOURCE_CVBS2);
                break;
            case Constant.INPUT_SOURCE_NAME_HDMI:
                switchSource(TvCommonManager.INPUT_SOURCE_HDMI);
                break;
            case Constant.INPUT_SOURCE_NAME_HDMI1:
                switchSource(TvCommonManager.INPUT_SOURCE_HDMI);
                break;
            case Constant.INPUT_SOURCE_NAME_HDMI2:
                switchSource(TvCommonManager.INPUT_SOURCE_HDMI2);
                break;
            case Constant.INPUT_SOURCE_NAME_VGA:
                switchSource(TvCommonManager.INPUT_SOURCE_VGA);
                break;
            case Constant.INPUT_SOURCE_NAME_MEDIA:
                //switchSource(TvCommonManager.INPUT_SOURCE_STORAGE);
                switchToStorage();
                break;
        }
    }

    private void switchSource(final int sourceIndex) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent(
                        "com.mstar.tv.tvplayer.ui.intent.action.SOURCE_CHANGE");
                intent.putExtra("inputSrc", sourceIndex);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                startActivity(intent);
                //updateInputSource(sourceIndex);
            }
        }).start();
    }

    private void updateInputSource(int inputSourceIndex) {
        long ret = -1;
        ContentValues vals = new ContentValues();
        vals.put("enInputSourceType", inputSourceIndex);
        LogUtils.v(TAG, "inputSourceIndex: " + inputSourceIndex);

        try {
            ret = MainApplication.getContext().getContentResolver().update(
                    Uri.parse("content://mstar.tv.usersetting/systemsetting"),
                    vals, null, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void startInstalledApp(AppsActivity appsActivity, String pkgName) {
        Intent intent = new Intent();
        intent = appsActivity.getPackageManager().getLaunchIntentForPackage(pkgName);
        if (intent != null) {
            intent.setPackage(null);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            appsActivity.startActivity(intent);
        }
    }

    private void switchToStorage() {
//        Intent intent = new Intent("com.mstar.android.intent.action.START_MEDIA_BROWSER", null);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//        startActivity(intent);
        startActivity("com.mstar.android.intent.action.START_MEDIA_BROWSER");
    }

    public void startActivity(String action) {
        Intent intent = new Intent(action);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        startActivity(intent);
    }

    public void startActivity(Intent intent) {
        MainApplication.getContext().startActivity(intent);
    }

    public void setInputSource(int sourceIndex) {
        TvCommonManager.getInstance().setInputSource(sourceIndex);
    }

    public void startAnimator(View view, boolean hasFocus, float scaleX,float scaleY) {
        view.animate().cancel();
        if (hasFocus) {
            ViewCompat.animate(view)
                    .scaleX(scaleX)
                    .scaleY(scaleY)
                    .setDuration(200)
                    .start();
        } else {
            ViewCompat.animate(view)
                    .scaleX(1.0f)
                    .scaleY(1.0f)
                    .setDuration(200)
                    .start();
        }
    }
}
