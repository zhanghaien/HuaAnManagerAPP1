package luo.library.base.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import luo.library.R;
import luo.library.base.utils.DensityUtil;

/**
 * 类名称：   luo.library.base.widget.dialog
 * 内容摘要： //说明主要功能。
 * 修改备注：
 * 创建时间： 2017/8/12 0012
 * 公司：     深圳市华移科技股份有限公司
 * 作者：     Mr 张
 */
public class CustomPopDialog extends Dialog {

    public CustomPopDialog(Context context) {
        super(context);
    }

    public CustomPopDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private Bitmap image;

        public Builder(Context context) {
            this.context = context;
        }

        public Bitmap getImage() {
            return image;
        }

        public void setImage(Bitmap image) {
            this.image = image;
        }

        public CustomPopDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final CustomPopDialog dialog = new CustomPopDialog(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.dialog_share_qrcode, null);
            dialog.setContentView(layout);
            ImageView img = (ImageView)layout.findViewById(R.id.ivQr);
            img.setImageBitmap(getImage());

            ImageView iv_close = (ImageView)layout.findViewById(R.id.iv_close);
            iv_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            Window dialogWindow = dialog.getWindow();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = (int) (DensityUtil.widthPixels(context)*0.8); // 宽度
            lp.height = (int) (DensityUtil.widthPixels(context)*0.84);
            dialogWindow.setAttributes(lp);
            return dialog;
        }
    }
}
