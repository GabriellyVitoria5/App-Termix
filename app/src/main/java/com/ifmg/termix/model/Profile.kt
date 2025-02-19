package com.ifmg.termix.model

data class Profile(
    val id: Int = 0,
    val playerName: String,
    val profileImage: ByteArray?,
    val victories: Int = 0,
    val losses: Int = 0,
    val totalGamesPlayed: Int = 0
)