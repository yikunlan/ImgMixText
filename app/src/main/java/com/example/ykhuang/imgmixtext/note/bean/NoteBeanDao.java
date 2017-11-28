package com.example.ykhuang.imgmixtext.note.bean;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by ykhuang on 2017/11/28.
 * 划重点： 1. 顶部使用 @Dao 声明这是 DAO，就是这么简单。
 * 2. CRUD 操作全部使用注解声明，需要具体 sql 语句的，直接在注解里书写就好。
 * 具体支持哪些操作可以去 android.arch.persistence.room 包下面查看，我这边就随便写了几个。
 * 注意，Room会在编译时检查你的 sql 语句，如果有语法错误，或者表名、字段名错误，都会直接编译报错让你修改，避免运行时出现 crash。
 * 3. 你问我这些接口的实现在哪？答案是：Google 会帮你搞定。是的，你只需要写这些就可以了。
 */
@Dao
public interface NoteBeanDao {
    @Query("select * from NOTE")
    List<NoteData> queryNoteList();

    @Query("select * FROM NOTE WHERE TITLE = :title")
    NoteData getByNoteTitle(String title);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNote(NoteData noteData);

    @Delete()
    void delteNote(NoteData noteData);
}
