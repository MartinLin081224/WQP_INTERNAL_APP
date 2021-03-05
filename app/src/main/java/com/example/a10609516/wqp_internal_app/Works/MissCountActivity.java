package com.example.a10609516.wqp_internal_app.Works;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a10609516.wqp_internal_app.Basic.LoginActivity;
import com.example.a10609516.wqp_internal_app.R;
import com.example.a10609516.wqp_internal_app.Tools.WQPClickListener;
import com.example.a10609516.wqp_internal_app.Tools.WQPToolsActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MissCountActivity extends WQPToolsActivity {

    private SharedPreferences sp;

    private LinearLayout miss_llt;
    private LinearLayout nav_view;
    private DrawerLayout drawer;
    private Toolbar toolbar;

    private Context mContext = this;
    private String LOG = "MissCountActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miss_count);
        //取得控制項物件
        initViews();
        //Menu的onClickListener
        MenuListener();
        //設置Toolbar
        SetToolBar();
        //與OKHttp連線(GroupMissCount)工務未回報派工數量
        sendRequestWithOkHttpForMissCount();
    }

    /**
     * 取得控制項物件
     */
    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        nav_view = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer_layout);
        miss_llt = (LinearLayout) findViewById(R.id.miss_llt);
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

    //設置Toolbar
    private void SetToolBar() {
        TextView log_out_txt = findViewById(R.id.log_out_txt);
        TextView id_txt = findViewById(R.id.id_txt);
        id_txt.setText("林裕淵");

        toolbar.setTitle("各區未回報數量");
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

                finish();
                Intent intent_login = new Intent(MissCountActivity.this, LoginActivity.class);
                startActivity(intent_login);
            }
        });
    }

    /**
     * 與OkHttp建立連線
     */
    private void sendRequestWithOkHttpForMissCount() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //接收LoginActivity傳過來的值
                SharedPreferences user_id = getSharedPreferences("user_id", MODE_PRIVATE);
                String user_id_data = user_id.getString("ID", "");
                Log.i(LOG, user_id_data);
                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("U_ACC", user_id_data)
                            .build();
                    Log.i(LOG, user_id_data);
                    Request request = new Request.Builder()
                            //.url("http://192.168.0.172/WQP_OS/MissionGroupMissCount.php")
                            .url("http://a.wqp-water.com.tw/WQP_OS/MissionGroupMissCount.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.i(LOG, responseData);
                    parseJSONWithJSONObjectForMissCount(responseData);
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
    private void parseJSONWithJSONObjectForMissCount(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON格式改為字串
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String eng_local = jsonObject.getString("LOCAL_PLACE");
                String eng_name = jsonObject.getString("ENG_EMP");
                String miss_count = jsonObject.getString("MISSED");

                Log.e(LOG, eng_name);

                //JSONArray加入SearchData資料
                ArrayList<String> JArrayList = new ArrayList<String>();
                JArrayList.add(eng_local);
                JArrayList.add(eng_name);
                JArrayList.add(miss_count);

                //HandlerMessage更新UI
                Bundle b = new Bundle();
                b.putStringArrayList("JSON_data", JArrayList);
                Message msg = mHandler.obtainMessage();
                msg.setData(b);
                msg.what = 1;
                msg.sendToTarget();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新UI
     */
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    LinearLayout small_llt = new LinearLayout(MissCountActivity.this);
                    small_llt.setOrientation(LinearLayout.HORIZONTAL);

                    Bundle jb = msg.getData();
                    ArrayList<String> JArrayList = new ArrayList<String>();
                    //int i = b.getStringArrayList("JSON_data").size();
                    JArrayList = jb.getStringArrayList("JSON_data");

                    //顯示每筆LinearLayout的地區
                    TextView dynamically_local;
                    dynamically_local = new TextView(MissCountActivity.this);
                    dynamically_local.setText(JArrayList.get(0).substring(0, 2));
                    dynamically_local.setGravity(Gravity.CENTER);
                    //dynamically_local.setWidth(50);
                    dynamically_local.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                    dynamically_local.setTextColor(mContext.getResources().getColor(R.color.WQP_White));

                    //顯示每筆LinearLayout的工務
                    TextView dynamically_name;
                    dynamically_name = new TextView(MissCountActivity.this);
                    dynamically_name.setText(JArrayList.get(1));
                    dynamically_name.setGravity(Gravity.CENTER);
                    //dynamically_name.setWidth(50);
                    dynamically_name.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                    dynamically_name.setTextColor(mContext.getResources().getColor(R.color.WQP_White));

                    //顯示每筆LinearLayout的數量
                    TextView dynamically_count;
                    dynamically_count = new TextView(MissCountActivity.this);
                    dynamically_count.setText(JArrayList.get(2));
                    dynamically_count.setGravity(Gravity.CENTER);
                    dynamically_count.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                    dynamically_count.setTextColor(mContext.getResources().getColor(R.color.WQP_White));

                    //設置每筆TableLayout的分隔線
                    TextView dynamically_txt = new TextView(MissCountActivity.this);
                    dynamically_txt.setBackgroundColor(Color.rgb(220, 220, 220));

                    LinearLayout.LayoutParams small_pm1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f);
                    LinearLayout.LayoutParams small_pm2 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                    small_llt.addView(dynamically_local, small_pm1);
                    small_llt.addView(dynamically_name, small_pm2);
                    small_llt.addView(dynamically_count, small_pm2);

                    miss_llt.addView(dynamically_txt, LinearLayout.LayoutParams.MATCH_PARENT, 3);
                    miss_llt.addView(small_llt);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

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