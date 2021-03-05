package com.example.a10609516.wqp_internal_app.FCM;

/**
 * Created by 10609516 on 2020/12/02.
 */

public class MyNotice {
    private static final MyNotice ourInstance = new MyNotice();

    public static MyNotice getInstance() {
        return ourInstance;
    }

    private MyNotice() {
    }

    private OnMessageReceivedListener mOnMessageReceivedListener;
    public interface OnMessageReceivedListener{
        void onMessageReceived(String s);
    }
    public void setOnMessageReceivedListener(OnMessageReceivedListener listener){
        mOnMessageReceivedListener = listener;
    }
    public void notifyOnMessageReceived(String s){
        if(mOnMessageReceivedListener != null){
            mOnMessageReceivedListener.onMessageReceived(s);
        }
    }
}
