package com.tou4u.dulejule.contentviewer;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;

import com.tou4u.dulejule.util.DebugUtil;
import com.tou4u.dulejule.contentviewer.main.MainActivity;
import com.tou4u.dulejule.downloading.DetailDownloader;
import com.tou4u.dulejule.downloading.PhotoDownloader;


public class ContentLoadManager implements ImageContentAdapter.BindCallBack, BaseContentAdapter.BindCallBack {

    private static final String TAG = "ContentLoadManager";

    private MainActivity mActivity;

    private PhotoDownloader<ContentHolder> mPhotoDownloader;
    private DetailDownloader<ContentHolder> mDetailDownloader;

    private int mContentType;

    public ContentLoadManager(MainActivity activity, int contentType) {
        mContentType = contentType;
        mActivity = activity;
        try {

            if (contentType == MainActivity.CONTENT_TYPE_CODE_NOTICE_BASE) {
                Handler responseHandlerForDetail = new Handler();
                mDetailDownloader = new DetailDownloader<>(responseHandlerForDetail);

                mDetailDownloader.setDetailDownloadListener(
                        new DetailDownloader.DetailDownloadListener<ContentHolder>() {
                            @Override
                            public void onDetailDownloaded(ContentHolder contentHolder, String detail) {
                                try {
                                    if (detail != null) {
                                        if (contentHolder instanceof BaseContentHolder) {
                                            ((BaseContentHolder) contentHolder).bindDetail(detail);
                                        }
                                    }
                                } catch (IllegalStateException e) {
                                    DebugUtil.logE(TAG + "TYPE " + mContentType, "Error bind detail", e);
                                }
                            }
                        }
                );

                mDetailDownloader.start();
                mDetailDownloader.getLooper();
            } else {
                Handler responseHandlerForPhoto = new Handler();
                mPhotoDownloader = new PhotoDownloader<>(responseHandlerForPhoto);

                mPhotoDownloader.setPhotoDownloadListener(
                        new PhotoDownloader.PhotoDownloadListener<ContentHolder>() {
                            @Override
                            public void onPhotoDownloaded(ContentHolder contentHolder, Bitmap bitmap) {
                                try {
                                    if (bitmap != null) {
                                        Drawable drawable = new BitmapDrawable(mActivity.getResources(), bitmap);
                                        if (contentHolder instanceof ImageContentHolder) {
                                            ((ImageContentHolder) contentHolder).bindPhoto(drawable);
                                        }
                                    }
                                } catch (IllegalStateException e) {
                                    DebugUtil.logE(TAG + "TYPE " + mContentType, "Error bind image", e);
                                }
                            }
                        }
                );

                mPhotoDownloader.start();
                mPhotoDownloader.getLooper();
            }


        } catch (IllegalStateException e) {
            DebugUtil.logE(TAG + "TYPE " + mContentType, "Error On Create", e);
        }
    }

    public void clearQueue() {
        if (mPhotoDownloader != null)
            mPhotoDownloader.clearQueue();
        if (mDetailDownloader != null)
            mDetailDownloader.clearQueue();
    }

    public void quit() {
        if (mPhotoDownloader != null)
            mPhotoDownloader.quit();
        if (mDetailDownloader != null)
            mDetailDownloader.quit();
    }

    @Override
    public void onBind(BaseContentHolder holder, String contentIndex) {
        if (mDetailDownloader != null) {
            mDetailDownloader.queueDetail(holder, contentIndex);
        }
    }

    @Override
    public void onBind(ImageContentHolder holder, String imageURL) {
        if (mPhotoDownloader != null) {
            mPhotoDownloader.queuePhoto(holder, imageURL);
        }
    }
}
