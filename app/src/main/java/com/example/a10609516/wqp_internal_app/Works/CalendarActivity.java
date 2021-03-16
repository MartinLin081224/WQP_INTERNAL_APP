package com.example.a10609516.wqp_internal_app.Works;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CalendarActivity extends WQPToolsActivity {

    private SharedPreferences sp;

    private LinearLayout nav_view;
    private DrawerLayout drawer;
    private Toolbar toolbar;

    private LinearLayout search_llt, calendar_llt;
    private Spinner company_spinner;
    private TextView date_txt, search_up_txt, search_down_txt, dynamically_txt, dynamically_type,
            dynamically_customer, dynamically_phone, dynamically_address;
    private Button work_date_btn, last_btn, search_btn, next_btn;
    private ProgressBar dynamically_PGTime;

    int x, y;
    private String date, last_date, next_date;
    private SimpleDateFormat simpleDateFormat;

    private Context mContext = this;

    private String LOG = "CalendarActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(LOG, "onCreate");
        setContentView(R.layout.activity_calendar);
        //取得控制項物件
        initViews();
        //Menu的onClickListener
        MenuListener();
        //設置Toolbar
        SetToolBar();
        //分公司的Spinner下拉選單
        WorkTypeSpinner();
        //獲取當天日期
        GetDate();
        //與OKHttp連線取得行事曆資料
        sendRequestWithOKHttpOfCalendar();
    }

    /**
     * 取得控制項物件
     */
    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        nav_view = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer_layout);
        search_llt = (LinearLayout) findViewById(R.id.search_llt);
        calendar_llt = (LinearLayout) findViewById(R.id.calendar_llt);
        company_spinner = (Spinner) findViewById(R.id.company_spinner);
        date_txt = (TextView) findViewById(R.id.date_txt);
        search_up_txt = (TextView) findViewById(R.id.search_up_txt);
        search_down_txt = (TextView) findViewById(R.id.search_down_txt);
        work_date_btn = (Button) findViewById(R.id.work_date_btn);
        last_btn = (Button) findViewById(R.id.last_btn);
        search_btn = (Button) findViewById(R.id.search_btn);
        next_btn = (Button) findViewById(R.id.next_btn);

        //點查詢收起search_llt
        search_up_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_llt.setVisibility(View.GONE);
                search_up_txt.setVisibility(View.GONE);
                search_down_txt.setVisibility(View.VISIBLE);
            }
        });

        //點查詢打開search_llt
        search_down_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_llt.setVisibility(View.VISIBLE);
                search_up_txt.setVisibility(View.VISIBLE);
                search_down_txt.setVisibility(View.GONE);
            }
        });

        //查詢日期選擇器的日期
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date_txt.setText(work_date_btn.getText().toString());
                calendar_llt.removeAllViews();
                sendRequestWithOKHttpOfSearch();
            }
        });

        //查詢往上一日
        last_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar_llt.removeAllViews();

                LastDate();
                date_txt.setText(last_date);
                work_date_btn.setText(last_date);

                sendRequestWithOKHttpOfLast();
            }
        });

        //查詢往下一日
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar_llt.removeAllViews();

                NextDate();
                date_txt.setText(next_date);
                work_date_btn.setText(next_date);

                sendRequestWithOKHttpOfNext();
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

        toolbar.setTitle("派工行事曆");
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
                Intent intent_login = new Intent(CalendarActivity.this, LoginActivity.class);
                startActivity(intent_login);
            }
        });
    }

    /**
     * 分公司的Spinner下拉選單
     */
    private void WorkTypeSpinner() {
        final String[] select = {"請選擇  ", "台北拓亞鈦,快撥28868", "桃園分公司,快撥31338", "新竹分公司,快撥37888", "台中分公司,快撥42088", "高雄分公司,快撥73568"};
        ArrayAdapter<String> selectList = new ArrayAdapter<String>(CalendarActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                select);
        company_spinner.setAdapter(selectList);
    }

    /**
     * 獲取當天日期
     */
    private void GetDate() {
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = simpleDateFormat.format(new java.util.Date());
        date_txt.setText(date);
        work_date_btn.setText(date);
    }

    /**
     * 獲取date_txt的前一日
     */
    private void LastDate() {
        String view_date = work_date_btn.getText().toString();
        Log.e(LOG, view_date);
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(simpleDateFormat.parse(view_date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, -1);
        //SimpleDateFormat outLast = new SimpleDateFormat("yyyy-MM-dd");
        last_date = simpleDateFormat.format(c.getTime());
    }

    /**
     * 獲取date_txt的後一日
     */
    private void NextDate() {
        String view_date = work_date_btn.getText().toString();
        Log.e(LOG, view_date);
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(simpleDateFormat.parse(view_date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, 1);
        //SimpleDateFormat outNext = new SimpleDateFormat("yyyy-MM-dd");
        next_date = simpleDateFormat.format(c.getTime());
    }

    /**
     * 與OkHttp(CalendarData)建立連線_new
     */
    private void sendRequestWithOKHttpOfCalendar() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //接收LoginActivity傳過來的值
                SharedPreferences user_id = getSharedPreferences("user_id", MODE_PRIVATE);
                String user_id_data = user_id.getString("ID", "");
                Log.e(LOG, user_id_data);

                String getdate = date_txt.getText().toString();

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("User_id", user_id_data)
                            .add("Work_date", getdate)
                            .build();
                    Log.e(LOG, getdate);
                    Request request = new Request.Builder()
                            //.url("http://192.168.0.172/WQP_OS/CalendarData.php")
                            .url("http://a.wqp-water.com.tw/WQP_OS/CalendarData.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectOfCalendar(responseData);
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
    private void parseJSONWithJSONObjectOfCalendar(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON格式改為字串
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String esvd_booking_period = jsonObject.getString("START_TIME");
                String esvd_booking_period_end = jsonObject.getString("END_TIME");
                String WORK_TYPE_NAME = jsonObject.getString("WORK_TYPE");
                String ESV_CONTACTOR = jsonObject.getString("CONTACT_PERSON");
                String ESV_TEL_NO1 = jsonObject.getString("ESV_TEL_NO1");
                String ESV_ADDRESS = jsonObject.getString("ESV_ADDRESS");
                String User_name = jsonObject.getString("EMPLOYEE_A");
                String User_name2 = jsonObject.getString("EMPLOYEE_B");
                String Group_name = jsonObject.getString("COMPANY");
                String turn_id = jsonObject.getString("ID");

                //JSONArray加入SearchData資料
                ArrayList<String> JArrayList = new ArrayList<String>();
                JArrayList.add(esvd_booking_period);
                JArrayList.add(esvd_booking_period_end);
                JArrayList.add(WORK_TYPE_NAME);
                JArrayList.add(ESV_CONTACTOR);
                JArrayList.add(ESV_TEL_NO1);
                JArrayList.add(ESV_ADDRESS);
                JArrayList.add(User_name);
                JArrayList.add(User_name2);
                JArrayList.add(Group_name);
                JArrayList.add(turn_id);

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
     * 與OkHttp(CalendarData)建立連線_new
     */
    private void sendRequestWithOKHttpOfSearch() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //接收LoginActivity傳過來的值
                SharedPreferences user_id = getSharedPreferences("user_id", MODE_PRIVATE);
                String user_id_data = user_id.getString("ID", "");
                Log.e(LOG, user_id_data);

                String s_date = work_date_btn.getText().toString();
                String group_name = String.valueOf(company_spinner.getSelectedItem()).substring(0, 5);
                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("User_id", user_id_data)
                            .add("Work_date", s_date)
                            .add("Group_name", group_name)
                            .build();
                    Log.e(LOG, user_id_data);
                    Log.e(LOG, s_date);
                    Log.e(LOG, group_name);
                    Request request = new Request.Builder()
                            //.url("http://192.168.0.172/WQP_OS/CalendarData.php")
                            .url("http://a.wqp-water.com.tw/WQP_OS/CalendarData.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectOfSearch(responseData);
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
    private void parseJSONWithJSONObjectOfSearch(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON格式改為字串
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String esvd_booking_period = jsonObject.getString("START_TIME");
                String esvd_booking_period_end = jsonObject.getString("END_TIME");
                String WORK_TYPE_NAME = jsonObject.getString("WORK_TYPE");
                String ESV_CONTACTOR = jsonObject.getString("CONTACT_PERSON");
                String ESV_TEL_NO1 = jsonObject.getString("ESV_TEL_NO1");
                String ESV_ADDRESS = jsonObject.getString("ESV_ADDRESS");
                String User_name = jsonObject.getString("EMPLOYEE_A");
                String User_name2 = jsonObject.getString("EMPLOYEE_B");
                String Group_name = jsonObject.getString("COMPANY");
                String turn_id = jsonObject.getString("ID");

                //JSONArray加入SearchData資料
                ArrayList<String> JArrayList = new ArrayList<String>();
                JArrayList.add(esvd_booking_period);
                JArrayList.add(esvd_booking_period_end);
                JArrayList.add(WORK_TYPE_NAME);
                JArrayList.add(ESV_CONTACTOR);
                JArrayList.add(ESV_TEL_NO1);
                JArrayList.add(ESV_ADDRESS);
                JArrayList.add(User_name);
                JArrayList.add(User_name2);
                JArrayList.add(Group_name);
                JArrayList.add(turn_id);

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
     * 與OkHttp(CalendarData)建立連線_new
     */
    private void sendRequestWithOKHttpOfLast() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String group_name = String.valueOf(company_spinner.getSelectedItem()).substring(0, 5);
                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("Work_date", last_date)
                            .add("Group_name", group_name)
                            .build();
                    Log.e(LOG, last_date);
                    Request request = new Request.Builder()
                            //.url("http://192.168.0.172/WQP_OS/CalendarData.php")
                            .url("http://a.wqp-water.com.tw/WQP_OS/CalendarData.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectOfLast(responseData);
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
    private void parseJSONWithJSONObjectOfLast(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON格式改為字串
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String esvd_booking_period = jsonObject.getString("START_TIME");
                String esvd_booking_period_end = jsonObject.getString("END_TIME");
                String WORK_TYPE_NAME = jsonObject.getString("WORK_TYPE");
                String ESV_CONTACTOR = jsonObject.getString("CONTACT_PERSON");
                String ESV_TEL_NO1 = jsonObject.getString("ESV_TEL_NO1");
                String ESV_ADDRESS = jsonObject.getString("ESV_ADDRESS");
                String User_name = jsonObject.getString("EMPLOYEE_A");
                String User_name2 = jsonObject.getString("EMPLOYEE_B");
                String Group_name = jsonObject.getString("COMPANY");
                String turn_id = jsonObject.getString("ID");

                //JSONArray加入SearchData資料
                ArrayList<String> JArrayList = new ArrayList<String>();
                JArrayList.add(esvd_booking_period);
                JArrayList.add(esvd_booking_period_end);
                JArrayList.add(WORK_TYPE_NAME);
                JArrayList.add(ESV_CONTACTOR);
                JArrayList.add(ESV_TEL_NO1);
                JArrayList.add(ESV_ADDRESS);
                JArrayList.add(User_name);
                JArrayList.add(User_name2);
                JArrayList.add(Group_name);
                JArrayList.add(turn_id);

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
     * 與OkHttp(CalendarData)建立連線_new
     */
    private void sendRequestWithOKHttpOfNext() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String group_name = String.valueOf(company_spinner.getSelectedItem()).substring(0, 5);
                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("Work_date", next_date)
                            .add("Group_name", group_name)
                            .build();
                    Log.e(LOG, next_date);
                    Request request = new Request.Builder()
                            //.url("http://192.168.0.172/WQP_OS/CalendarData.php")
                            .url("http://a.wqp-water.com.tw/WQP_OS/CalendarData.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectOfNext(responseData);
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
    private void parseJSONWithJSONObjectOfNext(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON格式改為字串
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String esvd_booking_period = jsonObject.getString("START_TIME");
                String esvd_booking_period_end = jsonObject.getString("END_TIME");
                String WORK_TYPE_NAME = jsonObject.getString("WORK_TYPE");
                String ESV_CONTACTOR = jsonObject.getString("CONTACT_PERSON");
                String ESV_TEL_NO1 = jsonObject.getString("ESV_TEL_NO1");
                String ESV_ADDRESS = jsonObject.getString("ESV_ADDRESS");
                String User_name = jsonObject.getString("EMPLOYEE_A");
                String User_name2 = jsonObject.getString("EMPLOYEE_B");
                String Group_name = jsonObject.getString("COMPANY");
                String turn_id = jsonObject.getString("ID");

                //JSONArray加入SearchData資料
                ArrayList<String> JArrayList = new ArrayList<String>();
                JArrayList.add(esvd_booking_period);
                JArrayList.add(esvd_booking_period_end);
                JArrayList.add(WORK_TYPE_NAME);
                JArrayList.add(ESV_CONTACTOR);
                JArrayList.add(ESV_TEL_NO1);
                JArrayList.add(ESV_ADDRESS);
                JArrayList.add(User_name);
                JArrayList.add(User_name2);
                JArrayList.add(Group_name);
                JArrayList.add(turn_id);

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
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    //製作Dip
                    Resources resources = getResources();
                    float name_Dip = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 36, resources.getDisplayMetrics());
                    int name_dip = Math.round(name_Dip);
                    float type_Dip = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 125, resources.getDisplayMetrics());
                    int type_dip = Math.round(type_Dip);
                    float pg_Dip = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 26, resources.getDisplayMetrics());
                    int pg_dip = Math.round(pg_Dip);

                    LinearLayout big_llt = new LinearLayout(CalendarActivity.this);
                    big_llt.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout name_llt = new LinearLayout(CalendarActivity.this);
                    name_llt.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout small_llt1 = new LinearLayout(CalendarActivity.this);
                    small_llt1.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout small_llt2 = new LinearLayout(CalendarActivity.this);
                    small_llt2.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout dynamically_llt = new LinearLayout(CalendarActivity.this);
                    dynamically_llt.setBackgroundResource(R.drawable.calendar_h_divider);
                    HorizontalScrollView dynamically_hsv = new HorizontalScrollView(CalendarActivity.this);

                    Bundle jb = msg.getData();
                    ArrayList<String> JArrayList = new ArrayList<String>();
                    JArrayList = jb.getStringArrayList("JSON_data");
                    for (int i = 0; i < jb.getStringArrayList("JSON_data").size(); i++) {
                        dynamically_txt = new TextView(CalendarActivity.this);
                        dynamically_txt.setText(JArrayList.get(6).replace("A", ""));
                        dynamically_txt.setTextColor(mContext.getResources().getColor(R.color.WQP_White));
                        dynamically_txt.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 24);
                        dynamically_txt.setGravity(Gravity.LEFT);
                        dynamically_txt.setPadding(10, 2, 0, 0);
                        dynamically_txt.setWidth(name_dip);
                        dynamically_txt.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
                        dynamically_txt.getPaint().setAntiAlias(true);

                        name_llt.addView(dynamically_txt, LinearLayout.LayoutParams.MATCH_PARENT, name_dip);
                        if (dynamically_txt.getText().equals("")) {
                            dynamically_txt.setVisibility(View.GONE);
                        }

                        dynamically_type = new TextView(CalendarActivity.this);
                        dynamically_type.setText(JArrayList.get(0) + " " + JArrayList.get(2));
                        dynamically_type.setTextColor(mContext.getResources().getColor(R.color.WQP_White));
                        dynamically_type.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                        dynamically_type.setGravity(Gravity.LEFT);
                        dynamically_type.setPadding(5, 0, 0, 4);
                        //dynamically_type.setWidth(type_dip);
                        if (JArrayList.get(0).equals("09:00")) {
                            x = 0;
                        }
                        if (JArrayList.get(0).equals("10:00")) {
                            x = 10;
                        }
                        if (JArrayList.get(0).equals("11:00")) {
                            x = 20;
                        }
                        if (JArrayList.get(0).equals("12:00")) {
                            x = 30;
                        }
                        if (JArrayList.get(0).equals("13:00")) {
                            x = 40;
                        }
                        if (JArrayList.get(0).equals("14:00")) {
                            x = 50;
                        }
                        if (JArrayList.get(0).equals("15:00")) {
                            x = 60;
                        }
                        if (JArrayList.get(0).equals("16:00")) {
                            x = 70;
                        }
                        if (JArrayList.get(0).equals("17:00")) {
                            x = 80;
                        }
                        if (JArrayList.get(0).equals("18:00")) {
                            x = 90;
                        }
                        if (JArrayList.get(0).equals("19:00")) {
                            x = 100;
                        }
                        if (JArrayList.get(0).equals("20:00")) {
                            x = 110;
                        }
                        if (JArrayList.get(0).equals("21:00")) {
                            x = 120;
                        }
                        if (JArrayList.get(1).equals("09:00")) {
                            y = 10;
                        }
                        if (JArrayList.get(1).equals("10:00")) {
                            y = 20;
                        }
                        if (JArrayList.get(1).equals("11:00")) {
                            y = 30;
                        }
                        if (JArrayList.get(1).equals("12:00")) {
                            y = 40;
                        }
                        if (JArrayList.get(1).equals("13:00")) {
                            y = 50;
                        }
                        if (JArrayList.get(1).equals("14:00")) {
                            y = 60;
                        }
                        if (JArrayList.get(1).equals("15:00")) {
                            y = 70;
                        }
                        if (JArrayList.get(1).equals("16:00")) {
                            y = 80;
                        }
                        if (JArrayList.get(1).equals("17:00")) {
                            y = 90;
                        }
                        if (JArrayList.get(1).equals("18:00")) {
                            y = 100;
                        }
                        if (JArrayList.get(1).equals("19:00")) {
                            y = 110;
                        }
                        if (JArrayList.get(1).equals("20:00")) {
                            y = 120;
                        }
                        if (JArrayList.get(1).equals("21:00")) {
                            y = 130;
                        }
                        dynamically_PGTime = new ProgressBar(CalendarActivity.this, null, android.R.attr.progressBarStyleHorizontal);
                        dynamically_PGTime.setMax(130);
                        dynamically_PGTime.setProgress(x);
                        dynamically_PGTime.setSecondaryProgress(y);
                        dynamically_PGTime.setProgressDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.progressbar_bg, null));
                        //dynamically_PGTime.setBackgroundResource(R.drawable.progressbar_bg);

                        small_llt1.addView(dynamically_type, type_dip, pg_dip);
                        small_llt1.addView(dynamically_PGTime, LinearLayout.LayoutParams.MATCH_PARENT, pg_dip);

                        dynamically_customer = new TextView(CalendarActivity.this);
                        dynamically_customer.setText("客戶名稱 : " + JArrayList.get(3));
                        dynamically_customer.setTextColor(mContext.getResources().getColor(R.color.WQP_White));
                        dynamically_customer.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                        dynamically_customer.setGravity(Gravity.LEFT);
                        dynamically_customer.setPadding(5, 0, 0, 0);

                        dynamically_phone = new TextView(CalendarActivity.this);
                        dynamically_phone.setText(" 聯絡電話 : " + JArrayList.get(4));
                        dynamically_phone.setTextColor(mContext.getResources().getColor(R.color.WQP_White));
                        dynamically_phone.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                        dynamically_phone.setGravity(Gravity.LEFT);
                        dynamically_phone.setPadding(5, 0, 0, 0);

                        dynamically_address = new TextView(CalendarActivity.this);
                        dynamically_address.setText(" 派工地址 : " + JArrayList.get(5));
                        dynamically_address.setTextColor(mContext.getResources().getColor(R.color.WQP_White));
                        dynamically_address.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                        dynamically_address.setGravity(Gravity.LEFT);
                        dynamically_address.setPadding(5, 0, 0, 0);
                        //dynamically_address.setTextIsSelectable(true);

                        small_llt2.addView(dynamically_customer);
                        small_llt2.addView(dynamically_phone);
                        small_llt2.addView(dynamically_address);
                    }

                    for (int a = 3; a <= 23; a++) {
                        small_llt2.getChildAt(a).setVisibility(View.GONE);
                    }
                    dynamically_hsv.addView(small_llt2);

                    big_llt.addView(name_llt, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    big_llt.addView(small_llt1, LinearLayout.LayoutParams.MATCH_PARENT, pg_dip);
                    big_llt.addView(dynamically_hsv, LinearLayout.LayoutParams.WRAP_CONTENT, pg_dip);
                    big_llt.addView(dynamically_llt);

                    calendar_llt.addView(big_llt, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
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