@file:OptIn(ExperimentalPreviewMapboxNavigationAPI::class)
@file:Suppress("DEPRECATION")

package fr.yaniv.visitar


//import com.mapbox.navigation.dropin.NavigationView

import android.Manifest
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.navigation.NavigationView
import com.mapbox.api.directions.v5.DirectionsCriteria
import com.mapbox.api.directions.v5.models.Bearing
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.api.directions.v5.models.RouteOptions
import com.mapbox.api.matching.v5.MapboxMapMatching
import com.mapbox.api.matching.v5.models.MapMatchingResponse
import com.mapbox.bindgen.Expected
import com.mapbox.core.constants.Constants.PRECISION_6
import com.mapbox.geojson.*
import com.mapbox.maps.*
import com.mapbox.maps.extension.style.image.image
import com.mapbox.maps.extension.style.layers.addLayer
import com.mapbox.maps.extension.style.layers.generated.CircleLayer
import com.mapbox.maps.extension.style.layers.generated.circleLayer
import com.mapbox.maps.extension.style.layers.generated.lineLayer
import com.mapbox.maps.extension.style.layers.generated.symbolLayer
import com.mapbox.maps.extension.style.layers.getLayer
import com.mapbox.maps.extension.style.layers.properties.generated.IconAnchor
import com.mapbox.maps.extension.style.layers.properties.generated.LineCap
import com.mapbox.maps.extension.style.layers.properties.generated.LineJoin
import com.mapbox.maps.extension.style.layers.properties.generated.Visibility
import com.mapbox.maps.extension.style.sources.addSource
import com.mapbox.maps.extension.style.sources.generated.geoJsonSource
import com.mapbox.maps.extension.style.sources.getSource
import com.mapbox.maps.extension.style.style
import com.mapbox.maps.extension.style.utils.ColorUtils
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.Plugin.*
import com.mapbox.maps.plugin.animation.camera
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.navigation.base.ExperimentalPreviewMapboxNavigationAPI
import com.mapbox.navigation.base.TimeFormat
import com.mapbox.navigation.base.extensions.applyDefaultNavigationOptions
import com.mapbox.navigation.base.extensions.applyLanguageAndVoiceUnitOptions
import com.mapbox.navigation.base.formatter.DistanceFormatterOptions
import com.mapbox.navigation.base.formatter.UnitType
import com.mapbox.navigation.base.options.NavigationOptions
import com.mapbox.navigation.base.route.*
import com.mapbox.navigation.base.trip.model.RouteLegProgress
import com.mapbox.navigation.base.trip.model.RouteProgress
import com.mapbox.navigation.core.MapboxNavigation
import com.mapbox.navigation.core.MapboxNavigationProvider
import com.mapbox.navigation.core.arrival.ArrivalObserver
import com.mapbox.navigation.core.directions.session.RoutesObserver
import com.mapbox.navigation.core.formatter.MapboxDistanceFormatter
import com.mapbox.navigation.core.replay.MapboxReplayer
import com.mapbox.navigation.core.replay.ReplayLocationEngine
import com.mapbox.navigation.core.replay.route.ReplayProgressObserver
import com.mapbox.navigation.core.replay.route.ReplayRouteMapper
import com.mapbox.navigation.core.reroute.RerouteController
import com.mapbox.navigation.core.reroute.RerouteState
import com.mapbox.navigation.core.trip.session.LocationMatcherResult
import com.mapbox.navigation.core.trip.session.LocationObserver
import com.mapbox.navigation.core.trip.session.RouteProgressObserver
import com.mapbox.navigation.core.trip.session.VoiceInstructionsObserver
import com.mapbox.navigation.dropin.view.MapboxExtendableButton
import com.mapbox.navigation.ui.base.util.MapboxNavigationConsumer
import com.mapbox.navigation.ui.maneuver.api.MapboxManeuverApi
import com.mapbox.navigation.ui.maneuver.model.ManeuverOptions
import com.mapbox.navigation.ui.maneuver.view.MapboxManeuverView
import com.mapbox.navigation.ui.maps.camera.NavigationCamera
import com.mapbox.navigation.ui.maps.camera.data.MapboxNavigationViewportDataSource
import com.mapbox.navigation.ui.maps.camera.lifecycle.NavigationBasicGesturesHandler
import com.mapbox.navigation.ui.maps.camera.state.NavigationCameraState
import com.mapbox.navigation.ui.maps.camera.transition.NavigationCameraTransitionOptions
import com.mapbox.navigation.ui.maps.camera.view.MapboxRecenterButton
import com.mapbox.navigation.ui.maps.camera.view.MapboxRouteOverviewButton
import com.mapbox.navigation.ui.maps.location.NavigationLocationProvider
import com.mapbox.navigation.ui.maps.route.arrow.api.MapboxRouteArrowApi
import com.mapbox.navigation.ui.maps.route.arrow.api.MapboxRouteArrowView
import com.mapbox.navigation.ui.maps.route.arrow.model.RouteArrowOptions
import com.mapbox.navigation.ui.maps.route.line.api.MapboxRouteLineApi
import com.mapbox.navigation.ui.maps.route.line.api.MapboxRouteLineView
import com.mapbox.navigation.ui.maps.route.line.model.MapboxRouteLineOptions
import com.mapbox.navigation.ui.maps.route.line.model.RouteLine
import com.mapbox.navigation.ui.tripprogress.api.MapboxTripProgressApi
import com.mapbox.navigation.ui.tripprogress.model.*
import com.mapbox.navigation.ui.tripprogress.view.MapboxTripProgressView
import com.mapbox.navigation.ui.utils.internal.extensions.getBitmap
import com.mapbox.navigation.ui.voice.api.MapboxSpeechApi
import com.mapbox.navigation.ui.voice.api.MapboxVoiceInstructionsPlayer
import com.mapbox.navigation.ui.voice.model.SpeechAnnouncement
import com.mapbox.navigation.ui.voice.model.SpeechError
import com.mapbox.navigation.ui.voice.model.SpeechValue
import com.mapbox.navigation.ui.voice.model.SpeechVolume
import com.mapbox.navigation.ui.voice.view.MapboxSoundButton
import com.mapbox.navigation.utils.internal.toPoint
import com.mapbox.turf.*
import com.mapbox.turf.TurfMeasurement.bbox
import com.mapbox.turf.TurfMeasurement.destination
import com.mapbox.turf.TurfMisc.nearestPointOnLine
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.Locale.FRENCH


