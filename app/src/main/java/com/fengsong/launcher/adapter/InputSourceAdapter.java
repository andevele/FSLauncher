package com.fengsong.launcher.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.fengsong.launcher.R;
import com.fengsong.launcher.base.ViewListener.ItemFocusChangeListener;
import com.fengsong.launcher.util.Constant;

import java.util.List;

/**
 * zhulf 20191031
 * andevele@163.com
 * 主页通道列表适配器
 */
public class InputSourceAdapter extends RecyclerView.Adapter<InputSourceAdapter.CustomViewHolder> {
    private final Context context;
    private final List<String> dataSource;
    private final int layoutId;
    private OnItemClickListener onItemClickListener;
    private ItemFocusChangeListener focusChangeListener;

    public interface OnItemClickListener {
        void onItemClick(int layoutPos);
    }

    public InputSourceAdapter(Context context, List<String> dataSource, int layoutId) {
        this.context = context;
        this.dataSource = dataSource;
        this.layoutId = layoutId;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setOnItemFocusChangeListener(ItemFocusChangeListener focusChangeListener) {
        this.focusChangeListener = focusChangeListener;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder viewHolder, int position) {
        viewHolder.mTextView.setText(dataSource.get(position));
        String sourceName = dataSource.get(position);

        switch (sourceName) {
            case Constant.INPUT_SOURCE_NAME_ATV:
                viewHolder.mSourceIcon.setImageResource(R.drawable.ic_atv);
                break;
            case Constant.INPUT_SOURCE_NAME_DTV:
                viewHolder.mSourceIcon.setImageResource(R.drawable.ic_dtv);
                break;
            case Constant.INPUT_SOURCE_NAME_AV1:
                viewHolder.mSourceIcon.setImageResource(R.drawable.ic_av_1);
                break;
            case Constant.INPUT_SOURCE_NAME_AV2:
                viewHolder.mSourceIcon.setImageResource(R.drawable.ic_av_2);
                break;
            case Constant.INPUT_SOURCE_NAME_HDMI:
                viewHolder.mSourceIcon.setImageResource(R.drawable.ic_hdmi_1);
                break;
            case Constant.INPUT_SOURCE_NAME_HDMI1:
                viewHolder.mSourceIcon.setImageResource(R.drawable.ic_hdmi_1);
                break;
            case Constant.INPUT_SOURCE_NAME_HDMI2:
                viewHolder.mSourceIcon.setImageResource(R.drawable.ic_hdmi_2);
                break;
            case Constant.INPUT_SOURCE_NAME_VGA:
                viewHolder.mSourceIcon.setImageResource(R.drawable.ic_vga);
                break;
            case Constant.INPUT_SOURCE_NAME_MEDIA:
                viewHolder.mSourceIcon.setImageResource(R.drawable.ic_usb);
                break;
            default:
                break;

        }
        viewHolder.itemView.setTag(position);
        if (onItemClickListener == null) {
            return;
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int layoutPos = viewHolder.getLayoutPosition();
                onItemClickListener.onItemClick(layoutPos);
            }
        });
        viewHolder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(focusChangeListener != null) {
                    focusChangeListener.onFocusChange(view,hasFocus,1.05f,1.1f);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    static class CustomViewHolder extends RecyclerView.ViewHolder {
        private ImageView mSourceIcon;
        private TextView mTextView;

        public CustomViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.source_text);
            mSourceIcon = (ImageView) itemView.findViewById(R.id.source_icon);
        }
    }
}
