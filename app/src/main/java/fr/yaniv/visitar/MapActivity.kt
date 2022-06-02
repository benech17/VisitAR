@file:OptIn(ExperimentalPreviewMapboxNavigationAPI::class)

package fr.yaniv.visitar


//import com.mapbox.navigation.dropin.NavigationView
import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.maps.*
import com.mapbox.maps.extension.style.layers.generated.circleLayer
import com.mapbox.maps.extension.style.layers.generated.lineLayer
import com.mapbox.maps.extension.style.layers.properties.generated.LineCap
import com.mapbox.maps.extension.style.layers.properties.generated.LineJoin
import com.mapbox.maps.extension.style.layers.properties.generated.Visibility
import com.mapbox.maps.extension.style.sources.generated.geoJsonSource
import com.mapbox.maps.extension.style.style
import com.mapbox.navigation.base.ExperimentalPreviewMapboxNavigationAPI
import com.mapbox.navigation.dropin.NavigationView
import com.mapbox.navigation.ui.tripprogress.model.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

var mapView: MapView? = null
var navigationView: NavigationView? = null
const val REQUEST_LOCATION_PERMISSION = 1

class MapActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val LINE_SOURCE_ID = "LineString"
        val POINT_SOURCE_ID = "PointString"
        val RED_ICON_ID = "PointIcon"
        val resources = this.getResources()
        val polygonFeatureJson = resources.openRawResource(R.raw.carte_projet_pmr).bufferedReader().use{ it.readText() }
        val data = FeatureCollection.fromJson(polygonFeatureJson)
        val line = data.features()!!.get(0)
        data.features()!!.removeAt(0)

        mapView = findViewById(R.id.mapView)
        navigationView = findViewById(R.id.navigationView)
        mapView!!.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS)
        mapView!!.getMapboxMap().setCamera(
            CameraOptions.Builder().center(
                Point.fromLngLat(
                    2.344173, 48.856017
                )
            ).zoom(14.0).build()
        )
        mapView!!.getMapboxMap().loadStyle(
            styleExtension = style(Style.MAPBOX_STREETS) {
                /*
                +image(RED_ICON_ID) {
                    bitmap(BitmapFactory.decodeResource(resources, R.drawable.ic_red_marker))
                }
                */
                +geoJsonSource(LINE_SOURCE_ID) {
                    feature(line)
                }
                +geoJsonSource(POINT_SOURCE_ID) {
                    featureCollection(data)
                }
                +lineLayer("linelayer", LINE_SOURCE_ID) {
                    lineCap(LineCap.ROUND)
                    lineJoin(LineJoin.ROUND)
                    lineOpacity(0.7)
                    lineWidth(8.0)
                    lineColor("red")
                }

                +circleLayer("pointsLayer", POINT_SOURCE_ID) {
                }
            })

        /*
        val navigationOptions = NavigationOptions.Builder(this)
            .accessToken("YOUR_ACCESS_TOKEN")
            .build()
        val mapboxNavigation = MapboxNavigationProvider.create(navigationOptions)
        */
        //à gérer après

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationPermission()
            return
        }
        //mapboxNavigation.startTripSession()
        //mapboxNavigation.stopTripSession()

        val btnNav = findViewById<Button>(R.id.btnNav)
        btnNav.setOnClickListener {
            mapView!!.setVisibility(View.GONE)
            navigationView!!.setVisibility(View.VISIBLE)
        }
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    @AfterPermissionGranted(REQUEST_LOCATION_PERMISSION)
    private fun requestLocationPermission() {
        val perms = arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION)
        if (EasyPermissions.hasPermissions(this, *perms)) {
            Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show()
        } else {
            EasyPermissions.requestPermissions(
                this,
                "Please grant the location permission",
                REQUEST_LOCATION_PERMISSION,
                *perms
            )
        }
    }

    /*
    fun onCleared() {
        MapboxNavigationProvider.destroy()
    }
     */
}

