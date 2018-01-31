package com.tou4u.dulejule.contentviewer;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;

import android.view.View;
import android.widget.Toast;

import com.tou4u.dulejule.util.DebugUtil;
import com.tou4u.dulejule.contentviewer.main.MainActivity;
import com.tou4u.dulejule.data.Content;

public class OnContentClick implements View.OnClickListener {

    private static final String TAG = "OnContentClick";
    private ContentHolder mContentHolder;
    private MainActivity mActivity;

    public OnContentClick(MainActivity activity, ContentHolder contentHolder) {
        mContentHolder = contentHolder;
        mActivity = activity;
    }

    @Override
    public void onClick(View v) {
        if (mContentHolder.hasBindContent())
            new AlertDialog.Builder(mActivity)
                    .setTitle("자세한 정보")
                    .setPositiveButton("링크", getOnLinkAction())
                    .show();
        else
            Toast.makeText(mActivity, "로딩된 데이터가 없습니다.", Toast.LENGTH_SHORT).show();
    }

    private Intent getLinkIntent(String linkURL) {
        Uri uri = Uri.parse(linkURL);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent = intent.createChooser(intent, "인터넷");
        return intent;
    }

    private DialogInterface.OnClickListener getOnLinkAction() {

        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Content content = mContentHolder.getBindContent();

                Intent intent = getLinkIntent(content.getLinkURL());
                mActivity.startActivity(intent);

                DebugUtil.logD(TAG, String.format("%s", content.getLinkURL()));
            }
        };
    }
}
