package com.example.lossqrcode;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
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
import com.example.lossqrcode.adapter.ScanResultAdapter;
import com.example.lossqrcode.entity.RemnantGoodsEntity;
import com.example.lossqrcode.service.ApiService;
import com.example.lossqrcode.service.RemnantGoodsProvider;
import com.example.lossqrcode.ui.widget.LoadingDialog;
import com.example.lossqrcode.ui.widget.MyDialog;
import com.jy.third.pjhs.dto.Response;

public class ScanResultActivity extends Activity implements OnClickListener,
		OnScrollListener {
	private ListView partListView;
	private TextView btnUploadData;
	private TextView btnOpenScan;
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

	private ScanResultAdapter adapter;
	private List<RemnantGoodsEntity> partList = new ArrayList<RemnantGoodsEntity>();

	private IntentFilter filter = new IntentFilter("ACTION_BAR_SCAN");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan_result);
		partListView = (ListView) findViewById(R.id.scan_result_listview);
		adapter = new ScanResultAdapter(this);
		partListView.setAdapter(adapter);

		// 设置emptyView
		tvAll = (TextView) findViewById(R.id.tv_all);
		emptyView = findViewById(R.id.empty_view);
		progressBar = (ProgressBar) emptyView.findViewById(R.id.progress);
		tvEmpty = (TextView) emptyView.findViewById(R.id.tv_empty);
		partListView.setEmptyView(emptyView);

		// 设置底部加载footerview
		footerView = getLayoutInflater().inflate(R.layout.load_more_footer,
				null);
		footerLoadProgress = (ProgressBar) footerView
				.findViewById(R.id.load_more_footer_progress);
		footerLoadText = (TextView) footerView
				.findViewById(R.id.load_more_footer_text);
		// partListView.addFooterView(footerView);
		// partListView.setOnScrollListener(this);

		btnUploadData = (TextView) findViewById(R.id.btn_upload_data);
		btnOpenScan = (TextView) findViewById(R.id.tv_manual_input);
		btnUploadData.setOnClickListener(this);
		btnOpenScan.setOnClickListener(this);

		/*
		 * RemnantGoodsEntity rgEntity=new RemnantGoodsEntity();
		 * rgEntity.setId("4028ee334a0eec53014a0eee1a090004");
		 * RemnantGoodsProvider.getInstance(this).updatePart(rgEntity);
		 */
		// 获取上次已扫描的零件
		partList = RemnantGoodsProvider.getInstance(this)
				.getAllHaveScannedPart();
		if(partList!=null&&partList.size()>0){
			tvAll.setText("已扫描零件总数：" + partList.size());
		}else{
			tvAll.setText("已扫描零件总数：" + "0");
		}
		// 刷新列表
		adapter.loadMore(partList);
		// 设置emptyView
		if (partList == null || partList.size() == 0) {
			setEmptyViewNoData();
		}
		
		if(partList!=null){
			for(int i=0;i<partList.size();i++){
				System.out.println("state:"+partList.get(i).getState());
			}
		}
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// 注册广播来获取扫描结果
		this.registerReceiver(receiver, filter);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		// 注销获取扫描结果的广播
		this.unregisterReceiver(receiver);
		super.onPause();

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		receiver = null;
		filter = null;
		super.onDestroy();

	}

	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			// 此处获取扫描结果信息
			final String appNo = intent.getStringExtra("EXTRA_SCAN_DATA");
			System.out.println("ddddd:"+appNo);
			if(TextUtils.isEmpty(appNo)){
				Toast.makeText(ScanResultActivity.this, "请重新扫描", Toast.LENGTH_SHORT).show();
				return;
			}
			showScanResult(appNo);
		}
	};
	
	private void showScanResult(String appNo){
		RemnantGoodsEntity rgEntity = new RemnantGoodsEntity();
		rgEntity.setAppNo(appNo);
		// 和数据库数据对比，是否存在本地零件中
		List<RemnantGoodsEntity> rgList = RemnantGoodsProvider
				.getInstance(this).isExistedInDB(rgEntity);
		if (rgList != null && rgList.size() > 0) {
			RemnantGoodsEntity newRgEntity = rgList.get(0);
//			String id=newRgEntity.getId();
//			System.out.println("扫描id:"+id);
			if (newRgEntity.getState() != null
					&& newRgEntity.getState().equals("02")) {
				Toast.makeText(this, "此零件已被扫描", Toast.LENGTH_SHORT)
						.show();
			} else if((newRgEntity.getState() == null)||(newRgEntity.getState() != null
					&& newRgEntity.getState().equals("01"))){// 将此零件标记为已扫描零件
				newRgEntity.setAppNo(appNo);
				newRgEntity.setScanDate(new Date());
				newRgEntity.setState("02");
				RemnantGoodsProvider.getInstance(this)
						.updateHaveScannedPart(newRgEntity);
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
		// TODO Auto-generated method stub
		if (v.getId() == R.id.tv_manual_input) {// 手动输入
			showInputCodeDialog();
			
		} else {// 上传扫描结果
			if(partList.size()==0){
				Toast.makeText(this, "没有可上传的零件数据", Toast.LENGTH_SHORT).show();
				return;
			}
			uploadScanData();
		}

	}
	
	private void showInputCodeDialog(){
		final MyDialog inputDialog=new MyDialog(this, R.layout.dialog_input_barcode);
		final EditText etInputCode=(EditText)inputDialog.findViewById(R.id.et_barcode);
		TextView tvCancel=(TextView)inputDialog.findViewById(R.id.tv_cancel);
		TextView tvSure=(TextView)inputDialog.findViewById(R.id.tv_sure);
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
				String barCodeStr=etInputCode.getText().toString().trim();
				if(TextUtils.isEmpty(barCodeStr)){
					Toast.makeText(ScanResultActivity.this, R.string.prompt_manual_input_barcode, Toast.LENGTH_SHORT).show();
					return;
				}
				showScanResult(barCodeStr);
				inputDialog.dismiss();
			}
		});
		inputDialog.show();
		showInputMethod(etInputCode);
	}
	
	private void showInputMethod(final View v){
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}, 200);
		
	}

	private void uploadScanData() {
		RemnantGoodsEntity dto = new RemnantGoodsEntity();
		dto.setSearchResultList(partList);
		String userId = ((MyApplication) getApplicationContext()).getUserId();

		dialog = new LoadingDialog(this, "正在上传...");
		dialog.show();

		requestQueue = Volley.newRequestQueue(this);
		requestQueue.add(ApiService.uploadScanData(userId, dto,
				new Listener<Response<RemnantGoodsEntity>>() {

					@Override
					public void onResponse(Response<RemnantGoodsEntity> response) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						if ("0".equals(response.getResponseType())) {
							// 上传成功，将数据库对应零件标记为已提货状态 state = "03"
//							RemnantGoodsProvider.getInstance(
//									ScanResultActivity.this)
//									.updateToAlreadyToken(partList);
							//删除已上传零件列表
							RemnantGoodsProvider.getInstance(ScanResultActivity.this).clearHaveSubmitData(partList);
							// 同时删除数据缓存
							partList.clear();
							// 刷新已扫描零件列表
							adapter.reset();
							setEmptyViewNoData();

							Toast.makeText(ScanResultActivity.this, "数据上传成功",
									Toast.LENGTH_SHORT).show();
						} else {

							Toast.makeText(ScanResultActivity.this,
									response.getErrMsg(), Toast.LENGTH_SHORT)
									.show();
						}

					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						Toast.makeText(ScanResultActivity.this, "数据上传失败",
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
			partList = RemnantGoodsProvider.getInstance(this)
					.getRemnantGoodsToTake(PAGE_SIZE, pageIndex);
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
}
