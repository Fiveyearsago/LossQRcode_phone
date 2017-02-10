package com.example.lossqrcode.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.lossqrcode.R;
import com.example.lossqrcode.entity.DataDownloadEntity;
import com.example.lossqrcode.service.RemnantGoodsProvider;
import com.example.lossqrcode.ui.NLScanResultActivity;
import com.example.lossqrcode.ui.PhotoActivity;
import com.example.lossqrcode.utils.Constants;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class NLScanResultAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    public List<DataDownloadEntity> partList;
    private TextView tvAll;
    private LCallBack lCallBack;

    public NLScanResultAdapter(Context context, LCallBack lCallBack, List<DataDownloadEntity> partList) {
        this.context = context;
        this.lCallBack = lCallBack;
        inflater = LayoutInflater.from(context);
        this.partList = partList;
    }

    /**
     * ���ظ�������
     *
     * @param partList
     */
    public void loadMore(List<DataDownloadEntity> partList) {
        if (this.partList != null && partList != null) {
            this.partList.addAll(partList);
            notifyDataSetChanged();
        }
    }

    public void loadMore(DataDownloadEntity rgEntity) {
        if (rgEntity != null) {
            this.partList.add(0, rgEntity);
            notifyDataSetChanged();
        }
    }

    public void reset() {
        partList.clear();
        notifyDataSetChanged();
    }

    /**
     * ˢ������
     *
     * @param partList
     */
    public void refresh(List<DataDownloadEntity> partList) {
        if (this.partList != null && partList != null) {
            this.partList.clear();
            this.partList.addAll(partList);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return partList == null ? 0 : partList.size();
    }

    @Override
    public Object getItem(int position) {
        // notifyDataSetChanged();
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        // if(convertView==null){
        convertView = inflater.inflate(R.layout.nlscan_result_list_item,
                parent, false);

        holder = new ViewHolder();
        holder.tvBarcode = (TextView) convertView.findViewById(R.id.tv_barcode);
         holder.tvGoodsNo = (TextView) convertView
                .findViewById(R.id.tv_goods_no);


        holder.tvReportNo = (TextView) convertView
                .findViewById(R.id.tv_report_no);
        holder.tvVehicleModel = (TextView) convertView
                .findViewById(R.id.tv_vehicle_model);
        holder.tvPartName = (TextView) convertView
                .findViewById(R.id.tv_part_name);
        holder.tvCreateDate = (TextView) convertView
                .findViewById(R.id.tv_create_date);
        holder.tvTakeGoodsDate = (TextView) convertView
                .findViewById(R.id.tv_take_goods_date);
        holder.tViewCareState = (TextView) convertView
                .findViewById(R.id.careStateTxt);
        holder.tvBack = (TextView) convertView.findViewById(R.id.returnBack);
        holder.tvRepairAddress = (TextView) convertView.findViewById(R.id.repairAddress);
        convertView.setTag(holder);
        // }
        // else{
        // holder=(ViewHolder)convertView.getTag();
        // }

        final DataDownloadEntity remnantGood4App = partList.get(position);
        convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO ���һ��Item������Ƭ
//				Toast.makeText(context, remnantGood4App.getUrl(), Toast.LENGTH_SHORT).show();
//				��ת��ͼƬҳ��
//                if (!remnantGood4App.getUrl().equals("")&&!remnantGood4App.getUrl().equals(" ")&&remnantGood4App.getUrl()!=null) {
//                    Intent photoIntent = new Intent(context, PhotoActivity.class);
//                    photoIntent.putExtra("url", remnantGood4App.getUrl());
//                    context.startActivity(photoIntent);
//                } else {
//                    Toast.makeText(context, "����Ƭ", Toast.LENGTH_SHORT).show();
//                }
                Intent photoIntent = new Intent(context, PhotoActivity.class);
                //������������id��������ƣ��������ţ����ƺ�
                photoIntent.putExtra("id", remnantGood4App.getId());
                photoIntent.putExtra("partId", remnantGood4App.getPartId());
                photoIntent.putExtra("ljmc", remnantGood4App.getLjmc());
                photoIntent.putExtra("barghdh", remnantGood4App.getBarghdh());
                photoIntent.putExtra("barcph", remnantGood4App.getBarcph());

                context.startActivity(photoIntent);
            }
        });
        String barcode = remnantGood4App.getBarCode();
        String reportNo = remnantGood4App.getBah();
        String vehicleModel = remnantGood4App.getCxmc();
        String partName = remnantGood4App.getLjmc();
        Date createDate = remnantGood4App.getCreateDate();
        Date scanDate = remnantGood4App.getScanDate();
        String repairAddress = remnantGood4App.getRepairAddress();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String createDateStr = "";
        String scanDateStr = "";
        if (createDate != null) {
            createDateStr = sdf.format(createDate);
            holder.tvCreateDate.setText(createDateStr);
        }
        if (scanDate != null) {
            scanDateStr = sdf.format(scanDate);
            holder.tvTakeGoodsDate.setText(scanDateStr);
        }
        String careStateTxt = remnantGood4App.getCareState();
        if ("0".equals(careStateTxt) || careStateTxt == null
                || "".equals(careStateTxt)) {
            holder.tViewCareState.setText("δ��ע");
            holder.tViewCareState.setVisibility(View.GONE);

        } else {
            holder.tViewCareState.setText("��ע");
        }

        holder.tvBarcode.setText(barcode);
//        holder.tvGoodsNo.setText(remnantGood4App.getBarghdh());
        holder.tvGoodsNo.setText(remnantGood4App.getRepairPhone());
        holder.tvGoodsNo.setTextColor(Color.BLUE);
//        final String phone=holder.tvGoodsNo.getText().toString().trim();
//        holder.tvGoodsNo.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //����绰
//
//                Intent intent = new Intent();
//                intent.setAction("android.intent.action.CALL");
//                intent.setData(Uri.parse("tel:"+ phone));//mobileΪ��Ҫ����ĵ绰���룬ģ������Ϊģ�������Ҳ��
//                context.startActivity(intent);
//            }
//        });
        holder.tvVehicleModel.setText(vehicleModel);
        holder.tvPartName.setText(partName);
        holder.tvReportNo.setText(reportNo);
        holder.tvRepairAddress.setText(repairAddress);
        holder.tvBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DataDownloadEntity dataDownloadEntity = partList.get(position);
                dataDownloadEntity.setState(Constants.STATE_WAIT_TAKE_GOODS);
                RemnantGoodsProvider.getInstance(context)
                        .updateWaitReceivePart2(dataDownloadEntity);

                partList.remove(position);
                notifyDataSetChanged();
                lCallBack.refreshCount();
            }
        });
        return convertView;
    }

    interface refreshCount {
        public void refresh();
    }

    @Override
    public boolean areAllItemsEnabled() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        // TODO Auto-generated method stub
        return false;
    }

    static class ViewHolder {

        public TextView tvBarcode;

        /**
         * ��������
         */
        public TextView tvGoodsNo;
        /**
         * ������
         */
        public TextView tvReportNo;
        /**
         * ����
         */
        public TextView tvVehicleModel;
        /**
         * �������
         */
        public TextView tvPartName;

        /**
         * ��������
         */
        public TextView tvCreateDate;
        /**
         * �������
         */
        public TextView tvTakeGoodsDate;

        private TextView tViewCareState;// ��ע״̬
        private TextView tvBack;// ��ԭ
        private TextView tvRepairAddress;// ��ԭ
    }

    public interface LCallBack {
        public void refreshCount(); //
    }
}