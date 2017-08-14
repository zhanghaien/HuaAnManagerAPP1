package com.sinosafe.xb.manager.module.home.xiaofeidai.supplementInfo;

import android.os.AsyncTask;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kincai.libjpeg.ImageUtils;
import com.sinosafe.xb.manager.R;
import com.sinosafe.xb.manager.bean.LoanUserInfo;
import com.sinosafe.xb.manager.utils.Constant;
import com.sinosafe.xb.manager.utils.MyFaceNetWorkWarranty;
import com.sinosafe.xb.manager.utils.fileupload.FileUploadPresenter;
import com.sinosafe.xb.manager.widget.dialog.DialogHeadPortrait;
import com.sinosafe.xb.manager.widget.dialog.DialogResidentialAddress;
import com.sinosafe.xb.manager.widget.edit.BandCardEditText;
import com.sinosafe.xb.manager.widget.edit.ClearEditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import luo.library.base.base.BaseFragmentActivity;
import luo.library.base.bean.WheelViewBean;
import luo.library.base.utils.MyLog;
import luo.library.base.widget.dialog.DialogWheelView;

/**
 * 类名称：   com.sinosafe.xb.manager.module.home.xiaofeidai.supplementInfo
 * 内容摘要： //个人信息。
 * 修改备注：
 * 创建时间： 2017/8/9 0009
 * 公司：     深圳市华移科技股份有限公司
 * 作者：     Mr 张
 */
public abstract class PersonInfoBase extends BaseFragmentActivity {

    protected final int INTO_IDCARDSCAN_PAGE = 100;
    protected final int RESULT_REQUEST_PHOTO = 1005;
    @BindView(R.id.ll_personal_info)
    protected LinearLayout ll_personal_info;
    @BindView(R.id.tvMyName)
    protected TextView mTvMyName;
    @BindView(R.id.tvMyPhone)
    protected TextView mTvMyPhone;
    @BindView(R.id.tv_residentialAddress)
    protected TextView mTvResidentialAddress;
    @BindView(R.id.et_detailedAddress)
    protected ClearEditText mEtDetailedAddress;
    @BindView(R.id.tv_educationStatus)
    protected TextView mTvEducationStatus;
    @BindView(R.id.tv_marriageStatus)
    protected TextView mTvMarriageStatus;
    //@BindView(R.id.rb_unmarried)
    //RadioButton mRbUnmarried;
    //@BindView(R.id.rb_married)
    //RadioButton mRbMarried;
    @BindView(R.id.et_spousePhone)
    protected ClearEditText mEtSpousePhone;
    @BindView(R.id.et_spouseName)
    protected ClearEditText mEtSpouseName;
    @BindView(R.id.et_spouseIDCard)
    protected BandCardEditText mEtSpouseIDCard;
    @BindView(R.id.iv_spouseCard_positive)
    protected ImageView mIvSpouseCardPositive;
    @BindView(R.id.iv_spouseCard_negative)
    protected ImageView mIvSpouseCardNegative;
    @BindView(R.id.btn_next)
    protected Button mBtnNext;
    @BindView(R.id.spouseInfoLayout)
    protected LinearLayout spouseInfoLayout;

    protected DialogResidentialAddress dialogAddress;

    protected List<WheelViewBean> educations = new ArrayList<>();
    protected DialogWheelView dialogWheelView;
    protected LoanUserInfo.UserDetail userDetail;
    //配偶身份证,正面
    protected String spouseIdCardPhoto = "";
    //配偶身份证,反面
    protected String spouseIdCardPhoto2 = "";
    protected int side = -1;
    protected FileUploadPresenter fileUploadPresenter;
    protected MyFaceNetWorkWarranty myFaceNetWorkWarranty;

    protected List<WheelViewBean> marriageStatus = new ArrayList<>();
    protected DialogWheelView marriageWheelView;
    protected DialogHeadPortrait dialogHeadPortrait;
    //身份证正面压缩后的路径
    protected String compressPath;

    protected abstract void camera();
    protected abstract void photo();
    protected abstract void uploadImageOcridcard(String compressPath);

    /**
     * 选择配偶身份证照片
     */
    protected void chooseHeadPortrait() {
        if(dialogHeadPortrait==null){
            dialogHeadPortrait = new DialogHeadPortrait(this, new DialogHeadPortrait.OnChooseListener() {
                @Override
                public void onChoose(int type) {
                    if (type == 0)
                        camera();
                    else
                        photo();
                    dialogHeadPortrait.dismiss();
                }
            });
        }
        dialogHeadPortrait.show();
    }

    /**
     * 异步压缩图片
     */
    protected class ImageCompressAsyncTask extends AsyncTask<String,Integer,String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                String compressPath = Constant.getImageFile("").getPath();
                ImageUtils.compressBitmap(params[0],compressPath);
                MyLog.e("恭喜~~~~~~~~~图片压缩成功-----------------");
                return compressPath;
            } catch (Exception e) {
                MyLog.e("图片压缩失败-----------------"+e.getMessage());
                return "";
            }
        }
        @Override
        protected void onPostExecute(String Str) {
            uploadImageOcridcard(Str);
        }
    }
}
