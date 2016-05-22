package com.jiang.deliciousfood.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.jiang.deliciousfood.R;
import com.jiang.deliciousfood.bean.MenuParse;

import org.xutils.x;

import java.util.List;

/**
 * Created by Administrator on 2016/4/22.
 * 菜谱分类的适配器
 */
public class MenuAdapter extends BaseAdapter {
    List<MenuParse.ResultBean.DataBean> mDataBeanList;
    Context mContext;

    public MenuAdapter(List<MenuParse.ResultBean.DataBean> dataBeanList, Context context) {
        mDataBeanList = dataBeanList;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mDataBeanList!=null?mDataBeanList.size():0;
    }

    @Override
    public Object getItem(int i) {
        return mDataBeanList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder=null;
        if(view==null){
            view= LayoutInflater.from(mContext).inflate(R.layout.menu_adapter,null);
            viewHolder=new ViewHolder();
            viewHolder.imageView= (ImageView) view.findViewById(R.id.albums);
            viewHolder.textView1= (TextView) view.findViewById(R.id.menu_title);
            viewHolder.textView2= (TextView) view.findViewById(R.id.menu_imtro);

            view.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) view.getTag();
        }
        viewHolder.textView1.setText(mDataBeanList.get(position).getTitle());
        viewHolder.textView2.setText("\t\t\t\t"+mDataBeanList.get(position).getImtro());
        viewHolder.imageView.setImageResource(R.mipmap.blank_space);
        String url=mDataBeanList.get(position).getAlbums().get(0);
        if(url!=null){
            x.image().bind(viewHolder.imageView,url);
        }

        return view;
    }

    class ViewHolder{
        ImageView imageView;
        TextView textView1;
        TextView textView2;
    }

    /**
     * Volley加载图片
     * @param imageView
     * @param url
     */
    public void downloadImage(final ImageView imageView,String url){

        RequestQueue mQueue = Volley.newRequestQueue(mContext);
        ImageRequest imageRequest = new ImageRequest(
                url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        imageView.setImageBitmap(response);
                    }
                }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                imageView.setImageResource(R.mipmap.ic_launcher);
            }
        });
        mQueue.add(imageRequest);
    }
}
