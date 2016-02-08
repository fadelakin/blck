package com.fisheradelakin.blck;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;

import java.io.File;
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

        //Picasso.with(mContext).load(file).into(holder.picture);
        holder.picture.setImageURI(Uri.fromFile(file));
    }

    @Override
    public int getItemCount() {
        return mFileList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView picture;

        public ViewHolder(View itemView) {
            super(itemView);

            picture = (SimpleDraweeView) itemView.findViewById(R.id.image);
        }
    }
}
