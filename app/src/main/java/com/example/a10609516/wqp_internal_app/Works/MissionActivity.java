package com.example.a10609516.wqp_internal_app.Works;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
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
import android.widget.Toast;

import com.example.a10609516.wqp_internal_app.Basic.HomeActivity;
import com.example.a10609516.wqp_internal_app.Basic.LoginActivity;
import com.example.a10609516.wqp_internal_app.Basic.RequisitionActivity;
import com.example.a10609516.wqp_internal_app.Basic.RequisitionSearchActivity;
import com.example.a10609516.wqp_internal_app.Basic.VersionActivity;
import com.example.a10609516.wqp_internal_app.Boss.ApplyExchangeActivity;
import com.example.a10609516.wqp_internal_app.Clerk.QuotationActivity;
import com.example.a10609516.wqp_internal_app.DepartmentAndDIY.SetupDemandActivity;
import com.example.a10609516.wqp_internal_app.DepartmentAndDIY.StationReportActivity;
import com.example.a10609516.wqp_internal_app.DepartmentAndDIY.StationReportSearchActivity;
import com.example.a10609516.wqp_internal_app.Manager.InventoryActivity;
import com.example.a10609516.wqp_internal_app.Manager.OrderSearchActivity;
import com.example.a10609516.wqp_internal_app.R;
import com.example.a10609516.wqp_internal_app.Tools.WQPToolsActivity;
import com.example.easywaylocation.EasyWayLocation;
import com.example.easywaylocation.GetLocationDetail;
import com.example.easywaylocation.Listener;
import com.example.easywaylocation.LocationData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.easywaylocation.EasyWayLocation.LOCATION_SETTING_REQUEST_CODE;

public class MissionActivity extends WQPToolsActivity implements Listener, LocationData.AddressCallBack {

    private SharedPreferences sp;
    private TextView no_report_txt, reported_txt, closed_txt;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Button[] dynamically_btn;
    private String status;

    private LinearLayout nav_view, nav_mission;
    private DrawerLayout drawer;
    private Toolbar toolbar;

    private EasyWayLocation easyWayLocation;
    GetLocationDetail getLocationDetail;
    private TextView location, latLong;

    private Context mContext = this;

    private String LOG = "MissionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission);
        //取得控制項物件
        initViews();
        //Menu的onClickListener
        MenuListener();
        //設置Toolbar
        SetToolBar();

        getLocationDetail = new GetLocationDetail(this, this);
        easyWayLocation = new EasyWayLocation(this, false,this);
        if (permissionIsGranted()) {
            doLocationWork();
        } else {
            // Permission not granted, ask for it
            //testLocationRequest.requestPermission(121);
        }

        nav_mission.removeAllViews();
        status = "0";

