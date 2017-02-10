package com.example.lossqrcode.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lossqrcode.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by songran on 16/7/6.
 */
public class ImageAdapter extends BaseAdapter {
    private ArrayList<Bitmap> bitmaps;
    private Context context;
    private ArrayList<String> paths;

    public ImageAdapter(ArrayList<Bitmap> bitmaps, Context context, ArrayList<String> paths) {
        this.bitmaps = bitmaps;
        this.context = context;
        this.paths = paths;
    }

    @Override
    public int getCount() {
        return bitmaps.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ImageView imageView = (ImageView) LayoutInflater.from(context).inflate(R.layout.item_image, null);
        imageView.setImageBitmap(bitmaps.get(position));
        AbsListView.LayoutParams param = new AbsListView.LayoutParams(300, 380);
        imageView.setLayoutParams(param);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageDialog(bitmaps.get(position), paths.get(position),position);

            }
        });
        return imageView;
    }

    private void showImageDialog(Bitmap bitmap, final String path, final int position) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.image_preview, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (path.equals("")) {
            builder.setView(view)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            dialog.dismiss();
                        }

                    });
        } else {
            builder.setView(view)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            dialog.dismiss();
                        }
                    }).setNegativeButton("删除", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                    File file = new File(path);
                    if (file.exists()) {
                        if (file.isFile()) {
                            file.delete();
                        }
                    }
                    bitmaps.remove(position);
                    notifyDataSetChanged();
                }
            });
        }
        ImageView bigImageIv = (ImageView) view.findViewById(R.id.big_image);
        bigImageIv.setImageBitmap(bitmap);
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        ViewGroup.LayoutParams lp = bigImageIv.getLayoutParams();
        if (bitmap.getWidth() < bitmap.getHeight()) {
            lp.width = width * 5 / 6;
            lp.height = height * 5 / 6;
        } else {
            lp.width = bitmap.getWidth() * 2;
            lp.height = bitmap.getHeight() * 2;
        }
        bigImageIv.setLayoutParams(lp);

        builder.show();

    }
}