const val REQUEST_LOCATION_PERMISSION = 1

class MapActivity : AppCompatActivity() {
    private companion object {
        private const val BUTTON_ANIMATION_DURATION = 1500L
    }
    private lateinit var mapView: MapView
    private val navigationLocationProvider = NavigationLocationProvider()
    private lateinit var navigationCamera: NavigationCamera
    private lateinit var viewportDataSource: MapboxNavigationViewportDataSource
    private lateinit var mapboxNavigation: MapboxNavigation
    private lateinit var maneuverApi: MapboxManeuverApi
    private lateinit var tripProgressApi: MapboxTripProgressApi
    private lateinit var routeLineApi: MapboxRouteLineApi
    private lateinit var speechApi: MapboxSpeechApi
    private val routeArrowApi: MapboxRouteArrowApi = MapboxRouteArrowApi()
    private lateinit var routeArrowView: MapboxRouteArrowView
    private val mapboxReplayer = MapboxReplayer()
    private val replayLocationEngine = ReplayLocationEngine(mapboxReplayer)
    private val replayProgressObserver = ReplayProgressObserver(mapboxReplayer)
    private lateinit var routeLineView: MapboxRouteLineView
    private lateinit var soundButton: MapboxSoundButton
    private lateinit var recenterButton: MapboxRecenterButton
    private lateinit var routeOverview: MapboxRouteOverviewButton
    private lateinit var maneuverView: MapboxManeuverView
    private lateinit var tripProgressCard: CardView
    private lateinit var tripProgressView: MapboxTripProgressView
    private lateinit var waypointTitle: TextView
    private lateinit var stop: MapboxExtendableButton
    private lateinit var circuit: DirectionsRoute
    private lateinit var points: MutableList<Point>
    private lateinit var wayPointList: MutableList<Point>
    private lateinit var waypointNames: MutableList<String>
    private var destinationReached: Boolean = false

