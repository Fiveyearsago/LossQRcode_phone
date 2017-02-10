package com.example.lossqrcode;

import java.util.List;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.Volley;
import com.example.lossqrcode.entity.RemnantGoodsEntity;
import com.example.lossqrcode.service.ApiService;
import com.example.lossqrcode.service.RemnantGoodsProvider;
import com.example.lossqrcode.ui.widget.LoadingDialog;
import com.example.lossqrcode.utils.Constants;
import com.jy.third.pjhs.dto.Response;
import com.jy.third.pjhs.dto.fit.RemnantGood4AppDTO;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	/**
	 * 待收货
	 */
	private Button btnWaitRecGoods;
	/**
	 * 已收货
	 */
	private Button btnAlreadyRecGoods;

	/**
	 * 已扫描
	 */
	private Button btnHaveScanedGoods;

	private LoadingDialog dialog;

	private TextView tvLoadPartData;

	private RequestQueue requestQueue;

	private static final String IS_FIRST = "is_first_loading";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 竖屏
		setContentView(R.layout.activity_main);

		initData();

		btnWaitRecGoods = (Button) findViewById(R.id.btn_wait_receive_goods);
		btnHaveScanedGoods = (Button) findViewById(R.id.btn_have_scanned_goods);
		btnAlreadyRecGoods = (Button) findViewById(R.id.btn_already_receive_goods);
		tvLoadPartData = (TextView) findViewById(R.id.tv_load_data);
		tvLoadPartData.setOnClickListener(this);
		btnWaitRecGoods.setOnClickListener(this);
		btnHaveScanedGoods.setOnClickListener(this);
		btnAlreadyRecGoods.setOnClickListener(this);

	}

	private void initData() {
		boolean isFirst = getSharedPreferences(Constants.SP, MODE_PRIVATE)
				.getBoolean(IS_FIRST, true);
		if (isFirst) {
			// loadRemnantGoods();
		}

		// RemnantGoodsProvider.getInstance(this).getRemnantGoodsToTake();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_wait_receive_goods:
			startActivity(new Intent(this, WaitReceiveActivity.class));
			break;

		case R.id.btn_have_scanned_goods:
			Intent intent = new Intent(this, ScanResultActivity.class);
			startActivity(intent);
			break;

		case R.id.btn_already_receive_goods:
			startActivity(new Intent(this, NotFoundPartActivity.class));
			break;

		case R.id.tv_load_data:
			// RemnantGoodsProvider.getInstance(this).clearData();
			loadRemnantGoods();
			break;

		default:
			break;
		}
	}

	private void loadRemnantGoods() {
		dialog = new LoadingDialog(this, "正在更新数据...");
		dialog.show();

		String userId = ((MyApplication) getApplicationContext()).getUserId();
		RemnantGood4AppDTO<RemnantGoodsEntity> remnantGood4AppDTO = new RemnantGood4AppDTO<RemnantGoodsEntity>();
		requestQueue = Volley.newRequestQueue(this);
		requestQueue
				.add(ApiService
						.loadRemnantGoods(
								userId,
								remnantGood4AppDTO,
								new Listener<Response<RemnantGood4AppDTO<RemnantGoodsEntity>>>() {

									@Override
									public void onResponse(
											Response<RemnantGood4AppDTO<RemnantGoodsEntity>> response) {
										// TODO Auto-generated method stub
										if ("0".equals(response
												.getResponseType())) {
											getSharedPreferences(Constants.SP,
													MODE_PRIVATE)
													.edit()
													.putBoolean(IS_FIRST, false)
													.commit();
											//删除待收货数据，重新下载，且保存已扫描和未找到数据
											
											RemnantGoodsProvider
													.getInstance(
															MainActivity.this)
													.saveRemnantGoods(
															response.getData()
																	.getSearchResultList());
											printData(response.getData()
													.getSearchResultList());

											printData(RemnantGoodsProvider
													.getInstance(
															MainActivity.this)
													.getAllData());
											dialog.dismiss();
											Toast.makeText(MainActivity.this,
													"数据初始化成功",
													Toast.LENGTH_SHORT).show();
										} else {
											dialog.dismiss();
											Toast.makeText(MainActivity.this,
													response.getErrMsg(),
													Toast.LENGTH_SHORT).show();
										}

									}
								}, new ErrorListener() {

									@Override
									public void onErrorResponse(
											VolleyError error) {
										// TODO Auto-generated method stub
										error.printStackTrace();
										dialog.dismiss();
										Toast.makeText(MainActivity.this,
												"数据更新失败", Toast.LENGTH_SHORT)
												.show();
									}
								}));
	}

	private void printData(List<RemnantGoodsEntity> list) {
		System.out.println(".................");
		for (int i = 0; i < list.size(); i++) {
			System.out.println("barcode:" + list.get(i).getAppNo() + " state:"
					+ list.get(i).getState() + "username:"
					+ list.get(i).getUsername());
		}

	}

}
