package com.example.listapp;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebBackForwardList;
import android.webkit.WebHistoryItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Controller {
    Activity activity;
    String currentUrl;
    WebView myWebViw;

    public List<Message> historyList = new ArrayList<Message>();
    public List<Message> bookmarkList = new ArrayList<Message>();

    public Controller(Activity activity) {
        this.activity = activity;
        SharedPreferences sharedPreferences = activity.getSharedPreferences("MyPrefs", MODE_PRIVATE);
        currentUrl  = sharedPreferences.getString("myHomeUrl", "https://www.google.com");
        setupHomeScreen(currentUrl);
    }

    public Controller(Activity activity, String url) {
        this.activity = activity;
        currentUrl = url;
        setupHomeScreen(currentUrl);
    }

    public String getCurrentUrl() {
        return currentUrl;
    }

    private void setupHomeScreen(String url) {
        activity.setContentView(R.layout.activity_main);

        EditText input = (EditText) activity.findViewById(R.id.urlinput);

        ImageButton addBookmarkBtn = (ImageButton) this.activity.findViewById(R.id.addBookmarkBtn);
        addBookmarkBtn.setOnClickListener((view -> {
            String bookmarkurl = myWebViw.getUrl();
            String title = myWebViw.getTitle();
            Bitmap icon =  myWebViw.getFavicon();

            Message bookMarkref = null;
            for (Message bookmark : bookmarkList){
                if(bookmark.getUrl().equals(bookmarkurl)){
                    bookMarkref = bookmark;

                }
            }
            if (bookMarkref != null){
                bookmarkList.remove(bookMarkref);
                addBookmarkBtn.setImageResource(R.drawable.baseline_star_outline_24);
            }else {
                bookmarkList.add(new Message(title, bookmarkurl, icon));
                addBookmarkBtn.setImageResource(R.drawable.baseline_star_24);
            }
        }));

        WebViewClient myWebViewClient = (new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                currentUrl = url;
                if (historyList.isEmpty() || !historyList.get(historyList.size() - 1).getUrl().equals(currentUrl)){
                    historyList.add(new Message(view.getTitle(), url, view.getFavicon()));
                }
                String pageUrl = view.getUrl();

                boolean hasBookMark = false;
                for (Message bookmark : bookmarkList){
                    if(bookmark.getUrl().equals(pageUrl)){
                        addBookmarkBtn.setImageResource(R.drawable.baseline_star_24);
                        hasBookMark = true;
                    }
                }
                if (!hasBookMark){
                    addBookmarkBtn.setImageResource(R.drawable.baseline_star_outline_24);
                }
            }
            @Override
            public void onPageFinished(WebView view, String url) {

            }
        });
        myWebViw = (WebView) activity.findViewById(R.id.webview);
        WebSettings webSettings = myWebViw.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebViw.setWebViewClient(myWebViewClient);

        input.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press

                    String inputUrl = "";
                    if (input.getText() != null) {
                        if (input.getText().toString().contains("https://www."))
                            inputUrl = input.getText().toString();
                        else if (input.getText().toString().contains("www."))
                            inputUrl = "https://" + input.getText();
                        else
                            inputUrl = "https://www." + input.getText();
                    }

                    myWebViw.loadUrl(inputUrl);
                    currentUrl = inputUrl;
                    input.setText("");
                    return true;
                }
                return false;
            }
        });

        myWebViw.loadUrl(url);

        ImageButton backBtn = (ImageButton) this.activity.findViewById(R.id.backBtn);
        backBtn.setOnClickListener((view -> {
            if (myWebViw.canGoBack()) {
                myWebViw.goBack();
            } else if(!historyList.isEmpty() && historyList.size() >= 2){
                myWebViw.loadUrl(historyList.get(historyList.size() - 2).getUrl());
            }
        }));

        ImageButton forwardBtn = (ImageButton) this.activity.findViewById(R.id.forwardBtn);
        forwardBtn.setOnClickListener((view -> {
            if (myWebViw.canGoForward()) {
                myWebViw.goForward();
            }
        }));

        ImageButton historyBtn = (ImageButton) this.activity.findViewById(R.id.historyBtn);
        historyBtn.setOnClickListener((view -> {
            setupHistoryScreen();

        }));

        ImageButton bookmarkBtn = (ImageButton) this.activity.findViewById(R.id.bookmarkBtn);
        bookmarkBtn.setOnClickListener((view -> {
            setupBookmarkScreen();

        }));

        ImageButton settingBtn = (ImageButton) this.activity.findViewById(R.id.settingBtn);
        settingBtn.setOnClickListener((view -> {
            SharedPreferences sharedPreferences = activity.getSharedPreferences("MyPrefs", MODE_PRIVATE);
            int myTheme = sharedPreferences.getInt("MyTheme", 0);
            myTheme = (myTheme + 1) % 5;

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("MyTheme", myTheme);
            editor.apply();
            activity.recreate();

        }));

        ImageButton homeBtn = (ImageButton) this.activity.findViewById(R.id.homeBtn);
        homeBtn.setOnClickListener((view -> {
            SharedPreferences sharedPreferences = activity.getSharedPreferences("MyPrefs", MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("myHomeUrl", currentUrl);
            editor.apply();

            Toast.makeText(activity, "Set Homescreen Successfully", Toast.LENGTH_SHORT).show();

        }));



    }

    private void setupHistoryScreen() {
        activity.setContentView(R.layout.history_layout);

        ListView list = (ListView) this.activity.findViewById(R.id.history_listView);

//        WebBackForwardList historyList = myWebViw.copyBackForwardList();
//        int totalHistoryItems = historyList.getSize();
//
//        for (int i = 0; i < totalHistoryItems; i++) {
//            WebHistoryItem historyItem = historyList.getItemAtIndex(i);
//            String url = historyItem.getUrl();
//            String title = historyItem.getTitle();
//            Bitmap icon = historyItem.getFavicon();
//
//            this.historyList.add(new Message(title, url, icon));
//        }

        ListViewAdapter listAdapter = new ListViewAdapter(activity, this.historyList);

        list.setAdapter(listAdapter);
        list.setOnItemClickListener((parent, view, position, id) -> {
            setupHomeScreen(this.historyList.get(position).getUrl());
        });

        ImageButton backBtn = (ImageButton) this.activity.findViewById(R.id.backBtn);
        backBtn.setOnClickListener((view -> {
            setupHomeScreen(currentUrl);
        }));

        ImageButton bookmarkBtn = (ImageButton) this.activity.findViewById(R.id.bookmarkBtn);
        bookmarkBtn.setOnClickListener((view -> {
            setupBookmarkScreen();

        }));

    }

    public void setupBookmarkScreen(){
        activity.setContentView(R.layout.bookmark_layout);
        ListView list = (ListView) this.activity.findViewById(R.id.bookmark_listView);
        ListViewAdapter listAdapter = new ListViewAdapter(activity, this.bookmarkList);

        list.setAdapter(listAdapter);
        list.setOnItemClickListener((parent, view, position, id) -> {
            setupHomeScreen(this.bookmarkList.get(position).getUrl());
        });

        ImageButton backBtn = (ImageButton) this.activity.findViewById(R.id.backBtn);
        backBtn.setOnClickListener((view -> {
            setupHomeScreen(currentUrl);
        }));

        ImageButton historyBtn = (ImageButton) this.activity.findViewById(R.id.historyBtn);
        historyBtn.setOnClickListener((view -> {
            setupHistoryScreen();

        }));

    }

    public void onBackPress() {
        Log.d("ONBACK", "Loading previous site if exists");
        if (myWebViw.canGoBack()) {
            myWebViw.goBack();
        } else {
            Toast.makeText(activity, activity.getString(R.string.shut_down), Toast.LENGTH_LONG).show();
            activity.finish();
        }
    }

}
