package com.material.components.actividadesfragment

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import com.alejandrolora.finalapp.goToActivity
import com.material.components.R
import com.material.components.activity.login.ForgotPasswordActivity
import com.material.components.drawer.DashboarActivity
import com.material.components.register.MyAdapter

/**
 * @author Abraham Casas Aguilar
 */
class GestionActividadesActivity : AppCompatActivity() {

    var tabLayout: TabLayout? = null
    var viewPager: ViewPager? = null
    private var exitTime: Long = 0

    //this class is for
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gestion_actividades)
        tabLayout = findViewById<TabLayout>(R.id.tabLayoutActividades)//id_layout from activity_gestion
        viewPager = findViewById<ViewPager>(R.id.viewPagerActividades)//id_view_pager from activity_gestion
        //actividades
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Actividades"))//pestañas de actividades
        tabLayout!!.addTab(tabLayout!!.newTab().setText("En proceso"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("En revisión"))
        //finalizado
        //concluido
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Finalizado"))

        // tabLayout!!.tabGravity = TabLayout.GRAVITY_FILL
        val adapter = GestionActividadesAdapter(this, supportFragmentManager, tabLayout!!.tabCount)
        viewPager!!.adapter = adapter
        viewPager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager!!.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
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