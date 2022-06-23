package fr.yaniv.visitar

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File

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
    }

    private fun dirChecker(dir: String) {
        val f = File(dir)
        if (!f.isDirectory) {
            f.mkdirs()
        }
    }
}