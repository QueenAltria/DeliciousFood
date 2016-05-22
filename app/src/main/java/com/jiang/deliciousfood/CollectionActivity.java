package com.jiang.deliciousfood;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.jiang.deliciousfood.bean.CollectionBean;
import com.jiang.deliciousfood.utils.CollectionDao;

import java.util.ArrayList;
import java.util.List;

public class CollectionActivity extends AppCompatActivity {
    ListView mListView;
    Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        mListView= (ListView) findViewById(R.id.collection_list);

        mToolbar = (Toolbar) findViewById(R.id.collection_toolbar);
        mToolbar.setTitle("我的收藏");
        mToolbar.setNavigationIcon(R.mipmap.ic_chevron_left_white_24dp);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setBackgroundColor(getResources().getColor(R.color.colorBlue));
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorBlue), 0);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(CollectionActivity.this, MainActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //startActivity(intent);
                CollectionActivity.this.finish();
            }
        });

        CollectionDao collectionDao=new CollectionDao(CollectionActivity.this);
        final List<CollectionBean> list=collectionDao.findAll();
        final List<String> list1=new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            String name=list.get(i).getFoodName();
            list1.add(name);
        }

        CollectionAdapter adapter=new CollectionAdapter(list1,this);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String id=String.valueOf(list.get(i).getFoodId());
                Intent intent=new Intent(CollectionActivity.this,DetailedActivity.class);
                intent.putExtra("foodid",id);
                startActivity(intent);
            }
        });
    }

    class CollectionAdapter extends BaseAdapter{
        List<String> mList=new ArrayList<>();
        Context mContext;

        public CollectionAdapter(List<String> list, Context context) {
            mList = list;
            mContext = context;
        }

        @Override
        public int getCount() {
            return mList!=null?mList.size():0;
        }

        @Override
        public Object getItem(int i) {
            return mList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view= LayoutInflater.from(mContext).inflate(R.layout.layout_children,null);
            TextView textView= (TextView) view.findViewById(R.id.second_textview);
            textView.setText(mList.get(i));
            return view;
        }


    }
}
