package pl.wsei.pam.lab02

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import pl.wsei.pam.lab01.R
import pl.wsei.pam.lab03.Lab03Activity

class Lab02Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lab02)
    }

    private fun openGrid(size: IntArray) {
        val intent = Intent(this, Lab03Activity::class.java)
        intent.putExtra("size", size)
        startActivity(intent)
    }

    fun onClickBtn6_6(v: View) {
        Toast.makeText(this, "6 x 6", Toast.LENGTH_SHORT).show()
        openGrid(intArrayOf(6, 6))
    }

    fun onClickBtn4_4(v: View) {
        Toast.makeText(this, "4 x 4", Toast.LENGTH_SHORT).show()
        openGrid(intArrayOf(4, 4))
    }

    fun onClickBtn4_3(v: View) {
        Toast.makeText(this, "4 x 3", Toast.LENGTH_SHORT).show()
        openGrid(intArrayOf(4, 3))
    }

    fun onClickBtn3_2(v: View) {
        Toast.makeText(this, "3 x 2", Toast.LENGTH_SHORT).show()
        openGrid(intArrayOf(3, 2))
    }
}