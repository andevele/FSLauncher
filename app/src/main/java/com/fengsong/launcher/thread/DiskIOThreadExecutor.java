package com.fengsong.launcher.thread;

import com.fengsong.launcher.util.LogUtils;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * zhulf 20191031
 * andevele@163.com
 * 单个线程池实现
 */
public class DiskIOThreadExecutor implements Executor {
    private static final String TAG = "DiskIOThreadExecutor";

    private final Executor mDiskIO;

    public DiskIOThreadExecutor() {
        mDiskIO = Executors.newSingleThreadExecutor();
    }

    @Override
    public void execute(Runnable command) {
        if (command == null) {
            try {
                LogUtils.e(TAG, "comman is null,please check code");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        mDiskIO.execute(command);
    }
}
