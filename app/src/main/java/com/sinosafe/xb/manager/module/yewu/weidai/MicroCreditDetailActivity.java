package com.sinosafe.xb.manager.module.yewu.weidai;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import com.lzy.imagepicker.bean.ImageItem;
import com.sinosafe.xb.manager.BaseMainActivity;
import com.sinosafe.xb.manager.R;
import com.sinosafe.xb.manager.api.APIManager;
import com.sinosafe.xb.manager.api.rxjava.RxHttpBaseResultFunc;
import com.sinosafe.xb.manager.api.rxjava.RxSchedulersHelper;
import com.sinosafe.xb.manager.api.rxjava.RxSubscriber;
import com.sinosafe.xb.manager.api.xutilshttp.OnResponseListener;
import com.sinosafe.xb.manager.api.xutilshttp.XutilsBaseHttp;
import com.sinosafe.xb.manager.bean.BaseEntity;
import com.sinosafe.xb.manager.bean.YeWuBean;
import com.sinosafe.xb.manager.model.ClientModel;
import com.sinosafe.xb.manager.module.yewu.xiaofeidai.bean.BaseResult;
import com.sinosafe.xb.manager.module.yewu.xiaofeidai.bean.LoanDetail;
import com.sinosafe.xb.manager.utils.Constant;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import luo.library.base.base.BaseFragmentActivity;
import luo.library.base.utils.GsonUtil;
import luo.library.base.utils.MyLog;

/**
 * 微贷详情和进度
 */
public class MicroCreditDetailActivity extends BaseFragmentActivity {
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;

    private SimpleFragmentPagerAdapter pagerAdapter;
    //贷款详情和进度
    private LoanDetail loanDetail;
    private String serno = "";
    private String prdId = "";
    //事项类型1：审批中；2：待放款；3：还款中；4：已拒绝；5：提供资料；6：去缴费；9：在线签投保单
    private int type = 0;
    //贷后凭证标记
    private String isLoanCheck;
    //从哪里逻辑跳转过来，YeWuAdapter
    private String from;

