package pl.wsei.pam.lab04

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import pl.wsei.pam.lab01.R

import android.os.Handler
import android.view.View
import android.widget.ImageButton


class BoardActivity : AppCompatActivity() {
    private lateinit var handler: Handler
    private val icons = Array(4) { IntArray(4) }
    private var pairCounter: Int = 0 // Licznik par
    private var clickedTiles: MutableList<Int> = mutableListOf() // Lista klikniętych kafelków
    private var clickedButtons: MutableList<ImageButton> = mutableListOf() // Lista klikniętych przycisków

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board)

        handler = Handler(mainLooper)
        fillBoardRandomly()
    }

    fun clickButton(view: View) {
        val v = view as ImageButton
        val tag = v.tag.toString()
        val (row, col) = tag.split(" ").map { it.toInt() }
        v.setImageResource(icons[row][col])
        clickedTiles.add(icons[row][col])
        clickedButtons.add(v)

        handler.postDelayed({
            if (clickedTiles.size == 2) {
                if (clickedTiles[0] == clickedTiles[1]) {
                    pairCounter++
                    clickedButtons.forEach { btn ->
                        btn.isEnabled = false
                    }
                } else {
                    clickedTiles.clear()
                    clickedButtons.forEach { btn ->
                        btn.setImageResource(R.drawable.deck)
                    }
                }
                clickedTiles.clear()
                clickedButtons.clear()
            }
            if (pairCounter == 8) {
//                if (pairCounter == totalPairs)
                finishActivityAndReturnResult(8)
//                finishActivityAndReturnResult(Activity.RESULT_OK)
            }
        }, 1000)
    }

    private fun finishActivityAndReturnResult(resultCode: Int) {
        val returnIntent = Intent()
        setResult(resultCode, returnIntent)
        finish()
    }


    private fun fillBoardRandomly() {
        val tempIcons = listOf(
            R.drawable.fig_1, R.drawable.fig_2, R.drawable.fig_3, R.drawable.fig_4,
            R.drawable.fig_5, R.drawable.fig_6, R.drawable.fig_7, R.drawable.fig_8,
            R.drawable.fig_1, R.drawable.fig_2, R.drawable.fig_3, R.drawable.fig_4,
            R.drawable.fig_5, R.drawable.fig_6, R.drawable.fig_7, R.drawable.fig_8
        ).shuffled()

        tempIcons.forEachIndexed { index, icon ->
            icons[index / 4][index % 4] = icon
        }
    }

    // Inne metody
}
