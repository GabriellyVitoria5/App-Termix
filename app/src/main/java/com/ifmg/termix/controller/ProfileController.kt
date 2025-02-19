package com.ifmg.termix.controller

import android.content.Context
import com.ifmg.termix.model.Profile
import com.ifmg.termix.repository.ProfileRepository

class ProfileController(context: Context) {

    lateinit var profileRepository: ProfileRepository

    init {
        profileRepository = ProfileRepository(context)
    }

    fun createProfile(playerName: String, profileImage: ByteArray?) {
        val profile = Profile(playerName = playerName, profileImage = profileImage)
        profileRepository.insertProfile(profile)
    }

    fun getProfile(id: Int): Profile? {
        return profileRepository.getProfile(id)
    }




}