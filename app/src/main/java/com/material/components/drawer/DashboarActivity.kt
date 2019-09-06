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
import android.support.v7.widget.AppCompatSeekBar
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.*
import android.widget.*
import com.alejandrolora.finalapp.goToActivity
import com.alejandrolora.finalapp.toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.material.components.R
import com.material.components.actividadesfragment.GestionActividadesActivity
import com.material.components.actividadesfragmentadmin.*
import com.material.components.activity.about.EstatusChecadorActivity
import com.material.components.activity.bottomsheet.UserActivity
import com.material.components.activity.button.ActividadesActivity
import com.material.components.activity.card.*
import com.material.components.activity.dialog.EncuestaActivity
import com.material.components.activity.form.*
import com.material.components.activity.login.LoginCardOverlap
//import com.material.components.adapter.ActividadesAdapter
import com.material.components.adapter.EncuestaAdapter
import com.material.components.adapter.EventoAdapter
import com.material.components.adapter.UserAdapter
import com.material.components.checador.CheckActivity
import com.material.components.checador.GenerarQrJActivity
import com.material.components.model.*
import com.material.components.utils.Tools
import kotlinx.android.synthetic.main.activity_actividades.*
import kotlinx.android.synthetic.main.activity_actividades.listView
import kotlinx.android.synthetic.main.activity_card_basic.*
import kotlinx.android.synthetic.main.activity_dashboard_administrador.*
import kotlinx.android.synthetic.main.activity_dashboard_empresa.*
import kotlinx.android.synthetic.main.activity_dashboard_usuario.*
import kotlinx.android.synthetic.main.activity_encuesta.*
import kotlinx.android.synthetic.main.activity_user.*
import kotlinx.android.synthetic.main.list_view_usuario.view.*
import java.text.SimpleDateFormat
import java.util.*

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
    var id_empresa = ""
    //declare val for save the collection
    private val eventosCollection: CollectionReference
    //declare val for save the collection
    private val encuestasCollection: CollectionReference
    //declare val for save the collection
    private val anuncioCollection: CollectionReference
    //declare val for save the collection
    private val usuariosCollection: CollectionReference
    //declare val for save the collection
    private val actividadesCollection: CollectionReference
    //declare val for save the collection
    private val detalleEventosCollection: CollectionReference
    //declare val for save the collection
    private val detalleEncuestasCollection: CollectionReference
    //declare val for save the collection
    private val detalleAnuncioCollection: CollectionReference

    //init the val for get the collection the Firebase with cloud firestore
    init {
        FirebaseApp.initializeApp(this)
        //save the collection marks on val maksCollection
        userCollection = FirebaseFirestore.getInstance().collection("Usuarios")
        empresaCollection = FirebaseFirestore.getInstance().collection("Empresas")
        eventosCollection = FirebaseFirestore.getInstance().collection("Eventos")
        encuestasCollection = FirebaseFirestore.getInstance().collection("Encuestas")
        anuncioCollection = FirebaseFirestore.getInstance().collection("Anuncios")
        usuariosCollection = FirebaseFirestore.getInstance().collection("Usuarios")
        actividadesCollection = FirebaseFirestore.getInstance().collection("Actividades")
        //______
        detalleEventosCollection = FirebaseFirestore.getInstance().collection("detalleEventos")
        detalleEncuestasCollection = FirebaseFirestore.getInstance().collection("detalleEncuestas")
        detalleAnuncioCollection = FirebaseFirestore.getInstance().collection("detalleAnuncios")

    }

    private var rol2 = ""
    private var plan_pago = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var sharedPreference = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE)
        var rol = sharedPreference.getString("rol", "").toString()
        var sharedPreferencet = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE)
        id_empresa = sharedPreferencet.getString("id_empresa", "")

        val email = mAuth.currentUser!!.email.toString()
        val empresa = userCollection.whereEqualTo("email", email)
        empresa.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    rol2 = document.get("rol").toString()
                }

                if (rol.equals("empresa")) {
                    setContentView(R.layout.activity_dashboard_empresa)
                    consultasEmpresaNotificaciones()
                    val empresaEstatus = empresaCollection.whereEqualTo("id_empresa", id_empresa)
                    empresaEstatus.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                        if (task.isSuccessful) {
                            for (document in task.result!!) {
                                plan_pago = document.get("estatus").toString()
                            }
                            when (plan_pago) {
                                "mensual" -> {

                                }
                                "pruebainicial" -> {

                                }
                                "gratuita" -> {//empresa
                                    showDialog()
                                    val color = Color.parseColor("#D3D3D3")
                                    MiPerfilE.isEnabled = false
                                    MiPerfilE.background.setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

                                    PanelUsuariosE.isEnabled = false
                                    PanelUsuariosE.background.setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

                                    AgregarEventosE.isEnabled = false
                                    AgregarEventosE.background.setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

                                    AdministrarEventosE.isEnabled = false
                                    AdministrarEventosE.background.setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

                                    AgregarEncuestaE.isEnabled = false
                                    AgregarEncuestaE.background.setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

                                    AdministrarEncuestasE.isEnabled = false
                                    AdministrarEncuestasE.background.setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

                                    AdministradorActividadesAdminE.isEnabled = false
                                    AdministradorActividadesAdminE.background.setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

                                    AgregarAnuncioE.isEnabled = false
                                    AgregarAnuncioE.background.setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

                                    AdministrarAnunciosE.isEnabled = false
                                    AdministrarAnunciosE.background.setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

                                    GenerarQRE.isEnabled = false
                                    GenerarQRE.background.setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

                                    EstatusChecadorE.isEnabled = false
                                    EstatusChecadorE.background.setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))
                                    ////222
                                    MiPerfilE2.isEnabled = false

                                    PanelUsuariosE2.isEnabled = false
                                    AdministradorActividadesAdminE2.isEnabled = false

                                    AgregarEventosE2.isEnabled = false

                                    AdministrarEventosE2.isEnabled = false

                                    AgregarEncuestaE2.isEnabled = false

                                    AdministrarEncuestasE2.isEnabled = false

                                    AgregarAnuncioE2.isEnabled = false

                                    AdministrarAnunciosE2.isEnabled = false

                                    GenerarQRE2.isEnabled = false

                                    EstatusChecadorE2.isEnabled = false

                                }
                                else -> {

                                }
                            }
                        } else {
                            Log.w("saasas", "Error getting documents.", task.exception)
                        }

                    })//end for expression lambdas this very cool


                    //Perfil y usuarios (3) YA TIENE PROGRAMACION REACTIVA
                    MiPerfilE.setOnClickListener {
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
                        } else if (rol2.equals("administrador") || rol2.equals("usuario")) {
                            goToActivity<CardOverlap> {
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            }
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                        }
                    }
                    PanelUsuariosE.setOnClickListener {
                        //205
                        goToActivity<UserActivity> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                    }

                    //Checador (6) no es necesario programacion reactiva
                    GenerarQRE.setOnClickListener {
                        //25003
                        goToActivity<GenerarQrJActivity> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }
                    EstatusChecadorE.setOnClickListener {
                        //25004
                        goToActivity<EstatusChecadorActivity> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                    }

                    AdministradorActividadesAdminE.setOnClickListener {
                        goToActivity<GestionActividadesAActivity> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }

                    //Eventos  (9) YA TIENE PROGRAMACION REACTIVA
                    AgregarEventosE.setOnClickListener {
                        //1603
                        goToActivity<FormProfileData> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }
                    VerEventosE.setOnClickListener {
                        //401
                        goToActivity<CardBasic> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }
                    AdministrarEventosE.setOnClickListener {
                        //1601
                        goToActivity<AdministrarEventoActivity> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }

                    //Encuestas  (12) YA CONTIENE PROGRAMACION REACTIVA
                    AgregarEncuestaE.setOnClickListener {
                        //1604
                        goToActivity<AgregarEncuestaActivity> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }
                    VerEncuestasE.setOnClickListener {
                        //604
                        goToActivity<EncuestaActivity> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }
                    AdministrarEncuestasE.setOnClickListener {
                        //1605  (ver estadisticas)
                        goToActivity<EstadisticaActivity> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }

                    //Anuncios (15) YA CONTIENE PROGRAMACION REACTIVA
                    AgregarAnuncioE.setOnClickListener {
                        //1602
                        goToActivity<AgregarAnuncioActivity> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }
                    VerAnuncioE.setOnClickListener {
                        //405
                        goToActivity<CardWizardLight> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }
                    AdministrarAnunciosE.setOnClickListener {
                        //402
                        goToActivity<AdministrarAnunciosActivity> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                    }

                    CerrarSesionE.setOnClickListener {
                        val builder = AlertDialog.Builder(this)
                        builder.setMessage("Estas seguro de cerrar sesión?").setPositiveButton("Si", DialogInterface.OnClickListener { dialog, id ->
                            mAuth.signOut()
                            goToActivity<LoginCardOverlap> { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
                        }).setNegativeButton("No", DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
                                .show()
                    }
                    InfoCodigoEmpresaE.setOnClickListener {
                        showConfirmDialog()
                    }
                    //______________________________________________________________________
                    MiPerfilE2.setOnClickListener {
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
                        } else if (rol2.equals("administrador") || rol2.equals("usuario")) {
                            goToActivity<CardOverlap> {
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            }
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                        }
                    }
                    PanelUsuariosE2.setOnClickListener {
                        //205
                        goToActivity<UserActivity> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                    }

                    //Checador (6) no es necesario programacion reactiva
                    GenerarQRE2.setOnClickListener {
                        //25003
                        goToActivity<GenerarQrJActivity> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }
                    EstatusChecadorE2.setOnClickListener {
                        //25004
                        goToActivity<EstatusChecadorActivity> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                    }

                    AdministradorActividadesAdminE2.setOnClickListener {
                        goToActivity<GestionActividadesAActivity> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }

                    //Eventos  (9) YA TIENE PROGRAMACION REACTIVA
                    AgregarEventosE2.setOnClickListener {
                        //1603
                        goToActivity<FormProfileData> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }
                    VerEventosE2.setOnClickListener {
                        //401
                        goToActivity<CardBasic> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }
                    AdministrarEventosE2.setOnClickListener {
                        //1601
                        goToActivity<AdministrarEventoActivity> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }

                    //Encuestas  (12) YA CONTIENE PROGRAMACION REACTIVA
                    AgregarEncuestaE2.setOnClickListener {
                        //1604
                        goToActivity<AgregarEncuestaActivity> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }
                    VerEncuestasE2.setOnClickListener {
                        //604
                        goToActivity<EncuestaActivity> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }
                    AdministrarEncuestasE2.setOnClickListener {
                        //1605  (ver estadisticas)
                        goToActivity<EstadisticaActivity> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }

                    //Anuncios (15) YA CONTIENE PROGRAMACION REACTIVA
                    AgregarAnuncioE2.setOnClickListener {
                        //1602
                        goToActivity<AgregarAnuncioActivity> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }
                    VerAnuncioE2.setOnClickListener {
                        //405
                        goToActivity<CardWizardLight> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }
                    AdministrarAnunciosE2.setOnClickListener {
                        //402
                        goToActivity<AdministrarAnunciosActivity> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                    }

                    CerrarSesionE2.setOnClickListener {
                        val builder = AlertDialog.Builder(this)
                        builder.setMessage("Estas seguro de cerrar sesión?").setPositiveButton("Si", DialogInterface.OnClickListener { dialog, id ->
                            mAuth.signOut()
                            goToActivity<LoginCardOverlap> { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
                        }).setNegativeButton("No", DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
                                .show()
                    }
                } else if (rol2.equals("usuario")) {
                    setContentView(R.layout.activity_dashboard_usuario)
                    MiPerfilU.isEnabled = false
                    MiPerfilU2.isEnabled = false
                    val empresaEstatus = empresaCollection.whereEqualTo("id_empresa", id_empresa)
                    empresaEstatus.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                        if (task.isSuccessful) {
                            for (document in task.result!!) {
                                plan_pago = document.get("estatus").toString()
                            }
                            when (plan_pago) {
                                "mensual" -> {

                                }
                                "pruebainicial" -> {

                                }
                                "gratuita" -> {//empresa
                                    showDialog()
                                    val color = Color.parseColor("#D3D3D3")
                                    MiPerfilU.isEnabled = false
                                    MiPerfilU.background.setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))
                                    ////222
                                    ChecarU.isEnabled = false
                                    ChecarU.background.setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))
                                    ChecarU2.isEnabled = false

                                    MiPerfilU2.isEnabled = false
                                }
                                else -> {

                                }
                            }
                        } else {
                            Log.w("saasas", "Error getting documents.", task.exception)
                        }

                    })//end for expression lambdas this very cool

                    consultasUsuarioNotificaciones()




                    //Perfil y usuarios (3) YA TIENE PROGRAMACION REACTIVA
                    MiPerfilU.setOnClickListener {
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
                        } else if (rol2.equals("administrador") || rol2.equals("usuario")) {
                            goToActivity<CardOverlap> {
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            }
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                        }
                    }
                    VerActividadesU.setOnClickListener {
                        //301
                        /*goToActivity<ActividadesActivity> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)*/

                        goToActivity<GestionActividadesActivity> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }

                    //Checador (6) no es necesario programacion reactiva
                    ChecarU.setOnClickListener {
                        //25002
                        goToActivity<CheckActivity> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }

                    //Eventos  (9) YA TIENE PROGRAMACION REACTIVA
                    VerEventosU.setOnClickListener {
                        //401
                        goToActivity<CardBasic> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }

                    //Encuestas  (12) YA CONTIENE PROGRAMACION REACTIVA
                    VerEncuestasU.setOnClickListener {
                        //604
                        goToActivity<EncuestaActivity> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }

                    //Anuncios (15) YA CONTIENE PROGRAMACION REACTIVA
                    VerAnuncioU.setOnClickListener {
                        //405
                        goToActivity<CardWizardLight> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }

                    CerrarSesionU.setOnClickListener {
                        val builder = AlertDialog.Builder(this)
                        builder.setMessage("Estas seguro de cerrar sesión?").setPositiveButton("Si", DialogInterface.OnClickListener { dialog, id ->
                            mAuth.signOut()
                            goToActivity<LoginCardOverlap> { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
                        }).setNegativeButton("No", DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
                                .show()
                    }
                    //______________________________________________________________________
                    //Perfil y usuarios (3) YA TIENE PROGRAMACION REACTIVA
                    MiPerfilU2.setOnClickListener {
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
                        } else if (rol2.equals("administrador") || rol2.equals("usuario")) {
                            goToActivity<CardOverlap> {
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            }
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                        }
                    }
                    VerActividadesU2.setOnClickListener {
                        //301
                        /*goToActivity<ActividadesActivity> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)*/

                        goToActivity<GestionActividadesActivity> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }

                    //Checador (6) no es necesario programacion reactiva
                    ChecarU2.setOnClickListener {
                        //25002
                        goToActivity<CheckActivity> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }

                    //Eventos  (9) YA TIENE PROGRAMACION REACTIVA
                    VerEventosU2.setOnClickListener {
                        //401
                        goToActivity<CardBasic> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }

                    //Encuestas  (12) YA CONTIENE PROGRAMACION REACTIVA
                    VerEncuestasU2.setOnClickListener {
                        //604
                        goToActivity<EncuestaActivity> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }

                    //Anuncios (15) YA CONTIENE PROGRAMACION REACTIVA
                    VerAnuncioU2.setOnClickListener {
                        //405
                        goToActivity<CardWizardLight> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }

                    CerrarSesionU2.setOnClickListener {
                        val builder = AlertDialog.Builder(this)
                        builder.setMessage("Estas seguro de cerrar sesión?").setPositiveButton("Si", DialogInterface.OnClickListener { dialog, id ->
                            mAuth.signOut()
                            goToActivity<LoginCardOverlap> { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
                        }).setNegativeButton("No", DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
                                .show()
                    }
                } else if (rol2.equals("administrador")) {
                    setContentView(R.layout.activity_dashboard_administrador)
                    consultasAdministradorNotificaciones()
                    val empresaEstatus = empresaCollection.whereEqualTo("id_empresa", id_empresa)
                    empresaEstatus.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                        if (task.isSuccessful) {
                            for (document in task.result!!) {
                                plan_pago = document.get("estatus").toString()
                            }
                            when (plan_pago) {
                                "mensual" -> {

                                }
                                "pruebainicial" -> {

                                }
                                "gratuita" -> {//empresa
                                    showDialog()
                                    val color = Color.parseColor("#D3D3D3")
                                    MiPerfilA.isEnabled = false
                                    MiPerfilA.background.setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

                                    PanelUsuariosA.isEnabled = false
                                    PanelUsuariosA.background.setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

                                    AgregarEventosA.isEnabled = false
                                    AgregarEventosA.background.setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

                                    AdministrarEventosA.isEnabled = false
                                    AdministrarEventosA.background.setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

                                    AgregarEncuestaA.isEnabled = false
                                    AgregarEncuestaA.background.setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

                                    AdministrarEncuestasA.isEnabled = false
                                    AdministrarEncuestasA.background.setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

                                    AdministradorActividadesAdmin.isEnabled = false
                                    AdministradorActividadesAdmin.background.setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

                                    AgregarAnuncioA.isEnabled = false
                                    AgregarAnuncioA.background.setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

                                    AdministrarAnunciosA.isEnabled = false
                                    AdministrarAnunciosA.background.setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

                                    GenerarQRA.isEnabled = false
                                    GenerarQRA.background.setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

                                    ChecarA.isEnabled = false
                                    ChecarA.background.setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))


                                    EstatusChecadorA.isEnabled = false
                                    EstatusChecadorA.background.setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))
                                    ////222
                                    AdministradorActividadesAdmin2.isEnabled = false

                                    MiPerfilA2.isEnabled = false
                                    ChecarA2.isEnabled = false

                                    PanelUsuariosA2.isEnabled = false

                                    AgregarEventosA2.isEnabled = false

                                    AdministrarEventosA2.isEnabled = false

                                    AgregarEncuestaA2.isEnabled = false

                                    AdministrarEncuestasA2.isEnabled = false

                                    AgregarAnuncioA2.isEnabled = false

                                    AdministrarAnunciosA2.isEnabled = false

                                    GenerarQRA2.isEnabled = false

                                    EstatusChecadorA2.isEnabled = false

                                }
                                else -> {

                                }
                            }
                        } else {
                            Log.w("saasas", "Error getting documents.", task.exception)
                        }

                    })//end for expression lambdas this very cool

                    //Perfil y usuarios (3) YA TIENE PROGRAMACION REACTIVA
                    MiPerfilA.setOnClickListener {
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
                        } else if (rol2.equals("administrador") || rol2.equals("usuario")) {
                            goToActivity<CardOverlap> {
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            }
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                        }
                    }
                    PanelUsuariosA.setOnClickListener {
                        //205
                        goToActivity<UserActivity> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                    }
                    VerActividadesA.setOnClickListener {
                        //301
                        /* goToActivity<ActividadesActivity> {
                             flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                         }
                         overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)*/
                        goToActivity<GestionActividadesActivity> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }

                    //Checador (6) no es necesario programacion reactiva
                    ChecarA.setOnClickListener {
                        //25002
                        goToActivity<CheckActivity> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }
                    GenerarQRA.setOnClickListener {
                        //25003
                        goToActivity<GenerarQrJActivity> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }
                    EstatusChecadorA.setOnClickListener {
                        //25004
                        goToActivity<EstatusChecadorActivity> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                    }

                    AdministradorActividadesAdmin.setOnClickListener {
                        goToActivity<GestionActividadesAActivity> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }

                    //Eventos  (9) YA TIENE PROGRAMACION REACTIVA
                    AgregarEventosA.setOnClickListener {
                        //1603
                        goToActivity<FormProfileData> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }
                    VerEventosA.setOnClickListener {
                        //401
                        goToActivity<CardBasic> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }
                    AdministrarEventosA.setOnClickListener {
                        //1601
                        goToActivity<AdministrarEventoActivity> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }

                    //Encuestas  (12) YA CONTIENE PROGRAMACION REACTIVA
                    AgregarEncuestaA.setOnClickListener {
                        //1604
                        goToActivity<AgregarEncuestaActivity> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }
                    VerEncuestasA.setOnClickListener {
                        //604
                        goToActivity<EncuestaActivity> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }
                    AdministrarEncuestasA.setOnClickListener {
                        //1605  (ver estadisticas)
                        goToActivity<EstadisticaActivity> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }

                    //Anuncios (15) YA CONTIENE PROGRAMACION REACTIVA
                    AgregarAnuncioA.setOnClickListener {
                        //1602
                        goToActivity<AgregarAnuncioActivity> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }
                    VerAnuncioA.setOnClickListener {
                        //405
                        goToActivity<CardWizardLight> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }
                    AdministrarAnunciosA.setOnClickListener {
                        //402
                        goToActivity<AdministrarAnunciosActivity> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                    }

                    CerrarSesionA.setOnClickListener {
                        val builder = AlertDialog.Builder(this)
                        builder.setMessage("Estas seguro de cerrar sesión?").setPositiveButton("Si", DialogInterface.OnClickListener { dialog, id ->
                            mAuth.signOut()
                            goToActivity<LoginCardOverlap> { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
                        }).setNegativeButton("No", DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
                                .show()
                    }
                    //______________________________________________________________________
                    MiPerfilA2.setOnClickListener {
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
                        } else if (rol2.equals("administrador") || rol2.equals("usuario")) {
                            goToActivity<CardOverlap> {
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            }
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                        }
                    }
                    PanelUsuariosA2.setOnClickListener {
                        //205
                        goToActivity<UserActivity> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                    }
                    VerActividadesA2.setOnClickListener {
                        //301
                        /* goToActivity<ActividadesActivity> {
                             flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                         }
                         overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)*/
                        goToActivity<GestionActividadesActivity> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }

                    //Checador (6) no es necesario programacion reactiva
                    ChecarA2.setOnClickListener {
                        //25002
                        goToActivity<CheckActivity> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }
                    GenerarQRA2.setOnClickListener {
                        //25003
                        goToActivity<GenerarQrJActivity> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }
                    EstatusChecadorA2.setOnClickListener {
                        //25004
                        goToActivity<EstatusChecadorActivity> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                    }

                    AdministradorActividadesAdmin2.setOnClickListener {
                        goToActivity<GestionActividadesAActivity> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }

                    //Eventos  (9) YA TIENE PROGRAMACION REACTIVA
                    AgregarEventosA2.setOnClickListener {
                        //1603
                        goToActivity<FormProfileData> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }
                    VerEventosA2.setOnClickListener {
                        //401
                        goToActivity<CardBasic> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }
                    AdministrarEventosA2.setOnClickListener {
                        //1601
                        goToActivity<AdministrarEventoActivity> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }

                    //Encuestas  (12) YA CONTIENE PROGRAMACION REACTIVA
                    AgregarEncuestaA2.setOnClickListener {
                        //1604
                        goToActivity<AgregarEncuestaActivity> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }
                    VerEncuestasA2.setOnClickListener {
                        //604
                        goToActivity<EncuestaActivity> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }
                    AdministrarEncuestasA2.setOnClickListener {
                        //1605  (ver estadisticas)
                        goToActivity<EstadisticaActivity> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }

                    //Anuncios (15) YA CONTIENE PROGRAMACION REACTIVA
                    AgregarAnuncioA2.setOnClickListener {
                        //1602
                        goToActivity<AgregarAnuncioActivity> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }
                    VerAnuncioA2.setOnClickListener {
                        //405
                        goToActivity<CardWizardLight> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }
                    AdministrarAnunciosA2.setOnClickListener {
                        //402
                        goToActivity<AdministrarAnunciosActivity> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                    }

                    CerrarSesionA2.setOnClickListener {
                        val builder = AlertDialog.Builder(this)
                        builder.setMessage("Estas seguro de cerrar sesión?").setPositiveButton("Si", DialogInterface.OnClickListener { dialog, id ->
                            mAuth.signOut()
                            goToActivity<LoginCardOverlap> { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
                        }).setNegativeButton("No", DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
                                .show()
                    }
                }
                getDataUser()
            } else {
                Log.w("saasas", "Error getting documents.", task.exception)
            }

        })//end for expression lambdas this very cool

        //getDataUser()
        //apartir de aqui se van a hacer instancias a las ventanas correspondientes
    }

    private fun consultasEmpresaNotificaciones() {
        var email_mio = mAuth.currentUser!!.email.toString()

        val consultaUsuario = userCollection.whereEqualTo("id_empresa", id_empresa)
        //beggin with consult
        consultaUsuario.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                val itemUsuario = ArrayList<Usuario>()//lista local de una sola instancia
                var con = 0
                for (document in task.result!!) {
                    con++
                }
                if (con == 0) {
                    EmpresaUsuariosN.text = "0"
                } else {
                    EmpresaUsuariosN.text = con.toString()
                }
            } else {
                Log.w("saasas", "Error getting documents.", task.exception)
            }

        })//end for expression lambdas this very cool
        //____________________________________________________________________
        /*    val consultaEvento = eventosCollection.whereEqualTo("id_empresa", id_empresa)
            //beggin with consult
            consultaEvento.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                if (task.isSuccessful) {
                    var con = 0
                    for (document in task.result!!) {
                        var fechaBD = document.get("fecha").toString()
                        var diaBD = fechaBD.substring(0, 2).toInt()//dd
                        var mesBD = fechaBD.substring(3, 5).toInt()//mm
                        var anoBD = fechaBD.substring(6, 8).toInt()//yyyy

                        val c = Calendar.getInstance()
                        val df = SimpleDateFormat("dd/MM/yy")
                        val fechaCalendar = df.format(c.getTime()).toString()
                        var diaCalendar = fechaCalendar.substring(0, 2).toInt()//dd
                        var mesCalendar = fechaCalendar.substring(3, 5).toInt()//mm
                        var anoCalendar = fechaCalendar.substring(6, 8).toInt()//yyyy

                        if (diaCalendar <= diaBD && mesCalendar <= mesBD && anoCalendar <= anoBD) {
                            con++
                        }
                    }
                    if (con == 0) {
                        EmpresaVerEventoN.text = "0"
                    } else {
                        EmpresaVerEventoN.text = con.toString()
                    }

                } else {
                    Log.w("saasas", "Error getting documents.", task.exception)
                }

            })//end for expression lambdas this very cool
            */

        val consultaEvento = detalleEventosCollection.whereEqualTo("id_empresa", id_empresa).whereEqualTo("correo_usuario", email_mio).whereEqualTo("estatus", "0")
        //beggin with consult
        consultaEvento.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                var con = 0
                for (document in task.result!!) {
                    con++
                }
                if (con == 0) {
                    EmpresaVerEventoN.text = "0"
                } else {
                    EmpresaVerEventoN.text = con.toString()
                }

            } else {
                Log.w("saasas", "Error getting documents.", task.exception)
            }

        })//end for expression lambdas this very cool

        //____________________________________________________________________
        val consultaEncuestas = detalleEncuestasCollection.whereEqualTo("id_empresa", id_empresa).whereEqualTo("correo_usuario", email_mio).whereEqualTo("estatus", "0")
        //beggin with consult
        consultaEncuestas.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                var con = 0
                for (document in task.result!!) {
                    con++
                }
                if (con == 0) {
                    EmpresaVerEncuestaN.text = "0"

                } else {
                    EmpresaVerEncuestaN.text = con.toString()
                }
            } else {
                Log.w("saasas", "Error getting documents.", task.exception)
            }

        })//end for expression lambdas this very cool
        //____________________________________________________________________

        val anuncioConsulta = detalleAnuncioCollection.whereEqualTo("id_empresa", id_empresa).whereEqualTo("correo_usuario", email_mio).whereEqualTo("estatus", "0")
        //beggin with consult
        anuncioConsulta.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                var con = 0
                for (document in task.result!!) {
                    con++
                }
                if (con == 0) {
                    EmpresaVerAnuncioN.text = "0"
                } else {
                    EmpresaVerAnuncioN.text = con.toString()
                }
            } else {
                Log.w("saasas", "Error getting documents.", task.exception)
            }

        })//end for expression lambdas this very cool

    }

    private fun consultasAdministradorNotificaciones() {
        var email_mio = mAuth.currentUser!!.email.toString()
        val consultaUsuario = userCollection.whereEqualTo("id_empresa", id_empresa)
        //beggin with consult
        consultaUsuario.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                val itemUsuario = ArrayList<Usuario>()//lista local de una sola instancia
                var con = 0
                for (document in task.result!!) {
                    con++
                }
                if (con == 0) {
                    AdministradorUsuariosN.text = "0"
                } else {
                    AdministradorUsuariosN.text = con.toString()
                }
            } else {
                Log.w("saasas", "Error getting documents.", task.exception)
            }

        })//end for expression lambdas this very cool
        //____________________________________________________________________
        var email = mAuth.currentUser!!.email.toString()
        val consultaActividad = actividadesCollection.whereEqualTo("correo", email).whereEqualTo("estatus", "actividades")
        //beggin with consult
        consultaActividad.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                var con = 0
                for (document in task.result!!) {
                    con++
                }
                if (con == 0) {
                    AdministradorActividadesN.text = "0"

                } else {
                    AdministradorActividadesN.text = con.toString()
                }
            } else {
                Log.w("saasas", "Error getting documents.", task.exception)
            }

        })//end for expression lambdas this very cool
        //____________________________________________________________________
        val consultaEvento = detalleEventosCollection.whereEqualTo("id_empresa", id_empresa).whereEqualTo("correo_usuario", email_mio).whereEqualTo("estatus", "0")
        //beggin with consult
        consultaEvento.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                var con = 0
                for (document in task.result!!) {
                    con++

                }
                if (con == 0) {
                    AdministradorVerEventosN.text = "0"

                } else {
                    AdministradorVerEventosN.text = con.toString()
                }

            } else {
                Log.w("saasas", "Error getting documents.", task.exception)
            }

        })//end for expression lambdas this very cool
        //____________________________________________________________________
        val consultaEncuestas = detalleEncuestasCollection.whereEqualTo("id_empresa", id_empresa).whereEqualTo("correo_usuario", email_mio).whereEqualTo("estatus", "0")
        //beggin with consult
        consultaEncuestas.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                var con = 0
                for (document in task.result!!) {
                    con++
                }
                if (con == 0) {
                    AdministradorVerEncuestasN.text = "0"

                } else {
                    AdministradorVerEncuestasN.text = con.toString()
                }
            } else {
                Log.w("saasas", "Error getting documents.", task.exception)
            }

        })//end for expression lambdas this very cool
        //____________________________________________________________________

        val anuncioConsulta = detalleAnuncioCollection.whereEqualTo("id_empresa", id_empresa).whereEqualTo("correo_usuario", email_mio).whereEqualTo("estatus", "0")
        //beggin with consult
        anuncioConsulta.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                var con = 0
                for (document in task.result!!) {
                    con++
                }
                if (con == 0) {
                    AdministradorVerAnunciosN.text = "0"
                } else {
                    AdministradorVerAnunciosN.text = con.toString()
                }
            } else {
                Log.w("saasas", "Error getting documents.", task.exception)
            }

        })//end for expression lambdas this very cool

    }

    private fun consultasUsuarioNotificaciones() {
        var email_mio = mAuth.currentUser!!.email.toString()
        //____________________________________________________________________
        var email = mAuth.currentUser!!.email.toString()
        val consultaActividad = actividadesCollection.whereEqualTo("correo", email).whereEqualTo("estatus", "actividades")
        //beggin with consult
        consultaActividad.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                var con = 0
                for (document in task.result!!) {
                    con++
                }
                if (con == 0) {
                    UsuarioVerActividadesN.text = "0"

                } else {
                    UsuarioVerActividadesN.text = con.toString()
                }
            } else {
                Log.w("saasas", "Error getting documents.", task.exception)
            }

        })//end for expression lambdas this very cool
        //____________________________________________________________________
        val consultaEvento = detalleEventosCollection.whereEqualTo("id_empresa", id_empresa).whereEqualTo("correo_usuario", email_mio).whereEqualTo("estatus", "0")
        //beggin with consult
        consultaEvento.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                var con = 0
                for (document in task.result!!) {
                    con++

                }
                if (con == 0) {
                    UsuarioVerEventosN.text = "0"

                } else {
                    UsuarioVerEventosN.text = con.toString()
                }

            } else {
                Log.w("saasas", "Error getting documents.", task.exception)
            }

        })//end for expression lambdas this very cool
        //____________________________________________________________________
        val consultaEncuestas = detalleEncuestasCollection.whereEqualTo("id_empresa", id_empresa).whereEqualTo("correo_usuario", email_mio).whereEqualTo("estatus", "0")
        //beggin with consult
        consultaEncuestas.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                var con = 0
                for (document in task.result!!) {
                    con++
                }
                if (con == 0) {
                    UsuarioVerEncuestasN.text = "0"

                } else {
                    UsuarioVerEncuestasN.text = con.toString()
                }
            } else {
                Log.w("saasas", "Error getting documents.", task.exception)
            }

        })//end for expression lambdas this very cool
        //____________________________________________________________________

        val anuncioConsulta = detalleAnuncioCollection.whereEqualTo("id_empresa", id_empresa).whereEqualTo("correo_usuario", email_mio).whereEqualTo("estatus", "0")
        //beggin with consult
        anuncioConsulta.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                var con = 0
                for (document in task.result!!) {
                    con++
                }
                if (con == 0) {
                    UsuarioVerAnunciosN.text = "0"

                } else {
                    UsuarioVerAnunciosN.text = con.toString()
                }
            } else {
                Log.w("saasas", "Error getting documents.", task.exception)
            }

        })//end for expression lambdas this very cool
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

    private fun showDialog() {
        //the header from dialog
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
        dialog.setContentView(R.layout.dialog_gratuita)
        dialog.setCancelable(true)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        //in this code I get the information on cloud firestore
        var txt1 = (dialog.findViewById<View>(R.id.cerrarGratuita) as TextView)
        txt1.setOnClickListener {
            dialog.dismiss()
        }
        //END FOR THE BACKEND ON SHOW
        (dialog.findViewById<View>(R.id.bt_close) as ImageButton).setOnClickListener { dialog.dismiss() }
        dialog.show()
        dialog.window!!.attributes = lp
    }

    /**
     * initToolbar(header)
     */
    @SuppressLint("ResourceType")
    private fun initToolbar() {
        val toolbar = findViewById<View>(R.id.toolbarD) as Toolbar
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
                        // MostrarCodigoA.text = "Fue creado el 17/06/2019"
                        //tituloCodigoA.text = "Hola! Comunícate"
                    } else {
                        //MostrarCodigoU.text = "Fue creado el 17/06/2019"
                        //tituloCodigoU.text = "Hola! Comunícate"
                        //deshabilitar funciones
                        val color = Color.parseColor("#D3D3D3")
                        var filter = LightingColorFilter(Color.GRAY, Color.GRAY)

                        /*PanelUsuarios.isEnabled = false
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
    */
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

                    /* VerActividades.isEnabled = false
                     VerActividades.background.setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

                     //VerActividades.getBackground().mutate().setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

                     Checar.isEnabled = false
                     Checar.background.setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))
                     //Checar.getBackground().mutate().setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))
    */
                    name = document.get("nombre").toString()
                    toolbar.title = "Hola, " + name
                    val id_empresa = document.get("id_empresa").toString()
                    MostrarCodigoE.text = id_empresa
                    tituloCodigoE.text = "Código Empresa"
                    InfoCodigoEmpresaE.text = "¿Qué es esto?"
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