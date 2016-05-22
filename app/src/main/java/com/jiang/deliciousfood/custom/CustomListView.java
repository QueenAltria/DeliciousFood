package com.jiang.deliciousfood.custom;

import android.app.Dialog;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Administrator on 2016/4/28.
 */

/**
 * 这个方法有一个毛病，就是默认显示的首项是ListView，需要手动把ScrollView滚动至最顶端。
 *  sv = (ScrollView) findViewById(R.id.scroll);
 *  sv.smoothScrollTo(0, 0);
 */
public class CustomListView extends ListView{

    public CustomListView(Context context) {
        super(context);

    }

    public CustomListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
