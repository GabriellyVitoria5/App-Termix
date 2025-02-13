package com.ifmg.termix.controller

import android.content.Context
import com.ifmg.termix.repository.WordRepository
import kotlin.random.Random

class GameController(var context: Context) {

    private val wordRepository = WordRepository(context)

    // Sortear uma palavra aleatória do banco
    fun getRandomWord(): String{
        val words = wordRepository.getAllWords()
        if (words.isNotEmpty()) {
            return words[Random.nextInt(words.size)].word.uppercase()
        }
        return ""
    }

    // Validar se a palavra sorteada tem o tamanho correto (e foi encontrada no banco corretamente)
    fun validateWord(chosenWord: String): Boolean{
        return chosenWord.length == 5
    }

    // Verificar se a palavra informada pelo usuário está no banco de palavras do jogo
    fun isWordInLocalDatabase(userWord: String): Boolean {
        val words = wordRepository.getAllWords().map { it.word.uppercase() }
        return userWord.uppercase() in words
    }


}