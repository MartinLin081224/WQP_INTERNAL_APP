package com.example.a10609516.wqp_internal_app.DepartmentAndDIY;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
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
import com.example.a10609516.wqp_internal_app.Clerk.QuotationActivity;
import com.example.a10609516.wqp_internal_app.Manager.InventoryActivity;
import com.example.a10609516.wqp_internal_app.Manager.OrderSearchActivity;
import com.example.a10609516.wqp_internal_app.R;
import com.example.a10609516.wqp_internal_app.Tools.Adapter;
import com.example.a10609516.wqp_internal_app.Tools.WQPToolsActivity;
import com.example.a10609516.wqp_internal_app.Works.CalendarActivity;
import com.example.a10609516.wqp_internal_app.Works.EngPointsActivity;
import com.example.a10609516.wqp_internal_app.Works.GPSActivity;
import com.example.a10609516.wqp_internal_app.Works.MissCountActivity;
import com.example.a10609516.wqp_internal_app.Works.MissionActivity;
import com.example.a10609516.wqp_internal_app.Works.PointsActivity;
import com.example.a10609516.wqp_internal_app.Works.ScheduleActivity;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.impl.OnItemClickListener;
import com.yanzhenjie.album.widget.divider.Api21ItemDivider;
import com.yanzhenjie.album.widget.divider.Divider;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StationReportSearchActivity extends WQPToolsActivity {

    private SharedPreferences sp;

    private LinearLayout nav_view;
    private DrawerLayout drawer;
    private Toolbar toolbar;

    private ScrollView search_scv;
    private LinearLayout search_llt, result_llt, correct_llt;
    private TableLayout detail_tlt;
    private Button date_btn, search_btn, correct_btn, check_btn, cancel_btn, add_btn, preview_btn, choice_btn, upload_btn;
    private Spinner type_spinner, store_spinner, count_spinner, quantity_spinner;
    private TextView search_up_txt, search_down_txt, id_txt, p_id_txt, type_txt, store_txt, date_txt;
    private TextView dynamically_txt1, dynamically_txt2, dynamically_txt3, dynamically_txt4;
    private EditText day_amount_edt, return_count_edt, return_money_edt, reason_edt, response_edt;

    private ArrayAdapter<String> adapter, adapter2, adapter3;
    private String[] type = new String[]{"?????????", "????????????", "?????????", "?????????", "?????????"};
    private String[] empty = new String[]{"(?????????)"};
    private String[] diy = new String[]{};
    private Context context;

    private String[] count = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "30??????"};

    private ImageView[] dynamically_imv;

    private Adapter mAdapter;
    private ArrayList<AlbumFile> mAlbumFiles;
    private RecyclerView recyclerView;

    private OkHttpClient photo_client;
    private String path;
    private String savePath = "";
    private File image_file;
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    private Context mContext = this;
    private String LOG = "StationReportSearchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_report_search);
        //?????????????????????
        initViews();
        //Menu???onClickListener
        MenuListener();
        //??????Toolbar
        SetToolBar();
        //????????????Spinner
        SpinnerAdapter();
        //???????????????
        AlbumGlide();
    }

    /**
     * ?????????????????????
     */
    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        nav_view = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer_layout);
        recyclerView = findViewById(R.id.recycler_view);
        search_scv = (ScrollView) findViewById(R.id.search_scv);
        search_llt = (LinearLayout) findViewById(R.id.search_llt);
        result_llt = (LinearLayout) findViewById(R.id.result_llt);
        correct_llt = (LinearLayout) findViewById(R.id.correct_llt);
        detail_tlt = (TableLayout) findViewById(R.id.detail_tlt);
        date_btn = (Button) findViewById(R.id.date_btn);
        search_btn = (Button) findViewById(R.id.search_btn);
        correct_btn = (Button) findViewById(R.id.correct_btn);
        check_btn = (Button) findViewById(R.id.check_btn);
        cancel_btn = (Button) findViewById(R.id.cancel_btn);
        add_btn = (Button) findViewById(R.id.add_btn);
        preview_btn = (Button) findViewById(R.id.preview_btn);
        choice_btn = (Button) findViewById(R.id.choice_btn);
        upload_btn = (Button) findViewById(R.id.upload_btn);
        type_spinner = (Spinner) findViewById(R.id.type_spinner);
        store_spinner = (Spinner) findViewById(R.id.store_spinner);
        count_spinner = (Spinner) findViewById(R.id.count_spinner);
        quantity_spinner = (Spinner) findViewById(R.id.quantity_spinner);
        search_up_txt = (TextView) findViewById(R.id.search_up_txt);
        search_down_txt = (TextView) findViewById(R.id.search_down_txt);
        id_txt = (TextView) findViewById(R.id.id_txt);
        p_id_txt = (TextView) findViewById(R.id.p_id_txt);
        type_txt = (TextView) findViewById(R.id.type_txt);
        store_txt = (TextView) findViewById(R.id.store_txt);
        date_txt = (TextView) findViewById(R.id.date_txt);
        day_amount_edt = (EditText) findViewById(R.id.day_amount_edt);
        return_count_edt = (EditText) findViewById(R.id.return_count_edt);
        return_money_edt = (EditText) findViewById(R.id.return_money_edt);
        reason_edt = (EditText) findViewById(R.id.reason_edt);
        response_edt = (EditText) findViewById(R.id.response_edt);

        //???????????????search_llt
        search_up_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_llt.setVisibility(View.GONE);
                search_up_txt.setVisibility(View.GONE);
                search_down_txt.setVisibility(View.VISIBLE);
            }
        });

        //???????????????search_llt
        search_down_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_llt.setVisibility(View.VISIBLE);
                search_up_txt.setVisibility(View.VISIBLE);
                search_down_txt.setVisibility(View.GONE);
            }
        });

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (date_btn.getText().toString().equals("") || type_spinner.getSelectedItemId() == 0 || store_spinner.getSelectedItemId() == 0) {
                    Toast.makeText(StationReportSearchActivity.this, "???????????????????????????", Toast.LENGTH_SHORT).show();
                } else {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            type_txt.setText("");
                            store_txt.setText("");
                            date_txt.setText("");
                            day_amount_edt.setText("");
                            return_count_edt.setText("");
                            return_money_edt.setText("");
                            reason_edt.setText("");
                            response_edt.setText("");
                        }
                    });

                    search_llt.setVisibility(View.GONE);
                    search_up_txt.setVisibility(View.GONE);
                    search_down_txt.setVisibility(View.VISIBLE);
                    result_llt.setVisibility(View.VISIBLE);
                    detail_tlt.setVisibility(View.VISIBLE);
                    detail_tlt.removeAllViews();
                    //???OkHttp???????????? ?????????????????????????????????ID
                    sendRequestWithOkHttpForStationReportSearchMaster();
                    //???OkHttp???????????? ????????????????????????
                    sendRequestWithOkHttpForStationReportSearchDetail();
                }
            }
        });

        correct_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        correct_btn.setVisibility(View.GONE);
                        correct_llt.setVisibility(View.VISIBLE);
                        count_spinner.setEnabled(true);
                        quantity_spinner.setEnabled(true);
                        /*day_amount_edt.setFocusable(true);
                        return_count_edt.setFocusable(true);
                        return_money_edt.setFocusable(true);
                        reason_edt.setFocusable(true);
                        response_edt.setFocusable(true);*/
                    }
                });
            }
        });

        check_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //???OkHttp???????????? ??????????????????
                sendRequestWithOkHttpForStationReportCorrectMaster();
                correct_btn.setVisibility(View.VISIBLE);
                correct_llt.setVisibility(View.GONE);
                Toast.makeText(StationReportSearchActivity.this, "????????????", Toast.LENGTH_SHORT).show();
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                correct_btn.setVisibility(View.VISIBLE);
                correct_llt.setVisibility(View.GONE);
                //???OkHttp???????????? ?????????????????????????????????ID
                sendRequestWithOkHttpForStationReportSearchMaster();
                detail_tlt.removeAllViews();
                //???OkHttp???????????? ????????????????????????
                sendRequestWithOkHttpForStationReportSearchDetail();
                Toast.makeText(StationReportSearchActivity.this, "????????????", Toast.LENGTH_SHORT).show();
            }
        });

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //??????LoginActivity???????????????
                SharedPreferences user_id = getSharedPreferences("user_id_data", MODE_PRIVATE);
                String user_id_data = user_id.getString("ID", "");
                String store = String.valueOf(store_spinner.getSelectedItem());
                String date = date_btn.getText().toString();

                Intent intent_add = new Intent(StationReportSearchActivity.this, StationReportAddActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("USER_ID", user_id_data);
                bundle.putString("SHOP_ID", store);
                bundle.putString("BUSINESS_DATE", date);

                intent_add.putExtras(bundle);//????????????????????????
                startActivity(intent_add);

            }
        });

        preview_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previewImage(0);
            }
        });

        choice_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recyclerView.getChildCount() != 0) {
                    uploadImage();
                    //???OkHttp????????????(??????????????????)
                    sendRequestWithOkHttpForOrderPhotoUpdate();
                    Toast.makeText(StationReportSearchActivity.this, "????????????", Toast.LENGTH_SHORT).show();
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
                Intent intent_login = new Intent(StationReportSearchActivity.this, LoginActivity.class);
                startActivity(intent_login);
            }
        });
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
     * ??????????????????
     */
    private void SpinnerAdapter() {
        context = this;
        //?????????????????????????????????????????????
        adapter = new ArrayAdapter<String>(this, R.layout.white_blod_spinner, type);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type_spinner.setAdapter(adapter);
        type_spinner.setOnItemSelectedListener(type_selectListener);
        //?????????????????????????????????????????????????????????
        adapter2 = new ArrayAdapter<String>(this,  R.layout.white_blod_spinner, empty);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        store_spinner.setAdapter(adapter2);

    }

    /**
     * ????????? ?????????????????????????????????
     */
    private AdapterView.OnItemSelectedListener type_selectListener = new AdapterView.OnItemSelectedListener() {
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
            //?????????????????????????????????????????????
            //int pos = type_spinner.getSelectedItemPosition();
            //???DIYStore.PHP????????????
            sendRequestWithOkHttpForStore();
        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    };

    /**
     * ???OkHttp????????????(DIYStore)
     */
    private void sendRequestWithOkHttpForStore() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String spinner_select = String.valueOf(type_spinner.getSelectedItem());
                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("STORE", spinner_select)
                            .build();
                    Log.e("PictureActivity", spinner_select);
                    Request request = new Request.Builder()
                            //.url("http://192.168.0.172/WQP_OS/DIYStore.php")
                            .url("http://a.wqp-water.com.tw/WQP_OS/DIYStore.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectForStore(responseData);
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
    private void parseJSONWithJSONObjectForStore(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            ArrayList<String> JArrayList = new ArrayList<String>();
            diy = new String[]{};
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON??????????????????
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String DIY_store = jsonObject.getString("D_NAME");
                //JSONArray??????SearchData??????
                JArrayList.add(DIY_store);
                diy = JArrayList.toArray(new String[i]);
                //Log.e("PictureActivity3", diy[i]);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (type_spinner.getSelectedItemId() == 0) {
                        adapter2 = new ArrayAdapter<String>(StationReportSearchActivity.this,  R.layout.white_blod_spinner, empty);
                        adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                        store_spinner.setAdapter(adapter2);
                    } else {
                        //??????????????????Adapter/*????????????????????????type2[pos]*/
                        adapter2 = new ArrayAdapter<String>(context,  R.layout.white_blod_spinner, diy);
                        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        //???????????????????????????Spinner
                        store_spinner.setAdapter(adapter2);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ???OkHttp???????????? ?????????????????????????????????ID
     */
    private void sendRequestWithOkHttpForStationReportSearchMaster() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                //??????LoginActivity???????????????
                SharedPreferences user_id = getSharedPreferences("user_id", MODE_PRIVATE);
                String user_id_data = user_id.getString("ID", "");
                //??????????????????????????????
                String date = date_btn.getText().toString();
                String store = String.valueOf(store_spinner.getSelectedItem());

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("USER_ID", user_id_data)
                            .add("BUSINESS_DATE", date)
                            .add("SHOP_ID", store)

                            .build();
                    Log.e(LOG, user_id_data);
                    Log.e(LOG, date);
                    Log.e(LOG, store);

                    Request request = new Request.Builder()
                            //.url("http://192.168.0.172/WQP_OS/StationShopBusinessSearchMaster.php")
                            .url("http://a.wqp-water.com.tw/WQP_OS/StationShopBusinessSearchMaster.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, requestBody.toString());
                    Log.e(LOG, response.toString());
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectForRequisitionSearchMaster(responseData);
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
    private void parseJSONWithJSONObjectForRequisitionSearchMaster(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON??????????????????
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String R_ID = jsonObject.getString("R_ID");
                String path_name = jsonObject.getString("PATH_ID");
                String shop_name = jsonObject.getString("SHOP_ID");
                String business_date = jsonObject.getString("BUSINESS_DATE");
                String day_amount = jsonObject.getString("DAY_AMOUNT");
                String visitors_count = jsonObject.getString("VISITORS_COUNT");
                String return_count = jsonObject.getString("RETURN_COUNT");
                String return_money = jsonObject.getString("RETURN_MONEY");
                String unsold_reason = jsonObject.getString("UNSOLD_REASON");
                String customer_count = jsonObject.getString("CUSTOMER_COUNT");
                String q_response = jsonObject.getString("Q_RESPONSE");
                String R_P_ID = jsonObject.getString("R_P_ID");

                Log.e(LOG, R_ID);
                Log.e(LOG, path_name);
                Log.e(LOG, shop_name);
                Log.e(LOG, business_date);
                Log.e(LOG, day_amount);
                Log.e(LOG, visitors_count);
                Log.e(LOG, return_count);
                Log.e(LOG, return_money);
                Log.e(LOG, unsold_reason);
                Log.e(LOG, customer_count);
                Log.e(LOG, q_response);
                Log.e(LOG, R_P_ID);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter3 = new ArrayAdapter<String>(StationReportSearchActivity.this, R.layout.white_blod_spinner, count);
                        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        count_spinner.setAdapter(adapter3);
                        //????????????ResponseText??????????????????????????? ????????????ResponseText???Spinner??????
                        for (int a = 0; a < count.length; a++) {
                            if (count[a].equals(visitors_count)) {
                                count_spinner.setSelection(a, true);
                                break;
                            }
                        }
                        quantity_spinner.setAdapter(adapter3);
                        for (int b = 0; b < count.length; b++) {
                            if (count[b].equals(customer_count)) {
                                quantity_spinner.setSelection(b, true);
                                break;
                            }
                        }

                        count_spinner.setEnabled(false);
                        quantity_spinner.setEnabled(false);
                        /*day_amount_edt.setFocusable(false);
                        return_count_edt.setFocusable(false);
                        return_money_edt.setFocusable(false);
                        reason_edt.setFocusable(false);
                        response_edt.setFocusable(false);*/

                        id_txt.setText(R_ID);
                        p_id_txt.setText(R_P_ID);
                        type_txt.setText(path_name);
                        store_txt.setText(shop_name);
                        date_txt.setText(business_date);
                        day_amount_edt.setText(day_amount);
                        return_count_edt.setText(return_count);
                        return_money_edt.setText(return_money);
                        reason_edt.setText(unsold_reason);
                        response_edt.setText(q_response);

                    }
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ???OkHttp???????????? ??????????????????
     */
    private void sendRequestWithOkHttpForStationReportCorrectMaster() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                String R_ID = id_txt.getText().toString();
                String day_amount = day_amount_edt.getText().toString();
                String return_count = return_count_edt.getText().toString();
                String return_money = return_money_edt.getText().toString();
                String reason = reason_edt.getText().toString();
                String response_suggest = response_edt.getText().toString();
                String count = String.valueOf(count_spinner.getSelectedItem());
                String quantity = String.valueOf(quantity_spinner.getSelectedItem());

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("R_ID", R_ID)
                            .add("DAY_AMOUNT", day_amount)
                            .add("RETURN_COUNT", return_count)
                            .add("RETURN_MONEY", return_money)
                            .add("UNSOLD_REASON", reason)
                            .add("Q_RESPONSE", response_suggest)
                            .add("VISITORS_COUNT", count)
                            .add("CUSTOMER_COUNT", quantity)
                            .build();
                    Log.e(LOG, R_ID);
                    Request request = new Request.Builder()
                            //.url("http://192.168.0.172/WQP_OS/StationShopBusinessCorrectMaster.php")
                            .url("http://a.wqp-water.com.tw/WQP_OS/StationShopBusinessCorrectMaster.php")
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
     * ???OkHttp???????????? ????????????????????????
     */
    private void sendRequestWithOkHttpForStationReportSearchDetail() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                //??????LoginActivity???????????????
                SharedPreferences user_id = getSharedPreferences("user_id", MODE_PRIVATE);
                String user_id_data = user_id.getString("ID", "");
                //??????????????????????????????
                String date = date_btn.getText().toString();
                String store = String.valueOf(store_spinner.getSelectedItem());

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("USER_ID", user_id_data)
                            .add("BUSINESS_DATE", date)
                            .add("SHOP_ID", store)

                            .build();
                    Log.e(LOG, user_id_data);
                    Log.e(LOG, date);
                    Log.e(LOG, store);

                    Request request = new Request.Builder()
                            //.url("http://192.168.0.172/WQP_OS/StationShopBusinessSearchDetail.php")
                            .url("http://a.wqp-water.com.tw/WQP_OS/StationShopBusinessSearchDetail.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, requestBody.toString());
                    Log.e(LOG, response.toString());
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectForRequisitionSearchDetail(responseData);
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
    private void parseJSONWithJSONObjectForRequisitionSearchDetail(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            dynamically_imv = new ImageView[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON??????????????????
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String D_R_ID = jsonObject.getString("D_R_ID");
                String d_item_name = jsonObject.getString("ITEM_NAME");
                String d_item_count = jsonObject.getString("ITEM_COUNT");
                String d_item_amount = jsonObject.getString("ITEM_AMOUNT");

                //JSONArray??????SearchData??????
                ArrayList<String> JArrayList = new ArrayList<String>();
                JArrayList.add(D_R_ID);
                JArrayList.add(d_item_name);
                JArrayList.add(d_item_count);
                JArrayList.add(d_item_amount);

                Log.e(LOG, D_R_ID);
                Log.e(LOG, d_item_name);
                Log.e(LOG, d_item_count);
                Log.e(LOG, d_item_amount);

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
                    Resources resources = getResources();
                    float type_Dip = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, resources.getDisplayMetrics());
                    int type_dip = Math.round(type_Dip);
                    //????????????????????????TableLayout,?????????TableRow????????????TableLayout
                    //????????????????????????
                    detail_tlt.setStretchAllColumns(true);
                    //?????????TableLayout???TableRow
                    TableRow big_tbr = new TableRow(StationReportSearchActivity.this);
                    //????????????????????????TableLayout
                    TableLayout small_tb = new TableLayout(StationReportSearchActivity.this);
                    //??????????????????????????????
                    small_tb.setStretchAllColumns(true);
                    small_tb.setBackgroundResource(R.drawable.frame_gray_white_line);
                    //??????TableRow?????????
                    TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

                    Bundle jb = msg.getData();
                    ArrayList<String> JArrayList = new ArrayList<String>();
                    JArrayList = jb.getStringArrayList("JSON_data");

                    Log.e(LOG, JArrayList.get(0));
                    //????????????TableLayout???Button???????????????????????????Button???ID
                    int loc = 0;
                    for (int j = 0; j < dynamically_imv.length; j++) {
                        if (dynamically_imv[j] == null) {
                            loc = j;
                            break;
                        }
                    }
                    dynamically_imv[loc] = new ImageView(StationReportSearchActivity.this);
                    dynamically_imv[loc].setImageResource(R.drawable.quotation);
                    dynamically_imv[loc].setScaleType(ImageView.ScaleType.CENTER);
                    dynamically_imv[loc].setId(loc);
                    dynamically_imv[loc].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for (int a = 0; a < dynamically_imv.length; a++) {
                                if (v.getId() == dynamically_imv[a].getId()) {
                                    Intent intent_quotation = new Intent(StationReportSearchActivity.this, StationReportCorrectActivity.class);
                                    //?????????TableLayout?????????TableRow??????
                                    TableRow tb_tbr = (TableRow) detail_tlt.getChildAt(a);
                                    //?????????TableRow?????????TableLayout??????
                                    TableLayout tb_tbr_tb = (TableLayout) tb_tbr.getChildAt(1);
                                    //?????????TableLayout?????????TableRow??????
                                    TableRow tb_tbr_tb_tbr = (TableRow) tb_tbr_tb.getChildAt(0);
                                    //?????????????????????
                                    for (int x = 0; x < 4; x++) {
                                        //???TableRow??????layout_column??????
                                        TextView order_txt = (TextView) tb_tbr_tb_tbr.getChildAt(x);
                                        String ResponseText = order_txt.getText().toString();
                                        //???SQL??????????????????MaintainActivity
                                        Bundle bundle = new Bundle();
                                        bundle.putString("ResponseText" + x, ResponseText);

                                        intent_quotation.putExtras(bundle);//????????????????????????
                                        Log.e(LOG, ResponseText);

                                    }
                                    detail_tlt.removeAllViews();
                                    startActivity(intent_quotation);
                                }
                            }
                        }
                    });

                    //????????????TableLayout???SQL??????
                    //TextView dynamically_txt1;
                    dynamically_txt1 = new TextView(StationReportSearchActivity.this);
                    dynamically_txt1.setText(JArrayList.get(0));
                    dynamically_txt1.setPadding(0, 2, 0, 0);
                    dynamically_txt1.setGravity(Gravity.LEFT);
                    dynamically_txt1.setTextColor(Color.rgb(6, 102, 219));
                    dynamically_txt1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                    dynamically_txt1.setVisibility(View.GONE);
                    //????????????TableLayout???SQL??????
                    //TextView dynamically_txt2;
                    dynamically_txt2 = new TextView(StationReportSearchActivity.this);
                    dynamically_txt2.setText(JArrayList.get(1).trim());
                    dynamically_txt2.setPadding(0, 2, 0, 0);
                    dynamically_txt2.setGravity(Gravity.LEFT);
                    dynamically_txt2.setTextColor(Color.rgb(6, 102, 219));
                    dynamically_txt2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                    //????????????TableLayout???SQL??????
                    //TextView dynamically_txt3;
                    dynamically_txt3 = new TextView(StationReportSearchActivity.this);
                    dynamically_txt3.setText(JArrayList.get(2));
                    dynamically_txt3.setPadding(0, 2, 0, 0);
                    dynamically_txt3.setGravity(Gravity.LEFT);
                    dynamically_txt3.setTextColor(Color.rgb(6, 102, 219));
                    dynamically_txt3.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                    dynamically_txt3.setVisibility(View.GONE);
                    //????????????TableLayout???SQL??????
                    //TextView dynamically_txt3;
                    dynamically_txt4 = new TextView(StationReportSearchActivity.this);
                    dynamically_txt4.setText(JArrayList.get(3));
                    dynamically_txt4.setPadding(0, 2, 0, 0);
                    dynamically_txt4.setGravity(Gravity.LEFT);
                    dynamically_txt4.setTextColor(Color.rgb(6, 102, 219));
                    dynamically_txt4.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                    dynamically_txt4.setVisibility(View.GONE);
                    TableRow tr1 = new TableRow(StationReportSearchActivity.this);
                    tr1.setGravity(Gravity.CENTER_HORIZONTAL);
                    tr1.addView(dynamically_txt1, new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, type_dip));
                    tr1.addView(dynamically_txt2, new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, type_dip));
                    tr1.addView(dynamically_txt3, new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, type_dip));
                    tr1.addView(dynamically_txt4, new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, type_dip));

                    small_tb.addView(tr1, layoutParams);
                    //about_ImageView??????0,??????WRAP_CONTENT,????????????1
                    big_tbr.addView(dynamically_imv[loc], new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
                    //small_tb??????0,??????WRAP_CONTENT,????????????3
                    big_tbr.addView(small_tb, new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 2));
                    //dynamically_imv[0].setVisibility(View.INVISIBLE);

                    TableRow.LayoutParams the_param3;
                    the_param3 = (TableRow.LayoutParams) small_tb.getLayoutParams();
                    the_param3.span = 2;
                    the_param3.width = TableRow.LayoutParams.MATCH_PARENT;
                    small_tb.setLayoutParams(the_param3);

                    detail_tlt.addView(big_tbr);

                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * ???OkHttp????????????(??????????????????)
     */
    private void sendRequestWithOkHttpForOrderPhotoUpdate() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                //??????LoginActivity???????????????
                SharedPreferences user_id = getSharedPreferences("user_id", MODE_PRIVATE);
                String user_id_data = user_id.getString("ID", "");
                String p_id = p_id_txt.getText().toString();
                String report_date = date_txt.getText().toString();

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("R_P_ID", p_id)
                            .add("FILE_NAME", savePath)
                            .add("USER_ID", user_id_data)
                            .build();
                    Log.e(LOG, p_id);
                    Log.e(LOG, report_date);
                    Log.e(LOG, user_id_data);
                    Request request = new Request.Builder()
                            //.url("http://192.168.0.172/WQP_OS/StationShopBusinessFileUpdate.php")
                            .url("http://a.wqp-water.com.tw/WQP_OS/StationShopBusinessFileUpdate.php")
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
     * ???????????????
     */
    private void AlbumGlide() {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));//??????????????????
        Divider divider = new Api21ItemDivider(Color.TRANSPARENT, 10, 10);
        recyclerView.addItemDecoration(divider);

        mAdapter = new Adapter(this, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                previewImage(position);
            }
        });
        recyclerView.setAdapter(mAdapter);

        Log.e(LOG + "111", mAdapter.toString());
    }

    /**
     * Select picture, from album.
     */
    private void selectImage() {
        Album.image(this)
                .multipleChoice()
                .camera(true)
                //??????????????????
                .columnCount(2)
                //??????????????????
                .selectCount(30)
                .checkedList(mAlbumFiles)

                .onResult(new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(@NonNull ArrayList<AlbumFile> result) {
                        mAlbumFiles = result;
                        mAdapter.notifyDataSetChanged(mAlbumFiles);
                        //mTvMessage.setVisibility(result.size() > 0 ? View.VISIBLE : View.GONE);
                        Log.e(LOG, result.toString());
                    }
                })
                .onCancel(new Action<String>() {
                    @Override
                    public void onAction(@NonNull String result) {
                        Toast.makeText(StationReportSearchActivity.this, R.string.canceled, Toast.LENGTH_LONG).show();
                    }
                })
                .start();
    }

    /**
     * Preview image, to album.
     */
    private void previewImage(int position) {
        if (mAlbumFiles == null || mAlbumFiles.size() == 0) {
            Toast.makeText(this, R.string.no_selected, Toast.LENGTH_LONG).show();
        } else {
            Album.galleryAlbum(this)
                    .checkable(true)
                    .checkedList(mAlbumFiles)
                    .currentPosition(position)

                    .onResult(new Action<ArrayList<AlbumFile>>() {
                        @Override
                        public void onAction(@NonNull ArrayList<AlbumFile> result) {
                            mAlbumFiles = result;
                            mAdapter.notifyDataSetChanged(mAlbumFiles);
                            //mTvMessage.setVisibility(result.size() > 0 ? View.VISIBLE : View.GONE);

                            Log.e(LOG + "123", mAlbumFiles.toString());
                        }
                    })
                    .start();
        }
    }

    /**
     * ???????????????????????????????????????
     */
    public boolean onTouchEvent(MotionEvent event) {
        if(null != this.getCurrentFocus()){
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super .onTouchEvent(event);
    }

    private void uploadImage() {
        photo_client = new OkHttpClient();
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        //?????????????????????????????????????????????SERVER
        for(int x = 0; x < mAlbumFiles.size(); x++) {
            //GET the file path
            AlbumFile file = mAlbumFiles.get(x);
            path = file.getPath();
            Log.e(LOG + "888", path);

            image_file = new File(path);
            builder.addFormDataPart("img", image_file.getName(), RequestBody.create(MEDIA_TYPE_PNG, image_file));
            Log.e(LOG + "8888", image_file.getName());

            String fileName = "C:\\xampp\\htdocs\\WQP\\station_order_pic\\";
            savePath = savePath + fileName + image_file.getName() + ",";
            Log.e(LOG + "BBB", savePath);

            // TODO upload...
            MultipartBody requestBody = builder.build();
            Request request = new Request.Builder()
                    //.url("http://192.168.0.172/WQP_OS/RequisitionPicture.php")
                    .url("http://a.wqp-water.com.tw/WQP_OS/StationOrderPicture.php")
                    .post(requestBody)
                    .build();
            photo_client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(LOG + "FAIL", "onFailure: ??????" + e.getLocalizedMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.e(LOG + "SUCCESS", "onResponse: " + response.body().string());
                    //????????????????????????....
                }
            });
        }

        Log.e(LOG + "BBBB", savePath);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(LOG, "onRestart");
        detail_tlt.removeAllViews();
        //???OkHttp???????????? ?????????????????????????????????ID
        sendRequestWithOkHttpForStationReportSearchMaster();
        //???OkHttp???????????? ????????????????????????
        sendRequestWithOkHttpForStationReportSearchDetail();
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