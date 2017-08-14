package com.sinosafe.xb.manager.module.home.mianqian;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.megvii.livenesslib.LivenessActivity;
import com.sinosafe.xb.manager.BaseMainActivity;
import com.sinosafe.xb.manager.R;
import com.sinosafe.xb.manager.api.APIManager;
import com.sinosafe.xb.manager.api.rxjava.RxHttpBaseResultFunc;
import com.sinosafe.xb.manager.api.rxjava.RxSchedulersHelper;
import com.sinosafe.xb.manager.api.rxjava.RxSubscriber;
import com.sinosafe.xb.manager.api.xutilshttp.OnResponseListener;
import com.sinosafe.xb.manager.api.xutilshttp.XutilsBaseHttp;
import com.sinosafe.xb.manager.bean.BaseEntity;
import com.sinosafe.xb.manager.bean.VerifyResultBean;
import com.sinosafe.xb.manager.bean.YeWuBean;
import com.sinosafe.xb.manager.model.ClientModel;
import com.sinosafe.xb.manager.module.home.loanmanager.PaymentHistoryActivity;
import com.sinosafe.xb.manager.module.home.supplyInfo.ImageSelectActivity2;
import com.sinosafe.xb.manager.module.yewu.xiaofeidai.bean.BaseResult;
import com.sinosafe.xb.manager.module.yewu.xiaofeidai.bean.LoanDetail;
import com.sinosafe.xb.manager.utils.Constant;
import com.sinosafe.xb.manager.utils.MyFaceNetWorkWarranty;
import com.sinosafe.xb.manager.utils.MyUtils;
import com.sinosafe.xb.manager.utils.T;

import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import luo.library.base.base.BaseFragmentActivity;
import luo.library.base.base.BaseImage;
import luo.library.base.utils.GsonUtil;
import luo.library.base.utils.IntentUtil;
import luo.library.base.utils.MyLog;
import luo.library.base.widget.MoneyView;
import luo.library.base.widget.dialog.DialogInput;
import luo.library.base.widget.dialog.DialogMessage;
import pub.devrel.easypermissions.EasyPermissions;

import static com.sinosafe.xb.manager.R.id.btn_confirm_add;
import static luo.library.base.utils.GsonUtil.GsonToBean;


/**
 * 客户详情
 */
public class MianQianDetailActivity extends BaseFragmentActivity {

    @BindView(R.id.ivPhoto)
    ImageView mIvPhoto;
    @BindView(R.id.tvDistance)
    TextView mTvDistance;
    @BindView(R.id.tvName)
    TextView mTvName;
    @BindView(R.id.tvPhone)
    TextView mTvPhone;
    @BindView(R.id.tvAddress)
    TextView mTvAddress;
    @BindView(R.id.tvProductName)
    TextView mTvProductName;
    @BindView(R.id.tvApplyDate)
    TextView mTvApplyDate;
    @BindView(R.id.tvApplyAccount)
    MoneyView mTvApplyAccount;
    @BindView(R.id.tvMoneyType)
    TextView mTvMoneyType;
    @BindView(R.id.tvInterestRates)
    TextView mTvInterestRates;
    @BindView(R.id.tvTermType)
    TextView mTvTermType;
    @BindView(R.id.tvApplyMonth)
    TextView mTvApplyMonth;
    @BindView(R.id.tvPayType)
    TextView mTvPayType;
    @BindView(R.id.tvGuaranteeType)
    TextView mTvGuaranteeType;
    @BindView(R.id.tvBankAccount)
    TextView mTvBankAccount;
    @BindView(btn_confirm_add)
    Button mBtnConfirmAdd;
    @BindView(R.id.paymentHistoryLayout)
    LinearLayout paymentHistoryLayout;
    @BindView(R.id.btnTouBaodan)
    Button mBtnTouBaodan;
    @BindView(R.id.btnInterview)
    Button mBtnInterview;
    @BindView(R.id.buttonLayout)
    LinearLayout mButtonLayout;

    @BindView(R.id.ll_apply_pass)
    LinearLayout mApplyPass;//批复结结果 通过
    //审核通过
    @BindView(R.id.tvMoney)
    MoneyView mTvMoney;//金额
    @BindView(R.id.tv_loan_deadline)
    TextView mLoanDeadline;//贷款期限
    @BindView(R.id.tv_pass_repayment_methods)
    TextView mPassRepaymentMethods;//还款方式
    @BindView(R.id.tv_premium_rate)
    TextView mPremiumRate;//保险费率
    @BindView(R.id.tv_lending_rate)
    TextView mLendingRate;//贷款利率
    @BindView(R.id.tvLoanDes)
    TextView tvLoanDes;

