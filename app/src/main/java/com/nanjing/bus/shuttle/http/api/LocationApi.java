package com.nanjing.bus.shuttle.http.api;

import com.fasterxml.jackson.annotation.JsonRawValue;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by fylder on 2017/3/11.
 */

public interface LocationApi {

    /**
     * 上传位置
     *
     * @param line  线路
     * @param lat   纬度
     * @param lng   经度
     * @param time  时间戳 秒
     * @param speed 速度
     */
    @Headers({"Content-Type: application/json"})
    @FormUrlEncoded
    @POST("busPosition")
    Observable<String> test(@Field("line") String line, @Field("lat") double lat, @Field("lng") double lng, @Field("time") long time, @Field("speed") float speed);


    @Headers({"Content-Type: application/json"})
    @JsonRawValue
    @POST("busPosition")
    Observable<String> uploadLocation(@Body RequestBody requestBody);
}
