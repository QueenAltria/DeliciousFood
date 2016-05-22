package com.jiang.deliciousfood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiang.deliciousfood.R;
import com.jiang.deliciousfood.bean.DetailsParse;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/28.
 */
public class StepAdapter extends BaseAdapter{
    List<DetailsParse.ResultBean.DataBean.StepsBean> stepList=new ArrayList<>();
    Context mContext;

    public StepAdapter(List<DetailsParse.ResultBean.DataBean.StepsBean> stepList, Context context) {
        this.stepList = stepList;
        mContext = context;
    }

    @Override
    public int getCount() {
        return stepList!=null?stepList.size():0;
    }

    @Override
    public Object getItem(int i) {
        return stepList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder=null;
        if(view==null){
            view= LayoutInflater.from(mContext).inflate(R.layout.details_adapter,null);
            viewHolder=new ViewHolder();
            viewHolder.imageView= (ImageView) view.findViewById(R.id.steps_img);
            viewHolder.textView= (TextView) view.findViewById(R.id.steps_txt);


            view.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) view.getTag();
        }
        viewHolder.textView.setText("\t\t\t\t"+stepList.get(position).getStep());

        viewHolder.imageView.setImageResource(R.mipmap.blank_space);
        String url=stepList.get(position).getImg();
        if(url!=null){
            x.image().bind(viewHolder.imageView,url);
        }

        return view;
    }

    class ViewHolder{
        ImageView  imageView;
        TextView textView;
    }
}
