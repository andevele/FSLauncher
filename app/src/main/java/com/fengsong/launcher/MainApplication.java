package com.fengsong.launcher;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.fengsong.launcher.data.AppData;
import com.fengsong.launcher.data.DataAsyncTask;
import com.fengsong.launcher.util.Constant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * zhulf 20191031
 * andevele@163.com
 * apk主Application
 */
public class MainApplication extends Application {
    Map<String, List<String>> dataMap = new HashMap<String, List<String>>();
    private static Context context = null;

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        context = getApplicationContext();
        DataAsyncTask dataAsyncTask = new DataAsyncTask();
        dataAsyncTask.setDataTask(new DataAsyncTask.DataTask() {
            @Override
            public void excuteSuccess(List<String> list) {
                dataMap.put(Constant.INPUT_SOURCE_SECTION, list);
            }

            @Override
            public void executeFailed() {

            }
        });
        dataAsyncTask.execute(Constant.INPUT_SOURCE_JSON_FILE, Constant.INPUT_SOURCE_SECTION);
        AppData.getInstance().catchAppInfo();
    }

    public Map<String, List<String>> getDataMap() {
        return dataMap;
    }

    public static Context getContext() {
        return context;
    }
}
