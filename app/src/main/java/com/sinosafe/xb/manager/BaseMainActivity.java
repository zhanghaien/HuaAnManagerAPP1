package com.sinosafe.xb.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.megvii.idcardquality.IDCardQualityLicenseManager;
import com.megvii.licensemanager.Manager;
import com.megvii.livenessdetection.LivenessLicenseManager;
import com.megvii.livenesslib.util.ConUtil;
import com.sinosafe.xb.manager.api.rxjava.RxHttpResultFunc;
import com.sinosafe.xb.manager.api.rxjava.RxSchedulersHelper;
import com.sinosafe.xb.manager.api.rxjava.RxSubscriber;
import com.sinosafe.xb.manager.bean.BaseEntity;
import com.sinosafe.xb.manager.bean.LoginUserBean;
import com.sinosafe.xb.manager.bean.VersionBean;
import com.sinosafe.xb.manager.model.ClientModel;
import com.sinosafe.xb.manager.utils.MyAMapLocUtils;
import com.sinosafe.xb.manager.utils.MyUtils;
import com.sinosafe.xb.manager.utils.T;

import java.io.File;
import java.util.concurrent.TimeUnit;

import luo.library.base.base.BaseFragmentActivity;
import luo.library.base.utils.MyLog;
import luo.library.base.utils.UpdateService;
import luo.library.base.widget.dialog.DialogMessage;

/**
 * 类名称：   com.cnmobi.huaan.manager
 * 内容摘要： //首页基类。
 * 修改备注：
 * 创建时间： 2017/6/3 0003
 * 公司：     深圳市华移科技股份有限公司
 * 作者：     Mr 张
 */
public class BaseMainActivity extends BaseFragmentActivity {

    // 顶部菜单索引
    public static final int TAB_page1 = 0;
    public static final int TAB_page2 = 1;
    public static final int TAB_page3 = 2;
    public static final int TAB_page4 = 3;

    /**碎片适配器**/
    protected FragmentAdapter adapter;
    //登录用户信息
    public static LoginUserBean loginUserBean;
    protected MyAMapLocUtils aMapLocUtils;
    protected long exitTime = 0;
    protected String uuid;
    public static boolean exitOver = false;
    public static boolean isForeground = false;
    public static boolean hasNewMess = false;
    //下载链接
    private String url;
    private boolean isShowingVersionDialog = false;

    @Override
    protected void onStart() {
        super.onStart();
        //身份证 联网授权
        if(getSp("idCardNetWorkWarranty","")==null||getSp("idCardNetWorkWarranty","").equals("")){
            if(uuid==null||uuid.equals(""))
                uuid = ConUtil.getUUIDString(BaseMainActivity.this);
            idCardNetWorkWarranty();
        }

        if(getSp("faceNetWorkWarranty","")==null||getSp("faceNetWorkWarranty","").equals("")){
            if(uuid==null||uuid.equals(""))
                uuid = ConUtil.getUUIDString(BaseMainActivity.this);
            faceNetWorkWarranty();
        }
        mHandler.sendEmptyMessageDelayed(7,1*1000);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                T.showShortBottom("再按一次退出");
                exitTime = System.currentTimeMillis();
            } else {
                moveTaskToBack(false);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        try {
            unregisterReceiver(mMessageReceiver);
        }catch (Exception e){}
        super.onDestroy();
    }

