package com.tou4u.dulejule.contentviewer.main;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;

import com.tou4u.dulejule.R;

public class InfoOpener {

    private AlertDialog mHashTagDialog;
    private Context mContext;

    private static final String TITLE = "INFO";

    private static final String content = "Copyright (c) 2010, NAVER Corporation (http://www.nhncorp.com),\n" +
            "with Reserved Font Name Nanum, Naver Nanum, NanumGothic, Naver NanumGothic, NanumMyeongjo, Naver NanumMyeongjo, NanumBrush, Naver NanumBrush, NanumPen, Naver NanumPen, Naver NanumGothicEco, NanumGothicEco, Naver NanumMyeongjoEco, NanumMyeongjoEco, Naver NanumGothicLight, NanumGothicLight, NanumBarunGothic, Naver NanumBarunGothic,\n" +
            "This Font Software is licensed under the SIL Open Font license, Version 1.1.\n" +
            "This license is copied below, and is also available with a FAQ at: http://scripts.sil.org/OFL\n" +
            "SIL OPEN FONT LICENSE\n" +
            "Version 1.1 - 26 February 2007";

    private int selectedIndex;

    public InfoOpener(Context context) {
        mContext = context;
    }

    public void dismissDialog() {
        if (mHashTagDialog != null && mHashTagDialog.isShowing()) {
            mHashTagDialog.dismiss();
            mHashTagDialog = null;
        }
    }

    public void showDialog() {
        View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_info, null);

        mHashTagDialog = new AlertDialog.Builder(mContext)
                .setTitle(TITLE)
                .setView(v)
                .show();
    }


}
