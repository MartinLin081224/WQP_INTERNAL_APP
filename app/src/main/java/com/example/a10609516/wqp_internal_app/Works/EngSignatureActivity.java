package com.example.a10609516.wqp_internal_app.Works;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.a10609516.wqp_internal_app.R;
import com.example.a10609516.wqp_internal_app.Tools.SignView;
import com.example.a10609516.wqp_internal_app.Tools.WQPToolsActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EngSignatureActivity extends WQPToolsActivity {

    private SignView mView;
    private Button commit_btn,clear_btn;
    private Bitmap mSignBitmap;
    String signPath;
    String sign_name;

    private OkHttpClient client;

    private String LOG = "EngSignatureActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eng_signature);
        //動態取得 View 物件
        initViews();
        client = new OkHttpClient();
    }

    /**
     * 動態取得 View 物件
     */
    private void initViews() {
        mView = (SignView) findViewById(R.id.signView);
        commit_btn = (Button) findViewById(R.id.commit_btn);
        clear_btn = (Button) findViewById(R.id.clear_btn);
        commit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                saveSign(mView.getCachebBitmap());
                sendRequestWithOkHttpOfSignature();
                finish();
            }
        });
        clear_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mView.clear();
            }
        });
    }

    /**
     * signPath是圖片保存路徑
     * @param bit
     */
    public void saveSign(Bitmap bit){
        //儲存路徑
        mSignBitmap = bit;
        signPath = createFile();
    }

    /**
     * 生成Bitmap儲存簽名
     * @return
     */
    public String createFile() {
        //建立簽名檔儲存
        ByteArrayOutputStream byteArrayOutputStream = null;
        String _path = null;
        try {
            //接收LoginActivity傳過來的值
            SharedPreferences user_id = getSharedPreferences("user_id" , MODE_PRIVATE);
            String user_id_data = user_id.getString("ID" , "");
            Log.e(LOG,user_id_data);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String date = simpleDateFormat.format(new java.util.Date());
            Bundle bundle = getIntent().getExtras();
            String rm001 = bundle.getString("rm001");
            String rm002 = bundle.getString("rm002");
            String sign_dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/";
            _path = sign_dir + "Sign_"  + rm001 + "-" + rm002 + "_" + user_id_data + "_" + date +".png";
            sign_name = "Sign_" + rm001 + "-" + rm002 + "_" + user_id_data + "_" + date;
            Log.e("TAG",_path);
            byteArrayOutputStream = new ByteArrayOutputStream();
            mSignBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] photoBytes = byteArrayOutputStream.toByteArray();
            if (photoBytes != null) {
                new FileOutputStream(new File(_path)).write(photoBytes);
                //與OKHttp連線上傳簽名到SEVER端
                MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("img_1", sign_name + ".png", RequestBody.create(MediaType.parse("image/png"),photoBytes));
                MultipartBody build = builder.build();
                Request bi = new Request.Builder()
                        .url("http://a.wqp-water.com.tw/WQP_OS/SignaturePicture.php")
                        .post(build)
                        .build();
                client.newCall(bi).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.i(LOG,"onFailure : 失敗");
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.i(LOG,"onResponse : "+response.body().string());
                        //提交成功處理結果
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (byteArrayOutputStream != null)
                    byteArrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return _path;
    }

    /**
     * 與OkHttp建立連線(SignaturePicture)
     */
    private void sendRequestWithOkHttpOfSignature() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //接收LoginActivity傳過來的值
                SharedPreferences user_id = getSharedPreferences("user_id_data", MODE_PRIVATE);
                String user_id_data = user_id.getString("ID", "");
                Log.i(LOG, user_id_data);

                Bundle bundle = getIntent().getExtras();
                String rm001 = bundle.getString("rm001");
                String rm002 = bundle.getString("rm002");
                try {
                    OkHttpClient client = new OkHttpClient();
                    //POST
                    RequestBody requestBody = new FormBody.Builder()
                            .add("User_id", user_id_data)
                            .add("RM001", rm001)
                            .add("RM002", rm002)
                            .add("SIGN_FILE_NAME", sign_name + ".png")
                            .build();
                    Log.e(LOG, user_id_data);
                    Log.e(LOG, rm001);
                    Log.e(LOG, rm002);
                    Log.e(LOG, sign_name + ".png");
                    Request request = new Request.Builder()
                            .url("http://a.wqp-water.com.tw/WQP_OS/WorkSignatureLog.php")
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
        if (mSignBitmap != null) {
            mSignBitmap.recycle();
        }
    }
}
