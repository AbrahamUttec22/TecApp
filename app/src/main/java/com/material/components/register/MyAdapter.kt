package com.material.components.register
import android.support.v4.app.FragmentPagerAdapter
import android.content.Context;
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.View
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.material.components.R
import com.material.components.model.Empresa
import kotlinx.android.synthetic.main.tab1_fragment.view.*
/**
 * @author Abraham
 */
class MyAdapter(private var myContext: Context, fm: FragmentManager, internal var totalTabs: Int) : FragmentPagerAdapter(fm) {

    //declare val for save the collection
    private val marksCollection: CollectionReference
    //declare val for save the collection
    private val usuariosCollection: CollectionReference

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    //init the val for get the collection the Firebase with cloud firestore
    init {
        FirebaseApp.initializeApp(myContext)
        //save the collection marks on val maksCollection
        marksCollection = FirebaseFirestore.getInstance().collection("Empresas")
        usuariosCollection = FirebaseFirestore.getInstance().collection("Usuarios")
    }


    // this is for fragment tabs
    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> {
                //  val homeFragment: HomeFragment = HomeFragment()
                return Tab1Fragment()
            }
            1 -> {
                return Tab2Fragment()
            }
            else -> return null
        }
    }

    // this counts total number of tabs
    override fun getCount(): Int {
        return totalTabs
    }

}