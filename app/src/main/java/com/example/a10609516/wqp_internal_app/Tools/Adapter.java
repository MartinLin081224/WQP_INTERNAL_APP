package com.example.a10609516.wqp_internal_app.Tools;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a10609516.wqp_internal_app.R;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.impl.OnItemClickListener;
import com.yanzhenjie.album.util.AlbumUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
        * <p>Image adapter.</p>
        * Created by Yan Zhenjie on 2016/10/30.
        */
public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mInflater;
    private OnItemClickListener mItemClickListener;

    private List<AlbumFile> mAlbumFiles;

    public Adapter(Context context, OnItemClickListener itemClickListener) {
        this.mInflater = LayoutInflater.from(context);
        this.mItemClickListener = itemClickListener;
    }

    public void notifyDataSetChanged(List<AlbumFile> imagePathList) {
        this.mAlbumFiles = imagePathList;
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        AlbumFile albumFile = mAlbumFiles.get(position);
        if (albumFile.getMediaType() == AlbumFile.TYPE_IMAGE) {
            return AlbumFile.TYPE_IMAGE;
        } else {
            return AlbumFile.TYPE_VIDEO;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case AlbumFile.TYPE_IMAGE: {
                return new ImageViewHolder(mInflater.inflate(R.layout.item_content_image, parent, false), mItemClickListener);
            }
            case AlbumFile.TYPE_VIDEO: {
                return new VideoViewHolder(mInflater.inflate(R.layout.item_content_video, parent, false), mItemClickListener);
            }
            default: {
                throw new AssertionError("This should not be the case.");
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case AlbumFile.TYPE_IMAGE: {
                ((ImageViewHolder) holder).setData(mAlbumFiles.get(position));
                break;
            }
            case AlbumFile.TYPE_VIDEO: {
                ((VideoViewHolder) holder).setData(mAlbumFiles.get(position));
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mAlbumFiles == null ? 0 : mAlbumFiles.size();
    }

    private static class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private static String file_name;
        private final OnItemClickListener mItemClickListener;
        private ImageView mIvImage;

        ImageViewHolder(View itemView, OnItemClickListener itemClickListener) {
            super(itemView);
            this.mItemClickListener = itemClickListener;
            this.mIvImage = itemView.findViewById(R.id.iv_album_content_image);
            itemView.setOnClickListener(this);
        }

        public void setData(AlbumFile albumFile) {
            Album.getAlbumConfig().
                    getAlbumLoader().
                    load(mIvImage, albumFile);

            file_name = albumFile.getPath();
            Log.e("FILE", file_name);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }

    private static class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final OnItemClickListener mItemClickListener;

        private ImageView mIvImage;
        private TextView mTvDuration;

        VideoViewHolder(View itemView, OnItemClickListener itemClickListener) {
            super(itemView);
            this.mItemClickListener = itemClickListener;
            this.mIvImage = itemView.findViewById(com.yanzhenjie.album.R.id.iv_album_content_image);
            this.mTvDuration = itemView.findViewById(com.yanzhenjie.album.R.id.tv_duration);
            itemView.setOnClickListener(this);
        }

        void setData(AlbumFile albumFile) {
            Album.getAlbumConfig().
                    getAlbumLoader().
                    load(mIvImage, albumFile);
            mTvDuration.setText(AlbumUtils.convertDuration(albumFile.getDuration()));
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }

}



