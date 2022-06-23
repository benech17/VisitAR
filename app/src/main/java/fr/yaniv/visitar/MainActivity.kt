package fr.yaniv.visitar

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import fr.yaniv.visitar.fragments.MainFragment
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mapButton = findViewById<Button>(R.id.buttonMap)

        val itinerary = "itinerary_1"
        val backupDBPath = this.filesDir.path + "/VisitAR-db-backup"
        dirChecker(backupDBPath)

        if (!File("$backupDBPath/$itinerary/carte_projet_pmr.geojson").exists()) {
            mapButton.isEnabled = false
        }

        mapButton.setOnClickListener {
            val switchActivityIntent = Intent(this, MapActivity::class.java)
            switchActivityIntent.putExtra("path","$backupDBPath/$itinerary")
            startActivity(switchActivityIntent)
        }

        //injecter le fragment dans notre boite
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, MainFragment(this))
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun dirChecker(dir: String) {
        val f = File(dir)
        if (!f.isDirectory) {
            f.mkdirs()
        }
    }
}