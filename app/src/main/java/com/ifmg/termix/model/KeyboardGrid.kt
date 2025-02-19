package com.ifmg.termix.model

import android.widget.Button

class KeyboardGrid {

    // Letras do teclado
    val letters = listOf(
        "QWERTYUIOP",
        "ASDFGHJKLÇ",
        "ZXCVBNM"
    )

    val letterButtonsMap = mutableMapOf<String, Button>()
    val correctLetters = mutableSetOf<String>() // Letras que já foram confirmadas como corretas (verde)
    var enterButton: Button? = null
}