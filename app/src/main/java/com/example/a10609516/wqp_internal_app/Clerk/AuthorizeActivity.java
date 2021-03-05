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

import com.example.a10609516.wqp_internal_app.Basic.LoginActivity;
import com.example.a10609516.wqp_internal_app.R;
import com.example.a10609516.wqp_internal_app.Tools.WQPClickListener;
import com.example.a10609516.wqp_internal_app.Tools.WQPToolsActivity;

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
        //取得控制項物件
        initViews();
        //Menu的onClickListener
        MenuListener();
        //設置Toolbar
        SetToolBar();
        //取得QuotationActivity傳遞過來的值
        GetResponseData();
        //建立QuotationMaster.php OKHttp連線
        sendRequestWithOkHttpForMaster();
        //建立QuotationMaster.php OKHttp連線
        sendRequestWithOkHttpForDetail();
        //與OkHttp建立連線(QuotationRead.php)
        sendRequestWithOkHttpForQuotationRead();
    }

    /**
     * 取得控制項物件
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

        //核准報價單Button的監聽器
        approved_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TA005.toString().substring(0, 8).equals(user_id_data.toString())) {
                    Toast.makeText(AuthorizeActivity.this, "無權限審核此報價單", Toast.LENGTH_SHORT).show();
                }else {
                    //建立QuotationApproved.php OKHttp連線
                    sendRequestWithOkHttpForApproved();
                    Log.e(LOG,TA005.toString().substring(0, 8));
                    finish();
                }
            }
        });
        //駁回報價單Button的監聽器
        reject_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //建立QuotationReject.php OKHttp連線
                if(TA005.toString().substring(0, 8).equals(user_id_data.toString())) {
                    Toast.makeText(AuthorizeActivity.this, "無權限審核此報價單", Toast.LENGTH_SHORT).show();
                }else {
                    sendRequestWithOkHttpForReject();
                    Log.e(LOG, TA005.toString().substring(0, 8));
                    finish();
                }
            }
        });
        //取消退回報價單Button的監聽器
        cancellation_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //建立QuotationReject.php OKHttp連線
                if(TA005.toString().substring(0, 8).equals(user_id_data.toString())) {
                    Toast.makeText(AuthorizeActivity.this, "無權限審核此報價單", Toast.LENGTH_SHORT).show();
                }else {
                    sendRequestWithOkHttpForReject();
                    Log.e(LOG, TA005.toString().substring(0, 8));
                    finish();
                }
            }
        });
    }

    /**
     * Menu的onClickListener
     */
    private void MenuListener() {
        TextView home_txt = findViewById(R.id.home_txt);
        home_txt.setOnClickListener(new WQPClickListener());
        TextView exchange_txt = findViewById(R.id.exchange_txt);
        exchange_txt.setOnClickListener(new WQPClickListener());
        TextView schedule_txt = findViewById(R.id.schedule_txt);
        schedule_txt.setOnClickListener(new WQPClickListener());
        TextView calendar_txt = findViewById(R.id.calendar_txt);
        calendar_txt.setOnClickListener(new WQPClickListener());
        TextView mission_txt = findViewById(R.id.mission_txt);
        mission_txt.setOnClickListener(new WQPClickListener());
        TextView bonus_txt = findViewById(R.id.bonus_txt);
        bonus_txt.setOnClickListener(new WQPClickListener());
        TextView points_txt = findViewById(R.id.points_txt);
        points_txt.setOnClickListener(new WQPClickListener());
        TextView miss_report_txt = findViewById(R.id.miss_report_txt);
        miss_report_txt.setOnClickListener(new WQPClickListener());
        TextView gps_txt = findViewById(R.id.gps_txt);
        gps_txt.setOnClickListener(new WQPClickListener());
        TextView quotation_txt = findViewById(R.id.quotation_txt);
        quotation_txt.setOnClickListener(new WQPClickListener());
        TextView report_txt = findViewById(R.id.report_txt);
        report_txt.setOnClickListener(new WQPClickListener());
        TextView report_search_txt = findViewById(R.id.report_search_txt);
        report_search_txt.setOnClickListener(new WQPClickListener());
        TextView inventory_txt = findViewById(R.id.inventory_txt);
        inventory_txt.setOnClickListener(new WQPClickListener());
        TextView picking_txt = findViewById(R.id.picking_txt);
        picking_txt.setOnClickListener(new WQPClickListener());
        TextView requisition_txt = findViewById(R.id.requisition_txt);
        requisition_txt.setOnClickListener(new WQPClickListener());
        TextView progress_txt = findViewById(R.id.progress_txt);
        progress_txt.setOnClickListener(new WQPClickListener());
        TextView version_info_txt = findViewById(R.id.version_info_txt);
        version_info_txt.setOnClickListener(new WQPClickListener());
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

        toolbar.setTitle("報價單審核");
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

                finish();
                Intent intent_login = new Intent(AuthorizeActivity.this, LoginActivity.class);
                startActivity(intent_login);
            }
        });
    }

    /**
     * 取得SearchActivity傳遞過來的值
     */
    private void GetResponseData() {
        Bundle bundle = getIntent().getExtras();
        TA002TB002 = bundle.getString("ResponseText" + 0);
        ResponseText1 = bundle.getString("ResponseText" + 1);
        ResponseText2 = bundle.getString("ResponseText" + 2);
        company = bundle.getString("company");
        quotation_type = bundle.getString("quotation_type");

        if (company.toString().equals("拓霖")) {
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
     * 與OkHttp建立連線(已讀報價單)
     */
    private void sendRequestWithOkHttpForQuotationRead() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //接收LoginActivity傳過來的值
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
     * 與OkHttp建立連線(報價單單頭)
     */
    private void sendRequestWithOkHttpForMaster() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //接收LoginActivity傳過來的值
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
     * 獲得JSON字串並解析成String字串(報價單單頭)
     *
     * @param jsonData
     */
    private void parseJSONWithJSONObjectForMaster(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON格式改為字串
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                COMPANY = jsonObject.getString("COMPANY");
                //TA001 = jsonObject.getString("單別");
                TA001TA002 = jsonObject.getString("NUMBER");
                TA005 = jsonObject.getString("CLERK");
                //TA004 = jsonObject.getString("客戶代號");
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
                    COMPANY = "拓霖";
                } else {
                    COMPANY = "拓亞鈦";
                }
                //JSONArray加入SearchData資料
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

                //HandlerMessage更新UI
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
     * 更新UI(QuotationMaster)
     */
    Handler mHandlerForMaster = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //設定報價單狀態的樣式
            //核准、退回、取消確認Button的設定
            mode_txt.setText(PROCESS);
            if (PROCESS.toString().equals("送審中")) {
                mode_txt.setTextColor(Color.rgb(255, 165, 0));
            } else if (PROCESS.toString().equals("報價完成")) {
                mode_txt.setTextColor(Color.rgb(34, 195, 46));
                cancellation_llt.setVisibility(View.VISIBLE);
                yes_no_llt.setVisibility(View.GONE);
            } else {
                mode_txt.setTextColor(Color.rgb(220, 20, 60));
                cancellation_llt.setVisibility(View.GONE);
                yes_no_llt.setVisibility(View.GONE);
                separate_llt.setVisibility(View.GONE);
            }

            //單頭的Title
            final String[] title_array = {"公司別"/*, "單別"*/, "單號", "業務姓名", /*"客戶代號",*/
                    "客戶全名", "報價金額", "付款方式", "付款條件", "價格條件"
                    , "備註一", "備註二", "訂金"};
            switch (msg.what) {
                case 1:
                    //最外層有一個大的TableLayout,再設置TableRow包住小的TableLayout
                    //平均分配列的寬度
                    quotation_master_tb.setStretchAllColumns(true);
                    //設置大TableLayout的TableRow
                    TableRow big_tbr = new TableRow(AuthorizeActivity.this);
                    //設置每筆資料的小TableLayout
                    TableLayout small_tb = new TableLayout(AuthorizeActivity.this);
                    //全部列自動填充空白處
                    small_tb.setStretchAllColumns(true);
                    //small_tb.setBackgroundResource(R.drawable.bg_layered_green);

                    Bundle jb = msg.getData();
                    ArrayList<String> JArrayList = new ArrayList<String>();
                    //int i = b.getStringArrayList("JSON_data").size();
                    JArrayList = jb.getStringArrayList("JSON_data");

                    for (int i = 0; i < jb.getStringArrayList("JSON_data").size(); i++) {
                        //顯示每筆TableLayout的Title
                        //TextView dynamically_title;
                        dynamically_master_title = new TextView(AuthorizeActivity.this);
                        dynamically_master_title.setText(title_array[i].toString());
                        dynamically_master_title.setPadding(20, 5, 0, 2);
                        dynamically_master_title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                        dynamically_master_title.setGravity(Gravity.CENTER_VERTICAL);
                        dynamically_master_title.setTextColor(mContext.getResources().getColor(R.color.WQP_White));

                        //顯示每筆TableLayout的SQL資料
                        //TextView dynamically_txt;
                        dynamically_master_txt = new TextView(AuthorizeActivity.this);
                        dynamically_master_txt.setText(JArrayList.get(i));
                        dynamically_master_txt.setPadding(10, 5, 0, 2);
                        dynamically_master_txt.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                        dynamically_master_txt.setGravity(Gravity.CENTER_VERTICAL);
                        dynamically_master_txt.setTextColor(mContext.getResources().getColor(R.color.WQP_White));

                        //設置大TableLayout的TableRow
                        TableRow tr1 = new TableRow(AuthorizeActivity.this);
                        tr1.setWeightSum(2);
                        tr1.addView(dynamically_master_title, new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 0));
                        tr1.addView(dynamically_master_txt, new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 2));

                        small_tb.addView(tr1);
                    }
                    if (PERCENTAGE.toString().equals("")) {
                        small_tb.getChildAt(10).setVisibility(View.GONE);
                    }
                    //設置報價金額的顯示方式
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
     * 與OkHttp建立連線(報價單單身)
     */
    private void sendRequestWithOkHttpForDetail() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //接收LoginActivity傳過來的值
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
     * 獲得JSON字串並解析成String字串(報價單單身)
     *
     * @param jsonData
     */
    private void parseJSONWithJSONObjectForDetail(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON格式改為字串
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                TB003 = jsonObject.getString("ITEMS");
                TB004TB005 = jsonObject.getString("TB004TB005");
                TB007TB008 = jsonObject.getString("QUANTITY");
                TB009 = jsonObject.getString("PRICE");
                TB010 = jsonObject.getString("TOTAL_MONEY");
                TB025TB024 = jsonObject.getString("Q_MONEY");

                //JSONArray加入SearchData資料
                ArrayList<String> JArrayList = new ArrayList<String>();
                JArrayList.add(TB003);
                JArrayList.add(TB004TB005);
                JArrayList.add(TB007TB008);
                JArrayList.add(TB009);
                JArrayList.add(TB010);
                JArrayList.add(TB025TB024);

                Log.e(LOG, TB025TB024);

                //HandlerMessage更新UI
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
     * 更新UI(QuotationDetail)
     */
    Handler mHandlerForDetail = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    //製作Dip
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
                    dynamically_product_txt.setText("品號:"+JArrayList.get(1));
                    dynamically_product_txt.setTextColor(mContext.getResources().getColor(R.color.WQP_White));
                    dynamically_product_txt.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                    dynamically_product_txt.setGravity(Gravity.CENTER);
                    dynamically_product_txt.setPadding(10, 2, 0, 0);
                    product_llt.addView(dynamically_product_txt, LinearLayout.LayoutParams.MATCH_PARENT, product_dip);

                    dynamically_count_txt = new TextView(AuthorizeActivity.this);
                    dynamically_count_txt.setText("數量: "+JArrayList.get(2));
                    dynamically_count_txt.setTextColor(mContext.getResources().getColor(R.color.WQP_White));
                    dynamically_count_txt.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                    dynamically_count_txt.setGravity(Gravity.LEFT);
                    dynamically_count_txt.setPadding(10, 2, 0, 0);
                    count_llt.addView(dynamically_count_txt, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    dynamically_price_txt = new TextView(AuthorizeActivity.this);
                    dynamically_price_txt.setText("單價: $"+JArrayList.get(3));
                    dynamically_price_txt.setTextColor(mContext.getResources().getColor(R.color.WQP_White));
                    dynamically_price_txt.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                    dynamically_price_txt.setGravity(Gravity.LEFT);
                    dynamically_price_txt.setPadding(10, 2, 0, 0);
                    price_llt.addView(dynamically_price_txt, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    dynamically_total_txt = new TextView(AuthorizeActivity.this);
                    dynamically_total_txt.setText("金額: $"+JArrayList.get(4));
                    dynamically_total_txt.setTextColor(mContext.getResources().getColor(R.color.WQP_White));
                    dynamically_total_txt.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                    dynamically_total_txt.setGravity(Gravity.LEFT);
                    dynamically_total_txt.setPadding(10, 2, 0, 0);
                    total_llt.addView(dynamically_total_txt, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    dynamically_money_txt = new TextView(AuthorizeActivity.this);
                    dynamically_money_txt.setText("報價單顯示金額: $"+JArrayList.get(5));
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
     * 與OkHttp建立連線(報價單核准)
     */
    private void sendRequestWithOkHttpForApproved() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //接收LoginActivity傳過來的值
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
     * 與OkHttp建立連線(報價單退回)
     */
    private void sendRequestWithOkHttpForReject() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //接收LoginActivity傳過來的值
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