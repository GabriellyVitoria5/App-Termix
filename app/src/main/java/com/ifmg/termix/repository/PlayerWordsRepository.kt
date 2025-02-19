package com.ifmg.termix.repository

import android.content.ContentValues
import android.content.Context
import com.ifmg.termix.localdata.DatabaseContract
import com.ifmg.termix.localdata.DatabaseSQLite
import com.ifmg.termix.model.PlayerWords

class PlayerWordsRepository(context: Context) {

    private val database: DatabaseSQLite = DatabaseSQLite(context)

    // Inserir palavra jogada no banco de dados
    fun insertPlayerWord(playerWord: PlayerWords): Long {
        val db = database.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseContract.PLAYER_WORDS.COLUMN_NAME_GAME_ID, playerWord.gameId)
            put(DatabaseContract.PLAYER_WORDS.COLUMN_NAME_WORD, playerWord.word)
            put(DatabaseContract.PLAYER_WORDS.COLUMN_NAME_ATTEMPT, playerWord.attempt)
            put(DatabaseContract.PLAYER_WORDS.COLUMN_NAME_GAME_MODE, playerWord.gameMode)
        }

        return db.insert(DatabaseContract.PLAYER_WORDS.TABLE_NAME, null, values)
    }

    // Buscar todas as palavras inseridas pelo jogador em uma sessão de jogo específica
    fun getPlayerWordsByGameId(gameId: Int): List<PlayerWords> {
        val db = database.readableDatabase
        val wordsList = mutableListOf<PlayerWords>()
        val cursor = db.query(
            DatabaseContract.PLAYER_WORDS.TABLE_NAME,
            null,
            "${DatabaseContract.PLAYER_WORDS.COLUMN_NAME_GAME_ID} = ?",
            arrayOf(gameId.toString()),
            null, null, null
        )

        with(cursor) {
            while (moveToNext()) {
                wordsList.add(
                    PlayerWords(
                        id = getInt(getColumnIndexOrThrow(DatabaseContract.PLAYER_WORDS.COLUMN_NAME_ID)),
                        gameId = getInt(getColumnIndexOrThrow(DatabaseContract.PLAYER_WORDS.COLUMN_NAME_GAME_ID)),
                        word = getString(getColumnIndexOrThrow(DatabaseContract.PLAYER_WORDS.COLUMN_NAME_WORD)),
                        attempt = getInt(getColumnIndexOrThrow(DatabaseContract.PLAYER_WORDS.COLUMN_NAME_ATTEMPT)),
                        gameMode = getString(getColumnIndexOrThrow(DatabaseContract.PLAYER_WORDS.COLUMN_NAME_GAME_MODE))
                    )
                )
            }
            close()
        }
        return wordsList
    }

    fun getPlayerWordsForGameSession(gameSessionId: Long): List<PlayerWords> {
        val db = database.readableDatabase
        val playerWords = mutableListOf<PlayerWords>()
        val cursor = db.query(
            DatabaseContract.PLAYER_WORDS.TABLE_NAME,
            null,
            "game_id = ?",
            arrayOf(gameSessionId.toString()),
            null, null, null
        )

        // Adiciona as palavras encontradas à lista
        with(cursor) {
            while (moveToNext()) {
                playerWords.add(
                    PlayerWords(
                        id = getInt(getColumnIndexOrThrow(DatabaseContract.PLAYER_WORDS.COLUMN_NAME_ID)),
                        gameId = getInt(getColumnIndexOrThrow(DatabaseContract.PLAYER_WORDS.COLUMN_NAME_GAME_ID)),
                        word = getString(getColumnIndexOrThrow(DatabaseContract.PLAYER_WORDS.COLUMN_NAME_WORD)),
                        attempt = getInt(getColumnIndexOrThrow(DatabaseContract.PLAYER_WORDS.COLUMN_NAME_ATTEMPT)),
                        gameMode = getString(getColumnIndexOrThrow(DatabaseContract.PLAYER_WORDS.COLUMN_NAME_GAME_MODE))
                    )
                )
            }
            close()
        }

        return playerWords
    }

    // Retornar quantas tentativas o usuário usou para tentar descobrir a palavra
    fun getAttemptCount(gameId: Int): Int {
        val db = database.readableDatabase
        val cursor = db.rawQuery(
            "SELECT COUNT(*) FROM ${DatabaseContract.PLAYER_WORDS.TABLE_NAME} WHERE ${DatabaseContract.PLAYER_WORDS.COLUMN_NAME_GAME_ID} = ?",
            arrayOf(gameId.toString())
        )

        return cursor.use {
            if (it.moveToFirst()) it.getInt(0) else 0
        }
    }

}