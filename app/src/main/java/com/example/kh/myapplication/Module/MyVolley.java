package com.example.kh.myapplication.Module;

import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;

/**
 * Created by kh on 6/19/2017.
 */

public class MyVolley {
    private static  MyVolley myVolley;
    private Context context;
    private RequestQueue requestQueue;

    private MyVolley (Context context){
        this.context = context;
    }

    public synchronized static MyVolley getInstance(Context context){
        if(myVolley==null){
            myVolley = new MyVolley(context);
        }
        return myVolley;
    }

    public void startVolley(){
        Network network = new BasicNetwork(new HurlStack());
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024*1024);
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();
    }

    public void stopVolley(){
        requestQueue.start();
    }

    public <T> void addRequestVolley(Request<T> request){
        requestQueue.add(request);
    }
}
