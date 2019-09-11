package com.material.tecgurus.message;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.material.tecgurus.R;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class PruevaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueva);
        sendNotificationToPatner();
        //sendNotification();
    }


    private void sendNotificationToPatner() {
        Notification notification = new Notification("check", "i miss you");
        RequestNotificaton requestNotificaton = new RequestNotificaton();
        //token is id , whom you want to send notification ,
        String token = "daEU6FTj5tc:APA91bHHZQ8kKztxEunn8Yz6-n1cOxXwAZeLTH3gkRBaTPxuW6eQIDPxZaP31rR7bnIT8zoy6MhUSJHIOYjcdnKyp1ADGVbjDeBuAi7Cdnq3yjF3lUbZG9F84uLA9suMRqi6qHZ0ubqN";
        requestNotificaton.setToken(token);
        requestNotificaton.setNotification(notification);
        ApiInter apiService = ApiClient.getClient().create(ApiInter.class);
        Call<ResponseBody> responseBodyCall = apiService.sendChatNotification(requestNotificaton);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.w("EL MENSAJE", "DONE" + call.toString());
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }


    private void sendNotification() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://fcm.googleapis.com/fcm/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInter api = retrofit.create(ApiInter.class);
        String token = "daEU6FTj5tc:APA91bHHZQ8kKztxEunn8Yz6-n1cOxXwAZeLTH3gkRBaTPxuW6eQIDPxZaP31rR7bnIT8zoy6MhUSJHIOYjcdnKyp1ADGVbjDeBuAi7Cdnq3yjF3lUbZG9F84uLA9suMRqi6qHZ0ubqN";
        String title = "great match", body = "partiditos";
        Call<ResponseBody> call = api.sendNotification(token, title, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Toast.makeText(PruevaActivity.this, response.body().string(), Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }
}
