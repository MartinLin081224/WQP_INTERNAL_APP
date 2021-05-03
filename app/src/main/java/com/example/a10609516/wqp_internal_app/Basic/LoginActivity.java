package com.example.a10609516.wqp_internal_app.Basic;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a10609516.wqp_internal_app.R;

import org.json.JSONArray;
import org.json.JSONObject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private TextView version_txt;
    private EditText id_edt, pwd_edt;
    private CheckBox remember_cbx, auto_cbx;
    private ImageView login_imv;

    private SharedPreferences sp;

    private String userNameValue;
    private String passwordValue;
    private String COUNT, user_name;

    private Context mContext = this;

    private Boolean rem_isCheck;
    private Boolean auto_isCheck;

    private String LOG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(LOG, "onCreate");
        setContentView(R.layout.activity_login);
        //取消ActionBar
        getSupportActionBar().hide();
        //取得控制項物件
        initViews();
        //設定自動登入
        AutoLogin();
        SharedPreferences sharedPreferences = getSharedPreferences("fb_version", MODE_PRIVATE);
        sharedPreferences.edit().putString("FB_VER", version_txt.getText().toString()).apply();
    }

    //取得控制項物件
    private void initViews()
    {
        version_txt = findViewById(R.id.version_txt);
        id_edt = findViewById(R.id.id_edt);
        pwd_edt = findViewById(R.id.pwd_edt);
        remember_cbx = findViewById(R.id.remember_cbx);
        auto_cbx = findViewById(R.id.auto_cbx);
        login_imv = findViewById(R.id.login_imv);

        //登入按鈕監聽方法
        login_imv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (version_txt.getText().toString().equals("1.0.6")) {
                    userNameValue = id_edt.getText().toString();
                    passwordValue = pwd_edt.getText().toString();
                    //取得TokenID的OKHttp
                    sendRequestWithOkHttpOfTokenID();
                    //與OKHttp連線( )藉由登入輸入的員工ID取得員工姓名)
                    sendRequestWithOkHttpForUserName();
                    //與OKHttp連線(查詢輸入的帳號密碼是否正確)
                    sendRequestWithOkHttpForCheckUserAccount();
                } else {
                    Toast.makeText(getApplicationContext(), "請前往網站更新最新版本", Toast.LENGTH_SHORT);
                    new AlertDialog.Builder(mContext)
                            .setTitle("更新通知")
                            .setMessage("檢測到軟體重大更新\n請更新最新版本")
                            .setIcon(R.drawable.bwt_icon)
                            .setNegativeButton("確定",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            new Thread() {
                                                @Override
                                                public void run() {
                                                    super.run();
                                                    Uri uri = Uri.parse("http://m.wqp-water.com.tw/APP");
                                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                                    startActivity(intent);
                                                }
                                            }.start();
                                        }
                                    })
                            .setCancelable(false)
                            .show();
                }

            }
        });
    }

    //設定自動登入
    private void AutoLogin() {
        //打開Preferences，成稱為userInfo，如果存在則打開它，否則創建新的Preferences
        //Context.MODE_PRIVATE：指定該SharedPreferences數據只能被本應用程序讀、寫
        //Context.MODE_WORLD_READABLE：指定該SharedPreferences數據能被其他應用程序讀，但不能寫
        //Context.MODE_WORLD_WRITEABLE：指定該SharedPreferences數據能被其他應用程序讀寫。
        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        rem_isCheck = remember_cbx.isChecked();
        auto_isCheck = auto_cbx.isChecked();

        remember_cbx.setChecked(true);//設置記住密碼初始化為true

        //判斷記住密碼多選框的狀態
        if (sp.getBoolean("rem_isCheck", false)) {
            //設置默認是紀錄密碼狀態
            remember_cbx.setChecked(true);
            id_edt.setText(sp.getString("USER_NAME", ""));
            pwd_edt.setText(sp.getString("PASSWORD", ""));
            Log.e(LOG, "自動恢復保存的的帳號密碼");

            userNameValue = id_edt.getText().toString();
            passwordValue = pwd_edt.getText().toString();

            //判斷記住密碼多選框的狀態
            if (sp.getBoolean("auto_isCheck", false)) {
                //設置默認是自動登入狀態
                auto_cbx.setChecked(true);
                finish();
                //跳轉界面
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                LoginActivity.this.startActivity(intent);
                Toast.makeText(getApplicationContext(), "已自動登入", Toast.LENGTH_SHORT);
                Log.e(LOG, "自動登入");
            }

        }
    }

    /**
     * 與OKHttp連線(查詢輸入的帳號密碼是否正確)
     */
    private void sendRequestWithOkHttpForCheckUserAccount() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                String U_ACC = id_edt.getText().toString();
                String U_PWD = pwd_edt.getText().toString();
                Log.e(LOG, "U_ACC:" + U_ACC);
                Log.e(LOG, "U_PWD:" + U_PWD);

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("U_ACC", U_ACC)
                            .add("U_PWD", U_PWD)
                            .build();
                    Log.e(LOG, U_ACC);
                    Request request = new Request.Builder()
                            .url("http://a.wqp-water.com.tw/WQP_OS/CheckUserAccount.php")
                            //.url("http://192.168.0.172/WQP_OS/CheckUserAccount.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, requestBody.toString());
                    Log.e(LOG, response.toString());
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectForCheckAccount(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 取得帳號密碼是否正確
     *
     * @param jsonData
     */
    private void parseJSONWithJSONObjectForCheckAccount(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON格式改為字串
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                COUNT = jsonObject.getString("COUNT");
                Log.e(LOG, "COUNT123:" + COUNT);

                LoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (COUNT.equals("1")) {
                            Toast.makeText(LoginActivity.this, "登入成功", Toast.LENGTH_SHORT).show();
                            //登入成功和記住密碼框為選中狀態才保存用戶信息
                            if (remember_cbx.isChecked()) {
                                //記住用戶名、密碼
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("USER_NAME", userNameValue);
                                editor.putString("PASSWORD", passwordValue);
                                editor.putBoolean("rem_isCheck", rem_isCheck);
                                editor.putBoolean("auto_isCheck", auto_isCheck);
                                editor.apply();

                                Log.e(LOG + "選中保存密碼", "帳號：" + userNameValue +
                                        "\n" + "密碼：" + passwordValue +
                                        "\n" + "是否記住密碼：" + rem_isCheck +
                                        "\n" + "是否自動登入：" + auto_isCheck);
                            }
                            finish();
                            //將sharedPreferences儲存起來，讓所有Activity皆可使用
                            SharedPreferences sharedPreferences_id = getSharedPreferences("user_id", MODE_PRIVATE);
                            sharedPreferences_id.edit().putString("ID", id_edt.getText().toString()).apply();

                            //跳轉界面
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            LoginActivity.this.startActivity(intent);
                            //finish();

                        } else {
                            Toast.makeText(LoginActivity.this, "用戶名或密碼錯誤，請重新登入", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 與OKHttp連線( )藉由登入輸入的員工ID取得員工姓名)
     */
    private void sendRequestWithOkHttpForUserName() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String U_ACC = id_edt.getText().toString();
                Log.e(LOG, "U_ACC:" + U_ACC);
                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("U_ACC", U_ACC)
                            .build();
                    Request request = new Request.Builder()
                            .url("http://a.wqp-water.com.tw/WQP_OS/UserName.php")
                            //.url("http://192.168.0.172/WQP_OS/UserName.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.i(LOG, requestBody.toString());
                    Log.i(LOG, response.toString());
                    Log.i(LOG, responseData);
                    parseJSONWithJSONObjectForUserName(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 獲得JSON字串並解析成String字串
     *在TextView上SHOW出回傳的員工姓名
     * @param jsonData
     */
    private void parseJSONWithJSONObjectForUserName(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON格式改為字串
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                user_name = jsonObject.getString("MV002");
                SharedPreferences sharedPreferences_name = getSharedPreferences("user_name", MODE_PRIVATE);
                sharedPreferences_name.edit().putString("U_name", user_name).apply();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 與OkHttp建立連線(TokenID)
     */
    private void sendRequestWithOkHttpOfTokenID() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String U_ACC = id_edt.getText().toString();
                Log.e(LOG, "U_ACC:" + U_ACC);
                //接收LoginActivity傳過來的值
                SharedPreferences token_id = getSharedPreferences("app_token_id", MODE_PRIVATE);
                String app_token_id = token_id.getString("token_id", "");
                Log.e("FCM", app_token_id);
                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("User_id", U_ACC)
                            .add("Token_ID", app_token_id)
                            .build();
                    Log.e("FCM", U_ACC);
                    Request request = new Request.Builder()
                            //.url("http://192.168.0.172/WQP_OS/TokenID.php")
                            .url("http://a.wqp-water.com.tw/WQP_OS/TokenID.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.i("FCM", responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 自動變更圖片適中大小
     */
    private void ResizeIcon() {
        /*final float density = getResources().getDisplayMetrics().density;
        final int width = Math.round(44 * density);
        final int height = Math.round(44 * density);*/

        Log.e(LOG, String.valueOf(id_edt.getMeasuredHeight()));

        Drawable drawable1 = getResources().getDrawable(R.drawable.ic_user);
        drawable1.setBounds(0, 0, id_edt.getHeight(), id_edt.getHeight());//第一0是距左邊距离，第二0是距上邊距离，getHeight()分别是EditText長寬
        id_edt.setCompoundDrawables(drawable1, null, null, null);//只放左邊
        Drawable drawable2 = getResources().getDrawable(R.drawable.ic_password);
        drawable2.setBounds(0, 0, pwd_edt.getHeight(), pwd_edt.getHeight());//第一0是距左邊距离，第二0是距上邊距离，getHeight()分别是EditText長寬
        pwd_edt.setCompoundDrawables(drawable2, null, null, null);//只放左邊
    }

    /**
     * Activity窗口獲得或失去焦點時被调用,在onResume之後或onPause之後
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            Log.i(LOG, "onWindowFocusChanged is called and" + "hasFocus is true");
            //自動變更圖片適中大小
            ResizeIcon();
        }else {
            Log.i(LOG, "onWindowFocusChanged is called and" + "hasFocus is false");
            //自動變更圖片適中大小
            ResizeIcon();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(LOG, "onRestart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(LOG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(LOG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(LOG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(LOG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(LOG, "onDestroy");
    }
}