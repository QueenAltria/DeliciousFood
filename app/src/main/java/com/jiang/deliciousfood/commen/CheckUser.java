package com.jiang.deliciousfood.commen;

import android.content.Context;

import com.jiang.deliciousfood.bean.MyUser;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2016/4/24.
 * 检测本地是否存在用户
 */
public class CheckUser {
    public static boolean isExist(Context context){
        MyUser userInfo = BmobUser.getCurrentUser(context, MyUser.class);
        if(userInfo != null){
            // 允许用户使用应用
            return true;
        }else{
            //缓存用户对象为空时， 可打开用户注册界面…
            return false;
        }
    }
}
