package com.material.components.activity.card

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.alejandrolora.finalapp.toast
import com.bumptech.glide.Glide
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore

import com.material.components.R
import com.material.components.model.Anuncio
import com.material.components.utils.Tools
import kotlinx.android.synthetic.main.item_card_wizard_light.*
import kotlinx.android.synthetic.main.item_card_wizard_light.view.*
import kotlinx.android.synthetic.main.list_view_imagen.view.*

/**
 * @author Abraham
 * 21/06/2019
 * see the anuncios
 */
class CardWizardLight : AppCompatActivity() {

    private var viewPager: ViewPager? = null
    private var myViewPagerAdapter: MyViewPagerAdapter? = null
    private var about_title_array = arrayOf("")
    private var about_description_array = arrayOf("")
    private var about_images_array = intArrayOf(R.drawable.img_wizard_1)
    private var ubicacion= arrayOf("")

    //declare val for save the collection
    private val anuncioCollection: CollectionReference

    //init the val for get the collection the Firebase with cloud firestore
    init {
        FirebaseApp.initializeApp(this)
        //save the collection marks on val maksCollection
        anuncioCollection = FirebaseFirestore.getInstance().collection("Anuncios")
    }

    //  viewpager change listener
    internal var viewPagerPageChangeListener: ViewPager.OnPageChangeListener = object : ViewPager.OnPageChangeListener {

        override fun onPageSelected(position: Int) {
            bottomProgressDots(position)
        }

        override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {

        }

        override fun onPageScrollStateChanged(arg0: Int) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_wizard_light)
        MAX_STEP=0
        about_images_array= intArrayOf(0)
        about_title_array= emptyArray()
        about_description_array= emptyArray()
        addMarksListener(applicationContext)
        viewPager = findViewById<View>(R.id.view_pager) as ViewPager
    }


    //backend
    private fun addMarksListener(applicationContext: Context) {
        anuncioCollection.addSnapshotListener { snapshots, error ->
            if (error == null) {
                val changes = snapshots?.documentChanges
                if (changes != null) {
                    addChanges(changes,applicationContext)
                }
            } else {
                toast("Ha ocurrido un error intenta de nuevo")
            }
        }
    }

    /**
     * @param changes
     * aqui se hace el recorrido de la coleccion de cloudfirestore
     */
    private fun addChanges(changes: List<DocumentChange>,applicationContext: Context) {
        val itemAnuncio = ArrayList<Anuncio>()//lista local de una sola instancia
        for (change in changes) {
            itemAnuncio.add(change.document.toObject(Anuncio::class.java))//ir agregando los datos a la lista
        }//una ves agregado los campos mandar a llamar la vista
        addToList(itemAnuncio,applicationContext)//vista

    }

    /**
     * @param itemAnuncio
     */
    private fun addToList(itemAnuncio: List<Anuncio>,applicationContext: Context) {
        //set empty the data
        MAX_STEP=0
        about_title_array= emptyArray()
        about_description_array= emptyArray()
        about_images_array= intArrayOf()
        ubicacion= emptyArray()

        for (item in itemAnuncio) {//recorremos la lista de usuario para agregarlo a la lista de people
            about_title_array += arrayOf(item.titulo)
            about_description_array += arrayOf(item.description)
            about_images_array+= intArrayOf(R.drawable.img_wizard_1)
            ubicacion+=item.ubicacion

            MAX_STEP++
        }
        bottomProgressDots(0)//init the 0
        myViewPagerAdapter = MyViewPagerAdapter()
        viewPager!!.adapter = myViewPagerAdapter
        viewPager!!.addOnPageChangeListener(viewPagerPageChangeListener)
        Tools.setSystemBarColor(this, R.color.overlay_light_80)
        Tools.setSystemBarLight(this)
    }

    //see the view
    private fun bottomProgressDots(current_index: Int) {
        val dotsLayout = findViewById<View>(R.id.layoutDots) as LinearLayout
        val dots = arrayOfNulls<ImageView>(MAX_STEP)
        dotsLayout.removeAllViews()
        for (i in dots.indices) {
            dots[i] = ImageView(this)
            val width_height = 15
            val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams(width_height, width_height))
            params.setMargins(10, 10, 10, 10)
            dots[i]!!.setLayoutParams(params)
            dots[i]!!.setImageResource(R.drawable.shape_circle)
            dots[i]!!.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN)
            dotsLayout.addView(dots[i])
        }
        if (dots.size > 0) {
            dots[current_index]!!.setImageResource(R.drawable.shape_circle)
            dots[current_index]!!.setColorFilter(resources.getColor(R.color.light_green_600), PorterDuff.Mode.SRC_IN)
        }
    }

    /**
     * View pager adapter, for carrusel
     */
    inner class MyViewPagerAdapter : PagerAdapter() {
        private var layoutInflater: LayoutInflater? = null
        private var btnNext: Button? = null

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            layoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            val view = layoutInflater!!.inflate(R.layout.item_card_wizard_light, container, false)
            (view.findViewById<View>(R.id.title) as TextView).text = about_title_array[position]
            (view.findViewById<View>(R.id.description) as TextView).text = about_description_array[position]
            Glide
                    .with(view.context)
                    .load("${ubicacion[position]}")
                    .into(view.imagenItem)


            btnNext = view.findViewById<View>(R.id.btn_next) as Button

            if (position == about_title_array.size - 1) {
                btnNext!!.text = "Entendido"
            } else {
                btnNext!!.text = "Siguiente"
            }


            btnNext!!.setOnClickListener {
                val current = viewPager!!.currentItem + 1
               // toast("valor de current es "+current.toString())
                //toast("valor de MAN STEP es "+MAX_STEP.toString())
                //toast(""+current+"<"+""+MAX_STEP)
                if (current < MAX_STEP) {
                    // move to next screen
                    viewPager!!.currentItem = current
                } else {
                    finish()
                }
            }

            container.addView(view)
            return view
        }

        override fun getCount(): Int {
            return about_title_array.size
        }

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view === obj
        }


        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            val view = `object` as View
            container.removeView(view)
        }
    }

    companion object {
        private var MAX_STEP = 0
    }
}