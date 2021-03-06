package com.example.a10609516.wqp_internal_app.DepartmentAndDIY;

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
import com.example.a10609516.wqp_internal_app.Tools.WQPClickListener;
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

public class StationReportActivity extends WQPToolsActivity {

    private SharedPreferences sp;

    private LinearLayout nav_view;
    private DrawerLayout drawer;
    private Toolbar toolbar;

    private LinearLayout device_llt1, device_llt2, device_llt3, device_llt4, device_llt5, device_llt6, device_llt7, device_llt8, device_llt9, device_llt10,
            device_llt11, device_llt12, device_llt13, device_llt14, device_llt15, device_llt16, device_llt17, device_llt18, device_llt19, device_llt20;
    //????????????????????? //?????????????????????
    private Spinner type_spinner, store_spinner, count_spinner, category_spinner1, supplies_spinner1,
            category_spinner2, supplies_spinner2, category_spinner3, supplies_spinner3,
            category_spinner4, supplies_spinner4, category_spinner5, supplies_spinner5,
            category_spinner6, supplies_spinner6, category_spinner7, supplies_spinner7,
            category_spinner8, supplies_spinner8, category_spinner9, supplies_spinner9,
            category_spinner10, supplies_spinner10, category_spinner11, supplies_spinner11,
            category_spinner12, supplies_spinner12, category_spinner13, supplies_spinner13,
            category_spinner14, supplies_spinner14, category_spinner15, supplies_spinner15,
            category_spinner16, supplies_spinner16, category_spinner17, supplies_spinner17,
            category_spinner18, supplies_spinner18, category_spinner19, supplies_spinner19,
            category_spinner20, supplies_spinner20, quantity_spinner;
    private TextView add_txt1, add_txt2, add_txt3, add_txt4, add_txt5, add_txt6, add_txt7, add_txt8, add_txt9, add_txt10,
            add_txt11, add_txt12, add_txt13, add_txt14, add_txt15, add_txt16, add_txt17, add_txt18, add_txt19, add_txt20, check_count_txt;
    private EditText day_performance_edt, return_count_edt, return_money_edt, reason_edt, response_edt, month_performance_edt, count_edt1, money_edt1,
            count_edt2, money_edt2, count_edt3, money_edt3, count_edt4, money_edt4, count_edt5, money_edt5, count_edt6, money_edt6,
            count_edt7, money_edt7, count_edt8, money_edt8, count_edt9, money_edt9, count_edt10, money_edt10, count_edt11, money_edt11,
            count_edt12, money_edt12, count_edt13, money_edt13, count_edt14, money_edt14, count_edt15, money_edt15, count_edt16, money_edt16,
            count_edt17, money_edt17, count_edt18, money_edt18, count_edt19, money_edt19, count_edt20, money_edt20;
    private Button date_btn, preview_btn, choice_btn, upload_btn;
    private Context context;
    private String[] type = new String[]{"?????????", "????????????", "?????????", "?????????", "?????????"};
    private String[] empty = new String[]{"(?????????)"};
    private String[] diy = new String[]{};
    private String[] count = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "30??????"};
    private String[] category = new String[]{};
    private String[] supplies = new String[]{};

    private ArrayAdapter<String> adapter, adapter2, adapter3, adapter4, adapter5;

    private Adapter mAdapter;
    private ArrayList<AlbumFile> mAlbumFiles;
    private RecyclerView recyclerView;

    private OkHttpClient photo_client;
    private String path;
    private String savePath = "";
    private File image_file;
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    private Context mContext = this;
    private String LOG = "StationReportActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_report);
        //?????????????????????
        initViews();
        //Menu???onClickListener
        MenuListener();
        //??????Toolbar
        SetToolBar();
        //??????????????????
        SpinnerAdapter();
        //??????????????????????????????
        sendRequestWithOkHttpForDeviceCategory();
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
        device_llt1 = (LinearLayout) findViewById(R.id.device_llt1);
        device_llt2 = (LinearLayout) findViewById(R.id.device_llt2);
        device_llt3 = (LinearLayout) findViewById(R.id.device_llt3);
        device_llt4 = (LinearLayout) findViewById(R.id.device_llt4);
        device_llt5 = (LinearLayout) findViewById(R.id.device_llt5);
        device_llt6 = (LinearLayout) findViewById(R.id.device_llt6);
        device_llt7 = (LinearLayout) findViewById(R.id.device_llt7);
        device_llt8 = (LinearLayout) findViewById(R.id.device_llt8);
        device_llt9 = (LinearLayout) findViewById(R.id.device_llt9);
        device_llt10 = (LinearLayout) findViewById(R.id.device_llt10);
        device_llt11 = (LinearLayout) findViewById(R.id.device_llt11);
        device_llt12 = (LinearLayout) findViewById(R.id.device_llt12);
        device_llt13 = (LinearLayout) findViewById(R.id.device_llt13);
        device_llt14 = (LinearLayout) findViewById(R.id.device_llt14);
        device_llt15 = (LinearLayout) findViewById(R.id.device_llt15);
        device_llt16 = (LinearLayout) findViewById(R.id.device_llt16);
        device_llt17 = (LinearLayout) findViewById(R.id.device_llt17);
        device_llt18 = (LinearLayout) findViewById(R.id.device_llt18);
        device_llt19 = (LinearLayout) findViewById(R.id.device_llt19);
        device_llt20 = (LinearLayout) findViewById(R.id.device_llt20);
        type_spinner = (Spinner) findViewById(R.id.type_spinner);
        store_spinner = (Spinner) findViewById(R.id.store_spinner);
        count_spinner = (Spinner) findViewById(R.id.count_spinner);
        category_spinner1 = (Spinner) findViewById(R.id.category_spinner1);
        category_spinner2 = (Spinner) findViewById(R.id.category_spinner2);
        category_spinner3 = (Spinner) findViewById(R.id.category_spinner3);
        category_spinner4 = (Spinner) findViewById(R.id.category_spinner4);
        category_spinner5 = (Spinner) findViewById(R.id.category_spinner5);
        category_spinner6 = (Spinner) findViewById(R.id.category_spinner6);
        category_spinner7 = (Spinner) findViewById(R.id.category_spinner7);
        category_spinner8 = (Spinner) findViewById(R.id.category_spinner8);
        category_spinner9 = (Spinner) findViewById(R.id.category_spinner9);
        category_spinner10 = (Spinner) findViewById(R.id.category_spinner10);
        category_spinner11 = (Spinner) findViewById(R.id.category_spinner11);
        category_spinner12 = (Spinner) findViewById(R.id.category_spinner12);
        category_spinner13 = (Spinner) findViewById(R.id.category_spinner13);
        category_spinner14 = (Spinner) findViewById(R.id.category_spinner14);
        category_spinner15 = (Spinner) findViewById(R.id.category_spinner15);
        category_spinner16 = (Spinner) findViewById(R.id.category_spinner16);
        category_spinner17 = (Spinner) findViewById(R.id.category_spinner17);
        category_spinner18 = (Spinner) findViewById(R.id.category_spinner18);
        category_spinner19 = (Spinner) findViewById(R.id.category_spinner19);
        category_spinner20 = (Spinner) findViewById(R.id.category_spinner20);
        supplies_spinner1 = (Spinner) findViewById(R.id.supplies_spinner1);
        supplies_spinner2 = (Spinner) findViewById(R.id.supplies_spinner2);
        supplies_spinner3 = (Spinner) findViewById(R.id.supplies_spinner3);
        supplies_spinner4 = (Spinner) findViewById(R.id.supplies_spinner4);
        supplies_spinner5 = (Spinner) findViewById(R.id.supplies_spinner5);
        supplies_spinner6 = (Spinner) findViewById(R.id.supplies_spinner6);
        supplies_spinner7 = (Spinner) findViewById(R.id.supplies_spinner7);
        supplies_spinner8 = (Spinner) findViewById(R.id.supplies_spinner8);
        supplies_spinner9 = (Spinner) findViewById(R.id.supplies_spinner9);
        supplies_spinner10 = (Spinner) findViewById(R.id.supplies_spinner10);
        supplies_spinner11 = (Spinner) findViewById(R.id.supplies_spinner11);
        supplies_spinner12 = (Spinner) findViewById(R.id.supplies_spinner12);
        supplies_spinner13 = (Spinner) findViewById(R.id.supplies_spinner13);
        supplies_spinner14 = (Spinner) findViewById(R.id.supplies_spinner14);
        supplies_spinner15 = (Spinner) findViewById(R.id.supplies_spinner15);
        supplies_spinner16 = (Spinner) findViewById(R.id.supplies_spinner16);
        supplies_spinner17 = (Spinner) findViewById(R.id.supplies_spinner17);
        supplies_spinner18 = (Spinner) findViewById(R.id.supplies_spinner18);
        supplies_spinner19 = (Spinner) findViewById(R.id.supplies_spinner19);
        supplies_spinner20 = (Spinner) findViewById(R.id.supplies_spinner20);
        quantity_spinner = (Spinner) findViewById(R.id.quantity_spinner);
        add_txt1 = (TextView) findViewById(R.id.add_txt1);
        add_txt2 = (TextView) findViewById(R.id.add_txt2);
        add_txt3 = (TextView) findViewById(R.id.add_txt3);
        add_txt4 = (TextView) findViewById(R.id.add_txt4);
        add_txt5 = (TextView) findViewById(R.id.add_txt5);
        add_txt6 = (TextView) findViewById(R.id.add_txt6);
        add_txt7 = (TextView) findViewById(R.id.add_txt7);
        add_txt8 = (TextView) findViewById(R.id.add_txt8);
        add_txt9 = (TextView) findViewById(R.id.add_txt9);
        add_txt10 = (TextView) findViewById(R.id.add_txt10);
        add_txt11 = (TextView) findViewById(R.id.add_txt11);
        add_txt12 = (TextView) findViewById(R.id.add_txt12);
        add_txt13 = (TextView) findViewById(R.id.add_txt13);
        add_txt14 = (TextView) findViewById(R.id.add_txt14);
        add_txt15 = (TextView) findViewById(R.id.add_txt15);
        add_txt16 = (TextView) findViewById(R.id.add_txt16);
        add_txt17 = (TextView) findViewById(R.id.add_txt17);
        add_txt18 = (TextView) findViewById(R.id.add_txt18);
        add_txt19 = (TextView) findViewById(R.id.add_txt19);
        check_count_txt = (TextView) findViewById(R.id.check_count_txt);
        day_performance_edt = (EditText) findViewById(R.id.day_performance_edt);
        return_count_edt = (EditText) findViewById(R.id.return_count_edt);
        return_money_edt = (EditText) findViewById(R.id.return_money_edt);
        reason_edt = (EditText) findViewById(R.id.reason_edt);
        response_edt = (EditText) findViewById(R.id.response_edt);
        month_performance_edt = (EditText) findViewById(R.id.month_performance_edt);
        count_edt1 = (EditText) findViewById(R.id.count_edt1);
        count_edt2 = (EditText) findViewById(R.id.count_edt2);
        count_edt3 = (EditText) findViewById(R.id.count_edt3);
        count_edt4 = (EditText) findViewById(R.id.count_edt4);
        count_edt5 = (EditText) findViewById(R.id.count_edt5);
        count_edt6 = (EditText) findViewById(R.id.count_edt6);
        count_edt7 = (EditText) findViewById(R.id.count_edt7);
        count_edt8 = (EditText) findViewById(R.id.count_edt8);
        count_edt9 = (EditText) findViewById(R.id.count_edt9);
        count_edt10 = (EditText) findViewById(R.id.count_edt10);
        count_edt11 = (EditText) findViewById(R.id.count_edt11);
        count_edt12 = (EditText) findViewById(R.id.count_edt12);
        count_edt13 = (EditText) findViewById(R.id.count_edt13);
        count_edt14 = (EditText) findViewById(R.id.count_edt14);
        count_edt15 = (EditText) findViewById(R.id.count_edt15);
        count_edt16 = (EditText) findViewById(R.id.count_edt16);
        count_edt17 = (EditText) findViewById(R.id.count_edt17);
        count_edt18 = (EditText) findViewById(R.id.count_edt18);
        count_edt19 = (EditText) findViewById(R.id.count_edt19);
        count_edt20 = (EditText) findViewById(R.id.count_edt20);
        money_edt1 = (EditText) findViewById(R.id.money_edt1);
        money_edt2 = (EditText) findViewById(R.id.money_edt2);
        money_edt3 = (EditText) findViewById(R.id.money_edt3);
        money_edt4 = (EditText) findViewById(R.id.money_edt4);
        money_edt5 = (EditText) findViewById(R.id.money_edt5);
        money_edt6 = (EditText) findViewById(R.id.money_edt6);
        money_edt7 = (EditText) findViewById(R.id.money_edt7);
        money_edt8 = (EditText) findViewById(R.id.money_edt8);
        money_edt9 = (EditText) findViewById(R.id.money_edt9);
        money_edt10 = (EditText) findViewById(R.id.money_edt10);
        money_edt11 = (EditText) findViewById(R.id.money_edt11);
        money_edt12 = (EditText) findViewById(R.id.money_edt12);
        money_edt13 = (EditText) findViewById(R.id.money_edt13);
        money_edt14 = (EditText) findViewById(R.id.money_edt14);
        money_edt15 = (EditText) findViewById(R.id.money_edt15);
        money_edt16 = (EditText) findViewById(R.id.money_edt16);
        money_edt17 = (EditText) findViewById(R.id.money_edt17);
        money_edt18 = (EditText) findViewById(R.id.money_edt18);
        money_edt19 = (EditText) findViewById(R.id.money_edt19);
        money_edt20 = (EditText) findViewById(R.id.money_edt20);
        date_btn = (Button) findViewById(R.id.date_btn);
        preview_btn = (Button) findViewById(R.id.preview_btn);
        choice_btn = (Button) findViewById(R.id.choice_btn);
        upload_btn = (Button) findViewById(R.id.upload_btn);


        add_txt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_txt1.setVisibility(View.GONE);
                device_llt2.setVisibility(View.VISIBLE);
                //?????????????????? ???????????? ????????? ????????? ????????? ????????????
                sendRequestWithOkHttpForReportCheck();
            }
        });

        add_txt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_txt2.setVisibility(View.GONE);
                device_llt3.setVisibility(View.VISIBLE);
                //?????????????????? ???????????? ????????? ????????? ????????? ????????????
                sendRequestWithOkHttpForReportCheck();
            }
        });

        add_txt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_txt3.setVisibility(View.GONE);
                device_llt4.setVisibility(View.VISIBLE);
                //?????????????????? ???????????? ????????? ????????? ????????? ????????????
                sendRequestWithOkHttpForReportCheck();
            }
        });

        add_txt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_txt4.setVisibility(View.GONE);
                device_llt5.setVisibility(View.VISIBLE);
                //?????????????????? ???????????? ????????? ????????? ????????? ????????????
                sendRequestWithOkHttpForReportCheck();

            }
        });

        add_txt5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_txt5.setVisibility(View.GONE);
                device_llt6.setVisibility(View.VISIBLE);
                //?????????????????? ???????????? ????????? ????????? ????????? ????????????
                sendRequestWithOkHttpForReportCheck();
            }
        });

        add_txt6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_txt6.setVisibility(View.GONE);
                device_llt7.setVisibility(View.VISIBLE);
                //?????????????????? ???????????? ????????? ????????? ????????? ????????????
                sendRequestWithOkHttpForReportCheck();
            }
        });

        add_txt7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_txt7.setVisibility(View.GONE);
                device_llt8.setVisibility(View.VISIBLE);
                //?????????????????? ???????????? ????????? ????????? ????????? ????????????
                sendRequestWithOkHttpForReportCheck();
            }
        });

        add_txt8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_txt8.setVisibility(View.GONE);
                device_llt9.setVisibility(View.VISIBLE);
                //?????????????????? ???????????? ????????? ????????? ????????? ????????????
                sendRequestWithOkHttpForReportCheck();
            }
        });

        add_txt9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_txt9.setVisibility(View.GONE);
                device_llt10.setVisibility(View.VISIBLE);
                //?????????????????? ???????????? ????????? ????????? ????????? ????????????
                sendRequestWithOkHttpForReportCheck();
            }
        });

        add_txt10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_txt10.setVisibility(View.GONE);
                device_llt11.setVisibility(View.VISIBLE);
                //?????????????????? ???????????? ????????? ????????? ????????? ????????????
                sendRequestWithOkHttpForReportCheck();
            }
        });

        add_txt11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_txt11.setVisibility(View.GONE);
                device_llt12.setVisibility(View.VISIBLE);
                //?????????????????? ???????????? ????????? ????????? ????????? ????????????
                sendRequestWithOkHttpForReportCheck();
            }
        });

        add_txt12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_txt12.setVisibility(View.GONE);
                device_llt13.setVisibility(View.VISIBLE);
                //?????????????????? ???????????? ????????? ????????? ????????? ????????????
                sendRequestWithOkHttpForReportCheck();
            }
        });

        add_txt13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_txt13.setVisibility(View.GONE);
                device_llt14.setVisibility(View.VISIBLE);
                //?????????????????? ???????????? ????????? ????????? ????????? ????????????
                sendRequestWithOkHttpForReportCheck();
            }
        });

        add_txt14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_txt14.setVisibility(View.GONE);
                device_llt15.setVisibility(View.VISIBLE);
                //?????????????????? ???????????? ????????? ????????? ????????? ????????????
                sendRequestWithOkHttpForReportCheck();
            }
        });

        add_txt15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_txt15.setVisibility(View.GONE);
                device_llt16.setVisibility(View.VISIBLE);
                //?????????????????? ???????????? ????????? ????????? ????????? ????????????
                sendRequestWithOkHttpForReportCheck();
            }
        });

        add_txt16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_txt16.setVisibility(View.GONE);
                device_llt17.setVisibility(View.VISIBLE);
                //?????????????????? ???????????? ????????? ????????? ????????? ????????????
                sendRequestWithOkHttpForReportCheck();
            }
        });

        add_txt17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_txt17.setVisibility(View.GONE);
                device_llt18.setVisibility(View.VISIBLE);
                //?????????????????? ???????????? ????????? ????????? ????????? ????????????
                sendRequestWithOkHttpForReportCheck();
            }
        });

        add_txt18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_txt18.setVisibility(View.GONE);
                device_llt19.setVisibility(View.VISIBLE);
                //?????????????????? ???????????? ????????? ????????? ????????? ????????????
                sendRequestWithOkHttpForReportCheck();
            }
        });

        add_txt19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_txt19.setVisibility(View.GONE);
                device_llt20.setVisibility(View.VISIBLE);
                //?????????????????? ???????????? ????????? ????????? ????????? ????????????
                sendRequestWithOkHttpForReportCheck();
            }
        });

        day_performance_edt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //?????????????????? ???????????? ????????? ????????? ????????? ????????????
                sendRequestWithOkHttpForReportCheck();
            }
        });

        preview_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previewImage(0);
                //?????????????????? ???????????? ????????? ????????? ????????? ????????????
                sendRequestWithOkHttpForReportCheck();
            }
        });

        choice_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
                //?????????????????? ???????????? ????????? ????????? ????????? ????????????
                sendRequestWithOkHttpForReportCheck();
            }
        });

        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //?????????????????? ???????????? ????????? ????????? ????????? ????????????
                sendRequestWithOkHttpForReportCheck();

                try{
                    // delay 1 second
                    Thread.sleep(500);

                    if (date_btn.getText().toString().equals("")||type_spinner.getSelectedItemId() == 0 || store_spinner.getSelectedItemId() == 0) {
                        Toast.makeText(StationReportActivity.this, "???????????????/??????/??????", Toast.LENGTH_SHORT).show();
                    } else {
                        if (day_performance_edt.getText().toString().equals("")/* || month_performance_edt.getText().toString().equals("")*/) {
                            Toast.makeText(StationReportActivity.this, "?????????????????????", Toast.LENGTH_SHORT).show();
                        } else {
                            if ((category_spinner1.getSelectedItemId() != 0 && supplies_spinner1.getSelectedItemId() != 0) || (category_spinner1.getSelectedItemId() != 0 && supplies_spinner1.getSelectedItemId() == 0)) {
                                if (count_edt1.getText().toString().isEmpty() || money_edt1.getText().toString().isEmpty()) {
                                    Toast.makeText(StationReportActivity.this, "?????????????????????????????????", Toast.LENGTH_SHORT).show();
                                } else {
                                    if (supplies_spinner1.getSelectedItemId() == 0) {
                                        Toast.makeText(StationReportActivity.this, "???????????????????????????????????????", Toast.LENGTH_SHORT).show();
                                    } else {
                                        if (check_count_txt.getText().toString().equals("0")) {
                                            if (recyclerView.getChildCount() != 0) {
                                                //???OKHttp??????(?????????????????????SERVER)
                                                uploadImage();
                                                //???OKHttp??????(?????????????????????SERVER)
                                                sendRequestWithOkHttpForOrderPhoto();
                                                //???OKHttp??????(????????????)
                                                sendRequestWithOkHttpForReport();
                                                Toast.makeText(StationReportActivity.this, "??????&????????????????????????!", Toast.LENGTH_SHORT).show();

                                                //???0
                                                //??????????????????
                                                SpinnerAdapter();
                                                //??????????????????????????????
                                                sendRequestWithOkHttpForDeviceCategory();
                                                day_performance_edt.setText("");
                                                return_count_edt.setText("");
                                                return_money_edt.setText("");
                                                reason_edt.setText("");
                                                response_edt.setText("");

                                                count_edt1.setText("");
                                                count_edt2.setText("");
                                                count_edt3.setText("");
                                                count_edt4.setText("");
                                                count_edt5.setText("");
                                                count_edt6.setText("");
                                                count_edt7.setText("");
                                                count_edt8.setText("");
                                                count_edt9.setText("");
                                                count_edt10.setText("");
                                                count_edt11.setText("");
                                                count_edt12.setText("");
                                                count_edt13.setText("");
                                                count_edt14.setText("");
                                                count_edt15.setText("");
                                                count_edt16.setText("");
                                                count_edt17.setText("");
                                                count_edt18.setText("");
                                                count_edt19.setText("");
                                                count_edt20.setText("");
                                                money_edt1.setText("");
                                                money_edt2.setText("");
                                                money_edt3.setText("");
                                                money_edt4.setText("");
                                                money_edt5.setText("");
                                                money_edt6.setText("");
                                                money_edt7.setText("");
                                                money_edt8.setText("");
                                                money_edt9.setText("");
                                                money_edt10.setText("");
                                                money_edt11.setText("");
                                                money_edt12.setText("");
                                                money_edt13.setText("");
                                                money_edt14.setText("");
                                                money_edt15.setText("");
                                                money_edt16.setText("");
                                                money_edt17.setText("");
                                                money_edt18.setText("");
                                                money_edt19.setText("");
                                                money_edt20.setText("");

                                                recyclerView.removeAllViews();

                                            } else {
                                                //???OKHttp??????(????????????)
                                                sendRequestWithOkHttpForReport();
                                                Toast.makeText(StationReportActivity.this, "??????????????????!", Toast.LENGTH_SHORT).show();

                                                //???0
                                                //??????????????????
                                                SpinnerAdapter();
                                                //??????????????????????????????
                                                sendRequestWithOkHttpForDeviceCategory();
                                                day_performance_edt.setText("");
                                                return_count_edt.setText("");
                                                return_money_edt.setText("");
                                                reason_edt.setText("");
                                                response_edt.setText("");

                                                count_edt1.setText("");
                                                count_edt2.setText("");
                                                count_edt3.setText("");
                                                count_edt4.setText("");
                                                count_edt5.setText("");
                                                count_edt6.setText("");
                                                count_edt7.setText("");
                                                count_edt8.setText("");
                                                count_edt9.setText("");
                                                count_edt10.setText("");
                                                count_edt11.setText("");
                                                count_edt12.setText("");
                                                count_edt13.setText("");
                                                count_edt14.setText("");
                                                count_edt15.setText("");
                                                count_edt16.setText("");
                                                count_edt17.setText("");
                                                count_edt18.setText("");
                                                count_edt19.setText("");
                                                count_edt20.setText("");
                                                money_edt1.setText("");
                                                money_edt2.setText("");
                                                money_edt3.setText("");
                                                money_edt4.setText("");
                                                money_edt5.setText("");
                                                money_edt6.setText("");
                                                money_edt7.setText("");
                                                money_edt8.setText("");
                                                money_edt9.setText("");
                                                money_edt10.setText("");
                                                money_edt11.setText("");
                                                money_edt12.setText("");
                                                money_edt13.setText("");
                                                money_edt14.setText("");
                                                money_edt15.setText("");
                                                money_edt16.setText("");
                                                money_edt17.setText("");
                                                money_edt18.setText("");
                                                money_edt19.setText("");
                                                money_edt20.setText("");

                                                recyclerView.removeAllViews();

                                            }
                                        } else {
                                            Toast.makeText(StationReportActivity.this, "????????????????????????!\n????????????????????????????????????!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            } else {
                                if (check_count_txt.getText().toString().equals("0")) {
                                    //???OKHttp??????(????????????)
                                    sendRequestWithOkHttpForReport();
                                    Toast.makeText(StationReportActivity.this, "??????????????????!", Toast.LENGTH_SHORT).show();

                                    //???0
                                    //??????????????????
                                    SpinnerAdapter();
                                    //??????????????????????????????
                                    sendRequestWithOkHttpForDeviceCategory();
                                    day_performance_edt.setText("");
                                    return_count_edt.setText("");
                                    return_money_edt.setText("");
                                    reason_edt.setText("");
                                    response_edt.setText("");

                                    count_edt1.setText("");
                                    count_edt2.setText("");
                                    count_edt3.setText("");
                                    count_edt4.setText("");
                                    count_edt5.setText("");
                                    count_edt6.setText("");
                                    count_edt7.setText("");
                                    count_edt8.setText("");
                                    count_edt9.setText("");
                                    count_edt10.setText("");
                                    count_edt11.setText("");
                                    count_edt12.setText("");
                                    count_edt13.setText("");
                                    count_edt14.setText("");
                                    count_edt15.setText("");
                                    count_edt16.setText("");
                                    count_edt17.setText("");
                                    count_edt18.setText("");
                                    count_edt19.setText("");
                                    count_edt20.setText("");
                                    money_edt1.setText("");
                                    money_edt2.setText("");
                                    money_edt3.setText("");
                                    money_edt4.setText("");
                                    money_edt5.setText("");
                                    money_edt6.setText("");
                                    money_edt7.setText("");
                                    money_edt8.setText("");
                                    money_edt9.setText("");
                                    money_edt10.setText("");
                                    money_edt11.setText("");
                                    money_edt12.setText("");
                                    money_edt13.setText("");
                                    money_edt14.setText("");
                                    money_edt15.setText("");
                                    money_edt16.setText("");
                                    money_edt17.setText("");
                                    money_edt18.setText("");
                                    money_edt19.setText("");
                                    money_edt20.setText("");

                                    recyclerView.removeAllViews();
                                } else {
                                    Toast.makeText(StationReportActivity.this, "????????????????????????!\n????????????????????????????????????!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }

                } catch(InterruptedException e){
                    e.printStackTrace();
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
                Intent intent_login = new Intent(StationReportActivity.this, LoginActivity.class);
                startActivity(intent_login);
            }
        });
    }

    /**
     * ??????????????????
     */
    private void SpinnerAdapter() {
        context = this;
        //?????????????????????????????????????????????
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, type);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type_spinner.setAdapter(adapter);
        type_spinner.setOnItemSelectedListener(type_selectListener);
        //?????????????????????????????????????????????????????????
        adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, empty);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        store_spinner.setAdapter(adapter2);
        store_spinner.setOnItemSelectedListener(check_selectListener);

        //??????Spinner
        adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, count);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        count_spinner.setAdapter(adapter3);
        count_spinner.setOnItemSelectedListener(check_selectListener);

        //??????Spinner
        adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, count);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quantity_spinner.setAdapter(adapter3);
        quantity_spinner.setOnItemSelectedListener(check_selectListener);
    }

    /**
     * ????????? ?????????????????????????????????
     */
    private AdapterView.OnItemSelectedListener check_selectListener = new AdapterView.OnItemSelectedListener() {
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
            //?????????????????????????????????????????????
            //int pos = type_spinner.getSelectedItemPosition();
            //?????????????????? ???????????? ????????? ????????? ????????? ????????????
            sendRequestWithOkHttpForReportCheck();
        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    };

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
                        adapter2 = new ArrayAdapter<String>(StationReportActivity.this, android.R.layout.simple_spinner_item, empty);
                        adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                        store_spinner.setAdapter(adapter2);
                    } else {
                        //??????????????????Adapter/*????????????????????????type2[pos]*/
                        adapter2 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, diy);
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
     * ???OkHttp????????????(DeviceCategory)
     */
    private void sendRequestWithOkHttpForDeviceCategory() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("CATEGORY", "")
                            .build();
                    Log.e(LOG, "CATEGORY");
                    Request request = new Request.Builder()
                            //.url("http://192.168.0.172/WQP_OS/DeviceCategory.php")
                            .url("http://a.wqp-water.com.tw/WQP_OS/DeviceCategory.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectForDeviceCategory(responseData);
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
    private void parseJSONWithJSONObjectForDeviceCategory(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            ArrayList<String> JArrayList = new ArrayList<String>();
            category = new String[]{};
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON??????????????????
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String device_category = jsonObject.getString("C_NAME");
                //JSONArray??????SearchData??????
                JArrayList.add(device_category);
                category = JArrayList.toArray(new String[i]);
                //Log.e("PictureActivity3", diy[i]);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter4 = new ArrayAdapter<String>(StationReportActivity.this, android.R.layout.simple_dropdown_item_1line, category);
                    adapter4.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    category_spinner1.setAdapter(adapter4);
                    category_spinner1.setOnItemSelectedListener(category_selectListener1);
                    category_spinner2.setAdapter(adapter4);
                    category_spinner2.setOnItemSelectedListener(category_selectListener2);
                    category_spinner3.setAdapter(adapter4);
                    category_spinner3.setOnItemSelectedListener(category_selectListener3);
                    category_spinner4.setAdapter(adapter4);
                    category_spinner4.setOnItemSelectedListener(category_selectListener4);
                    category_spinner5.setAdapter(adapter4);
                    category_spinner5.setOnItemSelectedListener(category_selectListener5);
                    category_spinner6.setAdapter(adapter4);
                    category_spinner6.setOnItemSelectedListener(category_selectListener6);
                    category_spinner7.setAdapter(adapter4);
                    category_spinner7.setOnItemSelectedListener(category_selectListener7);
                    category_spinner8.setAdapter(adapter4);
                    category_spinner8.setOnItemSelectedListener(category_selectListener8);
                    category_spinner9.setAdapter(adapter4);
                    category_spinner9.setOnItemSelectedListener(category_selectListener9);
                    category_spinner10.setAdapter(adapter4);
                    category_spinner10.setOnItemSelectedListener(category_selectListener10);
                    category_spinner11.setAdapter(adapter4);
                    category_spinner11.setOnItemSelectedListener(category_selectListener11);
                    category_spinner12.setAdapter(adapter4);
                    category_spinner12.setOnItemSelectedListener(category_selectListener12);
                    category_spinner13.setAdapter(adapter4);
                    category_spinner13.setOnItemSelectedListener(category_selectListener13);
                    category_spinner14.setAdapter(adapter4);
                    category_spinner14.setOnItemSelectedListener(category_selectListener14);
                    category_spinner15.setAdapter(adapter4);
                    category_spinner15.setOnItemSelectedListener(category_selectListener15);
                    category_spinner16.setAdapter(adapter4);
                    category_spinner16.setOnItemSelectedListener(category_selectListener16);
                    category_spinner17.setAdapter(adapter4);
                    category_spinner17.setOnItemSelectedListener(category_selectListener17);
                    category_spinner18.setAdapter(adapter4);
                    category_spinner18.setOnItemSelectedListener(category_selectListener18);
                    category_spinner19.setAdapter(adapter4);
                    category_spinner19.setOnItemSelectedListener(category_selectListener19);
                    category_spinner20.setAdapter(adapter4);
                    category_spinner20.setOnItemSelectedListener(category_selectListener20);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ???????????? ?????????????????????????????????
     */
    private AdapterView.OnItemSelectedListener category_selectListener1 = new AdapterView.OnItemSelectedListener() {
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
            //?????????????????????????????????????????????
            //int pos = type_spinner.getSelectedItemPosition();
            //???DeviceSupplies.PHP????????????
            sendRequestWithOkHttpForDeviceSupplies1();
            //?????????????????? ???????????? ????????? ????????? ????????? ????????????
            sendRequestWithOkHttpForReportCheck();

            //????????????????????????"?????????"?????????????????????????????????
            if(category_spinner1.getSelectedItemId() == 0) {
                count_edt1.setText("");
                money_edt1.setText("");
            }

        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    };

    /**
     * ???OkHttp????????????(DIYStore)
     */
    private void sendRequestWithOkHttpForDeviceSupplies1() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                String category_select1 = String.valueOf(category_spinner1.getSelectedItem());

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("CATEGORY", category_select1)
                            .build();
                    Log.e(LOG, category_select1);
                    Request request = new Request.Builder()
                            //.url("http://192.168.0.172/WQP_OS/DeviceSupplies.php")
                            .url("http://a.wqp-water.com.tw/WQP_OS/DeviceSupplies.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectForDeviceSupplies1(responseData);
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
    private void parseJSONWithJSONObjectForDeviceSupplies1(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            ArrayList<String> JArrayList = new ArrayList<String>();
            supplies = new String[]{};
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON??????????????????
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String device_supplies = jsonObject.getString("C_DS_NAME");
                //JSONArray??????SearchData??????
                JArrayList.add(device_supplies);
                supplies = JArrayList.toArray(new String[i]);
                //Log.e("PictureActivity3", diy[i]);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //??????????????????Adapter/*????????????????????????type2[pos]*/
                    adapter5 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, supplies);
                    adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //???????????????????????????Spinner
                    supplies_spinner1.setAdapter(adapter5);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ???????????? ?????????????????????????????????
     */
    private AdapterView.OnItemSelectedListener category_selectListener2 = new AdapterView.OnItemSelectedListener() {
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
            //?????????????????????????????????????????????
            //int pos = type_spinner.getSelectedItemPosition();
            //???DeviceSupplies.PHP????????????
            sendRequestWithOkHttpForDeviceSupplies2();
            //?????????????????? ???????????? ????????? ????????? ????????? ????????????
            sendRequestWithOkHttpForReportCheck();

            //????????????????????????"?????????"?????????????????????????????????
            if(category_spinner2.getSelectedItemId() == 0) {
                count_edt2.setText("");
                money_edt2.setText("");
            }

        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    };

    /**
     * ???OkHttp????????????(DIYStore)
     */
    private void sendRequestWithOkHttpForDeviceSupplies2() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                String category_select2 = String.valueOf(category_spinner2.getSelectedItem());

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("CATEGORY", category_select2)
                            .build();
                    Log.e(LOG, category_select2);
                    Request request = new Request.Builder()
                            //.url("http://192.168.0.172/WQP_OS/DeviceSupplies.php")
                            .url("http://a.wqp-water.com.tw/WQP_OS/DeviceSupplies.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectForDeviceSupplies2(responseData);
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
    private void parseJSONWithJSONObjectForDeviceSupplies2(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            ArrayList<String> JArrayList = new ArrayList<String>();
            supplies = new String[]{};
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON??????????????????
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String device_supplies = jsonObject.getString("C_DS_NAME");
                //JSONArray??????SearchData??????
                JArrayList.add(device_supplies);
                supplies = JArrayList.toArray(new String[i]);
                //Log.e("PictureActivity3", diy[i]);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //??????????????????Adapter/*????????????????????????type2[pos]*/
                    adapter5 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, supplies);
                    adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //???????????????????????????Spinner
                    supplies_spinner2.setAdapter(adapter5);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ???????????? ?????????????????????????????????
     */
    private AdapterView.OnItemSelectedListener category_selectListener3 = new AdapterView.OnItemSelectedListener() {
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
            //?????????????????????????????????????????????
            //int pos = type_spinner.getSelectedItemPosition();
            //???DeviceSupplies.PHP????????????
            sendRequestWithOkHttpForDeviceSupplies3();
            //?????????????????? ???????????? ????????? ????????? ????????? ????????????
            sendRequestWithOkHttpForReportCheck();

            //????????????????????????"?????????"?????????????????????????????????
            if(category_spinner3.getSelectedItemId() == 0) {
                count_edt3.setText("");
                money_edt3.setText("");
            }

        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    };

    /**
     * ???OkHttp????????????(DIYStore)
     */
    private void sendRequestWithOkHttpForDeviceSupplies3() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                String category_select3 = String.valueOf(category_spinner3.getSelectedItem());

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("CATEGORY", category_select3)
                            .build();
                    Log.e(LOG, category_select3);
                    Request request = new Request.Builder()
                            //.url("http://192.168.0.172/WQP_OS/DeviceSupplies.php")
                            .url("http://a.wqp-water.com.tw/WQP_OS/DeviceSupplies.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectForDeviceSupplies3(responseData);
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
    private void parseJSONWithJSONObjectForDeviceSupplies3(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            ArrayList<String> JArrayList = new ArrayList<String>();
            supplies = new String[]{};
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON??????????????????
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String device_supplies = jsonObject.getString("C_DS_NAME");
                //JSONArray??????SearchData??????
                JArrayList.add(device_supplies);
                supplies = JArrayList.toArray(new String[i]);
                //Log.e("PictureActivity3", diy[i]);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //??????????????????Adapter/*????????????????????????type2[pos]*/
                    adapter5 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, supplies);
                    adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //???????????????????????????Spinner
                    supplies_spinner3.setAdapter(adapter5);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ???????????? ?????????????????????????????????
     */
    private AdapterView.OnItemSelectedListener category_selectListener4 = new AdapterView.OnItemSelectedListener() {
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
            //?????????????????????????????????????????????
            //int pos = type_spinner.getSelectedItemPosition();
            //???DeviceSupplies.PHP????????????
            sendRequestWithOkHttpForDeviceSupplies4();
            //?????????????????? ???????????? ????????? ????????? ????????? ????????????
            sendRequestWithOkHttpForReportCheck();

            //????????????????????????"?????????"?????????????????????????????????
            if(category_spinner4.getSelectedItemId() == 0) {
                count_edt4.setText("");
                money_edt4.setText("");
            }

        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    };

    /**
     * ???OkHttp????????????(DIYStore)
     */
    private void sendRequestWithOkHttpForDeviceSupplies4() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                String category_select4 = String.valueOf(category_spinner4.getSelectedItem());

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("CATEGORY", category_select4)
                            .build();
                    Log.e(LOG, category_select4);
                    Request request = new Request.Builder()
                            //.url("http://192.168.0.172/WQP_OS/DeviceSupplies.php")
                            .url("http://a.wqp-water.com.tw/WQP_OS/DeviceSupplies.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectForDeviceSupplies4(responseData);
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
    private void parseJSONWithJSONObjectForDeviceSupplies4(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            ArrayList<String> JArrayList = new ArrayList<String>();
            supplies = new String[]{};
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON??????????????????
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String device_supplies = jsonObject.getString("C_DS_NAME");
                //JSONArray??????SearchData??????
                JArrayList.add(device_supplies);
                supplies = JArrayList.toArray(new String[i]);
                //Log.e("PictureActivity3", diy[i]);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //??????????????????Adapter/*????????????????????????type2[pos]*/
                    adapter5 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, supplies);
                    adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //???????????????????????????Spinner
                    supplies_spinner4.setAdapter(adapter5);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ???????????? ?????????????????????????????????
     */
    private AdapterView.OnItemSelectedListener category_selectListener5 = new AdapterView.OnItemSelectedListener() {
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
            //?????????????????????????????????????????????
            //int pos = type_spinner.getSelectedItemPosition();
            //???DeviceSupplies.PHP????????????
            sendRequestWithOkHttpForDeviceSupplies5();
            //?????????????????? ???????????? ????????? ????????? ????????? ????????????
            sendRequestWithOkHttpForReportCheck();

            //????????????????????????"?????????"?????????????????????????????????
            if(category_spinner5.getSelectedItemId() == 0) {
                count_edt5.setText("");
                money_edt5.setText("");
            }

        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    };

    /**
     * ???OkHttp????????????(DIYStore)
     */
    private void sendRequestWithOkHttpForDeviceSupplies5() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                String category_select5 = String.valueOf(category_spinner5.getSelectedItem());

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("CATEGORY", category_select5)
                            .build();
                    Log.e(LOG, category_select5);
                    Request request = new Request.Builder()
                            //.url("http://192.168.0.172/WQP_OS/DeviceSupplies.php")
                            .url("http://a.wqp-water.com.tw/WQP_OS/DeviceSupplies.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectForDeviceSupplies5(responseData);
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
    private void parseJSONWithJSONObjectForDeviceSupplies5(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            ArrayList<String> JArrayList = new ArrayList<String>();
            supplies = new String[]{};
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON??????????????????
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String device_supplies = jsonObject.getString("C_DS_NAME");
                //JSONArray??????SearchData??????
                JArrayList.add(device_supplies);
                supplies = JArrayList.toArray(new String[i]);
                //Log.e("PictureActivity3", diy[i]);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //??????????????????Adapter/*????????????????????????type2[pos]*/
                    adapter5 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, supplies);
                    adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //???????????????????????????Spinner
                    supplies_spinner5.setAdapter(adapter5);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ???????????? ?????????????????????????????????
     */
    private AdapterView.OnItemSelectedListener category_selectListener6 = new AdapterView.OnItemSelectedListener() {
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
            //?????????????????????????????????????????????
            //int pos = type_spinner.getSelectedItemPosition();
            //???DeviceSupplies.PHP????????????
            sendRequestWithOkHttpForDeviceSupplies6();

            //????????????????????????"?????????"?????????????????????????????????
            if(category_spinner6.getSelectedItemId() == 0) {
                count_edt6.setText("");
                money_edt6.setText("");
            }

        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    };

    /**
     * ???OkHttp????????????(DIYStore)
     */
    private void sendRequestWithOkHttpForDeviceSupplies6() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                String category_select6 = String.valueOf(category_spinner6.getSelectedItem());

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("CATEGORY", category_select6)
                            .build();
                    Log.e(LOG, category_select6);
                    Request request = new Request.Builder()
                            //.url("http://192.168.0.172/WQP_OS/DeviceSupplies.php")
                            .url("http://a.wqp-water.com.tw/WQP_OS/DeviceSupplies.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectForDeviceSupplies6(responseData);
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
    private void parseJSONWithJSONObjectForDeviceSupplies6(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            ArrayList<String> JArrayList = new ArrayList<String>();
            supplies = new String[]{};
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON??????????????????
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String device_supplies = jsonObject.getString("C_DS_NAME");
                //JSONArray??????SearchData??????
                JArrayList.add(device_supplies);
                supplies = JArrayList.toArray(new String[i]);
                //Log.e("PictureActivity3", diy[i]);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //??????????????????Adapter/*????????????????????????type2[pos]*/
                    adapter5 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, supplies);
                    adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //???????????????????????????Spinner
                    supplies_spinner6.setAdapter(adapter5);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ???????????? ?????????????????????????????????
     */
    private AdapterView.OnItemSelectedListener category_selectListener7 = new AdapterView.OnItemSelectedListener() {
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
            //?????????????????????????????????????????????
            //int pos = type_spinner.getSelectedItemPosition();
            //???DeviceSupplies.PHP????????????
            sendRequestWithOkHttpForDeviceSupplies7();

            //????????????????????????"?????????"?????????????????????????????????
            if(category_spinner7.getSelectedItemId() == 0) {
                count_edt7.setText("");
                money_edt7.setText("");
            }

        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    };

    /**
     * ???OkHttp????????????(DIYStore)
     */
    private void sendRequestWithOkHttpForDeviceSupplies7() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                String category_select7 = String.valueOf(category_spinner7.getSelectedItem());

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("CATEGORY", category_select7)
                            .build();
                    Log.e(LOG, category_select7);
                    Request request = new Request.Builder()
                            //.url("http://192.168.0.172/WQP_OS/DeviceSupplies.php")
                            .url("http://a.wqp-water.com.tw/WQP_OS/DeviceSupplies.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectForDeviceSupplies7(responseData);
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
    private void parseJSONWithJSONObjectForDeviceSupplies7(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            ArrayList<String> JArrayList = new ArrayList<String>();
            supplies = new String[]{};
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON??????????????????
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String device_supplies = jsonObject.getString("C_DS_NAME");
                //JSONArray??????SearchData??????
                JArrayList.add(device_supplies);
                supplies = JArrayList.toArray(new String[i]);
                //Log.e("PictureActivity3", diy[i]);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //??????????????????Adapter/*????????????????????????type2[pos]*/
                    adapter5 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, supplies);
                    adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //???????????????????????????Spinner
                    supplies_spinner7.setAdapter(adapter5);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ???????????? ?????????????????????????????????
     */
    private AdapterView.OnItemSelectedListener category_selectListener8 = new AdapterView.OnItemSelectedListener() {
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
            //?????????????????????????????????????????????
            //int pos = type_spinner.getSelectedItemPosition();
            //???DeviceSupplies.PHP????????????
            sendRequestWithOkHttpForDeviceSupplies8();

            //????????????????????????"?????????"?????????????????????????????????
            if(category_spinner8.getSelectedItemId() == 0) {
                count_edt8.setText("");
                money_edt8.setText("");
            }

        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    };

    /**
     * ???OkHttp????????????(DIYStore)
     */
    private void sendRequestWithOkHttpForDeviceSupplies8() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                String category_select8 = String.valueOf(category_spinner8.getSelectedItem());

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("CATEGORY", category_select8)
                            .build();
                    Log.e(LOG, category_select8);
                    Request request = new Request.Builder()
                            //.url("http://192.168.0.172/WQP_OS/DeviceSupplies.php")
                            .url("http://a.wqp-water.com.tw/WQP_OS/DeviceSupplies.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectForDeviceSupplies8(responseData);
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
    private void parseJSONWithJSONObjectForDeviceSupplies8(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            ArrayList<String> JArrayList = new ArrayList<String>();
            supplies = new String[]{};
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON??????????????????
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String device_supplies = jsonObject.getString("C_DS_NAME");
                //JSONArray??????SearchData??????
                JArrayList.add(device_supplies);
                supplies = JArrayList.toArray(new String[i]);
                //Log.e("PictureActivity3", diy[i]);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //??????????????????Adapter/*????????????????????????type2[pos]*/
                    adapter5 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, supplies);
                    adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //???????????????????????????Spinner
                    supplies_spinner8.setAdapter(adapter5);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ???????????? ?????????????????????????????????
     */
    private AdapterView.OnItemSelectedListener category_selectListener9 = new AdapterView.OnItemSelectedListener() {
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
            //?????????????????????????????????????????????
            //int pos = type_spinner.getSelectedItemPosition();
            //???DeviceSupplies.PHP????????????
            sendRequestWithOkHttpForDeviceSupplies9();

            //????????????????????????"?????????"?????????????????????????????????
            if(category_spinner9.getSelectedItemId() == 0) {
                count_edt9.setText("");
                money_edt9.setText("");
            }

        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    };

    /**
     * ???OkHttp????????????(DIYStore)
     */
    private void sendRequestWithOkHttpForDeviceSupplies9() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                String category_select9 = String.valueOf(category_spinner9.getSelectedItem());

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("CATEGORY", category_select9)
                            .build();
                    Log.e(LOG, category_select9);
                    Request request = new Request.Builder()
                            //.url("http://192.168.0.172/WQP_OS/DeviceSupplies.php")
                            .url("http://a.wqp-water.com.tw/WQP_OS/DeviceSupplies.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectForDeviceSupplies9(responseData);
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
    private void parseJSONWithJSONObjectForDeviceSupplies9(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            ArrayList<String> JArrayList = new ArrayList<String>();
            supplies = new String[]{};
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON??????????????????
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String device_supplies = jsonObject.getString("C_DS_NAME");
                //JSONArray??????SearchData??????
                JArrayList.add(device_supplies);
                supplies = JArrayList.toArray(new String[i]);
                //Log.e("PictureActivity3", diy[i]);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //??????????????????Adapter/*????????????????????????type2[pos]*/
                    adapter5 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, supplies);
                    adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //???????????????????????????Spinner
                    supplies_spinner9.setAdapter(adapter5);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ???????????? ?????????????????????????????????
     */
    private AdapterView.OnItemSelectedListener category_selectListener10 = new AdapterView.OnItemSelectedListener() {
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
            //?????????????????????????????????????????????
            //int pos = type_spinner.getSelectedItemPosition();
            //???DeviceSupplies.PHP????????????
            sendRequestWithOkHttpForDeviceSupplies10();

            //????????????????????????"?????????"?????????????????????????????????
            if(category_spinner10.getSelectedItemId() == 0) {
                count_edt10.setText("");
                money_edt10.setText("");
            }

        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    };

    /**
     * ???OkHttp????????????(DIYStore)
     */
    private void sendRequestWithOkHttpForDeviceSupplies10() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                String category_select10 = String.valueOf(category_spinner10.getSelectedItem());

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("CATEGORY", category_select10)
                            .build();
                    Log.e(LOG, category_select10);
                    Request request = new Request.Builder()
                            //.url("http://192.168.0.172/WQP_OS/DeviceSupplies.php")
                            .url("http://a.wqp-water.com.tw/WQP_OS/DeviceSupplies.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectForDeviceSupplies10(responseData);
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
    private void parseJSONWithJSONObjectForDeviceSupplies10(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            ArrayList<String> JArrayList = new ArrayList<String>();
            supplies = new String[]{};
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON??????????????????
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String device_supplies = jsonObject.getString("C_DS_NAME");
                //JSONArray??????SearchData??????
                JArrayList.add(device_supplies);
                supplies = JArrayList.toArray(new String[i]);
                //Log.e("PictureActivity3", diy[i]);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //??????????????????Adapter/*????????????????????????type2[pos]*/
                    adapter5 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, supplies);
                    adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //???????????????????????????Spinner
                    supplies_spinner10.setAdapter(adapter5);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ???????????? ?????????????????????????????????
     */
    private AdapterView.OnItemSelectedListener category_selectListener11 = new AdapterView.OnItemSelectedListener() {
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
            //?????????????????????????????????????????????
            //int pos = type_spinner.getSelectedItemPosition();
            //???DeviceSupplies.PHP????????????
            sendRequestWithOkHttpForDeviceSupplies11();

            //????????????????????????"?????????"?????????????????????????????????
            if(category_spinner11.getSelectedItemId() == 0) {
                count_edt11.setText("");
                money_edt11.setText("");
            }

        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    };

    /**
     * ???OkHttp????????????(DIYStore)
     */
    private void sendRequestWithOkHttpForDeviceSupplies11() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                String category_select11 = String.valueOf(category_spinner11.getSelectedItem());

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("CATEGORY", category_select11)
                            .build();
                    Log.e(LOG, category_select11);
                    Request request = new Request.Builder()
                            //.url("http://192.168.0.172/WQP_OS/DeviceSupplies.php")
                            .url("http://a.wqp-water.com.tw/WQP_OS/DeviceSupplies.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectForDeviceSupplies11(responseData);
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
    private void parseJSONWithJSONObjectForDeviceSupplies11(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            ArrayList<String> JArrayList = new ArrayList<String>();
            supplies = new String[]{};
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON??????????????????
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String device_supplies = jsonObject.getString("C_DS_NAME");
                //JSONArray??????SearchData??????
                JArrayList.add(device_supplies);
                supplies = JArrayList.toArray(new String[i]);
                //Log.e("PictureActivity3", diy[i]);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //??????????????????Adapter/*????????????????????????type2[pos]*/
                    adapter5 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, supplies);
                    adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //???????????????????????????Spinner
                    supplies_spinner11.setAdapter(adapter5);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ???????????? ?????????????????????????????????
     */
    private AdapterView.OnItemSelectedListener category_selectListener12 = new AdapterView.OnItemSelectedListener() {
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
            //?????????????????????????????????????????????
            //int pos = type_spinner.getSelectedItemPosition();
            //???DeviceSupplies.PHP????????????
            sendRequestWithOkHttpForDeviceSupplies12();

            //????????????????????????"?????????"?????????????????????????????????
            if(category_spinner12.getSelectedItemId() == 0) {
                count_edt12.setText("");
                money_edt12.setText("");
            }

        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    };

    /**
     * ???OkHttp????????????(DIYStore)
     */
    private void sendRequestWithOkHttpForDeviceSupplies12() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                String category_select12 = String.valueOf(category_spinner12.getSelectedItem());

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("CATEGORY", category_select12)
                            .build();
                    Log.e(LOG, category_select12);
                    Request request = new Request.Builder()
                            //.url("http://192.168.0.172/WQP_OS/DeviceSupplies.php")
                            .url("http://a.wqp-water.com.tw/WQP_OS/DeviceSupplies.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectForDeviceSupplies12(responseData);
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
    private void parseJSONWithJSONObjectForDeviceSupplies12(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            ArrayList<String> JArrayList = new ArrayList<String>();
            supplies = new String[]{};
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON??????????????????
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String device_supplies = jsonObject.getString("C_DS_NAME");
                //JSONArray??????SearchData??????
                JArrayList.add(device_supplies);
                supplies = JArrayList.toArray(new String[i]);
                //Log.e("PictureActivity3", diy[i]);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //??????????????????Adapter/*????????????????????????type2[pos]*/
                    adapter5 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, supplies);
                    adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //???????????????????????????Spinner
                    supplies_spinner12.setAdapter(adapter5);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ???????????? ?????????????????????????????????
     */
    private AdapterView.OnItemSelectedListener category_selectListener13 = new AdapterView.OnItemSelectedListener() {
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
            //?????????????????????????????????????????????
            //int pos = type_spinner.getSelectedItemPosition();
            //???DeviceSupplies.PHP????????????
            sendRequestWithOkHttpForDeviceSupplies13();

            //????????????????????????"?????????"?????????????????????????????????
            if(category_spinner13.getSelectedItemId() == 0) {
                count_edt13.setText("");
                money_edt13.setText("");
            }

        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    };

    /**
     * ???OkHttp????????????(DIYStore)
     */
    private void sendRequestWithOkHttpForDeviceSupplies13() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                String category_select13 = String.valueOf(category_spinner13.getSelectedItem());

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("CATEGORY", category_select13)
                            .build();
                    Log.e(LOG, category_select13);
                    Request request = new Request.Builder()
                            //.url("http://192.168.0.172/WQP_OS/DeviceSupplies.php")
                            .url("http://a.wqp-water.com.tw/WQP_OS/DeviceSupplies.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectForDeviceSupplies13(responseData);
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
    private void parseJSONWithJSONObjectForDeviceSupplies13(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            ArrayList<String> JArrayList = new ArrayList<String>();
            supplies = new String[]{};
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON??????????????????
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String device_supplies = jsonObject.getString("C_DS_NAME");
                //JSONArray??????SearchData??????
                JArrayList.add(device_supplies);
                supplies = JArrayList.toArray(new String[i]);
                //Log.e("PictureActivity3", diy[i]);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //??????????????????Adapter/*????????????????????????type2[pos]*/
                    adapter5 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, supplies);
                    adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //???????????????????????????Spinner
                    supplies_spinner13.setAdapter(adapter5);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ???????????? ?????????????????????????????????
     */
    private AdapterView.OnItemSelectedListener category_selectListener14 = new AdapterView.OnItemSelectedListener() {
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
            //?????????????????????????????????????????????
            //int pos = type_spinner.getSelectedItemPosition();
            //???DeviceSupplies.PHP????????????
            sendRequestWithOkHttpForDeviceSupplies14();

            //????????????????????????"?????????"?????????????????????????????????
            if(category_spinner14.getSelectedItemId() == 0) {
                count_edt14.setText("");
                money_edt14.setText("");
            }

        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    };

    /**
     * ???OkHttp????????????(DIYStore)
     */
    private void sendRequestWithOkHttpForDeviceSupplies14() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                String category_select14 = String.valueOf(category_spinner14.getSelectedItem());

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("CATEGORY", category_select14)
                            .build();
                    Log.e(LOG, category_select14);
                    Request request = new Request.Builder()
                            //.url("http://192.168.0.172/WQP_OS/DeviceSupplies.php")
                            .url("http://a.wqp-water.com.tw/WQP_OS/DeviceSupplies.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectForDeviceSupplies14(responseData);
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
    private void parseJSONWithJSONObjectForDeviceSupplies14(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            ArrayList<String> JArrayList = new ArrayList<String>();
            supplies = new String[]{};
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON??????????????????
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String device_supplies = jsonObject.getString("C_DS_NAME");
                //JSONArray??????SearchData??????
                JArrayList.add(device_supplies);
                supplies = JArrayList.toArray(new String[i]);
                //Log.e("PictureActivity3", diy[i]);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //??????????????????Adapter/*????????????????????????type2[pos]*/
                    adapter5 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, supplies);
                    adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //???????????????????????????Spinner
                    supplies_spinner14.setAdapter(adapter5);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ???????????? ?????????????????????????????????
     */
    private AdapterView.OnItemSelectedListener category_selectListener15 = new AdapterView.OnItemSelectedListener() {
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
            //?????????????????????????????????????????????
            //int pos = type_spinner.getSelectedItemPosition();
            //???DeviceSupplies.PHP????????????
            sendRequestWithOkHttpForDeviceSupplies15();

            //????????????????????????"?????????"?????????????????????????????????
            if(category_spinner15.getSelectedItemId() == 0) {
                count_edt15.setText("");
                money_edt15.setText("");
            }

        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    };

    /**
     * ???OkHttp????????????(DIYStore)
     */
    private void sendRequestWithOkHttpForDeviceSupplies15() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                String category_select15 = String.valueOf(category_spinner15.getSelectedItem());

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("CATEGORY", category_select15)
                            .build();
                    Log.e(LOG, category_select15);
                    Request request = new Request.Builder()
                            //.url("http://192.168.0.172/WQP_OS/DeviceSupplies.php")
                            .url("http://a.wqp-water.com.tw/WQP_OS/DeviceSupplies.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectForDeviceSupplies15(responseData);
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
    private void parseJSONWithJSONObjectForDeviceSupplies15(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            ArrayList<String> JArrayList = new ArrayList<String>();
            supplies = new String[]{};
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON??????????????????
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String device_supplies = jsonObject.getString("C_DS_NAME");
                //JSONArray??????SearchData??????
                JArrayList.add(device_supplies);
                supplies = JArrayList.toArray(new String[i]);
                //Log.e("PictureActivity3", diy[i]);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //??????????????????Adapter/*????????????????????????type2[pos]*/
                    adapter5 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, supplies);
                    adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //???????????????????????????Spinner
                    supplies_spinner15.setAdapter(adapter5);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ???????????? ?????????????????????????????????
     */
    private AdapterView.OnItemSelectedListener category_selectListener16 = new AdapterView.OnItemSelectedListener() {
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
            //?????????????????????????????????????????????
            //int pos = type_spinner.getSelectedItemPosition();
            //???DeviceSupplies.PHP????????????
            sendRequestWithOkHttpForDeviceSupplies16();

            //????????????????????????"?????????"?????????????????????????????????
            if(category_spinner16.getSelectedItemId() == 0) {
                count_edt16.setText("");
                money_edt16.setText("");
            }

        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    };

    /**
     * ???OkHttp????????????(DIYStore)
     */
    private void sendRequestWithOkHttpForDeviceSupplies16() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                String category_select16 = String.valueOf(category_spinner16.getSelectedItem());

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("CATEGORY", category_select16)
                            .build();
                    Log.e(LOG, category_select16);
                    Request request = new Request.Builder()
                            //.url("http://192.168.0.172/WQP_OS/DeviceSupplies.php")
                            .url("http://a.wqp-water.com.tw/WQP_OS/DeviceSupplies.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectForDeviceSupplies16(responseData);
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
    private void parseJSONWithJSONObjectForDeviceSupplies16(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            ArrayList<String> JArrayList = new ArrayList<String>();
            supplies = new String[]{};
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON??????????????????
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String device_supplies = jsonObject.getString("C_DS_NAME");
                //JSONArray??????SearchData??????
                JArrayList.add(device_supplies);
                supplies = JArrayList.toArray(new String[i]);
                //Log.e("PictureActivity3", diy[i]);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //??????????????????Adapter/*????????????????????????type2[pos]*/
                    adapter5 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, supplies);
                    adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //???????????????????????????Spinner
                    supplies_spinner16.setAdapter(adapter5);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ???????????? ?????????????????????????????????
     */
    private AdapterView.OnItemSelectedListener category_selectListener17 = new AdapterView.OnItemSelectedListener() {
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
            //?????????????????????????????????????????????
            //int pos = type_spinner.getSelectedItemPosition();
            //???DeviceSupplies.PHP????????????
            sendRequestWithOkHttpForDeviceSupplies17();

            //????????????????????????"?????????"?????????????????????????????????
            if(category_spinner17.getSelectedItemId() == 0) {
                count_edt17.setText("");
                money_edt17.setText("");
            }

        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    };

    /**
     * ???OkHttp????????????(DIYStore)
     */
    private void sendRequestWithOkHttpForDeviceSupplies17() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                String category_select17 = String.valueOf(category_spinner17.getSelectedItem());

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("CATEGORY", category_select17)
                            .build();
                    Log.e(LOG, category_select17);
                    Request request = new Request.Builder()
                            //.url("http://192.168.0.172/WQP_OS/DeviceSupplies.php")
                            .url("http://a.wqp-water.com.tw/WQP_OS/DeviceSupplies.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectForDeviceSupplies17(responseData);
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
    private void parseJSONWithJSONObjectForDeviceSupplies17(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            ArrayList<String> JArrayList = new ArrayList<String>();
            supplies = new String[]{};
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON??????????????????
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String device_supplies = jsonObject.getString("C_DS_NAME");
                //JSONArray??????SearchData??????
                JArrayList.add(device_supplies);
                supplies = JArrayList.toArray(new String[i]);
                //Log.e("PictureActivity3", diy[i]);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //??????????????????Adapter/*????????????????????????type2[pos]*/
                    adapter5 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, supplies);
                    adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //???????????????????????????Spinner
                    supplies_spinner17.setAdapter(adapter5);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ???????????? ?????????????????????????????????
     */
    private AdapterView.OnItemSelectedListener category_selectListener18 = new AdapterView.OnItemSelectedListener() {
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
            //?????????????????????????????????????????????
            //int pos = type_spinner.getSelectedItemPosition();
            //???DeviceSupplies.PHP????????????
            sendRequestWithOkHttpForDeviceSupplies18();

            //????????????????????????"?????????"?????????????????????????????????
            if(category_spinner18.getSelectedItemId() == 0) {
                count_edt18.setText("");
                money_edt18.setText("");
            }

        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    };

    /**
     * ???OkHttp????????????(DIYStore)
     */
    private void sendRequestWithOkHttpForDeviceSupplies18() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                String category_select18 = String.valueOf(category_spinner18.getSelectedItem());

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("CATEGORY", category_select18)
                            .build();
                    Log.e(LOG, category_select18);
                    Request request = new Request.Builder()
                            //.url("http://192.168.0.172/WQP_OS/DeviceSupplies.php")
                            .url("http://a.wqp-water.com.tw/WQP_OS/DeviceSupplies.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectForDeviceSupplies18(responseData);
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
    private void parseJSONWithJSONObjectForDeviceSupplies18(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            ArrayList<String> JArrayList = new ArrayList<String>();
            supplies = new String[]{};
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON??????????????????
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String device_supplies = jsonObject.getString("C_DS_NAME");
                //JSONArray??????SearchData??????
                JArrayList.add(device_supplies);
                supplies = JArrayList.toArray(new String[i]);
                //Log.e("PictureActivity3", diy[i]);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //??????????????????Adapter/*????????????????????????type2[pos]*/
                    adapter5 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, supplies);
                    adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //???????????????????????????Spinner
                    supplies_spinner18.setAdapter(adapter5);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ???????????? ?????????????????????????????????
     */
    private AdapterView.OnItemSelectedListener category_selectListener19 = new AdapterView.OnItemSelectedListener() {
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
            //?????????????????????????????????????????????
            //int pos = type_spinner.getSelectedItemPosition();
            //???DeviceSupplies.PHP????????????
            sendRequestWithOkHttpForDeviceSupplies19();

            //????????????????????????"?????????"?????????????????????????????????
            if(category_spinner19.getSelectedItemId() == 0) {
                count_edt19.setText("");
                money_edt19.setText("");
            }

        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    };

    /**
     * ???OkHttp????????????(DIYStore)
     */
    private void sendRequestWithOkHttpForDeviceSupplies19() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                String category_select19 = String.valueOf(category_spinner19.getSelectedItem());

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("CATEGORY", category_select19)
                            .build();
                    Log.e(LOG, category_select19);
                    Request request = new Request.Builder()
                            //.url("http://192.168.0.172/WQP_OS/DeviceSupplies.php")
                            .url("http://a.wqp-water.com.tw/WQP_OS/DeviceSupplies.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectForDeviceSupplies19(responseData);
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
    private void parseJSONWithJSONObjectForDeviceSupplies19(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            ArrayList<String> JArrayList = new ArrayList<String>();
            supplies = new String[]{};
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON??????????????????
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String device_supplies = jsonObject.getString("C_DS_NAME");
                //JSONArray??????SearchData??????
                JArrayList.add(device_supplies);
                supplies = JArrayList.toArray(new String[i]);
                //Log.e("PictureActivity3", diy[i]);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //??????????????????Adapter/*????????????????????????type2[pos]*/
                    adapter5 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, supplies);
                    adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //???????????????????????????Spinner
                    supplies_spinner19.setAdapter(adapter5);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ???????????? ?????????????????????????????????
     */
    private AdapterView.OnItemSelectedListener category_selectListener20 = new AdapterView.OnItemSelectedListener() {
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
            //?????????????????????????????????????????????
            //int pos = type_spinner.getSelectedItemPosition();
            //???DeviceSupplies.PHP????????????
            sendRequestWithOkHttpForDeviceSupplies20();

            //????????????????????????"?????????"?????????????????????????????????
            if(category_spinner20.getSelectedItemId() == 0) {
                count_edt20.setText("");
                money_edt20.setText("");
            }

        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    };

    /**
     * ???OkHttp????????????(DIYStore)
     */
    private void sendRequestWithOkHttpForDeviceSupplies20() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                String category_select20 = String.valueOf(category_spinner20.getSelectedItem());

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("CATEGORY", category_select20)
                            .build();
                    Log.e(LOG, category_select20);
                    Request request = new Request.Builder()
                            //.url("http://192.168.0.172/WQP_OS/DeviceSupplies.php")
                            .url("http://a.wqp-water.com.tw/WQP_OS/DeviceSupplies.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e(LOG, responseData);
                    parseJSONWithJSONObjectForDeviceSupplies20(responseData);
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
    private void parseJSONWithJSONObjectForDeviceSupplies20(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            ArrayList<String> JArrayList = new ArrayList<String>();
            supplies = new String[]{};
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON??????????????????
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String device_supplies = jsonObject.getString("C_DS_NAME");
                //JSONArray??????SearchData??????
                JArrayList.add(device_supplies);
                supplies = JArrayList.toArray(new String[i]);
                //Log.e("PictureActivity3", diy[i]);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //??????????????????Adapter/*????????????????????????type2[pos]*/
                    adapter5 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, supplies);
                    adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //???????????????????????????Spinner
                    supplies_spinner20.setAdapter(adapter5);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ???OkHttp????????????(????????????)
     */
    private void sendRequestWithOkHttpForReport() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                //??????LoginActivity???????????????
                SharedPreferences user_id = getSharedPreferences("user_id", MODE_PRIVATE);
                String user_id_data = user_id.getString("ID", "");
                String shop_name = String.valueOf(store_spinner.getSelectedItem());
                String report_date = date_btn.getText().toString();
                String date_amount = day_performance_edt.getText().toString();
                String visitors_count = String.valueOf(count_spinner.getSelectedItem());
                String return_count = return_count_edt.getText().toString();
                String return_money = return_money_edt.getText().toString();
                String unsold_reason = reason_edt.getText().toString();
                String customer_count = String.valueOf(quantity_spinner.getSelectedItem());
                String q_response = response_edt.getText().toString();

                String item_name1 = String.valueOf(supplies_spinner1.getSelectedItem());
                String item_count1 = count_edt1.getText().toString();
                String item_amount1 = money_edt1.getText().toString();
                String item_name2 = String.valueOf(supplies_spinner2.getSelectedItem());
                String item_count2 = count_edt2.getText().toString();
                String item_amount2 = money_edt2.getText().toString();
                String item_name3 = String.valueOf(supplies_spinner3.getSelectedItem());
                String item_count3 = count_edt3.getText().toString();
                String item_amount3 = money_edt3.getText().toString();
                String item_name4 = String.valueOf(supplies_spinner4.getSelectedItem());
                String item_count4 = count_edt4.getText().toString();
                String item_amount4 = money_edt4.getText().toString();
                String item_name5 = String.valueOf(supplies_spinner5.getSelectedItem());
                String item_count5 = count_edt5.getText().toString();
                String item_amount5 = money_edt5.getText().toString();
                String item_name6 = String.valueOf(supplies_spinner6.getSelectedItem());
                String item_count6 = count_edt6.getText().toString();
                String item_amount6 = money_edt6.getText().toString();
                String item_name7 = String.valueOf(supplies_spinner7.getSelectedItem());
                String item_count7 = count_edt7.getText().toString();
                String item_amount7 = money_edt7.getText().toString();
                String item_name8 = String.valueOf(supplies_spinner8.getSelectedItem());
                String item_count8 = count_edt8.getText().toString();
                String item_amount8 = money_edt8.getText().toString();
                String item_name9 = String.valueOf(supplies_spinner9.getSelectedItem());
                String item_count9 = count_edt9.getText().toString();
                String item_amount9 = money_edt9.getText().toString();
                String item_name10 = String.valueOf(supplies_spinner10.getSelectedItem());
                String item_count10 = count_edt10.getText().toString();
                String item_amount10 = money_edt10.getText().toString();
                String item_name11 = String.valueOf(supplies_spinner11.getSelectedItem());
                String item_count11 = count_edt11.getText().toString();
                String item_amount11 = money_edt11.getText().toString();
                String item_name12 = String.valueOf(supplies_spinner12.getSelectedItem());
                String item_count12 = count_edt12.getText().toString();
                String item_amount12 = money_edt12.getText().toString();
                String item_name13 = String.valueOf(supplies_spinner13.getSelectedItem());
                String item_count13 = count_edt13.getText().toString();
                String item_amount13 = money_edt13.getText().toString();
                String item_name14 = String.valueOf(supplies_spinner14.getSelectedItem());
                String item_count14 = count_edt14.getText().toString();
                String item_amount14 = money_edt14.getText().toString();
                String item_name15 = String.valueOf(supplies_spinner15.getSelectedItem());
                String item_count15 = count_edt15.getText().toString();
                String item_amount15 = money_edt15.getText().toString();
                String item_name16 = String.valueOf(supplies_spinner16.getSelectedItem());
                String item_count16 = count_edt16.getText().toString();
                String item_amount16 = money_edt16.getText().toString();
                String item_name17 = String.valueOf(supplies_spinner17.getSelectedItem());
                String item_count17 = count_edt17.getText().toString();
                String item_amount17 = money_edt17.getText().toString();
                String item_name18 = String.valueOf(supplies_spinner18.getSelectedItem());
                String item_count18 = count_edt18.getText().toString();
                String item_amount18 = money_edt18.getText().toString();
                String item_name19 = String.valueOf(supplies_spinner19.getSelectedItem());
                String item_count19 = count_edt19.getText().toString();
                String item_amount19 = money_edt19.getText().toString();
                String item_name20 = String.valueOf(supplies_spinner20.getSelectedItem());
                String item_count20 = count_edt20.getText().toString();
                String item_amount20 = money_edt20.getText().toString();

                if (return_count.equals("")) {
                    return_count = "0";
                }
                if (return_money.equals("")) {
                    return_money = "0";
                }

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("USER_ID", user_id_data)
                            .add("SHOP_ID", shop_name)
                            .add("BUSINESS_DATE", report_date)
                            .add("DAY_AMOUNT", date_amount)
                            .add("VISITORS_COUNT", visitors_count)
                            .add("RETURN_COUNT", return_count)
                            .add("RETURN_MONEY", return_money)
                            .add("UNSOLD_REASON", unsold_reason)
                            .add("CUSTOMER_COUNT", customer_count)
                            .add("Q_RESPONSE", q_response)

                            .add("ITEM_NAME1", item_name1)
                            .add("ITEM_COUNT1", item_count1)
                            .add("ITEM_AMOUNT1", item_amount1)
                            .add("ITEM_NAME2", item_name2)
                            .add("ITEM_COUNT2", item_count2)
                            .add("ITEM_AMOUNT2", item_amount2)
                            .add("ITEM_NAME3", item_name3)
                            .add("ITEM_COUNT3", item_count3)
                            .add("ITEM_AMOUNT3", item_amount3)
                            .add("ITEM_NAME4", item_name4)
                            .add("ITEM_COUNT4", item_count4)
                            .add("ITEM_AMOUNT4", item_amount4)
                            .add("ITEM_NAME5", item_name5)
                            .add("ITEM_COUNT5", item_count5)
                            .add("ITEM_AMOUNT5", item_amount5)
                            .add("ITEM_NAME6", item_name6)
                            .add("ITEM_COUNT6", item_count6)
                            .add("ITEM_AMOUNT6", item_amount6)
                            .add("ITEM_NAME7", item_name7)
                            .add("ITEM_COUNT7", item_count7)
                            .add("ITEM_AMOUNT7", item_amount7)
                            .add("ITEM_NAME8", item_name8)
                            .add("ITEM_COUNT8", item_count8)
                            .add("ITEM_AMOUNT8", item_amount8)
                            .add("ITEM_NAME9", item_name9)
                            .add("ITEM_COUNT9", item_count9)
                            .add("ITEM_AMOUNT9", item_amount9)
                            .add("ITEM_NAME10", item_name10)
                            .add("ITEM_COUNT10", item_count10)
                            .add("ITEM_AMOUNT10", item_amount10)
                            .add("ITEM_NAME11", item_name11)
                            .add("ITEM_COUNT11", item_count11)
                            .add("ITEM_AMOUNT11", item_amount11)
                            .add("ITEM_NAME12", item_name12)
                            .add("ITEM_COUNT12", item_count12)
                            .add("ITEM_AMOUNT12", item_amount12)
                            .add("ITEM_NAME13", item_name13)
                            .add("ITEM_COUNT13", item_count13)
                            .add("ITEM_AMOUNT13", item_amount13)
                            .add("ITEM_NAME14", item_name14)
                            .add("ITEM_COUNT14", item_count14)
                            .add("ITEM_AMOUNT14", item_amount14)
                            .add("ITEM_NAME15", item_name15)
                            .add("ITEM_COUNT15", item_count15)
                            .add("ITEM_AMOUNT15", item_amount15)
                            .add("ITEM_NAME16", item_name16)
                            .add("ITEM_COUNT16", item_count16)
                            .add("ITEM_AMOUNT16", item_amount16)
                            .add("ITEM_NAME17", item_name17)
                            .add("ITEM_COUNT17", item_count17)
                            .add("ITEM_AMOUNT17", item_amount17)
                            .add("ITEM_NAME18", item_name18)
                            .add("ITEM_COUNT18", item_count18)
                            .add("ITEM_AMOUNT18", item_amount18)
                            .add("ITEM_NAME19", item_name19)
                            .add("ITEM_COUNT19", item_count19)
                            .add("ITEM_AMOUNT19", item_amount19)
                            .add("ITEM_NAME20", item_name20)
                            .add("ITEM_COUNT20", item_count20)
                            .add("ITEM_AMOUNT20", item_amount20)

                            .build();
                    Log.e(LOG, user_id_data);
                    Log.e(LOG, shop_name);
                    Log.e(LOG, report_date);
                    Log.e(LOG, date_amount);
                    Log.e(LOG, visitors_count);
                    Log.e(LOG, return_count);
                    Log.e(LOG, return_money);
                    Log.e(LOG, unsold_reason);
                    Log.e(LOG, customer_count);
                    Log.e(LOG, q_response);
                    Request request = new Request.Builder()
                            //.url("http://192.168.0.172/WQP_OS/StationShopBusinessReport.php")
                            .url("http://a.wqp-water.com.tw/WQP_OS/StationShopBusinessReport.php")
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
     * ???OkHttp????????????(??????????????????)
     */
    private void sendRequestWithOkHttpForOrderPhoto() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                //??????LoginActivity???????????????
                SharedPreferences user_id = getSharedPreferences("user_id", MODE_PRIVATE);
                String user_id_data = user_id.getString("ID", "");
                String shop_name = String.valueOf(store_spinner.getSelectedItem());
                String report_date = date_btn.getText().toString();

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("USER_ID", user_id_data)
                            .add("SHOP_ID", shop_name)
                            .add("BUSINESS_DATE", report_date)
                            .add("FILE_NAME", savePath)
                            .build();
                    Log.e(LOG, user_id_data);
                    Log.e(LOG, shop_name);
                    Log.e(LOG, report_date);
                    Request request = new Request.Builder()
                            //.url("http://192.168.0.172/WQP_OS/StationShopBusinessFile.php")
                            .url("http://a.wqp-water.com.tw/WQP_OS/StationShopBusinessFile.php")
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
     * ?????????????????? ???????????? ????????? ????????? ????????? ????????????
     */
    private void sendRequestWithOkHttpForReportCheck() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //??????LoginActivity???????????????
                SharedPreferences user_id = getSharedPreferences("user_id", MODE_PRIVATE);
                String user_id_data = user_id.getString("ID", "");
                String shop_name = String.valueOf(store_spinner.getSelectedItem());
                String report_date = date_btn.getText().toString();

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("USER_ID", user_id_data)
                            .add("SHOP_ID", shop_name)
                            .add("BUSINESS_DATE", report_date)
                            .add("FILE_NAME", savePath)
                            .build();
                    Log.e(LOG, user_id_data);
                    Log.e(LOG, shop_name);
                    Log.e(LOG, report_date);
                    Request request = new Request.Builder()
                            //.url("http://192.168.0.172/WQP_OS/StationShopBusinessCheck.php")
                            .url("http://a.wqp-water.com.tw/WQP_OS/StationShopBusinessCheck.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.i(LOG, responseData);
                    parseJSONWithJSONObjectOfReportCheck(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * ??????JSON??????????????????String??????
     *???TextView???SHOW????????????????????????
     * @param jsonData
     */
    private void parseJSONWithJSONObjectOfReportCheck(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON??????????????????
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String check_report = jsonObject.getString("COUNT");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        check_count_txt.setText(check_report);
                    }
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                        Toast.makeText(StationReportActivity.this, R.string.canceled, Toast.LENGTH_LONG).show();
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