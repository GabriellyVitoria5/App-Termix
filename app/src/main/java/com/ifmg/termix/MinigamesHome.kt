package com.ifmg.termix

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ifmg.termix.databinding.ActivityMinigamesHomeBinding

class MinigamesHome : AppCompatActivity() {

    private lateinit var minigameHomeBinding: ActivityMinigamesHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inflar os componentes da interface
        minigameHomeBinding = ActivityMinigamesHomeBinding.inflate(layoutInflater)
        setContentView(minigameHomeBinding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        registerButtonEvents()

    }

    // Configurar todos os eventos de botão
    private fun registerButtonEvents(){

        // Mover para a activity do modo de jogo infinito
        minigameHomeBinding.infiniteMinigameBtn.setOnClickListener {
            val intent = Intent(this, InfiniteMinigame::class.java)
            startActivity(intent)
        }

        // Mover para a activity do modo de jogo sem limite de tempo
        minigameHomeBinding.timeMinigameBtn.setOnClickListener {
            val intent = Intent(this, TimeLimitMinigame::class.java)
            startActivity(intent)
        }

        // Mover para a activity do modo de jogo para acertar o máximo de palavras possível no tempo estabelecido
        minigameHomeBinding.termixTurboMinigameBtn.setOnClickListener {
            val intent = Intent(this, TermixTurboMinigame::class.java)
            startActivity(intent)
        }

        // Mover para a activity inicial
        minigameHomeBinding.backToHomeMinigameHomeBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Mover para a activity do perfil do jogador
        minigameHomeBinding.profileBtn.setOnClickListener {
            val intent = Intent(this, Profile::class.java)
            startActivity(intent)
        }
    }
}