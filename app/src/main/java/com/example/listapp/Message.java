package com.example.listapp;

import android.graphics.Bitmap;

public class Message {
    private String title = "";
    private String url = "";

    private Bitmap icon = null;

    public Message(String title, String url, Bitmap icon) {
        this.title = title;
        this.url = url;
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }
}
