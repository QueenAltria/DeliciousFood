package com.jiang.deliciousfood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jaeger.library.StatusBarUtil;
import com.jiang.deliciousfood.adapter.StepAdapter;
import com.jiang.deliciousfood.bean.CollectionBean;
import com.jiang.deliciousfood.bean.DetailsParse;
import com.jiang.deliciousfood.bean.MenuParse;
import com.jiang.deliciousfood.custom.CustomListView;
import com.jiang.deliciousfood.utils.CollectionDao;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.thinkland.sdk.android.DataCallBack;
import com.thinkland.sdk.android.JuheData;
import com.thinkland.sdk.android.Parameters;
import com.wang.avi.AVLoadingIndicatorView;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class DetailedActivity extends AppCompatActivity {
    private TextView id_txt,title_txt,tags_txt,imtro_txt,ingredients_txt,burden_txt;
    private ImageView albums_img;
    List<DetailsParse.ResultBean.DataBean> list=new ArrayList<>();
    CustomListView listView;
    ScrollView mScrollView;
    String id;
    Toolbar mToolbar;
    LikeButton mLikeButton;
    CollectionDao collectionDao=new CollectionDao(DetailedActivity.this);
    AVLoadingIndicatorView mAVLoadingIndicatorView;


    public void jsondata(String data){
        Gson gson=new Gson();
        DetailsParse dp=gson.fromJson(data, DetailsParse.class);

        DetailsParse.ResultBean rb=dp.getResult();
        List<DetailsParse.ResultBean.DataBean> detailsList=new ArrayList<>();
        list=rb.getData();

        mAVLoadingIndicatorView.setVisibility(View.GONE);
        mLikeButton.setVisibility(View.VISIBLE);

        final DetailsParse.ResultBean.DataBean sb=list.get(0);
        int chaxun=Integer.parseInt(sb.getId());
        if(collectionDao.findById(chaxun)!=null){
            mLikeButton.setLiked(true);
        }
        mLikeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                Toast.makeText(DetailedActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                collectionDao.insert(new CollectionBean(Integer.parseInt(sb.getId()),sb.getTitle()));
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                Toast.makeText(DetailedActivity.this, "取消收藏", Toast.LENGTH_SHORT).show();
                collectionDao.delete(Integer.parseInt(sb.getId()));
            }
        });

        id_txt.setText("编号："+sb.getId());
        title_txt.setText("名称："+sb.getTitle());
        tags_txt.setText("Tags：\n"+"\t\t\t\t"+sb.getTags());
        imtro_txt.setText("简介：\n"+"\t\t\t\t"+sb.getImtro());
        ingredients_txt.setText("主料：\n"+"\t\t\t\t"+sb.getIngredients());
        burden_txt.setText("辅料：\n"+"\t\t\t\t"+sb.getBurden());
        x.image().bind(albums_img, sb.getAlbums().get(0));

        List<DetailsParse.ResultBean.DataBean.StepsBean> stepList=sb.getSteps();



        StepAdapter adapter=new StepAdapter(stepList,DetailedActivity.this);
        //adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorBlue), 0);

        mToolbar= (Toolbar) findViewById(R.id.get_toolbar);
        mToolbar.setNavigationIcon(R.mipmap.ic_chevron_left_white_24dp);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);

        id_txt= (TextView)findViewById(R.id.details_id);
        title_txt= (TextView) findViewById(R.id.details_title);
        tags_txt= (TextView) findViewById(R.id.details_tags);
        imtro_txt= (TextView) findViewById(R.id.details_imtro);
        ingredients_txt= (TextView)findViewById(R.id.details_ingredients);
        burden_txt= (TextView) findViewById(R.id.details_burden);
        albums_img= (ImageView) findViewById(R.id.details_albums);
        mScrollView= (ScrollView) findViewById(R.id.detail_scroll);
        mAVLoadingIndicatorView= (AVLoadingIndicatorView) findViewById(R.id.jiazaizhong);
        mLikeButton= (LikeButton) findViewById(R.id.star_button);
        mScrollView.smoothScrollTo(0, 0);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent=new Intent(DetailedActivity.this,MainActivity.class);
//                startActivity(intent);
                DetailedActivity.this.finish();
            }
        });

        listView= (CustomListView) findViewById(R.id.details_listview);

        Intent intent=getIntent();
        String id=intent.getStringExtra("foodid");

        Parameters params = new Parameters();
        int foodId=Integer.parseInt(id);
        params.add("id", foodId);
        params.add("dtype", "json");
        JuheData.executeWithAPI(DetailedActivity.this, 46, "http://apis.juhe.cn/cook/queryid", JuheData.GET, params, new DataCallBack() {
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


    }


}
