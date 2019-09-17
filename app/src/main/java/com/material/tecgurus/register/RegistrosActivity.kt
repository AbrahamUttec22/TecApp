package com.material.tecgurus.register
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.material.tecgurus.R
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager

/**
 * @author Abraham
 */
class RegistrosActivity : AppCompatActivity() {

    var tabLayout: TabLayout? = null
    var viewPager: ViewPager? = null
    private var exitTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registros)
        tabLayout = findViewById<TabLayout>(R.id.tabLayout)//id_layout from activity registros
        viewPager = findViewById<ViewPager>(R.id.viewPager)//id_view_pager from activity registros
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Empresa"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Empleado"))
        // tabLayout!!.tabGravity = TabLayout.GRAVITY_FILL
        val adapter = MyAdapter(this, supportFragmentManager, tabLayout!!.tabCount)
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
        if (System.currentTimeMillis() - exitTime > 2000) {
            //Toast.makeText(this, "Presiona de nuevo para salir de la aplicación", Toast.LENGTH_SHORT).show()
            exitTime = System.currentTimeMillis()
        } else {
            //finish()
        }
    }
}