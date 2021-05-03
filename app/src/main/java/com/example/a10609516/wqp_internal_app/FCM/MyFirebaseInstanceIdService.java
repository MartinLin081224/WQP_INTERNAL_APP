package com.example.a10609516.wqp_internal_app.FCM;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.i("FCM", "Token:"+token);

        sendRegistrationToServer(token);

        SendTokenID();

        //sendRequestWithOkHttp();
    }

    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }

    /**
     * 傳遞TokenID到LoginActivity做儲存
     */
    private void SendTokenID(){
        String token = FirebaseInstanceId.getInstance().getToken();

        SharedPreferences sharedPreferences = getSharedPreferences("app_token_id", MODE_PRIVATE);
        sharedPreferences.edit().putString("token_id", token).apply();
    }

}