package com.ifmg.termix.localdata

class DatabaseContract {

    // Definir o banco de dados
    companion object {
        const val DATABASE_NAME: String = "termix.db"
        const val DATABASE_VERSION: Int = 2

        const val SQL_CREATE_TABLES = "${WORD.SQL_CREATE} ${PLAYER_WORDS.SQL_CREATE}"
        const val SQL_DROP_TABLES = "${WORD.SQL_DROP} ${PLAYER_WORDS.SQL_DROP}"
    }

    // Definir a estrutura da tabela de palavras
    object WORD {
        const val TABLE_NAME = "words"

        const val COLUMN_NAME_ID = "id"
        const val COLUMN_NAME_WORD = "word"

        const val SQL_CREATE = "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
                "$COLUMN_NAME_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_NAME_WORD TEXT UNIQUE);"

        const val SQL_DROP = "DROP TABLE IF EXISTS $TABLE_NAME;"
    }

    // Definir a estrutura da tabela para armazenar as palavras escritas pelo jogador em uma jogada
    object PLAYER_WORDS {
        const val TABLE_NAME = "player_words"

        const val COLUMN_NAME_ID = "id"
        const val COLUMN_NAME_WORD = "word"
        const val COLUMN_NAME_ATTEMPT = "attempt"

        const val SQL_CREATE = "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
                "$COLUMN_NAME_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_NAME_WORD TEXT NOT NULL, " +
                "$COLUMN_NAME_ATTEMPT INTEGER NOT NULL);"

        const val SQL_DROP = "DROP TABLE IF EXISTS $TABLE_NAME;"
    }

}