package com.jiang.deliciousfood;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.jaeger.library.StatusBarUtil;
import com.jiang.deliciousfood.bean.MyUser;
import com.jiang.deliciousfood.custom.CustomRegist;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Android4.4 版本以上，返回的URI格式会和以前版本不同
 * 4.4之前  content://media/external/image/media/102
 *          content://com.android.providers.media.documents/document/image:3951
 */
public class RegisterActivity extends AppCompatActivity {
    private TextView mTextView;
    private ImageView mImageView;
    int REQUEST_CODE_PICK_IMAGE=0x101;
    int CAMERA_REQUEST_CODE=0x102;
    int CHOOSE_BIG_PICTURE=0x103;
    private CustomRegist cr_username,cr_password,cr_email,cr_phone;
    private Button mButton;
    BmobFile bmobFile;

    static final String IMAGE_FILE_LOCATION = "file:///sdcard/Meishi/temp.jpg";//temp file
    Uri imageUri = Uri.parse(IMAGE_FILE_LOCATION);//The Uri to store the big bitmap


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        //有冲突，设置之后注册的宽度会变宽
        //StatusBarUtil.setTransparent(this);

        mImageView = (ImageView) findViewById(R.id.head_image);
        mTextView = (TextView) findViewById(R.id.upload_head);
        mButton= (Button) findViewById(R.id.regist_btn);

        cr_username= (CustomRegist) findViewById(R.id.name_regist);
        cr_password= (CustomRegist) findViewById(R.id.pwd_regist);
        cr_email= (CustomRegist) findViewById(R.id.email_regist);
        cr_phone= (CustomRegist) findViewById(R.id.phone_regist);


        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
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

            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         if(requestCode == CAMERA_REQUEST_CODE){
            if(imageUri != null){
                String path="/sdcard/Meishi/temp.jpg";
                uploadImage(RegisterActivity.this, path);



            }
        }else if(requestCode==CHOOSE_BIG_PICTURE){
            if(imageUri != null){
                String path="/sdcard/Meishi/temp.jpg";
                uploadImage(RegisterActivity.this, path);





            }
        }
    }

    public void uploadImage(final Context context, String picPath) {
        bmobFile = new BmobFile(new File(picPath));
        bmobFile.uploadblock(context, new UploadFileListener() {

            @Override
            public void onSuccess() {
                //bmobFile.getFileUrl(context)--返回的上传文件的完整地址
                //Toast.makeText(context, "上传文件成功:" + bmobFile.getFileUrl(context), Toast.LENGTH_LONG).show();
                Log.e("jiang", bmobFile.getFileUrl(context));
                Bitmap bitmap = decodeUriAsBitmap(imageUri);//decode bitmap

                mImageView.setImageBitmap(bitmap);
            }

            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
            }

            @Override
            public void onFailure(int code, String msg) {
                //Toast.makeText(context, "上传失败" + msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void register(){
        final MyUser ub=new MyUser();
        ub.setUsername(cr_username.getRegistText());
        ub.setPassword(cr_password.getRegistText());
        ub.setEmail(cr_email.getRegistText());
        ub.setMobilePhoneNumber(cr_phone.getRegistText());
        ub.setHeadImage(bmobFile);

        ub.signUp(this, new SaveListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_LONG).show();
                login(cr_username.getRegistText().toString(),cr_password.getRegistText().toString());
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(int i, String s) {

                if (i == 301) {
                    Toast.makeText(RegisterActivity.this, "请输入正确的邮箱/手机格式", Toast.LENGTH_LONG).show();
                }
            }
        });
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

    private String img_path(Uri uri){
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor actualimagecursor = managedQuery(uri, proj, null, null, null);
        int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        actualimagecursor.moveToFirst();
        String img_path = actualimagecursor.getString(actual_image_column_index);
        return img_path;
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

    public void login(String name,String pwd) {
        MyUser bu2 = new MyUser();
        bu2.setUsername(name);
        bu2.setPassword(pwd);
        bu2.login(RegisterActivity.this, new SaveListener() {
            @Override
            public void onSuccess() {
                //Toast.makeText(RegisterActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                BmobUser user = BmobUser.getCurrentUser(RegisterActivity.this);
                if (user != null) {

                }
                //通过BmobUser user = BmobUser.getCurrentUser(context)获取登录成功后的本地用户信息
                //如果是自定义用户对象MyUser，可通过MyUser user = BmobUser.getCurrentUser(context,MyUser.class)获取自定义用户信息
            }

            @Override
            public void onFailure(int code, String msg) {

            }
        });
    }
}
