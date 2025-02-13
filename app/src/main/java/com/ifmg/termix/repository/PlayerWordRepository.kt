package com.ifmg.termix.repository

import android.content.ContentValues
import android.content.Context
import com.ifmg.termix.localdata.DatabaseContract
import com.ifmg.termix.localdata.DatabaseSQLite
import com.ifmg.termix.model.PlayerWords

class PlayerWordRepository(context: Context) {

    private val database: DatabaseSQLite = DatabaseSQLite(context)

    // Inserir palavra jogada no banco de dados
    fun insertPlayerWord(playerWord: PlayerWords): Long {
        val db = database.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseContract.PLAYER_WORDS.COLUMN_NAME_WORD, playerWord.word)
            put(DatabaseContract.PLAYER_WORDS.COLUMN_NAME_ATTEMPT, playerWord.attempt)
        }

        return db.insert(DatabaseContract.PLAYER_WORDS.TABLE_NAME, null, values)
    }

    // Buscar todas as palavras inseridas pelo jogador
    fun getAllPlayerWords(): List<PlayerWords> {
        val db = database.readableDatabase
        val wordsList = mutableListOf<PlayerWords>()
        val cursor = db.query(
            DatabaseContract.PLAYER_WORDS.TABLE_NAME,
            null, null, null, null, null, null
        )

        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.PLAYER_WORDS.COLUMN_NAME_ID))
                val word = getString(getColumnIndexOrThrow(DatabaseContract.PLAYER_WORDS.COLUMN_NAME_WORD))
                val attempt = getInt(getColumnIndexOrThrow(DatabaseContract.PLAYER_WORDS.COLUMN_NAME_ATTEMPT))
                wordsList.add(PlayerWords(id, word, attempt))
            }
            close()
        }
        return wordsList
    }

    // Deletar todas as palavras inseridas pelo jogador (caso o jogo seja resetado)
    fun clearPlayerWords() {
        val db = database.writableDatabase
        db.delete(DatabaseContract.PLAYER_WORDS.TABLE_NAME, null, null)
    }
}