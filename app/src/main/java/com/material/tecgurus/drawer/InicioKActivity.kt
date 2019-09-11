package com.material.tecgurus.drawer
import com.material.tecgurus.R
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBar
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.material.tecgurus.usuariosfragment.PanelUsuariosFragment

/**
 * @author Abraham Casas Aguilar
 */
class InicioKActivity : AppCompatActivity() {

    //menu desplegable
    private lateinit var mDrawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio_two)
        // val toolbar: Toolbar = findViewById(R.id.toolbarInicio)
        val toolbar: Toolbar = findViewById(R.id.toolbarInicio)
        setSupportActionBar(toolbar)
        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }
        mDrawerLayout = findViewById(R.id.drawerLayout)
        val navigationView: NavigationView = findViewById(R.id.navigationView)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            // set item as selected to persist highlight
            menuItem.isChecked = true
            // close drawer when item is tapped
            mDrawerLayout.closeDrawers()
            // Handle navigation view item clicks here.
            when (menuItem.itemId) {
                R.id.nav_item_inicio -> {
                    PanelUsuariosFragment()
                    toolbar.title = getString(R.string.inbox)
                    //navigationPosition = R.id.nav_item_inicio
                    //navigateToFragment(PanelUsuariosFragment.newInstance())

                }
            }
            // Add code here to update the UI based on the item selected
            // For example, swap UI fragments here
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item!!.itemId) {
            android.R.id.home -> {
                mDrawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}