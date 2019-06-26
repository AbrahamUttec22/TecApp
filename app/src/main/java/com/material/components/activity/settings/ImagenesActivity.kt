package com.material.components.activity.settings

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.google.firebase.storage.FirebaseStorage
import com.material.components.R
import com.material.components.adapter.ImagenAdapter
import com.material.components.model.ImagenProyecto
import com.material.components.utils.Tools
import kotlinx.android.synthetic.main.activity_imagenes.*



class ImagenesActivity : AppCompatActivity() {

    private lateinit var adapter: ImagenAdapter
    private lateinit var personImagen: List<ImagenProyecto>
    lateinit var storage: FirebaseStorage


    private var imagenes = arrayOf("2131231079")

    init {
        storage = FirebaseStorage.getInstance()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imagenes)
        initToolbar()
        //Log.w("IMAGENES",""+imagenes.toList())

        //personImagen=getImagen()
        adapter = ImagenAdapter(this, R.layout.list_view_imagen, imagenes)
        listView.adapter = adapter
    }

    private fun getImagen(): List<ImagenProyecto> {
        return listOf(
                ImagenProyecto("Alejandro"),
                ImagenProyecto("Fernando"),
                ImagenProyecto("Alicia"),
                ImagenProyecto("Paula"),
                ImagenProyecto("Alberto"),
                ImagenProyecto("Cristian")
        )
    }

    private fun initToolbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.setNavigationIcon(R.drawable.ic_menu)
        setSupportActionBar(toolbar)
        supportActionBar!!.setTitle("Imagenes")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        Tools.setSystemBarColor(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search_setting, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
