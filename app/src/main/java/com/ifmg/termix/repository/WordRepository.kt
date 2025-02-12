package com.ifmg.termix.repository

import android.content.ContentValues
import android.content.Context
import com.ifmg.termix.localdata.DatabaseContract
import com.ifmg.termix.localdata.DatabaseSQLite
import com.ifmg.termix.model.Word

class WordRepository(context: Context) {

    lateinit var database: DatabaseSQLite

    init {
        database = DatabaseSQLite(context)
    }

    // Inserir palavra no banco de dados
    // As palavras usadas no jogo estão em um arquivo local, ideal criar um servidor para armazená-las no futuro
    fun insertWord(word: Word): Long {
        val dataBaseEdit = database.writableDatabase
        val valuesWord:ContentValues = ContentValues()
        valuesWord.put(DatabaseContract.WORD.COLUMN_NAME_WORD,word.word)

        val idFood = dataBaseEdit.insert(
            DatabaseContract.WORD.TABLE_NAME,
            null,
            valuesWord)

        return idFood
    }

    // Buscar todas as palavras no bano
    fun getAllWords(): List<Word> {
        val db = database.readableDatabase
        val wordsList = mutableListOf<Word>()
        val cursor = db.query(DatabaseContract.WORD.TABLE_NAME, null, null, null, null, null, null)

        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(DatabaseContract.WORD.COLUMN_NAME_ID))
                val word = getString(getColumnIndexOrThrow(DatabaseContract.WORD.COLUMN_NAME_WORD))
                wordsList.add(Word(id, word))
            }
            close()
        }
        return wordsList
    }

    fun getRandomWord(): String? {
        val db = database.readableDatabase
        val query = "SELECT ${DatabaseContract.WORD.COLUMN_NAME_WORD} FROM ${DatabaseContract.WORD.TABLE_NAME} ORDER BY RANDOM() LIMIT 1"

        val cursor = db.rawQuery(query, null)
        var word: String? = null

        if (cursor.moveToFirst()) {
            word = cursor.getString(0)
        }
        cursor.close()
        db.close()

        return word
    }

}