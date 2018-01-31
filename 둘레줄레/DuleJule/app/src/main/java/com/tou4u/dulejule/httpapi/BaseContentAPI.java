package com.tou4u.dulejule.httpapi;

import com.tou4u.dulejule.data.Content;
import com.tou4u.dulejule.util.DebugUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class BaseContentAPI extends ContentAPI{

    private static final String TAG = "BaseContentAPI";

    private HashMap<String, String> header;

    public BaseContentAPI() {
        requester = new HttpRequester();
    }

    @Override
    public ArrayList<Content> getContents() {

        String url = "https://mplatform.seoul.go.kr/api/dule/importantNoticeList.do";
        String result = requester.requestGetData(url, null, null);

        if (result == null) {
            DebugUtil.logD(TAG, "Result None");
            return null;
        } else
            DebugUtil.logD(TAG, String.format("Get Result : %s", result));

        ArrayList<Content> contents = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(result);
            String state = root.getString("result");
            if(state.equals("success")) {
                JSONArray items = root.getJSONArray("list");
                final int size = items.length();
                for (int i = 0; i < size; i++) {
                    JSONObject content = items.getJSONObject(i);

                    String idx = content.getString("IDX");
                    idx = idx.split("idx=")[1];

                    String title = content.getString("TITLE");
                    String date = content.getString("REG_DATE");

                    String linkURL = null;

                    if (content.has("ORGN_LINK")) {
                        linkURL = content.getString("ORGN_LINK");
                    }

                    Content.Builder builder = new Content.Builder();
                    builder.setIndex(idx);
                    builder.setTitle(title);
                    builder.setLinkURL(linkURL);
                    builder.setDate(date);
                    contents.add(builder.build());
                }

                return contents;
            }
        } catch (JSONException e) {
            DebugUtil.logE(TAG, "Error On Make Json", e);
        }
        return null;
    }

}
