package eu.nanooq.otkd.activities

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import eu.nanooq.otkd.R
import eu.nanooq.otkd.activities.base.ViewModelActivity
import eu.nanooq.otkd.adapters.TrackDetailPagerAdapter
import eu.nanooq.otkd.viewModels.trackDetail.ITrackDetailView
import eu.nanooq.otkd.viewModels.trackDetail.TrackDetailViewModel
import kotlinx.android.synthetic.main.activity_track_detail.*
import timber.log.Timber

/**
 *
 * Created by rd on 04/08/2017.
 */
class TrackDetailActivity : ViewModelActivity<ITrackDetailView, TrackDetailViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.d("onCreate()")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track_detail)

        setupToolbar()

        val sectionId = intent.getIntExtra("sectionId", 0)
        val sectionName = intent.getStringExtra("sectionName")
        if (sectionName != null) {
            onToolbarTitleChange(sectionName.toUpperCase())
        }

        vDetailsPager.adapter = TrackDetailPagerAdapter(supportFragmentManager, sectionId)
        vDetailsTabs.setupWithViewPager(vDetailsPager)

    }

    private fun setupToolbar() {
        val toolbar: Toolbar = mToolbar as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        supportActionBar?.title = ""
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
    }

    fun onToolbarTitleChange(title: String) {
        supportActionBar?.title = title
    }

    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        return true
    }
}
