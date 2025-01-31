package com.ifmg.termix

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ifmg.termix.databinding.ActivityTimeLimitMinigameBinding

class TimeLimitMinigame : AppCompatActivity() {

    private lateinit var tileLimiteMinigameBinding: ActivityTimeLimitMinigameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inflar os componentes da interface
        tileLimiteMinigameBinding = ActivityTimeLimitMinigameBinding.inflate(layoutInflater)
        setContentView(tileLimiteMinigameBinding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        registerButtonEvents()
    }

    // Configurar todos os eventos de botão
    private fun registerButtonEvents(){

        // Voltar à tela de minijogos
        tileLimiteMinigameBinding.backToHomeBtn.setOnClickListener {
            val intent = Intent(this, MinigamesHome::class.java)
            startActivity(intent)
        }

        // Mover para a activity do perfil do jogador
        tileLimiteMinigameBinding.profileBtn.setOnClickListener {
            val intent = Intent(this, Profile::class.java)
            startActivity(intent)
        }
    }
}