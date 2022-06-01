package fr.yaniv.visitar.ui.modules

import android.content.Context
import android.hardware.Camera
import android.util.Log
import android.view.MotionEvent
import android.widget.FrameLayout
import android.widget.Toast

/**
 * Le module gérant les fonctions de la caméra
 */
class CameraModule(
    private val context: Context,
    private val cameraPreview: FrameLayout) {


    private var mCamera: Camera? = null
    private var mPreview: CameraPreview? = null
    private var lastDistance = 0f


    fun startCamera() {
        // Create an instance of Camera
        mCamera = getCameraInstance()


        mPreview = CameraPreview(context, mCamera!!)

        // On attache la caméra au layout
        cameraPreview.addView(mPreview!!)

        cameraPreview!!.setOnTouchListener { view, event ->
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
        }
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

}