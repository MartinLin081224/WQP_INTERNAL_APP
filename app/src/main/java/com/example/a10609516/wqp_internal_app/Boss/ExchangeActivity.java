package com.example.a10609516.wqp_internal_app.Boss;

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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class ExchangeActivity extends WQPToolsActivity {

    private SharedPreferences sp;

    private LinearLayout nav_view;
    private DrawerLayout drawer;
    private Toolbar toolbar;

    private LinearLayout exchange_detail_llt, yes_no_llt, reason_llt, suggest_llt;
    private TextView mode_txt, date_txt, client_txt, address_txt, applicant_txt, remark_txt;
    private EditText reason_edt, suggest_reason_edt;
    private CheckBox report_checkBox, warranty_checkBox, other_checkBox;
    private ImageView usual_imv;
    private Button approved_button, reject_button, reject_reason_button, cancel_button, suggest_reason_button, suggest_cancel_button;

    private String exchange_id, exchange, exchange_mode, gift, warranty, reason, reject_reason;
    private String LOG = "ExchangeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);
        //取得控制項物件
        initViews();
        //Menu的onClickListener
        MenuListener();
        //設置Toolbar
        SetToolBar();
        //取得ApplyExchangeActivity傳遞過來的值
        GetResponseData();
        //與OkHttp建立連線(換貨申請單單頭)
        sendRequestWithOkHttpForMaster();
        //與OkHttp建立連線(換貨申請單單身)
        sendRequestWithOkHttpForDetail();
        reason_llt.setVisibility(View.GONE);
        suggest_llt.setVisibility(View.GONE);
    }

    /**
     * 取得控制項物件
     */
    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        nav_view = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer_layout);
        exchange_detail_llt = (LinearLayout) findViewById(R.id.exchange_detail_llt);
        yes_no_llt = (LinearLayout) findViewById(R.id.yes_no_llt);
        reason_llt = (LinearLayout) findViewById(R.id.reason_llt);
        suggest_llt = (LinearLayout) findViewById(R.id.suggest_llt);
        mode_txt = (TextView) findViewById(R.id.mode_txt);
        date_txt = (TextView) findViewById(R.id.date_txt);
        client_txt = (TextView) findViewById(R.id.client_txt);
        address_txt = (TextView) findViewById(R.id.address_txt);
        applicant_txt = (TextView) findViewById(R.id.applicant_txt);
        remark_txt = (TextView) findViewById(R.id.remark_txt);
        reason_edt = (EditText) findViewById(R.id.reason_edt);
        suggest_reason_edt = (EditText) findViewById(R.id.suggest_reason_edt);
        usual_imv = (ImageView) findViewById(R.id.usual_imv);
        report_checkBox = (CheckBox) findViewById(R.id.report_checkBox);
        warranty_checkBox = (CheckBox) findViewById(R.id.warranty_checkBox);
        other_checkBox = (CheckBox) findViewById(R.id.other_checkBox);
        approved_button = (Button) findViewById(R.id.approved_button);
        reject_button = (Button) findViewById(R.id.reject_button);
        reject_reason_button = (Button) findViewById(R.id.reject_reason_button);
        cancel_button = (Button) findViewById(R.id.cancel_button);
        suggest_reason_button = (Button) findViewById(R.id.suggest_reason_button);
        suggest_cancel_button = (Button) findViewById(R.id.suggest_cancel_button);

        /*//跳到常用詞語介面
        usual_imv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExchangeActivity.this, UsualActivity.class);
                startActivity(intent);
            }
        });*/

        //核准換貨申請單Button的監聽器
        approved_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode_txt.getText().toString().equals("送審中")) {
                    suggest_llt.setVisibility(View.VISIBLE);
                    yes_no_llt.setVisibility(View.GONE);
                    approved_button.setClickable(false);
                    reject_button.setClickable(false);
                    suggest_reason_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //與OkHttp建立連線(換貨申請單核准)
                            sendRequestWithOkHttpForApproved();
                            mode_txt.setText("");
                            try{
                                // delay 1 second
                                Thread.sleep(500);
                                //與OkHttp建立連線(換貨申請單單頭)
                                sendRequestWithOkHttpForMaster();
                                approved_button.setVisibility(View.GONE);
                                finish();
                            } catch(InterruptedException e){
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    Toast.makeText(ExchangeActivity.this, "此換貨申請單未重新送審", Toast.LENGTH_SHORT).show();
                }
            }
        });

        suggest_cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suggest_llt.setVisibility(View.GONE);
                yes_no_llt.setVisibility(View.VISIBLE);
                approved_button.setClickable(true);
                reject_button.setClickable(true);
                suggest_reason_edt.setText("");
            }
        });

        //駁回換貨申請單Button的監聽器
        reject_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode_txt.getText().toString().equals("送審中") || mode_txt.getText().toString().equals("核准") ) {
                    reason_llt.setVisibility(View.VISIBLE);
                    yes_no_llt.setVisibility(View.GONE);
                    approved_button.setClickable(false);
                    reject_button.setClickable(false);
                    reject_reason_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //與OkHttp建立連線(換貨申請單退回)
                            sendRequestWithOkHttpForReject();
                            mode_txt.setText("");
                            try{
                                // delay 1 second
                                Thread.sleep(500);
                                //與OkHttp建立連線(換貨申請單單頭)
                                sendRequestWithOkHttpForMaster();
                                reason_llt.setVisibility(View.GONE);
                                yes_no_llt.setVisibility(View.VISIBLE);
                                approved_button.setClickable(true);
                                reject_button.setClickable(true);
                                reason_edt.setText("");
                                finish();
                            } catch(InterruptedException e){
                                e.printStackTrace();
                            }
                        }
                    });

                    /*AlertDialog.Builder builder = new AlertDialog.Builder(ExchangeActivity.this);
                    //builder.setMessage("駁回原因");

                    LayoutInflater inflater = ExchangeActivity.this.getLayoutInflater();
                    builder.setView(inflater.inflate(R.layout.layout_reject_reason, null));

                    reason_edt = inflater.inflate(R.layout.layout_reject_reason, null).findViewById(R.id.reason_edt);
                    reason_edt = (EditText) findViewById(R.id.reason_edt);

                    builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();*/
                }
            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reason_llt.setVisibility(View.GONE);
                yes_no_llt.setVisibility(View.VISIBLE);
                approved_button.setClickable(true);
                reject_button.setClickable(true);
                reason_edt.setText("");
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
                Intent intent_login = new Intent(ExchangeActivity.this, LoginActivity.class);
                startActivity(intent_login);
            }
        });
    }

    /**
     * 取得ApplyExchangeActivity傳遞過來的值
     */
    private void GetResponseData() {
        Bundle bundle = getIntent().getExtras();
        exchange_id = bundle.getString("exchange_id"+ 1).trim();
        Log.e(LOG , exchange_id);
    }

    /**
     * 取得UsualActivity傳遞過來的值
     */
    private void GetResponseData_Usual() {
        Bundle bundle = getIntent().getExtras();
        reason = bundle.getString("reason_txt").trim();
        Log.e(LOG, reason);
    }

    /**
     * 設置是否已收款的三個CheckBox只能一個被勾選
     *
     * @param view
     */
    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();
        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.report_checkBox:
                if (checked) {
                    warranty_checkBox.setChecked(false);
                    other_checkBox.setChecked(false);
                    reason_edt.setEnabled(false);
                }
                break;
            case R.id.warranty_checkBox:
                if (checked) {
                    report_checkBox.setChecked(false);
                    other_checkBox.setChecked(false);
                    reason_edt.setEnabled(true);
                }
                break;
            case R.id.other_checkBox:
                if (checked) {
                    report_checkBox.setChecked(false);
                    warranty_checkBox.setChecked(false);
                    reason_edt.setEnabled(true);
                }
                break;
        }
    }

    /**
     * 與OkHttp建立連線(換貨申請單單頭)
     */
    private void sendRequestWithOkHttpForMaster() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                Log.e(LOG , exchange_id);

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("EXID", exchange_id)
                            .build();
                    Request request = new Request.Builder()
                            .url("http://a.wqp-water.com.tw/WQP_OS/ApplyExchangeMaster.php")
                            //.url("http://192.168.0.172/WQP_OS/ApplyExchangeMaster.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, requestBody.toString());
                    Log.e(LOG, response.toString());
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectForMaster(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 獲得JSON字串並解析成String字串(換貨申請單單頭)
     *
     * @param jsonData
     */
    private void parseJSONWithJSONObjectForMaster(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON格式改為字串
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String CREATE_DATE = jsonObject.getString("CREATE_DATE");
                String CUST = jsonObject.getString("CUST");
                String CUST_ADDRESS = jsonObject.getString("CUST_ADDRESS");
                String CREATOR = jsonObject.getString("CREATOR");
                String REMARK = jsonObject.getString("REMARK");
                String PROCESS = jsonObject.getString("PROCESS");

                ExchangeActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mode_txt.setText(PROCESS);
                        if (PROCESS.toString().equals("送審中")) {
                            mode_txt.setTextColor(Color.rgb(255, 165, 0));
                        } else if (PROCESS.toString().equals("核准")) {
                            mode_txt.setTextColor(Color.rgb(34, 195, 46));
                                /*cancellation_llt.setVisibility(View.VISIBLE);
                                yes_no_llt.setVisibility(View.GONE);*/
                        } else {
                            mode_txt.setTextColor(Color.rgb(220, 20, 60));
                            //cancellation_llt.setVisibility(View.GONE);
                            yes_no_llt.setVisibility(View.GONE);
                            //separate_llt.setVisibility(View.GONE);
                        }

                        date_txt.setText(CREATE_DATE);
                        client_txt.setText(CUST);
                        address_txt.setText(CUST_ADDRESS);
                        applicant_txt.setText(CREATOR);
                        remark_txt.setText(REMARK);
                    }
                });


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 與OkHttp建立連線(換貨申請單單身)
     */
    private void sendRequestWithOkHttpForDetail() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                Log.e(LOG , exchange_id);

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("EXID", exchange_id)
                            .build();
                    Request request = new Request.Builder()
                            .url("http://a.wqp-water.com.tw/WQP_OS/ApplyExchangeDetail.php")
                            //.url("http://192.168.0.172/WQP_OS/ApplyExchangeDetail.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, requestBody.toString());
                    Log.e(LOG, response.toString());
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectForDetail(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 獲得JSON字串並解析成String字串(換貨申請單單身)
     *
     * @param jsonData
     */
    private void parseJSONWithJSONObjectForDetail(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON格式改為字串
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String NUMBER = jsonObject.getString("NUMBER");
                String MB001MB002 = jsonObject.getString("MB001MB002");
                String FILTER = jsonObject.getString("FILTER");
                String DEVICE = jsonObject.getString("DEVICE");
                String MAINTAIN = jsonObject.getString("MAINTAIN");
                String SCRAP = jsonObject.getString("SCRAP");
                String COUNT = jsonObject.getString("COUNT");
                String GIFT = jsonObject.getString("GIFT");
                String INSTAL_DATE = jsonObject.getString("INSTAL_DATE");
                String LAST_MAINTAIN = jsonObject.getString("LAST_MAINTAIN");
                String WARRANTY = jsonObject.getString("WARRANTY");

                //JSONArray加入SearchData資料
                ArrayList<String> JArrayList = new ArrayList<String>();
                JArrayList.add(NUMBER);
                JArrayList.add(MB001MB002);
                JArrayList.add(FILTER);
                JArrayList.add(DEVICE);
                JArrayList.add(MAINTAIN);
                JArrayList.add(SCRAP);
                JArrayList.add(COUNT);
                JArrayList.add(GIFT);
                JArrayList.add(INSTAL_DATE);
                JArrayList.add(LAST_MAINTAIN);
                JArrayList.add(WARRANTY);

                Log.e(LOG, MB001MB002);

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

                    LinearLayout big_llt = new LinearLayout(ExchangeActivity.this);
                    big_llt.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout item_llt = new LinearLayout(ExchangeActivity.this);
                    item_llt.setOrientation(LinearLayout.HORIZONTAL);
                    item_llt.setBackgroundColor(Color.rgb(94,134,193));
                    LinearLayout product_llt = new LinearLayout(ExchangeActivity.this);
                    product_llt.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout exchange_llt = new LinearLayout(ExchangeActivity.this);
                    exchange_llt.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout mode_llt = new LinearLayout(ExchangeActivity.this);
                    mode_llt.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout count_llt = new LinearLayout(ExchangeActivity.this);
                    count_llt.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout gift_llt = new LinearLayout(ExchangeActivity.this);
                    gift_llt.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout instal_llt = new LinearLayout(ExchangeActivity.this);
                    instal_llt.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout last_llt = new LinearLayout(ExchangeActivity.this);
                    last_llt.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout warranty_llt = new LinearLayout(ExchangeActivity.this);
                    warranty_llt.setOrientation(LinearLayout.HORIZONTAL);
                    HorizontalScrollView dynamically_hsv = new HorizontalScrollView(ExchangeActivity.this);

                    Bundle jb = msg.getData();
                    ArrayList<String> JArrayList = new ArrayList<String>();
                    //int i = b.getStringArrayList("JSON_data").size();
                    JArrayList = jb.getStringArrayList("JSON_data");

                    TextView dynamically_item_txt = new TextView(ExchangeActivity.this);
                    dynamically_item_txt.setText(JArrayList.get(0));
                    dynamically_item_txt.setTextColor(Color.rgb(0, 0, 0));
                    dynamically_item_txt.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                    dynamically_item_txt.setGravity(Gravity.LEFT);
                    dynamically_item_txt.setPadding(10, 10, 0, 3);
                    item_llt.addView(dynamically_item_txt, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    TextView dynamically_product_txt = new TextView(ExchangeActivity.this);
                    dynamically_product_txt.setText(JArrayList.get(1));
                    dynamically_product_txt.setTextColor(Color.rgb(0, 0, 0));
                    dynamically_product_txt.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                    dynamically_product_txt.setGravity(Gravity.CENTER);
                    dynamically_product_txt.setPadding(10, 0, 0, 3);
                    product_llt.addView(dynamically_product_txt, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, product_dip, 1));

                    if ((JArrayList.get(2).trim().equals("Y")) && (JArrayList.get(3).trim().equals("N"))) {
                        exchange = "更換內容 : 濾芯";
                    } else if ((JArrayList.get(2).trim().equals("N")) && (JArrayList.get(3).trim().equals("Y"))){
                        exchange = "更換內容 : 設備";
                    } else if ((JArrayList.get(2).trim().equals("Y")) && (JArrayList.get(3).trim().equals("Y"))){
                        exchange = "更換內容 : 設備、濾芯";
                    }

                    TextView dynamically_exchange_txt = new TextView(ExchangeActivity.this);
                    dynamically_exchange_txt.setText(exchange);
                    dynamically_exchange_txt.setTextColor(Color.rgb(0, 0, 0));
                    dynamically_exchange_txt.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                    dynamically_exchange_txt.setGravity(Gravity.LEFT);
                    dynamically_exchange_txt.setPadding(10, 0, 0, 3);
                    exchange_llt.addView(dynamically_exchange_txt, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    if ((JArrayList.get(4).trim().equals("Y")) && (JArrayList.get(5).trim().equals("N"))) {
                        exchange_mode = "原商品處理 : 維修";
                    } else if ((JArrayList.get(4).trim().equals("N")) && (JArrayList.get(5).trim().equals("Y"))){
                        exchange_mode = "原商品處理 : 報廢";
                    }

                    TextView dynamically_mode_txt = new TextView(ExchangeActivity.this);
                    dynamically_mode_txt.setText(exchange_mode);
                    dynamically_mode_txt.setTextColor(Color.rgb(0, 0, 0));
                    dynamically_mode_txt.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                    dynamically_mode_txt.setGravity(Gravity.LEFT);
                    dynamically_mode_txt.setPadding(10, 0, 0, 3);
                    mode_llt.addView(dynamically_mode_txt, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    TextView dynamically_count_txt = new TextView(ExchangeActivity.this);
                    dynamically_count_txt.setText("換貨數量 : " + JArrayList.get(6));
                    dynamically_count_txt.setTextColor(Color.rgb(0, 0, 0));
                    dynamically_count_txt.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                    dynamically_count_txt.setGravity(Gravity.LEFT);
                    dynamically_count_txt.setPadding(10, 0, 0, 3);
                    count_llt.addView(dynamically_count_txt, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    if (JArrayList.get(7).trim().equals("")) {
                        gift = "無";
                    } else {
                        gift = JArrayList.get(7);
                    }

                    TextView dynamically_gift_txt = new TextView(ExchangeActivity.this);
                    dynamically_gift_txt.setText("贈品 : " + gift);
                    dynamically_gift_txt.setTextColor(Color.rgb(0, 0, 0));
                    dynamically_gift_txt.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                    dynamically_gift_txt.setGravity(Gravity.LEFT);
                    dynamically_gift_txt.setPadding(10, 0, 0, 10);
                    gift_llt.addView(dynamically_gift_txt, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    TextView instal_date_txt = new TextView(ExchangeActivity.this);
                    instal_date_txt.setText("安裝日期 : " + JArrayList.get(8));
                    instal_date_txt.setTextColor(Color.rgb(0, 0, 0));
                    instal_date_txt.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                    instal_date_txt.setGravity(Gravity.LEFT);
                    instal_date_txt.setPadding(10, 0, 0, 10);
                    instal_llt.addView(instal_date_txt, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    TextView last_maintain_txt = new TextView(ExchangeActivity.this);
                    last_maintain_txt.setText("最近維護日期 : " + JArrayList.get(9));
                    last_maintain_txt.setTextColor(Color.rgb(0, 0, 0));
                    last_maintain_txt.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                    last_maintain_txt.setGravity(Gravity.LEFT);
                    last_maintain_txt.setPadding(10, 0, 0, 10);
                    last_llt.addView(last_maintain_txt, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    if (JArrayList.get(10).trim().equals("Y")) {
                        warranty = "是";
                    } else {
                        warranty = "否";
                    }

                    TextView warranty_txt = new TextView(ExchangeActivity.this);
                    warranty_txt.setText("是否保固內 : " + warranty);
                    warranty_txt.setTextColor(Color.rgb(0, 0, 0));
                    warranty_txt.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                    warranty_txt.setGravity(Gravity.LEFT);
                    warranty_txt.setPadding(10, 0, 0, 10);
                    warranty_llt.addView(warranty_txt, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    dynamically_hsv.addView(product_llt);

                    big_llt.addView(item_llt);
                    big_llt.addView(dynamically_hsv);
                    big_llt.addView(exchange_llt);
                    big_llt.addView(mode_llt);
                    big_llt.addView(count_llt);
                    big_llt.addView(gift_llt);
                    big_llt.addView(instal_llt);
                    big_llt.addView(last_llt);
                    big_llt.addView(warranty_llt);

                    exchange_detail_llt.addView(big_llt, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 與OkHttp建立連線(換貨申請單核准)
     */
    private void sendRequestWithOkHttpForApproved() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e(LOG , exchange_id);
                //接收LoginActivity傳過來的值
                SharedPreferences user_id = getSharedPreferences("user_id_data", MODE_PRIVATE);
                String user_id_data = user_id.getString("ID", "");
                Log.e(LOG, user_id_data);
                String applicant_name = applicant_txt.getText().toString();
                String applicant_id = applicant_name.substring(applicant_name.indexOf(":|:"), applicant_name.length()).replace(":|:", "");
                Log.e(LOG, "ID : " + applicant_id);
                String suggest_reason = suggest_reason_edt.getText().toString();

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("EXID", exchange_id)
                            .add("User_id", user_id_data)
                            .add("applicant_id", applicant_id)
                            .add("suggest_reason", suggest_reason)
                            .build();
                    Request request = new Request.Builder()
                            .url("http://a.wqp-water.com.tw/WQP_OS/ApplyExchangeApproved.php")
                            //.url("http://192.168.0.172/WQP_OS/ApplyExchangeApproved.php")
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
     * 與OkHttp建立連線(換貨申請單退回)
     */
    private void sendRequestWithOkHttpForReject() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e(LOG , exchange_id);
                //接收LoginActivity傳過來的值
                SharedPreferences user_id = getSharedPreferences("user_id_data", MODE_PRIVATE);
                String user_id_data = user_id.getString("ID", "");
                Log.e(LOG, user_id_data);
                String applicant_name = applicant_txt.getText().toString();
                String applicant_id = applicant_name.substring(applicant_name.indexOf(":|:"), applicant_name.length()).replace(":|:", "");
                Log.e(LOG, "ID : " + applicant_id);
                String report_reason = report_checkBox.getText().toString();
                String warranty_reason = warranty_checkBox.getText().toString();
                String other_reason = reason_edt.getText().toString();
                if (report_checkBox.isChecked()) {
                    reject_reason = report_reason;
                } else if (warranty_checkBox.isChecked()) {
                    reject_reason = warranty_reason;
                } else if (other_checkBox.isChecked()) {
                    reject_reason = other_reason;
                }


                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("EXID", exchange_id)
                            .add("User_id", user_id_data)
                            .add("applicant_id", applicant_id)
                            .add("reject_reason", reject_reason)
                            .build();
                    Request request = new Request.Builder()
                            .url("http://a.wqp-water.com.tw/WQP_OS/ApplyExchangeReject.php")
                            //.url("http://192.168.0.172/WQP_OS/ApplyExchangeReject.php")
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

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(LOG, "onRestart");
        //取得UsualActivity傳遞過來的值
        GetResponseData_Usual();
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