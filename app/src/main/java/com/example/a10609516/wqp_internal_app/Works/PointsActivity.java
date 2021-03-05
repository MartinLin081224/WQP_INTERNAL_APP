package com.example.a10609516.wqp_internal_app.Works;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a10609516.wqp_internal_app.Basic.LoginActivity;
import com.example.a10609516.wqp_internal_app.R;
import com.example.a10609516.wqp_internal_app.Tools.CirclePgBar;
import com.example.a10609516.wqp_internal_app.Tools.WQPClickListener;
import com.example.a10609516.wqp_internal_app.Tools.WQPToolsActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PointsActivity extends WQPToolsActivity {

    private SharedPreferences sp;

    private LinearLayout nav_view;
    private DrawerLayout drawer;
    private Toolbar toolbar;

    private CirclePgBar money_pgr, a_points_pgr, b_points_pgr, d_points_pgr, ab_points_pgr;

    private TextView local_txt, u_id_txt, user_txt;
    private String a_points, b_points, d_points, ab_points, money;
    private Float a_points_count, b_points_count, d_points_count, ab_points_count, money_count;

    private String LOG = "PointsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points);
        //取得控制項物件
        initViews();
        //Menu的onClickListener
        MenuListener();
        //設置Toolbar
        SetToolBar();
        //與OKHttp連線(UserName.php)
        sendRequestWithOkHttpForUserName();
        //與OKHttp連線(UserLocal.php)
        sendRequestWithOkHttpForUserLocal();
        //與OKHttp連線(WorkAllPoints.php)
        sendRequestWithOkHttpForWorkAllPoints();
    }

    /**
     * 取得控制項物件
     */
    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        nav_view = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer_layout);
        local_txt = (TextView) findViewById(R.id.local_txt);
        u_id_txt = (TextView) findViewById(R.id.u_id_txt);
        user_txt = (TextView) findViewById(R.id.user_txt);
        money_pgr = (CirclePgBar) findViewById(R.id.money_pgr);
        a_points_pgr = (CirclePgBar) findViewById(R.id.a_points_pgr);
        b_points_pgr = (CirclePgBar) findViewById(R.id.b_points_pgr);
        d_points_pgr = (CirclePgBar) findViewById(R.id.d_points_pgr);
        ab_points_pgr = (CirclePgBar) findViewById(R.id.ab_points_pgr);

        //接收LoginActivity傳過來的值
        SharedPreferences user_id = getSharedPreferences("user_id", MODE_PRIVATE);
        String user_id_data = user_id.getString("ID", "");
        Log.i(LOG, user_id_data);
        u_id_txt.setText("員編 : " + user_id_data);
    }

    /**
     * Menu的onClickListener
     */
    private void MenuListener() {
        TextView home_txt = findViewById(R.id.home_txt);
        home_txt.setOnClickListener(new WQPClickListener());
        TextView exchange_txt = findViewById(R.id.exchange_txt);
        exchange_txt.setOnClickListener(new WQPClickListener());
        TextView schedule_txt = findViewById(R.id.schedule_txt);
        schedule_txt.setOnClickListener(new WQPClickListener());
        TextView calendar_txt = findViewById(R.id.calendar_txt);
        calendar_txt.setOnClickListener(new WQPClickListener());
        TextView mission_txt = findViewById(R.id.mission_txt);
        mission_txt.setOnClickListener(new WQPClickListener());
        TextView bonus_txt = findViewById(R.id.bonus_txt);
        bonus_txt.setOnClickListener(new WQPClickListener());
        TextView points_txt = findViewById(R.id.points_txt);
        points_txt.setOnClickListener(new WQPClickListener());
        TextView miss_report_txt = findViewById(R.id.miss_report_txt);
        miss_report_txt.setOnClickListener(new WQPClickListener());
        TextView gps_txt = findViewById(R.id.gps_txt);
        gps_txt.setOnClickListener(new WQPClickListener());
        TextView quotation_txt = findViewById(R.id.quotation_txt);
        quotation_txt.setOnClickListener(new WQPClickListener());
        TextView report_txt = findViewById(R.id.report_txt);
        report_txt.setOnClickListener(new WQPClickListener());
        TextView report_search_txt = findViewById(R.id.report_search_txt);
        report_search_txt.setOnClickListener(new WQPClickListener());
        TextView inventory_txt = findViewById(R.id.inventory_txt);
        inventory_txt.setOnClickListener(new WQPClickListener());
        TextView picking_txt = findViewById(R.id.picking_txt);
        picking_txt.setOnClickListener(new WQPClickListener());
        TextView requisition_txt = findViewById(R.id.requisition_txt);
        requisition_txt.setOnClickListener(new WQPClickListener());
        TextView progress_txt = findViewById(R.id.progress_txt);
        progress_txt.setOnClickListener(new WQPClickListener());
        TextView version_info_txt = findViewById(R.id.version_info_txt);
        version_info_txt.setOnClickListener(new WQPClickListener());
    }

    /**
     * 設置Toolbar
     */
    private void SetToolBar() {
        TextView log_out_txt = findViewById(R.id.log_out_txt);
        TextView id_txt = findViewById(R.id.id_txt);

        //接收LoginActivity傳過來的值
        SharedPreferences user_name = getSharedPreferences("user_name", MODE_PRIVATE);
        String U_name = user_name.getString("U_name", "");
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                id_txt.setText(U_name);
            }
        });

        toolbar.setTitle("點數總覽");
        toolbar.setNavigationIcon(R.drawable.icon_menu);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(Gravity.LEFT); //CLOSE Nav Drawer!
                    Log.d(LOG, "Drawer : close");
                }else{
                    drawer.openDrawer(Gravity.LEFT); //OPEN Nav Drawer!
                    Log.d(LOG, "Drawer : open");
                }
            }
        });

        log_out_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                //清除登入界面的自動燈入和記住密碼的代碼
                sp.edit().putBoolean("auto_isCheck",false).commit();
                sp.edit().putBoolean("rem_isCheck",false).commit();
                sp.edit().putString("USER_NAME", "").commit();
                sp.edit().putString("PASSWORD", "").commit();
                sp.edit().putString("user_name", "").commit();

                finish();
                Intent intent_login = new Intent(PointsActivity.this, LoginActivity.class);
                startActivity(intent_login);
            }
        });
    }

    /**
     * 與資料庫連線 藉由登入輸入的員工ID取得員工姓名
     */
    private void sendRequestWithOkHttpForUserName() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //接收LoginActivity傳過來的值
                SharedPreferences user_id = getSharedPreferences("user_id", MODE_PRIVATE);
                String user_id_data = user_id.getString("ID", "");
                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("U_ACC", user_id_data)
                            .build();
                    Request request = new Request.Builder()
                            //.url("http://192.168.0.172/WQP_OS/UserName.php")
                            .url("http://a.wqp-water.com.tw/WQP_OS/UserName.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.i(LOG, responseData);
                    parseJSONWithJSONObjectOfUserName(responseData);
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
    private void parseJSONWithJSONObjectOfUserName(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON格式改為字串
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String user_name = jsonObject.getString("MV002");
                user_txt.setText("工務 : " + user_name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 與資料庫連線 藉由登入輸入的員工ID取得員工所在地區
     */
    private void sendRequestWithOkHttpForUserLocal() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //接收LoginActivity傳過來的值
                SharedPreferences user_id = getSharedPreferences("user_id", MODE_PRIVATE);
                String user_id_data = user_id.getString("ID", "");
                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("User_id", user_id_data)
                            .build();
                    Request request = new Request.Builder()
                            //.url("http://192.168.0.172/WQP_OS/UserLocal.php")
                            .url("http://a.wqp-water.com.tw/WQP_OS/UserLocal.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.i(LOG, responseData);
                    parseJSONWithJSONObjectOfUserLocal(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 獲得JSON字串並解析成String字串
     *在TextView上SHOW出回傳的員工所在地區
     * @param jsonData
     */
    private void parseJSONWithJSONObjectOfUserLocal(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON格式改為字串
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String user_local = jsonObject.getString("GROUP_NAME");
                local_txt.setText("地區別 : " + user_local.substring(0,2));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 與OkHttp建立連線_new
     */
    private void sendRequestWithOkHttpForWorkAllPoints() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //接收LoginActivity傳過來的值
                SharedPreferences user_id = getSharedPreferences("user_id", MODE_PRIVATE);
                String user_id_data = user_id.getString("ID", "");
                Log.e(LOG, user_id_data);

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("User_id", user_id_data)
                            .build();
                    Log.e(LOG, user_id_data);
                    Request request = new Request.Builder()
                            //.url("http://192.168.0.172/WQP_OS/WorkAllPoints.php")
                            .url("http://a.wqp-water.com.tw/WQP_OS/WorkAllPoints.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectForWorkAllPoints(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 獲得JSON字串並解析成String字串
     *
     * @param jsonData
     */
    private void parseJSONWithJSONObjectForWorkAllPoints(String jsonData) {
        Log.e("ENTER", "YES");
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON格式改為字串
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                a_points = jsonObject.getString("A_POINTS");
                b_points = jsonObject.getString("B_POINTS");
                d_points = jsonObject.getString("D_POINTS");
                ab_points = jsonObject.getString("AB_POINTS_SUM");
                money = jsonObject.getString("ASSIGN_MONEY");

                Log.e(LOG, a_points);
                Log.e(LOG, b_points);
                Log.e(LOG, d_points);
                Log.e(LOG, ab_points);
                Log.e(LOG, money);

                a_points_count = Float.parseFloat(a_points);
                b_points_count = Float.parseFloat(b_points);
                d_points_count = Float.parseFloat(d_points.trim());
                ab_points_count = Float.parseFloat(ab_points);
                money_count = Float.parseFloat(money.trim());

                money_pgr.setmCirclePgBar(0, money_count, 6000, Color.rgb(244, 164, 96));
                money_pgr.invalidate();
                a_points_pgr.setmCirclePgBar(0, a_points_count, 5000, Color.rgb(227, 38, 54));
                a_points_pgr.invalidate();
                b_points_pgr.setmCirclePgBar(0, b_points_count, 5000, Color.rgb(115, 230, 140));
                b_points_pgr.invalidate();
                d_points_pgr.setmCirclePgBar(0, d_points_count, 200, Color.rgb(30, 144, 255));
                d_points_pgr.invalidate();
                ab_points_pgr.setmCirclePgBar(0, ab_points_count, 8000, Color.rgb(255, 255, 0));
                ab_points_pgr.invalidate();
                Log.e("OVER", "YES");
            }
        } catch (Exception e) {
            Log.e("CATCH", "YES");
            e.printStackTrace();
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