package com.example.a10609516.wqp_internal_app.Clerk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a10609516.wqp_internal_app.Basic.HomeActivity;
import com.example.a10609516.wqp_internal_app.Basic.LoginActivity;
import com.example.a10609516.wqp_internal_app.Basic.RequisitionActivity;
import com.example.a10609516.wqp_internal_app.Basic.RequisitionSearchActivity;
import com.example.a10609516.wqp_internal_app.Basic.VersionActivity;
import com.example.a10609516.wqp_internal_app.Boss.ApplyExchangeActivity;
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

public class AuthorizeActivity extends WQPToolsActivity {

    private TableLayout quotation_master_tb;
    private LinearLayout quotation_detail_llt, yes_no_llt, cancellation_llt, separate_llt;
    private TextView dynamically_master_title, dynamically_master_txt, mode_txt, dynamically_item_txt, dynamically_product_txt
            , dynamically_count_txt, dynamically_price_txt, dynamically_total_txt, dynamically_money_txt;
    private Button approved_button, reject_button, cancellation_button;
    String TA002TB002, ResponseText1, ResponseText2, company, quotation_type, COMPANY
            , TA001, TA001TA002, TA004, TA005, TA004TA006, TA009, TA010, TA011, TA020
            , TA021, PERCENTAGE, TRANSATION, PROCESS, LOCKING, TB003, TB004TB005, TB007TB008
            , TB009, TB010, TB025TB024, user_id_data;

    private SharedPreferences sp;

    private LinearLayout nav_view;
    private DrawerLayout drawer;
    private Toolbar toolbar;

