package com.jiang.deliciousfood;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jaeger.library.StatusBarUtil;
import com.jiang.deliciousfood.bean.JsonParse;
import com.jiang.deliciousfood.bean.MenuParse;
import com.jiang.deliciousfood.bean.MyUser;
import com.jiang.deliciousfood.commen.AppManager;
import com.jiang.deliciousfood.commen.Constant;
import com.jiang.deliciousfood.fragment.FragmentDetails;
import com.jiang.deliciousfood.fragment.FragmentExpandable;
import com.jiang.deliciousfood.fragment.FragmentMenu;
import com.jiang.deliciousfood.fragment.FragmentRandom;
import com.meg7.widget.CustomShapeImageView;

import org.xutils.x;

import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import me.drakeet.materialdialog.MaterialDialog;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * 双击退出函数
     */
    private static Boolean isExit = false;
    CustomShapeImageView mCustomShapeImageView;
    FragmentExpandable mFragmentExpandable = new FragmentExpandable();
    FragmentManager mFragmentManager = getFragmentManager();
    TextView mTextViewName;
    LoginActivity mLoginActivity = new LoginActivity();
    FragmentMenu mFragmentMenu = new FragmentMenu();
    FragmentTransaction transaction = mFragmentManager.beginTransaction();
    FragmentRandom mFragmentRandom = new FragmentRandom();
    FragmentDetails mFragmentDetails = new FragmentDetails();
    SearchView searchView;
    Toolbar toolbar;
    ColorBroadcast broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        StatusBarUtil.setColorForDrawerLayout(this, (DrawerLayout) findViewById(R.id.drawer_layout), 0, 0);
        AppManager.getAppManager().addActivity(this);


        broadcastReceiver = new ColorBroadcast();
        IntentFilter filter = new IntentFilter(Constant.BroadcastAction);
        //注册广播接收器
        registerReceiver(broadcastReceiver, filter);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "功能尚未实现!", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
        fab.setVisibility(View.INVISIBLE);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        final MaterialDialog mMaterialDialog = new MaterialDialog(this);
        mMaterialDialog.setTitle("提示信息")
                .setMessage("当前应用会产生流量消耗费用，具体费用请和当地运行商协商")
                .setPositiveButton("我知道了", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                        transaction.add(R.id.content_parent, mFragmentRandom);

                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                })
                .setNegativeButton("退出", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppManager.getAppManager().finishAllActivity();
                    }
                });

        SharedPreferences sharedPreferences = this.getSharedPreferences("share", MODE_PRIVATE);
        boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (isFirstRun) {
            mMaterialDialog.show();
            editor.putBoolean("isFirstRun", false);
            editor.commit();
        } else {
            transaction.add(R.id.content_parent, mFragmentRandom);
            transaction.addToBackStack(null);
            transaction.commit();
        }


        //transaction.add(R.id.content_parent, mFragmentRecommend);
        mFragmentRandom.mCallBackData = new FragmentRandom.CallBackData() {
            @Override
            public void sendData(MenuParse.ResultBean.DataBean menuData) {
                mFragmentDetails.setAll(menuData);
                FragmentTransaction transaction = mFragmentManager.beginTransaction();
                transaction
                        .add(R.id.content_parent, mFragmentDetails)
                        .attach(mFragmentDetails)
                        .detach(mFragmentRandom)
                        .addToBackStack(null);

                transaction.commit();
            }
        };


        //获取用户名显示在左侧
        MyUser userInfo = BmobUser.getCurrentUser(MainActivity.this, MyUser.class);
        View headerView = navigationView.getHeaderView(0);
        mTextViewName = (TextView) headerView.findViewById(R.id.user_name);
        ImageView imageView = (ImageView) headerView.findViewById(R.id.head_image_right);
        Glide.with(this).load(R.drawable.chigua).into(imageView);

//        AssetManager mgr=getAssets();//得到AssetManager
//        Typeface tf=Typeface.createFromAsset(mgr, "fonts/ziti.ttc");//根据路径得到Typeface
//        mTextViewName.setTypeface(tf);//设置字体

        mTextViewName.setText(userInfo.getUsername());
        mTextViewName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });

        mCustomShapeImageView = (CustomShapeImageView) headerView.findViewById(R.id.customimageview);
        if (userInfo != null) {
            BmobFile bmobFile = userInfo.getHeadImage();
            if (bmobFile != null) {
                String url = bmobFile.getFileUrl(this);
                {
                    if (url != null) {
                        x.image().bind(mCustomShapeImageView, userInfo.getHeadImage().getFileUrl(this));
                    }
                }
            }
        } else {
            mCustomShapeImageView.setImageResource(R.mipmap.register_head);
        }

        mCustomShapeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UserInfoActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);//在菜单中找到对应控件的item
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

