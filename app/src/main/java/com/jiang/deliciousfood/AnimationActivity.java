package com.jiang.deliciousfood;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jaeger.library.StatusBarUtil;
import com.jiang.deliciousfood.commen.CheckUser;

import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Thread.sleep;

public class AnimationActivity extends AppCompatActivity {
    private ImageView mImageView;
    private AnimationSet mAnimationSet;
    private RotateAnimation mRotateAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);

        mImageView = (ImageView)findViewById(R.id.welcome_img);
        mImageView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.begin_anim));
        StatusBarUtil.setTranslucent(this, 0);

        //getLoginActivity();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Intent intent = new Intent(AnimationActivity.this, LoginActivity.class);
                    sleep(2000);
                    startActivity(intent);
                    sleep(500);
                    finish();
                    overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 定时器 2秒后跳转Activity
     */
    private void getLoginActivity() {

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                boolean isExist=CheckUser.isExist(AnimationActivity.this);
                if(isExist){
                    Intent intent = new Intent(AnimationActivity.this, MainActivity.class);

                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                }else{
                    Intent intent = new Intent(AnimationActivity.this, LoginActivity.class);

                    startActivity(intent);

                    finish();
                    overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                }
            }
        };
        timer.schedule(task, 2000);
    }
}
