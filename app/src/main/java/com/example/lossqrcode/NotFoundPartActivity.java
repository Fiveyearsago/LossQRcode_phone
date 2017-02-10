package com.example.lossqrcode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.example.lossqrcode.adapter.NotFoundAdapter;
import com.example.lossqrcode.adapter.WaitGroupAdapter;
import com.example.lossqrcode.entity.RemnantGoodsEntity;
import com.example.lossqrcode.service.RemnantGoodsProvider;
import com.example.lossqrcode.ui.widget.PinnedHeaderListView;
import com.example.lossqrcode.ui.widget.PinnedHeaderListView.OnItemClickListener;
import com.example.lossqrcode.utils.Constants;

import android.app.Activity;
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

public class NotFoundPartActivity extends Activity implements OnClickListener{
	private PinnedHeaderListView partListView;
	private TextView tvChoose;
	private TextView tvAll;
	private View bottomView;
	private Button btnMiss;

	private View emptyView;
	private ProgressBar progressBar;
	private TextView tvEmpty;


	private boolean isEdit = false;
	private long totalCount;
	private int selectedNum = 0;

	// private WaitTakeGoodsAdapter adapter;

	private NotFoundAdapter adapter;
	LinkedHashMap<String, List<RemnantGoodsEntity>> map = new LinkedHashMap<String, List<RemnantGoodsEntity>>();



	private Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_not_found_part);
		
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
						RemnantGoodsProvider.getInstance(context).updateWaitReceivePart(adapter.isSelected.get(key));
					}
				}
				Toast.makeText(context, "设置成功", Toast.LENGTH_SHORT).show();
				selectedNum=0;
				resetEditView();
				isEdit=false;
				adapter.isSelected.clear();
				
				loadAllData();
				//adapter.update(map, isEdit);
			}
		});
		partListView = (PinnedHeaderListView) findViewById(R.id.not_found_listview);
		partListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onSectionClick(AdapterView<?> adapterView, View view,
					int section, long id) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int section, int position, long id) {
				// TODO Auto-generated method stub
				if (isEdit == false) {
					return;
				}
				String key = section + "" + position;
				RemnantGoodsEntity rg = adapter.isSelected.get(key);
				if (rg == null) {
					RemnantGoodsEntity rgEntity = (RemnantGoodsEntity) adapter.map
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
		adapter = new NotFoundAdapter(map);
		partListView.setAdapter(adapter);

		// 设置emptyView
		emptyView = findViewById(R.id.empty_view);
		progressBar = (ProgressBar) emptyView.findViewById(R.id.progress);
		tvEmpty = (TextView) emptyView.findViewById(R.id.tv_empty);
		partListView.setEmptyView(emptyView);


		tvChoose = (TextView) findViewById(R.id.tv_choose);
		tvChoose.setOnClickListener(this);
		
		loadAllData();

	}
	
	private void resetEditView(){
		btnMiss.setText("(" + selectedNum + ")个零件恢复");
		bottomView.setVisibility(View.GONE);
		tvChoose.setText("选择");
	}
	
	private void loadAllData() {
		// 加载数据
		totalCount = RemnantGoodsProvider.getInstance(context)
				.getRemnantGoodsHaveMissedCount();
		tvAll.setText("未找到零件总数：" + totalCount);
		new LoadPartAsyncTask().execute();
	}

	private void setEmptyViewNoData() {
		progressBar.setVisibility(View.GONE);
		tvEmpty.setText("暂无数据");
	}

	private class LoadPartAsyncTask
			extends
			AsyncTask<Void, Void, LinkedHashMap<String, List<RemnantGoodsEntity>>> {

		@Override
		protected LinkedHashMap<String, List<RemnantGoodsEntity>> doInBackground(
				Void... params) {
			// TODO Auto-generated method stub
			map = RemnantGoodsProvider.getInstance(context)
					.getRemnantGoodsHaveMissed();
			return map;
		}

		@Override
		protected void onPostExecute(
				LinkedHashMap<String, List<RemnantGoodsEntity>> map) {
			if(map.size()==0){
				setEmptyViewNoData();
			}
			adapter.update(map, false);
			if (map.size() == 0) {
				tvChoose.setVisibility(View.GONE);
			}
			super.onPostExecute(map);
		}

	}

	public void back(View view) {
		finish();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
			if (isEdit == false) {
				tvChoose.setText("取消");
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

	}



}
