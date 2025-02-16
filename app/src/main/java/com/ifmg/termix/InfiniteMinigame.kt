package com.ifmg.termix

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ifmg.termix.controller.GameController
import com.ifmg.termix.databinding.ActivityInfiniteMinigameBinding


class InfiniteMinigame : AppCompatActivity() {

    private lateinit var infiniteMinigameBinding: ActivityInfiniteMinigameBinding

    private lateinit var gameController: GameController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        // Inflar os componentes da interface
        infiniteMinigameBinding = ActivityInfiniteMinigameBinding.inflate(layoutInflater)
        setContentView(infiniteMinigameBinding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Criar instância do controller
        gameController = GameController(this)

        // Registrar eventos
        registerButtonEvents(gameController)
    }

    // Configurar todos os eventos de botão
    private fun registerButtonEvents(gameController: GameController){

        // Voltar à tela de minijogos
        infiniteMinigameBinding.backToHomeInfiniteBtn.setOnClickListener {
            val intent = Intent(this, MinigamesHome::class.java)
            startActivity(intent)
        }

        // Mover para a activity do perfil do jogador
        infiniteMinigameBinding.profileInfiniteBtn.setOnClickListener {
            val intent = Intent(this, Profile::class.java)
            startActivity(intent)
        }

        // Mostrar regras do jogo em um pop up customizado
        infiniteMinigameBinding.ruleInfiniteBtn.setOnClickListener {
            gameController.showPopup(R.layout.infinite_rules)
        }

    }
}