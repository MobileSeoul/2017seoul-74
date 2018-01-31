package com.tou4u.dulejule.data;

import android.graphics.drawable.Drawable;

public class Content {

    private String loadedDetail;
    private Drawable loadedImage;

    public Drawable getLoadedImage() {
        return loadedImage;
    }

    public String getLoadedDetail() {
        return loadedDetail;
    }

    public void setLoadedDetail(String loadedDetail) {
        this.loadedDetail = loadedDetail;
    }

    public void setLoadedImage(Drawable loadedImage) {
        this.loadedImage = loadedImage;
    }

    private String index;
    private String title;
    private String imageURL;
    private String date;
    private String linkURL;

    public Content() {
        index = null;
    }

    public String getIndex() {
        return index;
    }

    public String getTitle() {
        return title;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getDate() {
        return date;
    }

    public String getLinkURL() {
        return linkURL;
    }

    public static final class Builder {
        private Content info;

        public Builder() {
            info = new Content();
        }

        public Content build() {
            return info;
        }

        public void setIndex(String index) {
            info.index = index;
        }

        public void setTitle(String title) {
            info.title = title;
        }

        public void setImageURL(String imageURL) {
            info.imageURL = imageURL;
        }

        public void setDate(String date) {
            info.date = date;
        }

        public void setLinkURL(String linkURL) {
            info.linkURL = linkURL;
        }
    }
}
