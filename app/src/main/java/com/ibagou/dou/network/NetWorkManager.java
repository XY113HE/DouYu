package com.ibagou.dou.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import com.ibagou.dou.network.api.DouYuNetApi;

/**
 * Created by sdbean-zlh on 16/5/20.
 */
public class NetWorkManager {
//    public static final String DOUYU_URL_BASE = "http://werewolf.53site.com/ScriptKill/";
    public static final String DOUYU_URL_BASE = "http://dou.ibagou.com/api/";
    public static final String DOUYU_VERSION = "v1/";

    private static NetWorkManager netWorkManager;
    private OkHttpClient okHttpClient;
    private DouYuNetApi douYuNetApi;

    private static Converter.Factory gsonConverterFactory;
    private static CallAdapter.Factory rxJavaCallAdapterFactory;

    public static NetWorkManager getInstance() {
        if (netWorkManager == null) {
            netWorkManager = new NetWorkManager();
        }
        return netWorkManager;
    }

    private NetWorkManager() {
        HttpLoggingInterceptor httpLoggingInterceptor=  new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE);
        this.okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();
        gsonConverterFactory = GsonConverterFactory.create();
        rxJavaCallAdapterFactory = RxJava2CallAdapterFactory.create();
    }

    public DouYuNetApi getDouYuNetApi() {
        if (douYuNetApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(DOUYU_URL_BASE)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            douYuNetApi = retrofit.create(DouYuNetApi.class);
        }
        return douYuNetApi;
    }

}
