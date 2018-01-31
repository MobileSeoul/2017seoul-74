package com.tou4u.dulejule.downloading;

import android.content.Context;
import android.os.AsyncTask;

import com.tou4u.dulejule.contentviewer.main.MainActivity;
import com.tou4u.dulejule.data.Content;
import com.tou4u.dulejule.httpapi.BaseContentAPI;
import com.tou4u.dulejule.httpapi.ContentAPI;
import com.tou4u.dulejule.httpapi.EventContentAPI;
import com.tou4u.dulejule.httpapi.ImageContentAPI;
import com.tou4u.dulejule.polling.QueryPreferences;
import com.tou4u.dulejule.util.DebugUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class RecentDownloader {

    private static final String TAG = "RecentDownloader";

    private ContentAPI baseAPI;
    private ContentAPI imageAPI;
    private ContentAPI eventAPI;

    private Context mContext;

    public RecentDownloader(Context context) {
        mContext = context;
        baseAPI = new BaseContentAPI();
        imageAPI = new ImageContentAPI();
        eventAPI = new EventContentAPI();
    }

    public String executeBaseAPI(Set<String> current) {
        return excuteAPI(baseAPI, current);
    }

    public String executeImageAPI(Set<String> current) {
        return excuteAPI(imageAPI, current);
    }

    public String executeEventAPI(Set<String> current) {
        return excuteAPI(eventAPI, current);
    }

    private String excuteAPI(ContentAPI api, Set<String> current) {
        ArrayList<Content> base = api.getContents();

        if (base != null) {
            if (base.size() > 0) {
                Content content = base.get(0);

                String link = content.getLinkURL();
                String date = content.getDate();
                String title = content.getTitle();
                HashSet<String> set = new HashSet<>();
                set.add(link);
                set.add(date);
                set.add(title);

                if (api instanceof BaseContentAPI) {
                    String index = content.getIndex();
                    set.add(index);
                    QueryPreferences.setRecentBaseContent(mContext, set);
                    if (current != null) {
                        if (!current.contains(index)) {
                            return title;
                        }
                    }
                } else if (api instanceof ImageContentAPI) {
                    QueryPreferences.setRecentImageContent(mContext, set);
                } else if (api instanceof EventContentAPI) {
                    QueryPreferences.setRecentEventContent(mContext, set);
                }

                String result = isRecent(current, content);
                return result;
            }
        }
        return null;
    }

    private String isRecent(Set<String> current, Content content) {

        String title = content.getTitle();
        String linkURL = content.getLinkURL();
        String date = content.getDate();

        if (current == null) {
            return title;
        }

        if (current.contains(title) && current.contains(linkURL) && current.contains(date)) {
            return null;
        }

        return title;
    }
}
