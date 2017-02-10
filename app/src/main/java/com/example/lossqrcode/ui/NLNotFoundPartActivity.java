package com.example.lossqrcode.ui;

import java.util.LinkedHashMap;
import java.util.List;

import com.example.lossqrcode.R;
import com.example.lossqrcode.adapter.NLNotFoundAdapter;
import com.example.lossqrcode.adapter.NotFoundAdapter;
import com.example.lossqrcode.entity.DataDownloadEntity;
import com.example.lossqrcode.entity.DataDownloadEntity;
import com.example.lossqrcode.service.RemnantGoodsProvider;
import com.example.lossqrcode.ui.widget.PinnedHeaderListView;
import com.example.lossqrcode.ui.widget.PinnedHeaderListView.OnItemClickListener;
import com.example.lossqrcode.utils.Constants;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class NLNotFoundPartActivity extends BaseActivity implements OnClickListener{
	private PinnedHeaderListView partListView;
	private TextView tvAll;
	private View bottomView;
	private Button btnMiss;

	private View emptyView;
	private ProgressBar progressBar;
	private TextView tvEmpty;


	private boolean isEdit = false;
	private long totalCount;
	private int selectedNum = 0;

	private NLNotFoundAdapter adapter;
	LinkedHashMap<String, List<DataDownloadEntity>> map = new LinkedHashMap<String, List<DataDownloadEntity>>();



	private Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nlnotfoundpart);
		
		setBarLeft(R.drawable.selector_left_arrow, "未找到");
		setBarLeftOnclickListener(this);
		setBarRightString("选择");
		setBarRightOnclickListener(this);
		
		this.context = this;

		tvAll = (TextView) findViewById(R.id.tv_all);
		bottomView = findViewById(R.id.bottomview);
		btnMiss = (Button) findViewById(R.id.btn_restore);
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
						adapter.isSelected.get(key).setState(Constants.STATE_WAIT_TAKE_GOODS);
						RemnantGoodsProvider.getInstance(context).updateWaitReceivePart2(adapter.isSelected.get(key));
					}
				}
				Toast.makeText(context, "设置成功", Toast.LENGTH_SHORT).show();
				selectedNum=0;
				resetEditView();
				isEdit=false;
				adapter.isSelected.clear();
				
				loadAllData();
				map = RemnantGoodsProvider.getInstance(context)
						.getHaveMissed();
				adapter=new NLNotFoundAdapter(map);
				adapter.notifyDataSetChanged();
				partListView.setAdapter(adapter);
			}
		});
		partListView = (PinnedHeaderListView) findViewById(R.id.not_found_listview);
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
					// checkbox.setBackgroundResource(R.drawable.friends_sends_pictures_select_icon_selected);
				} else {
					adapter.isSelected.put(key, null);
					selectedNum--;
					// checkbox.setBackgroundResource(R.drawable.friends_sends_pictures_select_icon_unselected);
				}
				btnMiss.setText("(" + selectedNum + ")个零件恢复");
				adapter.notifyDataSetChanged();
			}
		});
		adapter = new NLNotFoundAdapter(map);
		partListView.setAdapter(adapter);

		// 设置emptyView
		emptyView = findViewById(R.id.empty_view);
		progressBar = (ProgressBar) emptyView.findViewById(R.id.progress);
		tvEmpty = (TextView) emptyView.findViewById(R.id.tv_empty);
		partListView.setEmptyView(emptyView);
		
		loadAllData();

	}
	
	private void resetEditView(){
		btnMiss.setText("(" + selectedNum + ")个零件恢复");
		bottomView.setVisibility(View.GONE);
		setBarRightString("选择");
	}
	
	private void loadAllData() {
		// 加载数据
		totalCount = RemnantGoodsProvider.getInstance(context)
				.getRemnantGoodsHaveMissedCount();
		tvAll.setText("未找到零件总数：" + totalCount);
		loadData();
	}

	private void setEmptyViewNoData() {
		progressBar.setVisibility(View.GONE);
		tvEmpty.setText("暂无数据");
	}

	public void loadData() {
		map = RemnantGoodsProvider.getInstance(context)
				.getHaveMissed();
		if(map.size()==0){
			setEmptyViewNoData();
		}
		adapter.update(map, false);
		if (map.size() == 0) {
			getBtnRight().setVisibility(View.INVISIBLE);
		}
	}
	private class LoadPartAsyncTask
			extends
			AsyncTask<Void, Void, LinkedHashMap<String, List<DataDownloadEntity>>> {

		@Override
		protected LinkedHashMap<String, List<DataDownloadEntity>> doInBackground(
				Void... params) {
			// TODO Auto-generated method stub
			map = RemnantGoodsProvider.getInstance(context)
					.getHaveMissed();
			return map;
		}

		@Override
		protected void onPostExecute(
				LinkedHashMap<String, List<DataDownloadEntity>> map) {
			if(map.size()==0){
				setEmptyViewNoData();
			}
			adapter.update(map, false);
			if (map.size() == 0) {
				getBtnRight().setVisibility(View.INVISIBLE);
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
		
		case R.id.btn_bar_right:
			if (isEdit == false) {
				setBarRightString("取消");
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
		}

	}



}
