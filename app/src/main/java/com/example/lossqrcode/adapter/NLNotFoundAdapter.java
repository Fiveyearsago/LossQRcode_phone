package com.example.lossqrcode.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lossqrcode.R;
import com.example.lossqrcode.entity.DataDownloadEntity;
import com.example.lossqrcode.ui.widget.SectionedBaseAdapter;
import com.example.lossqrcode.utils.DateUtils;

public class NLNotFoundAdapter extends SectionedBaseAdapter {
	public LinkedHashMap<String, List<DataDownloadEntity>> map = new LinkedHashMap<String, List<DataDownloadEntity>>();
	public List<String> goodNoList = new ArrayList<String>();
	public Map<String, DataDownloadEntity> isSelected = new HashMap<String, DataDownloadEntity>();
	public boolean isEdit = false;

	public NLNotFoundAdapter(LinkedHashMap<String, List<DataDownloadEntity>> map) {
		this.map = map;

		if (this.map != null) {
			Iterator i = map.entrySet().iterator();
			while (i.hasNext()) {
				Map.Entry e = (Map.Entry) i.next();
				goodNoList.add((String) e.getKey());
			}
		}
	}

	public void update(LinkedHashMap<String, List<DataDownloadEntity>> map,
			boolean isEdit) {
		this.map = map;
		this.isEdit = isEdit;
		if (this.map != null) {
			Iterator i = map.entrySet().iterator();
			while (i.hasNext()) {
				Map.Entry e = (Map.Entry) i.next();
				goodNoList.add((String) e.getKey());
				System.out.println("key:" + e.getKey());
			}
		}
		notifyDataSetChanged();
	}

	@Override
	public Object getItem(int section, int position) {
		return null;
	}

	@Override
	public long getItemId(int section, int position) {
		return 0;
	}

	@Override
	public int getSectionCount() {
		return map != null ? map.size() : 0;
	}

	@Override
	public int getCountForSection(int section) {
		if (map != null) {
			if (goodNoList.size() > 0) {
				List<DataDownloadEntity> list = map
						.get(goodNoList.get(section));
				if (list != null) {
					return list.size();
				}
			}

		}
		return 0;
	}

	@Override
	public View getItemView(int section, int position, View convertView,
							final ViewGroup parent) {
		View view = null;
		if (convertView == null) {
			LayoutInflater inflator = (LayoutInflater) parent.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflator.inflate(R.layout.nlwait_receive_list_item, parent,
					false);
		} else {
			view = convertView;
		}
		final ImageView checkbox = (ImageView) view.findViewById(R.id.checkbox);
		TextView tvNo = (TextView) view.findViewById(R.id.tv_no);
		TextView tvBarcode = (TextView) view.findViewById(R.id.tv_barcode);
		TextView tvGoodsNo = (TextView) view.findViewById(R.id.tv_goods_no);
		TextView tvReportNo = (TextView) view.findViewById(R.id.tv_report_no);
		TextView tvVehicleModel = (TextView) view
				.findViewById(R.id.tv_vehicle_model);
		TextView tvPartName = (TextView) view.findViewById(R.id.tv_part_name);
		TextView tvCreateDate = (TextView) view
				.findViewById(R.id.tv_create_date);
		TextView tvRepairAddress = (TextView) view
				.findViewById(R.id.repairAddress);

		final DataDownloadEntity remnantGood4App = map.get(
				goodNoList.get(section)).get(position);

		String reportNo = remnantGood4App.getBah();
		String vehicleModel = remnantGood4App.getCxmc();
		String partName = remnantGood4App.getLjmc();
		Date createDate = remnantGood4App.getCreateDate();
		String repairAddress= remnantGood4App.getRepairAddress();

		if (createDate != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String createDateStr = sdf.format(createDate);
			tvCreateDate.setText(createDateStr);
			if (DateUtils.isDateBefore(createDate, new Date())) {
				tvNo.setBackgroundResource(R.drawable.unread_count_bg);
				tvNo.setTextColor(Color.parseColor("#ffffff"));
			} else {
				tvNo.setBackgroundResource(0);
				tvNo.setTextColor(Color.parseColor("#00BFFF"));
			}
		}
		tvNo.setText(position + 1 + "");
		tvRepairAddress.setText(repairAddress);
		tvBarcode.setText(remnantGood4App.getBarCode());
//		tvGoodsNo.setText(remnantGood4App.getBarghdh());
		tvGoodsNo.setText(remnantGood4App.getRepairPhone());
		tvGoodsNo.setTextColor(Color.BLUE);
		final String phone = tvGoodsNo.getText().toString().trim();
		tvGoodsNo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Dialog dialog = new AlertDialog.Builder( parent.getContext())
						.setTitle("提示")
						.setMessage("是否拨打电话:"+phone)
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								Intent intent = new Intent();
								intent.setAction("android.intent.action.CALL");
								intent.setData(Uri.parse("tel:" + phone));//mobile为你要拨打的电话号码，模拟器中为模拟器编号也可
								parent.getContext().startActivity(intent);
							}
						})
						.setNeutralButton("取消", null)
						.create();
				dialog.show();
				//拨打电话

			}
		});
		tvVehicleModel.setText(vehicleModel);
		tvPartName.setText(partName);
		tvReportNo.setText(reportNo);

		if (isEdit == false) {
			checkbox.setVisibility(View.GONE);
		} else {
			checkbox.setVisibility(View.VISIBLE);
		}

		final String pos = section + "" + position;
		final DataDownloadEntity rg = isSelected.get(pos);
		if (rg == null) {
			checkbox.setBackgroundResource(R.drawable.friends_sends_pictures_select_icon_unselected);
		} else {
			checkbox.setBackgroundResource(R.drawable.friends_sends_pictures_select_icon_selected);
		}

		return view;
	}

	@Override
	public View getSectionHeaderView(int section, View convertView,
			ViewGroup parent) {
		View view = null;
		if (convertView == null) {
			LayoutInflater inflator = (LayoutInflater) parent.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflator.inflate(R.layout.goodno_header_item, parent, false);
		} else {
			view = convertView;
		}
		TextView tvGoodNo = (TextView) view.findViewById(R.id.tv_goodno);
		TextView tvTotalNum = (TextView) view.findViewById(R.id.tv_total_num);
//		tvGoodNo.setText("供货单号：" + goodNoList.get(section));
		if (map.get(goodNoList.get(section)).get(0).getBarcph() != null) {
			tvGoodNo.setText(map.get(goodNoList.get(section)).get(0).getBarcph().toString());
		} else {
			tvGoodNo.setText("");
		}
		tvTotalNum.setText("数量：" + map.get(goodNoList.get(section)).size());
		return view;
	}

}
