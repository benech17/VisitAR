package fr.yaniv.visitar

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import fr.yaniv.visitar.data.WaypointData
import fr.yaniv.visitar.databinding.ActivityAractivityBinding
import fr.yaniv.visitar.ui.modules.CameraModule
import java.io.File
import java.util.*


class ARactivity : AppCompatActivity(),TextToSpeech.OnInitListener {
    private lateinit var path: String
    private var pointID: Int = 0
    private var tts: TextToSpeech? = null
    private var btnSpeak: Button? = null
    private var btnNoSpeak: Button? = null
    private var etSpeak: TextView? = null
    private var textDescription: TextView? = null
    var camera: CameraModule? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityAractivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_main)

        path = intent.extras?.getString("path")!!
        pointID = intent.extras?.getInt("id")!!
        val gson = Gson()
        val file = File("$path/waypoint_$pointID")
        val fileText = file.bufferedReader().use{ it.readText() }
        val waypoint = gson.fromJson(fileText,WaypointData::class.java)

        // view binding button and edit text
        btnSpeak = findViewById(R.id.btn_speak)
        etSpeak = findViewById(R.id.description_home)
        btnNoSpeak = findViewById(R.id.btn_no_speak)
        //textDescription = findViewById(R.id.description_home)
        val textDescription = findViewById<View>(R.id.description_home) as TextView
        textDescription.setMovementMethod(ScrollingMovementMethod());


        btnSpeak!!.isEnabled = false

        // TextToSpeech(Context: this, OnInitListener: this)
        tts = TextToSpeech(this, this)

        btnSpeak!!.setOnClickListener { speakOut() }
        btnNoSpeak!!.setOnClickListener {
            if (tts!!.isSpeaking) {
                tts!!.stop()
            } else {
                Toast.makeText(this, "Mode Vocale désactivé", Toast.LENGTH_SHORT).show()
            }
        }

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_aractivity)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_camera,
                //R.id.navigation_notifications,
                R.id.navigation_information,
                R.id.navigation_histoire
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        // --- Partie Caméra ---
        // On commence par demander les permissions si ce n'est déjà fait
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_DENIED
        )
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 100);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_DENIED
        )
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                100
            );
        // Initialisation & Démarrage de la caméra
        val cameraPreviewLayout = findViewById<FrameLayout>(R.id.camera_preview)
        camera = CameraModule(this, cameraPreviewLayout)
        camera!!.startCamera()

    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts!!.setLanguage(Locale.FRENCH)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The Language not supported!")
            } else {
                btnSpeak!!.isEnabled = true
            }
        }
    }

    private fun speakOut() {
        val text = etSpeak!!.text.toString()
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
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