package com.example.ykhuang.imgmixtext.note;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.example.ykhuang.imgmixtext.R;
import com.example.ykhuang.imgmixtext.note.bean.NoteBean;
import com.example.ykhuang.imgmixtext.widget.ImgMixTxtShowView;

public class MixShowActivity extends AppCompatActivity {
    public static final String NOTE_BEAN= "note_bean";

    private TextView note_title;
    private ImgMixTxtShowView note_content;

    public static void open(Context context , NoteBean noteBean){
        Intent intent = new Intent(context,MixShowActivity.class);
        intent.putExtra(NOTE_BEAN,noteBean);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mix_show);

        init();
        loadData();
    }

    private void loadData() {
        if(getIntent().getExtras()!=null){
            NoteBean noteBean = (NoteBean) getIntent().getExtras().get(NOTE_BEAN);

            if(noteBean!=null){
                if(!TextUtils.isEmpty(noteBean.title)){
                    note_title.setText(noteBean.title);
                }
                if(!TextUtils.isEmpty(noteBean.content)){
                    //使用html进行图文显示（只需要一句话就是这么简单）
                    note_content.setData(noteBean.content);
                }
            }
        }
    }

    private void init() {
        note_title = (TextView) findViewById(R.id.note_title);
        note_content = (ImgMixTxtShowView) findViewById(R.id.note_content);
    }

}
