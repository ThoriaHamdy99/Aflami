package com.example.ui.screens.home.model

import androidx.annotation.DrawableRes
import com.example.ui.R

enum class CardMood(@DrawableRes val iconResourceId: Int) {
    SAD(R.drawable.ic_mood_sad),
    THINKING(R.drawable.ic_mood_lookup),
    IN_LOVE(R.drawable.ic_mood_inlove),
    ANGRY(R.drawable.ic_mood_angry),
    UN_HAPPY(R.drawable.ic_mood_unhappy),
    CONFUSED(R.drawable.ic_mood_saddizzy);

    companion object {
        fun getModeByName(moodName: String): CardMood? {
            entries.forEach { mood ->
                if (mood.name == moodName) return mood
            }
            return null
        }
    }
}