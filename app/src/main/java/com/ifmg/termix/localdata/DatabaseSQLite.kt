package com.ifmg.termix.localdata

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseSQLite(context: Context) : SQLiteOpenHelper(
    context,
    DatabaseContract.DATABASE_NAME, null,
    DatabaseContract.DATABASE_VERSION
) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(DatabaseContract.WORD.SQL_CREATE)
        db?.execSQL(DatabaseContract.PLAYER_WORDS.SQL_CREATE)
        db?.execSQL(DatabaseContract.PROFILE.SQL_CREATE)
        db?.execSQL(DatabaseContract.CALENDAR.SQL_CREATE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(DatabaseContract.SQL_DROP_TABLES)
        onCreate(db)
    }

}