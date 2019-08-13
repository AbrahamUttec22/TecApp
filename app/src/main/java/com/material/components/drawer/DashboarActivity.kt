package com.material.components.drawer

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.LightingColorFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.*
import android.widget.Toast
import com.alejandrolora.finalapp.goToActivity
import com.alejandrolora.finalapp.toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.iid.FirebaseInstanceId
import com.material.components.R
import com.material.components.activity.about.EstatusChecadorActivity
import com.material.components.activity.bottomsheet.UserActivity
import com.material.components.activity.button.ActividadesActivity
import com.material.components.activity.card.*
import com.material.components.activity.dialog.EncuestaActivity
import com.material.components.activity.form.*
import com.material.components.activity.login.LoginCardOverlap
import com.material.components.checador.CheckActivity
import com.material.components.checador.GenerarQrJActivity
import com.material.components.model.Actividades
import com.material.components.utils.Tools
import kotlinx.android.synthetic.main.activity_dashboard_grid_fab.*

/**
 * @author Abraham Casas Aguilar
 */
class DashboarActivity : AppCompatActivity() {

    //get instance of firebase
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    //declare val for save the collection
    private val userCollection: CollectionReference
    //declare val for save the collection
    private val empresaCollection: CollectionReference
    private var name: String = ""

    //init the val for get the collection the Firebase with cloud firestore
    init {
        FirebaseApp.initializeApp(this)
        //save the collection marks on val maksCollection
        userCollection = FirebaseFirestore.getInstance().collection("Usuarios")
        empresaCollection = FirebaseFirestore.getInstance().collection("Empresas")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_dashboar)
        setContentView(R.layout.activity_dashboard_grid_fab)
        getDataUser()
        //apartir de aqui se van a hacer instancias a las ventanas correspondientes

