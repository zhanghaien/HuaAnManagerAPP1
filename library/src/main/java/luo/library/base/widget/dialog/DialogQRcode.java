package luo.library.base.widget.dialog;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import luo.library.R;
import luo.library.base.utils.DensityUtil;

/**
 * 二维码
 * Created by john lee on 2017/5/20.
 */

public class DialogQRcode extends AlertDialog {

    private Context context;
    private TextView tv_title;
    private TextView tv_name;
    private TextView tv_IDCardNum;
    private ImageView ivQr;
    private ImageView iv_close;

    public DialogQRcode(Context context) {
        super(context, R.style.AlertDialogTheme);
        this.context = context;
        initView();
    }

    public DialogQRcode(Context context,int width) {
        super(context, R.style.AlertDialogTheme);
        this.context = context;
        initView();
        tv_title.setVisibility(View.INVISIBLE);
        ViewGroup.LayoutParams params  = ivQr.getLayoutParams();
        params.width = (int)(DensityUtil.widthPixels(context)/5*3);
        params.height = params.width;
        ivQr.setLayoutParams(params);
    }

    private void initView() {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_qrcode, null);
        setView(view);

        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_IDCardNum = (TextView) view.findViewById(R.id.tv_IDCardNum);


        ivQr = (ImageView) view.findViewById(R.id.ivQr);
        iv_close = (ImageView) view.findViewById(R.id.iv_close);

        setCanceledOnTouchOutside(false);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    /**
     * 隐藏底部信息
     * @return
     */
    public DialogQRcode hideBottomTextView(){

        tv_name.setVisibility(View.GONE);
        tv_IDCardNum.setVisibility(View.GONE);
        return  this;
    }

    /**
     * 隐藏底部信息
     * @return
     */
    public DialogQRcode hideBottomTextView2(){

        tv_name.setVisibility(View.VISIBLE);
        return  this;
    }

    public DialogQRcode setTitleInfo(String title){

        tv_title.setText(title);
        return this;
    }

    /**
     * 设置二维码
     * @param bitmap
     * @return
     */
    public DialogQRcode setQRBitmap(Bitmap bitmap){

        ivQr.setImageBitmap(bitmap);
        return this;
    }

    /**
     * 设置二维码
     * @param resId
     * @return
     */
    public DialogQRcode setQRResId(int resId){

        ivQr.setImageResource(resId);
        return this;
    }

}
