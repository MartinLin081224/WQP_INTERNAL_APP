package com.example.a10609516.wqp_internal_app.Basic;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a10609516.wqp_internal_app.R;
import com.example.a10609516.wqp_internal_app.Tools.WQPClickListener;
import com.example.a10609516.wqp_internal_app.Tools.WQPToolsActivity;
import com.example.a10609516.wqp_internal_app.Works.CalendarActivity;
import com.example.a10609516.wqp_internal_app.Works.EngPointsActivity;
import com.example.a10609516.wqp_internal_app.Works.MissionActivity;
import com.example.a10609516.wqp_internal_app.Works.PointsActivity;
import com.example.a10609516.wqp_internal_app.Works.ScheduleActivity;

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

public class HomeActivity extends WQPToolsActivity {

    private SharedPreferences sp;
    private TextView log_out_txt;

    private LinearLayout nav_view;
    private DrawerLayout drawer;
    private Toolbar toolbar;

    private ImageView schedule_imv, calendar_imv, mission_imv, bonus_imv, points_imv;
    private String user_id_data;

    private String home, exchange, schedule, calendar, mission,
            bonus, points, miss_report, gps, quotation,
            report, report_search, inventory, picking, requisition,
            progress, version_info;

    private Context mContext = this;
    private String LOG = "HomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(LOG, "onCreate");
        setContentView(R.layout.activity_home);
        //取得控制項物件
        initViews();
        //Menu的onClickListener
        MenuListener();
        //設置Toolbar
        SetToolBar();
        //取得TokenID的OKHttp
        sendRequestWithOkHttpOfTokenID();
        //與OkHttp建立連線(Menu權限)
        sendRequestWithOkHttpForMenuAuthority();
        //與OKHttp連線(藉由登入輸入的員工ID取得員工姓名)
        sendRequestWithOkHttpForUserName();
    }

    //取得控制項物件
    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        nav_view = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer_layout);
        schedule_imv = findViewById(R.id.schedule_imv);
        calendar_imv = findViewById(R.id.calendar_imv);
        mission_imv = findViewById(R.id.mission_imv);
        bonus_imv = findViewById(R.id.bonus_imv);
        points_imv = findViewById(R.id.points_imv);

        schedule_imv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(HomeActivity.this, ScheduleActivity.class);
                startActivity(intent1);
            }
        });

        calendar_imv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(HomeActivity.this, CalendarActivity.class);
                startActivity(intent2);
            }
        });

        mission_imv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(HomeActivity.this, MissionActivity.class);
                startActivity(intent3);
            }
        });

        bonus_imv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent4 = new Intent(HomeActivity.this, PointsActivity.class);
                startActivity(intent4);
            }
        });

        points_imv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent5 = new Intent(HomeActivity.this, EngPointsActivity.class);
                startActivity(intent5);
            }
        });

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
        log_out_txt = findViewById(R.id.log_out_txt);
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

        toolbar.setTitle("首頁");
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
                sp = HomeActivity.this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                //清除登入界面的自動燈入和記住密碼的代碼
                sp.edit().putBoolean("auto_isCheck",false).commit();
                sp.edit().putBoolean("rem_isCheck",false).commit();
                sp.edit().putString("USER_NAME", "").commit();
                sp.edit().putString("PASSWORD", "").commit();
                sp.edit().putString("user_name", "").commit();

                finish();
                Intent intent_login = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent_login);
            }
        });
    }

    /**
     * 與OKHttp連線(藉由登入輸入的員工ID取得員工姓名)
     */
    private void sendRequestWithOkHttpForUserName() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences user_id = getSharedPreferences("user_id", MODE_PRIVATE);
                user_id_data = user_id.getString("ID", "");
                Log.e(LOG, "U_ACC:" + user_id_data);
                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("U_ACC", user_id_data)
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
                String user_name = jsonObject.getString("MV002");
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
                //接收LoginActivity傳過來的值
                SharedPreferences user_id = getSharedPreferences("user_id", MODE_PRIVATE);
                user_id_data = user_id.getString("ID", "");
                Log.e(LOG, "U_ACC:" + user_id_data);
                //接收LoginActivity傳過來的值
                SharedPreferences token_id = getSharedPreferences("app_token_id", MODE_PRIVATE);
                String app_token_id = token_id.getString("token_id", "");
                Log.e("FCM", app_token_id);
                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("User_id", user_id_data)
                            .add("Token_ID", app_token_id)
                            .build();
                    Log.e("FCM", user_id_data);
                    Request request = new Request.Builder()
                            //.url("http://192.168.0.172/WQP_OS/TokenID.php")
                            .url("http://a.wqp-water.com.tw/WQP_OS/TokenID.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e("FCM", responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 與OkHttp建立連線(Menu權限)
     */
    private void sendRequestWithOkHttpForMenuAuthority() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //接收LoginActivity傳過來的值
                SharedPreferences user_id = mContext.getSharedPreferences("user_id", MODE_PRIVATE);
                user_id_data = user_id.getString("ID", "");

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("U_ACC", user_id_data)
                            .build();
                    Log.e("MENU", user_id_data);
                    Request request = new Request.Builder()
                            .url("http://192.168.0.172/WQP_OS/UserMenuAuthority.php")
                            //.url("http://a.wqp-water.com.tw/WQP_OS/UserMenuAuthority.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e("MENU", request.toString());
                    Log.e("MENU", requestBody.toString());
                    Log.e("MENU", response.toString());
                    Log.e("MENU", responseData);
                    parseJSONWithJSONObjectForMenuAuthority(responseData);
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
    private void parseJSONWithJSONObjectForMenuAuthority(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON格式改為字串
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                home =jsonObject.getString("HOME");
                exchange =jsonObject.getString("EXCHANGE");
                schedule =jsonObject.getString("SCHEDULE");
                calendar =jsonObject.getString("CALENDAR");
                mission =jsonObject.getString("MISSION");
                bonus =jsonObject.getString("BONUS");
                points =jsonObject.getString("POINTS");
                miss_report =jsonObject.getString("MISS_REPORT");
                gps =jsonObject.getString("GPS");
                quotation =jsonObject.getString("QUOTATION");
                report =jsonObject.getString("REPORT");
                report_search =jsonObject.getString("REPORT_SEARCH");
                inventory =jsonObject.getString("INVENTORY");
                picking =jsonObject.getString("PICKING");
                requisition =jsonObject.getString("REQUISITION");
                progress =jsonObject.getString("PROGRESS");
                version_info =jsonObject.getString("VERSION_INFO");

                Log.e("MENU : home -", home);
                Log.e("MENU : exchange -", exchange);
                Log.e("MENU : schedule -", schedule);
                Log.e("MENU : calendar -", calendar);
                Log.e("MENU : mission -", mission);
                Log.e("MENU : bonus -", bonus);
                Log.e("MENU : points -", points);
                Log.e("MENU : miss_report -", miss_report);
                Log.e("MENU : gps -", gps);
                Log.e("MENU : quotation -", quotation);
                Log.e("MENU : report -", report);
                Log.e("MENU : report_search -", report_search);
                Log.e("MENU : inventory -", inventory);
                Log.e("MENU : picking -", picking);
                Log.e("MENU : requisition -", requisition);
                Log.e("MENU : progress -", progress);
                Log.e("MENU : version_info -", version_info);
            }
        } catch (Exception e) {
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