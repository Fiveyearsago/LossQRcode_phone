package com.example.lossqrcode;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.example.lossqrcode.adapter.AlreadyTakenGoodsAdapter;
import com.example.lossqrcode.entity.RemnantGoodsEntity;
import com.example.lossqrcode.entity.SearchCondition;
import com.example.lossqrcode.service.RemnantGoodsProvider;
import com.example.lossqrcode.ui.widget.DatePickerDialog;
import com.example.lossqrcode.ui.widget.DatePickerDialog.OnDateTimeSetListener;
import com.example.lossqrcode.ui.widget.LoadMoreListView;
import com.example.lossqrcode.ui.widget.LoadMoreListView.OnLoadMoreListener;
import com.example.lossqrcode.utils.DateUtils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AlreadyReceiveActivity extends Activity implements
		OnClickListener, OnLoadMoreListener {
	private LoadMoreListView partListView;
	private TextView tvAll;
	private View emptyView;
	private ProgressBar progressBar;
	private TextView tvEmpty;


	private static final int PAGE_SIZE = 10;
	private int pageIndex = 0;

	private TextView tvScreen;
	private PopupWindow searchWindow;

	private AlreadyTakenGoodsAdapter adapter;

	private List<RemnantGoodsEntity> partList;


	private TextView tvStartDate;
	private TextView tvEndDate;

	private static final int START_DATE_FLAG = 1;
	private static final int END_DATE_FLAG = 2;

	private Date startDate;
	private Date endDate;

	private Context context;
	private long totalCount;
	private boolean isRefresh = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_already_received);

		this.context = this;

		tvAll=(TextView)findViewById(R.id.tv_all);
		partListView = (LoadMoreListView) findViewById(R.id.already_received_listview);
		adapter = new AlreadyTakenGoodsAdapter(this);
		partListView.setAdapter(adapter);

		// 设置emptyView
		emptyView = findViewById(R.id.empty_view);
		progressBar = (ProgressBar) emptyView.findViewById(R.id.progress);
		tvEmpty = (TextView) emptyView.findViewById(R.id.tv_empty);
		partListView.setEmptyView(emptyView);

		partListView.setOnLoadMoreListener(this);

		tvScreen = (TextView) findViewById(R.id.tv_screen);
		tvScreen.setOnClickListener(this);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		partListView.setHasNoMore(false);
		loadAllData();
	}

	private void loadAllData() {
		// 加载数据
		totalCount = RemnantGoodsProvider.getInstance(context)
				.getRemnantGoodsHaveTokenCount();
		tvAll.setText("已收货数量："+totalCount);
		isRefresh = true;
		pageIndex = 0;
		new LoadPartAsyncTask().execute();
	}

	private class LoadPartAsyncTask extends
			AsyncTask<Void, Void, List<RemnantGoodsEntity>> {

		@Override
		protected List<RemnantGoodsEntity> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			List<RemnantGoodsEntity> partList = RemnantGoodsProvider
					.getInstance(context).getRemnantGoodsHaveTaken(PAGE_SIZE,
							pageIndex);
			return partList;
		}

		@Override
		protected void onPostExecute(List<RemnantGoodsEntity> partList) {
			if (isRefresh) {// 刷新数据
				adapter.refresh(partList);
				if (partList == null || partList.size() == 0) {
					setEmptyViewNoData();
				}
			} else {// 加载更多
				adapter.loadMore(partList);
				partListView.onLoadMoreComplete();
			}
			if (adapter.getCount() == totalCount) {
				partListView.setHasNoMore(true);
			}

			// TODO Auto-generated method stub
			super.onPostExecute(partList);
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
		if (searchWindow == null) {
			View view = LayoutInflater.from(this).inflate(
					R.layout.dialog_have_taken_screen_, null);
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
					// TODO Auto-generated method stub
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
						// 重新获取全部数据
						loadAllData();
						break;

					case R.id.btn_search:
						searchWindow.dismiss();
						SearchCondition condition = new SearchCondition();
						condition.reportNo = editReportNo.getText().toString()
								.trim();
						condition.vehicleModel = editVehicleModel.getText()
								.toString().trim();
						condition.partName = editPartName.getText().toString()
								.trim();
						condition.startDate = startDate;
						condition.endDate = endDate;

						partList = RemnantGoodsProvider.getInstance(
								AlreadyReceiveActivity.this)
								.getRemnantGoodsHaveTaken(condition);
						adapter.refresh(partList);

						if (partList == null || partList.size() == 0) {
							setEmptyViewNoData();
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
			searchWindow.showAsDropDown(tvScreen);
		} else {
			searchWindow.showAsDropDown(tvScreen);
		}
	}

	private void setDefaultDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);

		startDate = calendar.getTime();

		calendar.add(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.SECOND, -1);

		endDate = calendar.getTime();

		System.out.println(startDate);
		System.out.println(endDate);

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
		if(adapter.getCount()!=totalCount){
			isRefresh=false;
			pageIndex++;
			new LoadPartAsyncTask().execute();
		}

	}
}
