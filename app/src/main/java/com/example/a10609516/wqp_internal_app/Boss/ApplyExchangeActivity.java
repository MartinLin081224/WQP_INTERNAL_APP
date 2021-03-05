package com.example.a10609516.wqp_internal_app.Boss;

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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApplyExchangeActivity extends WQPToolsActivity {

    private SharedPreferences sp;

    private LinearLayout nav_view;
    private DrawerLayout drawer;
    private Toolbar toolbar;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayout apply_llt;

    private Button[] dynamically_btn, dynamically_id_btn;

    private String exchange;
    private String LOG = "ApplyExchangeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_exchange);
        //取得控制項物件
        initViews();
        //Menu的onClickListener
        MenuListener();
        //設置Toolbar
        SetToolBar();
        //與OkHttp建立連線(t查詢換貨申請單明細)
        sendRequestWithOkHttpForApplyExchange();
    }

    /**
     * 取得控制項物件
     */
    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        nav_view = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer_layout);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        apply_llt = (LinearLayout) findViewById(R.id.apply_llt);

        //UI介面下拉刷新
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(false);
                apply_llt.removeAllViews();
                //與OkHttp建立連線(查詢換貨申請單明細)
                sendRequestWithOkHttpForApplyExchange();
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

        toolbar.setTitle("換貨申請單");
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

                finish();
                Intent intent_login = new Intent(ApplyExchangeActivity.this, LoginActivity.class);
                startActivity(intent_login);
            }
        });
    }

    /**
     * 與OkHttp建立連線(t查詢換貨申請單明細)
     */
    private void sendRequestWithOkHttpForApplyExchange() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //接收LoginActivity傳過來的值
                SharedPreferences user_id = getSharedPreferences("user_id", MODE_PRIVATE);
                String user_id_data = user_id.getString("ID", "");
                Log.i(LOG, user_id_data);

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("U_ACC", user_id_data)
                            .build();
                    Log.e(LOG, user_id_data);
                    Request request = new Request.Builder()
                            //.url("http://192.168.0.172/WQP_OS/ApplyExchangeSearch.php")
                            .url("http://a.wqp-water.com.tw/WQP_OS/ApplyExchangeSearch.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, requestBody.toString());
                    Log.e(LOG, response.toString());
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectForApplyExchange(responseData);
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
    private void parseJSONWithJSONObjectForApplyExchange(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            dynamically_btn = new Button[jsonArray.length()];
            dynamically_id_btn = new Button[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON格式改為字串
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String EXID = jsonObject.getString("EXID");
                String COMPANY = jsonObject.getString("COMPANY");
                String CREATOR = jsonObject.getString("CREATOR");
                String CUST = jsonObject.getString("CUST");
                /*String FILTER = jsonObject.getString("FILTER");
                String DEVICE = jsonObject.getString("DEVICE");*/

                //JSONArray加入SearchData資料
                ArrayList<String> JArrayList = new ArrayList<String>();
                JArrayList.add(EXID);
                JArrayList.add(COMPANY);
                JArrayList.add(CREATOR);
                JArrayList.add(CUST);
                /*JArrayList.add(FILTER);
                JArrayList.add(DEVICE);*/

                Log.e(LOG, EXID);
                Log.e(LOG, COMPANY);
                Log.e(LOG, CREATOR);
                Log.e(LOG, CUST);
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
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    LinearLayout small_llt_s = new LinearLayout(ApplyExchangeActivity.this);
                    small_llt_s.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout small_llt0 = new LinearLayout(ApplyExchangeActivity.this);
                    small_llt0.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout small_llt00 = new LinearLayout(ApplyExchangeActivity.this);
                    small_llt00.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout small_llt1 = new LinearLayout(ApplyExchangeActivity.this);
                    small_llt1.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout small_llt2 = new LinearLayout(ApplyExchangeActivity.this);
                    small_llt2.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout small_llt3 = new LinearLayout(ApplyExchangeActivity.this);
                    small_llt3.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout small_llt4 = new LinearLayout(ApplyExchangeActivity.this);
                    small_llt4.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout small_llt5 = new LinearLayout(ApplyExchangeActivity.this);
                    small_llt5.setOrientation(LinearLayout.HORIZONTAL);
                    small_llt5.setGravity(Gravity.CENTER_HORIZONTAL);
                    LinearLayout big_llt1 = new LinearLayout(ApplyExchangeActivity.this);
                    big_llt1.setOrientation(LinearLayout.VERTICAL);
                    big_llt1.setGravity(Gravity.CENTER_VERTICAL);
                    LinearLayout big_llt2 = new LinearLayout(ApplyExchangeActivity.this);
                    big_llt2.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout large_llt = new LinearLayout(ApplyExchangeActivity.this);
                    large_llt.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout XL_llt = new LinearLayout(ApplyExchangeActivity.this);
                    XL_llt.setOrientation(LinearLayout.VERTICAL);
                    RelativeLayout id_rlt = new RelativeLayout(ApplyExchangeActivity.this);

                    Bundle jb = msg.getData();
                    ArrayList<String> JArrayList = new ArrayList<String>();
                    //int i = b.getStringArrayList("JSON_data").size();
                    JArrayList = jb.getStringArrayList("JSON_data");

                    //顯示每筆LinearLayout的ID
                    TextView dynamically_id;
                    dynamically_id = new TextView(ApplyExchangeActivity.this);
                    dynamically_id.setText(JArrayList.get(0).trim());
                    dynamically_id.setPadding(10, 0, 10, 3);
                    dynamically_id.setGravity(Gravity.LEFT);
                    //dynamically_id.setWidth(50);
                    dynamically_id.setVisibility(View.GONE);
                    dynamically_id.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                    dynamically_id.setTextColor(Color.rgb(105, 105, 105));

                   /* if ((JArrayList.get(4).trim().equals("Y")) && (JArrayList.get(5).trim().equals("N"))) {
                        exchange = "更換濾芯";
                    } else if ((JArrayList.get(4).trim().equals("N")) && (JArrayList.get(5).trim().equals("Y"))){
                        exchange = "更換設備";
                    } else if ((JArrayList.get(4).trim().equals("Y")) && (JArrayList.get(5).trim().equals("Y"))){
                        exchange = "更換設備、濾芯";
                    }*/

                    //顯示每筆LinearLayout的更換種類
                    TextView dynamically_exchange;
                    dynamically_exchange = new TextView(ApplyExchangeActivity.this);
                    dynamically_exchange.setText("換貨申請單");
                    dynamically_exchange.setPadding(10, 10, 10, 3);
                    dynamically_exchange.setGravity(Gravity.LEFT);
                    dynamically_exchange.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                    dynamically_exchange.setTextColor(Color.rgb(105, 105, 105));

                    //顯示每筆LinearLayout的客戶姓名
                    TextView dynamically_name;
                    dynamically_name = new TextView(ApplyExchangeActivity.this);
                    dynamically_name.setText("客戶姓名 : " + JArrayList.get(3).trim());
                    dynamically_name.setPadding(10, 0, 10, 3);
                    dynamically_name.setGravity(Gravity.LEFT);
                    //dynamically_name.setWidth(50);
                    dynamically_name.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                    dynamically_name.setTextColor(Color.rgb(105, 105, 105));

                    //顯示每筆LinearLayout的申請部門/人員
                    TextView dynamically_creator;
                    dynamically_creator = new TextView(ApplyExchangeActivity.this);
                    dynamically_creator.setText("申請部門/人員 : " + JArrayList.get(2).trim());
                    dynamically_creator.setPadding(10, 0, 10, 10);
                    dynamically_creator.setGravity(Gravity.LEFT);
                    dynamically_creator.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                    dynamically_creator.setTextColor(Color.rgb(105, 105, 105));

                    //設置每筆LinearLayout的間隔分隔線
                    TextView dynamically_txt0 = new TextView(ApplyExchangeActivity.this);
                    dynamically_txt0.setBackgroundColor(Color.rgb(220, 220, 220));
                    dynamically_txt0.setAlpha(0);

                    //設置每筆LinearLayout的間隔分隔線
                    TextView dynamically_txt1 = new TextView(ApplyExchangeActivity.this);
                    dynamically_txt1.setBackgroundColor(Color.rgb(220, 220, 220));
                    dynamically_txt1.setAlpha(0);

                    //設置每筆large_llt的分隔線
                    TextView dynamically_txt_s = new TextView(ApplyExchangeActivity.this);
                    dynamically_txt_s.setBackgroundColor(Color.rgb(220, 220, 220));
                    dynamically_txt_s.setAlpha(0);

                    //設置每筆TableLayout的Button監聽器、與動態新增Button的ID
                    int loc = 0;
                    for (int i = 0; i < dynamically_btn.length; i++) {
                        if (dynamically_btn[i] == null) {
                            loc = i;
                            break;
                        }
                    }
                    dynamically_btn[loc] = new Button(ApplyExchangeActivity.this);
                    dynamically_btn[loc].setBackgroundResource(R.drawable.icon_exchange);
                    //dynamically_btn[loc].setText("Google Map");
                    dynamically_btn[loc].setPadding(10, 0, 10, 20);
                    //dynamically_btn[loc].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                    //dynamically_btn[loc].setTextColor(Color.rgb(6, 102, 219));
                    dynamically_btn[loc].setId(loc);
                    dynamically_btn[loc].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for (int a = 0; a < dynamically_btn.length; a++) {
                                if (v.getId() == dynamically_btn[a].getId()) {
                                    Intent intent_ex = new Intent(ApplyExchangeActivity.this, ExchangeActivity.class);
                                    LinearLayout id_llt = (LinearLayout) apply_llt.getChildAt(a);
                                    for (int x = 0; x < 4; x++) {
                                        LinearLayout big_llt = (LinearLayout) id_llt.getChildAt(1);
                                        LinearLayout right_llt = (LinearLayout) big_llt.getChildAt(1);
                                        LinearLayout exchange_llt = (LinearLayout) right_llt.getChildAt(x);
                                        TextView id_txt = (TextView) exchange_llt.getChildAt(0);
                                        String id = id_txt.getText().toString();
                                        //將SQL裡的資料傳到MapsActivity
                                        Bundle bundle = new Bundle();
                                        bundle.putString("exchange_id" + x, id);
                                        //intent_gps.putExtra("TitleText", TitleText);//可放所有基本類別
                                        intent_ex.putExtras(bundle);//可放所有基本類別
                                    }
                                    startActivity(intent_ex);
                                    //進入MapsActivity後 清空gps_llt的資料
                                    apply_llt.removeAllViews();
                                }
                            }
                        }
                    });

                    //設置每筆TableLayout的Button監聽器、與動態新增Button的ID
                    int loc_id = 0;
                    for (int i = 0; i < dynamically_btn.length; i++) {
                        if (dynamically_btn[i] == null) {
                            loc_id = i;
                            break;
                        }
                    }
                    dynamically_id_btn[loc_id] = new Button(ApplyExchangeActivity.this);
                    //dynamically_btn[loc].setText("Google Map");
                    dynamically_id_btn[loc_id].setAlpha(0);
                    dynamically_id_btn[loc_id].setId(loc);
                    dynamically_id_btn[loc_id].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for (int a = 0; a < dynamically_btn.length; a++) {
                                if (v.getId() == dynamically_btn[a].getId()) {
                                    Intent intent_ex = new Intent(ApplyExchangeActivity.this, ExchangeActivity.class);
                                    LinearLayout id_llt = (LinearLayout) apply_llt.getChildAt(a);

                                    RelativeLayout r_big_llt = (RelativeLayout) id_llt.getChildAt(1);
                                    for (int x = 0; x < 4; x++) {
                                        LinearLayout big_llt = (LinearLayout) r_big_llt.getChildAt(0);
                                        LinearLayout right_llt = (LinearLayout) big_llt.getChildAt(1);
                                        LinearLayout exchange_llt = (LinearLayout) right_llt.getChildAt(x);
                                        TextView id_txt = (TextView) exchange_llt.getChildAt(0);
                                        String id = id_txt.getText().toString();
                                        //將SQL裡的資料傳到MapsActivity
                                        Bundle bundle = new Bundle();
                                        bundle.putString("exchange_id" + x, id);
                                        //intent_gps.putExtra("TitleText", TitleText);//可放所有基本類別
                                        intent_ex.putExtras(bundle);//可放所有基本類別
                                    }

                                    startActivity(intent_ex);
                                    //進入MapsActivity後 清空gps_llt的資料
                                    apply_llt.removeAllViews();
                                }
                            }
                        }
                    });

                    Log.e(LOG, dynamically_id.getText().toString());
                    Log.e(LOG, dynamically_name.getText().toString());
                    Log.e(LOG, dynamically_exchange.getText().toString());
                    Log.e(LOG, dynamically_creator.getText().toString());

                    LinearLayout.LayoutParams small_pm = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                    LinearLayout.LayoutParams btn_pm = new LinearLayout.LayoutParams(120, 120);
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                    small_llt0.addView(dynamically_txt0, LinearLayout.LayoutParams.MATCH_PARENT, 3);
                    small_llt1.addView(dynamically_id, small_pm);
                    small_llt2.addView(dynamically_exchange, small_pm);
                    small_llt3.addView(dynamically_name, small_pm);
                    small_llt4.addView(dynamically_creator, small_pm);
                    small_llt00.addView(dynamically_txt1, LinearLayout.LayoutParams.MATCH_PARENT, 3);
                    small_llt5.addView(dynamically_btn[loc], btn_pm);

                    big_llt1.addView(small_llt5);

                    big_llt2.addView(small_llt0);
                    big_llt2.addView(small_llt1);
                    big_llt2.addView(small_llt2);
                    big_llt2.addView(small_llt3);
                    big_llt2.addView(small_llt4);
                    big_llt2.addView(small_llt00);
                    //big_llt2.addView(small_llt5);

                    large_llt.setBackgroundResource(R.drawable.frame_empty_line);
                    large_llt.addView(big_llt1, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1));
                    large_llt.addView(big_llt2, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 3));

                    small_llt_s.addView(dynamically_txt_s, LinearLayout.LayoutParams.MATCH_PARENT, 10);

                    id_rlt.addView(large_llt, params);
                    id_rlt.addView(dynamically_id_btn[loc_id], params);

                    XL_llt.addView(small_llt_s);
                    XL_llt.addView(id_rlt);

                    apply_llt.addView(XL_llt);
                    LinearLayout first_llt = (LinearLayout) apply_llt.getChildAt(0);
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
        Log.d(LOG, "onRestart");
        apply_llt.removeAllViews();
        //與OkHttp建立連線(t查詢換貨申請單明細)
        sendRequestWithOkHttpForApplyExchange();
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