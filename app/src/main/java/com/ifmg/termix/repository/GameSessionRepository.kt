package com.ifmg.termix.repository

import android.content.ContentValues
import android.content.Context
import com.ifmg.termix.localdata.DatabaseContract
import com.ifmg.termix.localdata.DatabaseSQLite
import com.ifmg.termix.model.GameSession

class GameSessionRepository(context: Context) {

    private val database: DatabaseSQLite = DatabaseSQLite(context)

    // Inserir uma nova partida no banco
    fun insertGameSession(gameSession: GameSession): Long {
        val db = database.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseContract.GAME_SESSION.COLUMN_NAME_GAME_DATE, gameSession.gameDate)
            put(DatabaseContract.GAME_SESSION.COLUMN_NAME_MODE, gameSession.mode)
            put(DatabaseContract.GAME_SESSION.COLUMN_NAME_WORD, gameSession.word)
            put(DatabaseContract.GAME_SESSION.COLUMN_NAME_ATTEMPT, gameSession.attempt)
            put(DatabaseContract.GAME_SESSION.COLUMN_NAME_STATUS, gameSession.status)
        }

        return db.insert(DatabaseContract.GAME_SESSION.TABLE_NAME, null, values)
    }

    // Buscar uma partida pelo ID
    fun getGameSessionById(id: Int): GameSession? {
        val db = database.readableDatabase
        val cursor = db.query(
            DatabaseContract.GAME_SESSION.TABLE_NAME,
            null,
            "${DatabaseContract.GAME_SESSION.COLUMN_NAME_ID} = ?",
            arrayOf(id.toString()),
            null, null, null
        )

        return cursor.use {
            if (it.moveToFirst()) {
                GameSession(
                    id = it.getLong(it.getColumnIndexOrThrow(DatabaseContract.GAME_SESSION.COLUMN_NAME_ID)),
                    gameDate = it.getString(it.getColumnIndexOrThrow(DatabaseContract.GAME_SESSION.COLUMN_NAME_GAME_DATE)),
                    mode = it.getString(it.getColumnIndexOrThrow(DatabaseContract.GAME_SESSION.COLUMN_NAME_MODE)),
                    word = it.getString(it.getColumnIndexOrThrow(DatabaseContract.GAME_SESSION.COLUMN_NAME_WORD)),
                    attempt = it.getInt(it.getColumnIndexOrThrow(DatabaseContract.GAME_SESSION.COLUMN_NAME_ATTEMPT)),
                    status = it.getString(it.getColumnIndexOrThrow(DatabaseContract.GAME_SESSION.COLUMN_NAME_STATUS))
                )
            } else null
        }
    }

    // Verificar se já existe uma partida em andamento
    fun isGameInProgress(): Boolean {
        val db = database.readableDatabase
        val cursor = db.query(
            DatabaseContract.GAME_SESSION.TABLE_NAME,
            null,
            "${DatabaseContract.GAME_SESSION.COLUMN_NAME_STATUS} = ?",
            arrayOf("andamento"), // Status de "andamento"
            null, null, null
        )

        val isInProgress = cursor.use { it.moveToFirst() }
        cursor.close()

        return isInProgress
    }

    // Pegar o Id da partida em andamento
    fun getActiveGameId(mode: String): Int? {
        val db = database.readableDatabase
        val cursor = db.query(
            DatabaseContract.GAME_SESSION.TABLE_NAME,
            arrayOf(DatabaseContract.GAME_SESSION.COLUMN_NAME_ID),
            "${DatabaseContract.GAME_SESSION.COLUMN_NAME_MODE} = ? AND ${DatabaseContract.GAME_SESSION.COLUMN_NAME_STATUS} = ?",
            arrayOf(mode, "andamento"),
            null, null, null
        )

        return cursor.use {
            if (it.moveToFirst()) {
                it.getInt(it.getColumnIndexOrThrow(DatabaseContract.GAME_SESSION.COLUMN_NAME_ID))
            } else null
        }
    }

    // Atualizar o status de uma partida: "nao_iniciada", "andamento", "vitoria", "derrota", "terminou"
    fun updateGameStatus(id: Int, newStatus: String): Int {
        val db = database.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseContract.GAME_SESSION.COLUMN_NAME_STATUS, newStatus)
        }

        return db.update(
            DatabaseContract.GAME_SESSION.TABLE_NAME,
            values,
            "${DatabaseContract.GAME_SESSION.COLUMN_NAME_ID} = ?",
            arrayOf(id.toString())
        )
    }

    // Recuperar a palavra correta da partida ativa
    fun getCorrectWord(mode: String): String? {
        val db = database.readableDatabase
        val cursor = db.query(
            DatabaseContract.GAME_SESSION.TABLE_NAME,
            arrayOf(DatabaseContract.GAME_SESSION.COLUMN_NAME_WORD),
            "${DatabaseContract.GAME_SESSION.COLUMN_NAME_MODE} = ? AND ${DatabaseContract.GAME_SESSION.COLUMN_NAME_STATUS} = ?",
            arrayOf(mode, "andamento"),
            null, null, null
        )

        return cursor.use {
            if (it.moveToFirst()) {
                it.getString(it.getColumnIndexOrThrow(DatabaseContract.GAME_SESSION.COLUMN_NAME_WORD))
            } else {
                null
            }
        }
    }



}