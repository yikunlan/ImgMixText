package com.example.ykhuang.imgmixtext.note;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.example.ykhuang.imgmixtext.util.ImageUtils;
import com.example.ykhuang.imgmixtext.util.SDCardUtil;
import com.example.ykhuang.imgmixtext.util.ScreenUtils;
import com.example.ykhuang.imgmixtext.widget.ImgMixTxtView;

import java.util.ArrayList;

import me.iwf.photopicker.PhotoPicker;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 编辑图文混排
 */
public class EditNoteActivity extends Activity implements View.OnClickListener {
    private final int INSERT_FINISH = 0x001;

    private EditText et_note_title;
    private ImgMixTxtView itv_content;
    private ImageView btn_select_img;
    private TextView tv_submit;
    private Subscription subsInsert;


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
                    //异步操作保存数据库
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
                callGallery();

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
    /**
     * 调用图库选择
     */
    private void callGallery(){
//        //调用系统图库
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");// 相片类型
//        startActivityForResult(intent, 1);

        //调用第三方图库选择
        PhotoPicker.builder()
                .setPhotoCount(5)//可选择图片数量
                .setShowCamera(true)//是否显示拍照按钮
                .setShowGif(true)//是否显示动态图
                .setPreviewEnabled(true)//是否可以预览
                .start(this, PhotoPicker.REQUEST_CODE);
    }
    private void showMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null) {
                if (requestCode == 1){
                    //处理调用系统图库
                } else if (requestCode == PhotoPicker.REQUEST_CODE){
                    //异步方式插入图片
                    insertImagesSync(data);
                }
            }
        }
    }

    /**
     * 异步方式插入图片
     * @param data
     */
    private void insertImagesSync(final Intent data){
        subsInsert = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try{
                    int width = ScreenUtils.getScreenWidth(EditNoteActivity.this);
                    int height = ScreenUtils.getScreenHeight(EditNoteActivity.this);
                    ArrayList<String> photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                    //可以同时插入多张图片
                    for (String imagePath : photos) {
                        Bitmap bitmap = ImageUtils.getSmallBitmap(imagePath, width, height);//压缩图片
                        imagePath = SDCardUtil.saveToSdCard(bitmap);
                        subscriber.onNext(imagePath);
                    }
                    subscriber.onCompleted();
                }catch (Exception e){
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        })
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())//生产事件在io
                .observeOn(AndroidSchedulers.mainThread())//消费事件在UI线程
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        showMessage("图片插入成功:");
                    }

                    @Override
                    public void onError(Throwable e) {
                        showMessage("图片插入失败:"+e.getMessage());
                    }

                    @Override
                    public void onNext(String imagePath) {
                        itv_content.addImg(imagePath);
                    }
                });
    }
}
