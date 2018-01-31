package com.tou4u.dulejule.downloading;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.tou4u.dulejule.util.DebugUtil;
import com.tou4u.dulejule.httpapi.PhotoAPI;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class PhotoDownloader<T> extends HandlerThread {
    private static final String TAG = "PhotoDownloader";
    private static final int MESSAGE_DOWNLOAD = 0;

    private Handler mRequestHandler;
    private ConcurrentMap<T, String> mRequestMap = new ConcurrentHashMap<>();

    private Handler mResponseHandler;
    private PhotoDownloader.PhotoDownloadListener<T> mPhotoDownloadListener;

    public interface PhotoDownloadListener<T> {
        void onPhotoDownloaded(T target, Bitmap photo);
    }

    public void setPhotoDownloadListener(PhotoDownloader.PhotoDownloadListener<T> listener) {
        mPhotoDownloadListener = listener;
    }

    public PhotoDownloader(Handler responseHandler) {
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
                    DebugUtil.logD(TAG, "Got a request for URL: " + mRequestMap.get(target));
                    handleRequest(target);
                }
            }
        };
    }

    public void queuePhoto(T target, String url) {

        DebugUtil.logD(TAG, "Got a URL: " + url);

        if (url == null) {
            mRequestMap.remove(target);
        } else {
            mRequestMap.put(target, url);
            mRequestHandler.obtainMessage(MESSAGE_DOWNLOAD, target)
                    .sendToTarget();
        }
    }

    public void clearQueue() {
        mRequestHandler.removeMessages(MESSAGE_DOWNLOAD);
    }

    private void handleRequest(final T target) {

        try {
            final String url = mRequestMap.get(target);

            if (url == null) {
                return;
            }

            byte[] bitmapBytes = new PhotoAPI().getUrlBytes(url);
            final Bitmap bitmap = BitmapFactory
                    .decodeByteArray(bitmapBytes, 0, bitmapBytes.length);

            if (bitmapBytes == null) {
                DebugUtil.logD(TAG, "Bitmap created None");
            } else {
                DebugUtil.logD(TAG, "Bitmap created");
            }

            mResponseHandler.post(new Runnable() {
                public void run() {
                    if (mRequestMap.get(target) != url) {
                        return;
                    }

                    mRequestMap.remove(target);
                    mPhotoDownloadListener.onPhotoDownloaded(target, bitmap);
                }
            });

        } catch (IOException ioe) {
            DebugUtil.logE(TAG, "Error downloading image", ioe);
        }
    }
}
