package fr.yaniv.visitar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mapbox.geojson.Feature
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.layers.generated.lineLayer
import com.mapbox.maps.extension.style.layers.properties.generated.LineCap
import com.mapbox.maps.extension.style.layers.properties.generated.LineJoin
import com.mapbox.maps.extension.style.sources.generated.geoJsonSource
import com.mapbox.maps.extension.style.style


var mapView: MapView? = null

class MapActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val GEOJSON_SOURCE_ID = "LineString"
        val resources = this.getResources()
        val polygonFeatureJson = resources.openRawResource(R.raw.carte_projet_pmr).bufferedReader().use{ it.readText() }
        val itineraire = Feature.fromJson(polygonFeatureJson)

        val mapView = MapView(this)
        setContentView(mapView)
        // mapView = findViewById(R.id.mapView)
        mapView.getMapboxMap().setCamera(
            CameraOptions.Builder().center(
                Point.fromLngLat(
                    2.344173, 48.856017
                )
            ).zoom(14.0).build()
        )
        mapView.getMapboxMap().loadStyle(style(styleUri = Style.MAPBOX_STREETS) {
            +geoJsonSource(GEOJSON_SOURCE_ID) {
                feature(itineraire)
            }
            +lineLayer("linelayer", GEOJSON_SOURCE_ID) {
                lineCap(LineCap.ROUND)
                lineJoin(LineJoin.ROUND)
                lineOpacity(0.7)
                lineWidth(8.0)
                lineColor("red")
            }
        }
        )
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
