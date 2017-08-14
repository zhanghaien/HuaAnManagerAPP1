package com.sinosafe.xb.manager.widget.dialog;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;

import com.sinosafe.xb.manager.R;

/**
 * 选择头像
 * Created by john lee on 2017/5/20.
 */

public class DialogHeadPortrait extends DialogBottom {

    private OnChooseListener listener;

    public interface OnChooseListener {
        void onChoose(int type);
    }

    public DialogHeadPortrait(@NonNull Activity activity, OnChooseListener listener) {
        super(activity, true);
        this.listener = listener;
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View contentView = inflater.inflate(R.layout.popupwindow__head_portrait, null, false);
        setContentView(contentView);

        // 相机
        contentView.findViewById(R.id.btn_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogHeadPortrait.this.listener.onChoose(0);
            }
        });
        // 相册
        contentView.findViewById(R.id.btn_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogHeadPortrait.this.listener.onChoose(1);
            }
        });
        // 取消
        contentView.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
