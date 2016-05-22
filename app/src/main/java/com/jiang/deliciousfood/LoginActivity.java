package com.jiang.deliciousfood;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.jaeger.library.StatusBarUtil;
import com.jiang.deliciousfood.bean.MyUser;
import com.jiang.deliciousfood.commen.AppManager;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.ResetPasswordByEmailListener;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends AppCompatActivity {
    TextInputLayout username,password;
    Button mButton;
    Toolbar mToolbar;
    BmobUser user;
    TextView register,zhaoHui;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_login);
        StatusBarUtil.setTranslucent(this, 0);
        AppManager.getAppManager().addActivity(this);

        register= (TextView) findViewById(R.id.register_link);
        zhaoHui= (TextView) findViewById(R.id.register_zhaohui);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
            }
        });

        zhaoHui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                final View layout = inflater.inflate(R.layout.dialog_edit,
                        (ViewGroup) findViewById(R.id.dialog_edit_info));
                final EditText editText= (EditText) layout.findViewById(R.id.edit_info_after);
                final TextView textView= (TextView) layout.findViewById(R.id.into_warnning);
                textView.setText("请输入注册邮箱");
                AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this).setView(layout);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final String email = editText.getText().toString();
                        BmobUser.resetPasswordByEmail(LoginActivity.this, email, new ResetPasswordByEmailListener() {
                            @Override
                            public void onSuccess() {
                                // TODO Auto-generated method stub
                                Toast.makeText(LoginActivity.this, "重置密码请求成功，请到" + email + "邮箱进行密码重置操作", Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onFailure(int code, String e) {
                                // TODO Auto-generated method stub
                                Toast.makeText(LoginActivity.this, "请求失败"+e, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.show();
            }
        });


        //判断是否有用户，有的话则直接跳进主activity
        user = BmobUser.getCurrentUser(LoginActivity.this);
        if(user!=null){
            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
            //intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
        }


        username= (TextInputLayout) findViewById(R.id.username);
        password= (TextInputLayout) findViewById(R.id.password);
        mButton= (Button) findViewById(R.id.login_btn);

        username.setHint("用户名");
        password.setHint("密码");

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //register();
                login();
            }
        });
    }


    public void login() {
        MyUser bu2 = new MyUser();
        bu2.setUsername(username.getEditText().getText().toString());
        bu2.setPassword(password.getEditText().getText().toString());
        bu2.login(LoginActivity.this, new SaveListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_LONG).show();

                user = BmobUser.getCurrentUser(LoginActivity.this);
                if(user!=null){
                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                    LoginActivity.this.finish();
                }
                //通过BmobUser user = BmobUser.getCurrentUser(context)获取登录成功后的本地用户信息
                //如果是自定义用户对象MyUser，可通过MyUser user = BmobUser.getCurrentUser(context,MyUser.class)获取自定义用户信息
            }

            @Override
            public void onFailure(int code, String msg) {
                Toast.makeText(LoginActivity.this, "登录失败,请确认账号和密码", Toast.LENGTH_LONG).show();
                //Log.e("jiang",code+msg);
            }
        });
    }
}
