package com.tou4u.dulejule.contentviewer.main;

import android.content.Context;
import android.widget.Toast;

import com.tou4u.dulejule.contentviewer.main.network.NetworkUtil;
import com.tou4u.dulejule.util.DebugUtil;

public class ContentsUpdateExcuter implements MainDataObserver {

    private static final String TAG = "ContentsUpdateExcuter";

    private volatile boolean flag;

    private OnUpdateCallBack mCallBack;

    private Context mContext;

    @Override
    public void update(int currentPage) {
        if (NetworkUtil.getInstance().isConnected(mContext) == NetworkUtil.CODE_CONNECT) {
            DebugUtil.logD(TAG, String.format("ContentsUpdateExcute %d page",currentPage));
            mCallBack.onContentsUpdate(currentPage);
        }else {
            Toast.makeText(mContext, "네트워크에 연결되지 않았습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    interface OnUpdateCallBack {
        void onContentsUpdate(int currentPage);
    }

    public ContentsUpdateExcuter(Context context, OnUpdateCallBack callBack, MainDataSubject subject) {
        mCallBack = callBack;
        mContext = context;
        subject.addObserver(this);
    }

}