        //Perfil y usuarios (3) YA TIENE PROGRAMACION REACTIVA
        MiPerfil.setOnClickListener {
            //403 USUARIOS,404 EMPRESA
            //Hacer cnosulta para saber que perfil mostrar
            var sharedPreference = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE)
            var rol = sharedPreference.getString("rol", "").toString()
            if (rol.equals("empresa")) {
                //403 USUARIOS,404 EMPRESA
                //Hacer cnosulta para saber que perfil mostrar
                goToActivity<PerfilEmpresaActivity> {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            } else if (rol.equals("administrador") || rol.equals("usuario")) {
                goToActivity<CardOverlap> {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
        }
        PanelUsuarios.setOnClickListener {
            //205
            goToActivity<UserActivity> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        }
        VerActividades.setOnClickListener {
            //301
            goToActivity<ActividadesActivity> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        //Checador (6) no es necesario programacion reactiva
        Checar.setOnClickListener {
            //25002
            goToActivity<CheckActivity> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
        GenerarQR.setOnClickListener {
            //25003
            goToActivity<GenerarQrJActivity> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
        EstatusChecador.setOnClickListener {
            //25004
            goToActivity<EstatusChecadorActivity> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        }

        //Eventos  (9) YA TIENE PROGRAMACION REACTIVA
        AgregarEventos.setOnClickListener {
            //1603
            goToActivity<FormProfileData> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
        VerEventos.setOnClickListener {
            //401
            goToActivity<CardBasic> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
        AdministrarEventos.setOnClickListener {
            //1601
            goToActivity<AdministrarEventoActivity> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        //Encuestas  (12) YA CONTIENE PROGRAMACION REACTIVA
        AgregarEncuesta.setOnClickListener {
            //1604
            goToActivity<AgregarEncuestaActivity> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
        VerEncuestas.setOnClickListener {
            //604
            goToActivity<EncuestaActivity> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
        AdministrarEncuestas.setOnClickListener {
            //1605  (ver estadisticas)
            goToActivity<EstadisticaActivity> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        //Anuncios (15) YA CONTIENE PROGRAMACION REACTIVA
        AgregarAnuncio.setOnClickListener {
            //1602
            goToActivity<AgregarAnuncioActivity> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
        VerAnuncio.setOnClickListener {
            //405
            goToActivity<CardWizardLight> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
        AdministrarAnuncios.setOnClickListener {
            //402
            goToActivity<AdministrarAnunciosActivity> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        }

        CerrarSesion.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Estas seguro de cerrar sesión?").setPositiveButton("Si", DialogInterface.OnClickListener { dialog, id ->
                mAuth.signOut()
                goToActivity<LoginCardOverlap> { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
            }).setNegativeButton("No", DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
                    .show()
        }
        InfoCodigoEmpresa.setOnClickListener {
            showConfirmDialog()
        }
    }

    private fun showConfirmDialog() {
        //the header from dialog
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
        dialog.setContentView(R.layout.dialog_codigo)
        dialog.setCancelable(true)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        (dialog.findViewById<View>(R.id.bt_close) as AppCompatButton).setOnClickListener { v ->
            dialog.dismiss()
        }
        dialog.show()
        dialog.window!!.attributes = lp
    }

    private fun getDataUser() {
        initToolbar()
    }

    /**
     * initToolbar(header)
     */
    @SuppressLint("ResourceType")
    private fun initToolbar() {
        val toolbar = findViewById<View>(R.id.toolbarD) as Toolbar
        //toolbar.setNavigationIcon(R.drawable.ic_menu)
        // val actionBar = supportActionBar
        //  actionBar!!.title = "Hola," + name//aqui iria el nombre del usuario que inicia sesion
        //Esto es para mostrar una fecha de regreso
        //setSupportActionBar(toolbar)
        //supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val email = mAuth.currentUser!!.email.toString()
        val empleado = userCollection.whereEqualTo("email", email)
        //beggin with consult
        empleado.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    val rol = document.get("rol").toString()
                    name = document.get("name").toString()
                    toolbar.title = "Hola, " + name

                    if (rol == "administrador") {
                        MostrarCodigo.text = "Fue creado el 17/06/2019"
                        tituloCodigo.text = "Tg-Admin-Empleados"
                        InfoCodigoEmpresa.setVisibility(View.INVISIBLE)
                    } else {
                        MostrarCodigo.text = "Fue creado el 17/06/2019"
                        tituloCodigo.text = "Tg-Admin-Empleados"
                        InfoCodigoEmpresa.setVisibility(View.INVISIBLE)
                        //deshabilitar funciones
                        val color = Color.parseColor("#D3D3D3")
                        var filter = LightingColorFilter(Color.GRAY, Color.GRAY)

                        PanelUsuarios.isEnabled = false
                        PanelUsuarios.background.setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))
                        //PanelUsuarios.getBackground().mutate().setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

                        AgregarEventos.isEnabled = false
                        AgregarEventos.background.setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

                        //AgregarEventos.getBackground().mutate().setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

                        AdministrarEventos.isEnabled = false
                        AdministrarEventos.background.setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

                        //AdministrarEventos.getBackground().mutate().setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

                        AgregarEncuesta.isEnabled = false
                        AgregarEncuesta.background.setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

                        //AgregarEncuesta.getBackground().mutate().setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

                        AdministrarEncuestas.isEnabled = false
                        AdministrarEncuestas.background.setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

                        //AdministrarEncuestas.getBackground().mutate().setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

                        AgregarAnuncio.isEnabled = false
                        AgregarAnuncio.background.setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

                        //  AgregarAnuncio.getBackground().mutate().setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

                        AdministrarAnuncios.isEnabled = false
                        AdministrarAnuncios.background.setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

                        //AdministrarAnuncios.getBackground().mutate().setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

                        GenerarQR.isEnabled = false
                        GenerarQR.background.setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

                        //GenerarQR.getBackground().mutate().setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

                        EstatusChecador.isEnabled = false
                        EstatusChecador.background.setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

                        //EstatusChecador.getBackground().mutate().setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

                    }
                }

            } else {
                Log.w("saasas", "Error getting documents.", task.exception)
            }
        })//end for expression lambdas this very cool
        //case for empresa
        val empresa = empresaCollection.whereEqualTo("correo", email)
        empresa.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                for (document in task.result!!) {
                     val color = Color.parseColor("#D3D3D3")
                    //var filter = LightingColorFilter(Color.GRAY, Color.GRAY)

                    VerActividades.isEnabled = false
                    VerActividades.background.setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

                    //VerActividades.getBackground().mutate().setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

                    Checar.isEnabled = false
                    Checar.background.setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))
                    //Checar.getBackground().mutate().setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

                    name = document.get("nombre").toString()
                    toolbar.title = "Hola, " + name
                    val id_empresa = document.get("id_empresa").toString()
                    MostrarCodigo.text = id_empresa
                    tituloCodigo.text = "Código Empresa"
                    InfoCodigoEmpresa.text = "¿Qué es esto?"
                }
            } else {
                Log.w("saasas", "Error getting documents.", task.exception)
            }

        })//end for expression lambdas this very cool

        setSupportActionBar(toolbar)
        Tools.setSystemBarColor(this, R.color.colorPrimary)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_setting, menu)//menu de las opciones del toolbar, menu basic
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        } else if (item.itemId == R.id.action_search) {//icono de search
            //Toast.makeText(applicationContext, item.title, Toast.LENGTH_SHORT).show()
            toast("Diste click en search")
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        confirmarCierre()
    }

    private var exitTime: Long = 0

    private fun confirmarCierre() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            Toast.makeText(this, "Presiona de nuevo para salir de la aplicación", Toast.LENGTH_SHORT).show()
            exitTime = System.currentTimeMillis()
        } else {
            finish()
        }
    }
}
