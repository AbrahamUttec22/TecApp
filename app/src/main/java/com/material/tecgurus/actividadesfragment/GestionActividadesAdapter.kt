package com.material.tecgurus.actividadesfragment

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * @author Abraham
 */
class GestionActividadesAdapter(private var myContext: Context, fm: FragmentManager, internal var totalTabs: Int) : FragmentPagerAdapter(fm) {

    // this is for fragment tabs
    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> {
                var a = ActividadesFragment()
                return a

            }
            1 -> {
                var a = ProcesoFragment()
                return a

            }
            2 -> {
                var a = RevisionFragment()

                return a
            }
            3 -> {
                var a = FinalizadoFragment()

                return a
            }
            else -> return null
        }
    }

    // this counts total number of tabs
    override fun getCount(): Int {
        return totalTabs
    }

}