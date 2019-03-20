package com.shid.chat.Fragments;

import com.shid.chat.Notifications.MyResponse;
import com.shid.chat.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAb2GXolM:APA91bG98-WytzUd1450pjhYJd7TH-wumidN2hAw74tWV_HfxdO9-je-3p0FMm2oP9rF_GeblmzMwYfYwavRaNd8zYzVFJa0cmWNJLpm30yfvdq-g8u164qwtXEJnjQHhwT2AP-bW0WR"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
