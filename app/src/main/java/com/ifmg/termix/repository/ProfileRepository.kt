package com.ifmg.termix.repository

import android.content.ContentValues
import android.content.Context
import com.ifmg.termix.localdata.DatabaseContract
import com.ifmg.termix.localdata.DatabaseSQLite
import com.ifmg.termix.model.Profile

class ProfileRepository(context: Context) {

    lateinit var database: DatabaseSQLite

    init {
        database = DatabaseSQLite(context)
    }

    // Criar perfil do jogador
    fun insertProfile(profile: Profile): Long {
        val db = database.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseContract.PROFILE.COLUMN_NAME_PLAYER_NAME, "Jogador")
            put(DatabaseContract.PROFILE.COLUMN_NAME_PROFILE_IMAGE, null as ByteArray?)
            put(DatabaseContract.PROFILE.COLUMN_NAME_VICTORIES, 0)
            put(DatabaseContract.PROFILE.COLUMN_NAME_LOSSES, 0)
            put(DatabaseContract.PROFILE.COLUMN_NAME_TOTAL_GAMES_PLAYED, 0)
        }
        return db.insert(DatabaseContract.PROFILE.TABLE_NAME, null, values)
    }

    // Pegar informações do perfil de um jogador
    fun getProfile(id: Int): Profile? {
        val db = database.writableDatabase
        val cursor = db.query(
            DatabaseContract.PROFILE.TABLE_NAME, null,
            "${DatabaseContract.PROFILE.COLUMN_NAME_ID} = ?", arrayOf(id.toString()),
            null, null, null
        )
        return if (cursor.moveToFirst()) {
            Profile(
                cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.PROFILE.COLUMN_NAME_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.PROFILE.COLUMN_NAME_PLAYER_NAME)),
                cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseContract.PROFILE.COLUMN_NAME_PROFILE_IMAGE)),
                cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.PROFILE.COLUMN_NAME_VICTORIES)),
                cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.PROFILE.COLUMN_NAME_LOSSES)),
                cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.PROFILE.COLUMN_NAME_TOTAL_GAMES_PLAYED))
            )
        } else null
    }

    // Atualizar dados do perfil
    fun updateGameStats(victory: Boolean) {
        val db = database.writableDatabase
        val sql = if (victory) {
            "UPDATE ${DatabaseContract.PROFILE.TABLE_NAME} SET " +
                    "${DatabaseContract.PROFILE.COLUMN_NAME_VICTORIES} = ${DatabaseContract.PROFILE.COLUMN_NAME_VICTORIES} + 1, " +
                    "${DatabaseContract.PROFILE.COLUMN_NAME_TOTAL_GAMES_PLAYED} = ${DatabaseContract.PROFILE.COLUMN_NAME_TOTAL_GAMES_PLAYED} + 1"
        } else {
            "UPDATE ${DatabaseContract.PROFILE.TABLE_NAME} SET " +
                    "${DatabaseContract.PROFILE.COLUMN_NAME_LOSSES} = ${DatabaseContract.PROFILE.COLUMN_NAME_LOSSES} + 1, " +
                    "${DatabaseContract.PROFILE.COLUMN_NAME_TOTAL_GAMES_PLAYED} = ${DatabaseContract.PROFILE.COLUMN_NAME_TOTAL_GAMES_PLAYED} + 1"
        }
        db.execSQL(sql)
    }
}