package com.example.ykhuang.imgmixtext.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.ykhuang.imgmixtext.BuildConfig;
import com.example.ykhuang.imgmixtext.note.MixEntity;
import com.example.ykhuang.imgmixtext.note.adapter.MixMulAdapter;
import com.example.ykhuang.imgmixtext.R;
import com.example.ykhuang.imgmixtext.util.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ykhuang on 2017/11/21.
 * 图文混排的编辑控件
 */

public class ImgMixTxtView extends RecyclerView{
    private MixMulAdapter mMixMulAdapter;
    private List<MixEntity> mList;
    private EditText mFootEdit;
    private boolean isFootHasFocus;
    public ImgMixTxtView(Context context) {
        this(context,null);
    }
    public ImgMixTxtView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    private void init(){
        //默认设置为竖直排列的
        this.setLayoutManager(new LinearLayoutManager(getContext()));
        mList = new ArrayList<>();
        mMixMulAdapter = new MixMulAdapter(mList,MixEntity.VIEW_TYPE_EDIT);
        //添加底部的编辑editText
        View footView = LayoutInflater.from(getContext()).inflate(R.layout.mix_foot_item_edit,null);
        mMixMulAdapter.setFooterView(footView);
        mFootEdit = (EditText) footView.findViewById(R.id.et_mix_text);
        mFootEdit.setHint("请输入内容");
        this.setAdapter(mMixMulAdapter);

        //添加item的子控件的点击事件
        mMixMulAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()){
                    case R.id.iv_mix_delete_img:
                        mList.remove(position);
                        mMixMulAdapter.notifyDataSetChanged();
                        break;

                    default:
                        break;
                }
            }
        });

    }

    /**
     * 添加图片进来
     */
    public void addImg(String url){
        //先保存当前编辑的文字
        saveCurrentText();
        //添加图片进来
        MixEntity entity = new MixEntity(MixEntity.CONTENT_TYPE_IMG);
        entity.imgUrl = url;
        //插入图片到当前编辑的eidittext的下面
        mList.add(mMixMulAdapter.getCurrentPosition() +1,entity);

        mMixMulAdapter.notifyDataSetChanged();

        //自动滑到最底部的editText进行编辑
        this.scrollToPosition(mMixMulAdapter.getCurrentPosition());
    }

    /**
     * 保存当前正在编辑的文本，这个方法应该选择图片的就调用
     */
    private void saveCurrentText(){
        if(mFootEdit.hasFocus()){
            MixEntity entity = new MixEntity(MixEntity.CONTENT_TYPE_TEXT);
            entity.contentTxt = mFootEdit.getText().toString().trim();
            mList.add(entity);
            mMixMulAdapter.setmCurrentPosition(mList.size() - 1);
            mFootEdit.setHint("");
            mFootEdit.setText("");
        }else{
            EditText editText = mMixMulAdapter.getCurrentEdit();
            if(editText!=null){
                MixEntity entity = mList.get(mMixMulAdapter.getCurrentPosition());
                entity.contentTxt = editText.getText().toString().trim();
            }
        }

    }

    /**
     * 得到所有图片的list,用于上传到OSS使用
     * @return
     */
    public List<String> getImgList(){
        List<String> list = new ArrayList<>();
        for(MixEntity entity : mList){
            if(MixEntity.CONTENT_TYPE_IMG == entity.getItemType()){
                list.add(entity.imgUrl);
            }
        }
        return list;
    }
    public List<MixEntity> getDataList(){
        return this.mList;
    }

    /**
     * 修改imgUrl为服务器上面的地址
     * @param position
     */
    public void setNewImg(int position,String serverPath){
//        String[] endpointResource = BuildConfig.END_POINT.split("//");
//        String endPoint = "";
//        if(endpointResource.length>1){
//            endPoint = endpointResource[1];
//        }
//        String[] fileResource = mList.get(position).imgUrl.split("/");
//        String fileName = fileResource[fileResource.length - 1];
//        mList.get(position).imgUrl = "http://"+ BuildConfig.BUCKET_NAME+"."+endPoint+"/"+serverPath+fileName;
    }
    /**
     * 得到编辑后的HTML的内容
     * @return
     */
    public StringBuffer getContent(){
        StringBuffer content = new StringBuffer();
        for(MixEntity entity : mList){
            switch (entity.getItemType()){
                case MixEntity.CONTENT_TYPE_IMG:
                    content.append("<img src=\"").append(entity.imgUrl).append("\"/>");
                    break;
                case MixEntity.CONTENT_TYPE_TEXT:
                    content.append(entity.contentTxt);
                    break;
                default:
            }
        }
        content.append(mFootEdit.getText().toString().trim());
        return content;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(mList.size()<1){
                    mFootEdit.requestFocus();
                    showKeybord(mFootEdit);
                }
                break;
            default:
        }
        return super.onTouchEvent(e);
    }
    private void showKeybord(EditText editText){
//        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.showSoftInput(editText,InputMethodManager.SHOW_FORCED);
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
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
                if (new File(imagePath).exists()) {
                    entity  = new MixEntity(MixEntity.CONTENT_TYPE_IMG);
                    entity.imgUrl = imagePath;
                    list.add(entity);
                } else {
                    //找不到图片，应该自行提示找不到图
                }
            } else {
                entity  = new MixEntity(MixEntity.CONTENT_TYPE_TEXT);
                entity.contentTxt = text;
                list.add(entity);
            }
        }
        return list;
    }

}
