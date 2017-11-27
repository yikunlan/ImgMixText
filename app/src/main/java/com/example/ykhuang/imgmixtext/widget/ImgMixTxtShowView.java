package com.example.ykhuang.imgmixtext.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.example.ykhuang.imgmixtext.note.MixEntity;
import com.example.ykhuang.imgmixtext.note.adapter.MixMulAdapter;
import com.example.ykhuang.imgmixtext.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ykhuang on 2017/11/21.
 * 图文混排的展示控件
 */

public class ImgMixTxtShowView extends RecyclerView{
    private MixMulAdapter mMixMulAdapter;
    private List<MixEntity> mList;
    public ImgMixTxtShowView(Context context) {
        this(context,null);
    }
    public ImgMixTxtShowView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        //默认设置为竖直排列的
        this.setLayoutManager(new LinearLayoutManager(getContext()));
        mList = new ArrayList<>();
        mList.add(new MixEntity(MixEntity.CONTENT_TYPE_TEXT));
        mMixMulAdapter = new MixMulAdapter(mList,MixEntity.VIEW_TYPE_SHOW);
        this.setAdapter(mMixMulAdapter);
    }

    public void setData(String htmlString){
        mList.clear();
        mList.addAll(parseData(htmlString));
        mMixMulAdapter.notifyDataSetChanged();
    }
    private List<MixEntity> parseData(String html){
        List<MixEntity> list = new ArrayList<>();
        List<String> textList = StringUtils.cutStringByImgTag(html);
        for (int i = 0; i < textList.size(); i++) {
            String text = textList.get(i);
            MixEntity entity;
            if (text.contains("<img") && text.contains("src=")) {
                String imagePath = StringUtils.getImgSrc(text);
                entity  = new MixEntity(MixEntity.CONTENT_TYPE_IMG);
                entity.imgUrl = imagePath;
                list.add(entity);
            } else {
                entity  = new MixEntity(MixEntity.CONTENT_TYPE_TEXT);
                entity.contentTxt = text;
                list.add(entity);
            }
        }
        return list;
    }

}
