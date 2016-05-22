package com.jiang.deliciousfood.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jiang.deliciousfood.DetailedActivity;
import com.jiang.deliciousfood.R;
import com.jiang.deliciousfood.adapter.MenuAdapter;
import com.jiang.deliciousfood.bean.MenuParse;
import com.thinkland.sdk.android.DataCallBack;
import com.thinkland.sdk.android.JuheData;
import com.thinkland.sdk.android.Parameters;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2016/4/28.
 */
public class FragmentRandom extends Fragment {
    String id;
    String menu_name;
    int pn=0,rn=10;
    List<MenuParse.ResultBean.DataBean> list=new ArrayList<>();
    PullToRefreshListView listView;
    public CallBackData mCallBackData;
    AVLoadingIndicatorView mAVLoadingIndicatorView;


    public List<MenuParse.ResultBean.DataBean> jsondata(final String data){
        Gson gson=new Gson();
        MenuParse mp=gson.fromJson(data, MenuParse.class);
        if(!"200".equals(mp.getResultcode())){
            Toast.makeText(FragmentRandom.this.getActivity(), "请求数据失败\n(/ω・＼) \n再刷新一次", Toast.LENGTH_SHORT).show();
            return null;
        }
        MenuParse.ResultBean rb=mp.getResult();
        List<MenuParse.ResultBean.DataBean> menuList=new ArrayList<>();
        list=rb.getData();
        MenuAdapter adapter=new MenuAdapter(list,FragmentRandom.this.getActivity());
        //adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //MenuParse.ResultBean.DataBean dataBean=list.get(i);
                MenuParse.ResultBean.DataBean dataBean= (MenuParse.ResultBean.DataBean) adapterView.getItemAtPosition(i);
                Log.e("jiang", dataBean.getId() + dataBean.getTitle());
                //mCallBackData.sendData(dataBean);

                Intent intent=new Intent(FragmentRandom.this.getActivity(), DetailedActivity.class);
                intent.putExtra("foodid",dataBean.getId());
                startActivity(intent);

            }
        });

        return list;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_foodrandom,null);

        listView= (PullToRefreshListView) view.findViewById(R.id.recommend_listview);

        mAVLoadingIndicatorView= (AVLoadingIndicatorView) view.findViewById(R.id.jiazaizhong);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                Parameters params = new Parameters();
                Random random=new Random();
                int num=random.nextInt(359);
                int num2=random.nextInt(100);
                params.add("cid", num);
                params.add("dtype", "json");
                params.add("pn",num2);
                params.add("rn",rn);
                params.add("format", "1");
                JuheData.executeWithAPI(FragmentRandom.this.getActivity(), 46, "http://apis.juhe.cn/cook/index", JuheData.GET, params, new DataCallBack() {
                    @Override
                    public void onSuccess(int i, final String s) {
                        list = jsondata(s);
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

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });

        Parameters params = new Parameters();
        Random random=new Random();
        int num=random.nextInt(200);
        int num2=random.nextInt(80);
        params.add("cid", num);
        params.add("dtype", "json");
        params.add("pn",num2);
        params.add("rn",rn);
        params.add("format", "1");
        JuheData.executeWithAPI(FragmentRandom.this.getActivity(), 46, "http://apis.juhe.cn/cook/index", JuheData.GET, params, new DataCallBack() {
            @Override
            public void onSuccess(int i, final String s) {
                list = jsondata(s);
                mAVLoadingIndicatorView.setVisibility(View.GONE);
            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onFailure(int i, String error, Throwable throwable) {
                Log.d("jiang", error);
            }

        });

        return view;
    }

    public interface CallBackData{
        void sendData(MenuParse.ResultBean.DataBean menuData);
    }

}

