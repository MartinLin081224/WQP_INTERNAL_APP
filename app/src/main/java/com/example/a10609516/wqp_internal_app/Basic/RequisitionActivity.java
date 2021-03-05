package com.example.a10609516.wqp_internal_app.Basic;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a10609516.wqp_internal_app.R;
import com.example.a10609516.wqp_internal_app.Tools.Adapter;
import com.example.a10609516.wqp_internal_app.Tools.WQPClickListener;
import com.example.a10609516.wqp_internal_app.Tools.WQPToolsActivity;
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
import java.text.SimpleDateFormat;
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

public class RequisitionActivity extends WQPToolsActivity {

    private SharedPreferences sp;

    private LinearLayout nav_view;
    private DrawerLayout drawer;
    private Toolbar toolbar;

    private LinearLayout it_llt, ad_llt;
    private TextView name_txt1, date_txt1, name_txt2, date_txt2;
    private EditText demand_edt1, reason_edt1, demand_edt2, display_edt, address_edt, addressee_edt, phone_edt, detail_edt, content_edt;
    private Button it_btn, ad_btn, complete_btn, approach_btn, check_btn, preview_btn, choice_btn;
    private Spinner demand_spinner1, demand_spinner2, time_spinner;

    private ArrayAdapter<String> demand_listAdapter1, demand_listAdapter2, time_listAdapter;
    private String[] demand_type1 = new String[]{"請選擇", "WEBEIP", "APP", "ERP", "硬體設備", "其他"};
    private String[] demand_type2 = new String[]{"請選擇", "目錄", "POP海報", "展示架", "陳列展示", "其他"};
    private String[] time_type = new String[]{"請選擇", "上班時間", "非上班時間", "早上9點前", "晚上"};

    private TextView mTvMessage;

    private Adapter mAdapter;
    private ArrayList<AlbumFile> mAlbumFiles;
    private RecyclerView recyclerView;

    private OkHttpClient client;
    private String path;
    private String savePath = "";
    private File image_file;
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    private Context mContext = this;

