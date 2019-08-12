package com.material.components.activity.about

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.alejandrolora.finalapp.toast
import com.beloo.widget.chipslayoutmanager.layouter.Item
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.material.components.R
import com.material.components.adapter.AdministrarAnuncioAdapter
import com.material.components.adapter.EstatusChecadorAdapter
import com.material.components.model.Anuncio
import com.material.components.model.Checador
import com.material.components.utils.Tools
import kotlinx.android.synthetic.main.activity_encuesta.*
import java.text.SimpleDateFormat
import java.util.*

import com.itextpdf.text.Document
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import java.io.FileOutputStream
import java.lang.Exception
import android.os.Environment
import android.util.Log
import android.widget.TextView
import com.alejandrolora.finalapp.goToActivity
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.QuerySnapshot
import com.itextpdf.text.Element
import com.material.components.adapter.ActividadesAdapter
import com.material.components.drawer.DashboarActivity
import com.material.components.model.Actividades
import kotlinx.android.synthetic.main.activity_actividades.*
import kotlinx.android.synthetic.main.activity_encuesta.listView
import kotlinx.android.synthetic.main.list_view_estatus_checador.view.*
import org.w3c.dom.Text

/**
 * @author Abraham Casas Aguilar
 */
class EstatusChecadorActivity : AppCompatActivity() {

    private lateinit var adapter: EstatusChecadorAdapter
    private lateinit var eventoList: List<Checador>
    private lateinit var eventoListTwo: List<Checador>

    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private val STORAGE_CODE: Int = 100
    private var mesEscogido = ""
    //declare val for save the collection
    private val checadorCollection: CollectionReference
    private val usuarioCollection: CollectionReference


