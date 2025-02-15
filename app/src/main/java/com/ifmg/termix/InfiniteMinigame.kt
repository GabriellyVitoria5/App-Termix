package com.ifmg.termix

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ifmg.termix.databinding.ActivityInfiniteMinigameBinding


class InfiniteMinigame : AppCompatActivity() {

    private lateinit var infiniteMinigameBinding: ActivityInfiniteMinigameBinding

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

        registerButtonEvents()

    }

    // Configurar todos os eventos de botão
    private fun registerButtonEvents(){

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

        // TODO: adicionar em um método intermediário e criar evento do botão nos demais jogos
        // Mostrar regras do jogo em um pop up customizado
        infiniteMinigameBinding.ruleInfiniteBtn.setOnClickListener {
            val alertCustomdialog: View = LayoutInflater.from(this@InfiniteMinigame).inflate(R.layout.infinite_rules, null)
            val alert: AlertDialog.Builder = AlertDialog.Builder(this)
            alert.setView(alertCustomdialog)

            val dialog: AlertDialog = alert.create()
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()

        }

    }
}