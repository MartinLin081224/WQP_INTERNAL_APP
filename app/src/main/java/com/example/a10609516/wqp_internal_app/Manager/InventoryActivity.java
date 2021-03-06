package com.example.a10609516.wqp_internal_app.Manager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
import com.example.a10609516.wqp_internal_app.R;
import com.example.a10609516.wqp_internal_app.Tools.ScannerActivity;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class InventoryActivity extends WQPToolsActivity {

    private SharedPreferences sp;

    private LinearLayout nav_view;
    private DrawerLayout drawer;
    private Toolbar toolbar;

    private TextView date_txt;
    private ScrollView inventory_scv;
    private LinearLayout warehouse_llt, inventory_record_llt, loss_llt, stock_llt, vendor_llt;
    private TextView item_number_txt, item_name_txt, item_format_txt, stock_txt, unit_txt1,
            unit_txt2, vendor_txt, safe_txt, recent_in_txt, recent_out_txt,
            last_inventory_txt, loss_txt, record_txt, barcode_txt;
    private EditText factory_id_edt, actual_edt;
    private Button factory_id_btn, upload_btn;

    private String COMPANY, MB001, MB002, MB003, MB004, MB064,
            vendor_no, vendor_name, warehouse_no, warehouse_name, safe,
            in, out, inventory, company_ch, user_name,
            item_id, barcode;
    private String w_id, w_name, d_id;

    //????????????Activity??????
    private Class<?> mClss;
    //ZXING_CAMERA??????
    private static final int ZXING_CAMERA_PERMISSION = 1;

    private Context context;
    private Spinner company_spinner, warehouse_spinner;
    private ArrayAdapter<String> company_listAdapter, warehouse_listAdapter;
    private String[] company_list = new String[]{"??????", "?????????", "?????????"};
    private String[] empty = new String[]{"(?????????)"};
    private String[] warehouse_list = new String[]{};

    private Context mContext = this;
    private String LOG = "InventoryActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        //?????????????????????
        initViews();
        //Menu???onClickListener
        MenuListener();
        //??????Toolbar
        SetToolBar();
        //??????????????????
        GetDate();
        //????????????Spinner????????????
        CompanySpinner();
    }

    /**
     * ?????????????????????
     */
    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        nav_view = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer_layout);
        inventory_scv = (ScrollView) findViewById(R.id.inventory_scv);
        date_txt = (TextView) findViewById(R.id.date_txt);
        item_number_txt = (TextView) findViewById(R.id.item_number_txt);
        item_name_txt = (TextView) findViewById(R.id.item_name_txt);
        item_format_txt = (TextView) findViewById(R.id.item_format_txt);
        factory_id_edt = (EditText) findViewById(R.id.factory_id_edt);
        factory_id_btn = (Button) findViewById(R.id.factory_id_btn);
        barcode_txt = (TextView) findViewById(R.id.barcode_txt);
        company_spinner = (Spinner) findViewById(R.id.company_spinner);
        warehouse_spinner = (Spinner) findViewById(R.id.warehouse_spinner);
        unit_txt1 = (TextView) findViewById(R.id.unit_txt1);
        unit_txt2 = (TextView) findViewById(R.id.unit_txt2);
        stock_txt = (TextView) findViewById(R.id.stock_txt);
        safe_txt = (TextView) findViewById(R.id.safe_txt);
        vendor_txt = (TextView) findViewById(R.id.vendor_txt);
        recent_in_txt = (TextView) findViewById(R.id.recent_in_txt);
        recent_out_txt = (TextView) findViewById(R.id.recent_out_txt);
        last_inventory_txt = (TextView) findViewById(R.id.last_inventory_txt);
        actual_edt = (EditText) findViewById(R.id.actual_edt);
        warehouse_llt = (LinearLayout) findViewById(R.id.warehouse_llt);
        inventory_record_llt = (LinearLayout) findViewById(R.id.inventory_record_llt);
        loss_llt = (LinearLayout) findViewById(R.id.loss_llt);
        stock_llt = (LinearLayout) findViewById(R.id.stock_llt);
        vendor_llt = (LinearLayout) findViewById(R.id.vendor_llt);
        upload_btn = (Button) findViewById(R.id.upload_btn);
        loss_txt = (TextView) findViewById(R.id.loss_txt);
        record_txt = (TextView) findViewById(R.id.record_txt);

        warehouse_llt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        });

        factory_id_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload_btn.setVisibility(View.VISIBLE);
                loss_llt.setVisibility(View.GONE);
                actual_edt.setFocusable(true);
                actual_edt.setFocusableInTouchMode(true);
                actual_edt.requestFocus();
                factory_id_edt.setFocusable(true);
                factory_id_edt.setFocusableInTouchMode(true);
                factory_id_edt.requestFocus();
                item_number_txt.setText("");
                item_name_txt.setText("");
                item_format_txt.setText("");
                stock_txt.setText("");
                actual_edt.setText("");
                recent_in_txt.setText("");
                recent_out_txt.setText("");
                last_inventory_txt.setText("");
                inventory_record_llt.removeAllViews();
                sendRequestWithOkHttpForFactoryID();
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        });

        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequestWithOkHttpForWareHouseInventory();
                upload_btn.setVisibility(View.GONE);
                stock_llt.setVisibility(View.GONE);
                loss_llt.setVisibility(View.VISIBLE);
                actual_edt.setFocusable(false);
                actual_edt.setFocusableInTouchMode(false);
                float q = Float.parseFloat(stock_txt.getText().toString());
                float a = Float.parseFloat(actual_edt.getText().toString());
                final float count = a - q;
                InventoryActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loss_txt.setText("" + count);
                        inventory_scv.fullScroll(ScrollView.FOCUS_UP);
                    }
                });
                inventory_record_llt.removeAllViews();
                //???WareHouseLog.PHP????????????
                sendRequestWithOkHttpForWareHouseLog();
            }
        });

        record_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inventory_record_llt.removeAllViews();
                //???WareHouseLog.PHP????????????
                sendRequestWithOkHttpForWareHouseLog();
            }
        });

        //??????LoginActivity???????????????
        SharedPreferences department_id = getSharedPreferences("department_id", MODE_PRIVATE);
        d_id = department_id.getString("D_ID", "");
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

        toolbar.setTitle("?????????");
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
                Intent intent_login = new Intent(InventoryActivity.this, LoginActivity.class);
                startActivity(intent_login);
            }
        });
    }

    /**
     * ??????????????????
     */
    private void GetDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(new java.util.Date());
        date_txt.setText(date);
    }

    /**
     * ????????????Spinner????????????
     */
    private void CompanySpinner() {
        context = this;
        //?????????????????????????????????????????????
        company_listAdapter = new ArrayAdapter<String>(this, R.layout.white_blod_spinner, company_list);
        company_listAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        company_spinner.setAdapter(company_listAdapter);
        company_spinner.setOnItemSelectedListener(selectListener);
        //?????????????????????????????????????????????????????????
        warehouse_listAdapter = new ArrayAdapter<String>(this, R.layout.white_blod_spinner, empty);
        warehouse_listAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        warehouse_spinner.setAdapter(warehouse_listAdapter);
    }
    /*private void CompanySpinner() {
        //Spinner????????????
        company_listAdapter = new ArrayAdapter<String>(InventoryActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                company_list);
        company_spinner.setAdapter(company_listAdapter);
        company_spinner.setOnItemSelectedListener(selectListener);
    }*/

    /**
     * ????????????
     */
    /**
     * ?????????????????????????????????
     */
    private AdapterView.OnItemSelectedListener selectListener = new AdapterView.OnItemSelectedListener() {
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
            //?????????????????????????????????????????????
            //int pos = type_spinner.getSelectedItemPosition();
            //???WareHouse.PHP????????????
            sendRequestWithOkHttpForWareHouse();
        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    };
    /*private void WareHouseSpinner() {
        warehouse_listAdapter = new ArrayAdapter<String>(InventoryActivity.this,
                android.R.layout.simple_spinner_item,
                warehouse_list);
        warehouse_listAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        warehouse_spinner.setAdapter(warehouse_listAdapter);
        warehouse_spinner.setOnItemSelectedListener(selectListener);
    }*/

    /**
     * ???OkHttp????????????(DIYStore)
     */
    private void sendRequestWithOkHttpForWareHouse() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String spinner_select = String.valueOf(company_spinner.getSelectedItem());

                if (spinner_select.equals("??????")) {
                    company_ch = "WQP";
                } else if (spinner_select.equals("?????????")) {
                    company_ch = "TYT";
                } else {
                    company_ch = "BWT";
                }

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("COMPANY", company_ch)
                            .build();
                    Log.e(LOG, company_ch);
                    Request request = new Request.Builder()
                            .url("http://a.wqp-water.com.tw/WQP_OS/Warehouse.php")
                            //.url("http://192.168.0.172/WQP_OS/Warehouse.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectForWareHouse(responseData);
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
    private void parseJSONWithJSONObjectForWareHouse(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            ArrayList<String> JArrayList = new ArrayList<String>();
            warehouse_list = new String[]{};
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON??????????????????
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String warehouse_id = jsonObject.getString("MC001").trim() + "(" + jsonObject.getString("MC002").trim() + ")";
                //JSONArray??????SearchData??????
                JArrayList.add(warehouse_id);
                warehouse_list = JArrayList.toArray(new String[i]);
                //Log.e("PictureActivity3", diy[i]);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //??????????????????Adapter/*????????????????????????type2[pos]*/
                    warehouse_listAdapter = new ArrayAdapter<String>(context, R.layout.white_blod_spinner, warehouse_list);
                    warehouse_listAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //???????????????????????????Spinner
                    warehouse_spinner.setAdapter(warehouse_listAdapter);
                    warehouse_spinner.setOnItemSelectedListener(selectListener2);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ????????????????????????
     */
    private AdapterView.OnItemSelectedListener selectListener2 = new AdapterView.OnItemSelectedListener() {
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
            //????????????????????????????????????
            //int pos = type_spinner.getSelectedItemPosition();
            stock_txt.setText("");
            actual_edt.setText("");
            recent_in_txt.setText("");
            recent_out_txt.setText("");
            last_inventory_txt.setText("");
            inventory_record_llt.removeAllViews();
            InventoryActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    upload_btn.setVisibility(View.VISIBLE);
                    loss_llt.setVisibility(View.GONE);
                    actual_edt.setFocusable(true);
                    actual_edt.setFocusableInTouchMode(true);
                    actual_edt.requestFocus();
                    factory_id_edt.setFocusable(true);
                    factory_id_edt.setFocusableInTouchMode(true);
                    factory_id_edt.requestFocus();
                    //???WareHouseMoreSearch.PHP????????????
                    sendRequestWithOkHttpForWareHouseMoreSearch();
                    if (d_id.toString().equals("1000")){
                        stock_llt.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    };

    /**
     * Button?????????
     */
    public void scanCode(View view) {
        //startActivityForResult(new Intent(this, ScannerActivity.class), 1);
        launchActivity(ScannerActivity.class);
        upload_btn.setVisibility(View.VISIBLE);
        loss_llt.setVisibility(View.GONE);
        actual_edt.setFocusable(true);
        actual_edt.setFocusableInTouchMode(true);
        actual_edt.requestFocus();
        factory_id_edt.setFocusable(true);
        factory_id_edt.setFocusableInTouchMode(true);
        factory_id_edt.requestFocus();
        if (d_id.toString().equals("1000")){
            stock_llt.setVisibility(View.VISIBLE);
        }
    }

    /**
     * ????????????????????????????????????Intent????????????
     */
    public void launchActivity(Class<?> clss) {
        //??????????????????????????????
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            mClss = clss;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, ZXING_CAMERA_PERMISSION);
        } else {
            Intent intent = new Intent(this, clss);
            //startActivity(intent);
            startActivityForResult(intent, ZXING_CAMERA_PERMISSION);
        }
    }

    /**
     * ????????????????????????????????????
     *
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                //ZXing???????????????
                String contents = intent.getStringExtra("SCAN_RESULT");
                barcode_txt.setText(intent.getStringExtra("result_text"));
                actual_edt.setText("");
                sendRequestWithOkHttpForWareHouseSearch();
                inventory_record_llt.removeAllViews();
                //???WareHouseLog.PHP????????????
                sendRequestWithOkHttpForWareHouseLog();
            } else {
                if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(InventoryActivity.this, "????????????", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /**
     * ???OKHttp??????(????????????????????????)
     */
    private void sendRequestWithOkHttpForFactoryID() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String f_id = factory_id_edt.getText().toString();
                Log.e( LOG, f_id);

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("item_id", f_id)
                            .build();
                    Request request = new Request.Builder()
                            .url("http://a.wqp-water.com.tw/WQP_OS/WareHouseFactoryID.php")
                            //.url("http://192.168.0.172/WQP_OS/WareHouseFactoryID.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectForFactoryID(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * ??????????????????????????????????????????????????????????????????????????????BarCode
     *
     * @param jsonData
     */
    private void parseJSONWithJSONObjectForFactoryID(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON??????????????????
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                COMPANY = jsonObject.getString("COMPANY");
                MB001 = jsonObject.getString("MB001");
                MB002 = jsonObject.getString("MB002");
                MB003 = jsonObject.getString("MB003");
                MB004 = jsonObject.getString("MB004");
                safe = jsonObject.getString("SAFE");
                vendor_no = jsonObject.getString("V_NO");
                vendor_name = jsonObject.getString("V_NAME");
                item_id = jsonObject.getString("ITEM");
                barcode = jsonObject.getString("BarCode");

                InventoryActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        item_number_txt.setText(MB001.toString());
                        item_name_txt.setText(MB002.toString());
                        item_format_txt.setText(MB003.toString());
                        unit_txt1.setText(MB004.toString());
                        unit_txt2.setText(MB004.toString());
                        vendor_txt.setText(vendor_no.toString() + "|" + vendor_name.toString());
                        safe_txt.setText(safe.toString());
                        barcode_txt.setText(barcode.toString());
                        if (d_id.toString().equals("1000")){
                            stock_llt.setVisibility(View.VISIBLE);
                        }
                        //company_spinner.setVisibility(View.VISIBLE);
                        //warehouse_spinner.setVisibility(View.VISIBLE);
                        //???OKHttp??????(????????????????????????)
                        sendRequestWithOkHttpForWareHouseMoreSearch();
                        /*//????????????Spinner????????????
                        CompanySpinner();
                        //???????????????Spinner????????????
                        //WareHouseSpinner();
                        //????????????????????????
                        if (COMPANY.toString().equals("WQP")) {
                            company_ch = "??????";
                        } else if (COMPANY.toString().equals("TYT")) {
                            company_ch = "?????????";
                        } else {
                            company_ch = "?????????";
                        }
                        //????????????COMPANY??????????????????????????? ????????????????????????Spinner??????
                        for (int c = 0; c < company_list.length; c++) {
                            if (company_list[c].equals(company_ch)) {
                                company_spinner.setSelection(c, true);
                                break;
                            }
                        }*/
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ???OKHttp??????(????????????????????????)
     */
    private void sendRequestWithOkHttpForWareHouseSearch() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String barcode = barcode_txt.getText().toString();
                Log.e(LOG, barcode);

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("barcode", barcode)
                            .build();
                    Request request = new Request.Builder()
                            .url("http://a.wqp-water.com.tw/WQP_OS/WareHouseSearch.php")
                            //.url("http://192.168.0.172/WQP_OS/WareHouseSearch.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectForWareHouseSearch(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * ?????????????????????????????????????????????????????????????????????????????????????????????
     *
     * @param jsonData
     */
    private void parseJSONWithJSONObjectForWareHouseSearch(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON??????????????????
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                //COMPANY = jsonObject.getString("COMPANY");
                MB001 = jsonObject.getString("MB001");
                MB002 = jsonObject.getString("MB002");
                MB003 = jsonObject.getString("MB003");
                MB004 = jsonObject.getString("MB004");
                //MB064 = jsonObject.getString("??????");
                safe = jsonObject.getString("SAFE");
                vendor_no = jsonObject.getString("V_NO");
                vendor_name = jsonObject.getString("V_NAME");
                item_id = jsonObject.getString("ITEM");
                barcode = jsonObject.getString("BarCode");

                Log.e(LOG + "1", safe);

                InventoryActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        item_number_txt.setText(MB001.toString());
                        item_name_txt.setText(MB002.toString());
                        item_format_txt.setText(MB003.toString());
                        factory_id_edt.setText(item_id.toString());
                        unit_txt1.setText(MB004.toString());
                        unit_txt2.setText(MB004.toString());
                        vendor_txt.setText(vendor_no.toString() + "|" + vendor_name.toString());
                        safe_txt.setText(safe.toString());
                        if (d_id.toString().equals("1000")){
                            stock_llt.setVisibility(View.VISIBLE);
                        }
                        //company_spinner.setVisibility(View.VISIBLE);
                        //warehouse_spinner.setVisibility(View.VISIBLE);
                        //???OKHttp??????(????????????????????????)
                        sendRequestWithOkHttpForWareHouseMoreSearch();
                        /*//????????????Spinner????????????
                        CompanySpinner();
                        //???????????????Spinner????????????
                        //WareHouseSpinner();
                        //????????????????????????
                        if (COMPANY.toString().equals("WQP")) {
                            company_ch = "??????";
                        } else if (COMPANY.toString().equals("TYT")) {
                            company_ch = "?????????";
                        } else {
                            company_ch = "?????????";
                        }
                        //????????????COMPANY??????????????????????????? ????????????????????????Spinner??????
                        for (int c = 0; c < company_list.length; c++) {
                            if (company_list[c].equals(company_ch)) {
                                company_spinner.setSelection(c, true);
                                break;
                            }
                        }*/
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ???OKHttp??????(????????????????????????)
     */
    private void sendRequestWithOkHttpForWareHouseMoreSearch() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String company_select_more = String.valueOf(company_spinner.getSelectedItem());
                String MB001 = item_number_txt.getText().toString();
                String warehouse_no_select_more = String.valueOf(warehouse_spinner.getSelectedItem());
                w_id = warehouse_no_select_more.substring(0, warehouse_no_select_more.indexOf("("));
                /*if(warehouse_no_select_more.equals("BWT(BWT??????)")){
                    w_id = "BWT";
                }else if (warehouse_no_select_more.equals("BWT-De(BWT?????????)")){
                    w_id = "BWT-De";
                }else if (warehouse_no_select_more.equals("OM-BZA(??????-??????)")){
                    w_id = "OM-BZA";
                }else if (warehouse_no_select_more.equals("WPA(?????????)")){
                    w_id = "WPA";
                }else if (warehouse_no_select_more.equals("WQP(?????????)")){
                    w_id = "WQP";
                }*/

                Log.e(LOG, MB001);
                Log.e(LOG, warehouse_no_select_more);
                Log.e(LOG, w_id);

                if (company_select_more.toString().equals("??????")) {
                    company_select_more = "WQP";
                } else if (company_select_more.toString().equals("?????????")) {
                    company_select_more = "TYT";
                } else {
                    company_select_more = "BWT";
                }

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("COMPANY", company_select_more)
                            .add("MB001", MB001)
                            .add("WH_NO", w_id)
                            .build();
                    Request request = new Request.Builder()
                            .url("http://a.wqp-water.com.tw/WQP_OS/WareHouseMoreSearch.php")
                            //.url("http://192.168.0.172/WQP_OS/WareHouseMoreSearch.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectForWareHouseMoreSearch(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * ?????????????????????????????????????????????????????????????????????????????????
     *
     * @param jsonData
     */
    private void parseJSONWithJSONObjectForWareHouseMoreSearch(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON??????????????????
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                warehouse_no = jsonObject.getString("W_NO");
                warehouse_name = jsonObject.getString("W_NAME");
                MB064 = jsonObject.getString("ACTUAL");
                //safe = jsonObject.getString("????????????");
                in = jsonObject.getString("R_IN");
                out = jsonObject.getString("R_OUT");
                inventory = jsonObject.getString("R_LAST");

                Log.e(LOG, MB064);

                InventoryActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stock_txt.setText(MB064.toString());
                        recent_in_txt.setText(in.toString());
                        recent_out_txt.setText(out.toString());
                        last_inventory_txt.setText(inventory.toString());
                        //????????????????????????
                        actual_edt.setSelection(actual_edt.getText().length());
                    }
                });

                /*if (vendor_txt.getText().toString().equals(":|:")){
                    vendor_txt.setText("");
                }*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ???OKHttp??????(???????????????????????????????????????????????????????????????????????????Log)
     */
    private void sendRequestWithOkHttpForWareHouseLog() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String company_select_log = String.valueOf(company_spinner.getSelectedItem());
                String MB001 = item_number_txt.getText().toString();
                String warehouse_no_select_log = String.valueOf(warehouse_spinner.getSelectedItem());
                String w_id_log = warehouse_no_select_log.substring(0, warehouse_no_select_log.indexOf("("));
                /*if(warehouse_no_select_log.equals("BWT(BWT??????)")){
                    w_id = "BWT";
                    w_name = "BWT??????";
                }else if (warehouse_no_select_log.equals("BWT-De(BWT?????????)")){
                    w_id = "BWT-De";
                    w_name = "BWT?????????";
                }else if (warehouse_no_select_log.equals("OM-BZA(??????-??????)")){
                    w_id = "OM-BZA";
                    w_name = "??????-??????";
                }else if (warehouse_no_select_log.equals("WPA(?????????)")){
                    w_id = "WPA";
                    w_name = "?????????";
                }else if (warehouse_no_select_log.equals("WQP(?????????)")){
                    w_id = "WQP";
                    w_name = "?????????";
                }else if (warehouse_no_select_log.equals("BRT(???????????????)")){
                    w_id = "BRT";
                    w_name = "???????????????";
                }else if (warehouse_no_select_log.equals("DSV(?????????)")){
                    w_id = "DSV";
                    w_name = "?????????";
                }else if (warehouse_no_select_log.equals("OM-SOL(??????-??????)")){
                    w_id = "OM-SOL";
                    w_name = "??????-??????";
                }else if (warehouse_no_select_log.equals("Borrow(????????????)")){
                    w_id = "Borrow";
                    w_name = "????????????";
                }else if (warehouse_no_select_log.equals("01-10000AT(??????AQT???)")){
                    w_id = "01-10000AT";
                    w_name = "??????AQT???";
                }else if (warehouse_no_select_log.equals("01-10000TP(???????????????)")){
                    w_id = "01-10000TP";
                    w_name = "???????????????";
                }else if (warehouse_no_select_log.equals("01-10000TY(???????????????)")){
                    w_id = "01-10000TY";
                    w_name = "???????????????";
                }else if (warehouse_no_select_log.equals("01-10000HS(???????????????)")){
                    w_id = "01-10000HS";
                    w_name = "???????????????";
                }else if (warehouse_no_select_log.equals("01-10000TC(???????????????)")){
                    w_id = "01-10000TC";
                    w_name = "???????????????";
                }else if (warehouse_no_select_log.equals("01-10000KH(???????????????)")){
                    w_id = "01-10000KH";
                    w_name = "???????????????";
                }else if (warehouse_no_select_log.equals("01-00001TY(???????????????)")){
                    w_id = "01-00001TY";
                    w_name = "???????????????";
                }else if (warehouse_no_select_log.equals("04-00005(PCHOME??????)")){
                    w_id = "04-00005";
                    w_name = "PCHOME??????";
                }else if (warehouse_no_select_log.equals("04-00012(MOMO??????)")){
                    w_id = "04-00012";
                    w_name = "MOMO??????";
                }else if (warehouse_no_select_log.equals("04-00019(YAHOO??????)")){
                    w_id = "04-00019";
                    w_name = "YAHOO??????";
                }else if (warehouse_no_select_log.equals("04-00020(????????????)")){
                    w_id = "04-00020";
                    w_name = "????????????";
                }else if (warehouse_no_select_log.equals("04-00021(????????????)")){
                    w_id = "04-00021";
                    w_name = "????????????";
                }else if (warehouse_no_select_log.equals("04-00022(????????????)")){
                    w_id = "04-00022";
                    w_name = "????????????";
                }else if (warehouse_no_select_log.equals("04-00023(???????????????)")){
                    w_id = "04-00023";
                    w_name = "???????????????";
                }else if (warehouse_no_select_log.equals("04-00024(???????????????)")){
                    w_id = "04-00024";
                    w_name = "???????????????";
                }else if (warehouse_no_select_log.equals("04-00028(????????????)")){
                    w_id = "04-00028";
                    w_name = "????????????";
                }else if (warehouse_no_select_log.equals("04-00029(????????????)")){
                    w_id = "04-00029";
                    w_name = "????????????";
                }else if (warehouse_no_select_log.equals("04-00030(????????????)")){
                    w_id = "04-00030";
                    w_name = "????????????";
                }*/

                Log.e(LOG, MB001);
                Log.e(LOG, warehouse_no_select_log);
                Log.e(LOG, w_id_log);

                if (company_select_log.toString().equals("??????")) {
                    company_select_log = "WQP";
                } else if (company_select_log.toString().equals("?????????")) {
                    company_select_log = "TYT";
                } else {
                    company_select_log = "BWT";
                }

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("COMPANY", company_select_log)
                            .add("MB001", MB001)
                            .add("WH_NO", w_id_log)
                            .build();
                    Request request = new Request.Builder()
                            .url("http://a.wqp-water.com.tw/WQP_OS/WareHouseLog.php")
                            //.url("http://192.168.0.172/WQP_OS/WareHouseLog.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectForWareHouseLog(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * ???????????????????????????????????????????????????????????????????????????Log
     *
     * @param jsonData
     */
    private void parseJSONWithJSONObjectForWareHouseLog(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON??????????????????
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String user_name_log = jsonObject.getString("USERS");
                String quantity_log = jsonObject.getString("QUANTITY");
                String actual_log = jsonObject.getString("ACTUAL");
                String warehouse_name_log = jsonObject.getString("W_NAME");
                String inventory_log = jsonObject.getString("DATE");

                Log.e(LOG, quantity_log);

                //JSONArray??????SearchData??????
                ArrayList<String> JArrayList = new ArrayList<String>();
                JArrayList.add(user_name_log);
                JArrayList.add(quantity_log);
                JArrayList.add(actual_log);
                JArrayList.add(warehouse_name_log);
                JArrayList.add(inventory_log);

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
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    LinearLayout small_llt1 = new LinearLayout(InventoryActivity.this);
                    small_llt1.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout small_llt2 = new LinearLayout(InventoryActivity.this);
                    small_llt2.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout small_llt3 = new LinearLayout(InventoryActivity.this);
                    small_llt3.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout small_llt4 = new LinearLayout(InventoryActivity.this);
                    small_llt4.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout small_llt5 = new LinearLayout(InventoryActivity.this);
                    small_llt5.setOrientation(LinearLayout.HORIZONTAL);

                    Bundle jb = msg.getData();
                    ArrayList<String> JArrayList = new ArrayList<String>();
                    //int i = b.getStringArrayList("JSON_data").size();
                    JArrayList = jb.getStringArrayList("JSON_data");

                    //????????????TableLayout???????????????
                    TextView dynamically_name;
                    dynamically_name = new TextView(InventoryActivity.this);
                    dynamically_name.setText("???????????? : " + JArrayList.get(0));
                    dynamically_name.setGravity(Gravity.CENTER);
                    //dynamically_name.setWidth(50);
                    dynamically_name.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);

                    //????????????TableLayout?????????
                    TextView dynamically_quantity;
                    dynamically_quantity = new TextView(InventoryActivity.this);
                    dynamically_quantity.setText("???????????? : " + JArrayList.get(1));
                    dynamically_quantity.setGravity(Gravity.CENTER);
                    dynamically_quantity.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);

                    //????????????TableLayout?????????
                    TextView dynamically_actual;
                    dynamically_actual = new TextView(InventoryActivity.this);
                    dynamically_actual.setText("?????????????????? : " + JArrayList.get(2));
                    dynamically_actual.setGravity(Gravity.CENTER);
                    dynamically_actual.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);

                    //????????????TableLayout???????????????
                    TextView dynamically_vendor;
                    dynamically_vendor = new TextView(InventoryActivity.this);
                    dynamically_vendor.setText("???????????? : " + JArrayList.get(3));
                    dynamically_vendor.setGravity(Gravity.CENTER);
                    //dynamically_name.setWidth(50);
                    dynamically_vendor.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);

                    //????????????TableLayout???????????????
                    TextView dynamically_date;
                    dynamically_date = new TextView(InventoryActivity.this);
                    dynamically_date.setText("???????????? : " + JArrayList.get(4));
                    dynamically_date.setGravity(Gravity.CENTER);
                    dynamically_date.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);

                    //????????????TableLayout????????????
                    TextView dynamically_txt = new TextView(InventoryActivity.this);
                    dynamically_txt.setBackgroundColor(Color.rgb(220, 220, 220));

                    LinearLayout.LayoutParams small_pm = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);

                    small_llt1.addView(dynamically_name, small_pm);
                    small_llt2.addView(dynamically_quantity, small_pm);
                    small_llt3.addView(dynamically_actual, small_pm);
                    small_llt4.addView(dynamically_vendor, small_pm);
                    small_llt5.addView(dynamically_date, small_pm);

                    inventory_record_llt.addView(dynamically_txt, LinearLayout.LayoutParams.MATCH_PARENT, 3);
                    inventory_record_llt.addView(small_llt1);
                    inventory_record_llt.addView(small_llt2);
                    inventory_record_llt.addView(small_llt3);
                    inventory_record_llt.addView(small_llt4);
                    inventory_record_llt.addView(small_llt5);

                    inventory_record_llt.getChildAt(0).setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * ???OKHttp??????(??????????????????&??????Log)
     */
    private void sendRequestWithOkHttpForWareHouseInventory() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //??????LoginActivity???????????????
                SharedPreferences user_id = getSharedPreferences("user_id", MODE_PRIVATE);
                String user_id_data = user_id.getString("ID", "");
                Log.e(LOG, user_id_data);

                String company_select_inventory = String.valueOf(company_spinner.getSelectedItem());
                String MB001 = item_number_txt.getText().toString().trim();
                String quantity = stock_txt.getText().toString();
                String actual = actual_edt.getText().toString();
                String warehouse_no_select = String.valueOf(warehouse_spinner.getSelectedItem());
                String w_id_no = warehouse_no_select.substring(0, warehouse_no_select.indexOf("("));
                //String warehouse_name_select = String.valueOf(warehouse_spinner.getSelectedItem());
                //String w_id_name = warehouse_name_select.substring(warehouse_name_select.indexOf(":|:"), warehouse_name_select.length());
                /*if(warehouse_no_select.equals("BWT(BWT??????)")){
                    w_id = "BWT";
                    w_name = "BWT??????";
                }else if (warehouse_no_select.equals("BWT-De(BWT?????????)")){
                    w_id = "BWT-De";
                    w_name = "BWT?????????";
                }else if (warehouse_no_select.equals("OM-BZA(??????-??????)")){
                    w_id = "OM-BZA";
                    w_name = "??????-??????";
                }else if (warehouse_no_select.equals("WPA(?????????)")){
                    w_id = "WPA";
                    w_name = "?????????";
                }else if (warehouse_no_select.equals("WQP(?????????)")){
                    w_id = "WQP";
                    w_name = "?????????";
                }else if (warehouse_no_select.equals("BRT(???????????????)")){
                    w_id = "BRT";
                    w_name = "???????????????";
                }else if (warehouse_no_select.equals("DSV(?????????)")){
                    w_id = "DSV";
                    w_name = "?????????";
                }else if (warehouse_no_select.equals("OM-SOL(??????-??????)")){
                    w_id = "OM-SOL";
                    w_name = "??????-??????";
                }else if (warehouse_no_select.equals("Borrow(????????????)")){
                    w_id = "Borrow";
                    w_name = "????????????";
                }else if (warehouse_no_select.equals("01-10000AT(??????AQT???)")){
                    w_id = "01-10000AT";
                    w_name = "??????AQT???";
                }else if (warehouse_no_select.equals("01-10000TP(???????????????)")){
                    w_id = "01-10000TP";
                    w_name = "???????????????";
                }else if (warehouse_no_select.equals("01-10000TY(???????????????)")){
                    w_id = "01-10000TY";
                    w_name = "???????????????";
                }else if (warehouse_no_select.equals("01-10000HS(???????????????)")){
                    w_id = "01-10000HS";
                    w_name = "???????????????";
                }else if (warehouse_no_select.equals("01-10000TC(???????????????)")){
                    w_id = "01-10000TC";
                    w_name = "???????????????";
                }else if (warehouse_no_select.equals("01-10000KH(???????????????)")){
                    w_id = "01-10000KH";
                    w_name = "???????????????";
                }else if (warehouse_no_select.equals("01-00001TY(???????????????)")){
                    w_id = "01-00001TY";
                    w_name = "???????????????";
                }else if (warehouse_no_select.equals("04-00005(PCHOME??????)")){
                    w_id = "04-00005";
                    w_name = "PCHOME??????";
                }else if (warehouse_no_select.equals("04-00012(MOMO??????)")){
                    w_id = "04-00012";
                    w_name = "MOMO??????";
                }else if (warehouse_no_select.equals("04-00019(YAHOO??????)")){
                    w_id = "04-00019";
                    w_name = "YAHOO??????";
                }else if (warehouse_no_select.equals("04-00020(????????????)")){
                    w_id = "04-00020";
                    w_name = "????????????";
                }else if (warehouse_no_select.equals("04-00021(????????????)")){
                    w_id = "04-00021";
                    w_name = "????????????";
                }else if (warehouse_no_select.equals("04-00022(????????????)")){
                    w_id = "04-00022";
                    w_name = "????????????";
                }else if (warehouse_no_select.equals("04-00023(???????????????)")){
                    w_id = "04-00023";
                    w_name = "???????????????";
                }else if (warehouse_no_select.equals("04-00024(???????????????)")){
                    w_id = "04-00024";
                    w_name = "???????????????";
                }else if (warehouse_no_select.equals("04-00028(????????????)")){
                    w_id = "04-00028";
                    w_name = "????????????";
                }else if (warehouse_no_select.equals("04-00029(????????????)")){
                    w_id = "04-00029";
                    w_name = "????????????";
                }else if (warehouse_no_select.equals("04-00030(????????????)")){
                    w_id = "04-00030";
                    w_name = "????????????";
                }*/

                if (company_select_inventory.toString().equals("??????")) {
                    company_select_inventory = "WQP";
                } else if (company_select_inventory.toString().equals("?????????")) {
                    company_select_inventory = "TYT";
                } else {
                    company_select_inventory = "BWT";
                }

                Log.e(LOG, MB001);
                Log.e(LOG, actual);
                //Log.e("InventoryActivity", w_id);
                //Log.e("InventoryActivity", w_name);
                Log.e( LOG, w_id_no);

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("COMPANY", company_select_inventory)
                            .add("User_id", user_id_data)
                            .add("MB001", MB001)
                            .add("quantity", quantity)
                            .add("actual", actual)
                            .add("WH_NO", w_id_no)
                            //.add("WH_NAME", w_name)
                            .build();
                    Request request = new Request.Builder()
                            .url("http://a.wqp-water.com.tw/WQP_OS/WareHouseInventory.php")
                            //.url("http://192.168.0.172/WQP_OS/WareHouseInventory.php")
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
        Toast.makeText(this, "????????????", Toast.LENGTH_SHORT).show();
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