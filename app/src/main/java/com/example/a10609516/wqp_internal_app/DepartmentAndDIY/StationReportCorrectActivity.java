package com.example.a10609516.wqp_internal_app.DepartmentAndDIY;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a10609516.wqp_internal_app.Basic.LoginActivity;
import com.example.a10609516.wqp_internal_app.R;
import com.example.a10609516.wqp_internal_app.Tools.WQPClickListener;
import com.example.a10609516.wqp_internal_app.Tools.WQPToolsActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StationReportCorrectActivity extends WQPToolsActivity {

    private SharedPreferences sp;

    private LinearLayout nav_view;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private TextView id_txt, product_txt;
    private EditText count_edt, amount_edt;
    private Button check_btn, cancel_btn, delete_btn, delete_check_btn;

    private String LOG = "StationReportCorrectActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_report_correct);
        //取得控制項物件
        initViews();
        //Menu的onClickListener
        MenuListener();
        //設置Toolbar
        SetToolBar();
        //取得OrderSearchActivity傳遞過來的值
        GetResponseData();
    }

    /**
     * 取得控制項物件
     */
    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        nav_view = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer_layout);
        id_txt = (TextView) findViewById(R.id.id_txt);
        product_txt = (TextView) findViewById(R.id.product_txt);
        count_edt = (EditText) findViewById(R.id.count_edt);
        amount_edt = (EditText) findViewById(R.id.amount_edt);
        check_btn = (Button) findViewById(R.id.check_btn);
        cancel_btn = (Button) findViewById(R.id.cancel_btn);
        delete_btn = (Button) findViewById(R.id.delete_btn);
        delete_check_btn = (Button) findViewById(R.id.delete_check_btn);

        check_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //與OkHttp建立連線 修改日報明細
                sendRequestWithOkHttpForStationReportCorrectDetail();
                Toast.makeText(StationReportCorrectActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(StationReportCorrectActivity.this, "取消", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete_check_btn.setVisibility(View.VISIBLE);
                delete_btn.setVisibility(View.GONE);
            }
        });

        delete_check_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //與OkHttp建立連線 刪除日報明細
                sendRequestWithOkHttpForStationReportDeleteDetail();
                Toast.makeText(StationReportCorrectActivity.this, "刪除此明細", Toast.LENGTH_SHORT).show();
                finish();
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

        toolbar.setTitle("日報明細修正");
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
                Intent intent_login = new Intent(StationReportCorrectActivity.this, LoginActivity.class);
                startActivity(intent_login);
            }
        });
    }

    /**
     * 取得SearchActivity傳遞過來的值
     */
    private void GetResponseData() {
        Bundle bundle = getIntent().getExtras();
        String ResponseText0 = bundle.getString("ResponseText" + 0);
        String ResponseText1 = bundle.getString("ResponseText" + 1);
        String ResponseText2 = bundle.getString("ResponseText" + 2);
        String ResponseText3 = bundle.getString("ResponseText" + 3);
        id_txt.setText(ResponseText0);
        product_txt.setText(ResponseText1);
        count_edt.setText(ResponseText2);
        amount_edt.setText(ResponseText3);
        Log.e(LOG, ResponseText0);
        Log.e(LOG, ResponseText1);
        Log.e(LOG, ResponseText2);
        Log.e(LOG, ResponseText3);
    }

    /**
     * 與OkHttp建立連線 修改日報明細
     */
    private void sendRequestWithOkHttpForStationReportCorrectDetail() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                String D_R_ID = id_txt.getText().toString();
                String item_count = count_edt.getText().toString();
                String item_amount = amount_edt.getText().toString();

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("D_R_ID", D_R_ID)
                            .add("ITEM_COUNT", item_count)
                            .add("ITEM_AMOUNT", item_amount)
                            .build();
                    Log.e(LOG, D_R_ID);
                    Request request = new Request.Builder()
                            //.url("http://192.168.0.172/WQP_OS/StationShopBusinessCorrectDetail.php")
                            .url("http://a.wqp-water.com.tw/WQP_OS/StationShopBusinessCorrectDetail.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, requestBody.toString());
                    Log.e(LOG, response.toString());
                    Log.e(LOG, responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 與OkHttp建立連線 刪除日報明細
     */
    private void sendRequestWithOkHttpForStationReportDeleteDetail() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                String D_R_ID = id_txt.getText().toString();

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("D_R_ID", D_R_ID)
                            .build();
                    Log.e(LOG, D_R_ID);
                    Request request = new Request.Builder()
                            //.url("http://192.168.0.172/WQP_OS/StationShopBusinessDeleteDetail.php")
                            .url("http://a.wqp-water.com.tw/WQP_OS/StationShopBusinessDeleteDetail.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, requestBody.toString());
                    Log.e(LOG, response.toString());
                    Log.e(LOG, responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
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