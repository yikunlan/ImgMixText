package com.example.ykhuang.imgmixtext.note.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.ykhuang.imgmixtext.R;
import com.example.ykhuang.imgmixtext.note.MixEntity;
import com.example.ykhuang.imgmixtext.util.ImageLoaderUtil;

import java.util.List;

/**
 * Created by ykhuang on 2017/11/21.
 */

public class MixMulAdapter extends
        BaseMultiItemQuickAdapter<MixEntity, BaseViewHolder> {

    private boolean mIsInit = false;
    private int mCurrentPosition;
    private int mMixType;
    private EditText mCurrentEdit;
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public MixMulAdapter(List<MixEntity> data,int mixType) {
        super(data);
        addItemType(MixEntity.CONTENT_TYPE_TEXT, R.layout.mix_item_text);
        addItemType(MixEntity.CONTENT_TYPE_IMG, R.layout.mix_item_img);
        this.mMixType = mixType;
    }
    public int getCurrentPosition(){
        return mCurrentPosition;
    }
    public EditText getCurrentEdit(){
        return mCurrentEdit;
    }
    public void setmCurrentPosition(int position){
        this.mCurrentPosition = position;
    }

    @Override
    protected void convert(final BaseViewHolder holder, MixEntity entity) {
        switch (holder.getItemViewType()){
            case MixEntity.CONTENT_TYPE_IMG:


                ImageLoaderUtil.setUrlToGlide(holder.itemView.getContext(),R.mipmap.icon_photo_album,entity.imgUrl, (ImageView) holder.itemView.findViewById(R.id.iv_mix_img));
                if(MixEntity.VIEW_TYPE_EDIT == mMixType){
                    ((ImageView) holder.itemView.findViewById(R.id.iv_mix_delete_img)).setVisibility(View.VISIBLE);
                    //给关闭按钮添加监听事件
                    holder.addOnClickListener(R.id.iv_mix_delete_img);
                }else{
                    ((ImageView) holder.itemView.findViewById(R.id.iv_mix_delete_img)).setVisibility(View.GONE);
                }

                break;
            case MixEntity.CONTENT_TYPE_TEXT:
                if(entity!=null){
                    EditText editText = (EditText) holder.itemView.findViewById(R.id.et_mix_text);
                    if(MixEntity.VIEW_TYPE_EDIT == mMixType){
                        editText.setEnabled(true);
                        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                if (hasFocus) {
                                    mCurrentEdit = (EditText) v;
                                    mCurrentPosition = holder.getAdapterPosition();
                                }
                            }});
                        if(!TextUtils.isEmpty(entity.contentTxt)){
                            editText.setText(entity.contentTxt);
                        }else{
                            //第一次编辑进来的时候，显示hint为内容
                            if(this.getItemCount() == 1 && holder.getAdapterPosition() == 0){
//                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);

//                            editText.setLayoutParams(layoutParams);
                            }

                            //添加图片之后新增的editText
                            if(holder.getAdapterPosition() == (this.getItemCount() -1)){
                                editText.setMinLines(5);
                            }
                            editText.setText("");
                        }
                    }else{
                        editText.setEnabled(false);
                        if(!TextUtils.isEmpty(entity.contentTxt)){
                            editText.setText(entity.contentTxt);
                        }
                    }
                }
                break;
            default:
        }


    }
}