        //與OkHttp建立連線(t查詢為回報之任務明細)
        sendRequestWithOkHttpForMissionUnReported();
    }

    /**
     * 取得控制項物件
     */
    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        nav_view = findViewById(R.id.nav_view);
        nav_mission = findViewById(R.id.nav_mission);
        drawer = findViewById(R.id.drawer_layout);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        no_report_txt = findViewById(R.id.no_report_txt);
        reported_txt = findViewById(R.id.reported_txt);
        closed_txt = findViewById(R.id.closed_txt);
        location = findViewById(R.id.location);
        latLong = findViewById(R.id.latlong);

        //UI介面下拉刷新
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(false);
                nav_mission.removeAllViews();
                if (status.equals("0")) {
                    //與OkHttp建立連線(查詢未回報之任務明細)
                    sendRequestWithOkHttpForMissionUnReported();
                } else if (status.equals("1")) {
                    //與OkHttp建立連線(查詢已回報之任務明細)
                    sendRequestWithOkHttpForMissionReported();
                } else if (status.equals("2")) {
                    //與OkHttp建立連線(查詢已結案之任務明細)
                    sendRequestWithOkHttpForMissionClosed();
                }

            }
        });

        no_report_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                no_report_txt.setBackgroundResource(R.drawable.rectangle_radius_solid);
                reported_txt.setBackgroundResource(R.drawable.rectangle_radius_empty);
                closed_txt.setBackgroundResource(R.drawable.rectangle_radius_empty);
                no_report_txt.setTextColor(mContext.getResources().getColor(R.color.WQP_White));
                reported_txt.setTextColor(mContext.getResources().getColor(R.color.WQP_Blue));
                closed_txt.setTextColor(mContext.getResources().getColor(R.color.WQP_Blue));
                status = "0";
                nav_mission.removeAllViews();

                try{
                    // delay 1 second
                    Thread.sleep(300);
                    //與OkHttp建立連線(查詢未回報之任務明細)
                    sendRequestWithOkHttpForMissionUnReported();
                } catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        });
        reported_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reported_txt.setBackgroundResource(R.drawable.rectangle_radius_solid);
                no_report_txt.setBackgroundResource(R.drawable.rectangle_radius_empty);
                closed_txt.setBackgroundResource(R.drawable.rectangle_radius_empty);
                reported_txt.setTextColor(mContext.getResources().getColor(R.color.WQP_White));
                no_report_txt.setTextColor(mContext.getResources().getColor(R.color.WQP_Blue));
                closed_txt.setTextColor(mContext.getResources().getColor(R.color.WQP_Blue));
                status = "1";
                nav_mission.removeAllViews();

                try{
                    // delay 1 second
                    Thread.sleep(300);
                    //與OkHttp建立連線(查詢已回報之任務明細)
                    sendRequestWithOkHttpForMissionReported();
                } catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        });
        closed_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closed_txt.setBackgroundResource(R.drawable.rectangle_radius_solid);
                no_report_txt.setBackgroundResource(R.drawable.rectangle_radius_empty);
                reported_txt.setBackgroundResource(R.drawable.rectangle_radius_empty);
                closed_txt.setTextColor(mContext.getResources().getColor(R.color.WQP_White));
                no_report_txt.setTextColor(mContext.getResources().getColor(R.color.WQP_Blue));
                reported_txt.setTextColor(mContext.getResources().getColor(R.color.WQP_Blue));
                status = "2";
                nav_mission.removeAllViews();

                try{
                    // delay 1 second
                    Thread.sleep(300);
                    //與OkHttp建立連線(查詢已結案之任務明細)
                    sendRequestWithOkHttpForMissionClosed();
                } catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Menu的onClickListener
     */
    private void MenuListener() {
        TextView home_txt = findViewById(R.id.home_txt);
        //home_txt.setOnClickListener(new WQPClickListener());
        TextView exchange_txt = findViewById(R.id.exchange_txt);
        //exchange_txt.setOnClickListener(new WQPClickListener());
        TextView schedule_txt = findViewById(R.id.schedule_txt);
        //schedule_txt.setOnClickListener(new WQPClickListener());
        TextView calendar_txt = findViewById(R.id.calendar_txt);
        //calendar_txt.setOnClickListener(new WQPClickListener());
        TextView mission_txt = findViewById(R.id.mission_txt);
        //mission_txt.setOnClickListener(new WQPClickListener());
        TextView bonus_txt = findViewById(R.id.bonus_txt);
        //bonus_txt.setOnClickListener(new WQPClickListener());
        TextView points_txt = findViewById(R.id.points_txt);
        //points_txt.setOnClickListener(new WQPClickListener());
        TextView miss_report_txt = findViewById(R.id.miss_report_txt);
        //miss_report_txt.setOnClickListener(new WQPClickListener());
        TextView gps_txt = findViewById(R.id.gps_txt);
        //gps_txt.setOnClickListener(new WQPClickListener());
        TextView quotation_txt = findViewById(R.id.quotation_txt);
        //quotation_txt.setOnClickListener(new WQPClickListener());
        TextView report_txt = findViewById(R.id.report_txt);
        //report_txt.setOnClickListener(new WQPClickListener());
        TextView report_search_txt = findViewById(R.id.report_search_txt);
        //report_search_txt.setOnClickListener(new WQPClickListener());
        TextView setup_txt = findViewById(R.id.setup_txt);
        //setup_txt.setOnClickListener(new WQPClickListener());
        TextView inventory_txt = findViewById(R.id.inventory_txt);
        //inventory_txt.setOnClickListener(new WQPClickListener());
        TextView picking_txt = findViewById(R.id.picking_txt);
        //picking_txt.setOnClickListener(new WQPClickListener());
        TextView requisition_txt = findViewById(R.id.requisition_txt);
        //requisition_txt.setOnClickListener(new WQPClickListener());
        TextView progress_txt = findViewById(R.id.progress_txt);
        //progress_txt.setOnClickListener(new WQPClickListener());
        TextView version_info_txt = findViewById(R.id.version_info_txt);
        //version_info_txt.setOnClickListener(new WQPClickListener());

        SharedPreferences sharedPreferences_menu = getSharedPreferences("MENU", MODE_PRIVATE);
        String menu_home = sharedPreferences_menu.getString("menu_home", "");
        String menu_exchange = sharedPreferences_menu.getString("menu_exchange", "");
        String menu_schedule = sharedPreferences_menu.getString("menu_schedule", "");
        String menu_calendar = sharedPreferences_menu.getString("menu_calendar", "");
        String menu_mission = sharedPreferences_menu.getString("menu_mission", "");
        String menu_bonus = sharedPreferences_menu.getString("menu_bonus", "");
        String menu_points = sharedPreferences_menu.getString("menu_points", "");
        String menu_miss_report = sharedPreferences_menu.getString("menu_miss_report", "");
        String menu_gps = sharedPreferences_menu.getString("menu_gps", "");
        String menu_quotation = sharedPreferences_menu.getString("menu_quotation", "");
        String menu_report = sharedPreferences_menu.getString("menu_report", "");
        String menu_report_search = sharedPreferences_menu.getString("menu_report_search", "");
        String menu_setup = sharedPreferences_menu.getString("menu_setup", "");
        String menu_inventory = sharedPreferences_menu.getString("menu_inventory", "");
        String menu_picking = sharedPreferences_menu.getString("menu_picking", "");
        String menu_req = sharedPreferences_menu.getString("menu_req", "");
        String menu_progress = sharedPreferences_menu.getString("menu_progress", "");
        String menu_version_info = sharedPreferences_menu.getString("menu_version_info", "");

        Log.e("MENU", menu_home);
        Log.e("MENU", menu_exchange);
        Log.e("MENU", menu_schedule);
        Log.e("MENU", menu_calendar);
        Log.e("MENU", menu_mission);
        Log.e("MENU", menu_bonus);
        Log.e("MENU", menu_points);
        Log.e("MENU", menu_miss_report);
        Log.e("MENU", menu_gps);
        Log.e("MENU", menu_quotation);
        Log.e("MENU", menu_report);
        Log.e("MENU", menu_report_search);
        Log.e("MENU", menu_setup);
        Log.e("MENU", menu_inventory);
        Log.e("MENU", menu_picking);
        Log.e("MENU", menu_req);
        Log.e("MENU", menu_progress);
        Log.e("MENU", menu_version_info);

        home_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (menu_home.equals("1")) {
                    Intent intent0 = new Intent(mContext, HomeActivity.class);
                    mContext.startActivity(intent0);
                } else {
                    Toast.makeText(mContext, "【首頁】無執行權限", Toast.LENGTH_SHORT).show();
                }
            }
        });

        exchange_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (menu_exchange.equals("1")) {
                    Intent intent10 = new Intent(mContext, ApplyExchangeActivity.class);
                    mContext.startActivity(intent10);
                } else {
                    Toast.makeText(mContext, "【換貨申請單】無執行權限", Toast.LENGTH_SHORT).show();
                }
            }
        });

        schedule_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (menu_schedule.equals("1")) {
                    Intent intent11 = new Intent(mContext, ScheduleActivity.class);
                    mContext.startActivity(intent11);
                } else {
                    Toast.makeText(mContext, "【行程資訊】無執行權限", Toast.LENGTH_SHORT).show();
                }
            }
        });

        calendar_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (menu_calendar.equals("1")) {
                    Intent intent12 = new Intent(mContext, CalendarActivity.class);
                    mContext.startActivity(intent12);
                } else {
                    Toast.makeText(mContext, "【派工行事曆】無執行權限", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mission_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (menu_mission.equals("1")) {
                    Intent intent13 = new Intent(mContext, MissionActivity.class);
                    mContext.startActivity(intent13);
                } else {
                    Toast.makeText(mContext, "【派工任務】無執行權限", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bonus_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (menu_bonus.equals("1")) {
                    Intent intent14 = new Intent(mContext, PointsActivity.class);
                    mContext.startActivity(intent14);
                } else {
                    Toast.makeText(mContext, "【點數總覽】無執行權限", Toast.LENGTH_SHORT).show();
                }
            }
        });

        points_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (menu_points.equals("1")) {
                    Intent intent15 = new Intent(mContext, EngPointsActivity.class);
                    mContext.startActivity(intent15);
                } else {
                    Toast.makeText(mContext, "【點數明細】無執行權限", Toast.LENGTH_SHORT).show();
                }
            }
        });

        miss_report_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (menu_miss_report.equals("1")) {
                    Intent intent16 = new Intent(mContext, MissCountActivity.class);
                    mContext.startActivity(intent16);
                } else {
                    Toast.makeText(mContext, "【各區未回報數量】無執行權限", Toast.LENGTH_SHORT).show();
                }
            }
        });

        gps_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (menu_gps.equals("1")) {
                    Intent intent17 = new Intent(mContext, GPSActivity.class);
                    mContext.startActivity(intent17);
                } else {
                    Toast.makeText(mContext, "【工務打卡GPS】無執行權限", Toast.LENGTH_SHORT).show();
                }
            }
        });

        quotation_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (menu_quotation.equals("1")) {
                    Intent intent20 = new Intent(mContext, QuotationActivity.class);
                    mContext.startActivity(intent20);
                } else {
                    Toast.makeText(mContext, "【報價單審核】無執行權限", Toast.LENGTH_SHORT).show();
                }
            }
        });

        report_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (menu_report.equals("1")) {
                    Intent intent30 = new Intent(mContext, StationReportActivity.class);
                    mContext.startActivity(intent30);
                } else {
                    Toast.makeText(mContext, "【日報上傳作業】無執行權限", Toast.LENGTH_SHORT).show();
                }
            }
        });

        report_search_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (menu_report_search.equals("1")) {
                    Intent intent31 = new Intent(mContext, StationReportSearchActivity.class);
                    mContext.startActivity(intent31);
                } else {
                    Toast.makeText(mContext, "【日報查詢/修正】無執行權限", Toast.LENGTH_SHORT).show();
                }
            }
        });

        setup_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (menu_setup.equals("1")) {
                    Intent intent31 = new Intent(mContext, SetupDemandActivity.class);
                    mContext.startActivity(intent31);
                } else {
                    Toast.makeText(mContext, "【安裝查詢表】無執行權限", Toast.LENGTH_SHORT).show();
                }
            }
        });

        inventory_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (menu_inventory.equals("1")) {
                    Intent intent40 = new Intent(mContext, InventoryActivity.class);
                    mContext.startActivity(intent40);
                } else {
                    Toast.makeText(mContext, "【盤點單】無執行權限", Toast.LENGTH_SHORT).show();
                }

            }
        });

        picking_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (menu_picking.equals("1")) {
                    Intent intent41 = new Intent(mContext, OrderSearchActivity.class);
                    mContext.startActivity(intent41);
                } else {
                    Toast.makeText(mContext, "【撿料單】無執行權限", Toast.LENGTH_SHORT).show();
                }
            }
        });

        requisition_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (menu_req.equals("1")) {
                    Intent intent50 = new Intent(mContext, RequisitionActivity.class);
                    mContext.startActivity(intent50);
                } else {
                    Toast.makeText(mContext, "【需求申請單】無執行權限", Toast.LENGTH_SHORT).show();
                }
            }
        });

        progress_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (menu_progress.equals("1")) {
                    Intent intent51 = new Intent(mContext, RequisitionSearchActivity.class);
                    mContext.startActivity(intent51);
                } else {
                    Toast.makeText(mContext, "【進度查詢】無執行權限", Toast.LENGTH_SHORT).show();
                }
            }
        });

        version_info_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (menu_version_info.equals("1")) {
                    Intent intent60 = new Intent(mContext, VersionActivity.class);
                    mContext.startActivity(intent60);
                } else {
                    Toast.makeText(mContext, "【版本資訊】無執行權限", Toast.LENGTH_SHORT).show();
                }
            }
        });
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

        toolbar.setTitle("派工任務");
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
                sp.edit().putString("MENU", "").commit();

                finish();
                Intent intent_login = new Intent(MissionActivity.this, LoginActivity.class);
                startActivity(intent_login);
            }
        });
    }

    /**
     * 與OkHttp建立連線(查詢未回報之任務明細)
     */
    private void sendRequestWithOkHttpForMissionUnReported() {
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
                            .add("U_ACC", user_id_data)
                            .build();
                    Log.e(LOG, user_id_data);
                    Request request = new Request.Builder()
                            .url("http://a.wqp-water.com.tw/WQP_OS/MissionUnReportedSearch.php")
                            //.url("http://192.168.0.172/WQP_OS/MissionUnReportedSearch.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, requestBody.toString());
                    Log.e(LOG, response.toString());
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectForMissionUnReported(responseData);
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
    private void parseJSONWithJSONObjectForMissionUnReported(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            dynamically_btn = new Button[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON格式改為字串
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String CREATE_DATE = jsonObject.getString("CREATE_DATE");
                String RM002 = jsonObject.getString("RM002");
                String RM910 = jsonObject.getString("RM910");
                String RM003 = jsonObject.getString("RM003");
                String ML005 = jsonObject.getString("ML005");
                /*String DEVICE = jsonObject.getString("DEVICE");*/

                //JSONArray加入SearchData資料
                ArrayList<String> JArrayList = new ArrayList<String>();
                JArrayList.add(CREATE_DATE);
                JArrayList.add(RM002);
                JArrayList.add(RM910);
                JArrayList.add(RM003);
                JArrayList.add(ML005);
                /*JArrayList.add(DEVICE);*/

                Log.e(LOG, CREATE_DATE);
                Log.e(LOG, RM002);
                Log.e(LOG, RM910);
                Log.e(LOG, RM003);
                Log.e(LOG, ML005);
                //Log.e(LOG, "FILTER : " + FILTER + " / DEVICE : " + DEVICE);

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
     * 與OkHttp建立連線(查詢已回報之任務明細)
     */
    private void sendRequestWithOkHttpForMissionReported() {
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
                            .add("U_ACC", user_id_data)
                            .build();
                    Log.e(LOG, user_id_data);
                    Request request = new Request.Builder()
                            .url("http://a.wqp-water.com.tw/WQP_OS/MissionReportedSearch.php")
                            //.url("http://192.168.0.172/WQP_OS/MissionReportedSearch.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, requestBody.toString());
                    Log.e(LOG, response.toString());
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectForMissionReported(responseData);
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
    private void parseJSONWithJSONObjectForMissionReported(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            dynamically_btn = new Button[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON格式改為字串
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String CREATE_DATE = jsonObject.getString("CREATE_DATE");
                String RM002 = jsonObject.getString("RM002");
                String RM910 = jsonObject.getString("RM910");
                String RM003 = jsonObject.getString("RM003");
                String ML005 = jsonObject.getString("ML005");
                /*String DEVICE = jsonObject.getString("DEVICE");*/

                //JSONArray加入SearchData資料
                ArrayList<String> JArrayList = new ArrayList<String>();
                JArrayList.add(CREATE_DATE);
                JArrayList.add(RM002);
                JArrayList.add(RM910);
                JArrayList.add(RM003);
                JArrayList.add(ML005);
                /*JArrayList.add(DEVICE);*/

                Log.e(LOG, CREATE_DATE);
                Log.e(LOG, RM002);
                Log.e(LOG, RM910);
                Log.e(LOG, RM003);
                Log.e(LOG, ML005);
                //Log.e(LOG, "FILTER : " + FILTER + " / DEVICE : " + DEVICE);

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
     * 與OkHttp建立連線(查詢已結案之任務明細)
     */
    private void sendRequestWithOkHttpForMissionClosed() {
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
                            .add("U_ACC", user_id_data)
                            .build();
                    Log.e(LOG, user_id_data);
                    Request request = new Request.Builder()
                            .url("http://a.wqp-water.com.tw/WQP_OS/MissionClosedSearch.php")
                            //.url("http://192.168.0.172/WQP_OS/MissionClosedSearch.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, requestBody.toString());
                    Log.e(LOG, response.toString());
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectForMissionClosed(responseData);
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
    private void parseJSONWithJSONObjectForMissionClosed(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            dynamically_btn = new Button[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON格式改為字串
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String CREATE_DATE = jsonObject.getString("CREATE_DATE");
                String RM002 = jsonObject.getString("RM002");
                String RM910 = jsonObject.getString("RM910");
                String RM003 = jsonObject.getString("RM003");
                String ML005 = jsonObject.getString("ML005");
                /*String DEVICE = jsonObject.getString("DEVICE");*/

                //JSONArray加入SearchData資料
                ArrayList<String> JArrayList = new ArrayList<String>();
                JArrayList.add(CREATE_DATE);
                JArrayList.add(RM002);
                JArrayList.add(RM910);
                JArrayList.add(RM003);
                JArrayList.add(ML005);
                /*JArrayList.add(DEVICE);*/

                Log.e(LOG, CREATE_DATE);
                Log.e(LOG, RM002);
                Log.e(LOG, RM910);
                Log.e(LOG, RM003);
                Log.e(LOG, ML005);
                //Log.e(LOG, "FILTER : " + FILTER + " / DEVICE : " + DEVICE);

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
                    LinearLayout small_llt0 = new LinearLayout(MissionActivity.this);
                    small_llt0.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout small_llt1 = new LinearLayout(MissionActivity.this);
                    small_llt1.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout small_llt2 = new LinearLayout(MissionActivity.this);
                    small_llt2.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout big_llt1 = new LinearLayout(MissionActivity.this);
                    big_llt1.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout big_llt2 = new LinearLayout(MissionActivity.this);
                    big_llt2.setOrientation(LinearLayout.VERTICAL);
                    big_llt2.setGravity(Gravity.CENTER);
                    LinearLayout large_llt = new LinearLayout(MissionActivity.this);
                    large_llt.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout XL_llt = new LinearLayout(MissionActivity.this);
                    XL_llt.setOrientation(LinearLayout.VERTICAL);
                    RelativeLayout id_rlt = new RelativeLayout(MissionActivity.this);

                    Bundle jb = msg.getData();
                    ArrayList<String> JArrayList = new ArrayList<String>();
                    //int i = b.getStringArrayList("JSON_data").size();
                    JArrayList = jb.getStringArrayList("JSON_data");

                    //顯示每筆LinearLayout的Title
                    TextView dynamically_title;
                    dynamically_title = new TextView(MissionActivity.this);
                    dynamically_title.setText("新任務通知");
                    dynamically_title.setPadding(10, 10, 10, 0);
                    dynamically_title.setGravity(Gravity.LEFT);
                    //dynamically_title.setWidth(50);
                    dynamically_title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                    dynamically_title.setTextColor(mContext.getResources().getColor(R.color.WQP_Black));

                    //顯示每筆LinearLayout的日期時間
                    /*TextView dynamically_datetime;
                    dynamically_datetime = new TextView(MissionActivity.this);
                    dynamically_datetime.setText(JArrayList.get(0).trim());
                    //dynamically_datetime.setText(JArrayList.get(0).trim().substring(0, JArrayList.get(0).length()-4));
                    dynamically_datetime.setPadding(0, 10, 10, 0);
                    dynamically_datetime.setGravity(Gravity.RIGHT);
                    //dynamically_datetime.setWidth(50);
                    dynamically_datetime.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                    dynamically_datetime.setTextColor(mContext.getResources().getColor(R.color.WQP_Black));*/

                    //顯示每筆LinearLayout的派工編號
                    TextView dynamically_rm002;
                    dynamically_rm002 = new TextView(MissionActivity.this);
                    dynamically_rm002.setText("您有新任務(" + JArrayList.get(1).trim() + ")");
                    dynamically_rm002.setPadding(10, 0, 10, 0);
                    dynamically_rm002.setGravity(Gravity.LEFT);
                    dynamically_rm002.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                    dynamically_rm002.setTextColor(mContext.getResources().getColor(R.color.WQP_Gray));

                    //顯示每筆LinearLayout的派工任務日期
                    TextView dynamically_rm910;
                    dynamically_rm910 = new TextView(MissionActivity.this);
                    dynamically_rm910.setText("[" + JArrayList.get(2).trim() + "] ");
                    dynamically_rm910.setPadding(10, 0, 10, 10);
                    dynamically_rm910.setGravity(Gravity.RIGHT);
                    //dynamically_rm910.setWidth(50);
                    dynamically_rm910.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                    dynamically_rm910.setTextColor(mContext.getResources().getColor(R.color.WQP_Gray));

                    //顯示每筆LinearLayout的派工任務日期
                    TextView dynamically_rm003;
                    dynamically_rm003 = new TextView(MissionActivity.this);
                    dynamically_rm003.setText(JArrayList.get(3).trim());
                    dynamically_rm003.setPadding(10, 0, 10, 10);
                    dynamically_rm003.setGravity(Gravity.LEFT);
                    //dynamically_rm003.setWidth(50);
                    dynamically_rm003.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                    dynamically_rm003.setTextColor(mContext.getResources().getColor(R.color.WQP_Gray));

                    //顯示每筆派工任務是否已讀
                    ImageView dynamically_imv;
                    dynamically_imv = new ImageView(MissionActivity.this);
                    dynamically_imv.setImageResource(R.drawable.icon_unread);
                    dynamically_imv.setScaleType(ImageView.ScaleType.CENTER_CROP);

                    //設置每筆LinearLayout的間隔分隔線
                    TextView dynamically_txt0 = new TextView(MissionActivity.this);
                    dynamically_txt0.setBackgroundColor(mContext.getResources().getColor(R.color.WQP_Blue));

                    if(JArrayList.get(4).trim().equals("0")) {
                        dynamically_title.setTypeface(null, Typeface.BOLD);
                        //dynamically_datetime.setTypeface(null, Typeface.BOLD);
                        dynamically_rm002.setTypeface(null, Typeface.BOLD);
                        dynamically_rm910.setTypeface(null, Typeface.BOLD);
                        dynamically_rm003.setTypeface(null, Typeface.BOLD);
                        dynamically_imv.setVisibility(View.VISIBLE);
                    } else if (JArrayList.get(4).trim().equals("1")) {
                        dynamically_title.setTypeface(null, Typeface.NORMAL);
                        //dynamically_datetime.setTypeface(null, Typeface.NORMAL);
                        dynamically_rm002.setTypeface(null, Typeface.NORMAL);
                        dynamically_rm910.setTypeface(null, Typeface.NORMAL);
                        dynamically_rm003.setTypeface(null, Typeface.NORMAL);
                        dynamically_imv.setVisibility(View.INVISIBLE);
                    }

                    //設置每筆TableLayout的Button監聽器、與動態新增Button的ID
                    int loc = 0;
                    for (int i = 0; i < dynamically_btn.length; i++) {
                        if (dynamically_btn[i] == null) {
                            loc = i;
                            break;
                        }
                    }
                    dynamically_btn[loc] = new Button(MissionActivity.this);
                    //dynamically_btn[loc].setText("Google Map");
                    dynamically_btn[loc].setAlpha(0);
                    dynamically_btn[loc].setId(loc);
                    dynamically_btn[loc].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for (int a = 0; a < dynamically_btn.length; a++) {
                                if (v.getId() == dynamically_btn[a].getId()) {
                                    Intent intent_ex = new Intent(MissionActivity.this, MissionDetailActivity.class);
                                    LinearLayout id_llt = (LinearLayout) nav_mission.getChildAt(a);

                                    RelativeLayout big_llt = (RelativeLayout) id_llt.getChildAt(1);
                                    LinearLayout first_rlt = (LinearLayout) big_llt.getChildAt(0);
                                    LinearLayout mission_llt = (LinearLayout) first_rlt.getChildAt(0);
                                    LinearLayout detail_llt = (LinearLayout) mission_llt.getChildAt(1);
                                    TextView no_txt = (TextView) detail_llt.getChildAt(0);
                                    String rm002 = no_txt.getText().toString().replace("您有新任務(", "").replace(")", "");

                                    LinearLayout detail_llt2 = (LinearLayout) mission_llt.getChildAt(2);
                                    TextView type_txt = (TextView) detail_llt2.getChildAt(0);
                                    String rm003 = type_txt.getText().toString();
                                    //將SQL裡的資料傳到MissionDetailActivity
                                    Bundle bundle = new Bundle();
                                    bundle.putString("rm002", rm002);
                                    bundle.putString("rm003", rm003);
                                    Log.e(LOG, "RM002 : " + rm002);
                                    Log.e(LOG, "RM003 : " + rm003);
                                    //intent_gps.putExtra("TitleText", TitleText);//可放所有基本類別
                                    intent_ex.putExtras(bundle);//可放所有基本類別

                                    startActivity(intent_ex);
                                    //進入MissionDetailActivity後 清空gps_llt的資料
                                    nav_mission.removeAllViews();
                                }
                            }
                        }
                    });

                    SimpleDateFormat CurrentTime= new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    String currentDate = new SimpleDateFormat("yyyy-MM-dd  HH:mm", Locale.getDefault()).format(new Date());
                    Log.e(LOG, "DATE : " + currentDate);

                    //若任務逾期未回報該任務底色改為粉色
                    try {
                        Date get_date = CurrentTime.parse(currentDate);
                        Date mission_date = CurrentTime.parse(JArrayList.get(2).trim());

                        if (status.equals("0")) {
                            if(get_date.after(mission_date)) {
                                Log.e(LOG,"逾期");
                                id_rlt.setBackgroundColor(mContext.getResources().getColor(R.color.WQP_Pink));
                            } else {
                                Log.e(LOG,"未逾期");
                            }
                        }

                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    LinearLayout.LayoutParams small_2pm = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.7f);
                    LinearLayout.LayoutParams small_3pm = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 3.0f);
                    LinearLayout.LayoutParams btn_pm = new LinearLayout.LayoutParams(40, 40);
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                    small_llt0.addView(dynamically_title, small_2pm);
                    //small_llt0.addView(dynamically_datetime, small_3pm);
                    small_llt0.addView(dynamically_rm910, small_3pm);
                    small_llt1.addView(dynamically_rm002);
                    small_llt2.addView(dynamically_rm003, small_2pm);

                    big_llt1.addView(small_llt0);
                    big_llt1.addView(small_llt1);
                    big_llt1.addView(small_llt2);
                    big_llt2.addView(dynamically_imv, btn_pm);

                    large_llt.addView(big_llt1, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 9));
                    large_llt.addView(big_llt2, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1));

                    id_rlt.addView(large_llt, params);
                    id_rlt.addView(dynamically_btn[loc], params);

                    XL_llt.addView(dynamically_txt0, LinearLayout.LayoutParams.MATCH_PARENT, 3);
                    XL_llt.addView(id_rlt);

                    nav_mission.addView(XL_llt);

                    LinearLayout first_llt = (LinearLayout) nav_mission.getChildAt(0);
                    first_llt.getChildAt(0).setVisibility(View.GONE);

                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public boolean permissionIsGranted() {

        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void doLocationWork() {
        easyWayLocation.startLocation();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOCATION_SETTING_REQUEST_CODE) {
            easyWayLocation.onActivityResult(resultCode);
        }
    }

    @Override
    public void locationOn() {
        Toast.makeText(this, "Location On", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void currentLocation(Location location) {
        StringBuilder data = new StringBuilder();
        data.append(location.getLatitude());
        data.append(" , ");
        data.append(location.getLongitude());
        latLong.setText(data);
        getLocationDetail.getAddress(location.getLatitude(), location.getLongitude(), "xyz");
    }

    @Override
    public void locationCancelled() {
        Toast.makeText(this, "Location Cancelled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void locationData(LocationData locationData) {
        location.setText(locationData.getFull_address());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(LOG, "onRestart");
        no_report_txt.setBackgroundResource(R.drawable.rectangle_radius_solid);
        reported_txt.setBackgroundResource(R.drawable.rectangle_radius_empty);
        closed_txt.setBackgroundResource(R.drawable.rectangle_radius_empty);
        no_report_txt.setTextColor(mContext.getResources().getColor(R.color.WQP_White));
        reported_txt.setTextColor(mContext.getResources().getColor(R.color.WQP_Blue));
        closed_txt.setTextColor(mContext.getResources().getColor(R.color.WQP_Blue));
        nav_mission.removeAllViews();
        //與OkHttp建立連線(查詢未回報之任務明細)
        sendRequestWithOkHttpForMissionUnReported();
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