    private Context mContext = this;
    private String LOG = "AuthorizeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorize_activity);
        //?????????????????????
        initViews();
        //Menu???onClickListener
        MenuListener();
        //??????Toolbar
        SetToolBar();
        //??????QuotationActivity??????????????????
        GetResponseData();
        //??????QuotationMaster.php OKHttp??????
        sendRequestWithOkHttpForMaster();
        //??????QuotationMaster.php OKHttp??????
        sendRequestWithOkHttpForDetail();
        //???OkHttp????????????(QuotationRead.php)
        sendRequestWithOkHttpForQuotationRead();
    }

    /**
     * ?????????????????????
     */
    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        nav_view = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer_layout);
        quotation_master_tb = (TableLayout) findViewById(R.id.quotation_master_tb);
        quotation_detail_llt = (LinearLayout) findViewById(R.id.quotation_detail_llt);
        yes_no_llt = (LinearLayout) findViewById(R.id.yes_no_llt);
        cancellation_llt = (LinearLayout) findViewById(R.id.cancellation_llt);
        separate_llt = (LinearLayout) findViewById(R.id.separate_llt);
        mode_txt = (TextView) findViewById(R.id.mode_txt);
        approved_button = (Button) findViewById(R.id.approved_button);
        reject_button = (Button) findViewById(R.id.reject_button);
        cancellation_button = (Button) findViewById(R.id.cancellation_button);

        //???????????????Button????????????
        approved_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TA005.toString().substring(0, 8).equals(user_id_data.toString())) {
                    Toast.makeText(AuthorizeActivity.this, "???????????????????????????", Toast.LENGTH_SHORT).show();
                }else {
                    //??????QuotationApproved.php OKHttp??????
                    sendRequestWithOkHttpForApproved();
                    Log.e(LOG,TA005.toString().substring(0, 8));
                    finish();
                }
            }
        });
        //???????????????Button????????????
        reject_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //??????QuotationReject.php OKHttp??????
                if(TA005.toString().substring(0, 8).equals(user_id_data.toString())) {
                    Toast.makeText(AuthorizeActivity.this, "???????????????????????????", Toast.LENGTH_SHORT).show();
                }else {
                    sendRequestWithOkHttpForReject();
                    Log.e(LOG, TA005.toString().substring(0, 8));
                    finish();
                }
            }
        });
        //?????????????????????Button????????????
        cancellation_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //??????QuotationReject.php OKHttp??????
                if(TA005.toString().substring(0, 8).equals(user_id_data.toString())) {
                    Toast.makeText(AuthorizeActivity.this, "???????????????????????????", Toast.LENGTH_SHORT).show();
                }else {
                    sendRequestWithOkHttpForReject();
                    Log.e(LOG, TA005.toString().substring(0, 8));
                    finish();
                }
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

        toolbar.setTitle("???????????????");
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
                Intent intent_login = new Intent(AuthorizeActivity.this, LoginActivity.class);
                startActivity(intent_login);
            }
        });
    }

    /**
     * ??????SearchActivity??????????????????
     */
    private void GetResponseData() {
        Bundle bundle = getIntent().getExtras();
        TA002TB002 = bundle.getString("ResponseText" + 0);
        ResponseText1 = bundle.getString("ResponseText" + 1);
        ResponseText2 = bundle.getString("ResponseText" + 2);
        company = bundle.getString("company");
        quotation_type = bundle.getString("quotation_type");

        if (company.toString().equals("??????")) {
            company = "WQP";
        } else {
            company = "TYT";
        }

        Log.e(LOG, TA002TB002);
        Log.e(LOG, ResponseText1);
        Log.e(LOG, ResponseText2);
        Log.e(LOG, company);
        Log.e(LOG, quotation_type);
    }

    /**
     * ???OkHttp????????????(???????????????)
     */
    private void sendRequestWithOkHttpForQuotationRead() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //??????LoginActivity???????????????
                SharedPreferences user_id = getSharedPreferences("user_id", MODE_PRIVATE);
                String user_id_data = user_id.getString("ID", "");
                Log.e(LOG, user_id_data);
                Log.e(LOG, company);
                Log.e(LOG, quotation_type);
                Log.e(LOG, TA002TB002);

                String quotation_read = "1";

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("User_id", user_id_data)
                            .add("COMPANY", company)
                            .add("TA001", quotation_type)
                            .add("TA002", TA002TB002)
                            .add("Q_READ", quotation_read)
                            .build();
                    Log.e(LOG, user_id_data);
                    Log.e(LOG, quotation_type);
                    Log.e(LOG, TA002TB002);
                    Log.e(LOG, company);
                    Log.e(LOG, quotation_read);
                    Request request = new Request.Builder()
                            //.url("http://192.168.0.172/WQP_OS/QuotationRead.php")
                            .url("http://a.wqp-water.com.tw/WQP_OS/QuotationRead.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * ???OkHttp????????????(???????????????)
     */
    private void sendRequestWithOkHttpForMaster() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //??????LoginActivity???????????????
                SharedPreferences user_id = getSharedPreferences("user_id", MODE_PRIVATE);
                user_id_data = user_id.getString("ID", "");
                Log.e(LOG, user_id_data);
                Log.e(LOG, company);
                Log.e(LOG, quotation_type);
                Log.e(LOG, TA002TB002);

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("User_id", user_id_data)
                            .add("COMPANY", company)
                            .add("TA001", quotation_type)
                            .add("TA002", TA002TB002)
                            .build();
                    Request request = new Request.Builder()
                            //.url("http://192.168.0.172/WQP_OS/QuotationMaster.php")
                            .url("http://a.wqp-water.com.tw/WQP_OS/QuotationMaster.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectForMaster(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * ??????JSON??????????????????String??????(???????????????)
     *
     * @param jsonData
     */
    private void parseJSONWithJSONObjectForMaster(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON??????????????????
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                COMPANY = jsonObject.getString("COMPANY");
                //TA001 = jsonObject.getString("??????");
                TA001TA002 = jsonObject.getString("NUMBER");
                TA005 = jsonObject.getString("CLERK");
                //TA004 = jsonObject.getString("????????????");
                TA004TA006 = jsonObject.getString("CUST");
                TA009 = jsonObject.getString("Q_MONEY");
                TRANSATION = jsonObject.getString("PAY_MODE");
                TA011 = jsonObject.getString("PAY_CONDITION");
                TA010 = jsonObject.getString("MONEY_CONDITION");
                TA020 = jsonObject.getString("NOTE1");
                TA021 = jsonObject.getString("NOTE2");
                PERCENTAGE = jsonObject.getString("PERCENTAGE");
                PROCESS = jsonObject.getString("PROCESS");
                LOCKING = jsonObject.getString("LOCK");

                if (COMPANY.toString().equals("WQP")) {
                    COMPANY = "??????";
                } else {
                    COMPANY = "?????????";
                }
                //JSONArray??????SearchData??????
                ArrayList<String> JArrayList = new ArrayList<String>();
                JArrayList.add(COMPANY);
                //JArrayList.add(TA001);
                JArrayList.add(TA001TA002);
                JArrayList.add(TA005);
                //JArrayList.add(TA004);
                JArrayList.add(TA004TA006);
                JArrayList.add(TA009);
                JArrayList.add(TRANSATION);
                JArrayList.add(TA011);
                JArrayList.add(TA010);
                JArrayList.add(TA020);
                JArrayList.add(TA021);
                JArrayList.add(PERCENTAGE);

                Log.e(LOG, TA001TA002);
                Log.e(LOG, "LOCK:" + LOCKING);

                //HandlerMessage??????UI
                Bundle b = new Bundle();
                b.putStringArrayList("JSON_data", JArrayList);
                Message msg = mHandlerForMaster.obtainMessage();
                msg.setData(b);
                msg.what = 1;
                msg.sendToTarget();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ??????UI(QuotationMaster)
     */
    Handler mHandlerForMaster = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //??????????????????????????????
            //??????????????????????????????Button?????????
            mode_txt.setText(PROCESS);
            if (PROCESS.toString().equals("?????????")) {
                mode_txt.setTextColor(Color.rgb(255, 165, 0));
            } else if (PROCESS.toString().equals("????????????")) {
                mode_txt.setTextColor(Color.rgb(34, 195, 46));
                cancellation_llt.setVisibility(View.VISIBLE);
                yes_no_llt.setVisibility(View.GONE);
            } else {
                mode_txt.setTextColor(Color.rgb(220, 20, 60));
                cancellation_llt.setVisibility(View.GONE);
                yes_no_llt.setVisibility(View.GONE);
                separate_llt.setVisibility(View.GONE);
            }

            //?????????Title
            final String[] title_array = {"?????????"/*, "??????"*/, "??????", "????????????", /*"????????????",*/
                    "????????????", "????????????", "????????????", "????????????", "????????????"
                    , "?????????", "?????????", "??????"};
            switch (msg.what) {
                case 1:
                    //????????????????????????TableLayout,?????????TableRow????????????TableLayout
                    //????????????????????????
                    quotation_master_tb.setStretchAllColumns(true);
                    //?????????TableLayout???TableRow
                    TableRow big_tbr = new TableRow(AuthorizeActivity.this);
                    //????????????????????????TableLayout
                    TableLayout small_tb = new TableLayout(AuthorizeActivity.this);
                    //??????????????????????????????
                    small_tb.setStretchAllColumns(true);
                    //small_tb.setBackgroundResource(R.drawable.bg_layered_green);

                    Bundle jb = msg.getData();
                    ArrayList<String> JArrayList = new ArrayList<String>();
                    //int i = b.getStringArrayList("JSON_data").size();
                    JArrayList = jb.getStringArrayList("JSON_data");

                    for (int i = 0; i < jb.getStringArrayList("JSON_data").size(); i++) {
                        //????????????TableLayout???Title
                        //TextView dynamically_title;
                        dynamically_master_title = new TextView(AuthorizeActivity.this);
                        dynamically_master_title.setText(title_array[i].toString());
                        dynamically_master_title.setPadding(20, 5, 0, 2);
                        dynamically_master_title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                        dynamically_master_title.setGravity(Gravity.CENTER_VERTICAL);
                        dynamically_master_title.setTextColor(mContext.getResources().getColor(R.color.WQP_White));

                        //????????????TableLayout???SQL??????
                        //TextView dynamically_txt;
                        dynamically_master_txt = new TextView(AuthorizeActivity.this);
                        dynamically_master_txt.setText(JArrayList.get(i));
                        dynamically_master_txt.setPadding(10, 5, 0, 2);
                        dynamically_master_txt.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                        dynamically_master_txt.setGravity(Gravity.CENTER_VERTICAL);
                        dynamically_master_txt.setTextColor(mContext.getResources().getColor(R.color.WQP_White));

                        //?????????TableLayout???TableRow
                        TableRow tr1 = new TableRow(AuthorizeActivity.this);
                        tr1.setWeightSum(2);
                        tr1.addView(dynamically_master_title, new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 0));
                        tr1.addView(dynamically_master_txt, new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 2));

                        small_tb.addView(tr1);
                    }
                    if (PERCENTAGE.toString().equals("")) {
                        small_tb.getChildAt(10).setVisibility(View.GONE);
                    }
                    //?????????????????????????????????
                    small_tb.getChildAt(4).setBackgroundColor(mContext.getResources().getColor(R.color.WQP_Orange));
                    TableRow money_tbr = (TableRow) small_tb.getChildAt(4);
                    TextView money_txt = (TextView) money_tbr.getChildAt(1);
                    money_txt.setTypeface(null, Typeface.BOLD);
                    money_txt.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 19);

                    big_tbr.addView(small_tb);

                    TableRow.LayoutParams the_param3;
                    the_param3 = (TableRow.LayoutParams) small_tb.getLayoutParams();
                    the_param3.span = 2;
                    the_param3.width = TableRow.LayoutParams.MATCH_PARENT;
                    small_tb.setLayoutParams(the_param3);

                    quotation_master_tb.addView(big_tbr);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * ???OkHttp????????????(???????????????)
     */
    private void sendRequestWithOkHttpForDetail() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //??????LoginActivity???????????????
                SharedPreferences user_id = getSharedPreferences("user_id", MODE_PRIVATE);
                String user_id_data = user_id.getString("ID", "");
                Log.e(LOG, user_id_data);
                Log.e(LOG, company);
                Log.e(LOG, quotation_type);
                Log.e(LOG, TA002TB002);

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("User_id", user_id_data)
                            .add("COMPANY", company)
                            .add("TB001", quotation_type)
                            .add("TB002", TA002TB002)
                            .build();
                    Request request = new Request.Builder()
                            //.url("http://192.168.0.172/WQP_OS/QuotationDetail.php")
                            .url("http://a.wqp-water.com.tw/WQP_OS/QuotationDetail.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectForDetail(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * ??????JSON??????????????????String??????(???????????????)
     *
     * @param jsonData
     */
    private void parseJSONWithJSONObjectForDetail(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON??????????????????
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                TB003 = jsonObject.getString("ITEMS");
                TB004TB005 = jsonObject.getString("TB004TB005");
                TB007TB008 = jsonObject.getString("QUANTITY");
                TB009 = jsonObject.getString("PRICE");
                TB010 = jsonObject.getString("TOTAL_MONEY");
                TB025TB024 = jsonObject.getString("Q_MONEY");

                //JSONArray??????SearchData??????
                ArrayList<String> JArrayList = new ArrayList<String>();
                JArrayList.add(TB003);
                JArrayList.add(TB004TB005);
                JArrayList.add(TB007TB008);
                JArrayList.add(TB009);
                JArrayList.add(TB010);
                JArrayList.add(TB025TB024);

                Log.e(LOG, TB025TB024);

                //HandlerMessage??????UI
                Bundle b = new Bundle();
                b.putStringArrayList("JSON_data", JArrayList);
                Message msg = mHandlerForDetail.obtainMessage();
                msg.setData(b);
                msg.what = 1;
                msg.sendToTarget();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ??????UI(QuotationDetail)
     */
    Handler mHandlerForDetail = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    //??????Dip
                    Resources resources = getResources();
                    float product_Dip = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, resources.getDisplayMetrics());
                    int product_dip = Math.round(product_Dip);

                    LinearLayout big_llt = new LinearLayout(AuthorizeActivity.this);
                    big_llt.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout item_llt = new LinearLayout(AuthorizeActivity.this);
                    item_llt.setOrientation(LinearLayout.HORIZONTAL);
                    item_llt.setBackgroundColor(mContext.getResources().getColor(R.color.WQP_Orange));
                    LinearLayout product_llt = new LinearLayout(AuthorizeActivity.this);
                    product_llt.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout count_llt = new LinearLayout(AuthorizeActivity.this);
                    count_llt.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout price_llt = new LinearLayout(AuthorizeActivity.this);
                    price_llt.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout total_llt = new LinearLayout(AuthorizeActivity.this);
                    total_llt.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout money_llt = new LinearLayout(AuthorizeActivity.this);
                    money_llt.setOrientation(LinearLayout.HORIZONTAL);
                    HorizontalScrollView dynamically_hsv = new HorizontalScrollView(AuthorizeActivity.this);

                    Bundle jb = msg.getData();
                    ArrayList<String> JArrayList = new ArrayList<String>();
                    //int i = b.getStringArrayList("JSON_data").size();
                    JArrayList = jb.getStringArrayList("JSON_data");

                    dynamically_item_txt = new TextView(AuthorizeActivity.this);
                    dynamically_item_txt.setText(JArrayList.get(0));
                    dynamically_item_txt.setTextColor(mContext.getResources().getColor(R.color.WQP_White));
                    dynamically_item_txt.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                    dynamically_item_txt.setGravity(Gravity.LEFT);
                    dynamically_item_txt.setPadding(10, 2, 0, 0);
                    item_llt.addView(dynamically_item_txt, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    dynamically_product_txt = new TextView(AuthorizeActivity.this);
                    dynamically_product_txt.setText("??????:"+JArrayList.get(1));
                    dynamically_product_txt.setTextColor(mContext.getResources().getColor(R.color.WQP_White));
                    dynamically_product_txt.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                    dynamically_product_txt.setGravity(Gravity.CENTER);
                    dynamically_product_txt.setPadding(10, 2, 0, 0);
                    product_llt.addView(dynamically_product_txt, LinearLayout.LayoutParams.MATCH_PARENT, product_dip);

                    dynamically_count_txt = new TextView(AuthorizeActivity.this);
                    dynamically_count_txt.setText("??????: "+JArrayList.get(2));
                    dynamically_count_txt.setTextColor(mContext.getResources().getColor(R.color.WQP_White));
                    dynamically_count_txt.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                    dynamically_count_txt.setGravity(Gravity.LEFT);
                    dynamically_count_txt.setPadding(10, 2, 0, 0);
                    count_llt.addView(dynamically_count_txt, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    dynamically_price_txt = new TextView(AuthorizeActivity.this);
                    dynamically_price_txt.setText("??????: $"+JArrayList.get(3));
                    dynamically_price_txt.setTextColor(mContext.getResources().getColor(R.color.WQP_White));
                    dynamically_price_txt.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                    dynamically_price_txt.setGravity(Gravity.LEFT);
                    dynamically_price_txt.setPadding(10, 2, 0, 0);
                    price_llt.addView(dynamically_price_txt, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    dynamically_total_txt = new TextView(AuthorizeActivity.this);
                    dynamically_total_txt.setText("??????: $"+JArrayList.get(4));
                    dynamically_total_txt.setTextColor(mContext.getResources().getColor(R.color.WQP_White));
                    dynamically_total_txt.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                    dynamically_total_txt.setGravity(Gravity.LEFT);
                    dynamically_total_txt.setPadding(10, 2, 0, 0);
                    total_llt.addView(dynamically_total_txt, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    dynamically_money_txt = new TextView(AuthorizeActivity.this);
                    dynamically_money_txt.setText("?????????????????????: $"+JArrayList.get(5));
                    dynamically_money_txt.setTextColor(mContext.getResources().getColor(R.color.WQP_White));
                    dynamically_money_txt.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                    dynamically_money_txt.setGravity(Gravity.LEFT);
                    dynamically_money_txt.setPadding(10, 2, 0, 0);
                    money_llt.addView(dynamically_money_txt, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    dynamically_hsv.addView(product_llt);

                    big_llt.addView(item_llt);
                    big_llt.addView(dynamically_hsv);
                    big_llt.addView(count_llt);
                    big_llt.addView(price_llt);
                    big_llt.addView(total_llt);
                    big_llt.addView(money_llt);

                    quotation_detail_llt.addView(big_llt, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * ???OkHttp????????????(???????????????)
     */
    private void sendRequestWithOkHttpForApproved() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //??????LoginActivity???????????????
                SharedPreferences user_id = getSharedPreferences("user_id", MODE_PRIVATE);
                String user_id_data = user_id.getString("ID", "");
                Log.e(LOG, user_id_data);
                Log.e(LOG, company);
                Log.e(LOG, quotation_type);
                Log.e(LOG, TA002TB002);

                String TA005N = TA005.substring(0,8);

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("User_id", user_id_data)
                            .add("COMPANY", company)
                            .add("TA001", quotation_type)
                            .add("TA002", TA002TB002)
                            .add("TA005",TA005N)
                            .build();
                    Request request = new Request.Builder()
                            //.url("http://192.168.0.172/WQP_OS/QuotationApproved.php")
                            .url("http://a.wqp-water.com.tw/WQP_OS/QuotationApproved.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectForDetail(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * ???OkHttp????????????(???????????????)
     */
    private void sendRequestWithOkHttpForReject() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //??????LoginActivity???????????????
                SharedPreferences user_id = getSharedPreferences("user_id", MODE_PRIVATE);
                String user_id_data = user_id.getString("ID", "");
                Log.e(LOG, user_id_data);
                Log.e(LOG, company);
                Log.e(LOG, quotation_type);
                Log.e(LOG, TA002TB002);

                String TA005N = TA005.substring(0,8);

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("User_id", user_id_data)
                            .add("COMPANY", company)
                            .add("TA001", quotation_type)
                            .add("TA002", TA002TB002)
                            .add("TA005",TA005N)
                            .build();
                    Request request = new Request.Builder()
                            //.url("http://192.168.0.172/WQP_OS/QuotationReject.php")
                            .url("http://a.wqp-water.com.tw/WQP_OS/QuotationReject.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectForDetail(responseData);
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