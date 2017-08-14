package com.sinosafe.xb.manager.module.home.xiaofeidai;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.megvii.livenesslib.LivenessActivity;
import com.sinosafe.xb.manager.BaseMainActivity;
import com.sinosafe.xb.manager.R;
import com.sinosafe.xb.manager.api.APIManager;
import com.sinosafe.xb.manager.api.rxjava.RxHttpYlmResultFunc;
import com.sinosafe.xb.manager.api.rxjava.RxSchedulersHelper;
import com.sinosafe.xb.manager.api.rxjava.RxSubscriber;
import com.sinosafe.xb.manager.api.xutilshttp.OnResponseListener;
import com.sinosafe.xb.manager.api.xutilshttp.XutilsBaseHttp;
import com.sinosafe.xb.manager.bean.LoanUserInfo;
import com.sinosafe.xb.manager.model.ClientModel;
import com.sinosafe.xb.manager.module.home.xiaofeidai.bean.YaLianMengResult;
import com.sinosafe.xb.manager.utils.MyAMapLocUtils;
import com.sinosafe.xb.manager.utils.MyFaceNetWorkWarranty;
import com.sinosafe.xb.manager.utils.T;
import com.sinosafe.xb.manager.utils.fileupload.FileUploadPresenter;
import com.sinosafe.xb.manager.widget.dialog.DialogPowerOfAttorney;
import com.sinosafe.xb.manager.widget.edit.BandCardEditText;
import com.sinosafe.xb.manager.widget.edit.ClearEditText;

import org.xutils.http.RequestParams;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import luo.library.base.base.BaseFragmentActivity;
import luo.library.base.utils.GsonUtil;
import luo.library.base.utils.IntentUtil;
import luo.library.base.utils.MyLog;
import luo.library.base.widget.dialog.DialogMessage;

/**
 * 类名称：   com.sinosafe.xb.manager.module.home.xiaofeidai
 * 内容摘要： //消费贷、微贷申请基类。
 * 修改备注：
 * 创建时间： 2017/7/27 0027
 * 公司：     深圳市华移科技股份有限公司
 * 作者：     Mr 张
 */
public abstract class XiaoFeiDaiApplyBase extends BaseFragmentActivity {

    public static String AGREEMENT_TIP = "同意<font color=\"#7380F3\">《用户协议》</font>";
    protected static final int INTO_IDCARDSCAN_PAGE = 100;
    protected static final int INTO_BANKCARDSCAN_PAGE = 200;
    protected static final int PAGE_INTO_LIVENESS = 300;
    protected static final int ELECTRONIC_SIGNA = 400;
    protected boolean isDebuge = false, isAllCard = false;
    protected static final String KEY_ISDEBUGE = "KEY_ISDEBUGE";
    protected static final String KEY_ISALLCARD = "KEY_ISALLCARD";

    @BindView(R.id.et_customerName)
    protected ClearEditText mEtCustomerName;
    @BindView(R.id.et_iDCardNumber)
    protected BandCardEditText mEtIDCardNumber;
    @BindView(R.id.iv_scan_idCard)
    protected ImageView mIvScanIdCard;
    @BindView(R.id.et_card_holder)
    protected ClearEditText mEtCardHolder;
    @BindView(R.id.et_card_number)
    protected BandCardEditText mEtCardNumber;
    @BindView(R.id.iv_scan_bankcard)
    protected ImageView mIvScanBankcard;
    @BindView(R.id.et_phone)
    protected ClearEditText mEtPhone;
    //@BindView(R.id.et_code)
    //protected ClearEditText mEtCode;
    //@BindView(R.id.btn_get_code)
    //protected VerificationCodeButton mBtnGetCode;
    @BindView(R.id.cb_agreement)
    protected CheckBox mCbAgreement;
    @BindView(R.id.tv_agreement)
    protected TextView mTvAgreement;
    @BindView(R.id.btn_confirm_add)
    protected Button mBtnConfirmAdd;

    @BindView(R.id.iv_IDCard_positive)
    protected ImageView mIvIDCardPositive;
    @BindView(R.id.iv_IdCard_negative)
    protected ImageView mIvIDCardNegative;
    //配偶身份证,正面
    protected String idCardPositivePhoto = "";
    //配偶身份证,反面
    protected String idCardNegativePhoto = "";
    //0：扫描获取身份证信息；1：身份证正面；2：身份证反面
    protected int idCardOperator = 0;
    protected FileUploadPresenter fileUploadPresenter;
    protected boolean needUploadIDCardPhoto;

    //0：消费贷，1：微贷
    protected int type = -1;
    //贷款用户信息
    public static LoanUserInfo loanUserInfo;
    //消费贷产品ID,贷款期限，贷款金额，产品名字
    protected String prd_id,period,amount,prd_name;
    protected DialogPowerOfAttorney dialogPowerOfAttorney;
    //首次保存标记
    protected boolean saveCurrencyFlag = false;
    protected MyAMapLocUtils aMapLocUtils;
    protected MyFaceNetWorkWarranty myFaceNetWorkWarranty;

    protected abstract void saveApplyInformation();
    protected abstract void openIDCardScan();

