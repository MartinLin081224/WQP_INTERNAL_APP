package com.example.a10609516.wqp_internal_app;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.example.a10609516.wqp_internal_app.Tools.MediaLoader;

import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumConfig;

import java.util.Locale;

public class Application extends android.app.Application {

    private static Application instance;

    @Override
    public void onCreate() {
        super.onCreate();
        if (instance == null) {
            instance = this;

            Album.initialize(AlbumConfig.newBuilder(this)
                    .setAlbumLoader(new MediaLoader())
                    .setLocale(Locale.getDefault())
                    .build()
            );
        }
    }

    public static Application getInstance() {
        return instance;
    }
}