    private String LOG = "RequisitionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requisition);
        //取得控制項物件
        initViews();
        //Menu的onClickListener
        MenuListener();
        //設置Toolbar
        SetToolBar();
        //獲取當天日期
        GetDate();
        //需求類別Spinner
        DemandSpinner();
        //進場時間Spinner
        TimeSpinner();
        //照片選擇器
        AlbumGlide();
        //與OKHttp連線 藉由登入輸入的員工ID取得員工姓名
        sendRequestWithOkHttpForUserName();
    }

    /**
     * 取得控制項物件
     */
    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        nav_view = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer_layout);
        it_llt = (LinearLayout) findViewById(R.id.it_llt);
        ad_llt = (LinearLayout) findViewById(R.id.ad_llt);
        name_txt1 = (TextView) findViewById(R.id.name_txt1);
        date_txt1 = (TextView) findViewById(R.id.date_txt1);
        name_txt2 = (TextView) findViewById(R.id.name_txt2);
        date_txt2 = (TextView) findViewById(R.id.date_txt2);
        demand_edt1 = (EditText) findViewById(R.id.demand_edt1);
        demand_edt2 = (EditText) findViewById(R.id.demand_edt2);
        reason_edt1 = (EditText) findViewById(R.id.reason_edt1);
        display_edt = (EditText) findViewById(R.id.display_edt);
        address_edt = (EditText) findViewById(R.id.address_edt);
        addressee_edt = (EditText) findViewById(R.id.addressee_edt);
        phone_edt = (EditText) findViewById(R.id.phone_edt);
        detail_edt = (EditText) findViewById(R.id.detail_edt);
        content_edt = (EditText) findViewById(R.id.content_edt);
        demand_spinner1 = (Spinner) findViewById(R.id.demand_spinner1);
        demand_spinner2 = (Spinner) findViewById(R.id.demand_spinner2);
        time_spinner = (Spinner) findViewById(R.id.time_spinner);
        it_btn = (Button) findViewById(R.id.it_btn);
        ad_btn = (Button) findViewById(R.id.ad_btn);
        complete_btn = (Button) findViewById(R.id.complete_btn);
        approach_btn = (Button) findViewById(R.id.approach_btn);
        check_btn = (Button) findViewById(R.id.check_btn);
        preview_btn = (Button) findViewById(R.id.preview_btn);
        choice_btn = (Button) findViewById(R.id.choice_btn);
        mTvMessage = findViewById(R.id.tv_message);
        recyclerView = findViewById(R.id.recycler_view);

        //it_btn.setOnClickListener監聽器
        it_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                it_llt.setVisibility(View.VISIBLE);
                ad_llt.setVisibility(View.GONE);
            }
        });

        //ad_btn.setOnClickListener監聽器
        ad_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad_llt.setVisibility(View.VISIBLE);
                it_llt.setVisibility(View.GONE);
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

        check_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (it_llt.getVisibility() == View.VISIBLE ) {
                    if (complete_btn.getText().toString().equals("")) {
                        Toast.makeText(RequisitionActivity.this, "請選擇希望完成日期",Toast.LENGTH_SHORT).show();
                    } else {
                        if (demand_spinner1.getSelectedItemId() == 0) {
                            Toast.makeText(RequisitionActivity.this, "請選擇申請需求",Toast.LENGTH_SHORT).show();
                        } else if (demand_spinner1.getSelectedItemId() == 5){
                            if (demand_edt1.getText().toString().equals("")) {
                                Toast.makeText(RequisitionActivity.this, "請填寫申請需求",Toast.LENGTH_SHORT).show();
                            } else {
                                if (reason_edt1.getText().toString().equals("")) {
                                    Toast.makeText(RequisitionActivity.this, "請填寫申請原因暨目的", Toast.LENGTH_SHORT).show();
                                } else {
                                    //與OKHttp連線(上傳資訊需求單資料)
                                    sendRequestWithOkHttpForITRequisition();
                                    complete_btn.setText("");
                                    demand_edt1.setText("");
                                    reason_edt1.setText("");
                                    DemandSpinner();
                                }
                            }
                        } else {
                            if (reason_edt1.getText().toString().equals("")) {
                                Toast.makeText(RequisitionActivity.this, "請填寫申請原因暨目的",Toast.LENGTH_SHORT).show();
                            } else {
                                //與OKHttp連線(上傳資訊需求單資料)
                                sendRequestWithOkHttpForITRequisition();
                                complete_btn.setText("");
                                demand_edt1.setText("");
                                reason_edt1.setText("");
                                DemandSpinner();
                            }
                        }
                    }
                } else {
                    if (demand_spinner2.getSelectedItemId() == 0){
                        Toast.makeText(RequisitionActivity.this, "請選擇申請需求",Toast.LENGTH_SHORT).show();
                    } else {
                        if (approach_btn.getText().toString().equals("") || time_spinner.getSelectedItemId() == 0 || display_edt.getText().toString().equals("") || address_edt.getText().toString().equals("")
                                || addressee_edt.getText().toString().equals("")|| phone_edt.getText().toString().equals("") || detail_edt.getText().toString().equals("") || content_edt.getText().toString().equals("")) {
                            Toast.makeText(RequisitionActivity.this, "資料填寫不完整",Toast.LENGTH_SHORT).show();
                        } else {
                            //與OKHttp連線(上傳行銷需求單照片到SERVER)
                            uploadImage();
                            //與OKHttp連線(上傳行銷需求單資料)
                            sendRequestWithOkHttpForADRequisition();
                            approach_btn.setText("");
                            display_edt.setText("");
                            address_edt.setText("");
                            addressee_edt.setText("");
                            phone_edt.setText("");
                            detail_edt.setText("");
                            content_edt.setText("");
                            DemandSpinner();
                            TimeSpinner();
                            mAlbumFiles.clear();
                            recyclerView.removeAllViews();
                        }
                    }
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

        toolbar.setTitle("需求單");
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
                Intent intent_login = new Intent(RequisitionActivity.this, LoginActivity.class);
                startActivity(intent_login);
            }
        });
    }

    /**
     * 獲取當天日期
     */
    private void GetDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(new java.util.Date());
        date_txt1.setText(date);
        date_txt2.setText(date);
    }

    /**
     * 需求類別Spinner
     */
    private void DemandSpinner() {
        demand_listAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, demand_type1);
        demand_listAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        demand_spinner1.setAdapter(demand_listAdapter1);
        demand_spinner1.setOnItemSelectedListener(selectListener);

        demand_listAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, demand_type2);
        demand_listAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        demand_spinner2.setAdapter(demand_listAdapter2);
    }

    /**
     * 進場時間Spinner
     */
    private void TimeSpinner() {
        time_listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, time_type);
        time_listAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        time_spinner.setAdapter(time_listAdapter);
    }

    /**
     * 需求類別
     */
    /**
     * 第一個下拉類別的監看式
     */
    private AdapterView.OnItemSelectedListener selectListener = new AdapterView.OnItemSelectedListener() {
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
            //讀取第一個下拉選單是選擇第幾個
            //int pos = type_spinner.getSelectedItemPosition();
            if (String.valueOf(demand_spinner1.getSelectedItem()).equals("其他")) {
                Log.e(LOG, "123456");
                demand_edt1.setVisibility(View.VISIBLE);
            } else {
                Log.e(LOG, "654321");
                demand_edt1.setVisibility(View.INVISIBLE);
            }
        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    };

    /**
     * 照片選擇器
     */
    private void AlbumGlide() {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));//預覽欄位數量
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
                //選擇欄位數量
                .columnCount(2)
                //可選欄位數量
                .selectCount(6)
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
                        Toast.makeText(RequisitionActivity.this, R.string.canceled, Toast.LENGTH_LONG).show();
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
     * 點擊空白區域，隐藏虛擬鍵盤
     */
    public boolean onTouchEvent(MotionEvent event) {
        if(null != this.getCurrentFocus()){
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super .onTouchEvent(event);
    }

    /**
     * 與資料庫連線 藉由登入輸入的員工ID取得員工姓名
     */
    private void sendRequestWithOkHttpForUserName() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //接收LoginActivity傳過來的值
                SharedPreferences user_id = getSharedPreferences("user_id", MODE_PRIVATE);
                String user_id_data = user_id.getString("ID", "");
                Log.e(LOG, user_id_data);
                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("U_ACC", user_id_data)
                            .build();
                    Request request = new Request.Builder()
                            //.url("http://192.168.0.172/WQP_OS/UserName.php")
                            .url("http://a.wqp-water.com.tw/WQP_OS/UserName.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectOfUserName(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 獲得JSON字串並解析成String字串
     *在TextView上SHOW出回傳的員工姓名
     * @param jsonData
     */
    private void parseJSONWithJSONObjectOfUserName(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON格式改為字串
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String user_name = jsonObject.getString("MV002");
                name_txt1.setText(user_name);
                name_txt2.setText(user_name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 與OKHttp連線(上傳資訊需求單資料)
     */
    private void sendRequestWithOkHttpForITRequisition() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                String CREATOR = name_txt1.getText().toString();
                String CREATE_DATE = date_txt1.getText().toString();
                String WISH_DATE = complete_btn.getText().toString();
                String REMARK = reason_edt1.getText().toString();
                String REQUIRE_CLASS = String.valueOf(demand_spinner1.getSelectedItem());
                String REQUIRE_CLASS_NOTE = demand_edt1.getText().toString();

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("CREATOR", CREATOR)
                            .add("CREATE_DATE", CREATE_DATE)
                            .add("WISH_DATE", WISH_DATE)
                            .add("REMARK", REMARK)
                            .add("REQUIRE_CLASS", REQUIRE_CLASS)
                            .add("REQUIRE_CLASS_NOTE", REQUIRE_CLASS_NOTE)
                            .build();
                    Log.e(LOG,CREATOR);
                    Log.e(LOG,REQUIRE_CLASS);
                    Request request = new Request.Builder()
                            //.url("http://192.168.0.172/WQP_OS/ITRequisitionUpload.php")
                            .url("http://a.wqp-water.com.tw/WQP_OS/ITRequisitionUpload.php")
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
        Toast.makeText(this, "送出成功", Toast.LENGTH_SHORT).show();
    }

    /**
     * 與OKHttp連線(上傳行銷需求單資料)
     */
    private void sendRequestWithOkHttpForADRequisition() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                String CREATOR = name_txt2.getText().toString();
                String CREATE_DATE = date_txt2.getText().toString();
                String REMARK = content_edt.getText().toString();
                String REQUIRE_CLASS = String.valueOf(demand_spinner2.getSelectedItem());
                String SHOW_LOCATE = display_edt.getText().toString();
                String IN_TIME = approach_btn.getText().toString();
                String INTIME_DETAIL = String.valueOf(time_spinner.getSelectedItemId());
                String DELIVERY_ADDRESS = address_edt.getText().toString();
                String RECIPIENT = addressee_edt.getText().toString();
                String CONTACT_NUMBER = phone_edt.getText().toString();
                String SHOW_GOODS_CONTENT = detail_edt.getText().toString();

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("CREATOR", CREATOR)
                            .add("CREATE_DATE", CREATE_DATE)
                            .add("REMARK", REMARK)
                            .add("REQUIRE_CLASS", REQUIRE_CLASS)
                            .add("SHOW_LOCATE", SHOW_LOCATE)
                            .add("IN_TIME", IN_TIME)
                            .add("INTIME_DETAIL", INTIME_DETAIL)
                            .add("DELIVERY_ADDRESS", DELIVERY_ADDRESS)
                            .add("RECIPIENT", RECIPIENT)
                            .add("CONTACT_NUMBER", CONTACT_NUMBER)
                            .add("SHOW_GOODS_CONTENT", SHOW_GOODS_CONTENT)
                            .add("FILE_NAME", savePath)
                            .build();
                    Log.e(LOG,CREATOR);
                    Log.e(LOG,REQUIRE_CLASS);
                    Log.e(LOG,SHOW_LOCATE);
                    Log.e(LOG,IN_TIME);
                    Log.e(LOG,SHOW_GOODS_CONTENT);
                    Log.e(LOG,savePath);
                    Request request = new Request.Builder()
                            //.url("http://192.168.0.172/WQP_OS/ADRequisitionUpload.php")
                            .url("http://a.wqp-water.com.tw/WQP_OS/ADRequisitionUpload.php")
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
        Toast.makeText(this, "送出成功", Toast.LENGTH_SHORT).show();
    }

    private void uploadImage() {
        client = new OkHttpClient();
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        //取得檔案路徑及檔案名稱並上傳至SERVER
        for(int x = 0; x < mAlbumFiles.size(); x++) {
            AlbumFile file = mAlbumFiles.get(x);
            path = file.getPath();
            Log.e(LOG + "888", path);

            image_file = new File(path);
            builder.addFormDataPart("img", image_file.getName(), RequestBody.create(MEDIA_TYPE_PNG, image_file));
            Log.e(LOG + "8888", image_file.getName());

            String fileName = "C:\\xampp\\htdocs\\WQP\\demandform_pic\\";
            savePath = savePath + fileName + image_file.getName() + ",";
            Log.e(LOG + "BBB", savePath);

            // TODO upload...
            MultipartBody requestBody = builder.build();
            Request request = new Request.Builder()
                    //.url("http://192.168.0.172/WQP_OS/RequisitionPicture.php")
                    .url("http://a.wqp-water.com.tw/WQP_OS/RequisitionPicture.php")
                    .post(requestBody)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(LOG + "FAIL", "onFailure: 失敗" + e.getLocalizedMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.e(LOG + "SUCCESS", "onResponse: " + response.body().string());
                    //提交成功處理结果....
                }
            });
        }

        Log.e(LOG + "BBBB", savePath);

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