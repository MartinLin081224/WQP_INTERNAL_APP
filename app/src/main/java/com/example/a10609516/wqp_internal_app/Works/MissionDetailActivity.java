package com.example.a10609516.wqp_internal_app.Works;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.a10609516.wqp_internal_app.Basic.LoginActivity;
import com.example.a10609516.wqp_internal_app.R;
import com.example.a10609516.wqp_internal_app.Tools.WQPClickListener;
import com.example.a10609516.wqp_internal_app.Tools.WQPToolsActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MissionDetailActivity extends WQPToolsActivity {

    private SharedPreferences sp;

    private LinearLayout nav_view, actual_datetime_llt, log_llt;
    private DrawerLayout drawer;
    private Toolbar toolbar;

    private ImageView location_imv;
    private Button report_button;
    private TextView customer_txt, no_txt, type_txt, status_txt, address_txt, mission_txt,
            engineer_txt, start_datetime_txt, end_datetime_txt, actual_datetime_txt, manager_remark_txt, work_remark_txt;
    private String R_COUNT, U_name, rm002, RM001, RM0002, RM003, RM004, RM005, RM006, RM910, RM1112, RM013, RM014, RM022, RM023,
            ML001, ML002, ML004, ML005, ML006, ML008, O_ML009, N_ML009, O_ML010, N_ML010, O_ML011, N_ML011,
            O_ML012, N_ML012, O_ML013, N_ML013, O_ML014, N_ML014, O_ML015, N_ML015, O_ML016, N_ML016,
            O_ML017, N_ML017, O_ML018, N_ML018, O_ML019, N_ML019, O_ML020, N_ML020, O_ML021, N_ML021, ML024, ML_ID,
            maintain_datetime, pay_mode, get_money, have_get, is_money, is_get, manger_remark, work_remark, content_result;

    private String LOG = "MissionDetailActivity";
    private String commandStr;
    private LocationManager locationManager;
    private Context mContext = this;

    public static final int MY_PERMISSION_ACCESS_LOCATION = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_detail);
        //◎LocationManager.NETWORK_PROVIDER //使用網路定位
        commandStr = LocationManager.GPS_PROVIDER;
        //取得控制項物件
        initViews();
        //Menu的onClickListener
        MenuListener();
        //設置Toolbar
        SetToolBar();
        //與OkHttp建立連線(t查詢工務是否已讀任務)
        sendRequestWithOkHttpForMissionReadCheck();
        //與OkHttp建立連線(t取得派工任務明細)
        sendRequestWithOkHttpForMissionDetail();
        //與OkHttp建立連線(t取得派工任務Log)
        sendRequestWithOkHttpForMissionLog();
    }

    /**
     * 取得控制項物件
     */
    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        nav_view = findViewById(R.id.nav_view);
        actual_datetime_llt = findViewById(R.id.actual_datetime_llt);
        log_llt = findViewById(R.id.log_llt);
        drawer = findViewById(R.id.drawer_layout);
        customer_txt = findViewById(R.id.customer_txt);
        no_txt = findViewById(R.id.no_txt);
        type_txt = findViewById(R.id.type_txt);
        status_txt = findViewById(R.id.status_txt);
        address_txt = findViewById(R.id.address_txt);
        mission_txt = findViewById(R.id.mission_txt);
        engineer_txt = findViewById(R.id.engineer_txt);
        start_datetime_txt = findViewById(R.id.start_datetime_txt);
        end_datetime_txt = findViewById(R.id.end_datetime_txt);
        actual_datetime_txt = findViewById(R.id.actual_datetime_txt);
        manager_remark_txt = findViewById(R.id.manager_remark_txt);
        work_remark_txt = findViewById(R.id.work_remark_txt);
        location_imv = findViewById(R.id.location_imv);
        report_button = findViewById(R.id.report_button);

        location_imv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_location = new Intent(MissionDetailActivity.this, GuestMapsActivity.class);
                //將SQL裡的資料傳到MapsActivity
                Bundle bundle = new Bundle();
                bundle.putString("GUEST", RM001);
                bundle.putString("LOCATION", RM005);
                Log.e(LOG, "RM001 : " + RM001);
                Log.e(LOG, "RM005 : " + RM005);
                //intent_gps.putExtra("TitleText", TitleText);//可放所有基本類別
                intent_location.putExtras(bundle);//可放所有基本類別
                startActivity(intent_location);
            }
        });

        report_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_report = new Intent(MissionDetailActivity.this, MissionReportActivity.class);
                //接收MissionActivity傳過來的值
                Bundle bundle = new Bundle();
                bundle.putString("rm002", rm002);
                Log.e(LOG, "RM002 : " + rm002);
                //intent_gps.putExtra("TitleText", TitleText);//可放所有基本類別
                intent_report.putExtras(bundle);//可放所有基本類別
                startActivity(intent_report);
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
        U_name = user_name.getString("U_name", "");
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                id_txt.setText(U_name);
            }
        });

        toolbar.setTitle("任務回報");
        toolbar.setNavigationIcon(R.drawable.icon_menu);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(Gravity.LEFT); //CLOSE Nav Drawer!
                    Log.d(LOG, "Drawer : close");
                } else {
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
                sp.edit().putBoolean("auto_isCheck", false).commit();
                sp.edit().putBoolean("rem_isCheck", false).commit();
                sp.edit().putString("USER_NAME", "").commit();
                sp.edit().putString("PASSWORD", "").commit();
                sp.edit().putString("user_name", "").commit();

                finish();
                Intent intent_login = new Intent(MissionDetailActivity.this, LoginActivity.class);
                startActivity(intent_login);
            }
        });
    }

    /**
     * 與OkHttp建立連線(t查詢工務是否已讀任務)
     */
    private void sendRequestWithOkHttpForMissionReadCheck() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //接收LoginActivity傳過來的值
                SharedPreferences user_id = getSharedPreferences("user_id", MODE_PRIVATE);
                String user_id_data = user_id.getString("ID", "");
                //接收MissionActivity傳過來的值
                Bundle bundle = getIntent().getExtras();
                rm002 = bundle.getString("rm002").trim();

                Log.e(LOG, user_id_data);
                Log.e(LOG, rm002);

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("U_ACC", user_id_data)
                            .add("RM002", rm002)
                            .build();
                    Log.e(LOG, user_id_data);
                    Request request = new Request.Builder()
                            .url("http://a.wqp-water.com.tw/WQP_OS/MissionCheckRead.php")
                            //.url("http://192.168.0.172/WQP_OS/MissionCheckRead.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseJSONWithJSONObjectForMissionReadCheck(responseData);
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
     * 獲得JSON字串並解析成String字串
     *在TextView上SHOW出回傳的員工姓名
     * @param jsonData
     */
    private void parseJSONWithJSONObjectForMissionReadCheck(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON格式改為字串
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                R_COUNT = jsonObject.getString("R_COUNT");

                if (R_COUNT.equals("0")) {
                    //與OkHttp建立連線(t寫入工務已讀任務)
                    sendRequestWithOkHttpForMissionRead();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 與OkHttp建立連線(t寫入工務已讀任務)
     */
    private void sendRequestWithOkHttpForMissionRead() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //接收LoginActivity傳過來的值
                SharedPreferences user_id = getSharedPreferences("user_id", MODE_PRIVATE);
                String user_id_data = user_id.getString("ID", "");
                //接收MissionActivity傳過來的值
                Bundle bundle = getIntent().getExtras();
                rm002 = bundle.getString("rm002").trim();

                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                //利用locationManager.getLastKnownLocation取得當下的位置資料
                if (ActivityCompat.checkSelfPermission(MissionDetailActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MissionDetailActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    ActivityCompat.requestPermissions(MissionDetailActivity.this,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            MY_PERMISSION_ACCESS_LOCATION);
                    return;
                }
                Location location = locationManager.getLastKnownLocation(commandStr);
                String GPS = location.getLatitude() + ", " + location.getLongitude();

                Log.e(LOG, " 緯度 : " + location.getLatitude() + "經度 : " + location.getLongitude());

                Log.e(LOG, user_id_data);
                Log.e(LOG , rm002);

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("U_ACC", user_id_data)
                            .add("RM002", rm002)
                            .add("GPS", GPS)
                            .build();
                    Log.e(LOG, user_id_data);
                    Request request = new Request.Builder()
                            .url("http://a.wqp-water.com.tw/WQP_OS/MissionRead.php")
                            //.url("http://192.168.0.172/WQP_OS/MissionRead.php")
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
     * 與OkHttp建立連線(t取得派工任務明細)
     */
    private void sendRequestWithOkHttpForMissionDetail() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //接收LoginActivity傳過來的值
                SharedPreferences user_id = getSharedPreferences("user_id", MODE_PRIVATE);
                String user_id_data = user_id.getString("ID", "");
                //接收MissionActivity傳過來的值
                Bundle bundle = getIntent().getExtras();
                rm002 = bundle.getString("rm002").trim();

                Log.e(LOG, user_id_data);
                Log.e(LOG , rm002);

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("U_ACC", user_id_data)
                            .add("RM002", rm002)
                            .build();
                    Log.e(LOG, user_id_data);
                    Log.e(LOG, rm002);
                    Request request = new Request.Builder()
                            .url("http://a.wqp-water.com.tw/WQP_OS/MissionDetailSearch.php")
                            //.url("http://192.168.0.172/WQP_OS/MissionDetailSearch.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, requestBody.toString());
                    Log.e(LOG, response.toString());
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectForMissionDetail(responseData);
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
    private void parseJSONWithJSONObjectForMissionDetail(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON格式改為字串
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                RM001 = jsonObject.getString("RM001");
                RM0002 = jsonObject.getString("RM002");
                RM003 = jsonObject.getString("RM003");
                RM004 = jsonObject.getString("RM004");
                RM005 = jsonObject.getString("RM005");
                RM006 = jsonObject.getString("RM006");
                RM910 = jsonObject.getString("RM910");
                RM1112 = jsonObject.getString("RM1112");
                RM013 = jsonObject.getString("RM013");
                RM014 = jsonObject.getString("RM014");
                RM022 = jsonObject.getString("RM022");
                RM023 = jsonObject.getString("RM023");

                Log.e(LOG, RM001);
                Log.e(LOG, RM0002);
                Log.e(LOG, RM003);
                Log.e(LOG, RM004);
                Log.e(LOG, RM910);
                Log.e(LOG, RM1112);
                Log.e(LOG, RM013);
                Log.e(LOG, RM014);

                MissionDetailActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        customer_txt.setText(RM001.trim());
                        no_txt.setText(RM0002.trim());
                        type_txt.setText(RM003.trim());
                        status_txt.setText(RM004.trim());
                        address_txt.setText(RM005.trim());
                        mission_txt.setText(RM006.trim());
                        engineer_txt.setText(U_name.trim());
                        start_datetime_txt.setText(RM910.trim());
                        end_datetime_txt.setText(RM1112.trim());
                        manager_remark_txt.setText(RM022.trim());
                        work_remark_txt.setText(RM023.trim());

                        if (RM013.equals("") || RM014.equals("")) {
                        } else {
                            actual_datetime_llt.setVisibility(View.VISIBLE);
                            actual_datetime_txt.setText(RM013 + " ~ " + RM014);
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 與OkHttp建立連線(t取得派工任務Log)
     */
    private void sendRequestWithOkHttpForMissionLog() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //接收LoginActivity傳過來的值
                SharedPreferences user_id = getSharedPreferences("user_id", MODE_PRIVATE);
                String user_id_data = user_id.getString("ID", "");
                //接收MissionActivity傳過來的值
                Bundle bundle = getIntent().getExtras();
                rm002 = bundle.getString("rm002").trim();

                Log.e(LOG, user_id_data);
                Log.e(LOG , rm002);

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("U_ACC", user_id_data)
                            .add("RM002", rm002)
                            .build();
                    Log.e(LOG, user_id_data);
                    Log.e(LOG, rm002);
                    Request request = new Request.Builder()
                            .url("http://a.wqp-water.com.tw/WQP_OS/MissionLog.php")
                            //.url("http://192.168.0.172/WQP_OS/MissionLog.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, requestBody.toString());
                    Log.e(LOG, response.toString());
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectForMissionLog(responseData);
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
    private void parseJSONWithJSONObjectForMissionLog(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON格式改為字串
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                ML001 = jsonObject.getString("ML001").trim();
                ML002 = jsonObject.getString("ML002").trim();
                ML004 = jsonObject.getString("ML004").trim();
                ML005 = jsonObject.getString("ML005").trim();
                ML006 = jsonObject.getString("ML006").trim();
                ML008 = jsonObject.getString("ML008").trim();
                O_ML009 = jsonObject.getString("O_ML009").trim();
                N_ML009 = jsonObject.getString("N_ML009").trim();
                O_ML010 = jsonObject.getString("O_ML010").trim();
                N_ML010 = jsonObject.getString("N_ML010").trim();
                O_ML011 = jsonObject.getString("O_ML011").trim();
                N_ML011 = jsonObject.getString("N_ML011").trim();
                O_ML012 = jsonObject.getString("O_ML012").trim();
                N_ML012 = jsonObject.getString("N_ML012").trim();
                O_ML013 = jsonObject.getString("O_ML013").trim();
                N_ML013 = jsonObject.getString("N_ML013").trim();
                O_ML014 = jsonObject.getString("O_ML014").trim();
                N_ML014 = jsonObject.getString("N_ML014").trim();
                O_ML015 = jsonObject.getString("O_ML015").trim();
                N_ML015 = jsonObject.getString("N_ML015").trim();
                O_ML016 = jsonObject.getString("O_ML016").trim();
                N_ML016 = jsonObject.getString("N_ML016").trim();
                O_ML017 = jsonObject.getString("O_ML017").trim();
                N_ML017 = jsonObject.getString("N_ML017").trim();
                O_ML018 = jsonObject.getString("O_ML018").trim();
                N_ML018 = jsonObject.getString("N_ML018").trim();
                O_ML019 = jsonObject.getString("O_ML019").trim();
                N_ML019 = jsonObject.getString("N_ML019").trim();
                O_ML020 = jsonObject.getString("O_ML020").trim();
                N_ML020 = jsonObject.getString("N_ML020").trim();
                O_ML021 = jsonObject.getString("O_ML021").trim();
                N_ML021 = jsonObject.getString("N_ML021").trim();
                ML024 = jsonObject.getString("ML024").trim();
                ML_ID = jsonObject.getString("ML_ID").trim();

                //JSONArray加入SearchData資料
                ArrayList<String> JArrayList = new ArrayList<String>();
                JArrayList.add(ML001);//0
                JArrayList.add(ML002);//1
                JArrayList.add(ML004);//2
                JArrayList.add(ML005);//3
                JArrayList.add(ML006);//4
                JArrayList.add(ML008);//5
                JArrayList.add(O_ML009);//6
                JArrayList.add(N_ML009);//7
                JArrayList.add(O_ML010);//8
                JArrayList.add(N_ML010);//9
                JArrayList.add(O_ML011);//10
                JArrayList.add(N_ML011);//11
                JArrayList.add(O_ML012);//12
                JArrayList.add(N_ML012);//13
                JArrayList.add(O_ML013);//14
                JArrayList.add(N_ML013);//15
                JArrayList.add(O_ML014);//16
                JArrayList.add(N_ML014);//17
                JArrayList.add(O_ML015);//18
                JArrayList.add(N_ML015);//19
                JArrayList.add(O_ML016);//20
                JArrayList.add(N_ML016);//21
                JArrayList.add(O_ML017);//22
                JArrayList.add(N_ML017);//23
                JArrayList.add(O_ML018);//24
                JArrayList.add(N_ML018);//25
                JArrayList.add(O_ML019);//26
                JArrayList.add(N_ML019);//27
                JArrayList.add(O_ML020);//28
                JArrayList.add(N_ML020);//29
                JArrayList.add(O_ML021);//30
                JArrayList.add(N_ML021);//31
                JArrayList.add(ML024);//32
                JArrayList.add(ML_ID);//33

                Log.e(LOG, ML001);
                Log.e(LOG, ML002);
                Log.e(LOG, ML004);
                Log.e(LOG, ML005);
                Log.e(LOG, ML006);
                Log.e(LOG, ML008);
                Log.e(LOG, O_ML009);
                Log.e(LOG, N_ML009);
                Log.e(LOG, O_ML010);
                Log.e(LOG, N_ML010);
                Log.e(LOG, O_ML011);
                Log.e(LOG, N_ML011);
                Log.e(LOG, O_ML012);
                Log.e(LOG, N_ML012);
                Log.e(LOG, O_ML013);
                Log.e(LOG, N_ML013);
                Log.e(LOG, O_ML014);
                Log.e(LOG, N_ML014);
                Log.e(LOG, O_ML015);
                Log.e(LOG, N_ML015);
                Log.e(LOG, O_ML016);
                Log.e(LOG, N_ML016);
                Log.e(LOG, O_ML017);
                Log.e(LOG, N_ML017);
                Log.e(LOG, O_ML018);
                Log.e(LOG, N_ML018);
                Log.e(LOG, O_ML019);
                Log.e(LOG, N_ML019);
                Log.e(LOG, O_ML020);
                Log.e(LOG, N_ML020);
                Log.e(LOG, O_ML021);
                Log.e(LOG, N_ML021);
                Log.e(LOG, ML024);
                Log.e(LOG, ML_ID);

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
    Handler mHandler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    LinearLayout small_llt0 = new LinearLayout(MissionDetailActivity.this);
                    small_llt0.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout small_llt1 = new LinearLayout(MissionDetailActivity.this);
                    small_llt1.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout small_llt2 = new LinearLayout(MissionDetailActivity.this);
                    small_llt2.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout big_llt1 = new LinearLayout(MissionDetailActivity.this);
                    big_llt1.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout big_llt2 = new LinearLayout(MissionDetailActivity.this);
                    big_llt2.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout big_llt3 = new LinearLayout(MissionDetailActivity.this);
                    big_llt3.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout large_llt = new LinearLayout(MissionDetailActivity.this);
                    large_llt.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout XL_llt = new LinearLayout(MissionDetailActivity.this);
                    XL_llt.setOrientation(LinearLayout.VERTICAL);

                    Bundle jb = msg.getData();
                    ArrayList<String> JArrayList = new ArrayList<String>();
                    //int i = b.getStringArrayList("JSON_data").size();
                    JArrayList = jb.getStringArrayList("JSON_data");

                    //設置每筆LinearLayout的間隔分隔線
                    TextView dynamically_txt = new TextView(MissionDetailActivity.this);
                    dynamically_txt.setBackgroundColor(mContext.getResources().getColor(R.color.WQP_Blue));

                    //顯示每筆LinearLayout的Title
                    TextView dynamically_title;
                    dynamically_title = new TextView(MissionDetailActivity.this);
                    if (JArrayList.get(32).equals("0")) {
                        dynamically_title.setText("任務內容");
                    } else if (JArrayList.get(32).equals("1")){
                        dynamically_title.setText("任務回報");
                    }
                    dynamically_title.setPadding(10, 10, 10, 10);
                    dynamically_title.setGravity(Gravity.LEFT);
                    //dynamically_title.setWidth(50);
                    dynamically_title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 22);
                    dynamically_title.setTextColor(mContext.getResources().getColor(R.color.WQP_Gray));
                    Log.e(LOG, dynamically_title.getText().toString());

                    //顯示每筆LinearLayout的建立人員、時間
                    TextView dynamically_create;
                    dynamically_create = new TextView(MissionDetailActivity.this);
                    dynamically_create.setText(JArrayList.get(1) + " by " + JArrayList.get(0));
                    dynamically_create.setPadding(10, 10, 10, 10);
                    dynamically_create.setGravity(Gravity.LEFT);
                    //dynamically_create.setWidth(50);
                    dynamically_create.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                    dynamically_create.setTextColor(mContext.getResources().getColor(R.color.WQP_Gray));
                    Log.e(LOG, dynamically_create.getText().toString());

                    content_result = "";
                    if (!JArrayList.get(33).equals("1") && (!JArrayList.get(6).equals(JArrayList.get(7)) || !JArrayList.get(8).equals(JArrayList.get(9)) || !JArrayList.get(10).equals(JArrayList.get(11)) || !JArrayList.get(12).equals(JArrayList.get(13)))) {
                        maintain_datetime = "●變更任務時間 : \n" + JArrayList.get(6) + " " + JArrayList.get(8) + " ~ " + JArrayList.get(10) + " " + JArrayList.get(12) +
                                " -> " + JArrayList.get(7) + " " + JArrayList.get(9) + " ~ " + JArrayList.get(11) + " " + JArrayList.get(13);
                        content_result = content_result + maintain_datetime;
                    }
                    if (!JArrayList.get(33).equals("1") && !JArrayList.get(14).equals(JArrayList.get(15))) {
                        pay_mode =  "●變更付款方式 : " + JArrayList.get(14) + " -> " + JArrayList.get(15);
                        if (!content_result.equals("")) {
                            content_result =  content_result + "\n" + pay_mode;
                        } else {
                            content_result =  content_result + pay_mode;
                        }
                    }
                    if (!JArrayList.get(33).equals("1") && !JArrayList.get(16).equals(JArrayList.get(17))) {
                        get_money =  "●變更是否要收款 : " + JArrayList.get(16) + " -> " + JArrayList.get(17);
                        if (!content_result.equals("")) {
                            content_result =  content_result + "\n" + get_money;
                        } else {
                            content_result =  content_result + get_money;
                        }
                    }
                    if (!JArrayList.get(33).equals("1") && !JArrayList.get(18).equals(JArrayList.get(19))) {
                        have_get =  "●變更應收金額 : $" + JArrayList.get(18).replace(".00", "") + " -> $" + JArrayList.get(19).replace(".00", "");
                        if (!content_result.equals("")) {
                            content_result =  content_result + "\n" + have_get;
                        } else {
                            content_result =  content_result + have_get;
                        }
                    }

                    //顯示每筆LinearLayout的內容
                    TextView dynamically_content;
                    dynamically_content = new TextView(MissionDetailActivity.this);
                    if (JArrayList.get(32).equals("1")) {
                        if (JArrayList.get(2).equals("待處理") && JArrayList.get(3).equals("1")){
                            dynamically_content.setText("任務狀態 : 已讀取\n" + "讀取時間 : " + JArrayList.get(1));
                        } else if (JArrayList.get(2).equals("處理中") && JArrayList.get(3).equals("1") && !JArrayList.get(4).equals("無法施工")){
                            if (JArrayList.get(5).equals("")) {
                                dynamically_content.setText("任務狀態 : " + JArrayList.get(2) + "(" + JArrayList.get(4) + ")\n" + "回報時間 : " + JArrayList.get(1));
                            } else {
                                dynamically_content.setText("任務狀態 : " + JArrayList.get(2) + "(" + JArrayList.get(4) + ")\n" + "回報時間 : " + JArrayList.get(1) + "\n" + "現場狀況回報 : " + JArrayList.get(5));
                            }
                        } else if (JArrayList.get(2).equals("處理中") && JArrayList.get(3).equals("1") && !JArrayList.get(26).equals(JArrayList.get(27)) ){
                            if (JArrayList.get(5).equals("")) {
                                dynamically_content.setText("任務狀態 : " + JArrayList.get(2) + "(" + JArrayList.get(4) + ")\n" + "回報時間 : " + JArrayList.get(1) + "\n" + "檢修類別 : " + JArrayList.get(27));
                            } else {
                                dynamically_content.setText("任務狀態 : " + JArrayList.get(2) + "(" + JArrayList.get(4) + ")\n" + "回報時間 : " + JArrayList.get(1) + "\n" + "檢修類別 : " + JArrayList.get(27) + "\n" + "現場狀況回報 : " + JArrayList.get(5));
                            }
                        } else if (JArrayList.get(2).equals("處理中") && JArrayList.get(3).equals("1") && JArrayList.get(4).equals("無法施工")) {
                            if (JArrayList.get(5).equals("")) {
                                dynamically_content.setText("任務狀態 : " + JArrayList.get(2) + "(" + JArrayList.get(4) + ")\n" + "回報時間 : " + JArrayList.get(1) + "\n" + "無效原因 : " + JArrayList.get(25));
                            } else {
                                dynamically_content.setText("任務狀態 : " + JArrayList.get(2) + "(" + JArrayList.get(4) + ")\n" + "回報時間 : " + JArrayList.get(1) + "\n" + "無效原因 : " + JArrayList.get(25) + "\n" + "現場狀況回報 : " + JArrayList.get(5));
                            }
                        }
                    } else {
                        if (JArrayList.get(33).equals("1")) {
                            dynamically_content.setText("新增一項任務");
                        } else {
                            dynamically_content.setText(content_result);
                        }
                    }
                    dynamically_content.setPadding(10, 0, 10, 10);
                    dynamically_content.setGravity(Gravity.LEFT);
                    //dynamically_title.setWidth(50);
                    dynamically_content.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                    dynamically_content.setTextColor(mContext.getResources().getColor(R.color.WQP_Black));
                    Log.e(LOG, dynamically_content.getText().toString());

                    LinearLayout.LayoutParams small_2pm = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.7f);
                    LinearLayout.LayoutParams small_3pm = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 3.0f);
                    LinearLayout.LayoutParams btn_pm = new LinearLayout.LayoutParams(40, 40);
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                    small_llt0.addView(dynamically_title);
                    small_llt1.addView(dynamically_create);
                    small_llt2.addView(dynamically_content);

                    big_llt1.addView(small_llt0);
                    big_llt2.addView(small_llt1);
                    big_llt3.addView(small_llt2);

                    XL_llt.addView(dynamically_txt, LinearLayout.LayoutParams.MATCH_PARENT, 3);
                    XL_llt.addView(big_llt1);
                    XL_llt.addView(big_llt2);
                    XL_llt.addView(big_llt3);

                    log_llt.addView(XL_llt);

                    LinearLayout first_llt = (LinearLayout) log_llt.getChildAt(0);
                    first_llt.getChildAt(0).setVisibility(View.GONE);

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
        //與OkHttp建立連線(t取得派工任務明細)
        sendRequestWithOkHttpForMissionDetail();
        log_llt.removeAllViews();
        //與OkHttp建立連線(t取得派工任務Log)
        sendRequestWithOkHttpForMissionLog();
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