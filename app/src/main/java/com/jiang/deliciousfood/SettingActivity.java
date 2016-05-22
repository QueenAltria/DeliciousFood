package com.jiang.deliciousfood;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jaeger.library.StatusBarUtil;
import com.jiang.deliciousfood.commen.Constant;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.orhanobut.dialogplus.ViewHolder;

import java.io.IOException;
import java.util.Random;

import cn.bmob.v3.listener.BmobUpdateListener;
import cn.bmob.v3.update.BmobUpdateAgent;
import cn.bmob.v3.update.UpdateResponse;
import cn.bmob.v3.update.UpdateStatus;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar mToolbar;
    int mColor;
    TextView mSetAbout, mSetLevel, changeColor, mSetShare, mSetSend, mSetDemo, mUpdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mSetAbout = (TextView) findViewById(R.id.set_about);
        mSetLevel = (TextView) findViewById(R.id.set_level);
        changeColor = (TextView) findViewById(R.id.change_color);
        mSetShare = (TextView) findViewById(R.id.set_share);
        mSetSend = (TextView) findViewById(R.id.set_send);
        mSetDemo = (TextView) findViewById(R.id.set_demo);
        mUpdata = (TextView) findViewById(R.id.set_update);

        mToolbar = (Toolbar) findViewById(R.id.set_toolbar);
        mToolbar.setTitle("设置");
        mToolbar.setNavigationIcon(R.mipmap.ic_chevron_left_white_24dp);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setBackgroundColor(getResources().getColor(R.color.colorBlue));
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorBlue), 0);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //startActivity(intent);
                SettingActivity.this.finish();
            }
        });


        mSetAbout.setOnClickListener(this);
        mSetLevel.setOnClickListener(this);
        changeColor.setOnClickListener(this);
        mSetShare.setOnClickListener(this);
        mSetSend.setOnClickListener(this);
        mSetDemo.setOnClickListener(this);
        mUpdata.setOnClickListener(this);


        try {
            mSetLevel.setText("版本号\t" + getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        mSetLevel.setOnClickListener(new View.OnClickListener() {
            //需要监听几次点击事件数组的长度就为几
            //如果要监听双击事件则数组长度为2，如果要监听3次连续点击事件则数组长度为3...
            long[] mHints = new long[3];//初始全部为0

            @Override
            public void onClick(View v) {
                //将mHints数组内的所有元素左移一个位置
                System.arraycopy(mHints, 1, mHints, 0, mHints.length - 1);
                //获得当前系统已经启动的时间
                mHints[mHints.length - 1] = SystemClock.uptimeMillis();
                if (SystemClock.uptimeMillis() - mHints[0] <= 500) {
                    MediaPlayer mpMediaPlayer = new MediaPlayer();
                    AssetManager am = getAssets();
                    try {
                        mpMediaPlayer.setDataSource(am.openFd("caiyu.mp3").getFileDescriptor());
                        mpMediaPlayer.prepare();
                        mpMediaPlayer.start();
                    } catch (IllegalArgumentException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IllegalStateException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.set_about:
//                LayoutInflater inflater = getLayoutInflater();
//                View layout = inflater.inflate(R.layout.dialog_show,
//                        (ViewGroup) findViewById(R.id.dialog));
//                AlertDialog.Builder dialog = new AlertDialog.Builder(this).setView(layout);
//                dialog.setTitle("应用信息");
//                dialog.show();
                AlertDialog alertDialog = new AlertDialog.Builder(SettingActivity.this).create();
                alertDialog.show();
                Window window = alertDialog.getWindow();
                window.setContentView(R.layout.dialog_show);
                TextView tv_title = (TextView) window.findViewById(R.id.tv_dialog_title);
                tv_title.setText("相关信息");
                TextView tv_message = (TextView) window.findViewById(R.id.tv_dialog_message);
                tv_message.setText("该项目使用资源来自网络，\n 如有侵权( ﾟ 3ﾟ)请联系本人");


//                DialogPlus dialog =  DialogPlus . newDialog( this )
//                        .setOnItemClickListener( new  OnItemClickListener () {
//                            @Override
//                            public  void  onItemClick ( DialogPlus  dialog , Object  item , View  view , int  position ) {
//                            }
//                        })
//                        .setHeader(R.layout.dialog_view)
//                .create();
//                dialog . show();
                break;
            case R.id.change_color:
                Random random = new Random();
                mColor = 0xff000000 | random.nextInt(0xffffff);
                mToolbar.setBackgroundColor(mColor);
                StatusBarUtil.setColor(SettingActivity.this, mColor, 0);

                Intent intent2 = new Intent(Constant.BroadcastAction);
                intent2.putExtra("color", mColor);
                sendBroadcast(intent2);

                break;

            case R.id.set_share:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain"); //"image/*"
                intent.putExtra(Intent.EXTRA_SUBJECT, "共享软件");
                intent.putExtra(Intent.EXTRA_TEXT, "我在使用这个APP,你也快来试试吧\nhttp://42.96.197.126:10086/app-debug.apk");
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, "选择分享类型"));
                break;
            case R.id.set_send:
                //发邮件
                Intent email = new Intent(android.content.Intent.ACTION_SEND);
                email.setType("plain/text");
                String emailSubject = "反馈标题";
                //设置邮件默认地址
                email.putExtra(android.content.Intent.EXTRA_EMAIL, "jp95@qq.com");
                //设置邮件默认标题
                email.putExtra(android.content.Intent.EXTRA_SUBJECT, emailSubject);
                //设置要默认发送的内容
                email.putExtra(android.content.Intent.EXTRA_TEXT, "反馈内容");
                email.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                //调用系统的邮件系统
                startActivity(Intent.createChooser(email, "请选择邮件发送软件"));

                break;

            case R.id.set_demo:
                LayoutInflater inflater = getLayoutInflater();
                final View layout = inflater.inflate(R.layout.dialog_edit,
                        (ViewGroup) findViewById(R.id.dialog_edit_info));
                final EditText editText = (EditText) layout.findViewById(R.id.edit_info_after);
                final TextView textView = (TextView) layout.findViewById(R.id.into_warnning);
                textView.setText("请输入密码");
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                AlertDialog.Builder dialog = new AlertDialog.Builder(this).setView(layout);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if ("1024".equals(editText.getText().toString())) {
                            Intent demo = new Intent(SettingActivity.this, WaterFallActivity.class);
                            startActivity(demo);
                            finish();
                        } else {
                            Toast.makeText(SettingActivity.this, "拒绝访问", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.show();

                break;

            case R.id.set_update:
                BmobUpdateAgent.forceUpdate(SettingActivity.this);

                BmobUpdateAgent.setUpdateListener(new BmobUpdateListener() {

                    @Override
                    public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
                        // TODO Auto-generated method stub
                        if (updateStatus == UpdateStatus.Yes) {//版本有更新

                        } else if (updateStatus == UpdateStatus.No) {
                            Toast.makeText(SettingActivity.this, "版本无更新", Toast.LENGTH_SHORT).show();
                        } else if (updateStatus == UpdateStatus.EmptyField) {//此提示只是提醒开发者关注那些必填项，测试成功后，无需对用户提示
                            Toast.makeText(SettingActivity.this, "请检查你AppVersion表的必填项，1、target_size（文件大小）是否填写；2、path或者android_url两者必填其中一项。", Toast.LENGTH_SHORT).show();
                        } else if (updateStatus == UpdateStatus.IGNORED) {
                            Toast.makeText(SettingActivity.this, "该版本已被忽略更新", Toast.LENGTH_SHORT).show();
                        } else if (updateStatus == UpdateStatus.ErrorSizeFormat) {
                            Toast.makeText(SettingActivity.this, "请检查target_size填写的格式，请使用file.length()方法获取apk大小。", Toast.LENGTH_SHORT).show();
                        } else if (updateStatus == UpdateStatus.TimeOut) {
                            Toast.makeText(SettingActivity.this, "查询出错或查询超时", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
        }
    }
}
