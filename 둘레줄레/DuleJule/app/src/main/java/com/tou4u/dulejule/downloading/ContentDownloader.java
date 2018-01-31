package com.tou4u.dulejule.downloading;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.tou4u.dulejule.contentviewer.fragment.ContentListFragment;
import com.tou4u.dulejule.contentviewer.main.MainActivity;
import com.tou4u.dulejule.contentviewer.main.network.NetworkUtil;
import com.tou4u.dulejule.httpapi.BaseContentAPI;
import com.tou4u.dulejule.httpapi.EventContentAPI;
import com.tou4u.dulejule.httpapi.ImageContentAPI;
import com.tou4u.dulejule.polling.QueryPreferences;
import com.tou4u.dulejule.util.DebugUtil;
import com.tou4u.dulejule.data.Content;
import com.tou4u.dulejule.httpapi.ContentAPI;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class ContentDownloader extends AsyncTask<Object, Void, ArrayList<Content>> {

    private static final String TAG = "ContentDownloader";

    private OnLoadedCallBack mOnLoadedCallBack;

    private ContentAPI api;

    private int mContentType;

    private Context mContext;

    public interface OnLoadedCallBack {
        void onLoaded(ArrayList<Content> contents);
    }

    public ContentDownloader(Context context, OnLoadedCallBack onLoadedCallBack, int contentType) {
        mOnLoadedCallBack = onLoadedCallBack;
        mContext = context;
        mContentType = contentType;
        if (contentType == MainActivity.CONTENT_TYPE_CODE_NOTICE_BASE) {
            api = new BaseContentAPI();
        } else if (contentType == MainActivity.CONTENT_TYPE_CODE_NOTICE_IMAGE) {
            api = new ImageContentAPI();
        } else if (contentType == MainActivity.CONTENT_TYPE_CODE_NOTICE_EVENT) {
            api = new EventContentAPI();
        } else {
            api = null;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<Content> contents) {
        super.onPostExecute(contents);
        if (contents != null) {
            if (contents.size() > 0) {
                Content content = contents.get(0);
                String link = content.getLinkURL();
                String date = content.getDate();
                String title = content.getTitle();
                HashSet<String> set = new HashSet<>();
                set.add(link);
                set.add(date);
                set.add(title);
                if (mContentType == MainActivity.CONTENT_TYPE_CODE_NOTICE_BASE) {
                    String id = content.getIndex();
                    set.add(id);
                    QueryPreferences.setRecentBaseContent(mContext, set);
                } else if (mContentType == MainActivity.CONTENT_TYPE_CODE_NOTICE_IMAGE) {
                    QueryPreferences.setRecentImageContent(mContext, set);
                } else if (mContentType == MainActivity.CONTENT_TYPE_CODE_NOTICE_EVENT) {
                    QueryPreferences.setRecentEventContent(mContext, set);
                }
            }
            mOnLoadedCallBack.onLoaded(contents);
        }else {
            if (NetworkUtil.getInstance().isConnected(mContext) == NetworkUtil.CODE_CONNECT) {
                Toast.makeText(mContext, "공지 로딩 실패", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, "네트워크에 연결되지 않았습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected ArrayList<Content> doInBackground(Object... params) {
        try {
            if (api != null) {
                ArrayList<Content> contents = api.getContents();
                return contents;
            }
        } catch (NullPointerException e) {
            DebugUtil.logE(TAG, "Error On Background", e);
        }
        return null;
    }

    public void load() {
        execute();
    }
}
