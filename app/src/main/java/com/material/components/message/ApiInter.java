package com.material.components.message;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;

public interface ApiInter {

    @Headers({"Authorization: key=AAAAT_WETJk:APA91bFt_G_D8BGWrQsvv0l4FYRAh-TvaIhUlUROYVPSBGxoR8VYzS1ugVgM2eElUCfBMBZFZSmvJjcelA8TjsKbd2qbY2Fq2wbYSsPksd4FHQwA5Nz-VyhJHme5By1Of7vgle6DK-x1","Content-Type:application/json"})
    @POST("fcm/send")
    Call<ResponseBody> sendChatNotification(@Body RequestNotificaton requestNotificaton);


    @Headers({"Authorization: key=AAAAT_WETJk:APA91bFt_G_D8BGWrQsvv0l4FYRAh-TvaIhUlUROYVPSBGxoR8VYzS1ugVgM2eElUCfBMBZFZSmvJjcelA8TjsKbd2qbY2Fq2wbYSsPksd4FHQwA5Nz-VyhJHme5By1Of7vgle6DK-x1","Content-Type:application/json"})
    @POST("send")
    Call<ResponseBody> sendNotification(
            @Field("token") String token,
            @Field("title") String title,
            @Field("body") String body
    );
}
