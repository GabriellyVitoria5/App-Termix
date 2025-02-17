package com.ifmg.termix.localdata

class DatabaseContract {

    // Definir o banco de dados
    companion object {
        const val DATABASE_NAME: String = "termix.db"
        const val DATABASE_VERSION: Int = 1

        const val SQL_CREATE_TABLES = "${WORD.SQL_CREATE}, ${PLAYER_WORDS.SQL_CREATE}, ${PROFILE.SQL_CREATE}, ${CALENDAR.SQL_CREATE}"
        const val SQL_DROP_TABLES = "${WORD.SQL_DROP} ${PLAYER_WORDS.SQL_DROP} ${PROFILE.SQL_DROP} ${CALENDAR.SQL_DROP}"
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
        const val COLUMN_NAME_GAME_ID = "game_id" // Identificar uma partida
        const val COLUMN_NAME_WORD = "word"
        const val COLUMN_NAME_ATTEMPT = "attempt"
        const val COLUMN_NAME_GAME_MODE = "game_mode"

        const val SQL_CREATE = "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
                "$COLUMN_NAME_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_NAME_GAME_ID INTEGER NOT NULL, " +
                "$COLUMN_NAME_WORD TEXT NOT NULL, " +
                "$COLUMN_NAME_ATTEMPT INTEGER NOT NULL, " +
                "$COLUMN_NAME_GAME_MODE TEXT NOT NULL, " +
                "FOREIGN KEY($COLUMN_NAME_GAME_ID) REFERENCES ${GAME_SESSION.TABLE_NAME}(${GAME_SESSION.COLUMN_NAME_ID}) ON DELETE CASCADE);"

        const val SQL_DROP = "DROP TABLE IF EXISTS $TABLE_NAME;"
    }

    // Definir a estrutura da tabela que guarda os dados de uma partida
    object GAME_SESSION {
        const val TABLE_NAME = "game_session"

        const val COLUMN_NAME_ID = "id"
        const val COLUMN_NAME_GAME_DATE = "game_date"
        const val COLUMN_NAME_MODE = "mode"
        const val COLUMN_NAME_WORD = "word" // Palavra sorteada para o jogo
        const val COLUMN_NAME_ATTEMPT = "attempt"
        const val COLUMN_NAME_STATUS = "status"

        const val SQL_CREATE = "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
                "$COLUMN_NAME_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_NAME_GAME_DATE TEXT NOT NULL, " +
                "$COLUMN_NAME_MODE TEXT NOT NULL, " +
                "$COLUMN_NAME_WORD TEXT NOT NULL, " +
                "$COLUMN_NAME_ATTEMPT INTEGER NOT NULL, " +
                "$COLUMN_NAME_STATUS TEXT NOT NULL DEFAULT 'nao_iniciada');"

        const val SQL_DROP = "DROP TABLE IF EXISTS $TABLE_NAME;"
    }


    // Definir a estrutura da tabela de perfis de jogadores
    object PROFILE {
        const val TABLE_NAME = "profile"

        const val COLUMN_NAME_ID = "id"
        const val COLUMN_NAME_PLAYER_NAME = "player_name"
        const val COLUMN_NAME_PROFILE_IMAGE = "profile_image"
        const val COLUMN_NAME_VICTORIES = "victories"
        const val COLUMN_NAME_LOSSES = "losses"
        const val COLUMN_NAME_TOTAL_GAMES_PLAYED = "total_games_played"

        const val SQL_CREATE = "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
                "$COLUMN_NAME_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_NAME_PLAYER_NAME TEXT NOT NULL, " +
                "$COLUMN_NAME_PROFILE_IMAGE BLOB, " +
                "$COLUMN_NAME_VICTORIES INTEGER DEFAULT 0, " +
                "$COLUMN_NAME_LOSSES INTEGER DEFAULT 0, " +
                "$COLUMN_NAME_TOTAL_GAMES_PLAYED INTEGER DEFAULT 0);"

        const val SQL_DROP = "DROP TABLE IF EXISTS $TABLE_NAME;"
    }

    // Definir a estrutura da tabela de calendário (jogos diários)
    object CALENDAR {
        const val TABLE_NAME = "calendar"

        const val COLUMN_NAME_ID = "id"
        const val COLUMN_NAME_DATE = "date"
        const val COLUMN_NAME_GAME_STATUS = "game_status"

        const val SQL_CREATE = "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
                "$COLUMN_NAME_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_NAME_DATE TEXT NOT NULL, " + // Vitória, derrota ou dia não jogado
                "$COLUMN_NAME_GAME_STATUS TEXT NOT NULL);"

        const val SQL_DROP = "DROP TABLE IF EXISTS $TABLE_NAME;"
    }

}