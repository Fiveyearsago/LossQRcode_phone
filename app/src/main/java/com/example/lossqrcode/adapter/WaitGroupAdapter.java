package com.example.lossqrcode.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.example.lossqrcode.R;
import com.example.lossqrcode.entity.RemnantGoodsEntity;
import com.example.lossqrcode.ui.widget.SectionedBaseAdapter;
import com.example.lossqrcode.utils.DateUtils;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class WaitGroupAdapter extends SectionedBaseAdapter {
	public LinkedHashMap<String, List<RemnantGoodsEntity>> map = new LinkedHashMap<String, List<RemnantGoodsEntity>>();
	public List<String> goodNoList = new ArrayList<String>();
	public Map<String, RemnantGoodsEntity> isSelected = new HashMap<String, RemnantGoodsEntity>();
	public boolean isEdit = false;

	public WaitGroupAdapter(LinkedHashMap<String, List<RemnantGoodsEntity>> map) {
		this.map = map;

		if (this.map != null) {
			Iterator i = map.entrySet().iterator();
			while (i.hasNext()) {
				Map.Entry e = (Map.Entry) i.next();
				goodNoList.add((String) e.getKey());
				System.out.println("key:" + e.getKey());
			}
		}
		System.out.println(map.size());
	}

	public void update(LinkedHashMap<String, List<RemnantGoodsEntity>> map,
			boolean isEdit) {
		this.map = map;
		this.isEdit = isEdit;
		goodNoList.clear();
		if (this.map != null) {
			Iterator i = map.entrySet().iterator();
			while (i.hasNext()) {
				Map.Entry e = (Map.Entry) i.next();
				goodNoList.add((String) e.getKey());
				System.out.println("key:" + e.getKey());
			}
		}

		for (String key : map.keySet()) {
			System.out.println("good:" + key);
			List<RemnantGoodsEntity> subList = map.get(key);
			for (int i = 0; i < subList.size(); i++) {
				System.out.println("baoanhao:" + subList.get(i).getAppNo());
			}
		}
		notifyDataSetChanged();
	}

	@Override
	public Object getItem(int section, int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int section, int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSectionCount() {
		// TODO Auto-generated method stub
		return map != null ? map.size() : 0;
	}

	@Override
	public int getCountForSection(int section) {
		// TODO Auto-generated method stub
		if (map != null) {
			if (goodNoList.size() > 0) {
				List<RemnantGoodsEntity> list = map
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
			ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = null;
		if (convertView == null) {
			LayoutInflater inflator = (LayoutInflater) parent.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflator.inflate(R.layout.wait_receive_list_item, parent,
					false);
		} else {
			view = convertView;
		}
		final ImageView checkbox = (ImageView) view.findViewById(R.id.checkbox);
		TextView tvNo = (TextView) view.findViewById(R.id.tv_no);
		TextView tvBarcode = (TextView) view.findViewById(R.id.tv_barcode);
		TextView tvInsuranceCompany = (TextView) view
				.findViewById(R.id.tv_insurance_company);
		TextView tvGoodsNo = (TextView) view.findViewById(R.id.tv_goods_no);
		TextView tvReportNo = (TextView) view.findViewById(R.id.tv_report_no);
		TextView tvVehicleModel = (TextView) view
				.findViewById(R.id.tv_vehicle_model);
		TextView tvPartName = (TextView) view.findViewById(R.id.tv_part_name);
		TextView tvCreateDate = (TextView) view
				.findViewById(R.id.tv_create_date);

		final RemnantGoodsEntity remnantGood4App = map.get(
				goodNoList.get(section)).get(position);

		String reportNo = remnantGood4App.getBah();
		String vehicleModel = remnantGood4App.getCxmc();
		String partName = remnantGood4App.getLjmc();
		Date createDate = remnantGood4App.getCreateDate();

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
		tvBarcode.setText(remnantGood4App.getAppNo());
		tvInsuranceCompany.setText(remnantGood4App.getBxgsmc());
		tvGoodsNo.setText(remnantGood4App.getGoodNo());
		tvVehicleModel.setText(vehicleModel);
		tvPartName.setText(partName);
		tvReportNo.setText(reportNo);

		if (isEdit == false) {
			checkbox.setVisibility(View.GONE);
		} else {
			checkbox.setVisibility(View.VISIBLE);
		}

		final String pos = section + "" + position;
		final RemnantGoodsEntity rg = isSelected.get(pos);
		if (rg == null) {
			checkbox.setBackgroundResource(R.drawable.friends_sends_pictures_select_icon_unselected);
		} else {
			checkbox.setBackgroundResource(R.drawable.friends_sends_pictures_select_icon_selected);
		}

		// checkbox.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// if (rg==null) {
		// isSelected.put(pos, remnantGood4App);
		// checkbox
		// .setBackgroundResource(R.drawable.friends_sends_pictures_select_icon_selected);
		// } else {
		// isSelected.put(pos, null);
		// checkbox
		// .setBackgroundResource(R.drawable.friends_sends_pictures_select_icon_unselected);
		// }
		// }
		// });

		return view;
	}

	@Override
	public View getSectionHeaderView(int section, View convertView,
			ViewGroup parent) {
		// TODO Auto-generated method stub
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
		tvGoodNo.setText(goodNoList.get(section));
		tvTotalNum.setText("ÊýÁ¿£º" + map.get(goodNoList.get(section)).size());
		return view;
	}

}
