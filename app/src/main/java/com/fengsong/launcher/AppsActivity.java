package com.fengsong.launcher;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.fengsong.launcher.base.ViewListener.ItemFocusChangeListener;
import com.fengsong.launcher.base.ViewListener.OnItemClickListener;

import com.fengsong.launcher.adapter.AllAppsAdapter;
import com.fengsong.launcher.base.BaseActivity;
import com.fengsong.launcher.control.ControlManager;
import com.fengsong.launcher.data.AppData;
import com.fengsong.launcher.model.AppInfo;
import com.fengsong.launcher.net.NetworkMonitor;
import com.fengsong.launcher.view.CustomDecoration;
import com.fengsong.launcher.view.TopBar;

import java.util.ArrayList;
import java.util.List;

/**
 * zhulf 20191031
 * andevele@163.com
 * 所有app页面
 */
public class AppsActivity extends BaseActivity {
    private List<AppInfo> appList;
    private AllAppsAdapter allAppsAdapter;
    private static final int spanCount = 4;
    private GridLayoutManager gridLayoutManager;
    private RecyclerView appRecyclerView;
    //    private BorderView border;
    private Handler handler = new Handler();
    private TopBar topBar;
    private RelativeLayout topBarLayout;
    private NetworkMonitor.INetworkUpdateListener mNetworkUpdateListener;
    private NetworkMonitor mNetworkMonitor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.apps_main);
        initData();
        initView();
        super.onCreate(savedInstanceState);
    }

    private void initData() {
        appList = new ArrayList<AppInfo>();
        List<AppInfo> appInfoList = AppData.getInstance().getAppInfo();
        if (appInfoList == null || appInfoList.size() < 1) {
            appInfoList = AppData.getInstance().catchAppInfo();
        }
        appList = appInfoList;
        allAppsAdapter = new AllAppsAdapter(this, appInfoList);
        gridLayoutManager = new GridLayoutManager(this, spanCount);
    }

    private void initView() {
//        TextView titleLabel = (TextView) findViewById(R.id.title_label_text);
//        titleLabel.setText(getResources().getString(R.string.apps_page));
//        border = new BorderView(this);
        //border.setBackgroundResource(R.drawable.border_highlight);
//        border.setBackgroundResource(R.drawable.custom_border);
        appRecyclerView = (RecyclerView) findViewById(R.id.apps_list);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        appRecyclerView.setLayoutManager(gridLayoutManager);
        appRecyclerView.addItemDecoration(new CustomDecoration());
        appRecyclerView.setFocusable(false);
//        border.attachTo(appRecyclerView);
        allAppsAdapter.setHasStableIds(true);
        appRecyclerView.setItemAnimator(null);
        appRecyclerView.setAdapter(allAppsAdapter);
        appRecyclerView.scrollToPosition(0);

        allAppsAdapter.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onClick(String pkgName) {
                ControlManager.getInstance().startInstalledApp(AppsActivity.this, pkgName);
            }
        });

        allAppsAdapter.setOnItemFocusChangeListener(new ItemFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean hasFocus, float scaleX, float scaleY) {
                ControlManager.getInstance().startAnimator(view, hasFocus, scaleX, scaleY);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<AppInfo> appInfoList = AppData.getInstance().getAppInfo();
        if (appInfoList == null || appInfoList.size() < 1) {
            appInfoList = AppData.getInstance().catchAppInfo();
        }
        allAppsAdapter.updateData(appInfoList);
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    public void updateViews() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                appRecyclerView.setItemAnimator(null);
            }
        }, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}