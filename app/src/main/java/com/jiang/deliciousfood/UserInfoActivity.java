package com.jiang.deliciousfood;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jiang.deliciousfood.bean.MyUser;
import com.jiang.deliciousfood.commen.AppManager;
import com.jiang.deliciousfood.custom.CustomRegist;
import com.jiang.deliciousfood.custom.RoundImageView;
import com.jiang.deliciousfood.custom.WhewView;

import org.xutils.x;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.annotation.Annotation;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.EmailVerifyListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener{
    View view;
    Toolbar mToolbar;
    WebView mWebView;
    private TextView mTextView1, mTextView2, mTextView3, mTextView4,mTextView5,mTextView6;
    ImageView mImageView;


    private Button mButton,mButtonEditPwd;

    int CAMERA_REQUEST_CODE=0x102;
    int CHOOSE_BIG_PICTURE=0x103;
    BmobFile bmobFile;

    static final String IMAGE_FILE_LOCATION = "file:///sdcard/Meishi/temp.jpg";//temp file
    Uri imageUri = Uri.parse(IMAGE_FILE_LOCATION);//The Uri to store the big bitmap

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_user_info);
        AppManager.getAppManager().addActivity(this);

        initView();
        DisplayMetrics dm = getResources().getDisplayMetrics();
        final int screenWidth = dm.widthPixels;
        final int screenHeight = dm.heightPixels - 50;

        mToolbar = (Toolbar) findViewById(R.id.infotoolbar);
        mToolbar.setNavigationIcon(R.mipmap.ic_keyboard_arrow_left_white_36dp);
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(UserInfoActivity.this, MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//                startActivity(intent);
                UserInfoActivity.this.finish();
            }
        });

        mWebView = (WebView) findViewById(R.id.info_web);
        //设置WebView属性，能够执行Javascript脚本
        mWebView.getSettings().setJavaScriptEnabled(true);
        //加载需要显示的网页
        mWebView.loadUrl("file:///android_asset/little-halloween/index.html");
        //webview.loadUrl("http://trex-game.skipser.com/");
        //设置Web视图

        mWebView.setWebViewClient(new MyWebViewClient());

        class MyWebChromeClient extends WebChromeClient {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                result.confirm();
                return true;
            }
        }


        MyUser userInfo = BmobUser.getCurrentUser(this, MyUser.class);
        //userInfo.setNick("昵称one");
        BmobFile bmobFile = userInfo.getHeadImage();
        if (bmobFile != null) {
            String url = bmobFile.getFileUrl(this);
            if (url != null) {
                x.image().bind(mImageView, url);
            }
        }
        //String url=bmobFile.getFileUrl(this);
        if(userInfo.getNick()==null){
            mTextView1.setText("我是昵称");
        }else{
            mTextView1.setText(userInfo.getNick());
        }

        mTextView2.setText(userInfo.getObjectId());
        mTextView3.setText(userInfo.getMobilePhoneNumber());
        mTextView4.setText(userInfo.getEmail());
        if(userInfo.getMobilePhoneNumberVerified()==null||userInfo.getMobilePhoneNumberVerified()==false){
            mTextView5.setText("未验证");
        }else if(userInfo.getMobilePhoneNumberVerified()==true){
            mTextView5.setText("已验证");
        }
        if(userInfo.getEmailVerified()==null||userInfo.getEmailVerified()==false) {
            mTextView6.setText("未验证");
        } else if(userInfo.getEmailVerified()==true){
            mTextView6.setText("已验证");
        }

        //

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BmobUser.logOut(UserInfoActivity.this);
//                Intent intent = new Intent(UserInfoActivity.this, LoginActivity.class);
//                //intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
//                finish();
//                ActivityManager manager = (ActivityManager)getSystemService(UserInfoActivity.this.ACTIVITY_SERVICE); //获取应用程序管理器
//                manager.killBackgroundProcesses(getPackageName()); //强制结束当前应用程序
                AppManager.getAppManager().finishAllActivity();

            }
        });


    }

    private void initView() {
        mTextView1 = (TextView) findViewById(R.id.user_info_nickname);
        mTextView2 = (TextView) findViewById(R.id.user_info_mid);
        mTextView3 = (TextView) findViewById(R.id.user_info_phone);
        mTextView4 = (TextView) findViewById(R.id.user_info_email);
        mTextView5=(TextView) findViewById(R.id.user_info_phone_state);
        mTextView6=(TextView) findViewById(R.id.user_info_email_state);
        mImageView = (ImageView) findViewById(R.id.user_info_head);
        mButtonEditPwd= (Button) findViewById(R.id.edit_pwd);

        mTextView1.setOnClickListener(this);
        mTextView2.setOnClickListener(this);
        mTextView3.setOnClickListener(this);
        mTextView4.setOnClickListener(this);
        mTextView5.setOnClickListener(this);
        mTextView6.setOnClickListener(this);
        mImageView.setOnClickListener(this);
        mButtonEditPwd.setOnClickListener(this);


        mButton = (Button) findViewById(R.id.logout);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.user_info_head:

                AlertDialog.Builder builder = new AlertDialog.Builder(UserInfoActivity.this);
                builder.setTitle("请选择上传方式")
                        .setIcon(R.mipmap.header);

                final String[] mode = {"拍照上传", "从图库上传"};

                builder.setItems(mode, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 1) {
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
                            intent.setType("image/*");
                            intent.putExtra("crop", "true");
                            intent.putExtra("aspectX", 2);
                            intent.putExtra("aspectY", 2);
                            intent.putExtra("outputX", 600);
                            intent.putExtra("outputY", 600);
                            intent.putExtra("scale", true);
                            intent.putExtra("return-data", false);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                            intent.putExtra("noFaceDetection", true); // no face detection
                            startActivityForResult(intent, CHOOSE_BIG_PICTURE);
                        }else if(i==0){
//                            Intent cameraIntent = new Intent(
//                                    "android.media.action.IMAGE_CAPTURE");//拍照
//                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, getImageUri());
//                            //cameraIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
//                            RegisterActivity.this.startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);

                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//action is capture
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            cropImageUri(imageUri, 600, 600, CAMERA_REQUEST_CODE);
                            startActivityForResult(intent, CAMERA_REQUEST_CODE);//or TAKE_SMALL_PICTURE
                        }
                    }
                });

                builder.show();



                break;
            case R.id.user_info_nickname:
                showEditDialog(R.id.user_info_nickname);
                break;
            case R.id.user_info_phone:
                showEditDialog(R.id.user_info_phone);
                break;
            case R.id.user_info_email:
                showEditDialog(R.id.user_info_email);
                break;
            case R.id.user_info_phone_state:
                //验证手机
                break;
            case R.id.user_info_email_state:
                //验证邮箱
                final String email = mTextView4.getText().toString();
                AlertDialog.Builder builder2 = new AlertDialog.Builder(UserInfoActivity.this);
                builder2.setTitle("确认选择").setMessage("点击确定发送邮件到" + email);

                builder2.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(UserInfoActivity.this, "请求验证邮件成功，请到" + email + "邮箱中进行激活", Toast.LENGTH_SHORT).show();
                        BmobUser.requestEmailVerify(UserInfoActivity.this, email, new EmailVerifyListener() {
                            @Override
                            public void onSuccess() {
                                // TODO Auto-generated method stub
                                MyUser userInfo = BmobUser.getCurrentUser(UserInfoActivity.this, MyUser.class);
                                if (userInfo.getEmailVerified() == true) {
                                    mTextView6.setText("已验证");
                                }
                            }
                            @Override
                            public void onFailure(int code, String e) {
                                // TODO Auto-generated method stub

                            }
                        });

                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder2.show();

                break;
            case R.id.edit_pwd:
                showEditDialog(R.id.edit_pwd);
                break;
        }
    }

    public void showEditDialog(final int dataId){
        LayoutInflater inflater = getLayoutInflater();
        final View layout = inflater.inflate(R.layout.dialog_edit,
                (ViewGroup) findViewById(R.id.dialog_edit_info));
        AlertDialog.Builder dialog = new AlertDialog.Builder(this).setView(layout);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EditText editText= (EditText) layout.findViewById(R.id.edit_info_after);

                upData(dataId, editText.getText().toString());
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();
    }

    public void upData(int id,String info) {
        final MyUser userInfo2 = BmobUser.getCurrentUser(this, MyUser.class);
        MyUser newUser = new MyUser();
        if(id==R.id.user_info_nickname){
            mTextView1.setText(info);
            newUser.setNick(info);
        }else if(id==R.id.user_info_phone){
            mTextView3.setText(info);
            newUser.setMobilePhoneNumber(info);

        }else if(id==R.id.user_info_email){
            mTextView4.setText(info);
            newUser.setEmail(info);
        }else if(id==R.id.edit_pwd){
            newUser.setPassword(info);
        }

        newUser.update(this, userInfo2.getObjectId(), new UpdateListener() {
            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                Toast.makeText(UserInfoActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
                MyUser userInfo3 = BmobUser.getCurrentUser(UserInfoActivity.this, MyUser.class);
                mTextView1.setText(userInfo3.getNick());
                mTextView3.setText(userInfo3.getMobilePhoneNumber());
                mTextView4.setText(userInfo3.getEmail());

            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                // toast("更新用户信息失败:" + msg);
                Toast.makeText(UserInfoActivity.this, "更新失败"+code+"-----"+msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Web视图
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            showDialog(2);
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    private void cropImageUri(Uri uri, int outputX, int outputY, int requestCode){
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 2);
        intent.putExtra("aspectY", 2);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAMERA_REQUEST_CODE){
            if(imageUri != null){

                String path="/sdcard/Meishi/temp.jpg";
                uploadImage(UserInfoActivity.this, path);

            }
        }else if(requestCode==CHOOSE_BIG_PICTURE){
            if(imageUri != null){


                String path="/sdcard/Meishi/temp.jpg";
                uploadImage(UserInfoActivity.this, path);

            }
        }
    }

    private Bitmap decodeUriAsBitmap(Uri uri){
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    public void uploadImage(final Context context, String picPath) {
        bmobFile = new BmobFile(new File(picPath));
        bmobFile.uploadblock(context, new UploadFileListener() {

            @Override
            public void onSuccess() {
                //bmobFile.getFileUrl(context)--返回的上传文件的完整地址
                //Toast.makeText(context, "上传文件成功:" + bmobFile.getFileUrl(context), Toast.LENGTH_LONG).show();
                MyUser userInfo = BmobUser.getCurrentUser(UserInfoActivity.this, MyUser.class);

                x.image().bind(mImageView, bmobFile.getFileUrl(context));
                Bitmap bitmap = decodeUriAsBitmap(imageUri);//decode bitmap

                mImageView.setImageBitmap(bitmap);
                userInfo.setHeadImage(bmobFile);
                Log.e("jiang", userInfo.getHeadImage().getUrl()+userInfo.getUsername());
                Log.e("jiang", bmobFile.getFileUrl(context));

                MyUser newUser = new MyUser();
                newUser.setHeadImage(bmobFile);

                newUser.update(UserInfoActivity.this, userInfo.getObjectId(), new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        // TODO Auto-generated method stub
                        Toast.makeText(UserInfoActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        // TODO Auto-generated method stub
                        // toast("更新用户信息失败:" + msg);
                    }
                });
            }

            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
            }

            @Override
            public void onFailure(int code, String msg) {
                Toast.makeText(context, "上传失败" + msg, Toast.LENGTH_LONG).show();
                Log.e("jiang",msg);
            }
        });
    }
}
