package com.example.ykhuang.imgmixtext.note.bean;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

/**
 * Created by ykhuang on 2017/11/27.
 */
@Entity(tableName = "NOTE")//声明这是一个实体类。其中，tableName 如果不写，那么默认类名就是表名
public class NoteData implements Serializable{
    /**
     * 声明这是一个主键。其中，autoGenerate = true 代表自动生成，而且会随着数据增加自增长
     */
    @PrimaryKey(autoGenerate = true)
    private int _id;
    private String title;
    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int get_id() {

        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }
}
