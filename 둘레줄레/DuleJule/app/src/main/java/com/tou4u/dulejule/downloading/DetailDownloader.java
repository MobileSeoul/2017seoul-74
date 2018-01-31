package com.tou4u.dulejule.downloading;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.tou4u.dulejule.util.DebugUtil;
import com.tou4u.dulejule.httpapi.DetailAPI;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DetailDownloader<T> extends HandlerThread {
    private static final String TAG = "DetailDownloader";
    private static final int MESSAGE_DOWNLOAD = 0;

    private Handler mRequestHandler;
    private ConcurrentMap<T, String> mRequestMap = new ConcurrentHashMap<>();

    private Handler mResponseHandler;
    private DetailDownloader.DetailDownloadListener<T> mDetailDownloadListener;

    public interface DetailDownloadListener<T> {
        void onDetailDownloaded(T target, String detail);
    }

    public void setDetailDownloadListener(DetailDownloader.DetailDownloadListener<T> listener) {
        mDetailDownloadListener = listener;
    }

    public DetailDownloader(Handler responseHandler) {
        super(TAG);
        mResponseHandler = responseHandler;
    }

    @Override
    protected void onLooperPrepared() {
        mRequestHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MESSAGE_DOWNLOAD) {
                    T target = (T) msg.obj;
                    DebugUtil.logD(TAG,"Got a request for URL: " + mRequestMap.get(target));
                    handleRequest(target);
                }
            }
        };
    }

    public void queueDetail(T target, String contentID) {
        DebugUtil.logD(TAG, "Got a URL: " + contentID);

        if (contentID == null) {
            mRequestMap.remove(target);
        } else {
            mRequestMap.put(target, contentID);
            mRequestHandler.obtainMessage(MESSAGE_DOWNLOAD, target)
                    .sendToTarget();
        }
    }

    public void clearQueue() {
        mRequestHandler.removeMessages(MESSAGE_DOWNLOAD);
    }

    private void handleRequest(final T target) {

        final String contentID = mRequestMap.get(target);

        if (contentID == null) {
            DebugUtil.logD(TAG, "contentID None");
            return;
        }

        final String detail = new DetailAPI().getDetail(contentID);

        if (detail != null) {
            int size = detail.length();
            size = (size > 10) ? 10 : size;
            DebugUtil.logD(TAG, String.format("Detail download ( %s )", detail.substring(0, size)));
        }else{
            DebugUtil.logD(TAG, "Detail download None");
        }

        mResponseHandler.post(new Runnable() {
            public void run() {
                if (mRequestMap.get(target) != contentID) {
                    return;
                }
                mRequestMap.remove(target);
                mDetailDownloadListener.onDetailDownloaded(target, detail);
            }
        });


    }
}
