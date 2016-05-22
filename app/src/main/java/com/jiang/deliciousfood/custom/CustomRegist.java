package com.jiang.deliciousfood.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jiang.deliciousfood.R;

/**
 * Created by Administrator on 2016/4/25.
 * 自定义注册时候的View
 */
public class CustomRegist extends RelativeLayout {
    private View mView;
    private Drawable registimg;
    private String hintedit;
    private String  isPwd;
    private ImageView mImageView;
    private EditText mEditText;

    public CustomRegist(Context context) {
        super(context);
        mView = LayoutInflater.from(context).inflate(R.layout.layout_customregist, this);
        onFinishInflate();
    }

    public CustomRegist(Context context, AttributeSet attrs) {
        super(context, attrs);
        mView = LayoutInflater.from(context).inflate(R.layout.layout_customregist, this);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CustomRegist);
        registimg=array.getDrawable(R.styleable.CustomRegist_registimg);
        hintedit=array.getString(R.styleable.CustomRegist_edithint);
        isPwd=array.getString(R.styleable.CustomRegist_passtype);

        mEditText= (EditText) mView.findViewById(R.id.regist_edit);
        mImageView= (ImageView)mView. findViewById(R.id.regis_img);
    }

    /**
     * 完成xml加载,只有静态加载之后走这个方法，动态不执行
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mEditText= (EditText) mView.findViewById(R.id.regist_edit);
        mImageView = (ImageView)mView. findViewById(R.id.regis_img);
        setEditTextHint(hintedit);
        setLeftImg(registimg);
        setPassType(isPwd);
    }

    /**
     * 设置左边图片
     */
    public void setLeftImg(Drawable drawable) {
       mImageView.setImageDrawable(drawable);
    }

    /**
     * 设置数据框提示文字
     *
     * @param hint
     */
    public void setEditTextHint(String hint) {
        mEditText.setHint(hint);
    }

    public String getRegistText(){
        String info=mEditText.getText().toString();
        return info;
    }

    public void setPassType(String ispwd){
        if("true".equals(ispwd))
            mEditText.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }
}
