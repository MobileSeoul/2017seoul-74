package com.tou4u.dulejule.httpapi;

import com.tou4u.dulejule.data.Content;
import com.tou4u.dulejule.util.DebugUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class DetailAPI {

    private static final String TAG = "DetailAPI";

    private HttpRequester requester;

    public DetailAPI() {
        requester = new HttpRequester();
    }

    public String getDetail(String index) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("idx", index);

        String url = "https://mplatform.seoul.go.kr/api/dule/noticeDetail.do";
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
                JSONObject item = root.getJSONObject("data");
                String detail = null;
                if (item.has("CONTENTS")) {
                    detail = item.getString("CONTENTS");

                    detail = detail.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
                    detail = detail.replaceAll("\\n", "");
                    detail = detail.replaceAll("\\s+"," ");

                    if (detail.length() > 120) {
                        detail = detail.substring(0, 120) + "...";
                    }
                }
                return detail;
            }
        } catch (JSONException e) {
            DebugUtil.logE(TAG, "Error On Make Json", e);
        } catch (NullPointerException e) {
            DebugUtil.logE(TAG, "Error On Control Data", e);
        }
        return null;
    }

}
