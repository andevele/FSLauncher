package com.fengsong.launcher.base;

import android.view.View;

/**
 * zhulf 20191031
 * andevele@163.com
 * 监听器接口
 */
public class ViewListener {
    public interface OnItemClickListener {
        void onClick(String pkgName);
    }

    public interface ItemFocusChangeListener {
        void onFocusChange(View view, boolean hasFocus, float scaleX, float scaleY);
    }
}
