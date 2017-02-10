package com.example.lossqrcode.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.lossqrcode.MyApplication;
import com.example.lossqrcode.R;
import com.example.lossqrcode.adapter.ImageAdapter;
import com.example.lossqrcode.ui.widget.LoadingDialog;
import com.example.lossqrcode.utils.Constants;
import com.example.lossqrcode.utils.HttpUtil;
import com.example.lossqrcode.utils.ImageUtils;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


import com.example.lossqrcode.ui.ContinueUploadInputStreamEntity.ProgressListener;


public class PhotoActivity extends BaseActivity implements View.OnClickListener {

    private GridView gridview;
    private RequestQueue mQueue;
    private ImageAdapter imageAdapter;
    private ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
    private ArrayList<Bitmap> bitmapsNew = new ArrayList<Bitmap>();
    private ArrayList<Bitmap> bitmapsOld = new ArrayList<Bitmap>();
    private LoadingDialog dialog;
    private Handler mHandler;
    private static final int REQUEST_PICK_PHOTO = 0x001;
    private String id, ljmc, barghdh, barcph, partId, address = "";
    private String PATH = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + "/jyds/img/LossQRcode/";
    ;
    private ArrayList<String> paths;
    private Context context;
    private int dataLength;
    private int i, count;
    private Button btn_upload;
    private ProgressDialog progressDialog;
    String zipFilePath = PATH + File.separator
            + "fitAppTempFile2015" + ".zip";// 压缩包路径
    private int totalSize = 0;
    private int sentSize = 0;
    private long sendedLength = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        context = this;
        initView();
        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        handleUrl(msg.obj.toString());
//                        loadLocalImage();
                        if (dataLength == i) {
                            refreshAdapter();
                        }
                        break;
                    case 2:
                        refreshAdapter();
                        Toast.makeText(context, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                        PhotoActivity.this.finish();
                        break;
                    case 3:
                        Toast.makeText(context, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                        break;

                }
                super.handleMessage(msg);
            }
        };
    }

    //刷新适配器
    private void refreshAdapter() {
        bitmapsNew = loadLocalImage();
        bitmaps.clear();
        bitmaps.addAll(bitmapsOld);
        bitmaps.addAll(bitmapsNew);
        for (int j = 0; j < bitmapsOld.size(); j++) {
            paths.add(0, "");
        }
        Log.i("paths", paths.size() + "");
        imageAdapter = new ImageAdapter(bitmaps, context, paths);
        gridview.setAdapter(imageAdapter);
    }

    private void handleUrl(String jsonString) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonString);
            String data = jsonObject.getString("data");
            JSONObject jsonObject1 = new JSONObject(data);
            String code = jsonObject1.getString("code");
            String message = jsonObject1.getString("message");
            ArrayList<String> urls = new ArrayList<String>();
            if (code.equals("0631")) {
                dialog.dismiss();
                JSONArray jsonArray = jsonObject1.getJSONArray("piclist");
                dataLength = jsonArray.length();
                for (i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                    String url = jsonObject2.getString("url");
                    Log.i("url:::", url);
                    urls.add(url);
                }
                loadImage(urls);
            } else {
                dialog.dismiss();
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void initView() {
        setBarRightLeftString("图片");
        setBarLeft(R.drawable.selector_left_arrow, "返回");
        setBarLeftOnclickListener(this);
        setBarRightString("拍照");
        setBarRightOnclickListener(this);
        btn_upload = (Button) findViewById(R.id.btn_upload_photo);
        btn_upload.setOnClickListener(this);
        id = getIntent().getExtras().getString("id");
        ljmc = getIntent().getExtras().getString("ljmc");
        barghdh = getIntent().getExtras().getString("barghdh");
        barcph = getIntent().getExtras().getString("barcph");
        partId = getIntent().getExtras().getString("partId");
        gridview = (GridView) findViewById(R.id.gridViewImage);
        mQueue = Volley.newRequestQueue(this);
        loadUrl(id);
    }

    //加载本地图片
    private ArrayList<Bitmap> loadLocalImage() {

        paths = new ArrayList<String>();
        File f = new File(PATH + id);
        File[] files = f.listFiles();// 列出所有文件
        if (files != null) {
            bitmapsNew.clear();
//            int count = files.length;// 文件个数
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if (file.exists()) {

                    BitmapFactory.Options opts = new BitmapFactory.Options();
                    opts.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(file.getPath(), opts);
                    opts.inSampleSize = ImageUtils.computeSampleSize(opts, -1,
                            (int) (ImageUtils.getScreenH(context) * 0.5)
                                    * (int) (ImageUtils.getScreenW(context) * 0.4));
                    opts.inJustDecodeBounds = false;
                    opts.inInputShareable = true;
                    opts.inPurgeable = true;

                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(
                                file.getPath()), null, opts);
                        bitmapsNew.add(bitmap);
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (OutOfMemoryError e) {
                        // TODO: handle exception
                    }
                }
                paths.add(file.getPath());
                Log.i("file.getPath()", file.getPath());

            }
        }
        return bitmapsNew;
    }

    public String sendData(final String xmlData) throws Exception {
        final String[] dataString = {null};
        new Thread(new Runnable() {
            @Override
            public void run() {

                HttpClient httpClient = new HttpClient();
                httpClient.getHttpConnectionManager().getParams()
                        .setConnectionTimeout(20000);
                // 根据输入的ip地址和端口号 确定地址
                String url = Constants.URL;
                PostMethod postMethod = new PostMethod(url);
                // 指定请求内容的类型
                postMethod.setRequestHeader("Content-type", "text/html; charset=GBK");
                // 使用系统提供的默认的恢复策略,连不上得试三次
                postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                        new DefaultHttpMethodRetryHandler());
                try {
                    // 注入发送的内容
                    postMethod.setRequestEntity(new StringRequestEntity(xmlData, null,
                            null));
                    // 执行postMethod
                    int statusCode = httpClient.executeMethod(postMethod);
                    if (statusCode == HttpStatus.SC_OK) {
                        // 发送内容成功
                        byte[] responseBody = postMethod.getResponseBody();
                        // 处理内容
                        dataString[0] = new String(responseBody, "GBK");
                        Message message = new Message();
                        message.what = 1;
                        message.obj = dataString[0];
                        mHandler.sendMessage(message);
                    } else {// 失败
                        System.out.println("请求失败,状态码:" + statusCode);
                    }
                } catch (HttpException e) {
                    System.out.println("请求发生错误:" + e.getMessage());
                } catch (IOException e) {
                    System.out.println("请求发生错误:" + e.getMessage());
                } finally {
                    postMethod.releaseConnection();
                }
                Log.i("dataString", dataString[0]);
            }
        }).start();

        return dataString[0];
    }

    //请求借口获取url
    private void loadUrl(String id) {
        String username = ((MyApplication) getApplicationContext()).getUsername();
        dialog = new LoadingDialog(this, "正在请求图片...");
        dialog.show();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("flag", Constants.REQUEST_URL_DATA);
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("username", username);
            jsonObject1.put("remid", id);
            jsonObject.put("data", jsonObject1);
            sendData(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("json", jsonObject.toString());
    }

    //加载图片
    private void loadImage(ArrayList<String> urls) {
        for (int i = 0; i < urls.size(); i++) {
            ImageRequest imageRequest = new ImageRequest(
                    urls.get(i),
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {
                            bitmapsOld.add(response);
                            refreshAdapter();
                        }
                    }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
            mQueue.add(imageRequest);
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_bar_left:
                finish();
                break;
            //压缩上传照片
            case R.id.btn_upload_photo:
                compassImage();
                if (count > 0) {
                    new uploadTask().execute();
                }
                break;
            case R.id.btn_bar_right:
                //跳转拍照界面
                Intent photoIntent = new Intent(PhotoActivity.this, PhotoPicActivity.class);
                photoIntent.putExtra("id", id);
                photoIntent.putExtra("ljmc", ljmc);
                photoIntent.putExtra("barghdh", barghdh);
                photoIntent.putExtra("barcph", barcph);
                photoIntent.putExtra("partId", partId);
                startActivityForResult(photoIntent, REQUEST_PICK_PHOTO);
                break;
        }
    }

    class uploadTask extends AsyncTask<Void, Integer, Void> {
        boolean uploadSuccess = false;
        HttpPost httpPost = null;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(context, "请稍候", "正在上传");

        }

        @Override
        protected Void doInBackground(Void... params) {
            compassImage();
            int length = 0;
            File zipFile = new File(zipFilePath);
            if (zipFile.length() >= length) {
                try {
                    uploadSuccess = uploadFile(length, new OnProgressListener(
                            length));
                    if (uploadSuccess) {
                        if (zipFile.exists()) {
                            zipFile.delete();
                        }
                        publishProgress(3);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            super.onPostExecute(aVoid);
        }

        private boolean uploadFile(int position, ProgressListener lisenter)
                throws IOException {
            File zipFile = new File(zipFilePath);
            totalSize = (int) zipFile.length();
            publishProgress(1, totalSize);
            InputStream is = null;
            try {
                is = new FileInputStream(zipFile);
                is.skip(position);
                HttpContext localContext = new BasicHttpContext();
                HttpPost httpPost = new HttpPost(Constants.URL_UPLOAD_PHOTO);
                httpPost.addHeader("fileLength", String.valueOf(totalSize));
                ContinueUploadInputStreamEntity entity = new ContinueUploadInputStreamEntity(is, totalSize - position, lisenter);
                httpPost.setEntity(entity);
                HttpResponse response = HttpUtil.getHttpClient().execute(
                        httpPost, localContext);
                // 接收响应结果
                BufferedReader reader = null;
                String data = "";
                try {
                    reader = new BufferedReader(new InputStreamReader(response
                            .getEntity().getContent(), "GBK"));
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        data += line;
                    }
                } finally {
                    if (reader != null) {
                        reader.close();
                    }
                }
                // 解析结果
                try {
                    JSONObject json = new JSONObject(data);

                    JSONObject jsonObject = json.getJSONObject("data");
                    String responseFlag = jsonObject.getString("code");
                    String responseMessage = jsonObject.getString("message");
                    if (responseFlag.equals("0310")) {
                        //上传成功后删除本地文件 照片和压缩包
                        if (zipFile.exists()) {
                            zipFile.delete();
                        }
                        File fileDir = new File(PATH + id);
                        File[] imageList = fileDir.listFiles();
                        for (int j = 0; j < imageList.length; j++) {
                            File imageFile = imageList[j];
                            // 判断是不是文
                            if (imageFile.isFile() && imageFile.exists()) {
                                imageFile.delete();
                            }
                        }
                        progressDialog.dismiss();
                        Message message = new Message();
                        message.what = 2;
                        message.obj = responseMessage;
                        mHandler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = 2;
                        message.obj = responseMessage;
                        mHandler.sendMessage(message);
                    }
                    Log.i("message", responseMessage);
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (zipFile.exists()) {
                        zipFile.delete();
                    }
                    return false;
                }

            } finally {
                // 释放使用的流资源、网络资
                if (httpPost != null) {
                    httpPost.abort();
                    httpPost = null;
                }
                if (is != null) {
                    is.close();
                }
            }

            return uploadSuccess;

        }

        /**
         * 上传进度监听
         */
        private class OnProgressListener implements ProgressListener {
            int originalLength = 0;

            public OnProgressListener(int originalLength) {
                this.originalLength = originalLength;
            }

            @Override
            public void transferred(long num) {
                sentSize = this.originalLength + (int) num;
                sendedLength = num;
                publishProgress(2, sentSize);
            }

            @Override
            public void onUpload(InputStream in, OutputStream out) {
                if (isCancelled()) {
                    try {
                        in.close();
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

        }

        ;
    }

    //压缩图片
    private void compassImage() {
        //判断是否拍照
        count = loadLocalImage().size();
        if (count > 0) {
            String path = PATH + id;


            File zipFile = new File(zipFilePath);

            if (zipFile.exists()) {
                zipFile.delete();
            }
            File fileDir = new File(path);
            File[] imageList = fileDir.listFiles();
            ZipOutputStream outZip = null;
            // 1.创建文件
            try {
                zipFile.createNewFile();
                outZip = new ZipOutputStream(new FileOutputStream(zipFile));
                ZipEntry zipEntry = null;
                zipEntry = new ZipEntry("fitAppTempFile2015" + ".json");
                outZip.putNextEntry(zipEntry);
                outZip.write(commitContent().getBytes());
                Log.i("commitContent", commitContent());
                outZip.closeEntry();
                for (int j = 0; j < count; j++) {
                    int compassed = 0;// 已经压缩
                    File imageFile = imageList[j];
                    int compressTotal = (int) imageFile.length(); // 图片总大
                    // 判断是不是文
                    if (imageFile.isFile() && imageFile.exists()) {
                        zipEntry = new ZipEntry(imageFile.getName());
                        FileInputStream inputStream = new FileInputStream(imageFile.getAbsolutePath());
                        outZip.putNextEntry(zipEntry);

                        int len = -1;
                        byte[] buffer = new byte[4096];

                        while ((len = inputStream.read(buffer)) != -1) {
                            int end = len;
                            if ((compressTotal - compassed) < len) {
                                end = compressTotal - compassed;
                            }
                            outZip.write(buffer, 0, len);
                            compassed += end;// 累加已经上传的数据长

                        }
                        inputStream.close();
                        outZip.closeEntry();
                    }

                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (outZip != null) {
                    try {
                        outZip.close();
                    } catch (IOException ex) {

                    }
                }
            }

        } else {
            Toast.makeText(this, "未进行拍照，无需上传！", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        refreshAdapter();
    }

    private String commitContent() {
        JSONObject jsonObject1 = new JSONObject();
        String username = ((MyApplication) getApplicationContext())
                .getUsername();
        try {
            jsonObject1.put("goodNo", barghdh);
            jsonObject1.put("goodListId", id);
            jsonObject1.put("username", username);
            return jsonObject1.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
