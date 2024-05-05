package pl.wsei.pam.lab03

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.service.quicksettings.Tile
import android.view.Gravity
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pl.wsei.pam.lab01.R
import java.util.Random
import java.util.Stack
import java.util.Timer
import kotlin.concurrent.schedule

class Lab03Activity : AppCompatActivity() {
    private lateinit var mBoard: GridLayout

    private lateinit var mBoardModel: MemoryBoardView

    private fun animateMismatchedPair(button: ImageButton, action: Runnable) {
        val set = AnimatorSet()

        button.pivotX = button.width / 2.0f
        button.pivotY = button.height / 2.0f

        val moveLeft = ObjectAnimator.ofFloat(button, "translationX", 0f, -50f)
        val moveRight = ObjectAnimator.ofFloat(button, "translationX", -50f, 50f)
        moveLeft.duration = 100
        moveRight.duration = 100
        moveRight.repeatCount = 5
        moveRight.repeatMode = ValueAnimator.REVERSE

        set.playSequentially(moveLeft, moveRight)

        set.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                button.translationX = 0f

                action.run()
            }
        })
        set.start()
    }
    private fun animatePairedButton(button: ImageButton, action: Runnable ) {
        val set = AnimatorSet()
        val random = Random()
        button.pivotX = random.nextFloat() * 200f
        button.pivotY = random.nextFloat() * 200f

        val rotation = ObjectAnimator.ofFloat(button, "rotation", 1080f)
        val scallingX = ObjectAnimator.ofFloat(button, "scaleX", 1f, 4f)
        val scallingY = ObjectAnimator.ofFloat(button, "scaleY", 1f, 4f)
        val fade = ObjectAnimator.ofFloat(button, "alpha", 1f, 0f)
        set.startDelay = 500
        set.duration = 2000
        set.interpolator = DecelerateInterpolator()
        set.playTogether(rotation, scallingX, scallingY, fade)
        set.addListener(object: Animator.AnimatorListener {

            override fun onAnimationStart(animator: Animator) {
            }
            override fun onAnimationEnd(animator: Animator) {
                button.scaleX = 1f
                button.scaleY = 1f
                button.alpha = 0.0f
                action.run();
            }
            override fun onAnimationCancel(animator: Animator) {
            }
            override fun onAnimationRepeat(animator: Animator) {
            }
        })
        set.start()
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val gameState = mBoardModel.getState()
        outState.putIntArray("gameState", gameState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lab03)

        mBoard = findViewById(R.id.mBoardId)

        val size = intent.getIntArrayExtra("size") ?: intArrayOf(6, 6)

        mBoardModel = MemoryBoardView(mBoard, size[0], size[1])

        mBoardModel.setOnGameChangeListener { event ->
            handleGameStateChange(event)
        }

        savedInstanceState?.getIntArray("gameState")?.let { savedState ->
            mBoardModel.setState(savedState) // czy sis nie jest null, jesli nie jest,to próbuje pobrać z niego tablicę gameState
        }

        mBoard.columnCount = size[0]
        mBoard.rowCount = size[1]
    }

    private fun handleGameStateChange(event: MemoryGameEvent) {
        when (event.state) {
            GameStates.Matching -> event.tiles.forEach { it.isRevealed = true }
            GameStates.Match -> handleMatch(event)
            GameStates.NoMatch -> handleNoMatch(event)
            GameStates.Finished -> showGameFinished()
        }
    }

    private fun handleMatch(event: MemoryGameEvent) {
        event.tiles.forEach { it.isRevealed = true }
        event.tiles.forEach { tile ->
            animatePairedButton(tile.button, Runnable {
                runOnUiThread {

                }
            })
        }
    }

    private fun handleNoMatch(event: MemoryGameEvent) {
        event.tiles.forEach { it.isRevealed = true }
        event.tiles.forEach { tile ->
            animateMismatchedPair(tile.button, Runnable {
                runOnUiThread {
                    tile.isRevealed = false                  }
            })
        }
    }
    private fun showGameFinished() {
        runOnUiThread {
            Toast.makeText(this, "Game finished", Toast.LENGTH_SHORT).show()
        }
    }
    data class Tile(
        val button: ImageButton,
        val tileResource: Int,
        val deckResource: Int,
        val id: String
    ) {
        var isRevealed: Boolean = false
            set(value) {
                field = value
                button.setImageResource(if (value) tileResource else deckResource)
            }

        var isMatched: Boolean = false
    }

    enum class GameStates {
        Matching, Match, NoMatch, Finished
    }

    class MemoryGameLogic(private val maxMatches: Int) {
        private var valueFunctions: MutableList<() -> Int> = mutableListOf()
        private var matches: Int = 0

        fun process(value: () -> Int): GameStates {
            if (valueFunctions.size < 1) {
                valueFunctions.add(value)
                return GameStates.Matching
            }

            valueFunctions.add(value)
            val result = valueFunctions[0]() == valueFunctions[1]()
            matches += if (result) 1 else 0
            valueFunctions.clear()

            return when (result) {
                true -> if (matches == maxMatches) GameStates.Finished else GameStates.Match
                false -> GameStates.NoMatch
            }
        }
    }

    data class MemoryGameEvent(
        val tiles: List<Tile>,
        val state: GameStates
    )

    class MemoryBoardView(
        private val gridLayout: GridLayout,
        private val cols: Int,
        private val rows: Int
    ) {
        private val tiles: MutableMap<String, Tile> = mutableMapOf()

        private val icons: List<Int> = listOf(
            R.drawable.baseline_rocket_launch_24,
            R.drawable.fig_1,
            R.drawable.fig_2,
            R.drawable.fig_3,
            R.drawable.fig_4,
            R.drawable.fig_5,
            R.drawable.fig_6,
            R.drawable.fig_7,
            R.drawable.fig_8,
            R.drawable.fig_9,
            R.drawable.fig_10,
            R.drawable.fig_11,
            R.drawable.fig_12,
            R.drawable.fig_13,
            R.drawable.fig_14,
            R.drawable.fig_15,
            R.drawable.fig_16,
            R.drawable.fig_17,
        )

        init {
            if(tiles.isEmpty()){
                val shuffledIcons: MutableList<Int> = mutableListOf<Int>().also {
                    it.addAll(icons.subList(0, cols * rows / 2))
                    it.addAll(icons.subList(0, cols * rows / 2))
                    it.shuffle()
                }
                for (row in 0 until rows) {
                    for (col in 0 until cols) {
                        val button = ImageButton(gridLayout.context).apply {
                            layoutParams = GridLayout.LayoutParams().apply {
                                width = GridLayout.LayoutParams.WRAP_CONTENT
                                height = GridLayout.LayoutParams.WRAP_CONTENT
                                rowSpec = GridLayout.spec(row, 1f)
                                columnSpec = GridLayout.spec(col, 1f)
                                setGravity(Gravity.FILL)
                            }
                            tag = "$row,$col"
                        }
                        val resourceImage =
                            shuffledIcons.removeAt(0) // Pobiera i usuwa pierwszy element z listy
                        addTile(button, resourceImage)
                        gridLayout.addView(button)
                    }
                }
            }

        }

        private val deckResource: Int = R.drawable.deck
        private var onGameChangeStateListener: (MemoryGameEvent) -> Unit = { (e) -> }
        private val matchedPair: Stack<Tile> = Stack()
        private val logic: MemoryGameLogic = MemoryGameLogic(cols * rows / 2)

        private fun onClickTile(v: View) {
            val tile = tiles[v.tag]
            matchedPair.push(tile)
            val matchResult = logic.process {
                tile?.tileResource ?: -1
            }
            onGameChangeStateListener(MemoryGameEvent(matchedPair.toList(), matchResult))
            if (matchResult != GameStates.Matching) {
                matchedPair.clear()
            }
        }
        fun setOnGameChangeListener(listener: (event: MemoryGameEvent) -> Unit) {
            onGameChangeStateListener = listener
        }
        private fun addTile(button: ImageButton, resourceImage: Int) {
            button.setOnClickListener(::onClickTile)
            val key = button.tag.toString()
            val tile = Tile(button, resourceImage, deckResource, key)
            tiles[key] = tile
        }
        //Zapis stanu aktywności
        fun getState(): IntArray {
            return tiles.values.map { tile ->
                when {
                    tile.isMatched -> tile.id.toInt()
                    tile.isRevealed -> 0
                    else -> -1
                }
            }.toIntArray()
        }

        fun setState(state: IntArray) {
            state.forEachIndexed { index, value ->
                tiles.values.elementAtOrNull(index)?.let { tile ->
                    tile.isMatched = value > 0
                    tile.isRevealed = value >= 0
                }
            }
        }
    }
}