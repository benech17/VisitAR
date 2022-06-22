package fr.yaniv.visitar.ui.modules

import android.content.Context
import android.hardware.Camera
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.MotionEvent
import android.widget.FrameLayout
import android.widget.Toast
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.schedule

/**
 * Le module gérant les fonctions de la caméra
 */
class CameraModule(
    private val context: Context,
    private val cameraPreview: FrameLayout
) {

    val MEDIA_TYPE_IMAGE = 1
    val MEDIA_TYPE_VIDEO = 2

    var mCamera: Camera? = null
    private var mPreview: CameraPreview? = null
    private var lastDistance = 0f
    private var mPicture: Camera.PictureCallback? = null


    fun startCamera() {
        // Create an instance of Camera
        mCamera = getCameraInstance()


        mPreview = CameraPreview(context, mCamera!!)

        // On attache la caméra au layout
        cameraPreview.addView(mPreview!!)

        cameraPreview!!.setOnTouchListener { view, event ->
            try {
                if (event.getPointerCount() === 1) {
                    cameraFocus()
                    return@setOnTouchListener true
                }
                when (event.getAction() and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_POINTER_DOWN -> lastDistance = getFingersDistance(event)
                    MotionEvent.ACTION_MOVE -> {
                        val newDistance: Float = getFingersDistance(event)
                        if (newDistance > lastDistance) {
                            // getFingerSpace()
                            setZoom(true)
                        } else if (newDistance < lastDistance) {
                            // getFingerSpace()
                            setZoom(false)
                        }
                    }
                }

                return@setOnTouchListener true
            } catch(e: Exception) {
                return@setOnTouchListener false
            }
        }

        initImageCapture()
    }


    private fun getFingersDistance(event: MotionEvent): Float {
        val xDistance = event.getX(0) - event.getY(1)
        val yDistance = event.getY(0) - event.getY(1)
        return Math.sqrt((xDistance * xDistance + yDistance * yDistance).toDouble()).toFloat()
    }

    private fun setZoom(isZoomIn: Boolean) {
        if (!isSupportZoom()) {
            Log.w("Error", "Fonction zoom non supportée")
            return
        }
        val parameters = mCamera!!.parameters
        //parameters.focusMode = Camera.Parameters.FOCUS_MODE_MACRO
        val maxZoom = parameters.maxZoom
        var curZoom = parameters.zoom

        Log.i("INFO", "CurZoom: " + curZoom.toString())
        if (isZoomIn && curZoom < maxZoom) {
            curZoom++
        } else if (curZoom > 0) {
            curZoom--
        }
        parameters.zoom = curZoom
        Log.i("INFO", "MaxZoom: " + maxZoom.toString())
        Log.i("INFO", "Zoom: " + curZoom.toString())
        mCamera!!.parameters = parameters
        /* if (mHelperListener != null) {
            mHelperListener.onZoomChanged(maxZoom, curZoom)
        }*/
    }

    private fun isSupportZoom(): Boolean {
        var isSupport = false
        if (mCamera!!.parameters.isZoomSupported) {
            isSupport = true
        }
        return isSupport
    }

    /** A safe way to get an instance of the Camera object. */
    private fun getCameraInstance(): Camera? {
        return try {
            Camera.open() // attempt to get a Camera instance
        } catch (e: Exception) {
            // Camera is not available (in use or does not exist)
            null // returns null if camera is unavailable
        }
    }

    private fun cameraFocus() {
        if (mCamera == null) return
        val parameter = mCamera!!.parameters
        mCamera!!.autoFocus { success, camera ->
            Log.i("Info", "Focus!")
        }
    }

    /**
     * Enables zoom feature in native camera .  Called from listener of the view
     * used for zoom in  and zoom out.
     *
     *
     * @param zoomInOrOut  "false" for zoom in and "true" for zoom out
     */
    private fun zoomCamera(zoomInOrOut: Boolean) {
        if (mCamera != null) {
            val parameter: Camera.Parameters = mCamera!!.parameters
            if (parameter.isZoomSupported()) {
                val MAX_ZOOM: Int = parameter.getMaxZoom()
                var currnetZoom: Int = parameter.getZoom()
                if (zoomInOrOut && currnetZoom < MAX_ZOOM && currnetZoom >= 0) {
                    parameter.setZoom(++currnetZoom)
                } else if (!zoomInOrOut && currnetZoom <= MAX_ZOOM && currnetZoom > 0) {
                    parameter.setZoom(--currnetZoom)
                }
            } else Toast.makeText(context, "Zoom Not Avaliable", Toast.LENGTH_LONG).show()
            mCamera!!.parameters = parameter
        }
    }

    fun takeImageCapture() {
        mCamera?.takePicture(null, null, mPicture)
    }

    private fun initImageCapture() {
        mPicture = Camera.PictureCallback { data, _ ->
            val pictureFile: File = getOutputMediaFile(MEDIA_TYPE_IMAGE) ?: run {
                Log.d("Error", ("Error creating media file, check storage permissions"))
                return@PictureCallback
            }

            try {
                val fos = FileOutputStream(pictureFile)
                fos.write(data)
                fos.close()
            } catch (e: FileNotFoundException) {
                Log.d("Error", "File not found: ${e.message}")
            } catch (e: IOException) {
                Log.d("Error", "Error accessing file: ${e.message}")
            }

            Timer("CameraResume", false).schedule(500) {
                mPreview!!.startCameraPreview()
            }
        }
    }

    /** Create a file Uri for saving an image or video */
    private fun getOutputMediaFileUri(type: Int): Uri {
        return Uri.fromFile(getOutputMediaFile(type))
    }

    /** Create a File for saving an image or video */
    private fun getOutputMediaFile(type: Int): File? {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        val mediaStorageDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            "VisitARImages"
        )
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        mediaStorageDir.apply {
            if (!exists()) {
                if (!mkdirs()) {
                    Log.d("VisitARImages", "failed to create directory")
                    return null
                }
            }
        }

        // Create a media file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        return when (type) {
            MEDIA_TYPE_IMAGE -> {
                File("${mediaStorageDir.path}${File.separator}IMG_$timeStamp.jpg")
            }
            MEDIA_TYPE_VIDEO -> {
                File("${mediaStorageDir.path}${File.separator}VID_$timeStamp.mp4")
            }
            else -> null
        }
    }
}