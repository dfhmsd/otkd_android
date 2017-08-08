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

    lateinit var sectionItemJson: String
    val ITEM: String = "item"
    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.d("onCreate()")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_section_detail)

        setupToolbar()
        val json = intent.getStringExtra(ITEM)
        json?.run {
            sectionItemJson = json
        }
        if (savedInstanceState != null) {
            sectionItemJson = savedInstanceState.getString(ITEM)
        }

        vDetailsPager.adapter = SectionDetailPagerAdapter(sectionItemJson, supportFragmentManager)
        vDetailsTabs.setupWithViewPager(vDetailsPager)

    }

    private fun setupToolbar() {
        Timber.d("onCreate()")

        val toolbar: Toolbar = mToolbar as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        supportActionBar?.title = ""
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
    }

    override fun onToolbarTitleChange(title: String) {
        Timber.d("onCreate()")

        supportActionBar?.title = title
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Timber.d("onSaveInstanceState()")

        outState.putString(ITEM, sectionItemJson)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        Timber.d("onRestoreInstanceState()")

        super.onRestoreInstanceState(savedInstanceState)

    }
}
