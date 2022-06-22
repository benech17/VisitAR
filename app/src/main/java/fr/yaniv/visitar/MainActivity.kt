package fr.yaniv.visitar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import fr.yaniv.visitar.fragments.MainFragment
import fr.yaniv.visitar.ui.home.HomeFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mapButton = findViewById<Button>(R.id.buttonMap)
        mapButton.setOnClickListener {
            val switchActivityIntent = Intent(this, MapActivity::class.java)
            startActivity(switchActivityIntent)
        }

        //injecter le fragment dans notre boite
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, MainFragment())
        transaction.addToBackStack(null)
        transaction.commit()
    }
}