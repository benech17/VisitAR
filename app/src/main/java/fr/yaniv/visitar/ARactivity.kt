package fr.yaniv.visitar

import android.os.Bundle
import android.speech.tts.TextToSpeech
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import fr.yaniv.visitar.databinding.ActivityAractivityBinding
import java.util.*
import android.widget.Button
import android.widget.EditText
import android.util.Log
import android.widget.TextView

class ARactivity : AppCompatActivity(),TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    private var btnSpeak: Button? = null
    private var btnNoSpeak: Button?= null
    private var etSpeak: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityAractivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_main)

        // view binding button and edit text
        btnSpeak = findViewById(R.id.btn_speak)
        etSpeak = findViewById(R.id.description_home)

        btnSpeak!!.isEnabled = false

        // TextToSpeech(Context: this, OnInitListener: this)
        tts = TextToSpeech(this, this)

        btnSpeak!!.setOnClickListener { speakOut() }

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_aractivity)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_camera, R.id.navigation_notifications, R.id.navigation_information,R.id.navigation_histoire
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts!!.setLanguage(Locale.FRENCH)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS","The Language not supported!")
            } else {
                btnSpeak!!.isEnabled = true
            }
        }
    }
    private fun speakOut() {
        val text = etSpeak!!.text.toString()
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null,"")
    }

    public override fun onDestroy() {
        // Shutdown TTS when
        // activity is destroyed
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }

    private lateinit var binding: ActivityAractivityBinding

}