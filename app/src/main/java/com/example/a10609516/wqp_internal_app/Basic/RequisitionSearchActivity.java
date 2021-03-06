package com.example.a10609516.wqp_internal_app.Basic;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.a10609516.wqp_internal_app.Works.CalendarActivity;
import com.example.a10609516.wqp_internal_app.Works.EngPointsActivity;
import com.example.a10609516.wqp_internal_app.Works.GPSActivity;
import com.example.a10609516.wqp_internal_app.Works.MissCountActivity;
import com.example.a10609516.wqp_internal_app.Works.MissionActivity;
import com.example.a10609516.wqp_internal_app.Works.PointsActivity;
import com.example.a10609516.wqp_internal_app.Works.ScheduleActivity;

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

public class RequisitionSearchActivity extends WQPToolsActivity {

    private SharedPreferences sp;

    private LinearLayout nav_view;
    private DrawerLayout drawer;
    private Toolbar toolbar;

    private ScrollView search_scv;
    private LinearLayout separate_llt, result_llt;
    private EditText name_edt;
    private Button start_btn, end_btn, search_btn;
    private Spinner category_spinner;

    private ArrayAdapter<String> category_listAdapter;
    private String[] category_type = new String[]{"??????", "???????????????", "???????????????"};

    private String s_department, department, mode;

    private Context mContext = this;