    /**
     * 身份证 联网授权
     */
    public void idCardNetWorkWarranty() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Manager manager = new Manager(BaseMainActivity.this);
                IDCardQualityLicenseManager idCardLicenseManager = new IDCardQualityLicenseManager(BaseMainActivity.this);
                manager.registerLicenseManager(idCardLicenseManager);
                try {
                    manager.takeLicenseFromNetwork(uuid);
                }catch (Exception e){
                }
                //授权成功
                if (idCardLicenseManager.checkCachedLicense() > 0)
                    mHandler.sendEmptyMessage(1);
                    //授权失败
                else
                    mHandler.sendEmptyMessage(2);
            }
        }).start();
    }

    /**
     * 人脸识别 联网授权
     */
    public void faceNetWorkWarranty() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Manager manager = new Manager(BaseMainActivity.this);
                LivenessLicenseManager licenseManager = new LivenessLicenseManager(BaseMainActivity.this);
                manager.registerLicenseManager(licenseManager);
                try {
                    manager.takeLicenseFromNetwork(uuid);
                }catch (Exception e){
                }
                if (licenseManager.checkCachedLicense() > 0)
                    mHandler.sendEmptyMessage(3);
                else
                    mHandler.sendEmptyMessage(4);
            }
        }).start();
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    MyLog.e("身份证联网授权成功");
                    putSp("idCardNetWorkWarranty","idCardNetWorkWarranty");
                    break;
                case 2:
                    MyLog.e("身份证联网授权失败");
                    putSp("idCardNetWorkWarranty","");
                    mHandler.sendEmptyMessageDelayed(5,1000*30);
                    break;
                case 3:
                    putSp("faceNetWorkWarranty","faceNetWorkWarranty");
                    MyLog.e("人脸识别联网授权成功");
                    break;
                case 4:
                    putSp("faceNetWorkWarranty","");
                    MyLog.e("人脸识别联网授权成失败");
                    mHandler.sendEmptyMessageDelayed(6,1000*30);
                    break;
                case 5:
                    idCardNetWorkWarranty();
                    break;
                case 6:
                    faceNetWorkWarranty();
                    break;

                case 7:
                    checkVersion();
                    break;
            }
        }
    };

    //for receive customer msg from jpush server
    public MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        filter.addAction("COM_SINOSAFE_XB_BANNER_ACTION");
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                    String messge = intent.getStringExtra(KEY_MESSAGE);
                    String extras = intent.getStringExtra(KEY_EXTRAS);
                    StringBuilder showMsg = new StringBuilder();
                    showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                    if (!TextUtils.isEmpty(extras)) {
                        showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                    }
                    setCostomMsg(showMsg.toString());
                    hasNewMess = true;
                }
                //查看产品
                else if("COM_SINOSAFE_XB_BANNER_ACTION".equals(intent.getAction())){

                }
            } catch (Exception e){
            }
        }
    }

    /**
     * 版本检查
     */
    protected void checkVersion(){
        ClientModel.checkVersion("1","0")
                .timeout(20, TimeUnit.SECONDS)
                .compose(RxSchedulersHelper.<BaseEntity<VersionBean>>io_main())
                .map(new RxHttpResultFunc<VersionBean>())
                .subscribe(new VersionRxSubscriber());
    }

    class VersionRxSubscriber extends RxSubscriber<VersionBean> {
        @Override
        public void _onNext(VersionBean versionBean) {
            showVersionInfo(versionBean);
        }
        @Override
        public void _onError(String msg) {
            MyLog.e("版本检测反馈msg===="+msg);
        }
    }

    /**
     * 显示版本信息
     * @param versionBean
     */
    protected void showVersionInfo(VersionBean versionBean){
        if(versionBean.getVersion_code()<= MyUtils.getVersionCode(this)) {
            return;
        }
        if(UpdateService.isDownloading||isShowingVersionDialog)
            return;
        url = versionBean.getDownload_url();
        new DialogMessage(this,false).setTitle("版本检测")
                .setMess("当前有新版本可更新，旧版本即将不再提供服务，请尽快前往更新。")
                .setConfirmTips("确认")
                .setConfirmListener(new DialogMessage.OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        handleSoftUpdate();
                    }
                })
                .setOtherConfirmListener(new DialogMessage.OnOtherConfirmListener() {
                    @Override
                    public void onOtherConfirm() {
                        exitOver = true;
                        finish();
                    }
                }).show();
        isShowingVersionDialog = true;
    }

    /**
     * 处理软件更新
     */
    private void handleSoftUpdate(){
        //判断是否已经下载
        String  fileName = this.url.substring(this.url.lastIndexOf("/") + 1, this.url.length());
        File file = new File(UpdateService.DOWNLOAD_PATH + fileName);
        if(file.exists()){
            //startProgressDialog("更新中...");
            installApk(this,file);
        }else{
            startProgressDialog("更新中...");
            Intent intent = new Intent(BaseMainActivity.this,UpdateService.class);
            intent.putExtra("url",url);
            startService(intent);
        }
    }

    private  void installApk(Context context, File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
        exitOver = true;
        finish();
    }

    private void setCostomMsg(String msg){

    }

    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
    }


    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }
}
