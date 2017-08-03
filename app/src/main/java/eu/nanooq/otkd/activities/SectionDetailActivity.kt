package eu.nanooq.otkd.activities

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import eu.nanooq.otkd.R
import eu.nanooq.otkd.activities.base.ViewModelActivity
import eu.nanooq.otkd.adapters.SectionDetailPagerAdapter
import eu.nanooq.otkd.viewModels.IActivityToolbar
import eu.nanooq.otkd.viewModels.sectionDetail.ISectionDetailView
import eu.nanooq.otkd.viewModels.sectionDetail.SectionDetailViewModel
import kotlinx.android.synthetic.main.activity_section_detail.*
import timber.log.Timber

/**
 *
 * Created by rd on 31/07/2017.
 */
class SectionDetailActivity : ViewModelActivity<ISectionDetailView, SectionDetailViewModel>(), IActivityToolbar {

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.d("onCreate()")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_section_detail)

        setupToolbar()

        val sectionItemJson = intent.getStringExtra("item")

        vDetailsPager.adapter = SectionDetailPagerAdapter(sectionItemJson, supportFragmentManager)
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

    override fun onToolbarTitleChange(title: String) {
        supportActionBar?.title = title
    }
}
