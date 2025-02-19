package com.ifmg.termix.model

data class PlayerWords(
    val id: Int = 0,
    val gameId: Int, // ID de uma partida/sessão do jogo
    val word: String,
    val attempt: Int,
    val gameMode: String
)
