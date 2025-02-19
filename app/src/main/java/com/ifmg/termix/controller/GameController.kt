package com.ifmg.termix.controller

import android.content.Context
import android.widget.Toast
import com.ifmg.termix.model.GameSession
import com.ifmg.termix.model.PlayerWords
import com.ifmg.termix.repository.GameSessionRepository
import com.ifmg.termix.repository.PlayerWordsRepository
import com.ifmg.termix.repository.WordRepository
import kotlin.random.Random

// Controlar fluxo e ações do jogo principal e dos minigames
class GameController(var context: Context) {

    private val wordRepository = WordRepository(context)
    private val playerWordsRepository = PlayerWordsRepository(context)
    private val gameSessionRepository = GameSessionRepository(context)

    private val activeGameSessions = mutableMapOf<String, Int>() // Maperar o modo de jogo e id da partida>

    // Criar uma nova partida de um minijogo ou jogo diário
    fun startNewGameSession(mode: String) {

        // Já existe uma partida em andamento, não criar uma partida nova
        if (gameSessionRepository.isGameInProgress()) {
            return
        }

        // Escolher e validar a palavra secreta da partida
        val chosenWord = getRandomWord()
        if (!validateWord(chosenWord)) {
            return
        }

        // Criar uma nova partida
        val newGameSession = GameSession(
            id = 0,
            gameDate = System.currentTimeMillis().toString(),
            mode = mode,
            word = chosenWord,
            attempt = 0,
            status = "andamento"
        )

        // Iniciar uma nova partida em um modo de jogo
        val gameId = gameSessionRepository.insertGameSession(newGameSession)
        if (gameId != -1L) {
            activeGameSessions[mode] = gameId.toInt()
        }
    }

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

    // Recuperar uma partida ativa de um modo específico
    fun getActiveGameId(gameMode: String): Int? {
        return gameSessionRepository.getActiveGameId(gameMode)
    }

    // Salvar a palavra digitada pelo usuário no banco e relacionar essas palavras com uma partida já iniciada
    fun savePlayerWord(gameMode: String, word: String, attempt: Int) {
        val gameId = getActiveGameId(gameMode)

        if (gameId == null) {
            Toast.makeText(context, "Nenhuma partida ativa para o modo: $gameMode!", Toast.LENGTH_SHORT).show()
            return
        }

        // Salvar palavra escrita pelo jogador em uma partida
        val playerWord = PlayerWords(
            id = 0,
            gameId = gameId,
            word = word,
            attempt = attempt,
            gameMode = gameMode
        )

        playerWordsRepository.insertPlayerWord(playerWord)
    }

    // Encerrar uma partida específica e atualizar status no banco
    fun endGameSession(gameMode: String, win: Boolean) {
        val gameId = getActiveGameId(gameMode) ?: return

        val newStatus = if (win) "vitoria" else "derrota"
        gameSessionRepository.updateGameStatus(gameId, newStatus)

        Toast.makeText(context, "Partida ($gameMode) finalizada: $newStatus", Toast.LENGTH_SHORT).show()
        activeGameSessions.remove(gameMode) // Remover a partida finalizada
    }

    // Recuperar a partida de jogo ativa para um modo de jogo específico
    fun getCurrentGameSession(mode: String): GameSession? {
        val gameId = getActiveGameId(mode)
        return if (gameId != null) {
            val gameSession = gameSessionRepository.getGameSessionById(gameId)
            gameSession?.copy(attempt = playerWordsRepository.getAttemptCount(gameId))
        } else {
            null
        }
    }

    // Recuperar a palavra digitada pelo jogador em uma linha específica
    fun getPlayerWord(gameMode: String, attempt: Int): String? {
        val gameId = getActiveGameId(gameMode) // Obter o id da partida ativa de acordo com o modo de jogo

        // Não foi possível encontrar uma partida ativa para esse modo de jogo
        if (gameId == null) {
            return null
        }

        // Buscar as palavras digitadas pelo jogador na partida em andamento encontrada
        val playerWords = playerWordsRepository.getPlayerWordsByGameId(gameId)

        // Buscar a palavra para a linha (tentativa) específica
        val playerWord = playerWords.find { it.attempt == attempt }

        return playerWord?.word
    }

    // Recuperar palavras escritas por um jogador em uma partida em andamento
    fun getPlayerWordsForGameSession(gameSessionId: Long): List<PlayerWords> {
        val resultado = playerWordsRepository.getPlayerWordsForGameSession(gameSessionId)
        Toast.makeText(context, "$resultado", Toast.LENGTH_SHORT).show()
        return playerWordsRepository.getPlayerWordsForGameSession(gameSessionId)
    }

    // Recuperar a palavra correta da partida ativa para um modo específico
    fun getCorrectWord(mode: String): String {
        return gameSessionRepository.getCorrectWord(mode)!!
    }


}