    private val pixelDensity = Resources.getSystem().displayMetrics.density
    private val overviewPadding: EdgeInsets by lazy {
        EdgeInsets(
            140.0 * pixelDensity,
            40.0 * pixelDensity,
            120.0 * pixelDensity,
            40.0 * pixelDensity
        )
    }
    private val landscapeOverviewPadding: EdgeInsets by lazy {
        EdgeInsets(
            30.0 * pixelDensity,
            380.0 * pixelDensity,
            110.0 * pixelDensity,
            20.0 * pixelDensity
        )
    }
    private val followingPadding: EdgeInsets by lazy {
        EdgeInsets(
            180.0 * pixelDensity,
            40.0 * pixelDensity,
            150.0 * pixelDensity,
            40.0 * pixelDensity
        )
    }
    private val landscapeFollowingPadding: EdgeInsets by lazy {
        EdgeInsets(
            30.0 * pixelDensity,
            380.0 * pixelDensity,
            110.0 * pixelDensity,
            40.0 * pixelDensity
        )
    }

    private var isVoiceInstructionsMuted = false
        set(value) {
            field = value
            if (value) {
                soundButton.muteAndExtend(BUTTON_ANIMATION_DURATION)
                voiceInstructionsPlayer.volume(SpeechVolume(0f))
            } else {
                soundButton.unmuteAndExtend(BUTTON_ANIMATION_DURATION)
                voiceInstructionsPlayer.volume(SpeechVolume(1f))
            }
        }
    private lateinit var voiceInstructionsPlayer: MapboxVoiceInstructionsPlayer
    private val voiceInstructionsObserver = VoiceInstructionsObserver { voiceInstructions ->
        speechApi.generate(voiceInstructions, speechCallback)
    }
    private val speechCallback =
        MapboxNavigationConsumer<Expected<SpeechError, SpeechValue>> { expected ->
            expected.fold(
                { error ->
                    voiceInstructionsPlayer.play(
                        error.fallback,
                        voiceInstructionsPlayerCallback
                    )
                },
                { value ->
                    voiceInstructionsPlayer.play(
                        value.announcement,
                        voiceInstructionsPlayerCallback
                    )
                }
            )
        }
    private val voiceInstructionsPlayerCallback =
        MapboxNavigationConsumer<SpeechAnnouncement> { value ->
            speechApi.clean(value)
        }

    private val locationObserver = object : LocationObserver {
        var firstLocationUpdateReceived = false

        override fun onNewRawLocation(rawLocation: Location) {
        }

        override fun onNewLocationMatcherResult(locationMatcherResult: LocationMatcherResult) {
            val enhancedLocation = locationMatcherResult.enhancedLocation

            navigationLocationProvider.changePosition(
                location = enhancedLocation,
                keyPoints = locationMatcherResult.keyPoints,
            )

            val closestFeature = TurfClassification.nearestPoint(enhancedLocation.toPoint(),wayPointList)
            val featureLocation = Location("")
            featureLocation.latitude = closestFeature.latitude()
            featureLocation.longitude = closestFeature.longitude()
            val value = enhancedLocation.distanceTo(featureLocation)
            if (value <= 50.0) {
                var index = 0
                var minIndex = 0
                wayPointList.forEach {
                    if (it.coordinates() == closestFeature.coordinates()) {
                        minIndex = index
                    }
                    index += 1
                }
                val waypointName: String = waypointNames[minIndex]
                waypointTitle.visibility = View.VISIBLE
                waypointTitle.text = "Arrived at $waypointName"
                waypointTitle.setOnClickListener {
                    startARActivity(waypointName)
                    mapboxReplayer.stop()
                }
            } else {
                findViewById<TextView>(R.id.waypointTitle).visibility = View.GONE
            }

            viewportDataSource.onLocationChanged(enhancedLocation)
            viewportDataSource.evaluate()

            if (!firstLocationUpdateReceived) {
                firstLocationUpdateReceived = true
                navigationCamera.requestNavigationCameraToOverview(
                    stateTransitionOptions = NavigationCameraTransitionOptions.Builder()
                        .maxDuration(50)
                        .build()
                )
            }
        }
    }

