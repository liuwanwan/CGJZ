package com.cgjz;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015-11-9.
 */
public class MyHScrollView extends HorizontalScrollView {
    ScrollViewObserver mScrollViewObserver = new ScrollViewObserver();

    public MyHScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    public MyHScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public MyHScrollView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Log.i("pdwy", "MyHScrollView onTouchEvent");
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		/*
		 * 褰撴粴鍔ㄦ潯绉诲姩鍚庯紝寮曞彂 婊氬姩浜嬩欢銆傞�氱煡缁欒瀵熻�咃紝瑙傚療鑰呬細浼犺揪缁欏叾浠栫殑銆�
		 */
        if (mScrollViewObserver != null /*&& (l != oldl || t != oldt)*/) {
            mScrollViewObserver.NotifyOnScrollChanged(l, t, oldl, oldt);
        }
        Log.v("tt","tttttt");
        super.onScrollChanged(l, t, oldl, oldt);
    }

    /*
     * 璁㈤槄 鏈帶浠� 鐨� 婊氬姩鏉″彉鍖栦簨浠�
     * */
    public void AddOnScrollChangedListener(OnScrollChangedListener listener) {
        mScrollViewObserver.AddOnScrollChangedListener(listener);
    }

    /*
     * 鍙栨秷 璁㈤槄 鏈帶浠� 鐨� 婊氬姩鏉″彉鍖栦簨浠�
     * */
    public void RemoveOnScrollChangedListener(OnScrollChangedListener listener) {
        mScrollViewObserver.RemoveOnScrollChangedListener(listener);
    }

    /*
     * 褰撳彂鐢熶簡婊氬姩浜嬩欢鏃�
     */
    public static interface OnScrollChangedListener {
        public void onScrollChanged(int l, int t, int oldl, int oldt);
    }

    /*
     * 瑙傚療鑰�
     */
    public static class ScrollViewObserver {
        List<OnScrollChangedListener> mList;

        public ScrollViewObserver() {
            super();
            mList = new ArrayList<OnScrollChangedListener>();
        }

        public void AddOnScrollChangedListener(OnScrollChangedListener listener) {
            mList.add(listener);
        }

        public void RemoveOnScrollChangedListener(
                OnScrollChangedListener listener) {
            mList.remove(listener);
        }

        public void NotifyOnScrollChanged(int l, int t, int oldl, int oldt) {
            if (mList == null || mList.size() == 0) {
                return;
            }
            for (int i = 0; i < mList.size(); i++) {
                if (mList.get(i) != null) {
                    mList.get(i).onScrollChanged(l, t, oldl, oldt);
                }
            }
        }
    }
}
