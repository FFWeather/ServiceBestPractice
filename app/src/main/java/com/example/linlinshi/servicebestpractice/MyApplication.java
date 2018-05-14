package com.example.linlinshi.servicebestpractice;

import android.app.Application;
import android.content.Context;

/**
 * Created by linlinshi on 2018/5/14.
 */

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }
}
