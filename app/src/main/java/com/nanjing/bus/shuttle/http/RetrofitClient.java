package com.nanjing.bus.shuttle.http;

import com.nanjing.bus.shuttle.http.converter.StringConverterFactory;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;


/**
 * Retrofit配置
 * Created by 剑指锁妖塔 on 2016/4/26.
 */
public class RetrofitClient {

    private static final String baseUrl = "http://www.fylder.me:8099/";
    // private static final String baseUrl = "http://192.168.1.106:8080/photo/";

    /**
     * 获取一个ApiService
     */
    public static <T> T getInstance(final Class<T> service) {
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())   //RxJava回调
                .addConverterFactory(new StringConverterFactory())          //解析String
                .addConverterFactory(JacksonConverterFactory.create())      //jackson解析json
                //.addConverterFactory(GsonConverterFactory.create())       //gson解析json
                .baseUrl(baseUrl)
                .build();
        return retrofit.create(service);
    }

}
