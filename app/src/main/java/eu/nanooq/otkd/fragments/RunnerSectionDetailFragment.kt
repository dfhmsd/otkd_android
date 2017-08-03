package eu.nanooq.otkd.fragments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.data.geojson.GeoJsonLayer
import com.google.maps.android.data.geojson.GeoJsonLineString
import eu.nanooq.otkd.R
import eu.nanooq.otkd.fragments.base.ViewModelFragment
import eu.nanooq.otkd.inflate
import eu.nanooq.otkd.models.UI.SectionItem
import eu.nanooq.otkd.viewModels.IActivityToolbar
import eu.nanooq.otkd.viewModels.sectionDetail.IRunnerSectionDetailView
import eu.nanooq.otkd.viewModels.sectionDetail.RunnerSectionDetailVievModel
import kotlinx.android.synthetic.main.fragment_runner_section_detail.*
import timber.log.Timber


/**
 *
 * Created by rd on 31/07/2017.
 */
class RunnerSectionDetailFragment : ViewModelFragment<IRunnerSectionDetailView, RunnerSectionDetailVievModel>(), IRunnerSectionDetailView {

    private lateinit var googleMap: GoogleMap


    companion object {

        fun newInstance(sectionJson: String): RunnerSectionDetailFragment {
            val args = Bundle()
            args.putString("sectionItem", sectionJson)
            val frag = RunnerSectionDetailFragment()
            frag.arguments = args
            return frag
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Timber.d("onCreateView()")
        super.onCreateView(inflater, container, savedInstanceState)
        val view = container?.inflate(R.layout.fragment_runner_section_detail)



        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        Timber.d("onViewCreated()")

        super.onViewCreated(view, savedInstanceState)

//        runner_section_detail.requestDisallowInterceptTouchEvent(true)

//        runner_section_detail_container.setOnTouchListener { _, event ->
//            Timber.d("runner_section_detail_container OnTouchListen()")
//
//            runner_section_detail.requestDisallowInterceptTouchEvent(false)
//            runner_section_detail_container.onTouchEvent(event)
//        }

        mMapMask.setOnTouchListener { _, event ->
            val action = event.action
            when (action) {
                MotionEvent.ACTION_DOWN -> {
                    // Disallow ScrollView to intercept touch events.
                    runner_section_detail.requestDisallowInterceptTouchEvent(true)
                    // Disable touch on transparent view
                    false
                }

                MotionEvent.ACTION_UP -> {
                    // Allow ScrollView to intercept touch events.
                    runner_section_detail.requestDisallowInterceptTouchEvent(false)
                    true
                }

                MotionEvent.ACTION_MOVE -> {
                    runner_section_detail.requestDisallowInterceptTouchEvent(true)
                    false
                }

                else -> true
            }
        }

        mMapView?.onCreate(savedInstanceState)

        mMapView?.onResume()


        viewModel.getData()


    }

    private fun addSectionLineOverlay(sectionId: Int) {
        Timber.d("addSectionLineOverlay() $sectionId")

        try {
            MapsInitializer.initialize(activity.applicationContext)
            Timber.d("Google map initialized")

        } catch (e: Exception) {
            Timber.e("Google map initialization failed: ${e.message}")
        }
        mMapView?.isClickable = true
//        mMapView.setOnClickListener {
//            Timber.e("mapViewOnClickListener()")
//
//        }

        mMapView?.getMapAsync {
            Timber.d("getMapAsync()")
            googleMap = it
            googleMap.setOnCameraMoveStartedListener {
                Timber.d("OnCameraMoveStartedListener()")
                runner_section_detail.requestDisallowInterceptTouchEvent(true)

            }
            googleMap.setOnCameraMoveListener {
                Timber.d("OnCameraMoveListener()")
                runner_section_detail.requestDisallowInterceptTouchEvent(true)

            }
            googleMap.setOnCameraIdleListener {
                runner_section_detail.requestDisallowInterceptTouchEvent(false)

            }
            googleMap.setOnCameraMoveCanceledListener {
                runner_section_detail.requestDisallowInterceptTouchEvent(false)
            }

            googleMap.uiSettings.setAllGesturesEnabled(true)
            googleMap.setOnMapClickListener {
                Timber.e("googleMap OnClickListener()")

            }
//            googleMap.uiSettings.setAllGesturesEnabled(false)

            val geoLayer = GeoJsonLayer(googleMap, getSectionGeoData(sectionId), context)
            val feature = geoLayer.features.first()
            val geoPoints = feature.geometry.geometryObject as ArrayList<LatLng>
            val firstPoint = geoPoints.first()
            val lastPoint = geoPoints.last()



            googleMap.addMarker(MarkerOptions()
                    .position(firstPoint))
            googleMap.addMarker(MarkerOptions()
                    .position(lastPoint))


            geoLayer.addLayerToMap()
            val sectionBounds = getLayerBounds(geoLayer)

            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(sectionBounds, 20))

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

    private fun getLayerBounds(geoLayer: GeoJsonLayer): LatLngBounds? {
        val builder = LatLngBounds.builder()
        // Get the coordinates
        geoLayer.features
                .filter { it.hasGeometry() }
                .map { it.geometry }
                .map {
                    (it as GeoJsonLineString).coordinates
                    // Feed the coordinates to the builder.
                }
                .flatMap { it }
                .forEach { builder.include(it) }
        return builder.build()
    }

    override fun setupMap(item: SectionItem) {
        addSectionLineOverlay(item.Id)

    }

    override fun setupRunner(item: SectionItem) {

        val toolbarActivity = activity as IActivityToolbar
        toolbarActivity.onToolbarTitleChange(item.name.toUpperCase())

        vSectionLengthValue.text = "${item.length} km"
        vSectionHighValue.text = "${item.high} m"
        vSectionDownValue.text = "${item.down} m"
        vSectionDifficultyValue.text = item.difficulty.toString()

        vSectionDescription.text = item.description

        vSectionRunnerName.text = item.runnerName
        vSectionRunnerPlace.text = "Poradie: ${item.runnerOrder}"
        vSectionRunnerTime.text = "${item.runnerAverageTime} min/10 km"

        if (item.runnerName.isBlank()) {
            Glide
                    .with(context)
                    .load(R.drawable.ic_placeholder_unassigned)
                    .asBitmap()
                    .centerCrop()
                    .into(object : BitmapImageViewTarget(vSectionRunnerImage) {
                        override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) {
                            super.onLoadFailed(e, errorDrawable)
                            Timber.e("onLoadFailed")
                            val iconBmp = BitmapFactory.decodeResource(resources, R.drawable.ic_placeholder_unassigned)
                            val circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.resources, iconBmp)
                            circularBitmapDrawable.isCircular = true
                            vSectionRunnerImage.setImageDrawable(circularBitmapDrawable)
                        }

                        override fun setResource(resource: Bitmap) {
                            Timber.d("setResource")

                            val iconBmp = BitmapFactory.decodeResource(resources, R.drawable.ic_placeholder_unassigned)
                            val circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.resources, iconBmp)
                            circularBitmapDrawable.isCircular = true
                            vSectionRunnerImage.setImageDrawable(circularBitmapDrawable)
                        }
                    })
        } else {

            Glide
                    .with(context)
                    .load(item.runnerImgUrl)
                    .asBitmap()
                    .centerCrop()
                    .into(object : BitmapImageViewTarget(vSectionRunnerImage) {
                        override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) {
                            super.onLoadFailed(e, errorDrawable)
                            Timber.e("onLoadFailed")
                            val iconBmp = BitmapFactory.decodeResource(resources, R.drawable.ic_default_user)
                            val circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.resources, iconBmp)
                            circularBitmapDrawable.isCircular = true
                            vSectionRunnerImage.setImageDrawable(circularBitmapDrawable)
                        }

                        override fun setResource(resource: Bitmap) {
                            val circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.resources, resource)
                            circularBitmapDrawable.isCircular = true
                            vSectionRunnerImage.setImageDrawable(circularBitmapDrawable)
                        }
                    })
        }

    }
}