    //init the val for get the collection the Firebase with cloud firestore
    init {
        FirebaseApp.initializeApp(this)
        //save the collection marks on val maksCollection
        checadorCollection = FirebaseFirestore.getInstance().collection("Checador")
        usuarioCollection = FirebaseFirestore.getInstance().collection("Usuarios")

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estatus_checador)
        initToolbar()
        addMarksListener()
        addMarksListenerTWO()
        swipeRefreshLayout = findViewById(R.id.swipeEstatusChecador)
        swipeRefreshLayout!!.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            addMarksListener()
            swipeRefreshLayout!!.setRefreshing(false);
        })
    }

    /**
     * Listener for peopleCollection
     */
    private fun addMarksListener() {
        var sharedPreference = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE)
        var id_empresa = sharedPreference.getString("id_empresa", "")
        //toast(id_empresa)
        val c = Calendar.getInstance()
        val df = SimpleDateFormat("dd/MM/yyyy")
        val formattedDate = df.format(c.getTime()).toString()

        checadorCollection.whereEqualTo("id_empresa", id_empresa).whereEqualTo("fecha", formattedDate).orderBy("hora").addSnapshotListener { snapshots, error ->
            if (error == null) {
                val changes = snapshots?.documentChanges
                if (changes != null) {
                    //  addChanges(changes)
                    listenerDb()
                }
            } else {
                // toast("Ha ocurrido un error intente de nuevo")
            }
        }
    }

    private fun listenerDb() {
        var sharedPreference = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE)
        var id_empresa = sharedPreference.getString("id_empresa", "")
        //toast(id_empresa)
        val c = Calendar.getInstance()
        val df = SimpleDateFormat("dd/MM/yyyy")
        val formattedDate = df.format(c.getTime()).toString()
        val consul = checadorCollection.whereEqualTo("id_empresa", id_empresa).whereEqualTo("fecha", formattedDate).orderBy("hora")
        //beggin with consult
        consul.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                val itemChecador = ArrayList<Checador>()//lista local de una sola instancia
                for (document in task.result!!) {
                    itemChecador.add(document.toObject(Checador::class.java))//ir agregando los datos a la lista
                }
                eventoList = itemChecador
                Log.w("LISTA", eventoList.toString())
                adapter = EstatusChecadorAdapter(this, R.layout.list_view_estatus_checador, eventoList)
                //listView.btnCerrarEncuesta
                listView.adapter = adapter
            } else {
                Log.w("saasas", "Error getting documents.", task.exception)
            }

        })//end for expression lambdas this very cool


    }


    /**
     * @param changes
     * aqui se hace el recorrido de la coleccion de cloudfirestore
     */
    private fun addChanges(changes: List<DocumentChange>) {
        val itemChecador = ArrayList<Checador>()//lista local de una sola instancia
        for (change in changes) {
            itemChecador.add(change.document.toObject(Checador::class.java))//ir agregando los datos a la lista
        }//una ves agregado los campos mandar a llamar la vista
        eventoList = itemChecador
        Log.w("LISTA", eventoList.toString())
        adapter = EstatusChecadorAdapter(this, R.layout.list_view_estatus_checador, eventoList)
        //listView.btnCerrarEncuesta
        listView.adapter = adapter
    }//end for handler

    private fun addMarksListenerTWO() {
        var sharedPreference = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE)
        var id_empresa = sharedPreference.getString("id_empresa", "")
        checadorCollection.whereEqualTo("id_empresa", id_empresa).orderBy("fecha").orderBy("hora").addSnapshotListener { snapshots, error ->
            if (error == null) {
                val changes = snapshots?.documentChanges
                if (changes != null) {
                    addChangesTwo(changes)
                }
            } else {
                //toast("Ha ocurrido un error intente de nuevo")
            }
        }
    }

    /**
     * @param changes
     * aqui se hace el recorrido de la coleccion de cloudfirestore
     */
    private fun addChangesTwo(changes: List<DocumentChange>) {
        val itemChecadorTwo = ArrayList<Checador>()//lista local de una sola instancia
        for (change in changes) {
            itemChecadorTwo.add(change.document.toObject(Checador::class.java))//ir agregando los datos a la lista
        }//una ves agregado los campos mandar a llamar la vista
        eventoListTwo = itemChecadorTwo

    }//end for handler

    private fun validarmesprimero(mes: String) {
        //we need to handle runtime permission for devices with marshmallow and above
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            //system OS >= Marshmallow(6.0), check permission is enabled or not
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                //permission was not granted, request it
                val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                requestPermissions(permissions, 100)
            } else {
                //permission already granted, call savePdf() method
                savePdf(queMesRecibi(mes), mes)
            }
        } else {
            //system OS < marshmallow, call savePdf() method
            savePdf(queMesRecibi(mes), mes)
        }
    }

    private fun validarmessegundo(mes: String) {
        //we need to handle runtime permission for devices with marshmallow and above
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            //system OS >= Marshmallow(6.0), check permission is enabled or not
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED) {
                //permission was not granted, request it
                val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                requestPermissions(permissions, 100)
            } else {
                //permission already granted, call savePdf() method
                savePdf(queMesRecibi(mes), mes)
            }
        } else {
            //system OS < marshmallow, call savePdf() method
            savePdf(queMesRecibi(mes), mes)
        }
    }

    private fun validarmestercero(mes: String) {
        //we need to handle runtime permission for devices with marshmallow and above
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            //system OS >= Marshmallow(6.0), check permission is enabled or not
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED) {
                //permission was not granted, request it
                val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                requestPermissions(permissions, 100)
            } else {
                //permission already granted, call savePdf() method
                savePdf(queMesRecibi(mes), mes)
            }
        } else {
            //system OS < marshmallow, call savePdf() method
            savePdf(queMesRecibi(mes), mes)

        }
    }

    private fun queMesRecibi(mesRecibido: String): String {
        if (mesRecibido.equals("Enero")) {
            return "01"
        } else if (mesRecibido.equals("Febrero")) {
            return "02"
        } else if (mesRecibido.equals("Marzo")) {
            return "03"
        } else if (mesRecibido.equals("Abril")) {
            return "04"
        } else if (mesRecibido.equals("Mayo")) {
            return "05"
        } else if (mesRecibido.equals("Juio")) {
            return "06"
        } else if (mesRecibido.equals("Julio")) {
            return "07"
        } else if (mesRecibido.equals("Agosto")) {
            return "08"
        } else if (mesRecibido.equals("Septiembre")) {
            return "09"
        } else if (mesRecibido.equals("Octubre")) {
            return "10"
        } else if (mesRecibido.equals("Noviembre")) {
            return "11"
        } else if (mesRecibido.equals("Diciembre")) {
            return "12"
        }
        return ""
    }

    //front end
    //front end only
    //here the front end
    private fun initToolbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Horas de chequeo"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        Tools.setSystemBarColor(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_checador, menu)
        val c = Calendar.getInstance()
        val df = SimpleDateFormat("MM")
        val mes = df.format(c.getTime()).toString()
        menu!!.getItem(0)

        if (mes == "01") {
            menu.getItem(0).setTitle("Enero")
            menu.getItem(1).setVisible(false)
            menu.getItem(2).setVisible(false)
        } else if (mes == "02") {
            menu.getItem(0).setTitle("Febrero")
            menu.getItem(1).setTitle("Enero")
            menu.getItem(1).setVisible(false)
        } else if (mes == "03") {
            menu.getItem(0).setTitle("Marzo")
            menu.getItem(1).setTitle("Febrero")
            menu.getItem(2).setTitle("Enero")
        } else if (mes == "04") {
            menu.getItem(0).setTitle("Abril")
            menu.getItem(1).setTitle("Marzo")
            menu.getItem(2).setTitle("Febrero")
        } else if (mes == "05") {
            menu.getItem(0).setTitle("Mayo")
            menu.getItem(1).setTitle("Abril")
            menu.getItem(2).setTitle("Marzo")
        } else if (mes == "06") {
            menu.getItem(0).setTitle("Junio")
            menu.getItem(1).setTitle("Mayo")
            menu.getItem(2).setTitle("Abril")
        } else if (mes == "07") {
            menu.getItem(0).setTitle("Julio")
            menu.getItem(1).setTitle("Junio")
            menu.getItem(2).setTitle("Mayo")
        } else if (mes == "08") {
            menu.getItem(0).setTitle("Agosto")
            menu.getItem(1).setTitle("Julio")
            menu.getItem(2).setTitle("Junio")
        } else if (mes == "09") {
            menu.getItem(0).setTitle("Septiembre")
            menu.getItem(1).setTitle("Agosto")
            menu.getItem(2).setTitle("Julio")
        } else if (mes == "10") {
            menu.getItem(0).setTitle("Octubre")
            menu.getItem(1).setTitle("Septiembre")
            menu.getItem(2).setTitle("Agosto")
        } else if (mes == "11") {
            menu.getItem(0).setTitle("Noviembre")
            menu.getItem(1).setTitle("Octubre")
            menu.getItem(2).setTitle("Septiembre")
        } else if (mes == "12") {
            menu.getItem(2).setTitle("Diciembre")
            menu.getItem(0).setTitle("Noviembre")
            menu.getItem(1).setTitle("Octubre")
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            goToActivity<DashboarActivity> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        } else if (item!!.itemId == R.id.mesultimo) {
            mesEscogido = item.title.toString()
            validarmesprimero(item.title.toString())
        } else if (item!!.itemId == R.id.mespenultimo) {
            mesEscogido = item.title.toString()
            validarmessegundo(item.title.toString())
        } else if (item!!.itemId == R.id.mesantepenultimo) {
            mesEscogido = item.title.toString()
            validarmestercero(item.title.toString())
        }
        return super.onOptionsItemSelected(item)
    }

    //only on the first case if the user is already permit create pdf
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            STORAGE_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission from popup was granted, call savePdf() method
                    savePdf(queMesRecibi(mesEscogido), mesEscogido)
                } else {
                    Toast.makeText(this, "Permission denied...!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun savePdf(mesRecibido: String, mesNombre: String) {
        //create object of Document class
        val mDoc = Document()
        //pdf file name
        val mFileName = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis())
        //pdf file path
        val mFilePath = Environment.getExternalStorageDirectory().toString() + "/" + mFileName + ".pdf"
        try {

            //get text from EditText i.e. textEt
            //add author of the document (metadata)
            var bandera = 0
            eventoListTwo.forEach {
                var fechaBD = it.fecha
                var mesBD = fechaBD.substring(3, 5)//mm
                var anoDB = fechaBD.substring(6, 10)//yyyy
                val c2 = Calendar.getInstance()
                val df2 = SimpleDateFormat("yyyy")
                val ano2 = df2.format(c2.getTime()).toString()
                if (mesBD.equals(mesRecibido) && ano2.equals(anoDB)) {
                    if (bandera == 0) {
                        //create instance of PdfWriter class
                        PdfWriter.getInstance(mDoc, FileOutputStream(mFilePath))
                        //open the document for writing
                        mDoc.open()
                        mDoc.add(Paragraph("Checador en el mes de " + mesNombre))
                        mDoc.add(Paragraph("_______________________________"))
                        bandera++
                    }
                    mDoc.add(Paragraph("Nombre:" + it.nombre))
                    mDoc.add(Paragraph("Fecha:" + it.fecha))
                    mDoc.add(Paragraph("Hora:" + it.hora))
                    mDoc.add(Paragraph("_________________________________"))
                }
            }
            //close document
            if (bandera == 1) {
                mDoc.close()
                Toast.makeText(this, "$mFileName.pdf\nse  guardo en\n$mFilePath", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "No hay registros de checador en ese mes", Toast.LENGTH_SHORT).show()
            }
            //show file saved message with file name and path
        } catch (e: Exception) {
            //if anything goes wrong causing exception, get and show exception message
            Toast.makeText(this, "No hay registros de checador en ese mes", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        goToActivity<DashboarActivity> {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}
