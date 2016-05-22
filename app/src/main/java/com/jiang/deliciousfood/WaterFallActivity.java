package com.jiang.deliciousfood;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.jaeger.library.StatusBarUtil;
import com.jiang.deliciousfood.adapter.PictureAdapter;
import com.jiang.deliciousfood.bean.PictureParse;
import com.jiang.deliciousfood.commen.SpacesItemDecoration;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

public class WaterFallActivity extends AppCompatActivity {
    Toolbar mToolbar;
    RecyclerView mRecyclerView;
    SwipeRefreshLayout mSwipeRefreshWidget;
    int i=2;

    private List<PictureParse.ResultsBean> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_fall);
        mToolbar = (Toolbar) findViewById(R.id.waterfall_toolbar);
        mToolbar.setTitle("接口测试");
        mToolbar.setNavigationIcon(R.mipmap.ic_chevron_left_white_24dp);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setBackgroundColor(getResources().getColor(R.color.colorBlue));
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorBlue), 0);
        setSupportActionBar(mToolbar);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(WaterFallActivity.this, MainActivity.class);
//                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
                WaterFallActivity.this.finish();
            }
        });

        mRecyclerView= (RecyclerView) findViewById(R.id.recycler);
        mSwipeRefreshWidget = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_widget);

        mSwipeRefreshWidget.setColorSchemeResources(R.color.colorAccent);

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView,
                                             int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }
        });

        mSwipeRefreshWidget.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData(i+"");
                i++;
            }
        });
        initData("1");
    }

    private void initData(String i) {
        RequestParams requestParams=new RequestParams("http://gank.io/api/data/%E7%A6%8F%E5%88%A9/10/"+i);
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("jiang", result);
                Gson gson = new Gson();
                PictureParse pp = gson.fromJson(result, PictureParse.class);
                list = pp.getResults();
                StringBuffer sb=new StringBuffer();
                for (int i = 0; i < list.size(); i++) {
                    sb.append(list.get(i).getWho());
                }
                Log.e("jiang", sb.toString());



                //mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                mRecyclerView.setLayoutManager(new GridLayoutManager(WaterFallActivity.this,2));
                //设置adapter

                PictureAdapter adapter = new PictureAdapter(list,WaterFallActivity.this);
                mRecyclerView.setAdapter(adapter);
                mSwipeRefreshWidget.setRefreshing(false);
                //设置item之间的间隔
//                SpacesItemDecoration decoration = new SpacesItemDecoration(16);
//                mRecyclerView.addItemDecoration(decoration);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

}
