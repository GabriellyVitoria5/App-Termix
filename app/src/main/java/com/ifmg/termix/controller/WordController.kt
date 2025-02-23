package com.ifmg.termix.controller

import android.content.Context
import com.ifmg.termix.model.Word
import com.ifmg.termix.repository.WordRepository

class WordController(var context: Context) {

    lateinit var wordRepository: WordRepository

    init {
        wordRepository = WordRepository(context)
    }

    // Buscar palavras do arquivo local armazenada em assets
    private fun getWordsFromFile(): List<String> {
        val wordsList = mutableListOf<String>()
        try {
            context.assets.open("WordsDatabase").bufferedReader().useLines { lines ->
                wordsList.addAll(lines)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return wordsList
    }

    // Pegar todas palavras do arquivo local
    fun loadWordsFromFile() {
        val words = getWordsFromFile()
        for (word in words) {
            addWord(word)
        }
    }

    // Inserir palavras do jogo no banco
    fun addWord(word: String) {
        wordRepository.insertWord(Word(word = word))
    }

    // Carregar todas as palavras no banco de dados quando baixar a aplicação. As próximas vezes que o usuário abrir o app, não será carregado novamente
    fun loadWordsIfFirstRun() {
        val sharedPreferences = context.getSharedPreferences("TermixPrefs", Context.MODE_PRIVATE)
        val isFirstRun = sharedPreferences.getBoolean("firstRun", true)

        // Atualizar o SharedPreferences para não carregar os dados novamente no banco
        if (isFirstRun) {
            loadWordsFromFile()
            sharedPreferences.edit().putBoolean("firstRun", false).apply()
        }
    }

}