package com.tou4u.dulejule;

import android.app.Application;

import com.tsengvn.typekit.Typekit;

public class App extends Application {

    private static final String FONT_NAME_NanumPen = "fonts/NanumPen.ttf";
    private static final String FONT_NAME_GothicBold = "fonts/NanumGothicBold.ttf";
    private static final String FONT_NAME_MyeongjoBold = "fonts/NanumMyeongjoBold.ttf";
    private static final String FONT_NAME_Barun = "fonts/NanumBarunGothic.ttf";
    private static final String FONT_NAME_BarunBold = "fonts/NanumBarunGothicBold.ttf";


    @Override
    public void onCreate() {
        super.onCreate();

        Typekit.getInstance()
                .add("NanumPen",Typekit.createFromAsset(this, FONT_NAME_NanumPen))
                .add("GothicBold",Typekit.createFromAsset(this, FONT_NAME_GothicBold))
                .add("MyeongjoBold",Typekit.createFromAsset(this, FONT_NAME_MyeongjoBold))
                .add("NanumBarunGothic",Typekit.createFromAsset(this, FONT_NAME_Barun))
                .add("NanumBarunGothicBold",Typekit.createFromAsset(this, FONT_NAME_BarunBold));

    }
}
