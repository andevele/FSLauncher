package com.fengsong.launcher;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.fengsong.launcher.base.BaseActivity;
import com.fengsong.launcher.view.TopBar;

public class FsMainActivity extends BaseActivity {

    private TopBar topBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fs_activity_main);
        super.onCreate(savedInstanceState);
    }
}
