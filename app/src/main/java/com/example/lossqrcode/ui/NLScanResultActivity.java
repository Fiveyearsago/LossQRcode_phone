package com.example.lossqrcode.ui;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.Volley;
import com.example.lossqrcode.MyApplication;
import com.example.lossqrcode.R;
import com.example.lossqrcode.adapter.NLScanResultAdapter;
import com.example.lossqrcode.adapter.NLScanResultAdapter.LCallBack;
import com.example.lossqrcode.entity.DataDownloadEntity;
import com.example.lossqrcode.service.ApiService;
import com.example.lossqrcode.service.RemnantGoodsProvider;
import com.example.lossqrcode.ui.widget.LoadingDialog;
import com.example.lossqrcode.ui.widget.MyDialog;
import com.jy.third.pjhs.dto.NLBaseJson;
import com.jy.third.pjhs.dto.NLBaseResponse;
import com.jy.third.pjhs.dto.UploadScanDataReq;
import com.zbar.lib.CaptureActivity;

public class NLScanResultActivity extends BaseActivity implements
		OnClickListener, OnScrollListener {
	private final static int SCANNIN_GREQUEST_CODE = 1;
	private static final String TAG = "NLScanResultActivity";

	private ListView partListView;
	private Button btnUploadData;
	private TextView tvAll;
	private LoadingDialog dialog;

	private View emptyView;
	private ProgressBar progressBar;
	private TextView tvEmpty;

	private View footerView;
	private ProgressBar footerLoadProgress;
	private TextView footerLoadText;

	private int visibleLastIndex = 0; // 最后的可视项索引
	private int visibleItemCount; // 当前窗口可见项总数

	private static final int PAGE_SIZE = 10;

	private int pageIndex = 0;

	private RequestQueue requestQueue;

	private NLScanResultAdapter adapter;
	private List<DataDownloadEntity> partList = new ArrayList<DataDownloadEntity>();

	private IntentFilter filter = new IntentFilter("ACTION_BAR_SCAN");
	private TextView tvSelect;// 选择按钮

	// private TextView tvBack;//选择按钮

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nlscanresult);

		setBarLeft(R.drawable.selector_left_arrow, "已扫描");
		setBarLeftOnclickListener(this);
		setBarRightString("手动输入");
		setBarRightLeftString("扫描");
		setBarRightOnclickListener(this);
		partListView = (ListView) findViewById(R.id.scan_result_listview);
		partList = RemnantGoodsProvider.getInstance(this)
				.getAllHaveScannedPart2();
		adapter = new NLScanResultAdapter(this,new LCallBack() {
			
			@Override
			public void refreshCount() {
				// TODO 刷新零件数量
				partList = RemnantGoodsProvider.getInstance(
						NLScanResultActivity.this).getAllHaveScannedPart2();
				Log.i("partList.size()", partList.size() + "");
				if (partList != null && partList.size() > 0) {
					tvAll.setText("已扫描零件总数：" + partList.size());
				} else {
					tvAll.setText("已扫描零件总数：" + "0");
				}
			}
		},partList);

		// adapter.notifyDataSetChanged();
		// adapter.refresh(partList);
		partListView.setAdapter(adapter);
		partListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// TODO Auto-generated method stub
				// partList = RemnantGoodsProvider.getInstance(
				// NLScanResultActivity.this)
				// .getAllHaveScannedPart2();
				// Log.i("partList.size()", partList.size() + "");
				// if (partList != null && partList.size() > 0) {
				// tvAll.setText("已扫描零件总数：" + partList.size());
				// } else {
				// tvAll.setText("已扫描零件总数：" + "0");
				// }
			}
		});
		// 设置emptyView
		tvAll = (TextView) findViewById(R.id.tv_all);
		emptyView = findViewById(R.id.empty_view);
		progressBar = (ProgressBar) emptyView.findViewById(R.id.progress);
		tvEmpty = (TextView) emptyView.findViewById(R.id.tv_empty);
		partListView.setEmptyView(emptyView);
		// View
		// view=getLayoutInflater().inflate(R.layout.nlscan_result_list_item,
		// null);

		// 设置底部加载footerview
		footerView = getLayoutInflater().inflate(R.layout.load_more_footer,
				null);
		footerLoadProgress = (ProgressBar) footerView
				.findViewById(R.id.load_more_footer_progress);
		footerLoadText = (TextView) footerView
				.findViewById(R.id.load_more_footer_text);
		// partListView.addFooterView(footerView);
		// partListView.setOnScrollListener(this);

		btnUploadData = (Button) findViewById(R.id.btn_upload_data);
		btnUploadData.setOnClickListener(this);
		setBarRightLeftOnclickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(NLScanResultActivity.this,
						CaptureActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
			}
		});
		//
		// bt_saomiao.setOnHomeClick(new HomeClickListener() {
		//
		// @Override
		// public void onclick() {
		// Intent intent = new Intent();
		// intent.setClass(NLMainActivity.this, CaptureActivity.class);
		// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
		// }
		// });

		// 获取上次已扫描的零件
		partList = RemnantGoodsProvider.getInstance(this)
				.getAllHaveScannedPart2();
		if (partList != null && partList.size() > 0) {
			tvAll.setText("已扫描零件总数：" + partList.size());
		} else {
			tvAll.setText("已扫描零件总数：" + "0");
		}
		// 刷新列表