    private val routeProgressObserver = RouteProgressObserver { routeProgress ->
        viewportDataSource.onRouteProgressChanged(routeProgress)
        viewportDataSource.evaluate()

        val style = mapView.getMapboxMap().getStyle()
        if (style != null) {
            val maneuverArrowResult = routeArrowApi.addUpcomingManeuverArrow(routeProgress)
            routeArrowView.renderManeuverUpdate(style, maneuverArrowResult)
        }
        val maneuvers = maneuverApi.getManeuvers(routeProgress)
        maneuvers.fold(
            { error ->
                Toast.makeText(
                    this@MapActivity,
                    error.errorMessage,
                    Toast.LENGTH_SHORT
                ).show()
            },
            {
                maneuverView.visibility = View.VISIBLE
                maneuverView.renderManeuvers(maneuvers)
            }
        )
        tripProgressView.render(
            tripProgressApi.getTripProgress(routeProgress)
        )
    }

    private val routesObserver = RoutesObserver { routeUpdateResult ->
        if (routeUpdateResult.routes.isNotEmpty()) {
            val routeLines = routeUpdateResult.routes.map { RouteLine(it, null) }

            routeLineApi.setRoutes(
                routeLines
            ) { value ->
                mapView.getMapboxMap().getStyle()?.apply {
                    routeLineView.renderRouteDrawData(this, value)
                }
            }
            viewportDataSource.onRouteChanged(routeUpdateResult.routes.first())
            viewportDataSource.evaluate()
        } else {
            val style = mapView.getMapboxMap().getStyle()
            if (style != null) {
                routeLineApi.clearRouteLine { value ->
                    routeLineView.renderClearRouteLineValue(
                        style,
                        value
                    )
                }
                routeArrowView.render(style, routeArrowApi.clearArrows())
            }
            viewportDataSource.clearRouteData()
            viewportDataSource.evaluate()
        }
    }

