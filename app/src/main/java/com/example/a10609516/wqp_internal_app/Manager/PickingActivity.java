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
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class PickingActivity extends WQPToolsActivity {

    private SharedPreferences sp;

    private LinearLayout nav_view;
    private DrawerLayout drawer;
    private Toolbar toolbar;

    private LinearLayout picking_llt, id_llt, picking_record_llt, large_llt, big_llt;
    private ImageView add_imv, add_qrcode;
    private Button master_btn, id_btn, check_btn, vendor_sign_btn, sales_sign_btn;
    private TextView order_txt, separate_txt, item_number_txt, item_name_txt, item_format_txt,
            barcode_txt, unit_txt, today_record_txt, past_record_txt;
    private EditText factory_id_edt, picking_edt, note_edt;
    private Spinner type_spinner, company_spinner, warehouse_spinner;

    private ArrayAdapter<String> type_listAdapter, company_listAdapter, warehouse_listAdapter;
    private String[] type_list = new String[]{"?????????", "??????", "??????"};
    private String[] company_list = new String[]{"?????????", "??????", "?????????", "?????????"};
    private String[] empty = new String[]{"(?????????)"};
    private String[] warehouse_list = new String[]{};

    private String company, order_type, order_number, cust_id,
            COMPANY, MB001, MB002, MB003, MB004, company_ch,
            item_id, barcode, w_id, w_name, COUNT;

    //????????????Activity??????
    private Class<?> mClss;
    //ZXING_CAMERA??????
    private static final int ZXING_CAMERA_PERMISSION = 1;

    private Context mContext = this;

    private String LOG = "PickingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picking);
        //?????????????????????
        initViews();
        //Menu???onClickListener
        MenuListener();
        //??????Toolbar
        SetToolBar();
        //??????OrderSearchActivity??????????????????
        GetResponseData();
        //????????????WareHousePickingSearch.php????????????(???????????????????????????)
        sendRequestWithOkHttpForPickingSearch();
        //???/?????????Spinner
        TypeSpinner();
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
        picking_llt = (LinearLayout) findViewById(R.id.picking_llt);
        id_llt = (LinearLayout) findViewById(R.id.id_llt);
        picking_record_llt = (LinearLayout) findViewById(R.id.picking_record_llt);
        master_btn = (Button) findViewById(R.id.master_btn);
        id_btn = (Button) findViewById(R.id.id_btn);
        check_btn = (Button) findViewById(R.id.check_btn);
        vendor_sign_btn = (Button) findViewById(R.id.vendor_sign_btn);
        sales_sign_btn = (Button) findViewById(R.id.sales_sign_btn);
        add_imv = (ImageView) findViewById(R.id.add_imv);
        add_qrcode = (ImageView) findViewById(R.id.add_qrcode);
        order_txt = (TextView) findViewById(R.id.order_txt);
        separate_txt = (TextView) findViewById(R.id.separate_txt);
        item_number_txt = (TextView) findViewById(R.id.item_number_txt);
        item_name_txt = (TextView) findViewById(R.id.item_name_txt);
        item_format_txt = (TextView) findViewById(R.id.item_format_txt);
        barcode_txt = (TextView) findViewById(R.id.barcode_txt);
        unit_txt = (TextView) findViewById(R.id.unit_txt);
        today_record_txt = (TextView) findViewById(R.id.today_record_txt);
        past_record_txt = (TextView) findViewById(R.id.past_record_txt);
        factory_id_edt = (EditText) findViewById(R.id.factory_id_edt);
        picking_edt = (EditText) findViewById(R.id.picking_edt);
        note_edt = (EditText) findViewById(R.id.note_edt);
        type_spinner = (Spinner) findViewById(R.id.type_spinner);
        company_spinner = (Spinner) findViewById(R.id.company_spinner);
        warehouse_spinner = (Spinner) findViewById(R.id.warehouse_spinner);

        //picking_llt.setOnClickListener?????????  //??????/????????????
        picking_llt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        });
        //master_btn.setOnClickListener?????????  //?????????????????????
        master_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //????????????WareHousePickingMaster.php????????????(?????????????????????)
                sendRequestWithOkHttpForPickingMaster();
                Toast.makeText(PickingActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
                master_btn.setVisibility(View.GONE);
            }
        });
        //add_btn.setOnClickListener?????????  //????????????ID????????????
        add_imv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id_llt.getVisibility() == View.GONE) {
                    id_llt.setVisibility(View.VISIBLE);
                    separate_txt.setVisibility(View.VISIBLE);
                    factory_id_edt.setFocusable(true);
                    factory_id_edt.setFocusableInTouchMode(true);
                    factory_id_edt.requestFocus();
                } else {
                    id_llt.setVisibility(View.GONE);
                    separate_txt.setVisibility(View.GONE);
                }
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        });
        //id_btn.setOnClickListener?????????  //????????????ID????????????
        id_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picking_llt.setVisibility(View.VISIBLE);
                item_number_txt.setText("");
                item_name_txt.setText("");
                item_format_txt.setText("");
                barcode_txt.setText("");
                sendRequestWithOkHttpForFactoryID();
                factory_id_edt.setText("");
                picking_edt.setText("");
                note_edt.setText("");
                id_llt.setVisibility(View.GONE);
                check_btn.setVisibility(View.VISIBLE);
                //???/?????????Spinner
                //TypeSpinner();
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        });
        //check_btn.setOnClickListener?????????  //??????/??????????????????OKHTTP????????????
        check_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //????????????WareHousePickingSearch.php????????????(???????????????????????????)
                sendRequestWithOkHttpForPickingSearch();
                if (COUNT.equals("1")) {
                    if (String.valueOf(type_spinner.getSelectedItem()).equals("?????????")) {
                        Toast.makeText(PickingActivity.this, "??????????????????/?????????", Toast.LENGTH_SHORT).show();
                    } else {
                        if (picking_edt.getText().toString().equals("")) {
                            Toast.makeText(PickingActivity.this, "???????????????????????????", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(PickingActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
                            //???WareHouseLog.PHP????????????
                            sendRequestWithOkHttpForPickingDetail();
                            check_btn.setVisibility(View.GONE);
                            //????????????LOG
                            today_record_txt.setTextColor(Color.rgb(0, 127, 255));
                            past_record_txt.setTextColor(Color.rgb(62, 58, 57));
                            picking_record_llt.removeAllViews();
                            //???PickingLogToday.PHP????????????
                            sendRequestWithOkHttpForPickingLogToday();
                        }
                    }
                } else {
                    Toast.makeText(PickingActivity.this, "???????????????????????????", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //vendor_sign_btn.setOnClickListener?????????  //??????????????????????????????
        vendor_sign_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle order_bundle = getIntent().getExtras();
                company = order_bundle.getString("ResponseText" + 0);
                order_type = order_bundle.getString("ResponseText" + 1);
                order_number = order_bundle.getString("ResponseText" + 2);
                cust_id = order_bundle.getString("ResponseText" + 3);
                Intent intent = new Intent(PickingActivity.this, Picking_SignatureActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("ResponseText0", company);
                bundle.putString("ResponseText1", order_type);
                bundle.putString("ResponseText2", order_number);
                bundle.putString("ResponseText3", cust_id);
                intent.putExtras(bundle);//????????????????????????
                startActivity(intent);
                Log.e(LOG, company);
                Log.e(LOG, order_type);
                Log.e(LOG, order_number);
                Log.e(LOG, cust_id);
            }
        });
        //sales_sign_btn.setOnClickListener?????????  //??????????????????????????????
        sales_sign_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle order_bundle = getIntent().getExtras();
                company = order_bundle.getString("ResponseText" + 0);
                order_type = order_bundle.getString("ResponseText" + 1);
                order_number = order_bundle.getString("ResponseText" + 2);
                cust_id = order_bundle.getString("ResponseText" + 3);
                Intent intent = new Intent(PickingActivity.this, Picking_SignatureActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("ResponseText0", company);
                bundle.putString("ResponseText1", order_type);
                bundle.putString("ResponseText2", order_number);
                bundle.putString("ResponseText3", cust_id);
                intent.putExtras(bundle);//????????????????????????
                startActivity(intent);
                Log.e(LOG, company);
                Log.e(LOG, order_type);
                Log.e(LOG, order_number);
                Log.e(LOG, cust_id);
            }
        });

        today_record_txt.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                today_record_txt.setTextColor(mContext.getResources().getColor(R.color.WQP_Blue));
                past_record_txt.setTextColor(mContext.getResources().getColor(R.color.WQP_Black));
                picking_record_llt.removeAllViews();
                //???PickingLogToday.PHP????????????
                sendRequestWithOkHttpForPickingLogToday();
            }
        });
        past_record_txt.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                past_record_txt.setTextColor(mContext.getResources().getColor(R.color.WQP_Blue));
                today_record_txt.setTextColor(mContext.getResources().getColor(R.color.WQP_Black));
                picking_record_llt.removeAllViews();
                //???PickingLogPast.PHP????????????
                sendRequestWithOkHttpForPickingLogPast();
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
                Intent intent_login = new Intent(PickingActivity.this, LoginActivity.class);
                startActivity(intent_login);
            }
        });
    }

    /**
     * ??????SearchActivity??????????????????
     */
    private void GetResponseData() {
        Bundle bundle = getIntent().getExtras();
        String ResponseText0 = bundle.getString("ResponseText" + 0);
        String ResponseText1 = bundle.getString("ResponseText" + 1);
        String ResponseText2 = bundle.getString("ResponseText" + 2);
        String ResponseText3 = bundle.getString("ResponseText" + 3);
        order_txt.setText(ResponseText1 + "-" + ResponseText2);
        Log.e(LOG, ResponseText0);
        Log.e(LOG, ResponseText1);
        Log.e(LOG, ResponseText2);
        Log.e(LOG, ResponseText3);
    }

    /**
     * Button?????????
     * ??????QR CODE????????????
     */
    public void scanCode(View view) {
        //startActivityForResult(new Intent(this, ScannerActivity.class), 1);
        launchActivity(ScannerActivity.class);
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
                picking_llt.setVisibility(View.VISIBLE);
                factory_id_edt.setText(intent.getStringExtra("result_text"));
                item_number_txt.setText("");
                item_name_txt.setText("");
                item_format_txt.setText("");
                barcode_txt.setText("");
                sendRequestWithOkHttpForFactoryID();
                check_btn.setVisibility(View.VISIBLE);
                picking_edt.setText("");
                note_edt.setText("");
                //???/?????????Spinner
                //TypeSpinner();
                factory_id_edt.setText("");
            } else {
                if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(PickingActivity.this, "????????????", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /**
     * ???/?????????Spinner
     */
    private void TypeSpinner() {
        type_listAdapter = new ArrayAdapter<String>(this, R.layout.white_blod_spinner, type_list);
        type_listAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type_spinner.setAdapter(type_listAdapter);
    }

    /**
     * ????????????Spinner????????????
     */
    private void CompanySpinner() {
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

    /**
     * ???OKHttp??????(????????????????????????)
     */
    private void sendRequestWithOkHttpForFactoryID() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String f_id = factory_id_edt.getText().toString();
                Log.e(LOG, f_id);

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
                item_id = jsonObject.getString("ITEM");
                barcode = jsonObject.getString("BarCode");

                PickingActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        item_number_txt.setText(MB001.toString());
                        item_name_txt.setText(MB002.toString());
                        item_format_txt.setText(MB003.toString());
                        unit_txt.setText(MB004.toString());
                        barcode_txt.setText(barcode.toString());
                        /*//????????????Spinner????????????
                        CompanySpinner();*/
                        //????????????????????????
                        if (COMPANY.toString().equals("WQP")) {
                            company_ch = "??????";
                        } else if (COMPANY.toString().equals("TYT")) {
                            company_ch = "?????????";
                        } else if (COMPANY.toString().equals("BWT")){
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
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ??????UI
     */
    /*@SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    large_llt = new LinearLayout(PickingActivity.this);
                    large_llt.setOrientation(LinearLayout.HORIZONTAL);
                    big_llt = new LinearLayout(PickingActivity.this);
                    big_llt.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout small_llt1 = new LinearLayout(PickingActivity.this);
                    small_llt1.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout small_llt2 = new LinearLayout(PickingActivity.this);
                    small_llt2.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout small_llt3 = new LinearLayout(PickingActivity.this);
                    small_llt3.setOrientation(LinearLayout.HORIZONTAL);

                    Bundle jb = msg.getData();
                    ArrayList<String> JArrayList = new ArrayList<String>();
                    //int i = b.getStringArrayList("JSON_data").size();
                    JArrayList = jb.getStringArrayList("JSON_data");

                    TextView dynamically_empty1;
                    dynamically_empty1 = new TextView(PickingActivity.this);
                    TextView dynamically_empty2;
                    dynamically_empty2 = new TextView(PickingActivity.this);
                    TextView dynamically_empty3;
                    dynamically_empty3 = new TextView(PickingActivity.this);
                    TextView dynamically_empty4;
                    dynamically_empty4 = new TextView(PickingActivity.this);
                    TextView dynamically_empty5;
                    dynamically_empty5 = new TextView(PickingActivity.this);
                    TextView dynamically_empty6;
                    dynamically_empty6 = new TextView(PickingActivity.this);
                    TextView dynamically_empty7;
                    dynamically_empty7 = new TextView(PickingActivity.this);
                    TextView dynamically_empty8;
                    dynamically_empty8 = new TextView(PickingActivity.this);

                    //????????????LinearLayout?????????
                    TextView dynamically_number;
                    dynamically_number = new TextView(PickingActivity.this);
                    dynamically_number.setText("?????? : " + JArrayList.get(1));
                    dynamically_number.setTextColor(Color.rgb(6, 102, 219));
                    dynamically_number.setPadding(20, 10, 0, 0);
                    dynamically_number.setGravity(Gravity.CENTER_VERTICAL);
                    dynamically_number.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                    //dynamically_number.setWidth(50);

                    Button click_btn = new Button(PickingActivity.this);
                    click_btn.setText("??????");
                    click_btn.setBackgroundResource(R.drawable.check_button);
                    click_btn.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
                    click_btn.setTextColor(Color.rgb(6, 102, 219));
                    click_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //sendRequestWithOkHttpForPicking();
                        }
                    });

                    Button delete_btn = new Button(PickingActivity.this);
                    delete_btn.setText("??????");
                    delete_btn.setBackgroundResource(R.drawable.cancel_button);
                    delete_btn.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
                    delete_btn.setTextColor(Color.rgb(6, 102, 219));
                    delete_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            LinearLayout delete_llt = (LinearLayout) view.getParent();
                            picking_llt.removeView(delete_llt);
                        }
                    });

                    //????????????LinearLayout?????????
                    TextView dynamically_name;
                    dynamically_name = new TextView(PickingActivity.this);
                    dynamically_name.setText("?????? : " + JArrayList.get(2));
                    dynamically_name.setTextColor(Color.rgb(6, 102, 219));
                    dynamically_name.setPadding(20, 0, 0, 0);
                    dynamically_name.setGravity(Gravity.CENTER_VERTICAL);
                    dynamically_name.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);

                    //????????????LinearLayout?????????
                    EditText dynamically_count;
                    dynamically_count = new EditText(PickingActivity.this);
                    dynamically_count.setTextColor(Color.rgb(6, 102, 219));
                    dynamically_count.setPadding(20, 0, 0, 0);
                    dynamically_count.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                    dynamically_count.setInputType(InputType.TYPE_CLASS_NUMBER);

                    //????????????LinearLayout???????????????
                    TextView dynamically_unit;
                    dynamically_unit = new TextView(PickingActivity.this);
                    dynamically_unit.setText(JArrayList.get(4));
                    dynamically_unit.setTextColor(Color.rgb(6, 102, 219));
                    dynamically_unit.setPadding(20, 0, 0, 0);
                    dynamically_unit.setGravity(Gravity.CENTER_VERTICAL);
                    dynamically_unit.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);

                    //????????????LinearLayout????????????
                    TextView dynamically_txt = new TextView(PickingActivity.this);
                    dynamically_txt.setBackgroundColor(Color.rgb(220, 220, 220));
                    dynamically_txt.setPadding(20, 0, 0, 10);

                    //LinearLayout.LayoutParams small_pm = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);

                    small_llt1.addView(dynamically_empty1, new LinearLayout.LayoutParams(0 , LinearLayout.LayoutParams.WRAP_CONTENT, (float)0.1));
                    small_llt1.addView(dynamically_number, new LinearLayout.LayoutParams(0 , LinearLayout.LayoutParams.WRAP_CONTENT, 3));
                    small_llt1.addView(dynamically_empty2, new LinearLayout.LayoutParams(0 , LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                    small_llt2.addView(dynamically_empty3, new LinearLayout.LayoutParams(0 , LinearLayout.LayoutParams.WRAP_CONTENT, (float)0.1));
                    small_llt2.addView(dynamically_name, new LinearLayout.LayoutParams(0 , LinearLayout.LayoutParams.WRAP_CONTENT, 3));
                    small_llt2.addView(dynamically_empty4, new LinearLayout.LayoutParams(0 , LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                    small_llt3.addView(dynamically_empty5, new LinearLayout.LayoutParams(0 , LinearLayout.LayoutParams.WRAP_CONTENT, (float)0.1));
                    small_llt3.addView(dynamically_count, new LinearLayout.LayoutParams(0 , LinearLayout.LayoutParams.WRAP_CONTENT, 3));
                    small_llt3.addView(dynamically_unit, new LinearLayout.LayoutParams(0 , LinearLayout.LayoutParams.WRAP_CONTENT, (float)0.5));
                    small_llt3.addView(dynamically_empty6, new LinearLayout.LayoutParams(0 , LinearLayout.LayoutParams.WRAP_CONTENT, (float)0.5));

                    big_llt.addView(small_llt1);
                    big_llt.addView(small_llt2);
                    big_llt.addView(small_llt3);
                    big_llt.addView(dynamically_txt, LinearLayout.LayoutParams.MATCH_PARENT, 3);

                    large_llt.addView(big_llt, new LinearLayout.LayoutParams(0 , LinearLayout.LayoutParams.WRAP_CONTENT, 8));
                    large_llt.addView(delete_btn, new LinearLayout.LayoutParams(0 , LinearLayout.LayoutParams.MATCH_PARENT, 1));
                    large_llt.addView(click_btn, new LinearLayout.LayoutParams(0 , LinearLayout.LayoutParams.MATCH_PARENT, 1));

                    picking_llt.addView(large_llt);

                    *//*LinearLayout gone1 = (LinearLayout) picking_llt.getChildAt(0);
                    LinearLayout gone2 = (LinearLayout) gone1.getChildAt(0);
                    LinearLayout gone3 = (LinearLayout) gone2.getChildAt(0);
                    TextView gone4 = (TextView) gone3.getChildAt(0);
                    gone4.setVisibility(View.GONE);*//*

                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };*/

    /**
     * ???OkHttp????????????(?????????)
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
                } else if (spinner_select.equals("?????????")) {
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
                    warehouse_listAdapter = new ArrayAdapter<String>(mContext, R.layout.white_blod_spinner, warehouse_list);
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
            picking_record_llt.removeAllViews();
            PickingActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    picking_edt.setFocusable(true);
                    picking_edt.setFocusableInTouchMode(true);
                    picking_edt.requestFocus();
                    //???FactoryID.PHP????????????
                    //sendRequestWithOkHttpForFactoryID();
                }
            });
        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    };

    /**
     * ???OKHttp??????(???????????????????????????)
     */
    private void sendRequestWithOkHttpForPickingSearch() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bundle bundle = getIntent().getExtras();
                String COMPANY = bundle.getString("ResponseText" + 0);
                String TE001 = bundle.getString("ResponseText" + 1);
                String TE002 = bundle.getString("ResponseText" + 2);
                Log.e(LOG, COMPANY);

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("TE001", TE001)
                            .add("TE002", TE002)
                            .add("COMPANY", COMPANY)
                            .build();
                    Log.e(LOG, COMPANY + "-" + TE001 + "-" + TE002);
                    Request request = new Request.Builder()
                            .url("http://a.wqp-water.com.tw/WQP_OS/WareHousePickingSearch.php")
                            //.url("http://192.168.0.172/WQP_OS/WareHousePickingSearch.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectForPickingSearch(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * ?????????????????????????????????
     *
     * @param jsonData
     */
    private void parseJSONWithJSONObjectForPickingSearch(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON??????????????????
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                COUNT = jsonObject.getString("COUNT");
                Log.e(LOG, COUNT);
                if (COUNT.equals("1")) {
                    master_btn.setVisibility(View.GONE);
                } else {
                    master_btn.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ???OKHttp??????(?????????????????????)
     */
    private void sendRequestWithOkHttpForPickingMaster() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                SharedPreferences user_id = getSharedPreferences("user_id", MODE_PRIVATE);
                String user_id_data = user_id.getString("ID", "");
                Log.e(LOG, user_id_data);

                Bundle bundle = getIntent().getExtras();
                String COMPANY = bundle.getString("ResponseText" + 0);
                String TE001 = bundle.getString("ResponseText" + 1);
                String TE002 = bundle.getString("ResponseText" + 2);
                Log.e(LOG, COMPANY);

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("TE001", TE001)
                            .add("TE002", TE002)
                            .add("COMPANY", COMPANY)
                            .add("User_id", user_id_data)
                            .build();
                    Log.e(LOG, COMPANY + "-" + TE001 + "-" + TE002 + "-" + user_id_data);
                    Request request = new Request.Builder()
                            .url("http://a.wqp-water.com.tw/WQP_OS/WareHousePickingMaster.php")
                            //.url("http://192.168.0.172/WQP_OS/WareHousePickingMaster.php")
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
     * ???OKHttp??????(?????????????????????)
     */
    private void sendRequestWithOkHttpForPickingDetail() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                SharedPreferences user_id = getSharedPreferences("user_id", MODE_PRIVATE);
                String user_id_data = user_id.getString("ID", "");
                Log.e(LOG, user_id_data);

                Bundle bundle = getIntent().getExtras();
                String TE001 = bundle.getString("ResponseText" + 1).trim();
                String TE002 = bundle.getString("ResponseText" + 2).trim();
                String COMPANY = String.valueOf(company_spinner.getSelectedItem()).trim();
                String TE004 = item_number_txt.getText().toString().trim();
                String TE005 = picking_edt.getText().toString().trim();
                String TE006 = unit_txt.getText().toString().trim();
                String TE008 = String.valueOf(warehouse_spinner.getSelectedItem()).trim();
                String NOTE = note_edt.getText().toString().trim();
                String GET_BACK = String.valueOf(type_spinner.getSelectedItem()).trim();
                Log.e(LOG, TE004);

                if (warehouse_spinner.getSelectedItemId() == 0) {
                    Log.e(LOG, String.valueOf(warehouse_spinner.getSelectedItem()));
                } else if (warehouse_spinner.getSelectedItemId() == 1) {
                    Log.e(LOG, String.valueOf(warehouse_spinner.getSelectedItem()));
                }

                if(TE008.equals("BWT(BWT??????)")){
                    w_id = "BWT";
                    w_name = "BWT??????";
                }else if (TE008.equals("BWT-De(BWT?????????)")){
                    w_id = "BWT-De";
                    w_name = "BWT?????????";
                }else if (TE008.equals("OM-BZA(??????-??????)")){
                    w_id = "OM-BZA";
                    w_name = "??????-??????";
                }else if (TE008.equals("WPA(?????????)")){
                    w_id = "WPA";
                    w_name = "?????????";
                }else if (TE008.equals("WQP(?????????)")){
                    w_id = "WQP";
                    w_name = "?????????";
                }else if (TE008.equals("BRT(???????????????)")){
                    w_id = "BRT";
                    w_name = "???????????????";
                }else if (TE008.equals("DSV(?????????)")){
                    w_id = "DSV";
                    w_name = "?????????";
                }else if (TE008.equals("OM-SOL(??????-??????)")){
                    w_id = "OM-SOL";
                    w_name = "??????-??????";
                }else if (TE008.equals("Borrow(????????????)")){
                    w_id = "Borrow";
                    w_name = "????????????";
                }else if (TE008.equals("01-10000AT(??????AQT???)")){
                    w_id = "01-10000AT";
                    w_name = "??????AQT???";
                }else if (TE008.equals("01-10000TP(???????????????)")){
                    w_id = "01-10000TP";
                    w_name = "???????????????";
                }else if (TE008.equals("01-10000TY(???????????????)")){
                    w_id = "01-10000TY";
                    w_name = "???????????????";
                }else if (TE008.equals("01-10000HS(???????????????)")){
                    w_id = "01-10000HS";
                    w_name = "???????????????";
                }else if (TE008.equals("01-10000TC(???????????????)")){
                    w_id = "01-10000TC";
                    w_name = "???????????????";
                }else if (TE008.equals("01-10000KH(???????????????)")){
                    w_id = "01-10000KH";
                    w_name = "???????????????";
                }else if (TE008.equals("01-00001TY(???????????????)")){
                    w_id = "01-00001TY";
                    w_name = "???????????????";
                }

                if (COMPANY.toString().equals("??????")) {
                    COMPANY = "WQP";
                } else if (COMPANY.toString().equals("?????????")) {
                    COMPANY = "TYT";
                } else {
                    COMPANY = "BWT";
                }

                if (GET_BACK.equals("??????")) {
                    GET_BACK = "??????";
                }

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("TE001", TE001)
                            .add("TE002", TE002)
                            .add("COMPANY", COMPANY)
                            .add("User_id", user_id_data)
                            .add("TE004", TE004)
                            .add("TE005", TE005)
                            .add("TE006", TE006)
                            .add("TE008", w_id)
                            .add("NOTE", NOTE)
                            .add("GET_BACK", GET_BACK)
                            .build();
                    Log.e(LOG, COMPANY + "-" + TE004 + "-" + TE005 + "-" + TE006 + "-" + w_id);
                    Request request = new Request.Builder()
                            .url("http://a.wqp-water.com.tw/WQP_OS/WareHousePickingDetail.php")
                            //.url("http://192.168.0.172/WQP_OS/WareHousePickingDetail.php")
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
     * ???OKHttp??????(?????????/????????????????????????????????????????????????????????????????????????????????????/???????????????Log)
     */
    private void sendRequestWithOkHttpForPickingLogToday() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                Bundle bundle = getIntent().getExtras();
                String COMPANY = bundle.getString("ResponseText" + 0);
                String TE001 = bundle.getString("ResponseText" + 1);
                String TE002 = bundle.getString("ResponseText" + 2);

                Log.e(LOG, TE001);

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("TE001", TE001)
                            .add("TE002", TE002)
                            .add("COMPANY", COMPANY)
                            .build();
                    Request request = new Request.Builder()
                            .url("http://a.wqp-water.com.tw/WQP_OS/WareHousePickingLogToday.php")
                            //.url("http://192.168.0.172/WQP_OS/WareHousePickingLogToday.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectForPickingLog(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * ???OKHttp??????(?????????/????????????????????????????????????????????????????????????????????????????????????/???????????????Log)
     */
    private void sendRequestWithOkHttpForPickingLogPast() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                Bundle bundle = getIntent().getExtras();
                String COMPANY = bundle.getString("ResponseText" + 0);
                String TE001 = bundle.getString("ResponseText" + 1);
                String TE002 = bundle.getString("ResponseText" + 2);

                Log.e(LOG, TE001);

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("TE001", TE001)
                            .add("TE002", TE002)
                            .add("COMPANY", COMPANY)
                            .build();
                    Request request = new Request.Builder()
                            .url("http://a.wqp-water.com.tw/WQP_OS/WareHousePickingLogPast.php")
                            //.url("http://192.168.0.172/WQP_OS/WareHousePickingLogPast.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectForPickingLog(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * ??????????????????????????????????????????????????????????????????????????????????????????/???????????????Log
     *
     * @param jsonData
     */
    private void parseJSONWithJSONObjectForPickingLog(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON??????????????????
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String TE003 = jsonObject.getString("TE003");
                //String GET_BACK = new String(jsonObject.getString("GET_BACK").getBytes(),"utf-8");
                String GET_BACK = jsonObject.getString("GET_BACK");
                String TE004 = jsonObject.getString("TE004");
                String MB002 = jsonObject.getString("MB002");
                String MB003 = jsonObject.getString("MB003");
                String TE005 = jsonObject.getString("TE005");
                String TE006 = jsonObject.getString("TE006");
                String TE008 = jsonObject.getString("TE008");
                String G_B_DATE = jsonObject.getString("G_B_DATE");

                Log.e(LOG, GET_BACK);

                //JSONArray??????SearchData??????
                ArrayList<String> JArrayList = new ArrayList<String>();
                JArrayList.add(TE003);
                JArrayList.add(GET_BACK);
                JArrayList.add(TE004);
                JArrayList.add(MB002);
                JArrayList.add(MB003);
                JArrayList.add(TE005);
                JArrayList.add(TE006);
                JArrayList.add(TE008);
                JArrayList.add(G_B_DATE);

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
                    LinearLayout small_llt1 = new LinearLayout(PickingActivity.this);
                    small_llt1.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout small_llt2 = new LinearLayout(PickingActivity.this);
                    small_llt2.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout small_llt3 = new LinearLayout(PickingActivity.this);
                    small_llt3.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout small_llt4 = new LinearLayout(PickingActivity.this);
                    small_llt4.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout small_llt5 = new LinearLayout(PickingActivity.this);
                    small_llt5.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout small_llt6 = new LinearLayout(PickingActivity.this);
                    small_llt6.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout small_llt7 = new LinearLayout(PickingActivity.this);
                    small_llt7.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout small_llt8 = new LinearLayout(PickingActivity.this);
                    small_llt8.setOrientation(LinearLayout.HORIZONTAL);

                    Bundle jb = msg.getData();
                    ArrayList<String> JArrayList = new ArrayList<String>();
                    //int i = b.getStringArrayList("JSON_data").size();
                    JArrayList = jb.getStringArrayList("JSON_data");

                    //????????????LinearLayout?????????
                    TextView dynamically_number;
                    dynamically_number = new TextView(PickingActivity.this);
                    dynamically_number.setText("?????? : " + JArrayList.get(0));
                    dynamically_number.setGravity(Gravity.LEFT);
                    dynamically_number.setPadding(20, 3, 0, 0);
                    //dynamically_number.setWidth(50);
                    dynamically_number.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);

                    //????????????LinearLayout?????????/??????
                    TextView dynamically_type;
                    dynamically_type = new TextView(PickingActivity.this);
                    dynamically_type.setText("?????? : " + JArrayList.get(1));
                    dynamically_type.setGravity(Gravity.LEFT);
                    dynamically_type.setPadding(20, 3, 0, 0);
                    dynamically_type.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);

                    //????????????LinearLayout?????????
                    TextView dynamically_id;
                    dynamically_id = new TextView(PickingActivity.this);
                    dynamically_id.setText("?????? : " + JArrayList.get(2));
                    dynamically_id.setGravity(Gravity.LEFT);
                    dynamically_id.setPadding(20, 3, 0, 0);
                    dynamically_id.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);

                    //????????????LinearLayout?????????
                    TextView dynamically_name;
                    dynamically_name = new TextView(PickingActivity.this);
                    dynamically_name.setText("?????? : " + JArrayList.get(3));
                    dynamically_name.setGravity(Gravity.LEFT);
                    dynamically_name.setPadding(20, 3, 0, 0);
                    dynamically_name.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);

                    //????????????LinearLayout?????????
                    TextView dynamically_format;
                    dynamically_format = new TextView(PickingActivity.this);
                    dynamically_format.setText("?????? : " + JArrayList.get(4));
                    dynamically_format.setGravity(Gravity.LEFT);
                    dynamically_format.setPadding(20, 3, 0, 0);
                    //dynamically_format.setWidth(50);
                    dynamically_format.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);

                    //????????????LinearLayout???????????????
                    TextView dynamically_count;
                    dynamically_count = new TextView(PickingActivity.this);
                    dynamically_count.setText("???????????? : " + JArrayList.get(5) + " " + JArrayList.get(6));
                    dynamically_count.setGravity(Gravity.LEFT);
                    dynamically_count.setPadding(20, 3, 0, 0);
                    dynamically_count.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);

                    //????????????LinearLayout?????????
                    TextView dynamically_warehouse;
                    dynamically_warehouse = new TextView(PickingActivity.this);
                    dynamically_warehouse.setText("?????? : " + JArrayList.get(7));
                    dynamically_warehouse.setGravity(Gravity.LEFT);
                    dynamically_warehouse.setPadding(20, 3, 0, 0);
                    dynamically_warehouse.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);

                    //????????????LinearLayout??????/????????????
                    TextView dynamically_G_B_date;
                    dynamically_G_B_date = new TextView(PickingActivity.this);
                    dynamically_G_B_date.setText("???/???????????? : " + JArrayList.get(8).substring(0, 19));
                    dynamically_G_B_date.setGravity(Gravity.LEFT);
                    dynamically_G_B_date.setPadding(20, 3, 0, 3);
                    dynamically_G_B_date.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);

                    //????????????LinearLayout????????????
                    TextView dynamically_txt = new TextView(PickingActivity.this);
                    dynamically_txt.setBackgroundColor(Color.rgb(220, 220, 220));
                    dynamically_txt.setPadding(0, 0, 0, 0);

                    LinearLayout.LayoutParams small_pm = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);

                    small_llt1.addView(dynamically_number, small_pm);
                    small_llt2.addView(dynamically_type, small_pm);
                    small_llt3.addView(dynamically_id, small_pm);
                    small_llt4.addView(dynamically_name, small_pm);
                    small_llt5.addView(dynamically_format, small_pm);
                    small_llt6.addView(dynamically_count, small_pm);
                    small_llt7.addView(dynamically_warehouse, small_pm);
                    small_llt8.addView(dynamically_G_B_date, small_pm);

                    picking_record_llt.addView(dynamically_txt, LinearLayout.LayoutParams.MATCH_PARENT, 3);
                    picking_record_llt.addView(small_llt1);
                    picking_record_llt.addView(small_llt2);
                    picking_record_llt.addView(small_llt3);
                    picking_record_llt.addView(small_llt4);
                    picking_record_llt.addView(small_llt5);
                    picking_record_llt.addView(small_llt6);
                    picking_record_llt.addView(small_llt7);
                    picking_record_llt.addView(small_llt8);

                    picking_record_llt.getChildAt(0).setVisibility(View.GONE);

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