package com.fisheradelakin.blck;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by temidayo on 2/7/16.
 */
public class BlckAdapter extends RecyclerView.Adapter<BlckAdapter.ViewHolder> {

    private Context mContext;
    private List<File> mFileList;

    public BlckAdapter(Context context, List<File> fileList) {
        mContext = context;
        mFileList = fileList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.blck_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        File file = mFileList.get(position);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        //Returns null, sizes are in the options variable
        BitmapFactory.decodeFile(file.getAbsolutePath(), options);

        int width = options.outWidth;
        int height = options.outHeight;

        //boolean isInPortrait = height > width;

        SharedPreferences sharedPreferences
                = mContext.getSharedPreferences(BlckApplication.PREFS, Context.MODE_PRIVATE);

        int screenWidth = sharedPreferences.getInt(BlckApplication.SCREEN_WIDTH, 20);

        float scaleFactor = 0;
        if (width > 0)
            scaleFactor = screenWidth / width;

        int scaledHeight = (int) (height * scaleFactor);

        Picasso.with(mContext)
                .load(file)
                .rotate(getOrientationFromExif(file.getAbsolutePath()))
                .resize(screenWidth, scaledHeight)
                .into(holder.picture);
    }

    public void update(List<File> files) {
        mFileList.clear();
        mFileList = files;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mFileList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        ImageView picture;

        public ViewHolder(View itemView) {
            super(itemView);

            picture = (ImageView) itemView.findViewById(R.id.image);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("image/jpeg");
            share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(mFileList.get(getLayoutPosition())));
            mContext.startActivity(Intent.createChooser(share, "Share Image"));
        }

        @Override
        public boolean onLongClick(View v) {
            File file = mFileList.get(getLayoutPosition());
            if(file != null) {
                file.delete();
                Toast.makeText(mContext, "File deleted", Toast.LENGTH_SHORT).show();
                //notifyDataSetChanged();
                File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Blck");
                List<File> fileList = new ArrayList<>();
                for(File file1 : folder.listFiles()) {
                    if (file1.isFile()) {
                        fileList.add(file1);
                    }
                }
                update(fileList);
            }
            return true;
        }
    }

    private static float getOrientationFromExif(String imagePath) {
        int orientation = 0;

        try {
            ExifInterface exif = new ExifInterface(imagePath);
            int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (exifOrientation) {
                // Upside down portrait
                case ExifInterface.ORIENTATION_ROTATE_270:
                    orientation = 270;
                    break;
                // Upside down landscape
                case ExifInterface.ORIENTATION_ROTATE_180:
                    orientation = 180;
                    break;
                // Normal portrait
                case ExifInterface.ORIENTATION_ROTATE_90:
                    orientation = 90;
                    break;
                // Normal Landscape
                case ExifInterface.ORIENTATION_NORMAL:
                    orientation = 0;
                    break;
                default:
                    break;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return (float)orientation;
    }
}
