package pl.wsei.pam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import pl.wsei.pam.lab01.Lab01Activity
import pl.wsei.pam.lab01.R
import pl.wsei.pam.lab02.Lab02Activity
import pl.wsei.pam.lab06.NavigationActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
    }
    fun onClickMainBtnRunLab01(v: View){
        Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, Lab01Activity::class.java)
        startActivity(intent)
    }
    fun onClickMainBtnRunLab02(v: View){
        Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, Lab02Activity::class.java)
        startActivity(intent)
    }

    fun onClickMainBtnRunNavigation(v: View){
        Toast.makeText(this, "Clicked navigation butoon", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, NavigationActivity::class.java)
        startActivity(intent)
    }

}
