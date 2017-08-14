package com.sinosafe.xb.manager.api.rxjava;

import com.sinosafe.xb.manager.api.rxjava.myException.OtherException;
import com.sinosafe.xb.manager.api.rxjava.myException.ReloginException;
import com.sinosafe.xb.manager.bean.BaseEntity;

import rx.functions.Func1;

/**
 * 类名称：   com.cnmobi.jichengguang.rxjava
 * 内容摘要： //Rx处理服务器返回
 * 修改备注：
 * 创建时间： 2017/5/11
 * 公司：     深圳市华移科技股份有限公司
 * 作者：     Mr 张
 */

public class RxHttpResultFunc<T> implements Func1<BaseEntity<T>, T> {

    @Override public T call(BaseEntity<T> httpResult) {

        if(httpResult.getCode()==1)
            return httpResult.getResult();

        else if(httpResult.getCode()==422){
            throw new ReloginException(httpResult.getMsg());

        }else
            throw new OtherException(httpResult.getMsg());
        //return httpResult.getResult();
    }
}
