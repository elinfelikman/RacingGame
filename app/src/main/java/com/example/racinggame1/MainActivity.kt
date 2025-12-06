package com.example.racinggame1

import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import kotlin.random.Random

class MainActivity : AppCompatActivity() {


    private val handler = Handler(Looper.getMainLooper())
    private lateinit var gameRunnable: Runnable

    private val obstacleSpeed = 8f
    private val frameRate = 16L
    private var timeToSpawn = 0
    private val spawnInterval = 60

    private val obstacles = mutableListOf<ImageView>()

    // מידות גלובליות
    private var carWidth = 0f

    private lateinit var viewModel: GameViewModel
    private val lanePositions = floatArrayOf(0f, 0f, 0f)

    private var currentLives = 3
    private lateinit var heartViews: List<ImageView>
    private lateinit var mainLayout: ConstraintLayout



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val playerCar: ImageView = findViewById(R.id.car)
        val buttonLeft: ImageButton = findViewById(R.id.button_left)
        val buttonRight: ImageButton = findViewById(R.id.button_right)
        mainLayout = findViewById(R.id.main)
        val heart1: ImageView = findViewById(R.id.heart_1)
        val heart2: ImageView = findViewById(R.id.heart_2)
        val heart3: ImageView = findViewById(R.id.heart_3)
        heartViews = listOf(heart3, heart2, heart1)


        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)


        buttonLeft.setOnClickListener {
            viewModel.movePlayer("LEFT")
        }
        buttonRight.setOnClickListener {
            viewModel.movePlayer("RIGHT")
        }


        playerCar.post {
            carWidth = playerCar.width.toFloat()
            val laneOffset = (mainLayout.width.toFloat() / 2f) - (carWidth / 2f)
            lanePositions[0] = -laneOffset
            lanePositions[1] = 0f
            lanePositions[2] = laneOffset

            viewModel.playerLane.observe(this) { laneIndex ->
                playerCar.translationX = lanePositions[laneIndex]
            }

            startGame() // קריאה לפונקציית האתחול
        }
    }



    private fun startGame() {

        currentLives = 3
        heartViews.forEach { it.visibility = View.VISIBLE }


        obstacles.forEach { mainLayout.removeView(it) }
        obstacles.clear()

        viewModel.startGame()
        startGameLoop()
    }

    private fun startGameLoop() {

        if (::gameRunnable.isInitialized) {
            handler.removeCallbacks(gameRunnable)
        }

        gameRunnable = object : Runnable {
            override fun run() {
                moveObstacles()
                checkCollisions()
                timeToSpawn++
                if (timeToSpawn >= spawnInterval) {
                    spawnNewObstacle()
                    timeToSpawn = 0
                }
                handler.postDelayed(this, frameRate)
            }
        }
        handler.post(gameRunnable)
    }

    private fun spawnNewObstacle() {
        val obstacleImage = ImageView(this).apply {
            layoutParams = ViewGroup.LayoutParams(
                (carWidth * 0.8).toInt(),
                (carWidth * 1.2).toInt()
            )
            setImageResource(R.drawable.coal)
            translationY = 0f
            val randomLaneIndex = Random.nextInt(0, 3)
            translationX = lanePositions[randomLaneIndex]
            tag = "obstacle"
        }
        mainLayout.addView(obstacleImage)
        obstacles.add(obstacleImage)
    }

    private fun moveObstacles() {
        val obstaclesToRemove = mutableListOf<ImageView>()
        obstacles.forEach { obstacle ->
            obstacle.translationY += obstacleSpeed
            if (obstacle.translationY > mainLayout.height) {
                obstaclesToRemove.add(obstacle)
            }
        }
        obstaclesToRemove.forEach { obstacle ->
            mainLayout.removeView(obstacle)
            obstacles.remove(obstacle)
        }
    }

    private fun checkCollisions() {
        val playerCar: ImageView = findViewById(R.id.car)
        val playerRect = Rect().apply { playerCar.getHitRect(this) }

        val obstaclesIterator = obstacles.iterator()
        while (obstaclesIterator.hasNext()) {
            val obstacle = obstaclesIterator.next()
            val obstacleRect = Rect().apply { obstacle.getHitRect(this) }

            if (Rect.intersects(playerRect, obstacleRect)) {
                loseLife(obstacle)
                break
            }
        }
    }

    private fun loseLife(hitObstacle: ImageView) {
        if (currentLives > 0) {
            currentLives--
            if (currentLives < heartViews.size) {
                heartViews[currentLives].visibility = View.INVISIBLE
            }

            mainLayout.removeView(hitObstacle)
            obstacles.remove(hitObstacle)

            if (currentLives == 0) {
                gameOver()
            }
        }
    }


    private fun gameOver() {
        handler.removeCallbacks(gameRunnable)


        val toast = Toast.makeText(this, "Try again!", Toast.LENGTH_SHORT)


        val toastView = toast.view
        val toastMessage = toastView?.findViewById<android.widget.TextView>(android.R.id.message)


        toastMessage?.let {
            it.typeface = android.graphics.Typeface.DEFAULT_BOLD
        }


        toast.show()



        handler.postDelayed({
            startGame()
        }, 100)
    }
}