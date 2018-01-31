package com.tou4u.dulejule.httpapi;

import com.tou4u.dulejule.data.Content;

import java.util.ArrayList;

public abstract class ContentAPI {

    private static final String TAG = "ContentAPI";

    protected HttpRequester requester;

    public ContentAPI() {
        requester = new HttpRequester();
    }

    public abstract ArrayList<Content> getContents();

}
