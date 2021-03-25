package com.example.a10609516.wqp_internal_app.Works;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a10609516.wqp_internal_app.R;
import com.example.a10609516.wqp_internal_app.Tools.Adapter;
import com.example.a10609516.wqp_internal_app.Tools.WQPToolsActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import androidx.core.app.ActivityCompat;
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

import static com.example.a10609516.wqp_internal_app.Works.MissionDetailActivity.MY_PERMISSION_ACCESS_LOCATION;

public class MissionReportActivity extends WQPToolsActivity {

    private Toolbar toolbar;

    private LinearLayout censor_llt, unless_llt, pay_llt, is_get_llt, receivable_llt, have_get_llt, eng_money_llt, photo_llt,
            first_photo_llt, second_photo_llt, third_photo_llt, fourth_photo_llt, fifth_photo_llt, sixth_photo_llt, seventh_photo_llt, eighth_photo_llt, ninth_photo_llt, tenth_photo_llt;
    private TextView first_photo_txt, second_photo_txt, third_photo_txt, fourth_photo_txt, fifth_photo_txt, sixth_photo_txt, seventh_photo_txt, eighth_photo_txt, ninth_photo_txt, tenth_photo_txt;
    private ImageView image_view1, image_view2, image_view3, image_view4, image_view5, image_view6, image_view7, image_view8, image_view9, image_view10;
    private CheckBox is_get_money_checkBox, have_get_money_checkBox, not_get_money_checkBox;
    private TextView receivable_money_txt, gps_txt;
    private EditText eng_money_edt, eng_report_edt;
    private Button preview_btn, choice_btn, report_btn;
    private Spinner type_spinner, unless_spinner, censor_spinner, pay_spinner;
    private ArrayAdapter<String> type_listAdapter, unless_listAdapter, censor_listAdapter, pay_listAdapter;
    private String[] type_list = new String[]{};
    private String[] unless_list = new String[]{};
    private String[] censor_list = new String[]{};
    private String[] pay_list = new String[]{};
    private String user_id_data, rm001, rm002, RM003, RM015, RM016, RM017, RM018, RM019, RM020, RM021, ML008;
    private double latitude, longitude;

    private Adapter mAdapter1, mAdapter2, mAdapter3, mAdapter4, mAdapter5, mAdapter6, mAdapter7, mAdapter8, mAdapter9, mAdapter10;
    private ArrayList<AlbumFile> mAlbumFiles1, mAlbumFiles2, mAlbumFiles3, mAlbumFiles4, mAlbumFiles5, mAlbumFiles6, mAlbumFiles7, mAlbumFiles8, mAlbumFiles9, mAlbumFiles10;
    private RecyclerView recyclerView1, recyclerView2, recyclerView3, recyclerView4, recyclerView5, recyclerView6, recyclerView7, recyclerView8, recyclerView9, recyclerView10;