//            final FragmentManager manager=getFragmentManager();
//            FragmentRandom fragmentRandom=new FragmentRandom();
//            FragmentTransaction transaction=manager.beginTransaction();
//            transaction.replace(R.id.content_parent, fragmentRandom);
//            transaction.addToBackStack(null);
//
//            transaction.commit();
//
//
//            fragmentRandom.mCallBackData=new FragmentRandom.CallBackData() {
//                @Override
//                public void sendData(MenuParse.ResultBean.DataBean menuData) {
//                    FragmentDetails fragmentDetails=new FragmentDetails();
//                    fragmentDetails.setAll(menuData);
//                    FragmentTransaction transaction=manager.beginTransaction();
//                    transaction
//                            .replace(R.id.content_parent, fragmentDetails)
////                            .attach(mFragmentDetails)
////                            .detach(mFragmentRandom)
//                            .addToBackStack(null);
//
//                    transaction.commit();
//                }
//            };


            final FragmentRandom fragmentRandom = new FragmentRandom();
            final FragmentDetails fragmentDetails = new FragmentDetails();
            final FragmentManager manager = getFragmentManager();

            final FragmentTransaction transaction = manager.beginTransaction();

            transaction.replace(R.id.content_parent, fragmentRandom)
                    .attach(fragmentRandom).detach(fragmentDetails);
            transaction.addToBackStack(null);
            transaction.commit();
//            fragmentRandom.mCallBackData=new FragmentRandom.CallBackData() {
//                @Override
//                public void sendData(MenuParse.ResultBean.DataBean menuData) {
//
//                    fragmentDetails.setAll(menuData);
//
//                    FragmentTransaction newtransaction=manager.beginTransaction();
//                    newtransaction
//                            .add(R.id.content_parent, fragmentDetails)
//                            .attach(fragmentDetails)
//                            .detach(fragmentRandom)
//                            .addToBackStack(null);
//
//                    newtransaction.commit();
//                    if (manager.getBackStackEntryCount() < 0) {
//                        manager.popBackStack();
//                    }
//                }
//            };


        } else if (id == R.id.nav_gallery) {
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            FragmentExpandable expandable = new FragmentExpandable();
            transaction.replace(R.id.content_parent, expandable);
            transaction.addToBackStack(null);
            transaction.commit();

            expandable.mCallBack = new FragmentExpandable.CallBack() {
                @Override
                public void sendData(JsonParse.ResultBean.ListBean list) {
                    mFragmentMenu.setAll(list);
                    FragmentTransaction transaction = mFragmentManager.beginTransaction();
                    transaction
                            .replace(R.id.content_parent, mFragmentMenu)
//                            .attach(mFragmentMenu)
//                            .detach(mFragmentExpandable)
                            .addToBackStack(null);

                    transaction.commit();
                }
            };

            //开始跳转到最后一个fragment，后来改为跳转到activity，这段暂时没用
            mFragmentMenu.mCallBackData = new FragmentMenu.CallBackData() {
                @Override
                public void sendData(MenuParse.ResultBean.DataBean menuData) {
                    mFragmentDetails.setAll(menuData);
                    FragmentTransaction transaction = mFragmentManager.beginTransaction();
                    transaction
                            .replace(R.id.content_parent, mFragmentDetails)
                            .addToBackStack(null);

                    transaction.commit();
                }
            };

        } else if (id == R.id.nav_shoucang) {
            Intent intent = new Intent(MainActivity.this, CollectionActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_waimai) {
            Intent intent = new Intent(this, H5PayActivity.class);
            Bundle extras = new Bundle();
            String url = "http://m.meituan.com";
            extras.putString("url", url);
            intent.putExtras(extras);
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            //intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click(); //调用双击退出函数
        }
        return false;
    }

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            finish();
            System.exit(0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    class ColorBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int color = intent.getIntExtra("color", 0xff6b6b);
            StatusBarUtil.setColorForDrawerLayout(MainActivity.this, (DrawerLayout) findViewById(R.id.drawer_layout), color, 255);
            toolbar.setBackgroundColor(color);

        }
    }
}
