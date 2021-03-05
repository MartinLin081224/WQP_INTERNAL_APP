package com.example.a10609516.wqp_internal_app.Tools;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.a10609516.wqp_internal_app.R;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.AlbumLoader;

public class MediaLoader implements AlbumLoader {

    @Override
    public void load(ImageView imageView, AlbumFile albumFile) {
        load(imageView, albumFile.getPath());
    }

    @Override
    public void load(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .crossFade()
                .into(imageView);
    }
}
