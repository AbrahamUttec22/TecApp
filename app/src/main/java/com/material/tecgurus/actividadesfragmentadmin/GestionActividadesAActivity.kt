package com.material.tecgurus.actividadesfragmentadmin

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity;
import com.alejandrolora.finalapp.goToActivity
import com.material.tecgurus.R
import com.material.tecgurus.drawer.DashboarActivity

/**
 * @author Abraham Casas Aguilar
 */
class GestionActividadesAActivity : AppCompatActivity() {

    var tabLayout: TabLayout? = null
    var viewPager: ViewPager? = null
    private var exitTime: Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gestion_actividades_a)
        tabLayout = findViewById<TabLayout>(R.id.tabLayoutActividadesA)//id_layout from activity_gestion
        viewPager = findViewById<ViewPager>(R.id.viewPagerActividadesA)//id_view_pager from activity_gestion
        //actividades
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Actividades"))//pestañas de actividades
        tabLayout!!.addTab(tabLayout!!.newTab().setText("En proceso"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("En revisión"))
        //finalizado
        //concluido
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Finalizado"))

        // tabLayout!!.tabGravity = TabLayout.GRAVITY_FILL
        val adapter = GestionActividadesAAdapter(this, supportFragmentManager, tabLayout!!.tabCount)
        viewPager!!.adapter = adapter
        viewPager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager!!.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                viewPager!!.currentItem = tab.position

            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                viewPager!!.currentItem = tab.position

            }
        })
    }

    override fun onBackPressed() {
        doExitApp()
    }

    fun doExitApp() {
        finish()
        goToActivity<DashboarActivity> {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

}