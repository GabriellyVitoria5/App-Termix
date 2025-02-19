package com.ifmg.termix.model

data class GameSession(
    val id: Long = 0,
    val gameDate: String,
    val mode: String,
    val word: String,
    val attempt: Int,
    val status: String = "nao_iniciada"
)
