package com.jiang.deliciousfood;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jaeger.library.StatusBarUtil;
import com.jiang.deliciousfood.adapter.MenuAdapter;
import com.jiang.deliciousfood.bean.MenuParse;
import com.thinkland.sdk.android.DataCallBack;
import com.thinkland.sdk.android.JuheData;
import com.thinkland.sdk.android.Parameters;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

public class SearchableActivity extends AppCompatActivity {
    private TextView mTextView;
    Toolbar mToolbar;
    PullToRefreshListView listView;
    FragmentManager manager=getFragmentManager();
    FragmentTransaction transaction=manager.beginTransaction();
    AVLoadingIndicatorView mAVLoadingIndicatorView;

    List<MenuParse.ResultBean.DataBean> list=new ArrayList<>();

    String key;
    int pn=0,rn=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorBlue), 0);
        Intent intent=getIntent();
        key=intent.getStringExtra(SearchManager.QUERY);


        mToolbar= (Toolbar) findViewById(R.id.search_toolbar);
        mAVLoadingIndicatorView= (AVLoadingIndicatorView) findViewById(R.id.jiazaizhong);
        listView= (PullToRefreshListView) findViewById(R.id.search_list);
        mToolbar.setTitle("");
        mToolbar.setNavigationIcon(R.mipmap.ic_keyboard_backspace_white_36dp);
        setSupportActionBar(mToolbar);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(SearchableActivity.this, MainActivity.class);
//                startActivity(intent);
                SearchableActivity.this.finish();
            }
        });

        Parameters params = new Parameters();

        params.add("menu", key);
        params.add("dtype", "json");
        params.add("pn", "0");
        params.add("rn", "10");
        JuheData.executeWithAPI(SearchableActivity.this, 46, "http://apis.juhe.cn/cook/query.php", JuheData.GET, params, new DataCallBack() {
            @Override
            public void onSuccess(int i, final String s) {
                list = jsondata(s);
            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onFailure(int i, String error, Throwable throwable) {
                Log.d("jiang", error);
            }

        });
    }

    public List<MenuParse.ResultBean.DataBean> jsondata(String data){
        Gson gson=new Gson();
        MenuParse mp=gson.fromJson(data, MenuParse.class);
        if("202".equals(mp.getResultcode())){
            Toast.makeText(SearchableActivity.this, "搜索结果不存在，请重新搜索", Toast.LENGTH_SHORT).show();
            return null;
        }
        MenuParse.ResultBean rb=mp.getResult();
        List<MenuParse.ResultBean.DataBean> menuList=new ArrayList<>();
        mAVLoadingIndicatorView.setVisibility(View.GONE);
        list=rb.getData();
        final MenuAdapter adapter=new MenuAdapter(list,SearchableActivity.this);
        adapter.notifyDataSetChanged();
        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                pn += 10;
                listView.getLoadingLayoutProxy().setPullLabel("上拉加载更多");
                Parameters params = new Parameters();
                Log.e("jiang", "执行了第一行");
                params.add("menu", key);
                Log.e("jiang", "执行了此行");
                params.add("dtype", "json");
                params.add("pn", pn);
                params.add("rn", "10");
                JuheData.executeWithAPI(SearchableActivity.this, 46, "http://apis.juhe.cn/cook/query.php", JuheData.GET, params, new DataCallBack() {
                    @Override
                    public void onSuccess(int i, final String s) {
                        list = jsondata2(s);
                        listView.onRefreshComplete();
                    }

                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void onFailure(int i, String error, Throwable throwable) {
                        Log.d("jiang", error);
                    }

                });

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MenuParse.ResultBean.DataBean dataBean= (MenuParse.ResultBean.DataBean) adapterView.getItemAtPosition(i);
                //Toast.makeText(SearchableActivity.this, ""+ dataBean.getTitle(), Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(SearchableActivity.this,DetailedActivity.class);
                intent.putExtra("foodid",dataBean.getId());
                startActivity(intent);
            }
        });
        listView.setAdapter(adapter);

        return list;
    }

    public List<MenuParse.ResultBean.DataBean> jsondata2(String data){
        Gson gson=new Gson();
        MenuParse mp=gson.fromJson(data, MenuParse.class);
        MenuParse.ResultBean rb=mp.getResult();
        List<MenuParse.ResultBean.DataBean> menuList=new ArrayList<>();
        menuList=rb.getData();
        list.addAll(menuList);
        MenuAdapter adapter=new MenuAdapter(list,SearchableActivity.this);
        adapter.notifyDataSetChanged();
        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        adapter.notifyDataSetChanged();
        //listView.setAdapter(adapter);

        return list;
    }

}
