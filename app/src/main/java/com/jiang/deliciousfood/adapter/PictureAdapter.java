package com.jiang.deliciousfood.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiang.deliciousfood.R;
import com.jiang.deliciousfood.bean.PictureParse;

import org.xutils.x;

import java.util.List;

/**
 * Created by Administrator on 2016/5/3.
 */
public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.PictureView>{
    private List<PictureParse.ResultsBean> mBeanList;
    private Context mContext;

    public PictureAdapter(List<PictureParse.ResultsBean> list, Context context) {
        mBeanList=list;
        mContext=context;
    }

    @Override
    public PictureView onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.picture_item, viewGroup, false);
        return new PictureView(view);
    }

    @Override
    public void onBindViewHolder(PictureView masonryView, int position) {
        masonryView.imageView.setImageResource(R.mipmap.blank_space);
        x.image().bind(masonryView.imageView, mBeanList.get(position).getUrl());
        masonryView.textView.setText(mBeanList.get(position).getWho());

    }

    @Override
    public int getItemCount() {
        return mBeanList.size();
    }

    public static class PictureView extends  RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textView;

        public PictureView(View itemView){
            super(itemView);

            imageView= (ImageView) itemView.findViewById(R.id.masonry_item_img );
            textView= (TextView) itemView.findViewById(R.id.masonry_item_title);
        }

    }

}