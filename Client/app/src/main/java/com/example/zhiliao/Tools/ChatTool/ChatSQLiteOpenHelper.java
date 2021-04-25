package com.example.zhiliao.Tools.ChatTool;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ChatSQLiteOpenHelper extends SQLiteOpenHelper {
    private static String name = "chat.db";
    private static Integer version = 1;

    public ChatSQLiteOpenHelper(Context context) {
        super(context, name, null, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table records(id integer primary key autoincrement,name varchar(200),content varchar(500))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
