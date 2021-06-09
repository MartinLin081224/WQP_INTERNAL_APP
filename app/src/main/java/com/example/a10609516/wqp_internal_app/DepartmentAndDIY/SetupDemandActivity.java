package com.example.a10609516.wqp_internal_app.DepartmentAndDIY;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.a10609516.wqp_internal_app.Manager.InventoryActivity;
import com.example.a10609516.wqp_internal_app.Manager.OrderSearchActivity;
import com.example.a10609516.wqp_internal_app.R;
import com.example.a10609516.wqp_internal_app.Tools.WQPToolsActivity;
import com.example.a10609516.wqp_internal_app.Works.CalendarActivity;
import com.example.a10609516.wqp_internal_app.Works.EngPointsActivity;
import com.example.a10609516.wqp_internal_app.Works.GPSActivity;
import com.example.a10609516.wqp_internal_app.Works.MissCountActivity;
import com.example.a10609516.wqp_internal_app.Works.MissionActivity;
import com.example.a10609516.wqp_internal_app.Works.MissionDetailActivity;
import com.example.a10609516.wqp_internal_app.Works.PointsActivity;
import com.example.a10609516.wqp_internal_app.Works.ScheduleActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SetupDemandActivity extends WQPToolsActivity {

    private SharedPreferences sp;
    private TextView date_txt, no_setup_txt, has_been_setup_txt, mark_setup_txt;
    private Button start_btn, end_btn;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Button[] dynamically_btn;
    private String status;

    private LinearLayout nav_view, nav_setup, date_llt;
    private DrawerLayout drawer;
    private Toolbar toolbar;

    private Context mContext = this;

    private String LOG = "SetupDemandActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_demand);
        //取得控制項物件
        initViews();
        //Menu的onClickListener
        MenuListener();
        //設置Toolbar
        SetToolBar();
        nav_setup.removeAllViews();
        status = "0";
        //與OkHttp建立連線(查詢未建立施工單之訂單)
        sendRequestWithOkHttpForDemandNotBuild();
    }

    /**
     * 取得控制項物件
     */
    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        nav_view = findViewById(R.id.nav_view);
        nav_setup = findViewById(R.id.nav_setup);
        date_llt = findViewById(R.id.date_llt);
        drawer = findViewById(R.id.drawer_layout);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        date_txt = findViewById(R.id.date_txt);
        start_btn = findViewById(R.id.start_btn);
        end_btn = findViewById(R.id.end_btn);
        no_setup_txt = findViewById(R.id.no_setup_txt);
        has_been_setup_txt = findViewById(R.id.has_been_setup_txt);
        mark_setup_txt = findViewById(R.id.mark_setup_txt);

        //UI介面下拉刷新
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(false);
                nav_setup.removeAllViews();
                if (status.equals("0")) {
                    //與OkHttp建立連線(查詢未建立施工單之訂單)
                    sendRequestWithOkHttpForDemandNotBuild();
                } else if (status.equals("1")) {
                    //與OkHttp建立連線(查詢已回報之任務明細)
                    //sendRequestWithOkHttpForMissionReported();
                } else if (status.equals("2")) {
                    //與OkHttp建立連線(查詢已標記之任務明細)
                    //sendRequestWithOkHttpForMissionReported();
                }
            }
        });

        date_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (date_llt.getVisibility() == View.GONE) {
                    date_llt.setVisibility(View.VISIBLE);
                    date_txt.setText("單據日期 : ");
                } else {
                    date_llt.setVisibility(View.GONE);
                    date_txt.setText("單據日期▼");
                }

            }
        });

        no_setup_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                no_setup_txt.setBackgroundResource(R.drawable.rectangle_radius_solid);
                has_been_setup_txt.setBackgroundResource(R.drawable.rectangle_radius_empty);
                mark_setup_txt.setBackgroundResource(R.drawable.rectangle_radius_empty);
                no_setup_txt.setTextColor(mContext.getResources().getColor(R.color.WQP_White));
                has_been_setup_txt.setTextColor(mContext.getResources().getColor(R.color.WQP_Blue));
                mark_setup_txt.setTextColor(mContext.getResources().getColor(R.color.WQP_Blue));
                status = "0";
                nav_setup.removeAllViews();

                try{
                    // delay 1 second
                    Thread.sleep(300);
                    //與OkHttp建立連線(查詢未回報之任務明細)
                    //sendRequestWithOkHttpForMissionUnReported();
                } catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        });
        has_been_setup_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                has_been_setup_txt.setBackgroundResource(R.drawable.rectangle_radius_solid);
                no_setup_txt.setBackgroundResource(R.drawable.rectangle_radius_empty);
                mark_setup_txt.setBackgroundResource(R.drawable.rectangle_radius_empty);
                has_been_setup_txt.setTextColor(mContext.getResources().getColor(R.color.WQP_White));
                no_setup_txt.setTextColor(mContext.getResources().getColor(R.color.WQP_Blue));
                mark_setup_txt.setTextColor(mContext.getResources().getColor(R.color.WQP_Blue));
                status = "1";
                nav_setup.removeAllViews();

                try{
                    // delay 1 second
                    Thread.sleep(300);
                    //與OkHttp建立連線(查詢未回報之任務明細)
                    //sendRequestWithOkHttpForMissionUnReported();
                } catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        });

        mark_setup_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mark_setup_txt.setBackgroundResource(R.drawable.rectangle_radius_solid);
                no_setup_txt.setBackgroundResource(R.drawable.rectangle_radius_empty);
                has_been_setup_txt.setBackgroundResource(R.drawable.rectangle_radius_empty);
                mark_setup_txt.setTextColor(mContext.getResources().getColor(R.color.WQP_White));
                no_setup_txt.setTextColor(mContext.getResources().getColor(R.color.WQP_Blue));
                has_been_setup_txt.setTextColor(mContext.getResources().getColor(R.color.WQP_Blue));
                status = "2";
                nav_setup.removeAllViews();

                try{
                    // delay 1 second
                    Thread.sleep(300);
                    //與OkHttp建立連線(查詢未回報之任務明細)
                    //sendRequestWithOkHttpForMissionUnReported();
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
                    Intent intent32 = new Intent(mContext, SetupDemandActivity.class);
                    mContext.startActivity(intent32);
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

        toolbar.setTitle("安裝查詢表");
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
                Intent intent_login = new Intent(SetupDemandActivity.this, LoginActivity.class);
                startActivity(intent_login);
            }
        });
    }

    /**
     * 與OkHttp建立連線(查詢未建立施工單之訂單)
     */
    private void sendRequestWithOkHttpForDemandNotBuild() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //接收LoginActivity傳過來的值
                SharedPreferences user_id = getSharedPreferences("user_id", MODE_PRIVATE);
                String user_id_data = user_id.getString("ID", "");
                String start_date = start_btn.getText().toString();
                String end_date = end_btn.getText().toString();
                Log.e(LOG, user_id_data);
                Log.e(LOG, start_date);
                Log.e(LOG, end_date);

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("U_ACC", user_id_data)
                            .add("START_DATE", start_date)
                            .add("END_DATE", end_date)
                            .build();
                    Log.e(LOG, user_id_data);
                    Request request = new Request.Builder()
                            .url("http://a.wqp-water.com.tw/WQP_OS/DemandSearchNotBuild.php")
                            //.url("http://192.168.0.172/WQP_OS/DemandSearchNotBuild.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, requestBody.toString());
                    Log.e(LOG, response.toString());
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectForDemandNotBuild(responseData);
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
    private void parseJSONWithJSONObjectForDemandNotBuild(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            dynamically_btn = new Button[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON格式改為字串
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String TC001 = jsonObject.getString("TC001");
                String TC002 = jsonObject.getString("TC002");
                String MV001 = jsonObject.getString("MV001");
                String MV002 = jsonObject.getString("MV002");
                String TC003 = jsonObject.getString("TC003");
                String COMP2 = jsonObject.getString("COMP2");
                String TC004 = jsonObject.getString("TC004");
                String TC043 = jsonObject.getString("TC043");
                String TA_STATUS = jsonObject.getString("TA_STATUS");
                String SEN_ID = jsonObject.getString("SEN_ID");
                String SEN_COUNT_ID = jsonObject.getString("SEN_COUNT_ID");
                /*String DEVICE = jsonObject.getString("DEVICE");*/

                //JSONArray加入SearchData資料
                ArrayList<String> JArrayList = new ArrayList<String>();
                JArrayList.add(TC001);//訂單單別
                JArrayList.add(TC002);//訂單單號
                JArrayList.add(MV001);//員工編號
                JArrayList.add(MV002);//員工姓名
                JArrayList.add(TC003);//單據日期
                JArrayList.add(COMP2);//公司別
                JArrayList.add(TC004);//客戶編號
                JArrayList.add(TC043);//客戶全名
                JArrayList.add(TA_STATUS);//施工單狀態
                JArrayList.add(SEN_ID);//施工單流水號ID
                JArrayList.add(SEN_COUNT_ID);//施工單數量ID
                /*JArrayList.add(DEVICE);*/

                Log.e(LOG, TC001);
                Log.e(LOG, TC002);
                Log.e(LOG, MV001);
                Log.e(LOG, MV002);
                Log.e(LOG, TC003);
                Log.e(LOG, COMP2);
                Log.e(LOG, TC004);
                Log.e(LOG, TC043);
                Log.e(LOG, TA_STATUS);
                Log.e(LOG, SEN_ID);
                Log.e(LOG, SEN_COUNT_ID);
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
                    LinearLayout small_llt0 = new LinearLayout(SetupDemandActivity.this);
                    small_llt0.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout small_llt1 = new LinearLayout(SetupDemandActivity.this);
                    small_llt1.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout small_llt2 = new LinearLayout(SetupDemandActivity.this);
                    small_llt2.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout big_llt1 = new LinearLayout(SetupDemandActivity.this);
                    big_llt1.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout big_llt2 = new LinearLayout(SetupDemandActivity.this);
                    big_llt2.setOrientation(LinearLayout.VERTICAL);
                    big_llt2.setGravity(Gravity.CENTER);
                    LinearLayout large_llt = new LinearLayout(SetupDemandActivity.this);
                    large_llt.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout XL_llt = new LinearLayout(SetupDemandActivity.this);
                    XL_llt.setOrientation(LinearLayout.VERTICAL);
                    RelativeLayout id_rlt = new RelativeLayout(SetupDemandActivity.this);

                    Bundle jb = msg.getData();
                    ArrayList<String> JArrayList = new ArrayList<String>();
                    //int i = b.getStringArrayList("JSON_data").size();
                    JArrayList = jb.getStringArrayList("JSON_data");

                    //顯示每筆LinearLayout的訂單單別單號
                    TextView dynamically_TC001002;
                    dynamically_TC001002 = new TextView(SetupDemandActivity.this);
                    dynamically_TC001002.setText("訂單:" + JArrayList.get(0).trim() + "-" + JArrayList.get(1).trim() );
                    dynamically_TC001002.setPadding(10, 0, 10, 0);
                    dynamically_TC001002.setGravity(Gravity.LEFT);
                    dynamically_TC001002.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                    dynamically_TC001002.setTextColor(mContext.getResources().getColor(R.color.WQP_Gray));

                    //顯示每筆LinearLayout的訂單公司別
                    TextView dynamically_company;
                    dynamically_company = new TextView(SetupDemandActivity.this);
                    dynamically_company.setText("[" + JArrayList.get(5).trim() + "] ");
                    dynamically_company.setPadding(10, 0, 10, 10);
                    dynamically_company.setGravity(Gravity.RIGHT);
                    //dynamically_company.setWidth(50);
                    dynamically_company.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                    dynamically_company.setTextColor(mContext.getResources().getColor(R.color.WQP_Gray));

                    //顯示每筆LinearLayout的業務
                    TextView dynamically_MV001002;
                    dynamically_MV001002 = new TextView(SetupDemandActivity.this);
                    dynamically_MV001002.setText("業務:" + JArrayList.get(2).trim() + ":|:" + JArrayList.get(3).trim());
                    dynamically_MV001002.setPadding(10, 0, 10, 10);
                    dynamically_MV001002.setGravity(Gravity.LEFT);
                    //dynamically_MV001002.setWidth(50);
                    dynamically_MV001002.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                    dynamically_MV001002.setTextColor(mContext.getResources().getColor(R.color.WQP_Gray));

                    //顯示每筆LinearLayout的客戶
                    TextView dynamically_TC004043;
                    dynamically_TC004043 = new TextView(SetupDemandActivity.this);
                    dynamically_TC004043.setText("客戶:" + JArrayList.get(6).trim() + ":|:" + JArrayList.get(7).trim());
                    dynamically_TC004043.setPadding(10, 0, 10, 10);
                    dynamically_TC004043.setGravity(Gravity.LEFT);
                    //dynamically_TC004043.setWidth(50);
                    dynamically_TC004043.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                    dynamically_TC004043.setTextColor(mContext.getResources().getColor(R.color.WQP_Gray));

                    //設置每筆LinearLayout的間隔分隔線
                    TextView dynamically_txt0 = new TextView(SetupDemandActivity.this);
                    dynamically_txt0.setBackgroundColor(mContext.getResources().getColor(R.color.WQP_Blue));

                    //設置每筆TableLayout的Button監聽器、與動態新增Button的ID
                    int loc = 0;
                    for (int i = 0; i < dynamically_btn.length; i++) {
                        if (dynamically_btn[i] == null) {
                            loc = i;
                            break;
                        }
                    }
                    dynamically_btn[loc] = new Button(SetupDemandActivity.this);
                    //dynamically_btn[loc].setText("Google Map");
                    dynamically_btn[loc].setAlpha(0);
                    dynamically_btn[loc].setId(loc);
                    dynamically_btn[loc].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for (int a = 0; a < dynamically_btn.length; a++) {
                                if (v.getId() == dynamically_btn[a].getId()) {
                                    Intent intent_ex = new Intent(SetupDemandActivity.this, SetupMasterActivity.class);
                                    LinearLayout id_llt = (LinearLayout) nav_setup.getChildAt(a);

                                    RelativeLayout big_llt = (RelativeLayout) id_llt.getChildAt(1);
                                    LinearLayout first_rlt = (LinearLayout) big_llt.getChildAt(0);
                                    LinearLayout mission_llt = (LinearLayout) first_rlt.getChildAt(0);
                                    LinearLayout detail_llt = (LinearLayout) mission_llt.getChildAt(0);
                                    TextView no_txt = (TextView) detail_llt.getChildAt(0);
                                    Log.e(LOG, "訂單 : " + no_txt.getText().toString());
                                    String TC001002 = no_txt.getText().toString().replace("訂單:", "");
                                    String TC001 = TC001002.substring(0, TC001002.indexOf("-"));
                                    String TC002 = TC001002.substring(TC001002.indexOf("-") + 1);
                                    Log.e(LOG, "單別 : " + TC001);
                                    Log.e(LOG, "單號 : " + TC002);

                                    LinearLayout detail_llt2 = (LinearLayout) mission_llt.getChildAt(2);
                                    TextView cum_txt = (TextView) detail_llt2.getChildAt(0);
                                    String TC004043 = cum_txt.getText().toString().replace("客戶:", "");
                                    String TC004 = TC004043.substring(0, TC004043.indexOf(":|:"));
                                    String TC043 = TC004043.substring(TC004043.indexOf(":|:") + 3);
                                    Log.e(LOG, "客代 : " + TC004);
                                    Log.e(LOG, "客戶 : " + TC043);

                                    //將SQL裡的資料傳到MapsActivity
                                    Bundle bundle = new Bundle();
                                    bundle.putString("TC001002", TC001002);
                                    bundle.putString("TC004043", TC004043);
                                    Log.e(LOG, "訂單 : " + TC001002);
                                    Log.e(LOG, "客代 : " + TC004043);
                                    //intent_gps.putExtra("TitleText", TitleText);//可放所有基本類別
                                    intent_ex.putExtras(bundle);//可放所有基本類別

                                    startActivity(intent_ex);
                                    //進入MapsActivity後 清空gps_llt的資料
                                    nav_setup.removeAllViews();
                                }
                            }
                        }
                    });

                    LinearLayout.LayoutParams small_2pm = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.7f);
                    LinearLayout.LayoutParams small_3pm = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 3.0f);
                    LinearLayout.LayoutParams btn_pm = new LinearLayout.LayoutParams(40, 40);
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                    small_llt0.addView(dynamically_TC001002, small_3pm);
                    small_llt0.addView(dynamically_company, small_2pm);
                    small_llt1.addView(dynamically_MV001002);
                    small_llt2.addView(dynamically_TC004043);

                    big_llt1.addView(small_llt0);
                    big_llt1.addView(small_llt1);
                    big_llt1.addView(small_llt2);

                    large_llt.addView(big_llt1, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 9));
                    large_llt.addView(big_llt2, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0));

                    id_rlt.addView(large_llt, params);
                    id_rlt.addView(dynamically_btn[loc], params);

                    XL_llt.addView(dynamically_txt0, LinearLayout.LayoutParams.MATCH_PARENT, 3);
                    XL_llt.addView(id_rlt);

                    nav_setup.addView(XL_llt);

                    LinearLayout first_llt = (LinearLayout) nav_setup.getChildAt(0);
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
        no_setup_txt.setBackgroundResource(R.drawable.rectangle_radius_solid);
        has_been_setup_txt.setBackgroundResource(R.drawable.rectangle_radius_empty);
        mark_setup_txt.setBackgroundResource(R.drawable.rectangle_radius_empty);
        no_setup_txt.setTextColor(mContext.getResources().getColor(R.color.WQP_White));
        has_been_setup_txt.setTextColor(mContext.getResources().getColor(R.color.WQP_Blue));
        mark_setup_txt.setTextColor(mContext.getResources().getColor(R.color.WQP_Blue));
        nav_setup.removeAllViews();
        //與OkHttp建立連線(查詢未回報之任務明細)
        //sendRequestWithOkHttpForMissionUnReported();
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