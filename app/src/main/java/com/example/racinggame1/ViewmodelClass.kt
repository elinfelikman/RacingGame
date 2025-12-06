package com.example.racinggame1

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData


class GameViewModel : ViewModel() {

    val currentScore = MutableLiveData<Int>(0)


    private val totalLanes = 3



    val player = Player(1,3)


    val playerLane = MutableLiveData<Int>()

    fun startGame() {
        currentScore.value = 0

        player.lane = 1
        playerLane.value = player.lane
    }


    fun movePlayer(direction: String) {
        if (direction == "LEFT" && player.lane > 0) {

            player.lane--
        } else if (direction == "RIGHT" && player.lane < totalLanes - 1) {

            player.lane++
        }

        playerLane.value = player.lane
    }
}