    private val arrivalObserver = object : ArrivalObserver {
        override fun onWaypointArrival(routeProgress: RouteProgress) {
            // do something when the user arrives at a waypoint
        }

        override fun onNextRouteLegStart(routeLegProgress: RouteLegProgress) {
            // do something when the user starts a new leg
        }

        override fun onFinalDestinationArrival(routeProgress: RouteProgress) {
            if (!destinationReached) {
                mapView.getMapboxMap().getStyle {
                    it.getLayer("linelayer")?.visibility(Visibility.NONE)
                }
                destinationReached = true
                findViewById<Button>(R.id.btnNav).text = "Start Navigating"
                setRouteAndStartNavigation(listOf(circuit))
            }
            else {
                //btnNav.text = "Move to Itinerary"
                findViewById<Button>(R.id.btnNav).text = "Move to Itinerary"
                clearRouteAndStopNavigation()
                destinationReached = false
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        destinationReached = false
        val LINE_SOURCE_ID = "LineString"
        val POINT_SOURCE_ID = "PointString"
        val RED_ICON_ID = "PointIcon"
        val resources = this.getResources()
        val polygonFeatureJson = resources.openRawResource(R.raw.carte_projet_pmr).bufferedReader().use{ it.readText() }
        val data = FeatureCollection.fromJson(polygonFeatureJson)
        var line = data.features()!!.get(0)
        data.features()!!.removeAt(0)

        mapView = findViewById(R.id.mapView)
        soundButton = findViewById(R.id.soundButton)
        recenterButton = findViewById(R.id.recenter)
        routeOverview = findViewById(R.id.routeOverview)
        maneuverView = findViewById(R.id.maneuverView)
        tripProgressCard = findViewById(R.id.tripProgressCard)
        tripProgressView = findViewById(R.id.tripProgressView)
        waypointTitle = findViewById(R.id.waypointTitle)
        stop = findViewById(R.id.stop)

        mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS)
        mapView.getMapboxMap().loadStyle(styleExtension = style(Style.MAPBOX_STREETS){
            +image(RED_ICON_ID) {
                val marker = resources.getDrawable(R.drawable.ic_red_marker).getBitmap()
                bitmap(marker)
            }})

        val JSONLine = Objects.requireNonNull(line.geometry()) as LineString
        points = JSONLine.coordinates()
        wayPointList = mutableListOf()
        waypointNames = mutableListOf()

        val mapboxMapMatchingRequest = MapboxMapMatching.builder()
            .accessToken(getString(R.string.mapbox_access_token))
            .coordinates(points)
            .waypoints(0,points.size-1)
            .tidy(false)
            .steps(true)
            .voiceInstructions(true)
            .language(FRENCH)
            .bannerInstructions(true)
            .profile(DirectionsCriteria.PROFILE_WALKING)
            .voiceUnits(DirectionsCriteria.METRIC)
            .build()

        mapboxMapMatchingRequest.enqueueCall(object : Callback<MapMatchingResponse> {
            override fun onResponse(call: Call<MapMatchingResponse>, response: Response<MapMatchingResponse>) {
                if (response.isSuccessful) {
                    val matching = response.body()!!.matchings()!![0]
                    line = Feature.fromGeometry(LineString.fromPolyline(matching.geometry()!!, PRECISION_6))
                    mapView.getMapboxMap().getStyle()!!.addSource(
                        geoJsonSource(LINE_SOURCE_ID) {
                            feature(line)
                        })
                    mapView.getMapboxMap().getStyle()!!.addLayer(
                        lineLayer("linelayer", LINE_SOURCE_ID) {
                            lineCap(LineCap.ROUND)
                            lineJoin(LineJoin.ROUND)
                            lineOpacity(0.7)
                            lineWidth(8.0)
                            lineColor(ColorUtils.colorToRgbaString(Color.parseColor("#3bb2d0")))
                        })
                    circuit = matching.toDirectionRoute().toBuilder()
                        .routeIndex("0")
                        .build()
                    var waypointList = mutableListOf<Feature>()
                    for (feature in data.features()!!) {
                        val waypoint = nearestPointOnLine(feature.geometry() as Point,(line.geometry() as LineString).coordinates())
                        waypointList.add(waypoint)
                        wayPointList.add(waypoint.geometry() as Point)
                        waypointNames.add(feature.getStringProperty("name"))
                    }
                    mapView.getMapboxMap().getStyle()!!.addSource(
                        geoJsonSource(POINT_SOURCE_ID) {
                            featureCollection(FeatureCollection.fromFeatures(waypointList))
                        })
                    mapView.getMapboxMap().getStyle()!!.addLayer(
                        symbolLayer("pointLayer",POINT_SOURCE_ID) {
                            iconImage(RED_ICON_ID)
                            iconAnchor(IconAnchor.BOTTOM)
                            iconSize(0.5)
                        })
                }
                else {
                    mapboxMapMatchingRequest.enqueueCall(object : Callback<MapMatchingResponse> {
                        override fun onResponse(call: Call<MapMatchingResponse>, response: Response<MapMatchingResponse>) {
                            if (response.isSuccessful) {
                                val matching = response.body()!!.matchings()!![0]
                                line = Feature.fromGeometry(LineString.fromPolyline(matching.geometry()!!, PRECISION_6))
                                mapView.getMapboxMap().getStyle()!!.addSource(
                                    geoJsonSource(LINE_SOURCE_ID) {
                                        feature(line)
                                    })
                                mapView.getMapboxMap().getStyle()!!.addLayer(
                                    lineLayer("linelayer", LINE_SOURCE_ID) {
                                        lineCap(LineCap.ROUND)
                                        lineJoin(LineJoin.ROUND)
                                        lineOpacity(0.7)
                                        lineWidth(8.0)
                                        lineColor(ColorUtils.colorToRgbaString(Color.parseColor("#3bb2d0")))
                                    })
                                circuit = matching.toDirectionRoute().toBuilder()
                                    .routeIndex("0")
                                    .build()
                                var waypointList = mutableListOf<Feature>()
                                for (feature in data.features()!!) {
                                    val waypoint = nearestPointOnLine(feature.geometry() as Point,(line.geometry() as LineString).coordinates())
                                    waypointList.add(waypoint)
                                    wayPointList.add(waypoint.geometry() as Point)
                                    waypointNames.add(feature.getStringProperty("name"))
                                }
                                mapView.getMapboxMap().getStyle()!!.addSource(
                                    geoJsonSource(POINT_SOURCE_ID) {
                                        featureCollection(FeatureCollection.fromFeatures(waypointList))
                                    })
                                mapView.getMapboxMap().getStyle()!!.addLayer(
                                    symbolLayer("pointLayer",POINT_SOURCE_ID) {
                                        iconImage(RED_ICON_ID)
                                        iconAnchor(IconAnchor.BOTTOM)
                                        iconSize(0.5)
                                    })
                            }
                            else {
                                mapView.getMapboxMap().getStyle()!!.addSource(
                                    geoJsonSource(LINE_SOURCE_ID) {
                                        feature(line)
                                    })
                                mapView.getMapboxMap().getStyle()!!.addLayer(
                                    lineLayer("linelayer", LINE_SOURCE_ID) {
                                        lineCap(LineCap.ROUND)
                                        lineJoin(LineJoin.ROUND)
                                        lineOpacity(0.7)
                                        lineWidth(8.0)
                                        lineColor(ColorUtils.colorToRgbaString(Color.parseColor("#3bb2d0")))
                                    })
                                mapView.getMapboxMap().getStyle()!!.addSource(
                                    geoJsonSource(POINT_SOURCE_ID) {
                                        featureCollection(data)
                                    })
                                mapView.getMapboxMap().getStyle()!!.addLayer(
                                    symbolLayer("pointLayer",POINT_SOURCE_ID) {
                                        iconImage(RED_ICON_ID)
                                        iconAnchor(IconAnchor.BOTTOM)
                                        iconSize(0.5)
                                    })
                                for (feature in data.features()!!) {
                                    wayPointList.add(feature.geometry() as Point)
                                    waypointNames.add(feature.getStringProperty("name"))
                                }
                                circuit = DirectionsRoute.builder().build()
                            }
                        }
                        override fun onFailure(call: Call<MapMatchingResponse>, throwable: Throwable) {
                        }
                    })
                }
            }
            override fun onFailure(call: Call<MapMatchingResponse>, throwable: Throwable) {
            }
        })

        mapView.getMapboxMap().setCamera(
            CameraOptions.Builder().center(
                Point.fromLngLat(
                    2.344173, 48.856017
                )
            ).zoom(14.0).build()
        )

        mapboxNavigation = if (MapboxNavigationProvider.isCreated()) {
            MapboxNavigationProvider.retrieve()
        } else {
            MapboxNavigationProvider.create(
                NavigationOptions.Builder(this.applicationContext)
                    .accessToken(getString(R.string.mapbox_access_token))
                    .distanceFormatterOptions(DistanceFormatterOptions.Builder(this).locale(FRENCH).unitType(UnitType.METRIC).build())
                    .locationEngine(replayLocationEngine)
                    .build()
            )
        }
        mapboxNavigation.registerLocationObserver(locationObserver)

        val btnNav = findViewById<Button>(R.id.btnNav)
        btnNav.setOnClickListener {

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestLocationPermission()
                return@setOnClickListener
            }

            viewportDataSource = MapboxNavigationViewportDataSource(mapView.getMapboxMap())
            navigationCamera = NavigationCamera(
                mapView.getMapboxMap(),
                mapView.camera,
                viewportDataSource
            )
            mapView.camera.addCameraAnimationsLifecycleListener(
                NavigationBasicGesturesHandler(navigationCamera)
            )
            mapView.camera.addCameraAnimationsLifecycleListener(
                NavigationBasicGesturesHandler(navigationCamera)
            )
            navigationCamera.registerNavigationCameraStateChangeObserver { navigationCameraState ->
                when (navigationCameraState) {
                    NavigationCameraState.TRANSITION_TO_FOLLOWING,
                    NavigationCameraState.FOLLOWING -> recenterButton.visibility = View.INVISIBLE
                    NavigationCameraState.TRANSITION_TO_OVERVIEW,
                    NavigationCameraState.OVERVIEW,
                    NavigationCameraState.IDLE -> recenterButton.visibility = View.VISIBLE
                }
            }

            if (this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                viewportDataSource.overviewPadding = landscapeOverviewPadding
            } else {
                viewportDataSource.overviewPadding = overviewPadding
            }
            if (this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                viewportDataSource.followingPadding = landscapeFollowingPadding
            } else {
                viewportDataSource.followingPadding = followingPadding
            }

            val distanceFormatterOptions = mapboxNavigation.navigationOptions.distanceFormatterOptions

            maneuverApi = MapboxManeuverApi(
                MapboxDistanceFormatter(distanceFormatterOptions),
                ManeuverOptions.Builder().filterDuplicateManeuvers(true).build()
            )

            tripProgressApi = MapboxTripProgressApi(
                TripProgressUpdateFormatter.Builder(this)
                    .distanceRemainingFormatter(
                        DistanceRemainingFormatter(distanceFormatterOptions)
                    )
                    .timeRemainingFormatter(
                        TimeRemainingFormatter(this, FRENCH)
                    )
                    .percentRouteTraveledFormatter(
                        PercentDistanceTraveledFormatter()
                    )
                    .estimatedTimeToArrivalFormatter(
                        EstimatedTimeToArrivalFormatter(this, TimeFormat.TWENTY_FOUR_HOURS)
                    )
                    .build()
            )

            speechApi = MapboxSpeechApi(
                this,
                getString(R.string.mapbox_access_token),
                FRENCH.language
            )
            voiceInstructionsPlayer = MapboxVoiceInstructionsPlayer(
                this,
                getString(R.string.mapbox_access_token),
                FRENCH.language
            )

            val mapboxRouteLineOptions = MapboxRouteLineOptions.Builder(this)
                .withRouteLineBelowLayerId("road-label")
                .build()
            routeLineApi = MapboxRouteLineApi(mapboxRouteLineOptions)
            routeLineView = MapboxRouteLineView(mapboxRouteLineOptions)

            val routeArrowOptions = RouteArrowOptions.Builder(this).build()
            routeArrowView = MapboxRouteArrowView(routeArrowOptions)

            stop.setOnClickListener {
                clearRouteAndStopNavigation()
            }
            recenterButton.setOnClickListener {
                navigationCamera.requestNavigationCameraToFollowing()
                routeOverview.showTextAndExtend(BUTTON_ANIMATION_DURATION)
            }
            routeOverview.setOnClickListener {
                navigationCamera.requestNavigationCameraToOverview()
                recenterButton.showTextAndExtend(BUTTON_ANIMATION_DURATION)
            }
            soundButton.setOnClickListener {
                isVoiceInstructionsMuted = !isVoiceInstructionsMuted
            }
            soundButton.unmute()

            mapView.location.apply {
                setLocationProvider(navigationLocationProvider)

                locationPuck = LocationPuck2D(
                    bearingImage = ContextCompat.getDrawable(
                    this@MapActivity,
                    R.drawable.ic_puck_location
                    )
                )
                enabled = true
            }

            /*
            mapView.gestures.addOnMapLongClickListener { point ->
                findRoute(point)
                true
            }
            */
            if (!destinationReached) {
                btnNav.text = "Move to Itinerary"
                findRoute(points[0])
            }
            else {
                setRouteAndStartNavigation(listOf(circuit))
            }

            //setRouteAndStartNavigation(listOf(circuit))
            mapboxNavigation.startTripSession()
            recenterButton.visibility = View.VISIBLE
            //Toast.makeText(this@MapActivity,waypointArray.get(3).toString(),Toast.LENGTH_LONG).show()
        }
    }

