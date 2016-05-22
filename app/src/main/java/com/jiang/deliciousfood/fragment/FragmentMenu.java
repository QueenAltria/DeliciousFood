package com.jiang.deliciousfood.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jiang.deliciousfood.DetailedActivity;
import com.jiang.deliciousfood.R;
import com.jiang.deliciousfood.adapter.MenuAdapter;
import com.jiang.deliciousfood.bean.JsonParse;
import com.jiang.deliciousfood.bean.MenuParse;
import com.thinkland.sdk.android.DataCallBack;
import com.thinkland.sdk.android.JuheData;
import com.thinkland.sdk.android.Parameters;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/22.
 */
public class FragmentMenu extends Fragment {
    String id;
    String menu_name;
    int pn=0,rn=10;
    int cid;
    List<MenuParse.ResultBean.DataBean> list=new ArrayList<>();
    TextView mTextView;
    PullToRefreshListView listView;
    AVLoadingIndicatorView mAVLoadingIndicatorView;
    public CallBackData mCallBackData;

    public void setAll(JsonParse.ResultBean.ListBean menu){
        id=menu.getId();
        cid=Integer.parseInt(id);
        menu_name=menu.getName();

    }

    public List<MenuParse.ResultBean.DataBean> jsondata(String data){
        Gson gson=new Gson();
        MenuParse mp=gson.fromJson(data, MenuParse.class);

        mTextView.setText(menu_name + "\t\t总共" + mp.getResult().getTotalNum()+"种");

        MenuParse.ResultBean rb=mp.getResult();
        List<MenuParse.ResultBean.DataBean> menuList=new ArrayList<>();
        list=rb.getData();
        mAVLoadingIndicatorView.setVisibility(View.GONE);
        MenuAdapter adapter=new MenuAdapter(list,FragmentMenu.this.getActivity());
        adapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MenuParse.ResultBean.DataBean dataBean= (MenuParse.ResultBean.DataBean) adapterView.getItemAtPosition(i);
                Log.e("jiang", dataBean.getId() + dataBean.getTitle());
                //mCallBackData.sendData(dataBean);

                Intent intent=new Intent(FragmentMenu.this.getActivity(), DetailedActivity.class);
                intent.putExtra("foodid",dataBean.getId());
                startActivity(intent);
            }
        });
        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                pn+=10;
                listView.getLoadingLayoutProxy().setPullLabel("上拉加载更多");
                Parameters params = new Parameters();

                mTextView.setText(menu_name);
                params.add("cid", id);
                params.add("dtype", "json");
                params.add("pn",pn);
                params.add("rn","10");
                params.add("format", "1");
                JuheData.executeWithAPI(FragmentMenu.this.getActivity(), 46, "http://apis.juhe.cn/cook/index", JuheData.GET, params, new DataCallBack() {
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
        MenuAdapter adapter=new MenuAdapter(list,FragmentMenu.this.getActivity());
        adapter.notifyDataSetChanged();
        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        adapter.notifyDataSetChanged();
        //listView.setAdapter(adapter);

        return list;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_foodmenu,null);

        listView= (PullToRefreshListView) view.findViewById(R.id.menu_listview);
        mTextView= (TextView) view.findViewById(R.id.demo);
        mAVLoadingIndicatorView= (AVLoadingIndicatorView) view.findViewById(R.id.jiazaizhong);


        Parameters params = new Parameters();

//        AssetManager mgr=getResources().getAssets();//得到AssetManager
//        Typeface tf=Typeface.createFromAsset(mgr, "fonts/ziti.ttc");//根据路径得到Typeface
//        mTextView.setTypeface(tf);//设置字体

        params.add("cid", id);
        params.add("dtype", "json");
        params.add("pn","0");
        params.add("rn","10");
        params.add("format", "1");
        JuheData.executeWithAPI(FragmentMenu.this.getActivity(),46, "http://apis.juhe.cn/cook/index", JuheData.GET, params, new DataCallBack() {
            @Override
            public void onSuccess(int i, final String s) {
                list=jsondata(s);
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
