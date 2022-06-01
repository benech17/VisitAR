package fr.yaniv.visitar


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.maps.*
import com.mapbox.maps.extension.style.layers.generated.circleLayer
import com.mapbox.maps.extension.style.layers.generated.lineLayer
import com.mapbox.maps.extension.style.layers.properties.generated.LineCap
import com.mapbox.maps.extension.style.layers.properties.generated.LineJoin
import com.mapbox.maps.extension.style.sources.generated.geoJsonSource
import com.mapbox.maps.extension.style.style
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.navigation.base.options.NavigationOptions
import com.mapbox.navigation.core.MapboxNavigationProvider
import com.mapbox.navigation.ui.tripprogress.model.*



var mapView: MapView? = null

class MapActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val LINE_SOURCE_ID = "LineString"
        val POINT_SOURCE_ID = "PointString"
        val RED_ICON_ID = "PointIcon"
        val resources = this.getResources()
        val polygonFeatureJson = resources.openRawResource(R.raw.carte_projet_pmr).bufferedReader().use{ it.readText() }
        val data = FeatureCollection.fromJson(polygonFeatureJson)
        val line = data.features()!!.get(0)
        data.features()!!.removeAt(0)

        //val mapView = MapView(this)
        setContentView(R.layout.activity_map)
        mapView = findViewById(R.id.mapView)
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
}
