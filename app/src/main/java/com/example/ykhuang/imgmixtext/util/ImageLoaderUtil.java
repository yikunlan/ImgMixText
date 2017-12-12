package com.example.ykhuang.imgmixtext.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by ykhuang on 2017/8/9.
 * 图片下载器工具类
 */

public class ImageLoaderUtil {
    private static final String TAG = ImageLoaderUtil.class.getSimpleName().toString();


    /**
     * 设置默认图片
     * @param context
     * @param placeId
     * @param url
     * @param imageView
     */
    public static void setUrlToGlide(Context context,int placeId, String url, ImageView imageView) {
        Glide.with(context).load(url).into(imageView);
//        Glide.with(context)
//            .load(url)
//            .placeholder(placeId)
//             .error(placeId)
//            .crossFade()
//            .dontAnimate()
//            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//            .into(imageView);
    }

}
