package com.ifmg.termix

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.ifmg.termix.adapter.CalendarAdapter
import com.ifmg.termix.controller.ProfileController
import com.ifmg.termix.databinding.ActivityProfileBinding
import com.ifmg.termix.utils.CalendarUtils
import java.util.*

class Profile : AppCompatActivity() {

    private lateinit var profileBinding: ActivityProfileBinding

    private lateinit var profileController: ProfileController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        profileBinding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(profileBinding.root)

        profileController = ProfileController(this)

        registerButtonEvents()
        setupCalendar()
        setupStats()
    }

    private fun registerButtonEvents() {
        profileBinding.backBtn.setOnClickListener {
            finish()
        }
    }

    private fun setupCalendar() {
        val currentCalendar = Calendar.getInstance()
        val currentMonth = currentCalendar.get(Calendar.MONTH)
        val currentYear = currentCalendar.get(Calendar.YEAR)

        // Atualiza o cabeçalho do calendário
        val monthName = currentCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
        profileBinding.monthTextView.text = "$monthName $currentYear"

        // TODO: Buscar dados do banco sobre partidas jogadas e vitórias
        val days = CalendarUtils.generateMonthData(currentYear, currentMonth)

        profileBinding.calendarRecyclerView.layoutManager = GridLayoutManager(this, 7)
        profileBinding.calendarRecyclerView.adapter = CalendarAdapter(days)
    }

    private fun setupStats() {
        val profile = profileController.getProfile(1)

        profile?.let {
            profileBinding.gamesPerMode.text = it.totalGamesPlayed.toString()
            profileBinding.winStreak.text = "0" // Você pode calcular a sequência de vitórias se quiser
            profileBinding.victories.text = it.victories.toString()
            profileBinding.losses.text = it.losses.toString()
        } ?: run {
            // Se o perfil não existir, manter valores padrão
            profileBinding.gamesPerMode.text = "0"
            profileBinding.winStreak.text = "0"
            profileBinding.victories.text = "0"
            profileBinding.losses.text = "0"
        }
    }

}