//		adapter.loadMore(partList);
		// 设置emptyView
		if (partList == null || partList.size() == 0) {
			setEmptyViewNoData();
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		// 注册广播来获取扫描结果
		this.registerReceiver(receiver, filter);
		// 获取上次已扫描的零件
		partList = RemnantGoodsProvider.getInstance(this)
				.getAllHaveScannedPart2();
		if (partList != null && partList.size() > 0) {
			tvAll.setText("已扫描零件总数：" + partList.size());
		} else {
			tvAll.setText("已扫描零件总数：" + "0");
		}
	}

	@Override
	protected void onPause() {
		// 注销获取扫描结果的广播
		this.unregisterReceiver(receiver);
		super.onPause();

	}

	@Override
	protected void onDestroy() {
		receiver = null;
		filter = null;
		super.onDestroy();

	}

	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// 此处获取扫描结果信息
			String barcode = intent.getStringExtra("EXTRA_SCAN_DATA");
			if (TextUtils.isEmpty(barcode)) {
				Toast.makeText(NLScanResultActivity.this, "请重新扫描",
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
	};

	private void showScanResult(String barcode) {
		DataDownloadEntity rgEntity = new DataDownloadEntity();
		if (barcode.contains(",")) {
			rgEntity.setBarCode(barcode);
		} else {
			rgEntity.setBarysth(barcode);
		}
		// 和数据库数据对比，是否存在本地零件中
		List<DataDownloadEntity> rgList = RemnantGoodsProvider
				.getInstance(this).isExistedInDB(rgEntity);
		Log.i(TAG, "list size = " + rgList.size());
		Log.i(TAG, "barcode = " + barcode);
		if (rgList != null && rgList.size() > 0) {
			DataDownloadEntity newRgEntity = rgList.get(0);
			if (newRgEntity.getState() != null
					&& newRgEntity.getState().equals("02")) {
				Toast.makeText(this, "此零件已被扫描", Toast.LENGTH_SHORT).show();
			} else if ((newRgEntity.getState() == null)
					|| (newRgEntity.getState() != null && newRgEntity
							.getState().equals("01"))) {// 将此零件标记为已扫描零件
				newRgEntity.setScanDate(new Date());
				newRgEntity.setState("02");
				RemnantGoodsProvider.getInstance(this).updateHaveScannedPart2(
						newRgEntity);
				// 同时更新缓存列表
				partList.add(0, newRgEntity);
				// 刷新列表
				adapter.loadMore(newRgEntity);
			}

		} else {
			Toast.makeText(this, "找不到此零件", Toast.LENGTH_SHORT).show();
		}
	}

	private void setEmptyViewNoData() {
		progressBar.setVisibility(View.GONE);
		tvEmpty.setText("暂无数据");
	}

	public void back(View view) {
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_bar_left:
			finish();
			break;
		// case R.id.tv_select:
		// finish();
		// break;
		case R.id.btn_bar_right:
			showInputCodeDialog();
			break;

		case R.id.btn_upload_data:
			if (partList == null || partList.size() == 0) {
				Toast.makeText(this, "没有可上传的零件数据", Toast.LENGTH_SHORT).show();
				return;
			}
			uploadScanData(partList);
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
		tvCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				inputDialog.dismiss();
			}
		});
		tvSure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String barCodeStr = etInputCode.getText().toString().trim();
				if (TextUtils.isEmpty(barCodeStr)) {
					Toast.makeText(NLScanResultActivity.this,
							R.string.prompt_manual_input_barcode,
							Toast.LENGTH_SHORT).show();
					return;
				}
				showScanResult(barCodeStr);
				partList = RemnantGoodsProvider.getInstance(
						NLScanResultActivity.this).getAllHaveScannedPart2();
				if (partList != null && partList.size() > 0) {
					tvAll.setText("已扫描零件总数：" + partList.size());
				} else {
					tvAll.setText("已扫描零件总数：" + "0");
				}
				inputDialog.dismiss();
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

	private void uploadScanData(List<DataDownloadEntity> list) {
		List<String> scanIdList = new ArrayList<String>();
		List<String> scanGoodNoList = new ArrayList<String>();
		UploadScanDataReq updomain = new UploadScanDataReq();
		String username = ((MyApplication) getApplicationContext())
				.getUsername();
		updomain.setUsername(username);
		for (DataDownloadEntity entity : list) {
			scanIdList.add(entity.getId());
		}
		for (DataDownloadEntity entity : list) {
			scanGoodNoList.add(entity.getBarghdh());
		}
		updomain.setIds(scanIdList);
		updomain.setGoodNos(scanGoodNoList);

		dialog = new LoadingDialog(this, "正在上传...");
		dialog.show();

		requestQueue = Volley.newRequestQueue(this);
		requestQueue.add(ApiService.nlUploadScanData(updomain,
				new Listener<NLBaseJson<NLBaseResponse>>() {

					@Override
					public void onResponse(NLBaseJson<NLBaseResponse> response) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						if (response != null) {
							String code = response.getData().getCode();
							if ("0310".equals(code)) {

								RemnantGoodsProvider.getInstance(
										NLScanResultActivity.this)
										.clearHaveSubmitData2(partList);
								partList.clear();
								adapter.reset();
								setEmptyViewNoData();

								Toast.makeText(NLScanResultActivity.this,
										"数据上传成功", Toast.LENGTH_SHORT).show();

							} else if ("0199".equals(code)) {
								Toast.makeText(NLScanResultActivity.this,
										"网络或XML格式异常", Toast.LENGTH_SHORT)
										.show();
							}
						} else {
							Toast.makeText(NLScanResultActivity.this,
									"数据上传无返回", Toast.LENGTH_SHORT).show();
						}

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						dialog.dismiss();
						Toast.makeText(NLScanResultActivity.this, "数据上传失败",
								Toast.LENGTH_SHORT).show();
					}
				}));
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		int itemsLastIndex = adapter.getCount() - 1; // 数据集最后一项的索引
		int lastIndex = itemsLastIndex + 1;
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
				&& visibleLastIndex == lastIndex) {
			setFooterLoading();
			pageIndex++;
			// partList = RemnantGoodsProvider.getInstance(this)
			// .getRemnantGoodsToTake(PAGE_SIZE, pageIndex);
			adapter.loadMore(partList);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		this.visibleItemCount = visibleItemCount;
		visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
		long count = RemnantGoodsProvider.getInstance(this)
				.getRemnantGoodsHaveScannedCount();
		System.out.println("count:"
				+ RemnantGoodsProvider.getInstance(this)
						.getRemnantGoodsHaveScannedCount());
		System.out.println("totalitem:" + totalItemCount);
		if (partList.size() + 1 == visibleItemCount) {
			partListView.removeFooterView(footerView);
		}
		// 如果所有的记录选项等于数据集的条数，则移除列表底部视图
		if (totalItemCount == RemnantGoodsProvider.getInstance(this)
				.getRemnantGoodsHaveScannedCount() + 1) {
			setFooterHasNoMore();
		}
	}

	/**
	 * 加载中
	 */
	private void setFooterLoading() {
		footerLoadProgress.setVisibility(View.VISIBLE);
		footerLoadText.setText("加载中...");
	}

	/**
	 * 加载全部
	 */
	private void setFooterHasNoMore() {
		footerLoadProgress.setVisibility(View.GONE);
		footerLoadText.setText("已加载全部");
	}

	/**
	 * 处理结果
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case SCANNIN_GREQUEST_CODE:
			if (resultCode == RESULT_OK) {
				Bundle bundle = data.getExtras();
				// 此处获取扫描结果信息
				String barcode = bundle.getString("EXTRA_SCAN_DATA");
				if (TextUtils.isEmpty(barcode)) {
					Toast.makeText(NLScanResultActivity.this, "请重新扫描",
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
}
