package com.material.components.actividadesfragment
import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.material.components.register.Tab1Fragment
import com.material.components.register.Tab2Fragment

/**
 * @author Abraham
 */
class GestionActividadesAdapter(private var myContext: Context, fm: FragmentManager, internal var totalTabs: Int) : FragmentPagerAdapter(fm) {

    // this is for fragment tabs
    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> {
                //
                return ActividadesFragment()
            }
            1 -> {
                return ProcesoFragment()
            }
            2 -> {
                return RevisionFragment()
            }
            3 -> {
                return FinalizadoFragment()
            }
            else -> return null
        }
    }

    // this counts total number of tabs
    override fun getCount(): Int {
        return totalTabs
    }

}