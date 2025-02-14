package com.ifmg.termix.controller

import android.content.Context
import android.widget.Toast
import com.ifmg.termix.model.PlayerWords
import com.ifmg.termix.repository.PlayerWordRepository
import com.ifmg.termix.repository.WordRepository
import kotlin.random.Random

// Controlar fluxo e ações do jogo principal e dos minigames
class GameController(var context: Context) {

    private val wordRepository = WordRepository(context)
    private val playerWordRepository = PlayerWordRepository(context)

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

    // Salvar a palavra digitada pelo usuário no banco
    fun savePlayerWord(word: String, attempt: Int) {
        val resultado = playerWordRepository.insertPlayerWord(PlayerWords(0, word, attempt))
        Toast.makeText(context, resultado.toString(), Toast.LENGTH_LONG).show()
    }

    // Limpar palavras do usuário ao iniciar um novo jogo
    fun resetPlayerWords() {
        playerWordRepository.clearPlayerWords()
    }

    fun getAllPlayersWord(): List<PlayerWords>{
        return playerWordRepository.getAllPlayerWords()
    }

}