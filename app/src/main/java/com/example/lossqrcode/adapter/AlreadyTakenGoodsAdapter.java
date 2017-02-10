package com.example.lossqrcode.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lossqrcode.R;
import com.example.lossqrcode.entity.RemnantGoodsEntity;

public class AlreadyTakenGoodsAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private Context context;
	private List<RemnantGoodsEntity> partList;
	


	public AlreadyTakenGoodsAdapter(Context context) {
		this.context=context;
		inflater=LayoutInflater.from(context);
		partList=new ArrayList<RemnantGoodsEntity>();
		
	}
	
	/**
	 * 加载更多数据
	 * @param partList
	 */
	public void loadMore(List<RemnantGoodsEntity> partList){
		if(this.partList!=null&&partList!=null){
			this.partList.addAll(partList);
			notifyDataSetChanged();
		}
	}
	
	/**
	 * 刷新数据
	 * @param partList
	 */
	public void refresh(List<RemnantGoodsEntity> partList){
		if(this.partList!=null&&partList!=null){
			this.partList.clear();
			this.partList.addAll(partList);
			notifyDataSetChanged();
		}
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return partList==null?0:partList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder=null;
		if(convertView==null){
			convertView=inflater.inflate(R.layout.already_receive_list_item, parent,false);
			holder=new ViewHolder();
			holder.tvNo=(TextView)convertView.findViewById(R.id.tv_no);
			holder.tvInsuranceCompany=(TextView)convertView.findViewById(R.id.tv_insurance_company);
			holder.tvGoodsNo=(TextView)convertView.findViewById(R.id.tv_goods_no);
			holder.tvReportNo=(TextView)convertView.findViewById(R.id.tv_report_no);
			holder.tvVehicleModel=(TextView)convertView.findViewById(R.id.tv_vehicle_model);
			holder.tvPartName=(TextView)convertView.findViewById(R.id.tv_part_name);
			holder.tvCreateDate=(TextView)convertView.findViewById(R.id.tv_create_date);
			holder.tvTakeGoodsDate=(TextView)convertView.findViewById(R.id.tv_take_goods_date);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		
		RemnantGoodsEntity remnantGood4App=partList.get(position);
		
		String reportNo=remnantGood4App.getBah();
		String vehicleModel=remnantGood4App.getCxmc();
		String partName=remnantGood4App.getLjmc();
		Date createDate=remnantGood4App.getCreateDate();
		Date scanDate=remnantGood4App.getScanDate();
		
		String createDateStr=null;
		String scanDateStr = null;
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		if(createDate!=null){
			createDateStr=sdf.format(createDate);
			holder.tvCreateDate.setText(createDateStr);
		}
		if(scanDate!=null){
			scanDateStr=sdf.format(scanDate);
			holder.tvTakeGoodsDate.setText(scanDateStr);
		}
		holder.tvNo.setText(position+1+"");
		holder.tvInsuranceCompany.setText(remnantGood4App.getBxgsmc());
		holder.tvGoodsNo.setText(remnantGood4App.getGoodNo());
		holder.tvVehicleModel.setText(vehicleModel);
		holder.tvPartName.setText(partName);
		holder.tvReportNo.setText(reportNo);
		
		System.out.println("appno:"+remnantGood4App.getAppNo());
		return convertView;
	}
	
	
	
	static class ViewHolder{
		
		/**
		 * 保险公司
		 */
		public TextView tvNo;
		/**
		 * 保险公司
		 */
		public  TextView tvInsuranceCompany;
		/**
		 * 供货单号
		 */
		public TextView tvGoodsNo;
		/**
		 * 报案号
		 */
		public TextView tvReportNo;
		/**
		 * 车型
		 */
		public TextView tvVehicleModel;
		/**
		 * 配件名称
		 */
		public TextView tvPartName;
		/**
		 * 创建日期
		 */
		public TextView tvCreateDate;
		
		/**
		 * 提货日期
		 */
		public TextView tvTakeGoodsDate;
	}
	
}
