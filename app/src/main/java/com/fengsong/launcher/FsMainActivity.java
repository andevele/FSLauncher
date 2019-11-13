package com.fengsong.launcher;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.fengsong.launcher.adapter.InputSourceAdapter;
import com.fengsong.launcher.base.BaseActivity;
import com.fengsong.launcher.base.ViewListener;
import com.fengsong.launcher.control.ControlManager;
import com.fengsong.launcher.data.AppData;
import com.fengsong.launcher.data.DataAsyncTask;
import com.fengsong.launcher.util.Constant;
import com.fengsong.launcher.view.RecycleViewItemDivider;
import com.fengsong.launcher.view.TopBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FsMainActivity extends BaseActivity {

    private TopBar topBar;
    private RelativeLayout commonAppsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fs_activity_main);
        super.onCreate(savedInstanceState);
        initViews();

    }

    private void initViews() {
        commonAppsContainer = (RelativeLayout) findViewById(R.id.common_apps);
        for (int i = 0; i < commonAppsContainer.getChildCount(); i++) {
            commonAppsContainer.getChildAt(i).setOnClickListener(new ViewClickListener());
            commonAppsContainer.getChildAt(i).setOnFocusChangeListener(new ViewFocusChangeListener());
        }
        initInputSourceView();
    }

    class ViewClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch (id) {
                case R.id.view_youtube:
                    launchApp("com.google.android.youtube.tv");
                    break;
                case R.id.view_netflix:
                    launchApp("com.netflix.mediaclient");
                    break;
                case R.id.view_hotstar:
                    launchApp("in.startv.hotstar");
                    break;
                case R.id.view_tvstore:
                    launchApp("cm.aptoidetv.pt");
                    break;
                case R.id.view_file_explorer:
                    launchApp("net.micode.fileexplorer");
                    break;
                case R.id.view_apps:
                    startActivity("com.fengsong.launcher.action.allapps");
                    break;
                default:
                    break;
            }
        }
    }

    class ViewFocusChangeListener implements View.OnFocusChangeListener {

        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            ControlManager.getInstance().startAnimator(view,hasFocus,1.1f,1.1f);
        }
    }

    private void launchApp(String packageName) {
        if (isAppInstalled(getApplicationContext(), packageName)) {
            Intent intent = new Intent();
            intent = getApplicationContext().getPackageManager().getLaunchIntentForPackage(packageName);
            if (intent != null) {
                intent.setPackage(null);
                //MainActivity.this.startActivity(intent);
                ControlManager.getInstance().startActivity(intent);
            }
        } else {
            goToMarket(FsMainActivity.this, packageName);
        }
    }

    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static void goToMarket(Context context, String packageName) {
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
        }
    }

    private void startActivity(String action) {
        ControlManager.getInstance().startActivity(action);
    }

    private void initInputSourceView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.input_source_RecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, 1, false);
//        GridLayoutManager layoutManager = new GridLayoutManager(this,1);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setFocusable(true);
        recyclerView.addItemDecoration(new RecycleViewItemDivider(this, LinearLayoutManager.VERTICAL, 7, getResources().getColor(R.color.input_source_item_divide_bg)));
//        DividerItemDecoration divider = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
//        divider.setDrawable(ContextCompat.getDrawable(this,R.drawable.custom_divider));
//        recyclerView.addItemDecoration(divider);
//        recyclerView.addItemDecoration(new RecycleViewItemDivider(this,LinearLayoutManager.VERTICAL
//        , 5, getResources().getColor(R.color.input_source_item_divide_bg)));
//        border.attachTo(recyclerView);
        CreateSourceData(recyclerView, R.layout.input_soure_list_item);
    }

    private void CreateSourceData(final RecyclerView recyclerView, final int layoutId) {
        List<String> data = new ArrayList<String>();
        List<String> list = new ArrayList<String>();
        final MainApplication application = (MainApplication) getApplication();
        Map<String, List<String>> dataMap = application.getDataMap();
        List<String> dataList = dataMap.get(Constant.INPUT_SOURCE_SECTION);
        if (dataList == null || dataList.size() < 0) {
            DataAsyncTask dataAsyncTask = new DataAsyncTask();
            dataAsyncTask.setDataTask(new DataAsyncTask.DataTask() {
                @Override
                public void excuteSuccess(List<String> dataList) {
                    application.setDataMap(dataList);
                    updateList(recyclerView, dataList, layoutId);
                }

                @Override
                public void executeFailed() {

                }
            });
            dataAsyncTask.execute(Constant.INPUT_SOURCE_JSON_FILE, Constant.INPUT_SOURCE_SECTION);
            return;
        }

        updateList(recyclerView, dataList, layoutId);
    }

    private void updateList(RecyclerView recyclerView, final List<String> dataList, int layoutId) {
        InputSourceAdapter adapter = new InputSourceAdapter(getApplicationContext(), dataList, layoutId);
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(0);
        adapter.notifyDataSetChanged();
        adapter.setOnItemClickListener(new InputSourceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int layoutPos) {
                ControlManager.getInstance().switchSource(layoutPos, dataList);
            }
        });
        adapter.setOnItemFocusChangeListener(new ViewListener.ItemFocusChangeListener(){
            @Override
            public void onFocusChange(View view, boolean hasFocus, float scaleX, float scaleY) {
                ControlManager.getInstance().startAnimator(view,hasFocus,scaleX,scaleY);
            }
        });
    }
}
