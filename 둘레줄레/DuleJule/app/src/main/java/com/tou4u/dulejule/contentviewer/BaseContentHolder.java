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

public class BaseContentHolder extends RecyclerView.ViewHolder implements ContentHolder{

    private static final String TAG = "BaseContentHolder";

    private MainActivity mActivity;

    private Content mContent;
    private TextView mTitleTextView;
    private TextView mDateTextView;
    private TextView mDetailTextView;
    private CardView mCardView;

    public Content getBindContent() {
        return mContent;
    }

    public boolean hasBindContent() {
        return mContent != null;
    }

    public BaseContentHolder(MainActivity activity, View itemView) {
        super(itemView);
        mActivity = activity;

        mCardView = (CardView) itemView.findViewById(R.id.cardview);
        mCardView.setOnClickListener(new OnContentClick(mActivity, this));

        mTitleTextView = (TextView)
                itemView.findViewById(R.id.textview_base_content_title);
        mDateTextView = (TextView)
                itemView.findViewById(R.id.textview_base_content_date);
        mDetailTextView = (TextView)
                itemView.findViewById(R.id.imageview_base_content_detail);

    }

    public void bindContent(Content content) {
        mContent = content;
        mTitleTextView.setText(mContent.getTitle());
        mDateTextView.setText(mContent.getDate());

        String loadedDetail = mContent.getLoadedDetail();
        if (loadedDetail == null) {

        }else{
            mDetailTextView.setText(loadedDetail);
        }
        DebugUtil.logD(TAG, String.format("bind content %s", mContent.getTitle()));
    }

    public void bindDetail(String detail){
        mDetailTextView.setText(detail);
    }

}