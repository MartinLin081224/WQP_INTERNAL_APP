package com.example.a10609516.wqp_internal_app.Clerk;

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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class QuotationActivity extends WQPToolsActivity {

    private SharedPreferences sp;

    private LinearLayout nav_view;
    private DrawerLayout drawer;
    private Toolbar toolbar;

    private LinearLayout separate_llt;
    private TableLayout quotation_TableLayout;
    private Spinner company_spinner, quotation_type_spinner, quotation_mode_spinner;
    private EditText quotation_no_edt, customer_edt, clerk_edt;
    private Button search_btn;
    private ImageView[] dynamically_imv;
    private TextView dynamically_txt1, dynamically_txt2, dynamically_txt3;

    private Context mContext = this;
    private String LOG = "QuotationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotation);
        //取得控制項物件
        initViews();
        //Menu的onClickListener
        MenuListener();
        //設置Toolbar
        SetToolBar();
        //報價單公司別的Spinner下拉選單
        CompanySpinner();
        //報價單別的Spinner下拉選單
        TypeSpinner();
        //報價單狀態的Spinner下拉選單
        ModeSpinner();
    }

    /**
     * 取得控制項物件
     */
    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        nav_view = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer_layout);
        separate_llt = (LinearLayout) findViewById(R.id.separate_llt);
        quotation_TableLayout = (TableLayout) findViewById(R.id.quotation_TableLayout);
        company_spinner = (Spinner) findViewById(R.id.company_spinner);
        quotation_type_spinner = (Spinner) findViewById(R.id.quotation_type_spinner);
        quotation_mode_spinner = (Spinner) findViewById(R.id.quotation_mode_spinner);
        quotation_no_edt = (EditText) findViewById(R.id.quotation_no_edt);
        customer_edt = (EditText) findViewById(R.id.customer_edt);
        clerk_edt = (EditText) findViewById(R.id.clerk_edt);
        search_btn = (Button) findViewById(R.id.search_btn);

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //讓search_tablelayout資料清空
                quotation_TableLayout.removeAllViews();
                //separate_llt、title_llt、quotation_TableLayout
                separate_llt.setVisibility(View.VISIBLE);
                quotation_TableLayout.setVisibility(View.VISIBLE);
                //建立QuotationData.php OKHttp連線
                sendRequestWithOkHttpForQuotation();
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

        toolbar.setTitle("報價單查詢");
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
                Intent intent_login = new Intent(QuotationActivity.this, LoginActivity.class);
                startActivity(intent_login);
            }
        });
    }

    /**
     * 報價單公司別的Spinner下拉選單
     */
    private void CompanySpinner() {
        //Spinner下拉選單
        final String[] returns = {"拓霖", "拓亞鈦"};
        ArrayAdapter<String> company_List = new ArrayAdapter<>(QuotationActivity.this,
                R.layout.white_blod_spinner,
                returns);
        company_List.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        company_spinner.setAdapter(company_List);
    }

    /**
     * 報價單別的Spinner下拉選單
     */
    private void TypeSpinner() {
        //Spinner下拉選單
        final String[] returns = {"W210", "W211", "W212"};
        ArrayAdapter<String> quotation_type_List = new ArrayAdapter<>(QuotationActivity.this,
                R.layout.white_blod_spinner,
                returns);
        quotation_type_List.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quotation_type_spinner.setAdapter(quotation_type_List);
    }

    /**
     * 報價單狀態的Spinner下拉選單
     */
    private void ModeSpinner() {
        //Spinner下拉選單
        final String[] returns = {"送審中", "報價完成", "退回"};
        ArrayAdapter<String> quotation_mode_List = new ArrayAdapter<>(QuotationActivity.this,
                R.layout.white_blod_spinner,
                returns);
        quotation_mode_List.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quotation_mode_spinner.setAdapter(quotation_mode_List);
    }

    /**
     * 與OkHttp建立連線
     */
    private void sendRequestWithOkHttpForQuotation() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //接收LoginActivity傳過來的值
                SharedPreferences user_id = getSharedPreferences("user_id", MODE_PRIVATE);
                String user_id_data = user_id.getString("ID", "");
                Log.i(LOG, user_id_data);

                String company = String.valueOf(company_spinner.getSelectedItem());
                String quotation_type = String.valueOf(quotation_type_spinner.getSelectedItem());
                String quotation_no = quotation_no_edt.getText().toString();
                String customer = customer_edt.getText().toString();
                String clerk = clerk_edt.getText().toString();
                String quotation_mode = String.valueOf(quotation_mode_spinner.getSelectedItem());

                if (company.equals("拓霖")) {
                    company = "WQP";
                } else {
                    company = "TYT";
                }
                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("User_id", user_id_data)
                            .add("COMPANY", company)
                            .add("TA001", quotation_type)
                            .add("TA002", quotation_no)
                            .add("TA004", customer)
                            .add("TA005", clerk)
                            .add("PROCESS", quotation_mode)
                            .build();
                    Log.e(LOG, user_id_data);
                    Log.e(LOG, clerk);
                    Log.e(LOG, quotation_type);
                    Log.e(LOG, quotation_no);
                    Log.e(LOG, quotation_mode);
                    Request request = new Request.Builder()
                            //.url("http://192.168.0.172/WQP_OS/QuotationSearch.php")
                            .url("http://a.wqp-water.com.tw/WQP_OS/QuotationSearch.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, requestBody.toString());
                    Log.e(LOG, response.toString());
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectForQuotation(responseData);
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
    private void parseJSONWithJSONObjectForQuotation(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            dynamically_imv = new ImageView[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON格式改為字串
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String COMPANY = jsonObject.getString("COMPANY");
                String TA002 = jsonObject.getString("NUMBER");
                String TA004 = jsonObject.getString("CUST");
                String TA005 = jsonObject.getString("CLERK");

                //JSONArray加入SearchData資料
                ArrayList<String> JArrayList = new ArrayList<String>();
                JArrayList.add(COMPANY);
                JArrayList.add(TA002);
                JArrayList.add(TA004);
                JArrayList.add(TA005);

                Log.e(LOG, COMPANY);
                Log.e(LOG, TA002);
                Log.e(LOG, TA004);
                Log.e(LOG, TA005);

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
                    Resources resources = getResources();
                    float type_Dip = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, resources.getDisplayMetrics());
                    int type_dip = Math.round(type_Dip);
                    //最外層有一個大的TableLayout,再設置TableRow包住小的TableLayout
                    //平均分配列的寬度
                    quotation_TableLayout.setStretchAllColumns(true);
                    //設置大TableLayout的TableRow
                    TableRow big_tbr = new TableRow(QuotationActivity.this);
                    //設置每筆資料的小TableLayout
                    TableLayout small_tb = new TableLayout(QuotationActivity.this);
                    //全部列自動填充空白處
                    small_tb.setStretchAllColumns(true);
                    small_tb.setBackgroundResource(R.drawable.bg_blue);
                    //設定TableRow的寬高
                    TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

                    Bundle jb = msg.getData();
                    ArrayList<String> JArrayList = new ArrayList<String>();
                    JArrayList = jb.getStringArrayList("JSON_data");

                    Log.e(LOG, JArrayList.get(0));
                    //設置每筆TableLayout的Button監聽器、與動態新增Button的ID
                    int loc = 0;
                    for (int j = 0; j < dynamically_imv.length; j++) {
                        if (dynamically_imv[j] == null) {
                            loc = j;
                            break;
                        }
                    }
                    dynamically_imv[loc] = new ImageView(QuotationActivity.this);
                    dynamically_imv[loc].setImageResource(R.drawable.quotation);
                    dynamically_imv[loc].setScaleType(ImageView.ScaleType.CENTER);
                    dynamically_imv[loc].setId(loc);
                    dynamically_imv[loc].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for (int a = 0; a < dynamically_imv.length; a++) {
                                if (v.getId() == dynamically_imv[a].getId()) {
                                    Intent intent_quotation = new Intent(QuotationActivity.this, AuthorizeActivity.class);
                                    //取得大TableLayout中的大TableRow位置
                                    TableRow tb_tbr = (TableRow) quotation_TableLayout.getChildAt(a);
                                    //取得大TableRow中的小TableLayout位置
                                    TableLayout tb_tbr_tb = (TableLayout) tb_tbr.getChildAt(1);
                                    //取得小TableLayout中的小TableRow位置
                                    TableRow tb_tbr_tb_tbr = (TableRow) tb_tbr_tb.getChildAt(0);
                                    //派工資料的迴圈
                                    for (int x = 0; x < 3; x++) {
                                        //小TableRow中的layout_column位置
                                        TextView quotation_txt = (TextView) tb_tbr_tb_tbr.getChildAt(x);
                                        String ResponseText = quotation_txt.getText().toString();
                                        String company = String.valueOf(company_spinner.getSelectedItem());
                                        String quotation_type = String.valueOf(quotation_type_spinner.getSelectedItem());
                                        //將SQL裡的資料傳到MaintainActivity
                                        Bundle bundle = new Bundle();
                                        bundle.putString("ResponseText" + x, ResponseText);
                                        bundle.putString("company", company);
                                        bundle.putString("quotation_type", quotation_type);

                                        intent_quotation.putExtras(bundle);//可放所有基本類別
                                    }
                                    quotation_TableLayout.removeAllViews();
                                    separate_llt.setVisibility(View.GONE);
                                    startActivity(intent_quotation);
                                }
                            }
                        }
                    });

                    //顯示每筆TableLayout的SQL資料
                    //TextView dynamically_txt1;
                    dynamically_txt1 = new TextView(QuotationActivity.this);
                    dynamically_txt1.setText(JArrayList.get(1));
                    dynamically_txt1.setPadding(0, 2, 0, 0);
                    dynamically_txt1.setGravity(Gravity.LEFT);
                    dynamically_txt1.setTextColor(mContext.getResources().getColor(R.color.WQP_White));
                    dynamically_txt1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                    if (dynamically_txt1.getText().toString().equals("報價單號")) {
                        dynamically_txt1.setGravity(Gravity.CENTER_VERTICAL);
                        dynamically_txt1.setPadding(10, 5, 0, 0);
                        dynamically_txt1.setTextColor(mContext.getResources().getColor(R.color.WQP_White));
                        //dynamically_txt1.setBackgroundResource(R.drawable.warningline);
                    }
                    //顯示每筆TableLayout的SQL資料
                    //TextView dynamically_txt2;
                    dynamically_txt2 = new TextView(QuotationActivity.this);
                    dynamically_txt2.setText(JArrayList.get(2));
                    dynamically_txt2.setPadding(0, 2, 0, 0);
                    dynamically_txt2.setGravity(Gravity.LEFT);
                    dynamically_txt2.setTextColor(mContext.getResources().getColor(R.color.WQP_White));
                    dynamically_txt2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                    if (dynamically_txt2.getText().toString().equals("客戶代號")) {
                        dynamically_txt2.setGravity(Gravity.CENTER_VERTICAL);
                        dynamically_txt2.setPadding(30, 5, 0, 0);
                        dynamically_txt2.setTextColor(mContext.getResources().getColor(R.color.WQP_White));
                        //dynamically_txt2.setBackgroundResource(R.drawable.warningline);
                    }
                    //顯示每筆TableLayout的SQL資料
                    //TextView dynamically_txt3;
                    dynamically_txt3 = new TextView(QuotationActivity.this);
                    dynamically_txt3.setText(JArrayList.get(3));
                    dynamically_txt3.setPadding(0, 2, 0, 0);
                    dynamically_txt3.setGravity(Gravity.LEFT);
                    dynamically_txt3.setTextColor(mContext.getResources().getColor(R.color.WQP_White));
                    dynamically_txt3.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                    if (dynamically_txt3.getText().toString().equals("業務姓名")) {
                        dynamically_txt3.setGravity(Gravity.CENTER_VERTICAL);
                        dynamically_txt3.setPadding(18, 5, 0, 0);
                        dynamically_txt3.setTextColor(mContext.getResources().getColor(R.color.WQP_White));
                        //dynamically_txt3.setBackgroundResource(R.drawable.warningline);
                    }
                    TableRow tr1 = new TableRow(QuotationActivity.this);
                    tr1.setGravity(Gravity.CENTER_HORIZONTAL);
                    tr1.addView(dynamically_txt1, new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, type_dip));
                    tr1.addView(dynamically_txt2, new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, type_dip));
                    tr1.addView(dynamically_txt3, new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, type_dip));

                    small_tb.addView(tr1, layoutParams);
                    //about_ImageView寬為0,高為WRAP_CONTENT,權重比為1
                    big_tbr.addView(dynamically_imv[loc], new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
                    //small_tb寬為0,高為WRAP_CONTENT,權重比為3
                    big_tbr.addView(small_tb, new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 2));
                    dynamically_imv[0].setVisibility(View.INVISIBLE);

                    TableRow.LayoutParams the_param3;
                    the_param3 = (TableRow.LayoutParams) small_tb.getLayoutParams();
                    the_param3.span = 2;
                    the_param3.width = TableRow.LayoutParams.MATCH_PARENT;
                    small_tb.setLayoutParams(the_param3);

                    quotation_TableLayout.addView(big_tbr);
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