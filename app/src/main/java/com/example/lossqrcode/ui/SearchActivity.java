package com.example.lossqrcode.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.lossqrcode.MyApplication;
import com.example.lossqrcode.R;
import com.example.lossqrcode.entity.DataDownloadEntity;
import com.example.lossqrcode.service.ApiService;
import com.example.lossqrcode.ui.widget.LoadingDialog;
import com.example.lossqrcode.ui.widget.MyDialog;
import com.jy.third.pjhs.dto.DataDownloadReq;
import com.jy.third.pjhs.dto.NLBaseJson;
import com.jy.third.pjhs.dto.NLDataDownloadResp;
import com.zbar.lib.CaptureActivity;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static com.android.volley.Response.ErrorListener;
import static com.android.volley.Response.Listener;

public class SearchActivity extends BaseActivity implements
        View.OnClickListener {
    private RequestQueue requestQueue;
    private String barcode = "";
    private SearchDataAdapter adapter;
    private ListView searchList;
    private LoadingDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setBarLeft(R.drawable.selector_left_arrow, "查询配件");
        setBarLeftOnclickListener(this);
        setBarRightString("手动输入");
        setBarRightLeftString("扫描");
        setBarRightOnclickListener(this);
        setBarRightLeftOnclickListener(this);
        searchList = (ListView) findViewById(R.id.search_list);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_bar_left:
                finish();
                break;
            case R.id.btn_bar_right:
                showInputCodeDialog();
                break;
            case R.id.btn_bar_rightleft:
                Intent intent = new Intent();
                intent.setClass(SearchActivity.this,
                        CaptureActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, 1);
                break;
            default:
                break;
        }
    }

    private void showInputCodeDialog() {
        final MyDialog inputDialog = new MyDialog(this,
                R.layout.dialog_input_barcode);
        final EditText etInputCode = (EditText) inputDialog
                .findViewById(R.id.et_barcode);
        TextView tvCancel = (TextView) inputDialog.findViewById(R.id.tv_cancel);
        TextView tvSure = (TextView) inputDialog.findViewById(R.id.tv_sure);
        tvCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                inputDialog.dismiss();
            }
        });
        tvSure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String barCodeStr = etInputCode.getText().toString().trim();
                if (TextUtils.isEmpty(barCodeStr)) {
                    Toast.makeText(SearchActivity.this,
                            R.string.prompt_manual_input_barcode,
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                inputDialog.dismiss();
                barcode=barCodeStr;
                searchData();
            }
        });
        inputDialog.show();
        showInputMethod(etInputCode);
    }

    private void showInputMethod(final View v) {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(0,
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 200);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    // 此处获取扫描结果信息
                    barcode = bundle.getString("EXTRA_SCAN_DATA");
                    if (TextUtils.isEmpty(barcode)) {
                        Toast.makeText(SearchActivity.this, "请重新扫描",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    try {
                        barcode = new String(barcode.getBytes("utf-8"), "GB2312");
                        Log.i("barcode", barcode);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    searchData();
                }
                break;
        }
    }

    public void searchData() {
        progressDialog = new LoadingDialog(this, "请稍后");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String username = ((MyApplication) getApplicationContext()).getUsername();
        DataDownloadReq request = new DataDownloadReq();
        request.setUsername(username);
        request.setAppNo(barcode);
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(ApiService.searchData(request, new Listener<NLBaseJson<NLDataDownloadResp>>() {
            @Override
            public void onResponse(NLBaseJson<NLDataDownloadResp> response) {
                progressDialog.dismiss();
                String code = response.getData().getCode();
                if ("0641".equals(code)) {
                    String data= response.getData().toString();
                    Log.i("data", response.getData().toString());
                    List<DataDownloadEntity> downloadList = response.getData().getList();
                    if (downloadList.size()>0) {
                        adapter = new SearchDataAdapter(downloadList);
                        searchList.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }else {
                        Toast.makeText(SearchActivity.this, "未查找到数据", Toast.LENGTH_SHORT).show();
                    }
                } else if ("0649".equals(code)) {
                    progressDialog.dismiss();
                    Toast.makeText(SearchActivity.this, "查找数据失败", Toast.LENGTH_SHORT).show();
                }
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                Toast.makeText(SearchActivity.this, "查找数据失败", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    class SearchDataAdapter extends BaseAdapter {
        private List<DataDownloadEntity> datas;

        public SearchDataAdapter(List<DataDownloadEntity> datas) {
            this.datas = datas;
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(SearchActivity.this).inflate(R.layout.item_search,
                        null);
                holder = new ViewHolder();
                holder.textGroup = (TextView) convertView.findViewById(R.id.carGroupName);
                holder.textVehicle = (TextView) convertView.findViewById(R.id.carVehicleName);
                holder.textPartName = (TextView) convertView.findViewById(R.id.partName);
                holder.textAppNo = (TextView) convertView.findViewById(R.id.appNo);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.textGroup.setText(datas.get(position).getVehSeriName());
            holder.textVehicle.setText(datas.get(position).getCxmc());
            holder.textPartName.setText(datas.get(position).getLjmc());
            String barcodeString=datas.get(position).getBarCode();
            holder.textAppNo.setText(barcodeString.substring(barcodeString.lastIndexOf(",")+1,barcodeString.length()));
            return convertView;
        }
    }

    static class ViewHolder {
        TextView textGroup, textVehicle, textPartName, textAppNo;
    }
}
