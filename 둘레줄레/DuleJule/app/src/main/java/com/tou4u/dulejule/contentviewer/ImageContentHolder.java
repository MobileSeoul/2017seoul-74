package com.tou4u.dulejule.contentviewer;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tou4u.dulejule.R;
import com.tou4u.dulejule.contentviewer.main.MainActivity;
import com.tou4u.dulejule.data.Content;
import com.tou4u.dulejule.util.DebugUtil;

public class ImageContentHolder extends RecyclerView.ViewHolder implements ContentHolder {

    private static final String TAG = "ContentHolder";

    private MainActivity mActivity;

    private Content mContent;
    private TextView mTitleTextView;
    private TextView mDateTextView;
    private ImageView mImageView;
    private CardView mCardView;

    public Content getBindContent() {
        return mContent;
    }

    public boolean hasBindContent() {
        return mContent != null;
    }

    public ImageContentHolder(MainActivity activity, View itemView) {
        super(itemView);
        mActivity = activity;

        mCardView = (CardView) itemView.findViewById(R.id.cardview);
        mCardView.setOnClickListener(new OnContentClick(mActivity, this));

        mTitleTextView = (TextView)
                itemView.findViewById(R.id.textview_content_title);
        mDateTextView = (TextView)
                itemView.findViewById(R.id.textview_content_date);
        mImageView = (ImageView)
                itemView.findViewById(R.id.imageview_content_image);

    }

    public void bindContent(Content content) {
        mContent = content;
        mTitleTextView.setText(mContent.getTitle());
        mDateTextView.setText(mContent.getDate());

        Drawable loadedImage = mContent.getLoadedImage();
        if (loadedImage == null) {
            mImageView.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.no_image));
        } else {
            mImageView.setImageDrawable(loadedImage);
        }

        DebugUtil.logD(TAG, String.format("bind content %s", mContent.getTitle()));
    }

    public void bindPhoto(Drawable drawable) {
        mContent.setLoadedImage(drawable);
        mImageView.setImageDrawable(drawable);
    }

}