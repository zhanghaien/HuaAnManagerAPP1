# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

    #指定代码的压缩级别 代码混淆压缩比，在0~7之间，默认为5，一般不做修改
    -optimizationpasses 5

    #包明不混合大小写 混合后的类名为小写
    -dontusemixedcaseclassnames

    #指定不去忽略非公共库的类
    -dontskipnonpubliclibraryclasses

     #优化  不优化输入的类文件
    -dontoptimize

     #不做预校验，preverify是proguard的四个步骤之一，Android不需要preverify
    -dontpreverify

     #混淆时是否记录日志
    -verbose

    #保护注解
    -keepattributes *Annotation*
    # 抛出异常时保留代码行号
    -keepattributes SourceFile,LineNumberTable
    # 指定混淆是采用的算法，后面的参数是一个过滤器
    # 这个过滤器是谷歌推荐的算法，一般不做更改
    -optimizations !code/simplification/arithmetic,!field/*,!class/merging/*  # 混淆时所采用的算法

    #############################################
    #
    # Android开发中一些需要保留的公共部分
    #
    #############################################
    # 保持哪些类不被混淆
    -keep public class * extends android.app.Fragment
    -keep public class * extends android.app.Activity
    -keep public class * extends android.app.Application
    -keep public class * extends android.app.Service
    -keep public class * extends android.content.BroadcastReceiver
    -keep public class * extends android.content.ContentProvider
    -keep public class * extends android.app.backup.BackupAgentHelper
    -keep public class * extends android.preference.Preference
    -keep public class com.android.vending.licensing.ILicensingService
    -keep public class * extends android.view.View
    #保留继承的
    -keep public class * extends android.support.v4.**
    -keep public class * extends android.support.v7.**
    -keep public class * extends android.support.annotation.**
    # 保留在Activity中的方法参数是view的方法，
    # 这样以来我们在layout中写的onClick就不会被影响
    -keepclassmembers class * extends android.app.Activity{
        public void *(android.view.View);
    }

    #忽略警告
    -ignorewarning
    #apk 包内所有 class 的内部结构
    -dump class_files.txt
    #未混淆的类和成员
    -printseeds seeds.txt
    #列出从 apk 中删除的代码
    -printusage unused.txt
    #混淆前后的映射
    -printmapping mapping.txt

    #####混淆保护自己项目的部分代码以及引用的第三方jar包library#######
    #公司验证
    -keep class com.cnmobi.appmanagement.** { *; }
    #高德地图
    -keep class com.amap.api.** { *; }
    -keep class autonavi.aps.amapapi.model.** { *; }
    #饼状图
    -keep class lecho.lib.hellocharts.** { *; }
    #极光推送
    -dontwarn cn.jpush.**
    -keep class cn.jiguang.** { *; }
    -keep class cn.jpush.** { *; }
    #pdf阅读
    -keep class com.github.barteksc.pdfviewer.** { *; }
    #滚动选择器wheelview
    #-keep com.wheelview.** { *; }

    -keep class com.bumptech.glide.** { *; }
    -keep class com.goodle.android.gms.** { *; }
    -keep class com.huawei.android.** { *; }
    -keep class org.apache.** { *; }

    #银行卡扫描、人脸识别
    -keep class com.megvii.** { *; }
    #华安上传文件
    -keep class com.yzj.** { *; }
    #base64
    -keep class Decoder.** { *; }
    #友盟
    -keep class com.umeng.**{*;}
    #如果引用了v4或者v7包
    -dontwarn android.support.**
    -keep class android.support.**{*;}

    #rxjava adapter
    -keep class retrofit2.adapter.rxjava.**{*;}
    -keep class retrofit2.converter.gson.**{*;}
    # OkHttp3
    -dontwarn com.squareup.okhttp3.**
    -keep class com.squareup.okhttp3.** { *;}
    -dontwarn okio.**
    -keep class okhttp3.**{*;}
    # Okio
    -dontwarn com.squareup.**
    -dontwarn okio.**
    -keep class okio.**{*;}
    -keep public class org.codehaus.* { *; }
    -keep public class java.nio.* { *; }
    -keep class rx.**{*;}
    # RxJava RxAndroid
    -dontwarn sun.misc.**
    -keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
        long producerIndex;
        long consumerIndex;
    }
    -keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
        rx.internal.util.atomic.LinkedQueueNode producerNode;
    }
    -keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
        rx.internal.util.atomic.LinkedQueueNode consumerNode;
    }
    # Retrofit
    -dontwarn retrofit2.**
    -keep class retrofit2.** { *; }
    -keepattributes Signature
    -keepattributes Exceptions


    -keep class pub.devrel.easypermissions.**{*;}
    -keep class com.alibaba.fastjson.**{*;}
    #glide图片加载
    -keep class com.bumptech.glide.**{*;}
    # Glide
    -keep public class * implements com.bumptech.glide.module.GlideModule
    -keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
      **[] $VALUES;
      public *;
    }
    #图片选择器
    -keep class com.lzy.imagepicker.**{*;}


    ####混淆保护自己项目的部分代码以及引用的第三方jar包library-end####
    # 保留我们自定义控件（继承自View）不被混淆
    -keep public class * extends android.view.View {
        public <init>(android.content.Context);
        public <init>(android.content.Context, android.util.AttributeSet);
        public <init>(android.content.Context, android.util.AttributeSet, int);
        public void set*(...);
    }

    #保持 native 方法不被混淆
    -keepclasseswithmembernames class * {
        native <methods>;
    }

    #保持自定义控件类不被混淆
    -keepclasseswithmembers class * {
        public <init>(android.content.Context, android.util.AttributeSet);
    }

    #保持自定义控件类不被混淆
    -keepclassmembers class * extends android.app.Activity {
       public void *(android.view.View);
    }

    #保持 Parcelable 不被混淆
    -keep class * implements android.os.Parcelable {
      public static final android.os.Parcelable$Creator *;
    }
    #保持 Serializable 不被混淆
    -keepnames class * implements java.io.Serializable
    #xutils
    -keep class org.xutils.**{ *; }
    #umeng友盟统计
    -keep class com.umeng.** { *; }
    -keep class com.tencent.** { *; }
    #adapter也不能混淆
    -keep public class * extends android.widget.BaseAdapter { *; }
    -keep public class com.sinosafe.xb.manager.adapter.** { *; }
    #数据模型不要混淆
    -keep public class com.sinosafe.xb.manager.bean.** { *; }
    -keep public class com.sinosafe.xb.manager.module.home.weidai.bean.** { *; }
    -keep public class com.sinosafe.xb.manager.module.home.xiaofeidai.bean.** { *; }
    -keep public class com.sinosafe.xb.manager.module.yeji.bean.** { *; }
    -keep public class com.sinosafe.xb.manager.module.home.loanmanager.bean.**{ *; }
    -keep public class com.sinosafe.xb.manager.module.yewu.xiaofeidai.bean.**{ *; }
    -keep public class luo.library.base.bean.** { *; }
    -keep public class luo.library.base.widget.banner.CycleVpEntity{ *; }
    -keep public class com.lzy.imagepicker.bean.**{*; }

    #保持 Serializable 不被混淆并且enum 类也不被混淆
    -keepclassmembers class * implements java.io.Serializable {
        static final long serialVersionUID;
        private static final java.io.ObjectStreamField[] serialPersistentFields;
        !static !transient <fields>;
        !private <fields>;
        !private <methods>;
        private void writeObject(java.io.ObjectOutputStream);
        private void readObject(java.io.ObjectInputStream);
        java.lang.Object writeReplace();
        java.lang.Object readResolve();
    }

    -keepclassmembers enum * {     # 保持枚举 enum 类不被混淆
        public static **[] values();
        public static ** valueOf(java.lang.String);
    }

    -keepclassmembers class * {
        public void *ButtonClicked(android.view.View);
    }

    #不混淆资源类
    -keepclassmembers class **.R$* {
        public static <fields>;
    }

    #如果用用到Gson解析包的，直接添加下面这几行就能成功混淆，不然会报错。
    #gson
    #-libraryjars libs/gson-2.2.2.jar
    -keepattributes Signature
    # Gson specific classes
    -keep class sun.misc.Unsafe { *; }
    # Application classes that will be serialized/deserialized over Gson
    -keep class com.google.gson.examples.android.model.** { *; }


    #友盟
    -keepclassmembers class * {
       public <init> (org.json.JSONObject);
    }
    -keep public class [com.cnmobi.xiaomingwangguo].R$*{
    public static final int *;
    }
    -keepclassmembers enum * {
        public static **[] values();
        public static ** valueOf(java.lang.String);
    }
    # 对于带有回调函数的onXXEvent、**On*Listener的，不能被混淆
    -keepclassmembers class * {
        void *(**On*Event);
        void *(**On*Listener);
    }

    # webView处理，项目中没有使用到webView忽略即可
    -keepclassmembers class fqcn.of.javascript.interface.for.webview {
        public *;
    }
    -keepclassmembers class * extends android.webkit.webViewClient {
        public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
        public boolean *(android.webkit.WebView, java.lang.String);
    }
    -keepclassmembers class * extends android.webkit.webViewClient {
        public void *(android.webkit.webView, jav.lang.String);
    }
    # ButterKnife
    -keep class butterknife.** { *; }
    -dontwarn butterknife.internal.**
    -keep class **$$ViewBinder { *; }
    -keepclasseswithmembernames class * {
        @butterknife.* <fields>;
    }
    -keepclasseswithmembernames class * {
        @butterknife.* <methods>;
    }