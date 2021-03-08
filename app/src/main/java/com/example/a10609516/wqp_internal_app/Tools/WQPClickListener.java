package com.example.a10609516.wqp_internal_app.Tools;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.a10609516.wqp_internal_app.Basic.HomeActivity;
import com.example.a10609516.wqp_internal_app.Basic.RequisitionActivity;
import com.example.a10609516.wqp_internal_app.Basic.RequisitionSearchActivity;
import com.example.a10609516.wqp_internal_app.Basic.VersionActivity;
import com.example.a10609516.wqp_internal_app.Boss.ApplyExchangeActivity;
import com.example.a10609516.wqp_internal_app.Clerk.QuotationActivity;
import com.example.a10609516.wqp_internal_app.DepartmentAndDIY.StationReportActivity;
import com.example.a10609516.wqp_internal_app.DepartmentAndDIY.StationReportSearchActivity;
import com.example.a10609516.wqp_internal_app.Manager.InventoryActivity;
import com.example.a10609516.wqp_internal_app.Manager.OrderSearchActivity;
import com.example.a10609516.wqp_internal_app.R;
import com.example.a10609516.wqp_internal_app.Works.CalendarActivity;
import com.example.a10609516.wqp_internal_app.Works.EngPointsActivity;
import com.example.a10609516.wqp_internal_app.Works.GPSActivity;
import com.example.a10609516.wqp_internal_app.Works.MissCountActivity;
import com.example.a10609516.wqp_internal_app.Works.MissionActivity;
import com.example.a10609516.wqp_internal_app.Works.PointsActivity;
import com.example.a10609516.wqp_internal_app.Works.ScheduleActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class WQPClickListener extends WQPToolsActivity implements View.OnClickListener {

    private Context mContext;
    private String user_id_data;
    private String home, exchange, schedule, calendar, mission,
            bonus, points, miss_report, gps, quotation,
            report, report_search, inventory, picking, requisition,
            progress, version_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * 與OkHttp建立連線(Menu權限)
     */
    private void sendRequestWithOkHttpForMenuAuthority() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //接收LoginActivity傳過來的值
                SharedPreferences user_id = mContext.getSharedPreferences("user_id", MODE_PRIVATE);
                user_id_data = user_id.getString("ID", "");

                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("U_ACC", user_id_data)
                            .build();
                    Log.e("MENU", user_id_data);
                    Request request = new Request.Builder()
                            .url("http://192.168.0.172/WQP_OS/UserMenuAuthority.php")
                            //.url("http://a.wqp-water.com.tw/WQP_OS/UserMenuAuthority.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.e("MENU", request.toString());
                    Log.e("MENU", requestBody.toString());
                    Log.e("MENU", response.toString());
                    Log.e("MENU", responseData);
                    parseJSONWithJSONObjectForMenuAuthority(responseData);
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
    private void parseJSONWithJSONObjectForMenuAuthority(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                //JSON格式改為字串
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                home =jsonObject.getString("HOME");
                exchange =jsonObject.getString("EXCHANGE");
                schedule =jsonObject.getString("SCHEDULE");
                calendar =jsonObject.getString("CALENDAR");
                mission =jsonObject.getString("MISSION");
                bonus =jsonObject.getString("BONUS");
                points =jsonObject.getString("POINTS");
                miss_report =jsonObject.getString("MISS_REPORT");
                gps =jsonObject.getString("GPS");
                quotation =jsonObject.getString("QUOTATION");
                report =jsonObject.getString("REPORT");
                report_search =jsonObject.getString("REPORT_SEARCH");
                inventory =jsonObject.getString("INVENTORY");
                picking =jsonObject.getString("PICKING");
                requisition =jsonObject.getString("REQUISITION");
                progress =jsonObject.getString("PROGRESS");
                version_info =jsonObject.getString("VERSION_INFO");

                Log.e("MENU : home -", home);
                Log.e("MENU : exchange -", exchange);
                Log.e("MENU : schedule -", schedule);
                Log.e("MENU : calendar -", calendar);
                Log.e("MENU : mission -", mission);
                Log.e("MENU : bonus -", bonus);
                Log.e("MENU : points -", points);
                Log.e("MENU : miss_report -", miss_report);
                Log.e("MENU : gps -", gps);
                Log.e("MENU : quotation -", quotation);
                Log.e("MENU : report -", report);
                Log.e("MENU : report_search -", report_search);
                Log.e("MENU : inventory -", inventory);
                Log.e("MENU : picking -", picking);
                Log.e("MENU : requisition -", requisition);
                Log.e("MENU : progress -", progress);
                Log.e("MENU : version_info -", version_info);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        mContext = v.getContext();

        sendRequestWithOkHttpForMenuAuthority();



        switch (v.getId()) {
            // Your click even code for all activities
            case R.id.home_txt:
                try{
                    // delay 1 second
                    Thread.sleep(200);
                    if (home.equals("1")) {
                        Intent intent0 = new Intent(mContext, HomeActivity.class);
                        mContext.startActivity(intent0);
                    } else {
                        Toast.makeText(mContext, "【首頁】無執行權限", Toast.LENGTH_SHORT).show();
                    }
                    break;
                } catch(InterruptedException e){
                    e.printStackTrace();
                }
                break;

            case R.id.exchange_txt:
                try{
                    // delay 1 second
                    Thread.sleep(200);
                    if (exchange.equals("1")) {
                        Intent intent10 = new Intent(mContext, ApplyExchangeActivity.class);
                        mContext.startActivity(intent10);
                    } else {
                        Toast.makeText(mContext, "【換貨申請單】無執行權限", Toast.LENGTH_SHORT).show();
                    }
                    break;
                } catch(InterruptedException e){
                    e.printStackTrace();
                }
                break;

            case R.id.schedule_txt:
                try{
                    // delay 1 second
                    Thread.sleep(200);
                    if (schedule.equals("1")) {
                        Intent intent11 = new Intent(mContext, ScheduleActivity.class);
                        mContext.startActivity(intent11);
                    } else {
                        Toast.makeText(mContext, "【行程資訊】無執行權限", Toast.LENGTH_SHORT).show();
                    }
                    break;
                } catch(InterruptedException e){
                    e.printStackTrace();
                }
                break;

            case R.id.calendar_txt:
                try{
                    // delay 1 second
                    Thread.sleep(200);
                    if (calendar.equals("1")) {
                        Intent intent12 = new Intent(mContext, CalendarActivity.class);
                        mContext.startActivity(intent12);
                    } else {
                        Toast.makeText(mContext, "【派工行事曆】無執行權限", Toast.LENGTH_SHORT).show();
                    }
                    break;
                } catch(InterruptedException e){
                    e.printStackTrace();
                }
                break;

            case R.id.mission_txt:
                try{
                    // delay 1 second
                    Thread.sleep(200);
                    if (mission.equals("1")) {
                        Intent intent13 = new Intent(mContext, MissionActivity.class);
                        mContext.startActivity(intent13);
                    } else {
                        Toast.makeText(mContext, "【派工任務】無執行權限", Toast.LENGTH_SHORT).show();
                    }
                    break;
                } catch(InterruptedException e){
                    e.printStackTrace();
                }
                break;

            case R.id.bonus_txt:
                try{
                    // delay 1 second
                    Thread.sleep(200);
                    if (bonus.equals("1")) {
                        Intent intent14 = new Intent(mContext, PointsActivity.class);
                        mContext.startActivity(intent14);
                    } else {
                        Toast.makeText(mContext, "【點數總覽】無執行權限", Toast.LENGTH_SHORT).show();
                    }
                    break;
                } catch(InterruptedException e){
                    e.printStackTrace();
                }
                break;

            case R.id.points_txt:
                try{
                    // delay 1 second
                    Thread.sleep(200);
                    if (points.equals("1")) {
                        Intent intent15 = new Intent(mContext, EngPointsActivity.class);
                        mContext.startActivity(intent15);
                    } else {
                        Toast.makeText(mContext, "【點數明細】無執行權限", Toast.LENGTH_SHORT).show();
                    }
                    break;
                } catch(InterruptedException e){
                    e.printStackTrace();
                }
                break;

            case R.id.miss_report_txt:
                try{
                    // delay 1 second
                    Thread.sleep(200);
                    if (miss_report.equals("1")) {
                        Intent intent16 = new Intent(mContext, MissCountActivity.class);
                        mContext.startActivity(intent16);
                    } else {
                        Toast.makeText(mContext, "【各區未回報數量】無執行權限", Toast.LENGTH_SHORT).show();
                    }
                    break;
                } catch(InterruptedException e){
                    e.printStackTrace();
                }
                break;

            case R.id.gps_txt:
                try{
                    // delay 1 second
                    Thread.sleep(200);
                    if (gps.equals("1")) {
                        Intent intent17 = new Intent(mContext, GPSActivity.class);
                        mContext.startActivity(intent17);
                    } else {
                        Toast.makeText(mContext, "【工務打卡GPS】無執行權限", Toast.LENGTH_SHORT).show();
                    }
                    break;
                } catch(InterruptedException e){
                    e.printStackTrace();
                }
                break;

            case R.id.quotation_txt:
                try{
                    // delay 1 second
                    Thread.sleep(200);
                    if (quotation.equals("1")) {
                        Intent intent20 = new Intent(mContext, QuotationActivity.class);
                        mContext.startActivity(intent20);
                    } else {
                        Toast.makeText(mContext, "【報價單審核】無執行權限", Toast.LENGTH_SHORT).show();
                    }
                    break;
                } catch(InterruptedException e){
                    e.printStackTrace();
                }
                break;

            case R.id.report_txt:
                try{
                    // delay 1 second
                    Thread.sleep(200);
                    if (report.equals("1")) {
                        Intent intent30 = new Intent(mContext, StationReportActivity.class);
                        mContext.startActivity(intent30);
                    } else {
                        Toast.makeText(mContext, "【日報上傳作業】無執行權限", Toast.LENGTH_SHORT).show();
                    }
                    break;
                } catch(InterruptedException e){
                    e.printStackTrace();
                }
                break;

            case R.id.report_search_txt:
                try{
                    // delay 1 second
                    Thread.sleep(200);
                    if (report_search.equals("1")) {
                        Intent intent31 = new Intent(mContext, StationReportSearchActivity.class);
                        mContext.startActivity(intent31);
                    } else {
                        Toast.makeText(mContext, "【日報查詢/修正】無執行權限", Toast.LENGTH_SHORT).show();
                    }
                    break;
                } catch(InterruptedException e){
                    e.printStackTrace();
                }
                break;

            case R.id.inventory_txt:
                try{
                    // delay 1 second
                    Thread.sleep(200);
                    if (inventory.equals("1")) {
                        Intent intent40 = new Intent(mContext, InventoryActivity.class);
                        mContext.startActivity(intent40);
                    } else {
                        Toast.makeText(mContext, "【盤點單】無執行權限", Toast.LENGTH_SHORT).show();
                    }
                    break;
                } catch(InterruptedException e){
                    e.printStackTrace();
                }
                break;

            case R.id.picking_txt:
                try{
                    // delay 1 second
                    Thread.sleep(200);
                    if (picking.equals("1")) {
                        Intent intent41 = new Intent(mContext, OrderSearchActivity.class);
                        mContext.startActivity(intent41);
                    } else {
                        Toast.makeText(mContext, "【撿料單】無執行權限", Toast.LENGTH_SHORT).show();
                    }
                    break;
                } catch(InterruptedException e){
                    e.printStackTrace();
                }
                break;

            case R.id.requisition_txt:
                try{
                    // delay 1 second
                    Thread.sleep(200);
                    if (requisition.equals("1")) {
                        Intent intent50 = new Intent(mContext, RequisitionActivity.class);
                        mContext.startActivity(intent50);
                    } else {
                        Toast.makeText(mContext, "【需求申請單】無執行權限", Toast.LENGTH_SHORT).show();
                    }
                    break;
                } catch(InterruptedException e){
                    e.printStackTrace();
                }
                break;

            case R.id.progress_txt:
                try{
                    // delay 1 second
                    Thread.sleep(200);
                    if (progress.equals("1")) {
                        Intent intent51 = new Intent(mContext, RequisitionSearchActivity.class);
                        mContext.startActivity(intent51);
                    } else {
                        Toast.makeText(mContext, "【進度查詢】無執行權限", Toast.LENGTH_SHORT).show();
                    }
                    break;
                } catch(InterruptedException e){
                    e.printStackTrace();
                }
                break;

            case R.id.version_info_txt:
                try{
                    // delay 1 second
                    Thread.sleep(200);
                    if (version_info.equals("1")) {
                        Intent intent60 = new Intent(mContext, VersionActivity.class);
                        mContext.startActivity(intent60);
                    } else {
                        Toast.makeText(mContext, "【版本資訊】無執行權限", Toast.LENGTH_SHORT).show();
                    }
                    break;
                } catch(InterruptedException e){
                    e.printStackTrace();
                }
                break;

            default:
                break;
        }
    }
}
