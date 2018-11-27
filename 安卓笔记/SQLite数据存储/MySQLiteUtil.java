package com.puquan.health.bodyhealth.common.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

public class MySQLiteUtil extends SQLiteOpenHelper {

    String CREATE_TABLE_SQL = "create table memento_tb(_id integer primary key autoincrement,subject,body,date)";

    public MySQLiteUtil(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e("==update db==","oldV:"+oldVersion+",newV:"+newVersion);
    }
}
