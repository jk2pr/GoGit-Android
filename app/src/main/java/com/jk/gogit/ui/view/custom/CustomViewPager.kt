package com.jk.gogit.ui.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent


class CustomViewPager(context: Context, attrs: AttributeSet) : androidx.viewpager.widget.ViewPager(context, attrs) {

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return false
    }

}