    private fun findRoute(destination: Point) {
        val originLocation = navigationLocationProvider.lastLocation
        val originPoint = originLocation?.let {
            Point.fromLngLat(it.longitude, it.latitude)
        } ?: return

        mapboxNavigation.requestRoutes(
            RouteOptions.builder()
                .applyDefaultNavigationOptions()
                .profile(DirectionsCriteria.PROFILE_WALKING)
                //.applyLanguageAndVoiceUnitOptions(this)
                .voiceUnits(DirectionsCriteria.METRIC)
                .language(FRENCH.language)
                .coordinatesList(listOf(originPoint, destination))
                .bearingsList(
                    listOf(
                        Bearing.builder()
                            .angle(originLocation.bearing.toDouble())
                            .degrees(45.0)
                            .build(),
                        null
                    )
                )
                .layersList(listOf(mapboxNavigation.getZLevel(), null))
                .build(),
            object : RouterCallback {
                override fun onRoutesReady(
                    routes: List<DirectionsRoute>,
                    routerOrigin: RouterOrigin
                ) {
                    setRouteAndStartNavigation(routes)
                }

                override fun onFailure(
                    reasons: List<RouterFailure>,
                    routeOptions: RouteOptions
                ) {
                }

                override fun onCanceled(routeOptions: RouteOptions, routerOrigin: RouterOrigin) {
                }
            }
        )
    }

