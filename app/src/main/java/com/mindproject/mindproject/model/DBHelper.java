package com.mindproject.mindproject.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

public class DBHelper extends SQLiteOpenHelper {

    private SQLiteDatabase mDb;

    public DBHelper(Context context) {
        // конструктор суперкласса
        super(context, "inmindDB", null, 1);
        mDb = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DB", "--- onCreate database ---");
        // создаем таблицу с полями
        db.execSQL("create table inminddb ("
                + "id integer primary key autoincrement,"
                + "event_id integer UNIQUE);");
    }

    public Single<Integer> isExistIdInDatabase(int id){
        return Single.create(new SingleOnSubscribe<Integer>() {
            @Override
            public void subscribe(SingleEmitter<Integer> singleEmitter) throws Exception {
                Cursor cursor = mDb.rawQuery("SELECT * FROM inminddb WHERE event_id = " + id, null);
                Log.d("Database", cursor.getCount()+"");
                singleEmitter.onSuccess(cursor.getCount());
            }
        });
    }

    public Completable addIdToDatabase(ContentValues contentValues){
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter completableEmitter) throws Exception {
                long d = mDb.insert("inminddb", null, contentValues);
                Log.d("Database", "add = " + d);
                completableEmitter.onComplete();
            }
        });
    }

    public Completable deleteIdFromDatabase(int id){
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter completableEmitter) throws Exception {
                long d = mDb.delete("inminddb", "event_id = " + id, null);
                Log.d("Database", "add = " + d);
                completableEmitter.onComplete();
            }
        });
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
