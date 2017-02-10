package com.example.lossqrcode.ui;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;






import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lossqrcode.R;
import com.example.lossqrcode.adapter.NLWaitGroupAdapter;
import com.example.lossqrcode.adapter.WaitGroupAdapter;
import com.example.lossqrcode.entity.DataDownloadEntity;
import com.example.lossqrcode.entity.RemnantGoodsEntity;
import com.example.lossqrcode.entity.SearchCondition;
import com.example.lossqrcode.service.RemnantGoodsProvider;
import com.example.lossqrcode.ui.widget.DatePickerDialog;
import com.example.lossqrcode.ui.widget.DatePickerDialog.OnDateTimeSetListener;
import com.example.lossqrcode.ui.widget.LoadMoreListView.OnLoadMoreListener;
import com.example.lossqrcode.ui.widget.PinnedHeaderListView;
import com.example.lossqrcode.ui.widget.PinnedHeaderListView.OnItemClickListener;
import com.example.lossqrcode.utils.Constants;
import com.example.lossqrcode.utils.DateUtils;

public class NLWaitReceiveActivity extends BaseActivity implements OnClickListener,
		OnLoadMoreListener {
	
	private LinearLayout ll_main;
	private PinnedHeaderListView partListView;
	private TextView tvAll;
	private TextView tvDelay;
	private View bottomView;
	private Button btnMiss;

	private View emptyView;
	private ProgressBar progressBar;
	private TextView tvEmpty;

	private PopupWindow searchWindow;
	private TextView tvStartDate;
	private TextView tvEndDate;

	private boolean isEdit = false;
	private static final int PAGE_SIZE = 10;
	private int pageIndex = 0;
	private long totalCount;
	private int selectedNum = 0;

	private NLWaitGroupAdapter adapter;
	LinkedHashMap<String, List<DataDownloadEntity>> map = new LinkedHashMap<String, List<DataDownloadEntity>>();
	private List<DataDownloadEntity> partList = new ArrayList<DataDownloadEntity>();

	private static final int START_DATE_FLAG = 1;
	private static final int END_DATE_FLAG = 2;

	private Date startDate;
	private Date endDate;
	private Date beforeStartDate;

	private Context context;
	private boolean isRefresh = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nlfragment_wait_receive);

		this.context = this;

		ll_main = (LinearLayout) findViewById(R.id.main);
		tvAll = (TextView) findViewById(R.id.tv_all);
		tvDelay = (TextView) findViewById(R.id.tv_delay);
		bottomView = findViewById(R.id.bottomview);
		btnMiss = (Button) findViewById(R.id.btn_miss);
		btnMiss.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(selectedNum==0){
					Toast.makeText(context, "请选择零件", Toast.LENGTH_SHORT).show();
					return;
				}
				for (String key : adapter.isSelected.keySet()) {
					if(adapter.isSelected.get(key)!=null){
						adapter.isSelected.get(key).setState(Constants.STATE_ALREADY_MISSED);
						RemnantGoodsProvider.getInstance(context).updateHaveMissedPart2(adapter.isSelected.get(key));
					}
				}
				Toast.makeText(context, "设置成功", Toast.LENGTH_SHORT).show();
				selectedNum=0;
				resetEditView();
				isEdit=false;
				adapter.isSelected.clear();
				loadAllData();
			}
		});
		partListView = (PinnedHeaderListView) findViewById(R.id.wait_receive_listview);
		partListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onSectionClick(AdapterView<?> adapterView, View view,
					int section, long id) {

			}

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int section, int position, long id) {
				if (isEdit == false) {
					return;
				}
				String key = section + "" + position;
				DataDownloadEntity rg = adapter.isSelected.get(key);
				if (rg == null) {
					DataDownloadEntity rgEntity = (DataDownloadEntity) adapter.map
							.get(adapter.goodNoList.get(section)).get(position);
					adapter.isSelected.put(key, rgEntity);
					selectedNum++;
				} else {
					adapter.isSelected.put(key, null);
					selectedNum--;
				}
				btnMiss.setText("(" + selectedNum + ")个零件未找到");
				adapter.notifyDataSetChanged();
			}
		});
		adapter = new NLWaitGroupAdapter(map,this);
		partListView.setAdapter(adapter);

		// 设置emptyView
		emptyView = findViewById(R.id.empty_view);
		progressBar = (ProgressBar) emptyView.findViewById(R.id.progress);
		tvEmpty = (TextView) emptyView.findViewById(R.id.tv_empty);