    private fun setRouteAndStartNavigation(routes: List<DirectionsRoute>) {
        mapboxNavigation.setRoutes(routes)
        startSimulation(routes.first())

        soundButton.visibility = View.VISIBLE
        routeOverview.visibility = View.VISIBLE
        tripProgressCard.visibility = View.VISIBLE

        navigationCamera.requestNavigationCameraToOverview()
    }

    private fun clearRouteAndStopNavigation() {
        mapView.getMapboxMap().getStyle {
            it.getLayer("linelayer")?.let { layer -> layer.visibility(Visibility.VISIBLE) }
        }
        mapboxNavigation.setRoutes(listOf())
        mapboxReplayer.stop()

        soundButton.visibility = View.GONE
        maneuverView.visibility = View.GONE
        routeOverview.visibility = View.GONE
        tripProgressCard.visibility = View.GONE
    }

    private fun startSimulation(route: DirectionsRoute) {
        mapboxReplayer.run {
            stop()
            clearEvents()
            val replayEvents = ReplayRouteMapper().mapDirectionsRouteGeometry(route)
            pushEvents(replayEvents)
            seekTo(replayEvents.first())
            play()
        }
    }

    override fun onStart() {
        super.onStart()
        mapboxNavigation.registerRoutesObserver(routesObserver)
        mapboxNavigation.registerRouteProgressObserver(routeProgressObserver)
        mapboxNavigation.registerLocationObserver(locationObserver)
        mapboxNavigation.registerVoiceInstructionsObserver(voiceInstructionsObserver)
        mapboxNavigation.registerRouteProgressObserver(replayProgressObserver)
        mapboxNavigation.registerArrivalObserver(arrivalObserver)

        if (mapboxNavigation.getRoutes().isEmpty()) {
        // if simulation is enabled (ReplayLocationEngine set to NavigationOptions)
        // but we're not simulating yet,
        // push a single location sample to establish origin
            mapboxReplayer.pushEvents(
                listOf(
                    ReplayRouteMapper.mapToUpdateLocation(
                        eventTimestamp = 0.0,
                        point = Point.fromLngLat(2.3294,48.8595 )
                    )
                )
            )
            mapboxReplayer.playFirstLocation()
        }
        else {
            mapboxReplayer.play()
        }
    }

    override fun onStop() {
        super.onStop()
        mapboxNavigation.unregisterRoutesObserver(routesObserver)
        mapboxNavigation.unregisterRouteProgressObserver(routeProgressObserver)
        mapboxNavigation.unregisterLocationObserver(locationObserver)
        mapboxNavigation.unregisterVoiceInstructionsObserver(voiceInstructionsObserver)
        mapboxNavigation.unregisterRouteProgressObserver(replayProgressObserver)
        mapboxNavigation.unregisterArrivalObserver(arrivalObserver)
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        MapboxNavigationProvider.destroy()
        mapboxReplayer.finish()
        maneuverApi.cancel()
        routeLineApi.cancel()
        routeLineView.cancel()
        speechApi.cancel()
        voiceInstructionsPlayer.shutdown()
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

    private fun startARActivity(extras: String) {
        val intent = Intent(this, ARactivity::class.java)
        intent.putExtra("id",extras)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}