    private OkHttpClient photo_client;
    private String path1, path2, path3, path4, path5, path6, path7, path8, path9, path10;
    private String savePath1 = "";
    private String savePath2 = "";
    private String savePath3 = "";
    private String savePath4 = "";
    private String savePath5 = "";
    private String savePath6 = "";
    private String savePath7 = "";
    private String savePath8 = "";
    private String savePath9 = "";
    private String savePath10 = "";
    private File image_file1, image_file2, image_file3, image_file4, image_file5, image_file6, image_file7, image_file8, image_file9, image_file10;
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    private String LOG = "MissionReportActivity";
    private Context context;
    private String commandStr, GPS;
    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_report);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("處理回報");
        context = this;

        //取得控制項物件
        initViews();
        //處理狀況的Spinner
        sendRequestWithOkHttpForReportType();
        //設置收款金額的代入與取消
        //HaveGetMoney();
        //抓取設備GPS位置
        GPSLocation();
        //照片選擇器
        AlbumGlide1();
        AlbumGlide2();
        AlbumGlide3();
        AlbumGlide4();
        AlbumGlide5();
        AlbumGlide6();
        AlbumGlide7();
        AlbumGlide8();
        AlbumGlide9();
    }

    /**
     * 取得控制項物件
     */
    private void initViews() {
        censor_llt = findViewById(R.id.censor_llt);
        unless_llt = findViewById(R.id.unless_llt);
        pay_llt = findViewById(R.id.pay_llt);
        is_get_llt = findViewById(R.id.is_get_llt);
        receivable_llt = findViewById(R.id.receivable_llt);
        have_get_llt = findViewById(R.id.have_get_llt);
        eng_money_llt = findViewById(R.id.eng_money_llt);
        photo_llt = findViewById(R.id.photo_llt);
        first_photo_llt = findViewById(R.id.first_photo_llt);
        second_photo_llt = findViewById(R.id.second_photo_llt);
        third_photo_llt = findViewById(R.id.third_photo_llt);
        fourth_photo_llt = findViewById(R.id.fourth_photo_llt);
        fifth_photo_llt = findViewById(R.id.fifth_photo_llt);
        sixth_photo_llt = findViewById(R.id.sixth_photo_llt);
        seventh_photo_llt = findViewById(R.id.seventh_photo_llt);
        eighth_photo_llt = findViewById(R.id.eighth_photo_llt);
        ninth_photo_llt = findViewById(R.id.ninth_photo_llt);
        tenth_photo_llt = findViewById(R.id.tenth_photo_llt);
        first_photo_txt = findViewById(R.id.first_photo_txt);
        second_photo_txt = findViewById(R.id.second_photo_txt);
        third_photo_txt = findViewById(R.id.third_photo_txt);
        fourth_photo_txt = findViewById(R.id.fourth_photo_txt);
        fifth_photo_txt = findViewById(R.id.fifth_photo_txt);
        sixth_photo_txt = findViewById(R.id.sixth_photo_txt);
        seventh_photo_txt = findViewById(R.id.seventh_photo_txt);
        eighth_photo_txt = findViewById(R.id.eighth_photo_txt);
        ninth_photo_txt = findViewById(R.id.ninth_photo_txt);
        tenth_photo_txt = findViewById(R.id.tenth_photo_txt);
        image_view1 = findViewById(R.id.image_view1);
        image_view2 = findViewById(R.id.image_view2);
        image_view3 = findViewById(R.id.image_view3);
        image_view4 = findViewById(R.id.image_view4);
        image_view5 = findViewById(R.id.image_view5);
        image_view6 = findViewById(R.id.image_view6);
        image_view7 = findViewById(R.id.image_view7);
        image_view8 = findViewById(R.id.image_view8);
        image_view9 = findViewById(R.id.image_view9);
        image_view10 = findViewById(R.id.image_view10);
        recyclerView1 = findViewById(R.id.recycler_view1);
        recyclerView2 = findViewById(R.id.recycler_view2);
        recyclerView3 = findViewById(R.id.recycler_view3);
        recyclerView4 = findViewById(R.id.recycler_view4);
        recyclerView5 = findViewById(R.id.recycler_view5);
        recyclerView6 = findViewById(R.id.recycler_view6);
        recyclerView7 = findViewById(R.id.recycler_view7);
        recyclerView8 = findViewById(R.id.recycler_view8);
        recyclerView9 = findViewById(R.id.recycler_view9);
        recyclerView10 = findViewById(R.id.recycler_view10);
        type_spinner = findViewById(R.id.type_spinner);
        unless_spinner = findViewById(R.id.unless_spinner);
        censor_spinner = findViewById(R.id.censor_spinner);
        pay_spinner = findViewById(R.id.pay_spinner);
        receivable_money_txt = findViewById(R.id.receivable_money_txt);
        gps_txt = findViewById(R.id.gps_txt);
        eng_money_edt = findViewById(R.id.eng_money_edt);
        eng_report_edt = findViewById(R.id.eng_report_edt);
        is_get_money_checkBox = findViewById(R.id.is_get_money_checkBox);
        have_get_money_checkBox = findViewById(R.id.have_get_money_checkBox);
        not_get_money_checkBox = findViewById(R.id.not_get_money_checkBox);
        report_btn = findViewById(R.id.report_btn);
        //preview_btn = findViewById(R.id.preview_btn);
        //choice_btn = findViewById(R.id.choice_btn);

        /*preview_btn.setOnClickListener(new View.OnClickListener() {
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
        });*/

        image_view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage1();
            }
        });

        image_view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage2();
            }
        });

        image_view3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage3();
            }
        });

        image_view4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage4();
            }
        });

        image_view5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage5();
            }
        });

        image_view6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage6();
            }
        });

        image_view7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage7();
            }
        });

        image_view8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage8();
            }
        });

        image_view9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage9();
            }
        });

        report_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type_spinner.getSelectedItemId() == 0) {
                    Toast.makeText(MissionReportActivity.this, "【請選擇處理狀況】", Toast.LENGTH_SHORT).show();
                } else if (type_spinner.getSelectedItemId() == 1) {
                    //與OkHttp建立連線(任務回報-抵達目的)
                    sendRequestWithOkHttpForMissionReportArrive();
                    finish();
                } else if (type_spinner.getSelectedItemId() == 2) {
                    if ((RM003.equals("8") || RM003.equals("9")) && censor_spinner.getSelectedItemId() == 0) {
                        Toast.makeText(MissionReportActivity.this, "【請選擇檢修類別】", Toast.LENGTH_SHORT).show();
                    } else {
                        if (photo_llt.getVisibility() == View.GONE) {
                            //與OkHttp建立連線(任務回報-離開目的-檢修)
                            sendRequestWithOkHttpForMissionReportLeave();
                            finish();
                        } else {
                            if ((image_view1.getVisibility() == View.VISIBLE || image_view2.getVisibility() == View.VISIBLE || image_view3.getVisibility() == View.VISIBLE ||
                                    image_view4.getVisibility() == View.VISIBLE || image_view5.getVisibility() == View.VISIBLE) ||
                                    ((image_view7.getVisibility() == View.VISIBLE || image_view8.getVisibility() == View.VISIBLE) && RM003.equals("4"))) {
                                Toast.makeText(MissionReportActivity.this, "【請上傳現場照片(*為必傳項目)】", Toast.LENGTH_SHORT).show();
                            } else {
                                //與OkHttp建立連線(任務回報-離開目的)
                                sendRequestWithOkHttpForMissionReportLeave();
                                //上傳照片回Server
                                uploadImage1();
                                uploadImage2();
                                uploadImage3();
                                uploadImage4();
                                uploadImage5();
                                if (image_view6.getVisibility() == View.GONE) {
                                    uploadImage6();
                                }

                                if (RM003.equals("4")) {
                                    uploadImage7();
                                    uploadImage8();
                                    if (image_view9.getVisibility() == View.GONE) {
                                        uploadImage9();
                                    }
                                }
                                //與OkHttp建立連線(上傳照片路徑)
                                sendRequestWithOkHttpForMissionPhoto();
                                finish();
                            }
                        }
                    }
                } else if (type_spinner.getSelectedItemId() == 3) {
                    if (unless_spinner.getSelectedItemId() == 0) {
                        Toast.makeText(MissionReportActivity.this, "【請選擇無效派工原因】", Toast.LENGTH_SHORT).show();
                    } else {
                        if ((RM003.equals("8") || RM003.equals("9")) && censor_spinner.getSelectedItemId() == 0) {
                            Toast.makeText(MissionReportActivity.this, "【請選擇檢修類別】", Toast.LENGTH_SHORT).show();
                        } else {
                            //與OkHttp建立連線(任務回報-離開目的-無效派工)
                            sendRequestWithOkHttpForMissionReportUnless();
                            finish();
                        }
                    }
                }

            }
        });
    }

    /**
     * 處理狀況的Spinner
     * 與OkHttp建立連線(MissionReportType)
     */
    private void sendRequestWithOkHttpForReportType() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .build();
                    Request request = new Request.Builder()
                            .url("http://a.wqp-water.com.tw/WQP_OS/MissionReportType.php")
                            //.url("http://192.168.0.172/WQP_OS/MissionReportType.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectForReportType(responseData);
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
    private void parseJSONWithJSONObjectForReportType(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            ArrayList<String> JArrayList = new ArrayList<String>();
            type_list = new String[]{};
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON格式改為字串
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String R_name = jsonObject.getString("R_NAME");
                //JSONArray加入SearchData資料
                JArrayList.add(R_name);
                type_list = JArrayList.toArray(new String[i]);
                Log.e(LOG, R_name);
            }
            MissionReportActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //重新產生新的Adapter/*，用的是二維陣列type2[pos]*/
                    type_listAdapter = new ArrayAdapter<String>(context, R.layout.mission_spinner, type_list);
                    type_listAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //載入第二個下拉選單Spinner
                    type_spinner.setAdapter(type_listAdapter);
                    type_spinner.setOnItemSelectedListener(type_selectListener);
                    //無效派工的Spinner
                    sendRequestWithOkHttpForUnlessWork();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通路別 第一個下拉類別的監看式
     */
    private AdapterView.OnItemSelectedListener type_selectListener = new AdapterView.OnItemSelectedListener() {
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
            //讀取第一個下拉選單是選擇第幾個
            //int pos = type_spinner.getSelectedItemPosition();
            if (type_spinner.getSelectedItemId() == 0) {
                unless_llt.setVisibility(View.GONE);
                pay_llt.setVisibility(View.GONE);
                is_get_llt.setVisibility(View.GONE);
                receivable_llt.setVisibility(View.GONE);
                have_get_llt.setVisibility(View.GONE);
                eng_money_llt.setVisibility(View.GONE);
                photo_llt.setVisibility(View.GONE);
            } else if (type_spinner.getSelectedItemId() == 1) {
                unless_llt.setVisibility(View.GONE);
                pay_llt.setVisibility(View.GONE);
                is_get_llt.setVisibility(View.GONE);
                receivable_llt.setVisibility(View.GONE);
                have_get_llt.setVisibility(View.GONE);
                eng_money_llt.setVisibility(View.GONE);
                photo_llt.setVisibility(View.GONE);
            } else if (type_spinner.getSelectedItemId() == 2) {
                if ((RM003.equals("8") || RM003.equals("9"))) {
                    censor_llt.setVisibility(View.VISIBLE);
                    unless_llt.setVisibility(View.GONE);
                    pay_llt.setVisibility(View.VISIBLE);
                    is_get_llt.setVisibility(View.VISIBLE);
                    receivable_llt.setVisibility(View.VISIBLE);
                    have_get_llt.setVisibility(View.VISIBLE);
                    eng_money_llt.setVisibility(View.VISIBLE);
                    photo_llt.setVisibility(View.GONE);
                } else if ((RM003.equals("4") || RM003.equals("5") || RM003.equals("6"))) {
                    censor_llt.setVisibility(View.GONE);
                    unless_llt.setVisibility(View.GONE);
                    pay_llt.setVisibility(View.VISIBLE);
                    is_get_llt.setVisibility(View.VISIBLE);
                    receivable_llt.setVisibility(View.VISIBLE);
                    have_get_llt.setVisibility(View.VISIBLE);
                    eng_money_llt.setVisibility(View.VISIBLE);
                    photo_llt.setVisibility(View.VISIBLE);
                } else {
                    censor_llt.setVisibility(View.GONE);
                    unless_llt.setVisibility(View.GONE);
                    pay_llt.setVisibility(View.VISIBLE);
                    is_get_llt.setVisibility(View.VISIBLE);
                    receivable_llt.setVisibility(View.VISIBLE);
                    have_get_llt.setVisibility(View.VISIBLE);
                    eng_money_llt.setVisibility(View.VISIBLE);
                    photo_llt.setVisibility(View.GONE);
                }
            } else if (type_spinner.getSelectedItemId() == 3) {
                if ((RM003.equals("8") || RM003.equals("9"))) {
                    censor_llt.setVisibility(View.VISIBLE);
                } else {
                    censor_llt.setVisibility(View.GONE);
                }
                unless_llt.setVisibility(View.VISIBLE);
                pay_llt.setVisibility(View.VISIBLE);
                is_get_llt.setVisibility(View.VISIBLE);
                receivable_llt.setVisibility(View.VISIBLE);
                have_get_llt.setVisibility(View.VISIBLE);
                eng_money_llt.setVisibility(View.VISIBLE);
                photo_llt.setVisibility(View.GONE);
            }
        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    };

    /**
     * 無效派工的Spinner
     * 與OkHttp建立連線(MissionUnlessWork)
     */
    private void sendRequestWithOkHttpForUnlessWork() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .build();
                    Request request = new Request.Builder()
                            .url("http://a.wqp-water.com.tw/WQP_OS/MissionUnlessWork.php")
                            //.url("http://192.168.0.172/WQP_OS/MissionUnlessWork.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectForUnlessWork(responseData);
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
    private void parseJSONWithJSONObjectForUnlessWork(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            ArrayList<String> JArrayList = new ArrayList<String>();
            unless_list = new String[]{};
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON格式改為字串
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String F_name = jsonObject.getString("F_NAME");
                //JSONArray加入SearchData資料
                JArrayList.add(F_name);
                unless_list = JArrayList.toArray(new String[i]);
                Log.e(LOG, F_name);
            }
            MissionReportActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //重新產生新的Adapter/*，用的是二維陣列type2[pos]*/
                    unless_listAdapter = new ArrayAdapter<String>(context, R.layout.mission_spinner, unless_list);
                    unless_listAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //載入第二個下拉選單Spinner
                    unless_spinner.setAdapter(unless_listAdapter);
                    //檢修類別的Spinner
                    sendRequestWithOkHttpForCensorType();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 檢修類別的Spinner
     * 與OkHttp建立連線(MissionCensorType)
     */
    private void sendRequestWithOkHttpForCensorType() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .build();
                    Request request = new Request.Builder()
                            .url("http://a.wqp-water.com.tw/WQP_OS/MissionCensorType.php")
                            //.url("http://192.168.0.172/WQP_OS/MissionCensorType.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectForCensorType(responseData);
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
    private void parseJSONWithJSONObjectForCensorType(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            ArrayList<String> JArrayList = new ArrayList<String>();
            censor_list = new String[]{};
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON格式改為字串
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String C_name = jsonObject.getString("C_NAME");
                //JSONArray加入SearchData資料
                JArrayList.add(C_name);
                censor_list = JArrayList.toArray(new String[i]);
                Log.e(LOG, C_name);
            }
            MissionReportActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //重新產生新的Adapter/*，用的是二維陣列type2[pos]*/
                    censor_listAdapter = new ArrayAdapter<String>(context, R.layout.mission_spinner, censor_list);
                    censor_listAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //載入第二個下拉選單Spinner
                    censor_spinner.setAdapter(censor_listAdapter);
                    //付款方式的Spinner
                    sendRequestWithOkHttpForPayMode();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 付款方式的Spinner
     * 與OkHttp建立連線(MissionPayMode)
     */
    private void sendRequestWithOkHttpForPayMode() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .build();
                    Request request = new Request.Builder()
                            .url("http://a.wqp-water.com.tw/WQP_OS/MissionPayMode.php")
                            //.url("http://192.168.0.172/WQP_OS/MissionPayMode.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectForPayMode(responseData);
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
    private void parseJSONWithJSONObjectForPayMode(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            ArrayList<String> JArrayList = new ArrayList<String>();
            pay_list = new String[]{};
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON格式改為字串
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String cp_name = jsonObject.getString("CP_NAME");
                //JSONArray加入SearchData資料
                JArrayList.add(cp_name);
                pay_list = JArrayList.toArray(new String[i]);
                Log.e(LOG, cp_name);
            }
            MissionReportActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //重新產生新的Adapter/*，用的是二維陣列type2[pos]*/
                    pay_listAdapter = new ArrayAdapter<String>(context, R.layout.mission_spinner, pay_list);
                    pay_listAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //載入第二個下拉選單Spinner
                    pay_spinner.setAdapter(pay_listAdapter);
                    //與OkHttp建立連線(t取得派工任務回報明細)
                    sendRequestWithOkHttpForMissionReportDetail();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 與OkHttp建立連線(t取得派工任務回報明細)
     */
    private void sendRequestWithOkHttpForMissionReportDetail() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //接收LoginActivity傳過來的值
                SharedPreferences user_id = getSharedPreferences("user_id", MODE_PRIVATE);
                user_id_data = user_id.getString("ID", "");
                //接收MissionActivity傳過來的值
                Bundle bundle = getIntent().getExtras();
                rm001 = bundle.getString("rm001").trim();
                rm002 = bundle.getString("rm002").trim();


                Log.e(LOG, user_id_data);
                Log.e(LOG, rm002);

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("U_ACC", user_id_data)
                            .add("RM002", rm002)
                            .build();
                    Log.e(LOG, user_id_data);
                    Log.e(LOG, rm002);
                    Request request = new Request.Builder()
                            .url("http://a.wqp-water.com.tw/WQP_OS/MissionReportSearch.php")
                            //.url("http://192.168.0.172/WQP_OS/MissionReportSearch.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, requestBody.toString());
                    Log.e(LOG, response.toString());
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectForMissionReportDetail(responseData);
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
    private void parseJSONWithJSONObjectForMissionReportDetail(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON格式改為字串
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                RM003 = jsonObject.getString("RM003").trim();
                RM015 = jsonObject.getString("RM015").trim();
                RM016 = jsonObject.getString("RM016").trim();
                RM017 = jsonObject.getString("RM017").trim();
                RM018 = jsonObject.getString("RM018").trim();
                RM019 = jsonObject.getString("RM019").trim();

                Log.e(LOG, RM015);
                Log.e(LOG, RM016);
                Log.e(LOG, RM017);
                Log.e(LOG, RM018);
                Log.e(LOG, RM019);

                MissionReportActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        receivable_money_txt.setText(RM017.trim());
                        eng_money_edt.setText(RM019.trim());

                        //當迴圈與ResponseText內容一致時跳出迴圈 並顯示該ResponseText的Spinner位置
                        for (int i = 0; i < pay_list.length; i++) {
                            if (pay_list[i].equals(RM015)) {
                                pay_spinner.setSelection(i, true);
                                break;
                            }
                        }

                        if (RM016.equals("0")) {
                            is_get_money_checkBox.setChecked(false);
                        } else {
                            is_get_money_checkBox.setChecked(true);
                        }

                        if (RM018.equals("0")) {
                            not_get_money_checkBox.setChecked(true);
                            have_get_money_checkBox.setChecked(false);
                        } else {
                            have_get_money_checkBox.setChecked(true);
                            not_get_money_checkBox.setChecked(false);
                        }

                        if (RM003.equals("4")) {
                            first_photo_txt.setText("*安裝前現場照片\n(水槽)");
                            second_photo_txt.setText("*安裝前現場照片\n(廚下)");
                            third_photo_txt.setText("*安裝後現場照片\n(水槽)");
                            fourth_photo_txt.setText("*安裝後現場照片\n(廚下)");
                            fifth_photo_txt.setText("*安裝水壓");
                            sixth_photo_txt.setText("漏水斷路器");
                            seventh_photo_txt.setText("*工具與保護墊");
                            eighth_photo_txt.setText("*撕毀無效貼紙");
                            ninth_photo_txt.setText("減壓閥");
                        } else if (RM003.equals("5") || RM003.equals("6")) {
                            first_photo_txt.setText("*安裝前現場照片");
                            second_photo_txt.setText("*安裝後現場照片");
                            third_photo_txt.setText("*地排(近照)");
                            fourth_photo_txt.setText("*地排(遠照)");
                            fifth_photo_txt.setText("*安裝水壓");
                            sixth_photo_txt.setText("旁通(ByPass)");
                            seventh_photo_llt.setVisibility(View.GONE);
                            eighth_photo_llt.setVisibility(View.GONE);
                            ninth_photo_llt.setVisibility(View.GONE);
                            tenth_photo_llt.setVisibility(View.GONE);
                        }/* else if (RM003.equals("8") || RM003.equals("9")) {
                            censor_llt.setVisibility(View.VISIBLE);
                        }*/
                    }
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            case R.id.is_get_money_checkBox:
                if (checked) {
                    have_get_money_checkBox.setChecked(true);
                    not_get_money_checkBox.setChecked(false);
                    eng_money_edt.setText(RM017);
                } else {
                    have_get_money_checkBox.setChecked(false);
                    not_get_money_checkBox.setChecked(false);
                    eng_money_edt.setText("0");
                }
                break;
            case R.id.have_get_money_checkBox:
                if (checked) {
                    is_get_money_checkBox.setChecked(true);
                    not_get_money_checkBox.setChecked(false);
                    //如果have_get_money_checkBox被勾選 則收款金額直接帶入應收金額
                    eng_money_edt.setText(RM017);
                }
                break;
            case R.id.not_get_money_checkBox:
                if (checked) {
                    have_get_money_checkBox.setChecked(false);
                    eng_money_edt.setText("0");
                }
                break;
        }
    }

    /**
     * 設置收款金額的代入與取消
     */
    private void HaveGetMoney() {
        have_get_money_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //如果have_get_money_checkBox被勾選 則收款金額直接帶入應收金額
                    eng_money_edt.setText(RM017);
                }
                if (!isChecked) {
                    //如果have_get_money_checkBox被取消勾選 則收款金額清空
                    eng_money_edt.setText("0");
                }
            }
        });
        not_get_money_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    //如果not_get_all_checkBox被取消勾選 則收款金額清空
                    eng_money_edt.setText("0");
                }
            }
        });
    }

    /**
     * 照片選擇器
     */
    private void AlbumGlide1() {
        recyclerView1.setLayoutManager(new GridLayoutManager(this, 1));//預覽欄位數量
        Divider divider = new Api21ItemDivider(Color.TRANSPARENT, 10, 10);
        recyclerView1.addItemDecoration(divider);

        mAdapter1 = new Adapter(this, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                previewImage1(position);
            }
        });

        recyclerView1.setAdapter(mAdapter1);

        Log.e(LOG + "111", mAdapter1.toString());
    }

    /**
     * Select picture, from album.
     */
    private void selectImage1() {
        Album.image(this)
                .multipleChoice()
                .camera(true)
                //選擇欄位數量
                .columnCount(2)
                //可選欄位數量
                .selectCount(1)
                .checkedList(mAlbumFiles1)

                .onResult(new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(@NonNull ArrayList<AlbumFile> result) {
                        mAlbumFiles1 = result;
                        mAdapter1.notifyDataSetChanged(mAlbumFiles1);
                        image_view1.setVisibility(result.size() > 0 ? View.GONE : View.VISIBLE);
                        Log.e(LOG, result.toString());
                    }
                })
                .onCancel(new Action<String>() {
                    @Override
                    public void onAction(@NonNull String result) {
                        Toast.makeText(MissionReportActivity.this, R.string.canceled, Toast.LENGTH_LONG).show();
                        image_view1.setVisibility(View.VISIBLE);
                    }
                })
                .start();
    }

    /**
     * Preview image, to album.
     */
    private void previewImage1(int position) {
        if (mAlbumFiles1 == null || mAlbumFiles1.size() == 0) {
            Toast.makeText(this, R.string.no_selected, Toast.LENGTH_LONG).show();
        } else {
            Album.galleryAlbum(this)
                    .checkable(true)
                    .checkedList(mAlbumFiles1)
                    .currentPosition(position)

                    .onResult(new Action<ArrayList<AlbumFile>>() {
                        @Override
                        public void onAction(@NonNull ArrayList<AlbumFile> result) {
                            mAlbumFiles1 = result;
                            mAdapter1.notifyDataSetChanged(mAlbumFiles1);
                            image_view1.setVisibility(result.size() > 0 ? View.GONE : View.VISIBLE);

                            Log.e(LOG + "123", mAlbumFiles1.toString());
                        }
                    })
                    .start();
        }
    }

    /**
     * 照片選擇器
     */
    private void AlbumGlide2() {
        recyclerView2.setLayoutManager(new GridLayoutManager(this, 1));//預覽欄位數量
        Divider divider = new Api21ItemDivider(Color.TRANSPARENT, 10, 10);
        recyclerView2.addItemDecoration(divider);

        mAdapter2 = new Adapter(this, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                previewImage2(position);
            }
        });

        recyclerView2.setAdapter(mAdapter2);

        Log.e(LOG + "111", mAdapter2.toString());
    }

    /**
     * Select picture, from album.
     */
    private void selectImage2() {
        Album.image(this)
                .multipleChoice()
                .camera(true)
                //選擇欄位數量
                .columnCount(2)
                //可選欄位數量
                .selectCount(1)
                .checkedList(mAlbumFiles2)

                .onResult(new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(@NonNull ArrayList<AlbumFile> result) {
                        mAlbumFiles2 = result;
                        mAdapter2.notifyDataSetChanged(mAlbumFiles2);
                        image_view2.setVisibility(result.size() > 0 ? View.GONE : View.VISIBLE);
                        Log.e(LOG, result.toString());
                    }
                })
                .onCancel(new Action<String>() {
                    @Override
                    public void onAction(@NonNull String result) {
                        Toast.makeText(MissionReportActivity.this, R.string.canceled, Toast.LENGTH_LONG).show();
                    }
                })
                .start();
    }

    /**
     * Preview image, to album.
     */
    private void previewImage2(int position) {
        if (mAlbumFiles2 == null || mAlbumFiles2.size() == 0) {
            Toast.makeText(this, R.string.no_selected, Toast.LENGTH_LONG).show();
        } else {
            Album.galleryAlbum(this)
                    .checkable(true)
                    .checkedList(mAlbumFiles2)
                    .currentPosition(position)

                    .onResult(new Action<ArrayList<AlbumFile>>() {
                        @Override
                        public void onAction(@NonNull ArrayList<AlbumFile> result) {
                            mAlbumFiles2 = result;
                            mAdapter2.notifyDataSetChanged(mAlbumFiles2);
                            image_view2.setVisibility(result.size() > 0 ? View.GONE : View.VISIBLE);

                            Log.e(LOG + "123", mAlbumFiles2.toString());
                        }
                    })
                    .start();
        }
    }

    /**
     * 照片選擇器
     */
    private void AlbumGlide3() {
        recyclerView3.setLayoutManager(new GridLayoutManager(this, 1));//預覽欄位數量
        Divider divider = new Api21ItemDivider(Color.TRANSPARENT, 10, 10);
        recyclerView3.addItemDecoration(divider);

        mAdapter3 = new Adapter(this, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                previewImage3(position);
            }
        });

        recyclerView3.setAdapter(mAdapter3);

        Log.e(LOG + "111", mAdapter3.toString());
    }

    /**
     * Select picture, from album.
     */
    private void selectImage3() {
        Album.image(this)
                .multipleChoice()
                .camera(true)
                //選擇欄位數量
                .columnCount(2)
                //可選欄位數量
                .selectCount(1)
                .checkedList(mAlbumFiles3)

                .onResult(new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(@NonNull ArrayList<AlbumFile> result) {
                        mAlbumFiles3 = result;
                        mAdapter3.notifyDataSetChanged(mAlbumFiles3);
                        image_view3.setVisibility(result.size() > 0 ? View.GONE : View.VISIBLE);
                        Log.e(LOG, result.toString());
                    }
                })
                .onCancel(new Action<String>() {
                    @Override
                    public void onAction(@NonNull String result) {
                        Toast.makeText(MissionReportActivity.this, R.string.canceled, Toast.LENGTH_LONG).show();
                    }
                })
                .start();
    }

    /**
     * Preview image, to album.
     */
    private void previewImage3(int position) {
        if (mAlbumFiles3 == null || mAlbumFiles3.size() == 0) {
            Toast.makeText(this, R.string.no_selected, Toast.LENGTH_LONG).show();
        } else {
            Album.galleryAlbum(this)
                    .checkable(true)
                    .checkedList(mAlbumFiles3)
                    .currentPosition(position)

                    .onResult(new Action<ArrayList<AlbumFile>>() {
                        @Override
                        public void onAction(@NonNull ArrayList<AlbumFile> result) {
                            mAlbumFiles3 = result;
                            mAdapter3.notifyDataSetChanged(mAlbumFiles3);
                            image_view3.setVisibility(result.size() > 0 ? View.GONE : View.VISIBLE);

                            Log.e(LOG + "123", mAlbumFiles3.toString());
                        }
                    })
                    .start();
        }
    }

    /**
     * 照片選擇器
     */
    private void AlbumGlide4() {
        recyclerView4.setLayoutManager(new GridLayoutManager(this, 1));//預覽欄位數量
        Divider divider = new Api21ItemDivider(Color.TRANSPARENT, 10, 10);
        recyclerView4.addItemDecoration(divider);

        mAdapter4 = new Adapter(this, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                previewImage4(position);
            }
        });

        recyclerView4.setAdapter(mAdapter4);

        Log.e(LOG + "111", mAdapter4.toString());
    }

    /**
     * Select picture, from album.
     */
    private void selectImage4() {
        Album.image(this)
                .multipleChoice()
                .camera(true)
                //選擇欄位數量
                .columnCount(2)
                //可選欄位數量
                .selectCount(1)
                .checkedList(mAlbumFiles4)

                .onResult(new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(@NonNull ArrayList<AlbumFile> result) {
                        mAlbumFiles4 = result;
                        mAdapter4.notifyDataSetChanged(mAlbumFiles4);
                        image_view4.setVisibility(result.size() > 0 ? View.GONE : View.VISIBLE);
                        Log.e(LOG, result.toString());
                    }
                })
                .onCancel(new Action<String>() {
                    @Override
                    public void onAction(@NonNull String result) {
                        Toast.makeText(MissionReportActivity.this, R.string.canceled, Toast.LENGTH_LONG).show();
                    }
                })
                .start();
    }

    /**
     * Preview image, to album.
     */
    private void previewImage4(int position) {
        if (mAlbumFiles4 == null || mAlbumFiles4.size() == 0) {
            Toast.makeText(this, R.string.no_selected, Toast.LENGTH_LONG).show();
        } else {
            Album.galleryAlbum(this)
                    .checkable(true)
                    .checkedList(mAlbumFiles4)
                    .currentPosition(position)

                    .onResult(new Action<ArrayList<AlbumFile>>() {
                        @Override
                        public void onAction(@NonNull ArrayList<AlbumFile> result) {
                            mAlbumFiles4 = result;
                            mAdapter4.notifyDataSetChanged(mAlbumFiles4);
                            image_view4.setVisibility(result.size() > 0 ? View.GONE : View.VISIBLE);

                            Log.e(LOG + "123", mAlbumFiles4.toString());
                        }
                    })
                    .start();
        }
    }

    /**
     * 照片選擇器
     */
    private void AlbumGlide5() {
        recyclerView5.setLayoutManager(new GridLayoutManager(this, 1));//預覽欄位數量
        Divider divider = new Api21ItemDivider(Color.TRANSPARENT, 10, 10);
        recyclerView5.addItemDecoration(divider);

        mAdapter5 = new Adapter(this, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                previewImage5(position);
            }
        });

        recyclerView5.setAdapter(mAdapter5);

        Log.e(LOG + "111", mAdapter5.toString());
    }

    /**
     * Select picture, from album.
     */
    private void selectImage5() {
        Album.image(this)
                .multipleChoice()
                .camera(true)
                //選擇欄位數量
                .columnCount(2)
                //可選欄位數量
                .selectCount(1)
                .checkedList(mAlbumFiles5)

                .onResult(new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(@NonNull ArrayList<AlbumFile> result) {
                        mAlbumFiles5 = result;
                        mAdapter5.notifyDataSetChanged(mAlbumFiles5);
                        image_view5.setVisibility(result.size() > 0 ? View.GONE : View.VISIBLE);
                        Log.e(LOG, result.toString());
                    }
                })
                .onCancel(new Action<String>() {
                    @Override
                    public void onAction(@NonNull String result) {
                        Toast.makeText(MissionReportActivity.this, R.string.canceled, Toast.LENGTH_LONG).show();
                    }
                })
                .start();
    }

    /**
     * Preview image, to album.
     */
    private void previewImage5(int position) {
        if (mAlbumFiles5 == null || mAlbumFiles5.size() == 0) {
            Toast.makeText(this, R.string.no_selected, Toast.LENGTH_LONG).show();
        } else {
            Album.galleryAlbum(this)
                    .checkable(true)
                    .checkedList(mAlbumFiles5)
                    .currentPosition(position)

                    .onResult(new Action<ArrayList<AlbumFile>>() {
                        @Override
                        public void onAction(@NonNull ArrayList<AlbumFile> result) {
                            mAlbumFiles5 = result;
                            mAdapter5.notifyDataSetChanged(mAlbumFiles5);
                            image_view5.setVisibility(result.size() > 0 ? View.GONE : View.VISIBLE);

                            Log.e(LOG + "123", mAlbumFiles5.toString());
                        }
                    })
                    .start();
        }
    }

    /**
     * 照片選擇器
     */
    private void AlbumGlide6() {
        recyclerView6.setLayoutManager(new GridLayoutManager(this, 1));//預覽欄位數量
        Divider divider = new Api21ItemDivider(Color.TRANSPARENT, 10, 10);
        recyclerView6.addItemDecoration(divider);

        mAdapter6 = new Adapter(this, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                previewImage6(position);
            }
        });

        recyclerView6.setAdapter(mAdapter6);

        Log.e(LOG + "111", mAdapter6.toString());
    }

    /**
     * Select picture, from album.
     */
    private void selectImage6() {
        Album.image(this)
                .multipleChoice()
                .camera(true)
                //選擇欄位數量
                .columnCount(2)
                //可選欄位數量
                .selectCount(1)
                .checkedList(mAlbumFiles6)

                .onResult(new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(@NonNull ArrayList<AlbumFile> result) {
                        mAlbumFiles6 = result;
                        mAdapter6.notifyDataSetChanged(mAlbumFiles6);
                        image_view6.setVisibility(result.size() > 0 ? View.GONE : View.VISIBLE);
                        Log.e(LOG, result.toString());
                    }
                })
                .onCancel(new Action<String>() {
                    @Override
                    public void onAction(@NonNull String result) {
                        Toast.makeText(MissionReportActivity.this, R.string.canceled, Toast.LENGTH_LONG).show();
                    }
                })
                .start();
    }

    /**
     * Preview image, to album.
     */
    private void previewImage6(int position) {
        if (mAlbumFiles6 == null || mAlbumFiles6.size() == 0) {
            Toast.makeText(this, R.string.no_selected, Toast.LENGTH_LONG).show();
        } else {
            Album.galleryAlbum(this)
                    .checkable(true)
                    .checkedList(mAlbumFiles6)
                    .currentPosition(position)

                    .onResult(new Action<ArrayList<AlbumFile>>() {
                        @Override
                        public void onAction(@NonNull ArrayList<AlbumFile> result) {
                            mAlbumFiles6 = result;
                            mAdapter6.notifyDataSetChanged(mAlbumFiles6);
                            image_view6.setVisibility(result.size() > 0 ? View.GONE : View.VISIBLE);

                            Log.e(LOG + "123", mAlbumFiles6.toString());
                        }
                    })
                    .start();
        }
    }

    /**
     * 照片選擇器
     */
    private void AlbumGlide7() {
        recyclerView7.setLayoutManager(new GridLayoutManager(this, 1));//預覽欄位數量
        Divider divider = new Api21ItemDivider(Color.TRANSPARENT, 10, 10);
        recyclerView7.addItemDecoration(divider);

        mAdapter7 = new Adapter(this, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                previewImage7(position);
            }
        });

        recyclerView7.setAdapter(mAdapter7);

        Log.e(LOG + "111", mAdapter7.toString());
    }

    /**
     * Select picture, from album.
     */
    private void selectImage7() {
        Album.image(this)
                .multipleChoice()
                .camera(true)
                //選擇欄位數量
                .columnCount(2)
                //可選欄位數量
                .selectCount(1)
                .checkedList(mAlbumFiles7)

                .onResult(new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(@NonNull ArrayList<AlbumFile> result) {
                        mAlbumFiles7 = result;
                        mAdapter7.notifyDataSetChanged(mAlbumFiles7);
                        image_view7.setVisibility(result.size() > 0 ? View.GONE : View.VISIBLE);
                        Log.e(LOG, result.toString());
                    }
                })
                .onCancel(new Action<String>() {
                    @Override
                    public void onAction(@NonNull String result) {
                        Toast.makeText(MissionReportActivity.this, R.string.canceled, Toast.LENGTH_LONG).show();
                    }
                })
                .start();
    }

    /**
     * Preview image, to album.
     */
    private void previewImage7(int position) {
        if (mAlbumFiles7 == null || mAlbumFiles7.size() == 0) {
            Toast.makeText(this, R.string.no_selected, Toast.LENGTH_LONG).show();
        } else {
            Album.galleryAlbum(this)
                    .checkable(true)
                    .checkedList(mAlbumFiles7)
                    .currentPosition(position)

                    .onResult(new Action<ArrayList<AlbumFile>>() {
                        @Override
                        public void onAction(@NonNull ArrayList<AlbumFile> result) {
                            mAlbumFiles7 = result;
                            mAdapter7.notifyDataSetChanged(mAlbumFiles7);
                            image_view7.setVisibility(result.size() > 0 ? View.GONE : View.VISIBLE);

                            Log.e(LOG + "123", mAlbumFiles7.toString());
                        }
                    })
                    .start();
        }
    }

    /**
     * 照片選擇器
     */
    private void AlbumGlide8() {
        recyclerView8.setLayoutManager(new GridLayoutManager(this, 1));//預覽欄位數量
        Divider divider = new Api21ItemDivider(Color.TRANSPARENT, 10, 10);
        recyclerView8.addItemDecoration(divider);

        mAdapter8 = new Adapter(this, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                previewImage8(position);
            }
        });

        recyclerView8.setAdapter(mAdapter8);

        Log.e(LOG + "111", mAdapter8.toString());
    }

    /**
     * Select picture, from album.
     */
    private void selectImage8() {
        Album.image(this)
                .multipleChoice()
                .camera(true)
                //選擇欄位數量
                .columnCount(2)
                //可選欄位數量
                .selectCount(1)
                .checkedList(mAlbumFiles8)

                .onResult(new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(@NonNull ArrayList<AlbumFile> result) {
                        mAlbumFiles8 = result;
                        mAdapter8.notifyDataSetChanged(mAlbumFiles8);
                        image_view8.setVisibility(result.size() > 0 ? View.GONE : View.VISIBLE);
                        Log.e(LOG, result.toString());
                    }
                })
                .onCancel(new Action<String>() {
                    @Override
                    public void onAction(@NonNull String result) {
                        Toast.makeText(MissionReportActivity.this, R.string.canceled, Toast.LENGTH_LONG).show();
                    }
                })
                .start();
    }

    /**
     * Preview image, to album.
     */
    private void previewImage8(int position) {
        if (mAlbumFiles8 == null || mAlbumFiles8.size() == 0) {
            Toast.makeText(this, R.string.no_selected, Toast.LENGTH_LONG).show();
        } else {
            Album.galleryAlbum(this)
                    .checkable(true)
                    .checkedList(mAlbumFiles8)
                    .currentPosition(position)

                    .onResult(new Action<ArrayList<AlbumFile>>() {
                        @Override
                        public void onAction(@NonNull ArrayList<AlbumFile> result) {
                            mAlbumFiles8 = result;
                            mAdapter8.notifyDataSetChanged(mAlbumFiles8);
                            image_view8.setVisibility(result.size() > 0 ? View.GONE : View.VISIBLE);

                            Log.e(LOG + "123", mAlbumFiles8.toString());
                        }
                    })
                    .start();
        }
    }

    /**
     * 照片選擇器
     */
    private void AlbumGlide9() {
        recyclerView9.setLayoutManager(new GridLayoutManager(this, 1));//預覽欄位數量
        Divider divider = new Api21ItemDivider(Color.TRANSPARENT, 10, 10);
        recyclerView9.addItemDecoration(divider);

        mAdapter9 = new Adapter(this, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                previewImage9(position);
            }
        });

        recyclerView9.setAdapter(mAdapter9);

        Log.e(LOG + "111", mAdapter9.toString());
    }

    /**
     * Select picture, from album.
     */
    private void selectImage9() {
        Album.image(this)
                .multipleChoice()
                .camera(true)
                //選擇欄位數量
                .columnCount(2)
                //可選欄位數量
                .selectCount(1)
                .checkedList(mAlbumFiles9)

                .onResult(new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(@NonNull ArrayList<AlbumFile> result) {
                        mAlbumFiles9 = result;
                        mAdapter9.notifyDataSetChanged(mAlbumFiles9);
                        image_view9.setVisibility(result.size() > 0 ? View.GONE : View.VISIBLE);
                        Log.e(LOG, result.toString());
                    }
                })
                .onCancel(new Action<String>() {
                    @Override
                    public void onAction(@NonNull String result) {
                        Toast.makeText(MissionReportActivity.this, R.string.canceled, Toast.LENGTH_LONG).show();
                    }
                })
                .start();
    }

    /**
     * Preview image, to album.
     */
    private void previewImage9(int position) {
        if (mAlbumFiles9 == null || mAlbumFiles9.size() == 0) {
            Toast.makeText(this, R.string.no_selected, Toast.LENGTH_LONG).show();
        } else {
            Album.galleryAlbum(this)
                    .checkable(true)
                    .checkedList(mAlbumFiles9)
                    .currentPosition(position)

                    .onResult(new Action<ArrayList<AlbumFile>>() {
                        @Override
                        public void onAction(@NonNull ArrayList<AlbumFile> result) {
                            mAlbumFiles9 = result;
                            mAdapter9.notifyDataSetChanged(mAlbumFiles9);
                            image_view9.setVisibility(result.size() > 0 ? View.GONE : View.VISIBLE);

                            Log.e(LOG + "123", mAlbumFiles9.toString());
                        }
                    })
                    .start();
        }
    }

    /**
     * 上傳照片回Server
     */
    private void uploadImage1() {
        photo_client = new OkHttpClient();
        MultipartBody.Builder builder1 = new MultipartBody.Builder().setType(MultipartBody.FORM);

        //取得檔案路徑及檔案名稱並上傳至SERVER
        //GET the file path
        AlbumFile file1 = mAlbumFiles1.get(0);
        path1 = file1.getPath();

        Log.e(LOG + "888", "1." + path1);

        image_file1 = new File(path1);
        builder1.addFormDataPart("img", "m_pic_" + user_id_data + "_" + rm001 + "_" + rm002 + "_" + RM003 + "_1_" + image_file1.getName(), RequestBody.create(MEDIA_TYPE_PNG, image_file1));

        Log.e(LOG + "8888", image_file1.getName());

        String fileName = "C:\\xampp\\htdocs\\WQP_OS\\mission_report_pic\\";
        //savePath = savePath + fileName + image_file1.getName() + ",";
        savePath1 = fileName + "m_pic_" + user_id_data + "_" + rm001 + "_" + rm002 + "_" + RM003 + "_1_" + image_file1.getName();

        Log.e(LOG + "BBB", "1. " + savePath1);

        // TODO upload...
        MultipartBody requestBody1 = builder1.build();
        Request request1 = new Request.Builder()
                //.url("http://192.168.0.172/WQP/MissionReportPicture.php")
                .url("http://a.wqp-water.com.tw/WQP_OS/MissionReportPicture.php")
                .post(requestBody1)
                .build();

        photo_client.newCall(request1).enqueue(new Callback() {
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

        Log.e(LOG + "BBBB", "1. " + savePath1);

    }

    /**
     * 上傳照片回Server
     */
    private void uploadImage2() {
        photo_client = new OkHttpClient();
        MultipartBody.Builder builder2 = new MultipartBody.Builder().setType(MultipartBody.FORM);

        //取得檔案路徑及檔案名稱並上傳至SERVER
        //GET the file path
        AlbumFile file2 = mAlbumFiles2.get(0);
        path2 = file2.getPath();

        Log.e(LOG + "888", "2." + path2);

        image_file2 = new File(path2);
        builder2.addFormDataPart("img", "m_pic_" + user_id_data + "_" + rm001 + "_" + rm002 + "_" + RM003 + "_2_" + image_file2.getName(), RequestBody.create(MEDIA_TYPE_PNG, image_file2));

        Log.e(LOG + "8888", image_file2.getName());

        String fileName = "C:\\xampp\\htdocs\\WQP\\mission_report_pic\\";
        //savePath = savePath + fileName + image_file1.getName() + ",";
        savePath2 = fileName + "m_pic_" + user_id_data + "_" + rm001 + "_" + rm002 + "_" + RM003 + "_2_" + image_file2.getName();

        Log.e(LOG + "BBB", "2. " + savePath2);

        MultipartBody requestBody2 = builder2.build();
        Request request2 = new Request.Builder()
                //.url("http://192.168.0.172/WQP/MissionReportPicture.php")
                .url("http://a.wqp-water.com.tw/WQP_OS/MissionReportPicture.php")
                .post(requestBody2)
                .build();

        photo_client.newCall(request2).enqueue(new Callback() {
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

        Log.e(LOG + "BBBB", "2. " + savePath2);

    }

    /**
     * 上傳照片回Server
     */
    private void uploadImage3() {
        photo_client = new OkHttpClient();
        MultipartBody.Builder builder3 = new MultipartBody.Builder().setType(MultipartBody.FORM);

        //取得檔案路徑及檔案名稱並上傳至SERVER
        //GET the file path
        AlbumFile file3 = mAlbumFiles3.get(0);
        path3 = file3.getPath();

        Log.e(LOG + "888", "3." + path3);

        image_file3 = new File(path3);
        builder3.addFormDataPart("img", "m_pic_" + user_id_data + "_" + rm001 + "_" + rm002 + "_" + RM003 + "_3_" + image_file3.getName(), RequestBody.create(MEDIA_TYPE_PNG, image_file3));

        Log.e(LOG + "8888", image_file3.getName());

        String fileName = "C:\\xampp\\htdocs\\WQP\\mission_report_pic\\";
        //savePath = savePath + fileName + image_file1.getName() + ",";
        savePath3 = fileName + "m_pic_" + user_id_data + "_" + rm001 + "_" + rm002 + "_" + RM003 + "_3_" + image_file3.getName();

        Log.e(LOG + "BBB", "3. " + savePath3);

        // TODO upload...
        MultipartBody requestBody3 = builder3.build();
        Request request3 = new Request.Builder()
                //.url("http://192.168.0.172/WQP/MissionReportPicture.php")
                .url("http://a.wqp-water.com.tw/WQP_OS/MissionReportPicture.php")
                .post(requestBody3)
                .build();

        photo_client.newCall(request3).enqueue(new Callback() {
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

        Log.e(LOG + "BBBB", "3. " + savePath3);

    }

    /**
     * 上傳照片回Server
     */
    private void uploadImage4() {
        photo_client = new OkHttpClient();
        MultipartBody.Builder builder4 = new MultipartBody.Builder().setType(MultipartBody.FORM);

        //取得檔案路徑及檔案名稱並上傳至SERVER
        //GET the file path
        AlbumFile file4 = mAlbumFiles4.get(0);
        path4 = file4.getPath();

        Log.e(LOG + "888", "4." + path4);

        image_file4 = new File(path4);
        builder4.addFormDataPart("img", "m_pic_" + user_id_data + "_" + rm001 + "_" + rm002 + "_" + RM003 + "_4_" + image_file4.getName(), RequestBody.create(MEDIA_TYPE_PNG, image_file4));

        Log.e(LOG + "8888", image_file4.getName());

        String fileName = "C:\\xampp\\htdocs\\WQP\\mission_report_pic\\";
        //savePath = savePath + fileName + image_file1.getName() + ",";
        savePath4 = fileName + "m_pic_" + user_id_data + "_" + rm001 + "_" + rm002 + "_" + RM003 + "_4_" + image_file4.getName();

        Log.e(LOG + "BBB", "4. " + savePath4);

        // TODO upload...
        MultipartBody requestBody4 = builder4.build();
        Request request4 = new Request.Builder()
                //.url("http://192.168.0.172/WQP/MissionReportPicture.php")
                .url("http://a.wqp-water.com.tw/WQP_OS/MissionReportPicture.php")
                .post(requestBody4)
                .build();

        photo_client.newCall(request4).enqueue(new Callback() {
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

        Log.e(LOG + "BBBB", "4. " + savePath4);

    }

    /**
     * 上傳照片回Server
     */
    private void uploadImage5() {
        photo_client = new OkHttpClient();
        MultipartBody.Builder builder5 = new MultipartBody.Builder().setType(MultipartBody.FORM);

        //取得檔案路徑及檔案名稱並上傳至SERVER
        //GET the file path
        AlbumFile file5 = mAlbumFiles5.get(0);
        path5 = file5.getPath();

        Log.e(LOG + "888", " ,5." + path5);

        image_file5 = new File(path5);
        builder5.addFormDataPart("img", "m_pic_" + user_id_data + "_" + rm001 + "_" + rm002 + "_" + RM003 + "_5_" + image_file5.getName(), RequestBody.create(MEDIA_TYPE_PNG, image_file5));

        Log.e(LOG + "8888", image_file5.getName());

        String fileName = "C:\\xampp\\htdocs\\WQP\\mission_report_pic\\";
        //savePath = savePath + fileName + image_file1.getName() + ",";
        savePath5 = fileName + "m_pic_" + user_id_data + "_" + rm001 + "_" + rm002 + "_" + RM003 + "_5_" + image_file5.getName();

        Log.e(LOG + "BBB", "5. " + savePath5);

        // TODO upload...
        MultipartBody requestBody5 = builder5.build();
        Request request5 = new Request.Builder()
                //.url("http://192.168.0.172/WQP/MissionReportPicture.php")
                .url("http://a.wqp-water.com.tw/WQP_OS/MissionReportPicture.php")
                .post(requestBody5)
                .build();

        photo_client.newCall(request5).enqueue(new Callback() {
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

        Log.e(LOG + "BBBB", "5. " + savePath5);

    }

    /**
     * 上傳照片回Server
     */
    private void uploadImage6() {
        photo_client = new OkHttpClient();
        MultipartBody.Builder builder6 = new MultipartBody.Builder().setType(MultipartBody.FORM);

        //取得檔案路徑及檔案名稱並上傳至SERVER
        //GET the file path
        AlbumFile file6 = mAlbumFiles6.get(0);
        path6 = file6.getPath();

        Log.e(LOG + "888", "6." + path6);

        image_file6 = new File(path6);
        builder6.addFormDataPart("img", "m_pic_" + user_id_data + "_" + rm001 + "_" + rm002 + "_" + RM003 + "_6_" + image_file6.getName(), RequestBody.create(MEDIA_TYPE_PNG, image_file6));

        Log.e(LOG + "8888", image_file6.getName());

        String fileName = "C:\\xampp\\htdocs\\WQP\\mission_report_pic\\";
        //savePath = savePath + fileName + image_file1.getName() + ",";
        savePath6 = fileName + "m_pic_" + user_id_data + "_" + rm001 + "_" + rm002 + "_" + RM003 + "_6_" + image_file6.getName();

        Log.e(LOG + "BBB", "6. " + savePath6);

        // TODO upload...
        MultipartBody requestBody6 = builder6.build();
        Request request6 = new Request.Builder()
                //.url("http://192.168.0.172/WQP/MissionReportPicture.php")
                .url("http://a.wqp-water.com.tw/WQP_OS/MissionReportPicture.php")
                .post(requestBody6)
                .build();

        photo_client.newCall(request6).enqueue(new Callback() {
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

        Log.e(LOG + "BBBB", "6. " + savePath6);

    }

    /**
     * 上傳照片回Server
     */
    private void uploadImage7() {
        photo_client = new OkHttpClient();
        MultipartBody.Builder builder7 = new MultipartBody.Builder().setType(MultipartBody.FORM);

        //取得檔案路徑及檔案名稱並上傳至SERVER
        //GET the file path

        AlbumFile file7 = mAlbumFiles7.get(0);
        path7 = file7.getPath();

        Log.e(LOG + "888", "7." + path7);

        image_file7 = new File(path7);
        builder7.addFormDataPart("img", "m_pic_" + user_id_data + "_" + rm001 + "_" + rm002 + "_" + RM003 + "_7_" + image_file7.getName(), RequestBody.create(MEDIA_TYPE_PNG, image_file7));

        Log.e(LOG + "8888", image_file7.getName());

        String fileName = "C:\\xampp\\htdocs\\WQP\\mission_report_pic\\";
        //savePath = savePath + fileName + image_file1.getName() + ",";
        savePath7 = fileName + "m_pic_" + user_id_data + "_" + rm001 + "_" + rm002 + "_" + RM003 + "_7_" + image_file7.getName();

        Log.e(LOG + "BBB", "7. " + savePath7);

        // TODO upload...
        MultipartBody requestBody7 = builder7.build();
        Request request7 = new Request.Builder()
                //.url("http://192.168.0.172/WQP/MissionReportPicture.php")
                .url("http://a.wqp-water.com.tw/WQP_OS/MissionReportPicture.php")
                .post(requestBody7)
                .build();

        photo_client.newCall(request7).enqueue(new Callback() {
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

        Log.e(LOG + "BBBB", "7. " + savePath7);

    }

    /**
     * 上傳照片回Server
     */
    private void uploadImage8() {
        photo_client = new OkHttpClient();
        MultipartBody.Builder builder8 = new MultipartBody.Builder().setType(MultipartBody.FORM);

        //取得檔案路徑及檔案名稱並上傳至SERVER
        //GET the file path
        AlbumFile file8 = mAlbumFiles8.get(0);
        path8 = file8.getPath();


        Log.e(LOG + "888", "8." + path8);

        image_file8 = new File(path8);
        builder8.addFormDataPart("img", "m_pic_" + user_id_data + "_" + rm001 + "_" + rm002 + "_" + RM003 + "_8_" + image_file8.getName(), RequestBody.create(MEDIA_TYPE_PNG, image_file8));

        Log.e(LOG + "8888", image_file8.getName());

        String fileName = "C:\\xampp\\htdocs\\WQP\\mission_report_pic\\";
        //savePath = savePath + fileName + image_file1.getName() + ",";

        savePath8 = fileName + "m_pic_" + user_id_data + "_" + rm001 + "_" + rm002 + "_" + RM003 + "_8_" + image_file8.getName();

        Log.e(LOG + "BBB", "8. " + savePath8);

        // TODO upload...
        MultipartBody requestBody8 = builder8.build();
        Request request8 = new Request.Builder()
                //.url("http://192.168.0.172/WQP/MissionReportPicture.php")
                .url("http://a.wqp-water.com.tw/WQP_OS/MissionReportPicture.php")
                .post(requestBody8)
                .build();

        photo_client.newCall(request8).enqueue(new Callback() {
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

        Log.e(LOG + "BBBB", "8. " + savePath8);

    }

    /**
     * 上傳照片回Server
     */
    private void uploadImage9() {
        photo_client = new OkHttpClient();
        MultipartBody.Builder builder9 = new MultipartBody.Builder().setType(MultipartBody.FORM);

        //取得檔案路徑及檔案名稱並上傳至SERVER
        //GET the file path
        AlbumFile file9 = mAlbumFiles9.get(0);
        path9 = file9.getPath();


        Log.e(LOG + "888", "8." + path9);

        image_file9 = new File(path9);
        builder9.addFormDataPart("img", "m_pic_" + user_id_data + "_" + rm001 + "_" + rm002 + "_" + RM003 + "_9_" + image_file9.getName(), RequestBody.create(MEDIA_TYPE_PNG, image_file9));

        Log.e(LOG + "8888", image_file9.getName());

        String fileName = "C:\\xampp\\htdocs\\WQP\\mission_report_pic\\";
        //savePath = savePath + fileName + image_file1.getName() + ",";

        savePath9 = fileName + "m_pic_" + user_id_data + "_" + rm001 + "_" + rm002 + "_" + RM003 + "_9_" + image_file9.getName();

        Log.e(LOG + "BBB", "9. " + savePath9);

        // TODO upload...
        MultipartBody requestBody9 = builder9.build();
        Request request9 = new Request.Builder()
                //.url("http://192.168.0.172/WQP/MissionReportPicture.php")
                .url("http://a.wqp-water.com.tw/WQP_OS/MissionReportPicture.php")
                .post(requestBody9)
                .build();

        photo_client.newCall(request9).enqueue(new Callback() {
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

        Log.e(LOG + "BBBB", "9. " + savePath9);

    }

    /**
     * 與OkHttp建立連線(上傳照片路徑)
     */
    private void sendRequestWithOkHttpForMissionPhoto() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //接收LoginActivity傳過來的值
                SharedPreferences user_id = getSharedPreferences("user_id", MODE_PRIVATE);
                user_id_data = user_id.getString("ID", "");
                //接收MissionActivity傳過來的值
                Bundle bundle = getIntent().getExtras();
                rm001 = bundle.getString("rm001").trim();
                rm002 = bundle.getString("rm002").trim();

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("U_ACC", user_id_data)
                            .add("RM002", rm002)
                            .add("RM003", RM003)
                            .add("FILE_NAME1", savePath1)
                            .add("FILE_NAME2", savePath2)
                            .add("FILE_NAME3", savePath3)
                            .add("FILE_NAME4", savePath4)
                            .add("FILE_NAME5", savePath5)
                            .add("FILE_NAME6", savePath6)
                            .add("FILE_NAME7", savePath7)
                            .add("FILE_NAME8", savePath8)
                            .add("FILE_NAME9", savePath9)
                            .add("RM001", rm001)
                            .build();
                    Log.e(LOG, user_id_data);
                    Request request = new Request.Builder()
                            //.url("http://192.168.0.172/WQP_OS/MissionReportFile.php")
                            .url("http://a.wqp-water.com.tw/WQP_OS/MissionReportFile.php")
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
     * 與OkHttp建立連線(任務回報-抵達目的)
     */
    private void sendRequestWithOkHttpForMissionReportArrive() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //接收LoginActivity傳過來的值
                SharedPreferences user_id = getSharedPreferences("user_id", MODE_PRIVATE);
                user_id_data = user_id.getString("ID", "");
                //接收MissionActivity傳過來的值
                Bundle bundle = getIntent().getExtras();
                rm001 = bundle.getString("rm001").trim();
                rm002 = bundle.getString("rm002").trim();

                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                //利用locationManager.getLastKnownLocation取得當下的位置資料
                if (ActivityCompat.checkSelfPermission(MissionReportActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(MissionReportActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    ActivityCompat.requestPermissions(MissionReportActivity.this,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            MY_PERMISSION_ACCESS_LOCATION);
                    return;
                }

                //定義位置監聽器，接收來自LocationManager的通知。
                locationListener = new LocationListener() {
                    //位置管理服務利用requestLocationUpdates(String , long , float , LocationListener)方法註冊位置監聽器後，這些方法就會被呼叫。
                    //Provider狀態改變時呼叫此方法。
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    //位置改變時呼叫此方法。
                    @Override
                    public void onLocationChanged(Location location) {
                        // TODO Auto-generated method stub
                        System.out.println("onLocationChanged");
                    }

                    //用戶關閉provider時呼叫此方法。
                    @Override
                    public void onProviderDisabled(String provider) {
                        // TODO Auto-generated method stub
                        System.out.println("onProviderDisabled");

                    }

                    //用戶啟動provider時呼叫此方法。
                    @Override
                    public void onProviderEnabled(String provider) {
                        // TODO Auto-generated method stub
                        System.out.println("onProviderEnabled");
                    }
                };

                //Location location = locationManager.getLastKnownLocation(commandStr);
                LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener, Looper.getMainLooper());

                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    GPS = latitude + ", " + longitude;
                    Log.e(LOG, " 緯度 : " + location.getLatitude() + "經度 : " + location.getLongitude());
                }
                String report_GPS = latitude + ", " + longitude;

                ML008 = eng_report_edt.getText().toString();
                RM015 = Long.toString(pay_spinner.getSelectedItemId());
                if (is_get_money_checkBox.isChecked()) {
                    RM016 = "1";
                } else {
                    RM016 = "0";
                }
                RM017 = receivable_money_txt.getText().toString();
                if (have_get_money_checkBox.isChecked()) {
                    RM018 = "1";
                } else if (not_get_money_checkBox.isChecked()) {
                    RM018 = "0";
                }
                RM019 = eng_money_edt.getText().toString();
                RM020 = Long.toString(unless_spinner.getSelectedItemId());
                RM021 = Long.toString(censor_spinner.getSelectedItemId());

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("U_ACC", user_id_data)
                            .add("RM002", rm002)
                            .add("GPS", report_GPS)
                            .add("ML008", ML008)
                            .add("RM015", RM015)
                            .add("RM016", RM016)
                            .add("RM017", RM017)
                            .add("RM018", RM018)
                            .add("RM019", RM019)
                            .add("RM020", RM020)
                            .add("RM021", RM021)
                            .build();
                    Log.e(LOG, user_id_data);
                    Log.e(LOG, rm002);
                    Log.e(LOG, report_GPS);
                    Log.e(LOG, ML008);
                    Log.e(LOG, RM015);
                    Log.e(LOG, RM016);
                    Log.e(LOG, RM017);
                    Log.e(LOG, RM018);
                    Log.e(LOG, RM019);
                    Log.e(LOG, RM020);
                    Log.e(LOG, RM021);
                    Request request = new Request.Builder()
                            //.url("http://192.168.0.172/WQP_OS/MissionReportArrive.php")
                            .url("http://a.wqp-water.com.tw/WQP_OS/MissionReportArrive.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, request.toString());
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
     * 與OkHttp建立連線(任務回報-離開目的)
     */
    private void sendRequestWithOkHttpForMissionReportLeave() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //接收LoginActivity傳過來的值
                SharedPreferences user_id = getSharedPreferences("user_id", MODE_PRIVATE);
                user_id_data = user_id.getString("ID", "");
                //接收MissionActivity傳過來的值
                Bundle bundle = getIntent().getExtras();
                rm001 = bundle.getString("rm001").trim();
                rm002 = bundle.getString("rm002").trim();

                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                //利用locationManager.getLastKnownLocation取得當下的位置資料
                if (ActivityCompat.checkSelfPermission(MissionReportActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(MissionReportActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    ActivityCompat.requestPermissions(MissionReportActivity.this,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            MY_PERMISSION_ACCESS_LOCATION);
                    return;
                }

                //定義位置監聽器，接收來自LocationManager的通知。
                locationListener = new LocationListener() {
                    //位置管理服務利用requestLocationUpdates(String , long , float , LocationListener)方法註冊位置監聽器後，這些方法就會被呼叫。
                    //Provider狀態改變時呼叫此方法。
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    //位置改變時呼叫此方法。
                    @Override
                    public void onLocationChanged(Location location) {
                        // TODO Auto-generated method stub
                        System.out.println("onLocationChanged");
                    }

                    //用戶關閉provider時呼叫此方法。
                    @Override
                    public void onProviderDisabled(String provider) {
                        // TODO Auto-generated method stub
                        System.out.println("onProviderDisabled");

                    }

                    //用戶啟動provider時呼叫此方法。
                    @Override
                    public void onProviderEnabled(String provider) {
                        // TODO Auto-generated method stub
                        System.out.println("onProviderEnabled");
                    }
                };

                //Location location = locationManager.getLastKnownLocation(commandStr);
                LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener, Looper.getMainLooper());

                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    GPS = latitude + ", " + longitude;
                    //Log.e(LOG, " 緯度 : " + location.getLatitude() + "經度 : " + location.getLongitude());
                }
                String report_GPS = latitude + ", " + longitude;

                ML008 = eng_report_edt.getText().toString();
                RM015 = Long.toString(pay_spinner.getSelectedItemId());
                if (is_get_money_checkBox.isChecked()) {
                    RM016 = "1";
                } else {
                    RM016 = "0";
                }
                RM017 = receivable_money_txt.getText().toString();
                if (have_get_money_checkBox.isChecked()) {
                    RM018 = "1";
                } else if (not_get_money_checkBox.isChecked()) {
                    RM018 = "0";
                }
                RM019 = eng_money_edt.getText().toString();
                RM020 = Long.toString(unless_spinner.getSelectedItemId());
                RM021 = Long.toString(censor_spinner.getSelectedItemId());

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("U_ACC", user_id_data)
                            .add("RM002", rm002)
                            .add("GPS", report_GPS)
                            .add("ML008", ML008)
                            .add("RM015", RM015)
                            .add("RM016", RM016)
                            .add("RM017", RM017)
                            .add("RM018", RM018)
                            .add("RM019", RM019)
                            .add("RM020", RM020)
                            .add("RM021", RM021)
                            .build();
                    Log.e(LOG, user_id_data);
                    Log.e(LOG, "RM002 " + rm002);
                    Log.e(LOG, "GPS " + report_GPS);
                    Log.e(LOG, ML008);
                    Log.e(LOG, RM015);
                    Log.e(LOG, RM016);
                    Log.e(LOG, RM017);
                    Log.e(LOG, RM018);
                    Log.e(LOG, RM019);
                    Log.e(LOG, RM020);
                    Log.e(LOG, RM021);
                    Request request = new Request.Builder()
                            //.url("http://192.168.0.172/WQP_OS/MissionReportLeave.php")
                            .url("http://a.wqp-water.com.tw/WQP_OS/MissionReportLeave.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, request.toString());
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
     * 與OkHttp建立連線(任務回報-離開目的-無效派工)
     */
    private void sendRequestWithOkHttpForMissionReportUnless() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //接收LoginActivity傳過來的值
                SharedPreferences user_id = getSharedPreferences("user_id", MODE_PRIVATE);
                user_id_data = user_id.getString("ID", "");
                //接收MissionActivity傳過來的值
                Bundle bundle = getIntent().getExtras();
                rm001 = bundle.getString("rm001").trim();
                rm002 = bundle.getString("rm002").trim();

                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                //利用locationManager.getLastKnownLocation取得當下的位置資料
                if (ActivityCompat.checkSelfPermission(MissionReportActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(MissionReportActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    ActivityCompat.requestPermissions(MissionReportActivity.this,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            MY_PERMISSION_ACCESS_LOCATION);
                    return;
                }

                //定義位置監聽器，接收來自LocationManager的通知。
                locationListener = new LocationListener() {
                    //位置管理服務利用requestLocationUpdates(String , long , float , LocationListener)方法註冊位置監聽器後，這些方法就會被呼叫。
                    //Provider狀態改變時呼叫此方法。
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    //位置改變時呼叫此方法。
                    @Override
                    public void onLocationChanged(Location location) {
                        // TODO Auto-generated method stub
                        System.out.println("onLocationChanged");
                    }

                    //用戶關閉provider時呼叫此方法。
                    @Override
                    public void onProviderDisabled(String provider) {
                        // TODO Auto-generated method stub
                        System.out.println("onProviderDisabled");

                    }

                    //用戶啟動provider時呼叫此方法。
                    @Override
                    public void onProviderEnabled(String provider) {
                        // TODO Auto-generated method stub
                        System.out.println("onProviderEnabled");
                    }
                };

                //Location location = locationManager.getLastKnownLocation(commandStr);
                LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener, Looper.getMainLooper());

                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    GPS = latitude + ", " + longitude;
                    Log.e(LOG, " 緯度 : " + location.getLatitude() + "經度 : " + location.getLongitude());
                }
                String report_GPS = latitude + ", " + longitude;

                ML008 = eng_report_edt.getText().toString();
                RM015 = Long.toString(pay_spinner.getSelectedItemId());
                if (is_get_money_checkBox.isChecked()) {
                    RM016 = "1";
                } else {
                    RM016 = "0";
                }
                RM017 = receivable_money_txt.getText().toString();
                if (have_get_money_checkBox.isChecked()) {
                    RM018 = "1";
                } else if (not_get_money_checkBox.isChecked()) {
                    RM018 = "0";
                }
                RM019 = eng_money_edt.getText().toString();
                RM020 = Long.toString(unless_spinner.getSelectedItemId());
                RM021 = Long.toString(censor_spinner.getSelectedItemId());

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("U_ACC", user_id_data)
                            .add("RM002", rm002)
                            .add("GPS", report_GPS)
                            .add("ML008", ML008)
                            .add("RM015", RM015)
                            .add("RM016", RM016)
                            .add("RM017", RM017)
                            .add("RM018", RM018)
                            .add("RM019", RM019)
                            .add("RM020", RM020)
                            .add("RM021", RM021)
                            .build();
                    Log.e(LOG, user_id_data);
                    Log.e(LOG, rm002);
                    Log.e(LOG, report_GPS);
                    Log.e(LOG, ML008);
                    Log.e(LOG, RM015);
                    Log.e(LOG, RM016);
                    Log.e(LOG, RM017);
                    Log.e(LOG, RM018);
                    Log.e(LOG, RM019);
                    Log.e(LOG, RM020);
                    Log.e(LOG, RM021);
                    Request request = new Request.Builder()
                            //.url("http://192.168.0.172/WQP_OS/MissionReportUnless.php")
                            .url("http://a.wqp-water.com.tw/WQP_OS/MissionReportUnless.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, request.toString());
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
     * 抓取設備GPS位置
     */
    public void GPSLocation() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //利用locationManager.getLastKnownLocation取得當下的位置資料
        if (ActivityCompat.checkSelfPermission(MissionReportActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MissionReportActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(MissionReportActivity.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSION_ACCESS_LOCATION);
            return;
        }

        //定義位置監聽器，接收來自LocationManager的通知。
        locationListener = new LocationListener() {
            //位置管理服務利用requestLocationUpdates(String , long , float , LocationListener)方法註冊位置監聽器後，這些方法就會被呼叫。
            //Provider狀態改變時呼叫此方法。
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            //位置改變時呼叫此方法。
            @Override
            public void onLocationChanged(Location location) {
                // TODO Auto-generated method stub
                System.out.println("onLocationChanged");
            }

            //用戶關閉provider時呼叫此方法。
            @Override
            public void onProviderDisabled(String provider) {
                // TODO Auto-generated method stub
                System.out.println("onProviderDisabled");

            }

            //用戶啟動provider時呼叫此方法。
            @Override
            public void onProviderEnabled(String provider) {
                // TODO Auto-generated method stub
                System.out.println("onProviderEnabled");
            }
        };

        //Location location = locationManager.getLastKnownLocation(commandStr);
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);

        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            GPS = latitude + ", " + longitude;
            Log.e(LOG, " 緯度 : " + location.getLatitude() + "經度 : " + location.getLongitude());
            gps_txt.setText(GPS);
        }

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
        //◎LocationManager.NETWORK_PROVIDER //使用網路定位
        commandStr = LocationManager.GPS_PROVIDER;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates("gps", 60000, 1, locationListener);
        //抓取設備GPS位置
        GPSLocation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(LOG, "onPause");
        locationManager.removeUpdates(locationListener);
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