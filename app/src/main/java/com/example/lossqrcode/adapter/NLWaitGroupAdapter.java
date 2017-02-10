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
import com.example.lossqrcode.entity.DataDownloadEntity;
import com.example.lossqrcode.ui.PhotoActivity;
import com.example.lossqrcode.ui.widget.SectionedBaseAdapter;
import com.example.lossqrcode.utils.DateUtils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class NLWaitGroupAdapter extends SectionedBaseAdapter {
    public LinkedHashMap<String, List<DataDownloadEntity>> map = new LinkedHashMap<String, List<DataDownloadEntity>>();
    public List<String> goodNoList = new ArrayList<String>();
    public Map<String, DataDownloadEntity> isSelected = new HashMap<String, DataDownloadEntity>();
    public boolean isEdit = false;
    private Context context;

    public NLWaitGroupAdapter(LinkedHashMap<String, List<DataDownloadEntity>> map, Context context) {
        this.map = map;
        this.context = context;
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
        goodNoList.clear();
        if (this.map != null) {
            Iterator i = map.entrySet().iterator();
            while (i.hasNext()) {
                Map.Entry e = (Map.Entry) i.next();
                goodNoList.add((String) e.getKey());
            }
        }

        for (String key : map.keySet()) {
            List<DataDownloadEntity> subList = map.get(key);
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
                            ViewGroup parent) {
        View view = null;
//		if (convertView == null) {
        LayoutInflater inflator = (LayoutInflater) parent.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflator.inflate(R.layout.nlwait_receive_list_item, parent,
                false);
//		} else {
//			view = convertView;
//		}

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

        TextView tViewCareState = (TextView) view.findViewById(R.id.careStateTxt);
        TextView tvRepairAddress = (TextView) view
                .findViewById(R.id.repairAddress);

        final DataDownloadEntity remnantGood4App = map.get(
                goodNoList.get(section)).get(position);
//		view.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO 点击一项Item弹出照片
////				跳转到图片页面
//				if (!remnantGood4App.getUrl().equals("")&&!remnantGood4App.getUrl().equals(" ")&&remnantGood4App.getUrl()!=null) {
//					Intent photoIntent = new Intent(context, PhotoActivity.class);
//					photoIntent.putExtra("url", remnantGood4App.getUrl());
//					context.startActivity(photoIntent);
//				} else {
//					Toast.makeText(context, "无照片", Toast.LENGTH_SHORT).show();
//				}
//			}
//		});
        String repairAddress = remnantGood4App.getRepairAddress();
        String careStateTxt = remnantGood4App.getCareState();
        if ("0".equals(careStateTxt) || careStateTxt == null || "".equals(careStateTxt)) {
            tViewCareState.setText("未关注");
            tViewCareState.setVisibility(View.GONE);

        } else {
            tViewCareState.setText("关注");
        }
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
        tvRepairAddress.setText(repairAddress);
        tvBarcode.setText(remnantGood4App.getBarCode());

//		tvGoodsNo.setText(remnantGood4App.getBarghdh());
        tvGoodsNo.setText(remnantGood4App.getRepairPhone());
        final String phone = tvGoodsNo.getText().toString().trim();
        tvGoodsNo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new AlertDialog.Builder(context)
                        .setTitle("提示")
                        .setMessage("是否拨打电话:"+phone)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent intent = new Intent();
                                intent.setAction("android.intent.action.CALL");
                                intent.setData(Uri.parse("tel:" + phone));//mobile为你要拨打的电话号码，模拟器中为模拟器编号也可
                                context.startActivity(intent);
                            }
                        })
                        .setNeutralButton("取消", null)
                        .create();
                dialog.show();
                //拨打电话

            }
        });
        tvGoodsNo.setTextColor(Color.BLUE);
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
//		tvGoodNo.setText(goodNoList.get(section));

        if (map.get(goodNoList.get(section)).get(0).getBarcph() != null) {
            tvGoodNo.setText(map.get(goodNoList.get(section)).get(0).getBarcph().toString());
        } else {
            tvGoodNo.setText("");
        }
        tvTotalNum.setText("数量：" + map.get(goodNoList.get(section)).size());
        return view;
    }

}
