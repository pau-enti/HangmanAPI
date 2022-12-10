package com.hangman.api.web

import com.hangman.api.models.Game
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

object ActiveGames : ArrayList<Game>() {
    const val GAMES_MAX_TIME_SESSION_MILLIS = 1 * 60 * 60 * 1000L // 1 hour
    const val PURGE_PERIOD = 10 * 60 * 1000L // 10 min

    private val deathTime = HashMap<Game, Long>()

    private val killer = Timer(true).apply {
        scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                // println("Running purge ${System.currentTimeMillis()}")

                ActiveGames.removeAll { game ->
                    val death = deathTime[game] ?: return@removeAll true
                    death < System.currentTimeMillis()
                }
            }
        }, 0L, PURGE_PERIOD)
    }

    override fun add(element: Game): Boolean {
        deathTime[element] = System.currentTimeMillis() + GAMES_MAX_TIME_SESSION_MILLIS
        return super.add(element)
    }

    override fun remove(element: Game): Boolean {
        deathTime.remove(element)
        return super.remove(element)
    }
}