    /**
     * 查询银行卡类型
     */
    protected void getCardNosBank(){

        RequestParams params = new RequestParams(APIManager.SINOSAFE_URL+"client/lianpay/ylmQuery");
        params.addBodyParameter("token", BaseMainActivity.loginUserBean.getToken());
        params.addBodyParameter("bankcard", mEtCardNumber.getBankCardText());
        params.addBodyParameter("type", "1359");
        XutilsBaseHttp.requestType = -1;
        XutilsBaseHttp.post(params, onResponseListener2);
    }

    private OnResponseListener onResponseListener2 = new OnResponseListener() {
        @Override
        public void onRequestFailedCallback(String var1) {
            closeSVProgressHUD();
            MyLog.e("卡bin======"+var1);
            T.showShortBottom("服务器开小差了.");
        }

        @Override
        public void onCommonRequestCallback(String result) {
            MyLog.e("卡bin查询======"+result);
            YaLianMengResult yaLianMengResult = GsonUtil.GsonToBean(result,YaLianMengResult.class);
            if("0000".equals(yaLianMengResult.getResult())){
                YaLianMengResult.Data data = yaLianMengResult.getData();
                if("0".equals(data.getCode())&&"1".equals(data.getCard_type())){
                    cardNosBankCheck();
                }else{
                    showCheckErorInfoDialog(data.getDesc()+"\n"+"只能选择储蓄卡");
                }
            }else{
                showCheckErorInfoDialog(yaLianMengResult.getMsg());
            }
            closeSVProgressHUD();
        }
    };

    /**
     * 银行卡校验
     */
    protected void cardNosBankCheck(){

        Map<String,String> map = new HashMap<>();
        map.put("token", BaseMainActivity.loginUserBean.getToken());
        map.put("name", mEtCustomerName.getText().toString());
        map.put("idcard", mEtIDCardNumber.getBankCardText());
        map.put("bankcard", mEtCardNumber.getBankCardText());
        map.put("mobile", mEtPhone.getText().toString());
        map.put("type", "1362");
        ClientModel.ylmQuery(map)
                .timeout(10*2, TimeUnit.SECONDS)
                .compose(RxSchedulersHelper.<YaLianMengResult>io_main())
                .map(new RxHttpYlmResultFunc())
                .subscribe(new YlmQueryRxSubscriber());
    }

    class YlmQueryRxSubscriber extends RxSubscriber<YaLianMengResult> {

        @Override
        public void _onNext(YaLianMengResult yaLianMengResult) {

            if("0000".equals(yaLianMengResult.getResult())){
                YaLianMengResult.Data data = yaLianMengResult.getData();
                if("0".equals(data.getCode())){
                    T.showShortBottom(data.getDesc());
                    saveApplyInformation();
                }else{
                    showCheckErorInfoDialog(data.getDesc()+"\n"+data.getOrg_desc());
                    closeSVProgressHUD();
                }
            }else{
                showCheckErorInfoDialog(yaLianMengResult.getMsg());
                closeSVProgressHUD();
            }
        }

        @Override
        public void _onError(String msg) {
            closeSVProgressHUD();
            MyLog.e("四要素======"+msg);
            T.showShortBottom("服务器开小差了.");
        }
    }

    public void showCheckErorInfoDialog(String errorInfo){

        new DialogMessage(this).setTitle("签约结果")
                .setMess(errorInfo).setConfirmTips("确定").setOtherConfirmTips("取消")
                .setConfirmListener(new DialogMessage.OnConfirmListener() {
                    @Override
                    public void onConfirm() {

                    }
                })
                .setOtherConfirmListener(new DialogMessage.OnOtherConfirmListener() {
                    @Override
                    public void onOtherConfirm() {

                    }
                }).show();
    }

    /**
     * 人脸识别授权失败提示
     */
    protected void showFaceNetWorkWarrantyDialog(){
        new DialogMessage(this).setMess("人脸识别联网授权失败，请重新授权!")
                .setConfirmListener(new DialogMessage.OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        faceNetGrant();
                    }
                }).show();
    }

    /**
     * 人脸识别联网授权
     */
    protected void faceNetGrant(){
        startProgressDialog("授权中...");
        myFaceNetWorkWarranty.faceNetWorkWarranty(new MyFaceNetWorkWarranty.NetWorkWarrantyCallback() {
            @Override
            public void grantResult(boolean result) {
                stopProgressDialog();
                if(result){
                    T.showShortBottom("联网授权成功");
                    IntentUtil.gotoActivityForResult(XiaoFeiDaiApplyBase.this, LivenessActivity.class,PAGE_INTO_LIVENESS);
                }else{
                    showFaceNetWorkWarrantyDialog();
                }
            }
        });
    }



    /**
     * 身份证授权失败提示
     */
    protected void showIdCardNetWorkWarrantyDialog(){
        new DialogMessage(this).setMess("身份证扫描联网授权失败，请重新授权!")
                .setConfirmListener(new DialogMessage.OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        idCardNetGrant();
                    }
                }).show();
    }

    /**
     * 身份证联网授权
     */
    protected void idCardNetGrant(){
        startProgressDialog("授权中...");
        myFaceNetWorkWarranty.idCardNetWorkWarranty(new MyFaceNetWorkWarranty.NetWorkWarrantyCallback() {
            @Override
            public void grantResult(boolean result) {
                stopProgressDialog();
                if(result){
                    T.showShortBottom("联网授权成功");
                    openIDCardScan();
                }else{
                    showIdCardNetWorkWarrantyDialog();
                }
            }
        });
    }
}
