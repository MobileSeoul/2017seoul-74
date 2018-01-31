package com.tou4u.dulejule.httpapi;

import com.tou4u.dulejule.data.Content;
import com.tou4u.dulejule.util.DebugUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ImageContentAPI extends ContentAPI {

    private static final String TAG = "ImageContentAPI";

    private HashMap<String, String> header;

    public ImageContentAPI() {
        requester = new HttpRequester();
    }

    @Override
    public ArrayList<Content> getContents() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("pagenum", 1);

        String url = "https://mplatform.seoul.go.kr/api/dule/movieList.do";
        String result = requester.requestGetData(url, null, params);

        if (result == null) {
            DebugUtil.logD(TAG, "Result None");
            return null;
        } else
            DebugUtil.logD(TAG, String.format("Get Result : %s", result));

        ArrayList<Content> contents = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(result);
            String state = root.getString("result");
            if (state.equals("success")) {
                JSONArray items = root.getJSONArray("list");
                final int size = items.length();
                for (int i = 0; i < size; i++) {
                    JSONObject content = items.getJSONObject(i);
                    String title = content.getString("TITLE");
                    String imageURL = null;

                    if (content.has("IMG")) {
                        imageURL = content.getString("IMG");
                        String[] split = imageURL.split("\'");
                        if (split.length > 1) {
                            imageURL = split[1];
                            if (!imageURL.contains("http")) {
                                imageURL = null;
                            }
                        }
                    }

                    String linkURL = null;

                    if (content.has("LINK")) {
                        linkURL = content.getString("LINK");
                    }

                    String date = content.getString("REG_DATE");

                    Content.Builder builder = new Content.Builder();
                    builder.setTitle(title);
                    builder.setImageURL(imageURL);
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
