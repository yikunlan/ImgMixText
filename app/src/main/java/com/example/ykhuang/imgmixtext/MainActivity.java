package com.example.ykhuang.imgmixtext;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.ykhuang.imgmixtext.note.MixShowActivity;
import com.example.ykhuang.imgmixtext.note.bean.NoteBean;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView tv_right;
    RecyclerView rcv;
    //把这个作为编辑后数据保存的位置
    public static List<NoteBean> mDataList;
    private MainAdapter mAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadData();
        init();
    }

    private void loadData() {
        if(mDataList==null){
            mDataList = new ArrayList<>();
        }

        NoteBean noteBean = new NoteBean();
        noteBean.title="淡写年华";
        noteBean.content=" 许我们的心里藏有一个海洋，流出来的却是两行清泪，可我并不感到孤单，我只是简单地喜欢书写凄凉。曾经我们敷衍的情绪，在别人记忆的曲线里渐行渐远渐无言。难道曾经的悸动，只是岁月留给我一个人的错觉，我有点不相信自己的眼睛。我没有哭泣，只是学会了思念而已。" +
                "<img src=\"https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1351005021,1178955812&fm=27&gp=0.jpg\"/>" +
                " 曾经我们也有过如海子一般“面朝大海，春暖花开”的美好愿景，曾经我们也为“那一年”的往事刻骨铭心，曾经我们也不知道忙碌夜夜狂欢，曾经我们也有过假装欺骗自己……在我们过去的时光里，一件件悲伤的、快乐的往事像一粒粒珍珠般串联在一起，构成了我们如花似玉的年华。" +
                "<img src=\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1511782134860&di=97c51a34b7284c96694af2e6407cd668&imgtype=0&src=http%3A%2F%2Fscimg.jb51.net%2Fallimg%2F150814%2F14-150Q41A145606.jpg\"/>" +
                "回首似水流年，内心莫名的宁静，也许是我们的心慢慢变老渐渐远离年轻。曾经，我们会为某件事伤心欲绝，也会为某件事欢心跳跃，我们会爱某个人爱到死去活来，我们也会恨某个人恨到不能自己，我们可以爱，我们可以恨，但我们不可以漫不经心。然而，年华像风里的白桦叶一般被风弄得沙沙作响，可我们现在的心平静得就像溪水般缓缓流淌，原来谁都可以不用顾忌着谁，原来谁都可以忘记离合悲欢，谁都可以简单地生活。" +
                "<img src=\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1511781856406&di=cd91a87642aab467335a7b441802be6b&imgtype=0&src=http%3A%2F%2Fpic.58pic.com%2F58pic%2F11%2F74%2F02%2F58I58PIC2pM.jpg\"/>" ;

        mDataList.add(noteBean);
        mDataList.add(noteBean);
        mDataList.add(noteBean);
    }


    private void init(){
        tv_right = (TextView) findViewById(R.id.tv_right);
//        tv_right.setText("新建");

        rcv = (RecyclerView) findViewById(R.id.rcv);
        rcv.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MainAdapter(this,mDataList);
        rcv.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new MainAdapter.RecyclerViewItemClickListener(){

            @Override
            public void onItemClick(int position) {
                MixShowActivity.open(MainActivity.this,mDataList.get(position));
            }
        });
    }
}
