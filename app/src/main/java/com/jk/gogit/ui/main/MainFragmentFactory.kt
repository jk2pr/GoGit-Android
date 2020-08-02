package com.jk.gogit.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import javax.inject.Inject

class MainFragmentFactory
@Inject constructor() : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {

            MainFragment::class.java.name -> {
                val fragment = MainFragment()
                fragment
            }
            SplashFragment::class.java.name -> {
                val fragment = SplashFragment()
                fragment
            }
            else -> super.instantiate(classLoader, className)
        }
    }
}