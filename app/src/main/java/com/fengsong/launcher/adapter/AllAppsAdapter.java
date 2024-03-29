package com.fengsong.launcher.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fengsong.launcher.base.ViewListener.ItemFocusChangeListener;
import com.fengsong.launcher.base.ViewListener.OnItemClickListener;

import com.fengsong.launcher.R;
import com.fengsong.launcher.base.DataInterface;
import com.fengsong.launcher.data.AppData;
import com.fengsong.launcher.model.AppInfo;
import com.fengsong.launcher.util.ColorUtil;
import com.fengsong.launcher.util.Constant;

import java.util.List;

/**
 * zhulf 20191031
 * andevele@163.com
 * 所有app宫格适配器
 */
public class AllAppsAdapter extends RecyclerView.Adapter<AllAppsAdapter.ViewHolder> implements DataInterface.AppTaskCallBack {

    private View mView;
    private List<AppInfo> mlist;
    private Context mContext;
    private ViewHolder mVH;
    private OnItemClickListener listener;
    private OnItemLongClickListener longClickListener;
    private AppInfo appInfo = null;
    private ItemFocusChangeListener focusChangeListener;

    public AllAppsAdapter(Context context, List<AppInfo> list) {
        this.mlist = list;
        this.mContext = context;
        AppData.getInstance().setCallBack(this);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.apps_item_view,
                parent, false);
        mVH = new ViewHolder(mView);
        return mVH;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        appInfo = mlist.get(position);
        holder.icon.setBackground(appInfo.getAppIcon());
        holder.name.setText(appInfo.getAppName());
        switch (appInfo.getAppName()) {
            case Constant.APPS_NAME_APTOIDE:
                setBackgroundColor(holder, ColorUtil.getColors(2));
                break;
            case Constant.APPS_NAME_HOTSTAR:
                setBackgroundColor(holder, ColorUtil.getColors(0));
                break;
            case Constant.APPS_NAME_YOUTUBE:
                setBackgroundColor(holder, ColorUtil.getColors(4));
                break;
            case Constant.APPS_NAME_PRIMEVIDEO:
                setBackgroundColor(holder, ColorUtil.getColors(5));
                break;
            case Constant.APPS_NAME_LOCALMM:
                setBackgroundColor(holder, ColorUtil.getColors(6));
                break;
            case Constant.APPS_NAME_DOWNLOAD:
                setBackgroundColor(holder, ColorUtil.getColors(7));
                break;
            case Constant.APPS_NAME_APK_MANAGER:
                setBackgroundColor(holder, ColorUtil.getColors(8));
                break;
            case Constant.APPS_NAME_FILE_EXPLORER:
                setBackgroundColor(holder, ColorUtil.getColors(9));
                break;
            case Constant.APPS_NAME_FILE_SEARCH:
                setBackgroundColor(holder, ColorUtil.getColors(10));
                break;
            case Constant.APPS_NAME_FILE_MCAST:
                setBackgroundColor(holder, ColorUtil.getColors(11));
                break;
            default:
                setBackgroundColor(holder, ColorUtil.getColors(3));
                break;
        }

        final int itemposition = position;
//        Random random = new Random();
//        int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
//        holder.itemlayout.setBackgroundColor(color);
//        setBackgroundColor(holder);
        holder.itemlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    String pkg = mlist.get(position).getPackageName();
                    listener.onClick(pkg);
                }
            }
        });
        holder.itemlayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (longClickListener != null) {
                    longClickListener.onClick(itemposition);
                }
                return true;
            }
        });
        holder.itemlayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (focusChangeListener != null) {
                    focusChangeListener.onFocusChange(view, hasFocus, 1.1f, 1.1f);
                }
            }
        });
    }

    private void setBackgroundColor(ViewHolder holder, String color) {
        GradientDrawable gradientNormal = new GradientDrawable();
        gradientNormal.setCornerRadius(5);
        gradientNormal.setColor(Color.parseColor(color));

        GradientDrawable gradientFocused = new GradientDrawable();
        gradientFocused.setCornerRadius(5);
        gradientFocused.setColor(Color.parseColor(color));
        gradientFocused.setStroke(2, Color.parseColor("#FFFFFF"));

        setSelectorDrawable(holder.itemlayout, gradientNormal, gradientFocused);
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onAppInfoAdded(int size) {
        this.notifyDataSetChanged();
//        this.notifyItemInserted(size);
        //((AppsActivity)mContext).updateViews();
    }

    @Override
    public void onAppInfoRemoved(int size) {
        //this.notifyItemRemoved(size);
        this.notifyDataSetChanged();
    }

    public void updateData(List<AppInfo> list) {
        this.mlist = list;
        //this.notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemLongClickListener {
        void onClick(int position);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    public void setOnItemFocusChangeListener(ItemFocusChangeListener focusChangeListener) {
        this.focusChangeListener = focusChangeListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout itemlayout;
        public TextView name;
        public ImageView icon;

        public ViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.item_app_icon);
            itemlayout = (LinearLayout) itemView.findViewById(R.id.item_layout);
            name = (TextView) itemView.findViewById(R.id.item_app_name);
        }
    }

    public void setSelectorDrawable(View view, Drawable drawableNormal, Drawable drawableFocused) {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{android.R.attr.state_focused}, drawableFocused);
        drawable.addState(new int[]{-android.R.attr.state_focused}, drawableNormal);
        view.setBackground(drawable);
    }
}
