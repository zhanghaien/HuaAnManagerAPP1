package com.sinosafe.xb.manager.api.rxjava;

import com.sinosafe.xb.manager.module.home.xiaofeidai.bean.YaLianMengResult;

import rx.functions.Func1;

/**
 * 类名称：   com.cnmobi.jichengguang.rxjava
 * 内容摘要： //Rx处理服务器返回
 * 修改备注：
 * 创建时间： 2017/5/11
 * 公司：     深圳市华移科技股份有限公司
 * 作者：     Mr 张
 */

public class RxHttpYlmResultFunc implements Func1<YaLianMengResult, YaLianMengResult> {

    @Override public YaLianMengResult call(YaLianMengResult httpResult) {

        return httpResult;
    }
}