//		partListView.setEmptyView(emptyView);


		setBarLeft(R.drawable.selector_left_arrow, "待收货");
		setBarLeftOnclickListener(this);
		setBarRightString("筛选");
		setBarRightLeftString("选择");
		setBarRightOnclickListener(this);
		setBarRightLeftOnclickListener(this);
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		loadAllData();
	}

	private void loadAllData() {
		// 加载数据
		totalCount = RemnantGoodsProvider.getInstance(context)
				.getUntakeCount();
		long delayCount = RemnantGoodsProvider.getInstance(context)
				.getDelayCount2();
		tvDelay.setText("延迟收货数量：" + delayCount);
		tvAll.setText("待收货数量：" + totalCount);
		isRefresh = true;
		pageIndex = 0;
		new LoadPartAsyncTask().execute();
	}

	private void setEmptyViewNoData() {
		ll_main.setVisibility(View.GONE);
		emptyView.setVisibility(View.VISIBLE);
		progressBar.setVisibility(View.GONE);
		tvEmpty.setText("暂无数据");
	}

	private class LoadPartAsyncTask
			extends
			AsyncTask<Void, Void, LinkedHashMap<String, List<DataDownloadEntity>>> {
		

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			ll_main.setVisibility(View.GONE);
			emptyView.setVisibility(View.VISIBLE);
		}

		@Override
		protected LinkedHashMap<String, List<DataDownloadEntity>> doInBackground(
				Void... params) {
			map = RemnantGoodsProvider.getInstance(context)
					.getUntake();
			return map;
		}

		@Override
		protected void onPostExecute(
				LinkedHashMap<String, List<DataDownloadEntity>> map) {
			ll_main.setVisibility(View.VISIBLE);
			emptyView.setVisibility(View.GONE);
			adapter.update(map, false);

			if (map.size() == 0) {
				getBtnRightLeft().setVisibility(View.GONE);
			}
			super.onPostExecute(map);
		}

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
			
		case R.id.btn_bar_rightleft:
			if (isEdit == false) {
				setBarRightLeftString("取消");
				bottomView.setVisibility(View.VISIBLE);
				isEdit = true;
				adapter.update(map, isEdit);
			} else {
				isEdit = false;
				selectedNum = 0;
				resetEditView();
				adapter.isSelected.clear();
				adapter.update(map, isEdit);
			}
			break;
			
		case R.id.btn_bar_right:
			if (searchWindow == null) {
				View view = LayoutInflater.from(this).inflate(
						R.layout.dialog_part_info_screen, null);
				final EditText editReportNo = (EditText) view
						.findViewById(R.id.et_report_no);
				final EditText editVehicleModel = (EditText) view
						.findViewById(R.id.et_vehicle_model);
				final EditText editPartName = (EditText) view
						.findViewById(R.id.et_part_name);
				tvStartDate = (TextView) view.findViewById(R.id.tv_start_date);
				tvEndDate = (TextView) view.findViewById(R.id.tv_end_date);

				setDefaultDate();

				Button btnReset = (Button) view.findViewById(R.id.btn_reset);
				Button btnSearch = (Button) view.findViewById(R.id.btn_search);

				OnClickListener listener = new OnClickListener() {

					@Override
					public void onClick(View v) {
						switch (v.getId()) {
						case R.id.tv_start_date:
							showDatePickerDialog(START_DATE_FLAG);
							break;

						case R.id.tv_end_date:
							showDatePickerDialog(END_DATE_FLAG);
							break;

						case R.id.btn_reset:
							editReportNo.setText("");
							editVehicleModel.setText("");
							editPartName.setText("");
							setDefaultDate();

							loadAllData();
							break;

						case R.id.btn_search:
							searchWindow.dismiss();
							SearchCondition condition = new SearchCondition();
							condition.reportNo = editReportNo.getText()
									.toString().trim();
							condition.vehicleModel = editVehicleModel.getText()
									.toString().trim();
							condition.partName = editPartName.getText()
									.toString().trim();
							condition.startDate = beforeStartDate;
							condition.endDate = endDate;
							if (condition.reportNo!=null && !"".equals(condition.reportNo)||condition.partName!=null && !"".equals(condition.partName)) {
								map=RemnantGoodsProvider.getInstance(
										NLWaitReceiveActivity.this)
										.getUntake(condition);
								adapter.update(map, false);
								
								if (map == null || map.size() == 0) {
									setEmptyViewNoData();
								}
							}else{
								Toast.makeText(getApplicationContext(), "请输入报案号或车牌号", Toast.LENGTH_SHORT).show();
							}
							break;

						default:
							break;
						}
					}
				};
				tvStartDate.setOnClickListener(listener);
				tvEndDate.setOnClickListener(listener);
				btnReset.setOnClickListener(listener);
				btnSearch.setOnClickListener(listener);

				searchWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT);
				searchWindow.setAnimationStyle(R.style.popop_menu_anim_style);
				searchWindow.setBackgroundDrawable(new BitmapDrawable());
				searchWindow.setOutsideTouchable(true);
				searchWindow.setFocusable(true);
				searchWindow.update();
				searchWindow.showAsDropDown(getBtnRight());
			} else {
				searchWindow.showAsDropDown(getBtnRight());
			}
			break;
			
		default:
			break;
		}

	}
	
	private void resetEditView(){
		btnMiss.setText("(" + selectedNum + ")个零件未找到");
		bottomView.setVisibility(View.GONE);
		setBarRightLeftString("选择");
	}
	
	/**
	 * 前一天
	 */
	private void beforeOneDay(Calendar calendar){
		Calendar cal=(Calendar)calendar.clone();
		cal.add(Calendar.DAY_OF_MONTH, -1);
		beforeStartDate=cal.getTime();
	}

	private void setDefaultDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		beforeOneDay(calendar);
		startDate = calendar.getTime();

		calendar.add(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.SECOND, -1);

		endDate = calendar.getTime();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String startDateStr = sdf.format(startDate);
		String endDateStr = sdf.format(endDate);

		tvStartDate.setText(startDateStr);
		tvEndDate.setText(endDateStr);

	}

	private void showDatePickerDialog(final int dateFlag) {
		new DatePickerDialog(this, new OnDateTimeSetListener() {

			@Override
			public void onDateTimeSet(int year, int monthOfYear, int dayOfMonth) {
				// TODO Auto-generated method stub
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.YEAR, year);
				calendar.set(Calendar.MONTH, monthOfYear);
				calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

				if (dateFlag == START_DATE_FLAG) {// 开始时间
					calendar.set(Calendar.HOUR_OF_DAY, 0);
					calendar.set(Calendar.MINUTE, 0);
					calendar.set(Calendar.SECOND, 0);
					beforeOneDay(calendar);
					startDate = calendar.getTime();
					if (endDate != null) {
						if (!DateUtils.isDateBefore(startDate, endDate)) {
							calendar.add(Calendar.DAY_OF_MONTH, 1);
							calendar.add(Calendar.SECOND, -1);
							endDate = calendar.getTime();
						}
					}
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					tvStartDate.setText(sdf.format(startDate));
					tvEndDate.setText(endDate == null ? "" : sdf
							.format(endDate));
					System.out.println(startDate);
					System.out.println(endDate);
				} else {// 结束时间
					calendar.set(Calendar.HOUR_OF_DAY, 0);
					calendar.set(Calendar.MINUTE, 0);
					calendar.set(Calendar.SECOND, 0);
					calendar.add(Calendar.DAY_OF_MONTH, 1);
					calendar.add(Calendar.SECOND, -1);
					endDate = calendar.getTime();
					if (startDate != null) {
						if (!DateUtils.isDateBefore(startDate, endDate)) {
							calendar.set(Calendar.HOUR_OF_DAY, 0);
							calendar.set(Calendar.MINUTE, 0);
							calendar.set(Calendar.SECOND, 0);
							startDate = calendar.getTime();
						}
					}
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					tvStartDate.setText(startDate == null ? "" : sdf
							.format(startDate));
					tvEndDate.setText(sdf.format(endDate));
				}

			}
		}).show();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		if (adapter.getCount() != totalCount) {
			isRefresh = false;
			pageIndex++;
			new LoadPartAsyncTask().execute();
		}

	}
	
	
	
	
	
	
	
}
