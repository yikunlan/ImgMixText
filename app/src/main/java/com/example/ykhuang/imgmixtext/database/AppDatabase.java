package com.example.ykhuang.imgmixtext.database;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.ykhuang.imgmixtext.note.bean.NoteBeanDao;
import com.example.ykhuang.imgmixtext.note.bean.NoteData;

/**
 * Created by ykhuang on 2017/11/28.
 *
 * 划重点： 1. 顶部使用 @Database 声明这是一个数据库类，其中 entities里面声明你的数据库里究竟包含了哪几个实体；version 就不用我说了吧，
 * 写过 SqliteOpenHelper 的肯定不陌生了；第三个属性 exportSchema 比较有意思，Google 建议是传 true，这样可以把 Scheme 导出到一个文件夹里面，
 * Google 还建议你把这个文件上传到 VCS，具体的可以直接点进去看注释。 2. 这个类封装成单例，在任何地方需要执行数据库操作的时候，可以直接获得来使用，
 * Room 提供了一个静态的方法，用来在默认的构造方法里创建了一个数据库，我在这里起的名称是 user.db。 3. 把所有 Entity 的 DAO 接口类全部声明成 abstract 的到这里来。
 * 4. Google 会在编译时自动帮我们生成这些抽象类和方法的实现，代码在 app/build/generated/source/apt/debug
 */
@Database(entities = {NoteData.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase{
    private static AppDatabase instance;
    public static AppDatabase getDatabase(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class,"NOTE.db").build();
        }
        return  instance;
    }

    public static void onDestroy(){
        instance = null;
    }
    public abstract NoteBeanDao getNoteBeanDao();
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }
}
