package com.example.ykhuang.imgmixtext.note;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by ykhuang on 2017/11/21.
 */

public class MixEntity implements MultiItemEntity {
    //控件的类型：编辑，显示
    public static int VIEW_TYPE_EDIT = 0x001;
    public static int VIEW_TYPE_SHOW = 0x002;
    //内容的类型:文本、图片
    public final static int CONTENT_TYPE_TEXT = 0x003;
    public final static int CONTENT_TYPE_IMG = 0x004;

    private  int mItemType;
    public String imgUrl;
    public String contentTxt;

    public MixEntity(int itemType){
        this.mItemType = itemType;
    }

    @Override
    public int getItemType() {
        return mItemType;
    }
}
