package com.example.a10609516.wqp_internal_app.Works;

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
import android.widget.ScrollView;
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
import com.example.a10609516.wqp_internal_app.Tools.WQPClickListener;
import com.example.a10609516.wqp_internal_app.Tools.WQPToolsActivity;

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

public class EngPointsActivity extends WQPToolsActivity {

    private SharedPreferences sp;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ScrollView points_scv;
    private LinearLayout nav_view, points_llt;
    private DrawerLayout drawer;
    private Toolbar toolbar;

    private String LOG = "EngPointsActivity";
    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eng_points);
        //?????????????????????
        initViews();
        //Menu???onClickListener
        MenuListener();
        //??????Toolbar
        SetToolBar();
        //???OKHttp??????(?????????????????? WorkPointsDetail.php)
        sendRequestWithOkHttpForWorkPointsDetail();
    }

    /**
     * ?????????????????????
     */
    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        nav_view = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer_layout);
        points_scv = (ScrollView) findViewById(R.id.points_scv);
        points_llt = (LinearLayout) findViewById(R.id.points_llt);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);

        //UI??????????????????
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(false);
                points_llt.removeAllViews();
                //???OKHttp??????(?????????????????? WorkPointsDetail.php)
                sendRequestWithOkHttpForWorkPointsDetail();
            }
        });
    }

    /**
     * Menu???onClickListener
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
                    Toast.makeText(mContext, "???????????????????????????", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(mContext, "????????????????????????????????????", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(mContext, "?????????????????????????????????", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(mContext, "????????????????????????????????????", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(mContext, "?????????????????????????????????", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(mContext, "?????????????????????????????????", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(mContext, "?????????????????????????????????", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(mContext, "??????????????????????????????????????????", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(mContext, "???????????????GPS??????????????????", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(mContext, "????????????????????????????????????", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(mContext, "???????????????????????????????????????", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(mContext, "???????????????/????????????????????????", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(mContext, "????????????????????????????????????", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(mContext, "??????????????????????????????", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(mContext, "??????????????????????????????", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(mContext, "????????????????????????????????????", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(mContext, "?????????????????????????????????", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(mContext, "?????????????????????????????????", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * ??????Toolbar
     */
    private void SetToolBar() {
        TextView log_out_txt = findViewById(R.id.log_out_txt);
        TextView id_txt = findViewById(R.id.id_txt);

        //??????LoginActivity???????????????
        SharedPreferences user_name = getSharedPreferences("user_name", MODE_PRIVATE);
        String U_name = user_name.getString("U_name", "");
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                id_txt.setText(U_name);
            }
        });

        toolbar.setTitle("????????????");
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
                //?????????????????????????????????????????????????????????
                sp.edit().putBoolean("auto_isCheck",false).commit();
                sp.edit().putBoolean("rem_isCheck",false).commit();
                sp.edit().putString("USER_NAME", "").commit();
                sp.edit().putString("PASSWORD", "").commit();
                sp.edit().putString("user_name", "").commit();
                sp.edit().putString("MENU", "").commit();

                finish();
                Intent intent_login = new Intent(EngPointsActivity.this, LoginActivity.class);
                startActivity(intent_login);
            }
        });
    }

    /**
     * ???OKHttp??????(??????????????????)_new
     */
    private void sendRequestWithOkHttpForWorkPointsDetail() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //??????LoginActivity???????????????
                SharedPreferences user_id = getSharedPreferences("user_id", MODE_PRIVATE);
                String user_id_data = user_id.getString("ID", "");
                Log.e(LOG, user_id_data);

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("U_ACC", user_id_data)
                            .build();
                    Request request = new Request.Builder()
                            //.url("http://192.168.0.172/WQP_OS/MissionPointsDetail.php")
                            .url("http://a.wqp-water.com.tw/WQP_OS/MissionPointsDetail.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectForWorkPointsDetail(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * ??????????????????(A???B)
     *
     * @param jsonData
     */
    private void parseJSONWithJSONObjectForWorkPointsDetail(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON??????????????????
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String service_date = jsonObject.getString("WISH_DATETIME");
                String esvd_service_no = jsonObject.getString("SERVICE_NO");
                String work_type_name = jsonObject.getString("WORK_TYPE");
                String esvd_a_point = jsonObject.getString("A_POINTS");
                String esvd_b_point = jsonObject.getString("B_POINTS");
                String esvd_d_point = jsonObject.getString("D_POINTS");
                String esvd_eng_money = jsonObject.getString("POINTS_MONEY");

                Log.e(LOG, work_type_name);
                Log.e(LOG, esvd_a_point);
                Log.e(LOG, esvd_b_point);
                Log.e(LOG, esvd_d_point);
                Log.e(LOG, esvd_eng_money);

                //JSONArray??????SearchData??????
                ArrayList<String> JArrayList = new ArrayList<String>();
                JArrayList.add(service_date);
                JArrayList.add(esvd_service_no);
                JArrayList.add(work_type_name);
                JArrayList.add(esvd_a_point);
                JArrayList.add(esvd_b_point);
                JArrayList.add(esvd_d_point);
                JArrayList.add(esvd_eng_money);

                //HandlerMessage??????UI
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
     * ??????UI
     */
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    LinearLayout small_llt0 = new LinearLayout(EngPointsActivity.this);
                    small_llt0.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout small_llt1 = new LinearLayout(EngPointsActivity.this);
                    small_llt1.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout small_llt2 = new LinearLayout(EngPointsActivity.this);
                    small_llt2.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout small_llt3 = new LinearLayout(EngPointsActivity.this);
                    small_llt3.setOrientation(LinearLayout.HORIZONTAL);
                    small_llt3.setBackgroundResource(R.drawable.bg_layered_orange);
                    LinearLayout small_llt4 = new LinearLayout(EngPointsActivity.this);
                    small_llt4.setOrientation(LinearLayout.HORIZONTAL);
                    small_llt4.setBackgroundResource(R.drawable.bg_layered_orange);
                    LinearLayout line_llt1 = new LinearLayout(EngPointsActivity.this);
                    line_llt1.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout line_llt2 = new LinearLayout(EngPointsActivity.this);
                    line_llt2.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout line_llt3 = new LinearLayout(EngPointsActivity.this);
                    line_llt3.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout line_llt4 = new LinearLayout(EngPointsActivity.this);
                    line_llt4.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout big_llt = new LinearLayout(EngPointsActivity.this);
                    big_llt.setOrientation(LinearLayout.VERTICAL);
                    big_llt.setBackgroundResource(R.drawable.bg_blue);
                    LinearLayout empty_llt1 = new LinearLayout(EngPointsActivity.this);
                    empty_llt1.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout empty_llt2 = new LinearLayout(EngPointsActivity.this);
                    empty_llt2.setOrientation(LinearLayout.VERTICAL);

                    Bundle jb = msg.getData();
                    ArrayList<String> JArrayList = new ArrayList<String>();
                    //int i = b.getStringArrayList("JSON_data").size();
                    JArrayList = jb.getStringArrayList("JSON_data");

                    //????????????LinearLayout?????????
                    TextView dynamically_date;
                    dynamically_date = new TextView(EngPointsActivity.this);
                    dynamically_date.setText("???????????? : " + JArrayList.get(0).substring(0, 16));
                    dynamically_date.setGravity(Gravity.CENTER);
                    dynamically_date.setPadding(0, 10, 0, 10);
                    dynamically_date.setTextColor(mContext.getResources().getColor(R.color.WQP_White));
                    //dynamically_date.setWidth(50);
                    dynamically_date.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);

                    //????????????LinearLayout???????????????
                    TextView dynamically_type;
                    dynamically_type = new TextView(EngPointsActivity.this);
                    dynamically_type.setText("???????????? : " + JArrayList.get(2));
                    dynamically_type.setGravity(Gravity.CENTER);
                    dynamically_type.setPadding(0, 10, 0, 10);
                    dynamically_type.setTextColor(mContext.getResources().getColor(R.color.WQP_White));
                    //dynamically_type.setWidth(50);
                    dynamically_type.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);

                    //????????????LinearLayout???????????????
                    TextView dynamically_no;
                    dynamically_no = new TextView(EngPointsActivity.this);
                    dynamically_no.setText("???????????? : " + JArrayList.get(1));
                    dynamically_no.setGravity(Gravity.CENTER);
                    dynamically_no.setPadding(0, 10, 0, 10);
                    dynamically_no.setTextColor(mContext.getResources().getColor(R.color.WQP_White));
                    //dynamically_no.setWidth(50);
                    dynamically_no.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);

                    //????????????LinearLayout???A??????
                    TextView dynamically_a_points;
                    dynamically_a_points = new TextView(EngPointsActivity.this);
                    dynamically_a_points.setText("A?????? : " + JArrayList.get(3));
                    dynamically_a_points.setGravity(Gravity.CENTER);
                    dynamically_a_points.setPadding(0, 10, 0, 10);
                    dynamically_a_points.setTextColor(mContext.getResources().getColor(R.color.WQP_White));
                    //dynamically_a_points.setWidth(50);
                    dynamically_a_points.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);

                    //????????????LinearLayout???B??????
                    TextView dynamically_b_points;
                    dynamically_b_points = new TextView(EngPointsActivity.this);
                    dynamically_b_points.setText("B?????? : " + JArrayList.get(4));
                    dynamically_b_points.setGravity(Gravity.CENTER);
                    dynamically_b_points.setPadding(0, 10, 0, 10);
                    dynamically_b_points.setTextColor(mContext.getResources().getColor(R.color.WQP_White));
                    //dynamically_b_points.setWidth(50);
                    dynamically_b_points.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);

                    //????????????LinearLayout???D??????
                    TextView dynamically_d_points;
                    dynamically_d_points = new TextView(EngPointsActivity.this);
                    dynamically_d_points.setText("D?????? : " + JArrayList.get(5));
                    dynamically_d_points.setGravity(Gravity.CENTER);
                    dynamically_d_points.setPadding(0, 10, 0, 10);
                    dynamically_d_points.setTextColor(mContext.getResources().getColor(R.color.WQP_White));
                    //dynamically_d_points.setWidth(50);
                    dynamically_d_points.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);

                    //????????????LinearLayout???????????????
                    TextView dynamically_eng_money;
                    dynamically_eng_money = new TextView(EngPointsActivity.this);
                    dynamically_eng_money.setText("???????????? : " + JArrayList.get(6));
                    dynamically_eng_money.setGravity(Gravity.CENTER);
                    dynamically_eng_money.setPadding(0, 10, 0, 10);
                    dynamically_eng_money.setTextColor(mContext.getResources().getColor(R.color.WQP_White));
                    //dynamically_eng_money.setWidth(50);
                    dynamically_eng_money.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);

                    //????????????TableLayout????????????
                    TextView dynamically_txt1 = new TextView(EngPointsActivity.this);
                    dynamically_txt1.setBackgroundColor(Color.rgb(224, 255, 255));
                    //????????????TableLayout????????????
                    TextView dynamically_txt2 = new TextView(EngPointsActivity.this);
                    dynamically_txt2.setBackgroundColor(Color.rgb(224, 255, 255));
                    //????????????TableLayout????????????
                    TextView dynamically_txt3 = new TextView(EngPointsActivity.this);
                    dynamically_txt3.setBackgroundColor(Color.rgb(224, 255, 255));
                    //????????????TableLayout????????????
                    TextView dynamically_txt4 = new TextView(EngPointsActivity.this);
                    dynamically_txt4.setBackgroundColor(Color.rgb(224, 255, 255));
                    //????????????TableLayout????????????
                    TextView dynamically_txt5 = new TextView(EngPointsActivity.this);
                    dynamically_txt5.setBackgroundColor(Color.rgb(224, 255, 255));
                    //????????????TableLayout????????????
                    TextView dynamically_txt6 = new TextView(EngPointsActivity.this);
                    dynamically_txt6.setBackgroundColor(Color.rgb(224, 255, 255));

                    LinearLayout.LayoutParams small_pm = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);

                    small_llt0.addView(dynamically_date, small_pm);
                    line_llt1.addView(dynamically_txt1, LinearLayout.LayoutParams.MATCH_PARENT, 3);
                    small_llt1.addView(dynamically_no, small_pm);
                    line_llt2.addView(dynamically_txt2, LinearLayout.LayoutParams.MATCH_PARENT, 3);
                    small_llt2.addView(dynamically_type, small_pm);
                    line_llt3.addView(dynamically_txt3, LinearLayout.LayoutParams.MATCH_PARENT, 3);
                    small_llt3.addView(dynamically_a_points, small_pm);
                    small_llt3.addView(dynamically_txt4, 3, LinearLayout.LayoutParams.MATCH_PARENT);
                    small_llt3.addView(dynamically_b_points, small_pm);
                    line_llt4.addView(dynamically_txt5, LinearLayout.LayoutParams.MATCH_PARENT, 3);
                    small_llt4.addView(dynamically_d_points, small_pm);
                    small_llt4.addView(dynamically_txt6, 3, LinearLayout.LayoutParams.MATCH_PARENT);
                    small_llt4.addView(dynamically_eng_money, small_pm);

                    big_llt.addView(small_llt0);
                    big_llt.addView(line_llt1);
                    big_llt.addView(small_llt1);
                    big_llt.addView(line_llt2);
                    big_llt.addView(small_llt2);
                    big_llt.addView(line_llt3);
                    big_llt.addView(small_llt3);
                    big_llt.addView(line_llt4);
                    big_llt.addView(small_llt4);
                    big_llt.addView(empty_llt2, LinearLayout.LayoutParams.MATCH_PARENT, 12);

                    points_llt.addView(big_llt);
                    points_llt.addView(empty_llt1, LinearLayout.LayoutParams.MATCH_PARENT, 10);

                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * ????????????????????????
     *
     * @param view
     */
    public void GoTopBtn(View view) {
        Handler mHandler = new Handler();
        mHandler.post(new Runnable() {
            public void run() {
                //????????????????????????
                points_scv.fullScroll(ScrollView.FOCUS_UP);
            }
        });
    }

    /**
     * ????????????????????????
     *
     * @param view
     */
    public void GoDownBtn(View view) {
        Handler mHandler = new Handler();
        mHandler.post(new Runnable() {
            public void run() {
                //????????????????????????
                points_scv.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
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