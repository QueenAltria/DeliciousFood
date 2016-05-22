package com.jiang.deliciousfood.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.jiang.deliciousfood.bean.JsonParse;
import com.thinkland.sdk.android.DataCallBack;
import com.thinkland.sdk.android.JuheData;
import com.thinkland.sdk.android.Parameters;

import com.jiang.deliciousfood.R;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/17.
 */
public class FragmentExpandable extends Fragment {

    ExpandableListView mExpandableListView;
    List<String> parent = null;
    Map<String, List<JsonParse.ResultBean.ListBean>> map = null;
    private Context mContext;
    TextView mTextView;
    MyAdapter adapter = new MyAdapter();
    JsonParse.ResultBean.ListBean list;
    AVLoadingIndicatorView mAVLoadingIndicatorView;
    public CallBack mCallBack;


//    private Handler mHandler=new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            JsonParse jp= (JsonParse) msg.obj;
//            getResult(jp);
//        }
//    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.parent_view, null);

        mExpandableListView = (ExpandableListView) view.findViewById(R.id.list);
        mAVLoadingIndicatorView= (AVLoadingIndicatorView) view.findViewById(R.id.jiazaizhong);

        initData();

        mExpandableListView.setAdapter(adapter);

        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id) {
                JsonParse.ResultBean.ListBean list = (JsonParse.ResultBean.ListBean) adapter.getChild(groupPosition, childPosition);
                mCallBack.sendData(list);
                String info = list.getId();
                //Toast.makeText(FragmentExpandable.this.getActivity(), info, Toast.LENGTH_LONG).show();
                return false;
            }
        });

        return view;


    }

    //填充数据
    public void initData() {
        map = new HashMap<>();
        parent = new ArrayList<>();

            //Volley请求数据
//        RequestQueue mQueue = Volley.newRequestQueue(FragmentExpandable.this.getActivity());
//        String path="http://www.baidu.com";
//        String url="http://apis.juhe.cn/cook/category?parentid=&dtype=json&key=780cf5a649f30cc8e0a40c629a93dc59";
//        StringRequest stringRequest = new StringRequest(url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        //mTextView.setText(response);
//                        jsondata(response);
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("jiang", error.getMessage(), error);
//            }
//        });
//        mQueue.add(stringRequest);


        Parameters params = new Parameters();
        params.add("parentid", 0);
        params.add("dtype", "json");
        JuheData.executeWithAPI(FragmentExpandable.this.getActivity(), 46, "http://apis.juhe.cn/cook/category", JuheData.GET, params, new DataCallBack() {
            @Override
            public void onSuccess(int i, final String s) {
                //直接解析
                jsondata(s);
                //Handle解析
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        JsonParse jp=getjp(s);
//                        Message message = mHandler.obtainMessage();
//                        message.obj = jp;
//                        mHandler.sendMessage(message);
//                    }
//                }).start();
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

    //直接解析完毕JSon
    public void jsondata(String data) {
        Gson gson = new Gson();
        JsonParse jp = gson.fromJson(data, JsonParse.class);
        List<JsonParse.ResultBean> list = jp.getResult();
        for (int i = 0; i < list.size(); i++) {
            List<JsonParse.ResultBean.ListBean> list1 = list.get(i).getList();
            mAVLoadingIndicatorView.setVisibility(View.GONE);
            String key = list.get(i).getName().toString();
            String pId1 = list.get(i).getParentId().toString();
            parent.add(key);
            List<JsonParse.ResultBean.ListBean> listId = new ArrayList<>();
            for (int j = 0; j < list1.size(); j++) {
                listId.add(list1.get(j));
                map.put(key, listId);
                adapter.notifyDataSetChanged();
            }
        }
    }

    public JsonParse getjp(String data) {
        Gson gson = new Gson();
        JsonParse jp = gson.fromJson(data, JsonParse.class);
        return jp;
    }

    public void getResult(JsonParse jp) {
        List<JsonParse.ResultBean> list = jp.getResult();
        for (int i = 0; i < list.size(); i++) {
            List<JsonParse.ResultBean.ListBean> list1 = list.get(i).getList();
            String key = list.get(i).getName().toString();
            String pId1 = list.get(i).getParentId().toString();
            parent.add(key);
            List<JsonParse.ResultBean.ListBean> listId = new ArrayList<>();
            for (int j = 0; j < list1.size(); j++) {
                listId.add(list1.get(j));
                map.put(key, listId);
                adapter.notifyDataSetChanged();
            }
        }
    }


    //继承BaseExpandableListAdapter
    class MyAdapter extends BaseExpandableListAdapter {

        @Override
        public int getGroupCount() {
            return parent.size();
        }

        //获取当前父item下的子item的个数
        @Override
        public int getChildrenCount(int groupPosition) {
            String key = parent.get(groupPosition);
            int size = map.get(key).size();
            return size;
        }

        //获取当前父item的数据
        @Override
        public Object getGroup(int groupPosition) {
            return parent.get(groupPosition);
        }

        @Override
        //得到子item需要关联的数据
        public Object getChild(int groupPosition, int childPosition) {
            String key = parent.get(groupPosition);
            return (map.get(key).get(childPosition));
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        //得到子item的ID
        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        //设置父item组件
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                                 ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(FragmentExpandable.this.getActivity()).inflate(R.layout.layout_parent, null);
            }
            TextView tv = (TextView) convertView
                    .findViewById(R.id.parent_textview);
            tv.setText(FragmentExpandable.this.parent.get(groupPosition));
            return convertView;
        }

        //设置子item的组件
        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            String key = FragmentExpandable.this.parent.get(groupPosition);
            String info = map.get(key).get(childPosition).getName();
            if (convertView == null) {
                convertView = LayoutInflater.from(FragmentExpandable.this.getActivity()).inflate(R.layout.layout_children, null);
            }
            TextView tv = (TextView) convertView
                    .findViewById(R.id.second_textview);
            tv.setText(info);
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }
    }

    //回调接口，向下一个Fragment传递ID
    public interface CallBack {
        void sendData(JsonParse.ResultBean.ListBean list);
    }
}
