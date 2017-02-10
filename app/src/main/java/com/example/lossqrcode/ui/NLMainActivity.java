package com.example.lossqrcode.ui;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.lossqrcode.MyApplication;
import com.example.lossqrcode.R;
import com.example.lossqrcode.entity.DataDownloadEntity;
import com.example.lossqrcode.service.ApiService;
import com.example.lossqrcode.service.RemnantGoodsProvider;
import com.example.lossqrcode.ui.widget.HomeButton;
import com.example.lossqrcode.ui.widget.HomeButton.HomeClickListener;
import com.example.lossqrcode.ui.widget.LoadingDialog;
import com.jy.third.pjhs.dto.DataDownloadReq;
import com.jy.third.pjhs.dto.NLBaseJson;
import com.jy.third.pjhs.dto.NLDataDownloadResp;
import com.zbar.lib.CaptureActivity;

public class NLMainActivity extends BaseActivity implements OnClickListener {

    private HomeButton bt_waitreceive, bt_scaned, bt_notfind;

    private LoadingDialog dialog;
    private RequestQueue requestQueue;
    private Intent intent;

    private HomeButton bt_saomiao;
    private final static int SCANNIN_GREQUEST_CODE = 1;
    private static final String IS_FIRST = "is_first_loading";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// ����
        setContentView(R.layout.activity_nlmain);

        initActionBar();
        iniView();
        loadRemnantGoods();

    }

    private void initActionBar() {
        setBarTitle("��������ƽ̨");
        setBarRightIcon(R.drawable.selector_download);
        setBarRightOnclickListener(this);
        getBtnRightLeft().setVisibility(View.GONE);
    }

    private void iniView() {
        bt_waitreceive = (HomeButton) findViewById(R.id.bt_waitreceive);
        bt_scaned = (HomeButton) findViewById(R.id.bt_scaned);
        bt_notfind = (HomeButton) findViewById(R.id.bt_notfind);

        bt_saomiao = (HomeButton) findViewById(R.id.bt_saomiao);

        bt_waitreceive.setOnHomeClick(new HomeClickListener() {

            @Override
            public void onclick() {
                intent = new Intent(NLMainActivity.this, NLWaitReceiveActivity.class);
                startActivity(intent);
            }
        });
        bt_scaned.setOnHomeClick(new HomeClickListener() {

            @Override
            public void onclick() {
                intent = new Intent(NLMainActivity.this, NLScanResultActivity.class);
                startActivity(intent);
            }
        });
        bt_notfind.setOnHomeClick(new HomeClickListener() {

            @Override
            public void onclick() {
                intent = new Intent(NLMainActivity.this, NLNotFoundPartActivity.class);
                startActivity(intent);
            }
        });


        bt_saomiao.setOnHomeClick(new HomeClickListener() {

            @Override
            public void onclick() {
                Intent intent = new Intent();
                intent.setClass(NLMainActivity.this, CaptureActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
            }
        });
        findViewById(R.id.searchLayout).setOnClickListener(this);
    }

    /**
     * ������
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    // �˴���ȡɨ������Ϣ
                    String barcode = bundle.getString("EXTRA_SCAN_DATA");
                    if (TextUtils.isEmpty(barcode)) {
                        Toast.makeText(NLMainActivity.this, "������ɨ��",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    try {
                        barcode = new String(barcode.getBytes("utf-8"), "GB2312");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    showScanResult(barcode);
                }
                break;
        }

    }

    private void showScanResult(String barcode) {
        DataDownloadEntity rgEntity = new DataDownloadEntity();
        if (barcode.contains(",")) {
            rgEntity.setBarCode(barcode);
        } else {
            rgEntity.setBarysth(barcode);
        }
        // �����ݿ����ݶԱȣ��Ƿ���ڱ��������
        List<DataDownloadEntity> rgList = RemnantGoodsProvider
                .getInstance(this).isExistedInDB(rgEntity);
        if (rgList != null && rgList.size() > 0) {
            DataDownloadEntity newRgEntity = rgList.get(0);
            if (newRgEntity.getState() != null
                    && newRgEntity.getState().equals("02")) {
                Toast.makeText(this, "������ѱ�ɨ��", Toast.LENGTH_SHORT).show();
            } else if ((newRgEntity.getState() == null)
                    || (newRgEntity.getState() != null && newRgEntity
                    .getState().equals("01"))) {// ����������Ϊ��ɨ�����
                newRgEntity.setScanDate(new Date());
                newRgEntity.setState("02");
                RemnantGoodsProvider.getInstance(this).updateHaveScannedPart2(
                        newRgEntity);
//				// ͬʱ���»����б�
//				partList.add(0, newRgEntity);
//				// ˢ���б�
//				adapter.loadMore(newRgEntity);
            }

        } else {
            Toast.makeText(this, "�Ҳ��������", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {

            case R.id.btn_bar_right:
                loadRemnantGoods();
                break;
            case R.id.searchLayout:
                //��ת����ѯ����
                startActivity(new Intent(this,SearchActivity.class));
                break;
            default:
                break;
        }
    }

    private void loadRemnantGoods() {
        dialog = new LoadingDialog(this, "���ڸ�������...");
        dialog.setCancelable(false);
        dialog.show();

        String username = ((MyApplication) getApplicationContext()).getUsername();
        DataDownloadReq request = new DataDownloadReq();
        request.setUsername(username);
        requestQueue = Volley.newRequestQueue(this);
//		requestQueue.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
        requestQueue
                .add(ApiService
                        .dataDownload(
                                request,
                                new Listener<NLBaseJson<NLDataDownloadResp>>() {

                                    @Override
                                    public void onResponse(
                                            NLBaseJson<NLDataDownloadResp> response) {

                                        String code = response.getData().getCode();

                                        if ("0220".equals(code)) {
                                            Toast.makeText(NLMainActivity.this, "�û���������", Toast.LENGTH_SHORT).show();
                                        } else if ("0199".equals(code)) {
                                            Toast.makeText(NLMainActivity.this, "�����XML��ʽ�쳣", Toast.LENGTH_SHORT).show();
                                        } else if ("0210".equals(code)) {
                                            List<DataDownloadEntity> downloadList = response.getData().getList();
//											Collections.reverse(downloadList);
                                            if (downloadList.size() > 0) {
                                                RemnantGoodsProvider.getInstance(NLMainActivity.this)
                                                        .saveDataDownload1(response.getData().getList());

                                                dialog.dismiss();
                                                Toast.makeText(NLMainActivity.this,
                                                        "���ݳ�ʼ���ɹ�",
                                                        Toast.LENGTH_SHORT).show();
                                            } else {
                                                dialog.dismiss();
                                                Toast.makeText(NLMainActivity.this,
                                                        "��������",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                    }
                                }, new ErrorListener() {

                                    @Override
                                    public void onErrorResponse(
                                            VolleyError error) {
                                        error.printStackTrace();
                                        dialog.dismiss();
                                        Toast.makeText(NLMainActivity.this,
                                                "���ݸ���ʧ��", Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                }));
    }

}
