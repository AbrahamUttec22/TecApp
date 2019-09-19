package com.material.tecgurus.actividadesfragmentadmin

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class GestionActividadesAAdapter(private var myContext: Context, fm: FragmentManager, internal var totalTabs: Int) : FragmentPagerAdapter(fm) {

    // this is for fragment tabs
    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> {
                return ActividadadesAFragment()

            }
            1 -> {
                return ProcesoAFragment()

            }
            2 -> {
                return RevisionAFragment()
            }
            3 -> {
                return FinalizadoAFragment()
            }
            else -> return null
        }
    }

    // this counts total number of tabs
    override fun getCount(): Int {
        return totalTabs
    }

}