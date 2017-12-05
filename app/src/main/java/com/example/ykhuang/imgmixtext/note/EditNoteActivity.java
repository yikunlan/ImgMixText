package com.example.ykhuang.imgmixtext.note;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ykhuang.imgmixtext.MainActivity;
import com.example.ykhuang.imgmixtext.R;
import com.example.ykhuang.imgmixtext.database.AppDatabase;
import com.example.ykhuang.imgmixtext.note.bean.NoteData;
import com.example.ykhuang.imgmixtext.widget.ImgMixTxtView;

/**
 * 编辑图文混排
 */
public class EditNoteActivity extends AppCompatActivity implements View.OnClickListener {
    private final int INSERT_FINISH = 0x001;

    private EditText et_note_title;
    private ImgMixTxtView itv_content;
    private ImageView btn_select_img;
    private TextView tv_submit;

    public static void open(Context context){
        Intent intent = new Intent(context,EditNoteActivity.class);
        context.startActivity(intent);
    }
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case INSERT_FINISH:
                    showMessage("保存成功");
                    finish();
                    break;
                default:
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        init();

    }

    private void init() {
        et_note_title = (EditText) findViewById(R.id.et_note_title);
        itv_content = (ImgMixTxtView) findViewById(R.id.itv_content);
        btn_select_img = (ImageView) findViewById(R.id.btn_select_img);
        btn_select_img.setOnClickListener(this);
        tv_submit = (TextView) findViewById(R.id.tv_submit);
        tv_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_submit:
                if(checkCanSubmit()){
                    final NoteData noteData = new NoteData();
                    noteData.setTitle(et_note_title.getText().toString().trim());
                    noteData.setContent(itv_content.getContent().toString().trim());

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            AppDatabase.getDatabase(EditNoteActivity.this).getNoteBeanDao().insertNote(noteData);
                            Message msg = Message.obtain();
                            msg.what = INSERT_FINISH;
                            mHandler.sendMessage(msg);
                        }
                    }).start();
                }
                break;
            case R.id.btn_select_img:
                break;

            default:
        }
    }

    private boolean checkCanSubmit(){
        if(et_note_title.getText().toString().trim().length()<1){
            showMessage("标题不能为空");
            return false;
        }
        if(itv_content.getContent().toString().trim().length()<1){
            showMessage("内容不能为空");
            return false;

        }
        return  true;
    }

    private void showMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}
