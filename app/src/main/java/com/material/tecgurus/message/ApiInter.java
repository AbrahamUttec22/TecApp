package com.material.tecgurus.message;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import okhttp3.ResponseBody;
import retrofit2.http.Field;

public interface ApiInter {

    @Headers({"Authorization: key=AAAAIwqBBUA:APA91bEvmyAIGfHoUe5OPRQ7Ut1dID7una1rugIHUbEORyQxm-uuZdaObuzZg4i_x1UXEXvB1D4hwehDOsC0pw9jLZI-tL2pWpJimDAnCUqvmTNZUSp6Kb0XH54imcTwrQ5lEYUhZdIv","Content-Type:application/json"})
    @POST("fcm/send")
    Call<ResponseBody> sendChatNotification(@Body RequestNotificaton requestNotificaton);


    @Headers({"Authorization: key=AAAAIwqBBUA:APA91bEvmyAIGfHoUe5OPRQ7Ut1dID7una1rugIHUbEORyQxm-uuZdaObuzZg4i_x1UXEXvB1D4hwehDOsC0pw9jLZI-tL2pWpJimDAnCUqvmTNZUSp6Kb0XH54imcTwrQ5lEYUhZdIv","Content-Type:application/json"})
    @POST("send")
    Call<ResponseBody> sendNotification(
            @Field("token") String token,
            @Field("title") String title,
            @Field("body") String body
    );
}
