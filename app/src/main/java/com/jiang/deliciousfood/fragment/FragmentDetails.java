package com.jiang.deliciousfood.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jiang.deliciousfood.R;
import com.jiang.deliciousfood.adapter.MenuAdapter;
import com.jiang.deliciousfood.adapter.StepAdapter;
import com.jiang.deliciousfood.bean.DetailsParse;
import com.jiang.deliciousfood.bean.JsonParse;
import com.jiang.deliciousfood.bean.MenuParse;
import com.jiang.deliciousfood.custom.CustomListView;
import com.thinkland.sdk.android.DataCallBack;
import com.thinkland.sdk.android.JuheData;
import com.thinkland.sdk.android.Parameters;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2016/4/28.
 */
public class FragmentDetails extends Fragment{
   private TextView id_txt,title_txt,tags_txt,imtro_txt,ingredients_txt,burden_txt;
    private ImageView albums_img;
    List<DetailsParse.ResultBean.DataBean> list=new ArrayList<>();
    CustomListView listView;
    ScrollView mScrollView;
    String id;

    public void setAll(MenuParse.ResultBean.DataBean dataBean){
        id=dataBean.getId();
    }

    public void jsondata(String data){
        Gson gson=new Gson();
        DetailsParse dp=gson.fromJson(data, DetailsParse.class);

        DetailsParse.ResultBean rb=dp.getResult();
        List<DetailsParse.ResultBean.DataBean> detailsList=new ArrayList<>();
        list=rb.getData();

        DetailsParse.ResultBean.DataBean sb=list.get(0);

        id_txt.setText("编号："+sb.getId());
        title_txt.setText("名称："+sb.getTitle());
        tags_txt.setText("Tags：\n"+"\t\t\t\t"+sb.getTags());
        imtro_txt.setText("简介：\n"+"\t\t\t\t"+sb.getImtro());
        ingredients_txt.setText("主料：\n"+"\t\t\t\t"+sb.getIngredients());
        burden_txt.setText("辅料：\n"+"\t\t\t\t"+sb.getBurden());
        x.image().bind(albums_img, sb.getAlbums().get(0));

        List<DetailsParse.ResultBean.DataBean.StepsBean> stepList=sb.getSteps();



        StepAdapter adapter=new StepAdapter(stepList,FragmentDetails.this.getActivity());
        //adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_fooddetails,null);

        id_txt= (TextView) view.findViewById(R.id.details_id);
        title_txt= (TextView) view.findViewById(R.id.details_title);
        tags_txt= (TextView) view.findViewById(R.id.details_tags);
        imtro_txt= (TextView) view.findViewById(R.id.details_imtro);
        ingredients_txt= (TextView) view.findViewById(R.id.details_ingredients);
        burden_txt= (TextView) view.findViewById(R.id.details_burden);
        albums_img= (ImageView) view.findViewById(R.id.details_albums);
        mScrollView= (ScrollView) view.findViewById(R.id.detail_scroll);
        mScrollView.smoothScrollTo(0, 0);

        listView= (CustomListView) view.findViewById(R.id.details_listview);

        Parameters params = new Parameters();
        int foodId=Integer.parseInt(id);
        params.add("id", foodId);
        params.add("dtype", "json");
        JuheData.executeWithAPI(FragmentDetails.this.getActivity(), 46, "http://apis.juhe.cn/cook/queryid", JuheData.GET, params, new DataCallBack() {
            @Override
            public void onSuccess(int i, final String s) {
                jsondata(s);
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

    /**
     * 动态设置ListView的高度
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if(listView == null) return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

}

