package com.material.tecgurus.paypal

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.alejandrolora.finalapp.goToActivity
import com.material.tecgurus.R
import com.material.tecgurus.drawer.DashboarActivity
import kotlinx.android.synthetic.main.activity_planes_pago.*

/**
 * Created by:
 * @author Abraham Casas Aguilar
 */
class PlanesPagoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_planes_pago)
        btn_Mensual.setOnClickListener {
            goToActivity<PayPalPaymentActivity> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        btn_Anualidad.setOnClickListener {
            goToActivity<PayPalPaymentAnualidadActivity> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
        txtCancelar.setOnClickListener {
            goToActivity<DashboarActivity> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        }
    }

    override fun onBackPressed() {
        goToActivity<DashboarActivity> {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        super.onBackPressed()
    }
}
