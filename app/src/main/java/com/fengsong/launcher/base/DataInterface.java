package com.fengsong.launcher.base;

/**
 * zhulf 20191031
 * andevele@163.com
 * app任务执行接口
 */
public class DataInterface {
    public interface AppTaskCallBack {
        public void onAppInfoAdded(int size);

        public void onAppInfoRemoved(int size);
    }
}