    //贷款详情
    private MicroCreditDetailFragment microCreditDetailFragment;
    //贷款进度
    private MicroCreditProgressFragment microCreditProgressFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consume_detail);
        ButterKnife.bind(this);


        initView();
        showWithStatus("");
        queryLoanDetail();
    }

    private void initView() {

        setTitleText("贷款详情");

        serno = getIntent().getStringExtra("serno");
        prdId = getIntent().getStringExtra("prdId");
        type = getIntent().getIntExtra("type",0);
        isLoanCheck = getIntent().getStringExtra("isLoanCheck");
        from = getIntent().getStringExtra("from");

        // 设置适配器
        pagerAdapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(pagerAdapter);

        mTabLayout.post(new Runnable() {
            @Override
            public void run() {
                setIndicator(mTabLayout,35,35);
            }
        });
        // 为TabLayout设置ViewPager
        mTabLayout.setupWithViewPager(mViewPager);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //补充资料完成后、刷新数据
        if(requestCode==10000&&resultCode==RESULT_OK){
            if(isLoanCheck==null||"".equals(isLoanCheck)||"null".equals(isLoanCheck)){
                if(isNull(from)){
                    setResult(RESULT_OK);
                }else{
                    MyLog.e("==============================================================type="+type);
                    Intent intent = new Intent(Constant.REFRESH_YEWU_DATA);
                    intent.putExtra("type",type);
                    sendBroadcast(intent);
                }
            }else{
                //刷新还款中数据
                Intent intent = new Intent(Constant.REFRESH_YEWU_REPAYMENT_DATA);
                sendBroadcast(intent);
            }
            finish();
        }
    }


    class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {
        // TabLayout title
        private String tabTitles[] = new String[]{"详情", "审批进度"};

        public SimpleFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position==0) {
                if(microCreditDetailFragment==null)
                    microCreditDetailFragment = new MicroCreditDetailFragment();
                return microCreditDetailFragment;
            }
            else {
                if(microCreditProgressFragment==null)
                    microCreditProgressFragment = new MicroCreditProgressFragment();
                return microCreditProgressFragment;
            }
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // 返回纯文字
            return tabTitles[position];
        }
    }

    /**
     * 改变 TabLayout 下划线的长度
     * @param tabs
     * @param leftDip
     * @param rightDip
     */
    public void setIndicator(TabLayout tabs, int leftDip, int rightDip){
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }


    /**
     * 查询贷款信息及进度
     */
    protected void queryLoanDetail(){

        RequestParams params = new RequestParams(APIManager.SINOSAFE_URL+"manager/business/queryLoanDetail");
        params.addQueryStringParameter("token", BaseMainActivity.loginUserBean.getToken());
        params.addQueryStringParameter("serno", serno);
        params.addQueryStringParameter("prd_id",  prdId);
        XutilsBaseHttp.requestType = -1;
        XutilsBaseHttp.get(params, onResponseListener);

    }


    private OnResponseListener onResponseListener = new OnResponseListener() {
        @Override
        public void onRequestFailedCallback(String var1) {
            closeSVProgressHUD();
            MyLog.e("查询贷款信息及进度====="+var1);
            showErrorWithStatus("加载失败");
        }
        @Override
        public void onCommonRequestCallback(String result) {

            BaseResult entity = GsonUtil.GsonToBean(result,BaseResult.class);
            //访问成功
            if(entity.getCode().equals("1")){
                loanDetail = GsonUtil.GsonToBean(entity.getResult(),LoanDetail.class);;
                queryLoanImageFile();
                //事项类型1：审批中；2：待放款；3：还款中；4：已拒绝；5：提供资料；6：去缴费；9：在线签投保单
                YeWuBean yeWuBean = loanDetail.getLOAN_DETAIL();
                setTypeValue(yeWuBean);
                //显示贷款详情
                microCreditDetailFragment.initView(yeWuBean);
                //显示进度
                microCreditProgressFragment.initView(loanDetail.getLOAN_SCHEDUAL());
            }else{
                showErrorWithStatus(entity.getMsg());
                closeSVProgressHUD();
            }
        }
    };

    private void setTypeValue(YeWuBean yeWuBean){
        if(type==0){
            ////补充资料
            if("090".equals(yeWuBean.getNEW_APPROVE_STATUS())){
                type = 1;
            }
            // //待缴费||在线签投保单
            else if("995".equals(yeWuBean.getNEW_APPROVE_STATUS())||"091".equals(yeWuBean.getNEW_APPROVE_STATUS())||
                    "1001".equals(yeWuBean.getNEW_APPROVE_STATUS())){
                type = 2;
            }
            // 已放款 ==收集贷后凭证
            else if("1003".equals(yeWuBean.getNEW_APPROVE_STATUS())){
                type = 3;
            }
        }
    }

    /**
     * 加载影像图片文件
     */
    private void queryLoanImageFile(){

        Map<String,String> map = new HashMap<>();
        map.put("token",BaseMainActivity.loginUserBean.getToken());
        map.put("serno",serno);
        ClientModel.queryLoanImageFile(map)
            .timeout(20, TimeUnit.SECONDS)
            .compose(RxSchedulersHelper.<BaseEntity>io_main())
            .map(new RxHttpBaseResultFunc())
            .subscribe(new MyRxSubscriber());
    }

    class MyRxSubscriber extends RxSubscriber<BaseEntity>{
            @Override
            public void _onNext(BaseEntity entity) {
                closeSVProgressHUD();
                if(entity.getCode()==1){
                    handleLoanImages(entity.getResult().toString());
                }
            }
            @Override
            public void _onError(String msg) {
                closeSVProgressHUD();
            }
    }


    private void handleLoanImages(String filepaths){

        List<ImageItem> images = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(filepaths);
            Iterator<String> iterator = object.keys();
            while (iterator.hasNext()){
                String key = iterator.next();
                ImageItem imageItem = new ImageItem();
                imageItem.path = object.getString(key);
                images.add(imageItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //显示影像图片
        microCreditDetailFragment.initLoanImageFile(images);
    }

    public int getType() {
        return type;
    }
    public String getSerno() {
        return serno;
    }
    public String getIsLoanCheck() {
        return isLoanCheck;
    }
    public String getFrom() {
        return from;
    }
}
