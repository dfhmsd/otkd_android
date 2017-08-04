package eu.nanooq.otkd.views

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent


/**
 *
 * Created by rd on 03/08/2017.
 */

class NonSwipeableViewPager(context: Context, attrs: AttributeSet) : ViewPager(context, attrs) {

    var isPagingEnabled: Boolean = false

    override fun onTouchEvent(event: MotionEvent): Boolean = isPagingEnabled && super.onTouchEvent(event)

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean = isPagingEnabled && super.onInterceptTouchEvent(event)

    override fun canScrollHorizontally(direction: Int): Boolean = isPagingEnabled && super.canScrollHorizontally(direction)

    override fun executeKeyEvent(event: KeyEvent?): Boolean = if (isPagingEnabled) super.executeKeyEvent(event) else false

}
