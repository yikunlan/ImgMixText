package com.example.ykhuang.imgmixtext;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ykhuang.imgmixtext.note.bean.NoteData;

import java.util.List;

/**
 * Created by ykhuang on 2017/11/27.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MyViewHolder> {
    private List<NoteData> mList;
    private Context mContext;

    public MainAdapter(Context context, List<NoteData> list){
        this.mContext = context;
        this.mList = list;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_main,parent,false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        NoteData noteBean = mList.get(position);

        holder.setOnclick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClick(position);
            }
        });
        if(!TextUtils.isEmpty(noteBean.getTitle())){
            holder.tv_title.setText(noteBean.getTitle());
        }
        if(!TextUtils.isEmpty(noteBean.getContent())){
            holder.tv_content.setText(noteBean.getContent());
        }
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder{
        View rootView;
        TextView tv_title;
        TextView tv_content;
        public MyViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
        }

        public void setOnclick(View.OnClickListener listener) {
            if (rootView != null) {
                rootView.setOnClickListener(listener);
            }

        }
    }

    private RecyclerViewItemClickListener mItemClickListener;
    /**
     * 设置Item点击监听
     * @param listener
     */
    public void setOnItemClickListener(RecyclerViewItemClickListener listener){
        this.mItemClickListener = listener;
    }
    public interface RecyclerViewItemClickListener {
        void onItemClick(int position);
    }
}
