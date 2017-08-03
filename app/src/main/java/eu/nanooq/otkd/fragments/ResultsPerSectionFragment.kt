package eu.nanooq.otkd.fragments

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.data.geojson.GeoJsonLayer
import eu.nanooq.otkd.R
import eu.nanooq.otkd.adapters.SectionResultsRecAdapter
import eu.nanooq.otkd.fragments.base.ViewModelFragment
import eu.nanooq.otkd.inflate
import eu.nanooq.otkd.models.UI.SectionResult
import eu.nanooq.otkd.viewModels.main.results.IResultPerSectionView
import eu.nanooq.otkd.viewModels.main.results.ResultPerSectionViewModel
import kotlinx.android.synthetic.main.fragment_results_per_section.*
import timber.log.Timber



/**
 *
 * Created by rd on 03/08/2017.
 */
class ResultsPerSectionFragment : ViewModelFragment<IResultPerSectionView, ResultPerSectionViewModel>(), IResultPerSectionView {

    lateinit var mAdapter: SectionResultsRecAdapter


    companion object {
        fun newInstance(): ResultsPerSectionFragment {
            return ResultsPerSectionFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Timber.d("onCreateView()")
        super.onCreateView(inflater, container, savedInstanceState)
        val view = container?.inflate(R.layout.fragment_results_per_section)

        mAdapter = SectionResultsRecAdapter(ArrayList()) {
            //            onDetailItemClick(it) todo handle click
        }
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAdapter.hasStableIds()
        vSectionsResultsRecView.adapter = mAdapter
        vSectionsResultsRecView.isNestedScrollingEnabled = false
        vSectionsResultsRecView.layoutManager = LinearLayoutManager(context)
        vSectionsResultsRecView.hasFixedSize()

        mMapView?.onCreate(savedInstanceState)

        mMapView?.onResume()
        initMap()
        showMapData()

    }

    private fun showMapData() {

        mMapView?.getMapAsync {
            Timber.d("getMapAsync()")

            val googleMap = it
            it.uiSettings.setAllGesturesEnabled(true)

            var geoLayer: GeoJsonLayer? = null
            val markers = ArrayList<LatLng>()
            for (i in 1..36) {
                geoLayer = GeoJsonLayer(it, getSectionGeoData(i), context)
                val feature = geoLayer.features.first()
                val geoPoints = feature.geometry.geometryObject as ArrayList<LatLng>
                val firstPoint = geoPoints.first()
                val lastPoint = geoPoints.last()

                val firstMarker = MarkerOptions()
                        .position(firstPoint)
                val lastMarker = MarkerOptions()
                        .position(lastPoint)
                markers.add(firstMarker.position)
                markers.add(lastMarker.position)
                it.addMarker(firstMarker)
                it.addMarker(lastMarker)

                geoLayer.addLayerToMap()
            }

            val builder = LatLngBounds.Builder()
            for (marker in markers) {
                builder.include(marker)
            }
            val bounds = builder.build()

            geoLayer?.let {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 20))
            }
        }
    }

    private fun getSectionGeoData(sectionId: Int): Int {
        return when (sectionId) {
            0 -> R.raw.section_01
            1 -> R.raw.section_01
            2 -> R.raw.section_02
            3 -> R.raw.section_03
            4 -> R.raw.section_04
            5 -> R.raw.section_05
            6 -> R.raw.section_06
            7 -> R.raw.section_07
            8 -> R.raw.section_08
            9 -> R.raw.section_09
            10 -> R.raw.section_10
            11 -> R.raw.section_11
            12 -> R.raw.section_12
            13 -> R.raw.section_13
            14 -> R.raw.section_14
            15 -> R.raw.section_15
            16 -> R.raw.section_16
            17 -> R.raw.section_17
            18 -> R.raw.section_18
            19 -> R.raw.section_19
            20 -> R.raw.section_20
            21 -> R.raw.section_21
            22 -> R.raw.section_22
            23 -> R.raw.section_23
            24 -> R.raw.section_24
            25 -> R.raw.section_25
            26 -> R.raw.section_26
            27 -> R.raw.section_27
            28 -> R.raw.section_28
            29 -> R.raw.section_29
            30 -> R.raw.section_30
            31 -> R.raw.section_31
            32 -> R.raw.section_32
            33 -> R.raw.section_33
            34 -> R.raw.section_34
            35 -> R.raw.section_35
            36 -> R.raw.section_36
            else -> R.raw.section_01
        }
    }

    private fun initMap() {
        mMapMask.setOnTouchListener { _, event ->
            Timber.d("OnTouchListener() mapView $event")

            val action = event.action
            when (action) {
                MotionEvent.ACTION_DOWN -> {
                    // Disallow ScrollView to intercept touch events.
                    results_per_section.requestDisallowInterceptTouchEvent(true)
                    // Disable touch on transparent view
                    false
                }

                MotionEvent.ACTION_UP -> {
                    // Allow ScrollView to intercept touch events.
                    results_per_section.requestDisallowInterceptTouchEvent(false)
                    true
                }

                MotionEvent.ACTION_MOVE -> {
                    results_per_section.requestDisallowInterceptTouchEvent(true)
                    false
                }

                else -> true
            }
        }
        try {
            MapsInitializer.initialize(activity.applicationContext)
            Timber.d("Google map initialized")

        } catch (e: Exception) {
            Timber.e("Google map initialization failed: ${e.message}")
        }

    }

    override fun updateAdapter(newItems: ArrayList<SectionResult>) {
        Timber.d("updateAdapter() $newItems")

        mAdapter.addItems(newItems)
    }

    override fun updateLenghtOfAllSections(lenghtOfAllSections: Float) {
        vSectionResultLengthValue.text = "$lenghtOfAllSections"
    }

    override fun updateSectionsCount(size: Int) {
        vSectionsResultCount.text = "$size"
    }

    override fun updateTeamsStartedCount(teamsStartedCount: Int) {
        vSectionStartedValue.text = "$teamsStartedCount tímov"
    }

    override fun updateTeamsFinishedCount(teamsFinishedCount: Int) {
        vSectionFinishedValue.text = "$teamsFinishedCount"
    }
}
