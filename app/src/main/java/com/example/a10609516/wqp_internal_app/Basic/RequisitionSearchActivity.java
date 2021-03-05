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
    private String[] category_type = new String[]{"全部", "資訊需求單", "行銷需求單"};

    private String s_department, department, mode;

    private Context mContext = this;

    private String LOG = "RequisitionSearchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requisition_search);
        //取得控制項物件
        initViews();
        //Menu的onClickListener
        MenuListener();
        //設置Toolbar
        SetToolBar();
        //需求類別Spinner
        Category_Spinner();
    }

    /**
     * 取得控制項物件
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
                //與OkHttp建立連線 取得需求單資訊
                sendRequestWithOkHttpForRequisitionSearch();
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

        toolbar.setTitle("行程資訊");
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
                Intent intent_login = new Intent(RequisitionSearchActivity.this, LoginActivity.class);
                startActivity(intent_login);
            }
        });
    }

    /**
     * 需求類別Spinner
     */
    private void Category_Spinner() {
        category_listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, category_type);
        category_listAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category_spinner.setAdapter(category_listAdapter);
    }

    /**
     * 實現畫面置頂按鈕
     *
     * @param view
     */
    public void GoTopBtn(View view) {
        Handler mHandler = new Handler();
        mHandler.post(new Runnable() {
            public void run() {
                //實現畫面置頂按鈕
                search_scv.fullScroll(ScrollView.FOCUS_UP);
            }
        });
    }

    /**
     * 實現畫面置底按鈕
     *
     * @param view
     */
    public void GoDownBtn(View view) {
        Handler mHandler = new Handler();
        mHandler.post(new Runnable() {
            public void run() {
                //實現畫面置底按鈕
                search_scv.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    /**
     * 與OkHttp建立連線 取得需求單資訊
     */
    private void sendRequestWithOkHttpForRequisitionSearch() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                String user_name = name_edt.getText().toString();
                //獲取日期挑選器的數據
                String time_start = start_btn.getText().toString();
                String time_end = end_btn.getText().toString();
                String category = String.valueOf(category_spinner.getSelectedItem());

                if (category.equals("全部")) {
                    category = "";
                } else if (category.equals("資訊需求單")) {
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
     * 獲得JSON字串並解析成String字串
     *
     * @param jsonData
     */
    private void parseJSONWithJSONObjectForRequisitionSearch(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON格式改為字串
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

                //JSONArray加入SearchData資料
                ArrayList<String> JArrayList = new ArrayList<String>();
                JArrayList.add(type);
                JArrayList.add(requester_name);
                JArrayList.add(requester_dept);
                JArrayList.add(demand);
                JArrayList.add(status);
                JArrayList.add(remark);

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
                        department = "資訊需求單";
                    } else {
                        department = "行銷需求單";
                    }

                    //顯示每筆LinearLayout的需求單類型
                    TextView dynamically_department;
                    dynamically_department = new TextView(RequisitionSearchActivity.this);
                    dynamically_department.setText(department);
                    dynamically_department.setGravity(Gravity.CENTER);
                    //dynamically_department.setWidth(50);
                    dynamically_department.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                    dynamically_department.setTextColor(mContext.getResources().getColor(R.color.WQP_White));

                    if (JArrayList.get(4).equals("1")){
                        mode = "申請中";
                    } else if (JArrayList.get(4).equals("2")){
                        mode = "已完成";
                    } else {
                        mode = "作廢";
                    }

                    //顯示每筆LinearLayout的審核狀態
                    TextView dynamically_mode;
                    dynamically_mode = new TextView(RequisitionSearchActivity.this);
                    dynamically_mode.setText(mode);
                    dynamically_mode.setGravity(Gravity.CENTER);
                    dynamically_mode.setPadding(0, 10, 0, 10);
                    dynamically_mode.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);

                    if (mode.equals("申請中")) {
                        dynamically_mode.setTextColor(Color.rgb(255, 165, 0));
                    } else if (mode.equals("已完成")) {
                        dynamically_mode.setTextColor(Color.rgb(34, 195, 46));
                    } else {
                        dynamically_mode.setTextColor(Color.rgb(220, 20, 60));
                    }

                    //顯示每筆LinearLayout的申請人員部門
                    TextView dynamically_division;
                    dynamically_division = new TextView(RequisitionSearchActivity.this);
                    dynamically_division.setText(JArrayList.get(2));
                    dynamically_division.setGravity(Gravity.CENTER);
                    dynamically_division.setPadding(0, 10, 0, 10);
                    dynamically_division.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                    dynamically_division.setTextColor(mContext.getResources().getColor(R.color.WQP_White));

                    //顯示每筆LinearLayout的申請人員
                    TextView dynamically_user;
                    dynamically_user = new TextView(RequisitionSearchActivity.this);
                    dynamically_user.setText(JArrayList.get(1));
                    dynamically_user.setGravity(Gravity.CENTER);
                    //dynamically_user.setWidth(50);
                    dynamically_user.setPadding(0, 10, 0, 10);
                    dynamically_user.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                    dynamically_user.setTextColor(mContext.getResources().getColor(R.color.WQP_White));

                    //顯示每筆LinearLayout的需求內容
                    TextView dynamically_require;
                    dynamically_require = new TextView(RequisitionSearchActivity.this);
                    dynamically_require.setText(JArrayList.get(3));
                    dynamically_require.setGravity(Gravity.CENTER);
                    //dynamically_require.setWidth(50);
                    dynamically_require.setPadding(0, 10, 0, 10);
                    dynamically_require.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                    dynamically_require.setTextColor(mContext.getResources().getColor(R.color.WQP_White));

                    //顯示每筆LinearLayout的需求備註
                    TextView dynamically_remark;
                    dynamically_remark = new TextView(RequisitionSearchActivity.this);
                    dynamically_remark.setText(JArrayList.get(5));
                    dynamically_remark.setGravity(Gravity.CENTER);
                    //dynamically_remark.setWidth(50);
                    dynamically_remark.setPadding(5, 10, 5, 10);
                    dynamically_remark.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                    dynamically_remark.setTextColor(mContext.getResources().getColor(R.color.WQP_White));

                    //設置每筆TableLayout的分隔線
                    TextView dynamically_txt = new TextView(RequisitionSearchActivity.this);
                    dynamically_txt.setBackgroundColor(Color.rgb(220, 220, 220));

                    //設置每筆TableLayout的分隔線
                    TextView dynamically_txt1 = new TextView(RequisitionSearchActivity.this);
                    dynamically_txt1.setBackgroundColor(Color.rgb(224, 255, 255));
                    //設置每筆TableLayout的分隔線
                    TextView dynamically_txt2 = new TextView(RequisitionSearchActivity.this);
                    dynamically_txt2.setBackgroundColor(Color.rgb(224, 255, 255));
                    //設置每筆TableLayout的分隔線
                    TextView dynamically_txt3 = new TextView(RequisitionSearchActivity.this);
                    dynamically_txt3.setBackgroundColor(Color.rgb(224, 255, 255));
                    //設置每筆TableLayout的分隔線
                    TextView dynamically_txt4 = new TextView(RequisitionSearchActivity.this);
                    dynamically_txt4.setBackgroundColor(Color.rgb(224, 255, 255));
                    //設置每筆TableLayout的分隔線
                    TextView dynamically_txt5 = new TextView(RequisitionSearchActivity.this);
                    dynamically_txt5.setBackgroundColor(Color.rgb(224, 255, 255));
                    //設置每筆TableLayout的分隔線
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