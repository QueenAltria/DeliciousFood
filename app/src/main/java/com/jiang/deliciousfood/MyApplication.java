package com.jiang.deliciousfood;

import android.app.Activity;
import android.app.Application;

import com.alipay.share.sdk.openapi.APAPIFactory;
import com.alipay.share.sdk.openapi.IAPApi;
import com.jiang.deliciousfood.commen.Constant;
import com.thinkland.sdk.android.JuheSDKInitializer;

import org.xutils.x;

import java.util.LinkedList;
import java.util.List;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.update.BmobUpdateAgent;

/**
 * Created by Administrator on 2016/4/19.
 * 一些初始化的实现
 */
public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化聚合数据
        JuheSDKInitializer.initialize(getApplicationContext());
        //初始化BmobSDK\
        Bmob.initialize(this, "c1eb1f1c08e6dbab8cb7abd1759ea3f8");

        // 使用推送服务时的初始化操作
        BmobInstallation.getCurrentInstallation(this).save();
        // 启动推送服务
        BmobPush.startWork(this);

        //初始化Xutils
        x.Ext.init(this);

        IAPApi api = APAPIFactory.createZFBApi(getApplicationContext(), Constant.APP_ID, false);

    }
}