    private YeWuBean yeWuBean;
    //事项类型0：默认；1：贷后管理
    private int type = 0;
    private DialogInput dialogInput;
    private MyFaceNetWorkWarranty myFaceNetWorkWarranty;
    //从哪里逻辑跳转过来，YeWuAdapter
    private String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_detail);

        ButterKnife.bind(this);
        initView();
        showWithStatus("加载中...");
        queryLoanDetail();
    }

    private void initView() {

        setTitleText("客户详情");
        myFaceNetWorkWarranty = new MyFaceNetWorkWarranty();
        mBtnConfirmAdd.setText("办理面签");

        yeWuBean = (YeWuBean) getIntent().getSerializableExtra("yeWuBean");
        type = getIntent().getIntExtra("type", 0);
        from = getIntent().getStringExtra("from");
    }

    /**
     * 展示贷款详情
     */
    private void showLoanDetail(){
        mTvName.setText(yeWuBean.getCUS_NAME());
        mTvPhone.setText(Html.fromHtml("<u>"+yeWuBean.getPHONE()+"</u>"));
        YeWuBean.User user = yeWuBean.getUser();

        //待面签
        if("112".equals(yeWuBean.getNEW_APPROVE_STATUS())){
            mBtnConfirmAdd.setVisibility(View.GONE);
            mButtonLayout.setVisibility(View.VISIBLE);
        }

        if (user != null) {
            BaseImage.getInstance().displayCricleImage(this,
                    user.getHead_photo(), mIvPhoto, R.mipmap.icon_default_avatar);
        }
        //面签位置、距离
        MyUtils.setDistance(mTvDistance, yeWuBean.getCREDIT_COORDINATE());
        MyUtils.regeocodeSearched(this, mTvAddress, yeWuBean.getCREDIT_COORDINATE());

        mTvProductName.setText(yeWuBean.getPRD_NAME());
        mTvApplyDate.setText(yeWuBean.getAPPLY_DATE());
        mTvApplyAccount.setMoneyText(MyUtils.keepTwoDecimal(yeWuBean.getAMOUNT()));
        mTvMoneyType.setText("人民币");
        mTvInterestRates.setText((MyUtils.keepTwoDecimal(yeWuBean.getUSING_IR()*100)) + "%");
        mTvTermType.setText("月");
        mTvApplyMonth.setText(yeWuBean.getTERM() + "个月");

        mTvPayType.setText(MyUtils.getRepaymentModel(yeWuBean.getREPAYMENT_MODE()));
        mTvGuaranteeType.setText(MyUtils.getGuaranteeModel(yeWuBean.getASSURE_MEANS_MAIN()));
        mTvBankAccount.setText(yeWuBean.getBANK_CARD_NO());

        if (type == 1) {
            mBtnConfirmAdd.setText("收集贷后凭证");
            paymentHistoryLayout.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.btn_confirm_add, R.id.paymentHistoryLayout,
            R.id.btnTouBaodan, R.id.btnInterview,R.id.tvPhone})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm_add:

                nextOperator();
                break;

            case R.id.paymentHistoryLayout:
                Bundle bundle = new Bundle();
                bundle.putString("serno", yeWuBean.getSERNO());
                bundle.putString("prd_id", yeWuBean.getPRD_ID());
                bundle.putString("payType", yeWuBean.getREPAYMENT_MODE());
                IntentUtil.gotoActivityForResult(this, PaymentHistoryActivity.class, PAGE_INTO_LIVENESS);
                break;
            //打印投保单
            case R.id.btnTouBaodan:
                setCommonEmail();
                break;
            //办理面签
            case R.id.btnInterview:
                nextOperator();
                break;

            //拨打电话
            case R.id.tvPhone:
                callPhoneTips(yeWuBean.getPHONE());
                break;
        }
    }

    //启动面签
    private void nextOperator() {
        if (type == 0) {

            //判断是否授权成功
            if(!MyFaceNetWorkWarranty.isFaceNetWorkWarranty()){
                showFaceNetWorkWarrantyDialog();
                return;
            }

            if (!EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)) {
                EasyPermissions.requestPermissions(this, "", 0x03, Manifest.permission.CAMERA);
                return;
            }
            //mHandler.sendEmptyMessageDelayed(0, 100);
            IntentUtil.gotoActivityForResult(this, LivenessActivity.class,PAGE_INTO_LIVENESS);
        }
        //贷后管理
        else if (type == 1) {
            Bundle bundle = new Bundle();
            bundle.putInt("type", 1);
            bundle.putString("title", "收集贷后凭证");
            bundle.putString("prdType", yeWuBean.getPRD_TYPE());
            bundle.putString("serno", yeWuBean.getSERNO());
            IntentUtil.gotoActivityForResult(this, ImageSelectActivity2.class, bundle, 10000);
        }
    }

    /**
     * 授权失败提示
     */
    private void showFaceNetWorkWarrantyDialog(){
        new DialogMessage(this).setMess("人脸识别联网授权失败，请重新授权!")
                .setConfirmListener(new DialogMessage.OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        faceNetGrant();
                    }
                }).show();
    }

    /**
     * 联网授权
     */
    private void faceNetGrant(){
        startProgressDialog("授权中...");
        myFaceNetWorkWarranty.faceNetWorkWarranty(new MyFaceNetWorkWarranty.NetWorkWarrantyCallback() {
            @Override
            public void grantResult(boolean result) {
                stopProgressDialog();
                if(result){
                    nextOperator();
                }else{
                    showFaceNetWorkWarrantyDialog();
                }
            }
        });
    }

    private static final int PAGE_INTO_LIVENESS = 200;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAGE_INTO_LIVENESS && resultCode == RESULT_OK) {
            String result = data.getStringExtra("result");
            showWithStatus("请稍候");
            faceResult(result);
        }

        //收集完贷后凭证回来
        else if (requestCode == 10000 && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }

    /**
     * 查询贷款信息及进度
     */
    protected void queryLoanDetail(){

        RequestParams params = new RequestParams(APIManager.SINOSAFE_URL+"manager/business/queryLoanDetail");
        params.addQueryStringParameter("token", BaseMainActivity.loginUserBean.getToken());
        params.addQueryStringParameter("serno", yeWuBean.getSERNO());
        params.addQueryStringParameter("prd_id",  yeWuBean.getPRD_ID());
        XutilsBaseHttp.requestType = -1;
        XutilsBaseHttp.get(params, onResponseListener);

    }

    /**
     * 人脸识别结果处理
     *
     * @param jsonResult
     */
    private void faceResult(String jsonResult) {
        try {
            JSONObject result = new JSONObject(jsonResult);
            int resID = result.getInt("resultcode");
            String filePath = result.getString("filePath");
            String delta = result.getString("delta");

            if (resID == R.string.verify_success) {
                File file = new File(filePath);
                if (file == null && !file.exists()) {
                    closeSVProgressHUD();
                    T.showShortBottom("人脸识别失败");
                    return;
                }
                RequestParams params = new RequestParams("https://api.megvii.com/faceid/v2/verify");
                params.addBodyParameter("api_key", Constant.MEGVII_API_KEY);
                params.addBodyParameter("api_secret", Constant.MEGVII_API_SECRET);
                params.addBodyParameter("idcard_name", yeWuBean.getCUS_NAME());
                params.addBodyParameter("idcard_number", yeWuBean.getCERT_CODE());
                params.addBodyParameter("comparison_type", "1");
                params.addBodyParameter("face_image_type", "meglive");
                params.addBodyParameter("delta", delta);
                params.addBodyParameter("image_best", file);
                XutilsBaseHttp.requestType = 1;
                XutilsBaseHttp.post(params, onResponseListener);
            } else if (resID == R.string.liveness_detection_failed_not_video ||
                    resID == R.string.liveness_detection_failed_timeout ||
                    resID == R.string.liveness_detection_failed) {
                T.showShortBottom(result.getString("result"));
            }

        } catch (Exception e) {
            T.showShortBottom("人脸识别失败");
            closeSVProgressHUD();
            e.printStackTrace();
        }
    }

    private OnResponseListener onResponseListener = new OnResponseListener() {
        @Override
        public void onRequestFailedCallback(String var1) {
            closeSVProgressHUD();
            if(XutilsBaseHttp.requestType==1)
                T.showShortBottom("人脸识别失败");
            else{
                MyLog.e("查询贷款信息及进度====="+var1);
                showErrorWithStatus(var1);
            }
        }

        @Override
        public void onFaceSuccessListCallback(String result) {
            closeSVProgressHUD();
            VerifyResultBean verifyResultBean = GsonToBean(result, VerifyResultBean.class);
            double e_6 = 79.9;
            double confidence = verifyResultBean.getResult_faceid().getConfidence();
            if (confidence >= e_6) {
                T.showShortBottom("人脸识别成功");
                mHandler.sendEmptyMessageDelayed(0, 400);
            } else {
                T.showShortBottom("人脸识别失败");
            }
        }
        @Override
        public void onCommonRequestCallback(String result) {

            BaseResult entity = GsonUtil.GsonToBean(result,BaseResult.class);
            //访问成功
            if(entity.getCode().equals("1")){
                LoanDetail loanDetail = GsonUtil.GsonToBean(entity.getResult(),LoanDetail.class);;
                //事项类型1：审批中；2：待放款；3：还款中；4：已拒绝；5：提供资料；6：去缴费；9：在线签投保单
                yeWuBean = loanDetail.getLOAN_DETAIL();
                showLoanDetail();
                //显示批复结果
                LoanDetail.LoanResult loanResult = loanDetail.getLOAN_RESULT();
                if(loanResult!=null){
                    showLoanResult(loanResult);
                }
            }else{
                showErrorWithStatus("加载失败");
                MyLog.e("获取贷款详情反馈===="+entity.getMsg());
            }
            closeSVProgressHUD();
        }
    };

    /**
     * 批复结果
     * @param loanResult
     */
    public void showLoanResult(LoanDetail.LoanResult loanResult){
        /**
         * 991	拿回
         998	否决(不同意)
         997	通过
         111	审批中
         992	打回
         000	待发起
         112	审批待面签
         993	取消
         1000	缴费失败
         994	待出单
         996	已出单
         995	待缴费
         00	正在审批
         1002	待代扣保费
         090	待补充资料
         091	待在线签署投保单
         1001	待放款
         1003	已放款
         */
        String status = yeWuBean.getNEW_APPROVE_STATUS();

        if (status.equals("1001") || status.equals("1003")) {//审批通过
            approve(loanResult);
        } else if (status.equals("995")) {//待在线支付保费
            approve(loanResult);
        }else if (status.equals("1001")) {//待放款
            approve(loanResult);
        } else if (status.equals("091")) {//需要在线签署投保单
            approve(loanResult);
        }else if(status.equals("112")){
            approve(loanResult);
        }
    }
    /**
     * 批复结果
     */
    private void approve(LoanDetail.LoanResult loanResult) {

        mApplyPass.setVisibility(View.VISIBLE);
        tvLoanDes.setVisibility(View.GONE);
        mTvMoney.setMoneyText(MyUtils.keepTwoDecimal(loanResult.getAPPROVE_AMOUNT()));//贷款金额
        mLoanDeadline.setText(loanResult.getAPPROVE_TERM() + "月");//贷款期限
        if (loanResult.getAPPROVE_REPAYMODE() != null)
            mPassRepaymentMethods.setText(MyUtils.getRepaymentModel(loanResult.getAPPROVE_REPAYMODE()));//批准的还款方式
        mPremiumRate.setText((MyUtils.keepTwoDecimal(loanResult.getCOST_RATE()*100)) + "%");//保险费率
        mLendingRate.setText((MyUtils.keepTwoDecimal(loanResult.getAPPROVE_RATE()*100)) + "%");//贷款利率

    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Bundle bundle = new Bundle();
            bundle.putSerializable("yeWuBean", yeWuBean);
            bundle.putString("from",from);
            IntentUtil.gotoActivity(MianQianDetailActivity.this, BanLiMianQianActivity.class, bundle);
        }
    };


    /**
     * 设置常用邮箱
     */
    private void setCommonEmail() {
        dialogInput = new DialogInput(this, new DialogInput.OnTextBackListener() {
            @Override
            public void onTextBack(String text) {
                if (!MyUtils.isEmail(text)) {
                    T.showShortBottom("请输入常用邮箱");
                    return;
                }
                dialogInput.dismiss();
                startProgressDialog("发送中...");
                printApplicationForm(text);

            }
        });
        dialogInput.setEditText(BaseMainActivity.loginUserBean.getUsermail());
        dialogInput.setTitle("打印投保单").setTextInputType().
                setHint("请输入常用邮箱").show();
    }


    /**
     * 打印投保单
     */
    private void printApplicationForm(String emailStr) {

        Map<String, String> map = new HashMap<>();
        map.put("token", BaseMainActivity.loginUserBean.getToken());
        map.put("serno", yeWuBean.getSERNO());
        map.put("email", emailStr);
        map.put("cert_code", yeWuBean.getCERT_CODE());
        map.put("username", yeWuBean.getCUS_NAME());
        map.put("device_type", "01");
        map.put("type", "1");
        ClientModel.sendInsuranceEmail(map)
                .timeout(20, TimeUnit.SECONDS)
                .compose(RxSchedulersHelper.<BaseEntity>io_main())
                .map(new RxHttpBaseResultFunc())
                .subscribe(new EmailRxSubscriber());
    }


    class EmailRxSubscriber extends RxSubscriber<BaseEntity> {
        @Override
        public void _onNext(BaseEntity entity) {
            stopProgressDialog();
            if (entity.getCode() == 1) {
                T.showShortBottom(entity.getMsg());
            } else {
                showErrorWithStatus(entity.getMsg());
            }
        }

        @Override
        public void _onError(String msg) {
            MyLog.e("打印投保单失败反馈："+msg);
            stopProgressDialog();
            showErrorWithStatus(msg);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myFaceNetWorkWarranty.myDestory();
        mHandler.removeCallbacksAndMessages(null);
    }
}
