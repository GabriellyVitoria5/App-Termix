package com.ifmg.termix.localdata

class DatabaseContract {


    companion object {
        const val DATABASE_NAME: String = "termix.db"
        const val DATABASE_VERSION: Int = 1

        const val SQL_CREATE_TABLES = WORD.SQL_CREATE
        const val SQL_DROP_TABLES = WORD.SQL_DROP
    }

    object WORD {
        const val TABLE_NAME = "words"

        const val COLUMN_NAME_ID = "id"
        const val COLUMN_NAME_WORD = "word"

        const val SQL_CREATE = "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
                "$COLUMN_NAME_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_NAME_WORD TEXT UNIQUE);"

        const val SQL_DROP = "DROP TABLE IF EXISTS $TABLE_NAME;"
    }

}