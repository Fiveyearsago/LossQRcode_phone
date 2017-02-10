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

	private int visibleLastIndex = 0; // ���Ŀ���������
	private int visibleItemCount; // ��ǰ���ڿɼ�������

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

		// ����emptyView
		tvAll = (TextView) findViewById(R.id.tv_all);
		emptyView = findViewById(R.id.empty_view);
		progressBar = (ProgressBar) emptyView.findViewById(R.id.progress);
		tvEmpty = (TextView) emptyView.findViewById(R.id.tv_empty);
		partListView.setEmptyView(emptyView);

		// ���õײ�����footerview
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
		// ��ȡ�ϴ���ɨ������
		partList = RemnantGoodsProvider.getInstance(this)
				.getAllHaveScannedPart();
		if(partList!=null&&partList.size()>0){
			tvAll.setText("��ɨ�����������" + partList.size());
		}else{
			tvAll.setText("��ɨ�����������" + "0");
		}
		// ˢ���б�
		adapter.loadMore(partList);
		// ����emptyView
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
		// ע��㲥����ȡɨ����
		this.registerReceiver(receiver, filter);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		// ע����ȡɨ�����Ĺ㲥
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
			// �˴���ȡɨ������Ϣ
			final String appNo = intent.getStringExtra("EXTRA_SCAN_DATA");
			System.out.println("ddddd:"+appNo);
			if(TextUtils.isEmpty(appNo)){
				Toast.makeText(ScanResultActivity.this, "������ɨ��", Toast.LENGTH_SHORT).show();
				return;
			}
			showScanResult(appNo);
		}
	};
	
	private void showScanResult(String appNo){
		RemnantGoodsEntity rgEntity = new RemnantGoodsEntity();
		rgEntity.setAppNo(appNo);
		// �����ݿ����ݶԱȣ��Ƿ���ڱ��������
		List<RemnantGoodsEntity> rgList = RemnantGoodsProvider
				.getInstance(this).isExistedInDB(rgEntity);
		if (rgList != null && rgList.size() > 0) {
			RemnantGoodsEntity newRgEntity = rgList.get(0);
//			String id=newRgEntity.getId();
//			System.out.println("ɨ��id:"+id);
			if (newRgEntity.getState() != null
					&& newRgEntity.getState().equals("02")) {
				Toast.makeText(this, "������ѱ�ɨ��", Toast.LENGTH_SHORT)
						.show();
			} else if((newRgEntity.getState() == null)||(newRgEntity.getState() != null
					&& newRgEntity.getState().equals("01"))){// ����������Ϊ��ɨ�����
				newRgEntity.setAppNo(appNo);
				newRgEntity.setScanDate(new Date());
				newRgEntity.setState("02");
				RemnantGoodsProvider.getInstance(this)
						.updateHaveScannedPart(newRgEntity);
				// ͬʱ���»����б�
				partList.add(0, newRgEntity);
				// ˢ���б�
				adapter.loadMore(newRgEntity);
			}

		} else {
			Toast.makeText(this, "�Ҳ��������", Toast.LENGTH_SHORT).show();
		}
	}

	private void setEmptyViewNoData() {
		progressBar.setVisibility(View.GONE);
		tvEmpty.setText("��������");
	}

	public void back(View view) {
		finish();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.tv_manual_input) {// �ֶ�����
			showInputCodeDialog();
			
		} else {// �ϴ�ɨ����
			if(partList.size()==0){
				Toast.makeText(this, "û�п��ϴ����������", Toast.LENGTH_SHORT).show();
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

		dialog = new LoadingDialog(this, "�����ϴ�...");
		dialog.show();

		requestQueue = Volley.newRequestQueue(this);
		requestQueue.add(ApiService.uploadScanData(userId, dto,
				new Listener<Response<RemnantGoodsEntity>>() {

					@Override
					public void onResponse(Response<RemnantGoodsEntity> response) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						if ("0".equals(response.getResponseType())) {
							// �ϴ��ɹ��������ݿ��Ӧ������Ϊ�����״̬ state = "03"
//							RemnantGoodsProvider.getInstance(
//									ScanResultActivity.this)
//									.updateToAlreadyToken(partList);
							//ɾ�����ϴ�����б�
							RemnantGoodsProvider.getInstance(ScanResultActivity.this).clearHaveSubmitData(partList);
							// ͬʱɾ�����ݻ���
							partList.clear();
							// ˢ����ɨ������б�
							adapter.reset();
							setEmptyViewNoData();

							Toast.makeText(ScanResultActivity.this, "�����ϴ��ɹ�",
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
						Toast.makeText(ScanResultActivity.this, "�����ϴ�ʧ��",
								Toast.LENGTH_SHORT).show();
					}
				}));
	}


	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		int itemsLastIndex = adapter.getCount() - 1; // ���ݼ����һ�������
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
		// ������еļ�¼ѡ��������ݼ������������Ƴ��б�ײ���ͼ
		if (totalItemCount == RemnantGoodsProvider.getInstance(this)
				.getRemnantGoodsHaveScannedCount() + 1) {
			setFooterHasNoMore();
		}
	}

	/**
	 * ������
	 */
	private void setFooterLoading() {
		footerLoadProgress.setVisibility(View.VISIBLE);
		footerLoadText.setText("������...");
	}

	/**
	 * ����ȫ��
	 */
	private void setFooterHasNoMore() {
		footerLoadProgress.setVisibility(View.GONE);
		footerLoadText.setText("�Ѽ���ȫ��");
	}
}
