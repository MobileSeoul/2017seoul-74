package com.tou4u.dulejule.contentviewer;

import android.content.Context;
import android.widget.Toast;

import com.tou4u.dulejule.contentviewer.main.network.NetworkUtil;
import com.tou4u.dulejule.util.DebugUtil;
import com.tou4u.dulejule.data.Content;
import com.tou4u.dulejule.downloading.ContentDownloader;

import java.util.ArrayList;

public class ContentsLoadManager implements ContentDownloader.OnLoadedCallBack {

    private static final String TAG = "ContentsLoadManager";

    private ContentDownloader mContentDownloader;
    private ContentAdapter mContentAdapter;
    private Context mContext;

    public ContentsLoadManager(Context context, ContentAdapter adapter) {
        mContext = context;
        mContentAdapter = adapter;
    }

    public void loadContents(int contentType) {
        if (mContentDownloader != null) {
            mContentDownloader.cancel(true);
        }
        mContentDownloader = new ContentDownloader(mContext, this, contentType);
        mContentDownloader.load();
    }

    public void cancle() {
        if (mContentDownloader != null)
            mContentDownloader.cancel(true);
    }

    @Override
    public void onLoaded(ArrayList<Content> contents) {
        if (contents != null)
            mContentAdapter.changeContents(contents);
        else {
            DebugUtil.logD(TAG, "contents load none");
        }
    }
}