    private String LOG = "RequisitionSearchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requisition_search);
        //?????????????????????
        initViews();
        //Menu???onClickListener
        MenuListener();
        //??????Toolbar
        SetToolBar();
        //????????????Spinner
        Category_Spinner();
    }

    /**
     * ?????????????????????
     */
    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        nav_view = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer_layout);
        search_scv = (ScrollView) findViewById(R.id.search_scv);
        separate_llt = (LinearLayout) findViewById(R.id.separate_llt);
        result_llt = (LinearLayout) findViewById(R.id.result_llt);
        name_edt = (EditText) findViewById(R.id.name_edt);
        start_btn = (Button) findViewById(R.id.start_btn);
        end_btn = (Button) findViewById(R.id.end_btn);
        search_btn = (Button) findViewById(R.id.search_btn);
        category_spinner = (Spinner) findViewById(R.id.category_spinner);

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result_llt.removeAllViews();
                separate_llt.setVisibility(View.VISIBLE);
                result_llt.setVisibility(View.VISIBLE);
                //???OkHttp???????????? ?????????????????????
                sendRequestWithOkHttpForRequisitionSearch();
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
                Intent intent_login = new Intent(RequisitionSearchActivity.this, LoginActivity.class);
                startActivity(intent_login);
            }
        });
    }

    /**
     * ????????????Spinner
     */
    private void Category_Spinner() {
        category_listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, category_type);
        category_listAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category_spinner.setAdapter(category_listAdapter);
    }

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
                search_scv.fullScroll(ScrollView.FOCUS_UP);
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
                search_scv.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    /**
     * ???OkHttp???????????? ?????????????????????
     */
    private void sendRequestWithOkHttpForRequisitionSearch() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                String user_name = name_edt.getText().toString();
                //??????????????????????????????
                String time_start = start_btn.getText().toString();
                String time_end = end_btn.getText().toString();
                String category = String.valueOf(category_spinner.getSelectedItem());

                if (category.equals("??????")) {
                    category = "";
                } else if (category.equals("???????????????")) {
                    category = "1";
                } else {
                    category = "0";
                }

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("USER_NAME", user_name)
                            .add("START_DATE", time_start)
                            .add("END_DATE", time_end)
                            .add("PROGRESS", category)
                            .build();
                    Log.e(LOG, user_name);
                    Log.e(LOG, time_start);
                    Log.e(LOG, time_end);
                    Log.e(LOG, category);

                    Request request = new Request.Builder()
                            //.url("http://192.168.0.172/WQP_OS/RequisitionSearch.php")
                            .url("http://a.wqp-water.com.tw/WQP_OS/RequisitionSearch.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, requestBody.toString());
                    Log.e(LOG, response.toString());
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectForRequisitionSearch(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * ??????JSON??????????????????String??????
     *
     * @param jsonData
     */
    private void parseJSONWithJSONObjectForRequisitionSearch(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON??????????????????
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String type = jsonObject.getString("DEPARTMENT");
                String requester_name = jsonObject.getString("REQUESTER");
                String requester_dept = jsonObject.getString("REQUESTER_DEPT");
                String demand = jsonObject.getString("REQUIRE_CLASS");
                String status = jsonObject.getString("EXECUTION");
                String remark = jsonObject.getString("REMARK");

                Log.e(LOG, type);
                Log.e(LOG, requester_name);
                Log.e(LOG, requester_dept);
                Log.e(LOG, demand);
                Log.e(LOG, status);
                Log.e(LOG, remark);

                //JSONArray??????SearchData??????
                ArrayList<String> JArrayList = new ArrayList<String>();
                JArrayList.add(type);
                JArrayList.add(requester_name);
                JArrayList.add(requester_dept);
                JArrayList.add(demand);
                JArrayList.add(status);
                JArrayList.add(remark);

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
                    LinearLayout small_llt0 = new LinearLayout(RequisitionSearchActivity.this);
                    small_llt0.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout small_llt1 = new LinearLayout(RequisitionSearchActivity.this);
                    small_llt1.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout small_llt2 = new LinearLayout(RequisitionSearchActivity.this);
                    small_llt2.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout small_llt3 = new LinearLayout(RequisitionSearchActivity.this);
                    small_llt3.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout small_llt4 = new LinearLayout(RequisitionSearchActivity.this);
                    small_llt4.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout small_llt5 = new LinearLayout(RequisitionSearchActivity.this);
                    small_llt5.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout small_llt6 = new LinearLayout(RequisitionSearchActivity.this);
                    small_llt6.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout small_llt7 = new LinearLayout(RequisitionSearchActivity.this);
                    small_llt7.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout big_llt = new LinearLayout(RequisitionSearchActivity.this);
                    big_llt.setOrientation(LinearLayout.VERTICAL);
                    big_llt.setBackgroundResource(R.drawable.bg_blue);

                    Bundle jb = msg.getData();
                    ArrayList<String> JArrayList = new ArrayList<String>();
                    //int i = b.getStringArrayList("JSON_data").size();
                    JArrayList = jb.getStringArrayList("JSON_data");

                    if (JArrayList.get(0).equals("1")){
                        department = "???????????????";
                    } else {
                        department = "???????????????";
                    }

                    //????????????LinearLayout??????????????????
                    TextView dynamically_department;
                    dynamically_department = new TextView(RequisitionSearchActivity.this);
                    dynamically_department.setText(department);
                    dynamically_department.setGravity(Gravity.CENTER);
                    //dynamically_department.setWidth(50);
                    dynamically_department.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                    dynamically_department.setTextColor(mContext.getResources().getColor(R.color.WQP_White));

                    if (JArrayList.get(4).equals("1")){
                        mode = "?????????";
                    } else if (JArrayList.get(4).equals("2")){
                        mode = "?????????";
                    } else {
                        mode = "??????";
                    }

                    //????????????LinearLayout???????????????
                    TextView dynamically_mode;
                    dynamically_mode = new TextView(RequisitionSearchActivity.this);
                    dynamically_mode.setText(mode);
                    dynamically_mode.setGravity(Gravity.CENTER);
                    dynamically_mode.setPadding(0, 10, 0, 10);
                    dynamically_mode.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);

                    if (mode.equals("?????????")) {
                        dynamically_mode.setTextColor(Color.rgb(255, 165, 0));
                    } else if (mode.equals("?????????")) {
                        dynamically_mode.setTextColor(Color.rgb(34, 195, 46));
                    } else {
                        dynamically_mode.setTextColor(Color.rgb(220, 20, 60));
                    }

                    //????????????LinearLayout?????????????????????
                    TextView dynamically_division;
                    dynamically_division = new TextView(RequisitionSearchActivity.this);
                    dynamically_division.setText(JArrayList.get(2));
                    dynamically_division.setGravity(Gravity.CENTER);
                    dynamically_division.setPadding(0, 10, 0, 10);
                    dynamically_division.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                    dynamically_division.setTextColor(mContext.getResources().getColor(R.color.WQP_White));

                    //????????????LinearLayout???????????????
                    TextView dynamically_user;
                    dynamically_user = new TextView(RequisitionSearchActivity.this);
                    dynamically_user.setText(JArrayList.get(1));
                    dynamically_user.setGravity(Gravity.CENTER);
                    //dynamically_user.setWidth(50);
                    dynamically_user.setPadding(0, 10, 0, 10);
                    dynamically_user.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                    dynamically_user.setTextColor(mContext.getResources().getColor(R.color.WQP_White));

                    //????????????LinearLayout???????????????
                    TextView dynamically_require;
                    dynamically_require = new TextView(RequisitionSearchActivity.this);
                    dynamically_require.setText(JArrayList.get(3));
                    dynamically_require.setGravity(Gravity.CENTER);
                    //dynamically_require.setWidth(50);
                    dynamically_require.setPadding(0, 10, 0, 10);
                    dynamically_require.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                    dynamically_require.setTextColor(mContext.getResources().getColor(R.color.WQP_White));

                    //????????????LinearLayout???????????????
                    TextView dynamically_remark;
                    dynamically_remark = new TextView(RequisitionSearchActivity.this);
                    dynamically_remark.setText(JArrayList.get(5));
                    dynamically_remark.setGravity(Gravity.CENTER);
                    //dynamically_remark.setWidth(50);
                    dynamically_remark.setPadding(5, 10, 5, 10);
                    dynamically_remark.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                    dynamically_remark.setTextColor(mContext.getResources().getColor(R.color.WQP_White));

                    //????????????TableLayout????????????
                    TextView dynamically_txt = new TextView(RequisitionSearchActivity.this);
                    dynamically_txt.setBackgroundColor(Color.rgb(220, 220, 220));

                    //????????????TableLayout????????????
                    TextView dynamically_txt1 = new TextView(RequisitionSearchActivity.this);
                    dynamically_txt1.setBackgroundColor(Color.rgb(224, 255, 255));
                    //????????????TableLayout????????????
                    TextView dynamically_txt2 = new TextView(RequisitionSearchActivity.this);
                    dynamically_txt2.setBackgroundColor(Color.rgb(224, 255, 255));
                    //????????????TableLayout????????????
                    TextView dynamically_txt3 = new TextView(RequisitionSearchActivity.this);
                    dynamically_txt3.setBackgroundColor(Color.rgb(224, 255, 255));
                    //????????????TableLayout????????????
                    TextView dynamically_txt4 = new TextView(RequisitionSearchActivity.this);
                    dynamically_txt4.setBackgroundColor(Color.rgb(224, 255, 255));
                    //????????????TableLayout????????????
                    TextView dynamically_txt5 = new TextView(RequisitionSearchActivity.this);
                    dynamically_txt5.setBackgroundColor(Color.rgb(224, 255, 255));
                    //????????????TableLayout????????????
                    TextView dynamically_txt6 = new TextView(RequisitionSearchActivity.this);
                    dynamically_txt6.setBackgroundColor(Color.rgb(224, 255, 255));

                    LinearLayout.LayoutParams small_pm = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);

                    small_llt0.addView(dynamically_txt, LinearLayout.LayoutParams.MATCH_PARENT, 3);
                    small_llt1.addView(dynamically_department, small_pm);
                    small_llt1.addView(dynamically_txt1, 3, LinearLayout.LayoutParams.MATCH_PARENT);
                    small_llt1.addView(dynamically_mode, small_pm);
                    small_llt2.addView(dynamically_txt2, LinearLayout.LayoutParams.MATCH_PARENT, 3);
                    small_llt3.addView(dynamically_division, small_pm);
                    small_llt3.addView(dynamically_txt3, 3, LinearLayout.LayoutParams.MATCH_PARENT);
                    small_llt3.addView(dynamically_user, small_pm);
                    small_llt4.addView(dynamically_txt4, LinearLayout.LayoutParams.MATCH_PARENT, 3);
                    small_llt5.addView(dynamically_require, small_pm);
                    small_llt6.addView(dynamically_txt5, LinearLayout.LayoutParams.MATCH_PARENT, 3);
                    small_llt7.addView(dynamically_remark, small_pm);

                    big_llt.addView(small_llt0);
                    big_llt.addView(small_llt1);
                    big_llt.addView(small_llt2);
                    big_llt.addView(small_llt3);
                    big_llt.addView(small_llt4);
                    big_llt.addView(small_llt5);
                    big_llt.addView(small_llt6);
                    big_llt.addView(small_llt7);

                    result_llt.addView(big_llt);
                    LinearLayout first_llt = (LinearLayout) result_llt.getChildAt(0);
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