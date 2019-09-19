package com.material.tecgurus.checador
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.material.tecgurus.R
import com.material.tecgurus.utils.Tools

/**
 * @author Abraham Casas Aguilar
 */
class GenerarQRActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generar_qr)
        initToolbar()
        var sharedPreference = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE)
        var id_empresa = sharedPreference.getString("id_empresa", "")
        /*     Handler().postDelayed({
                 val multiFormatWriter = MultiFormatWriter()
                 val bitMatrix = multiFormatWriter.encode("avervavqwewer", BarcodeFormat.QR_CODE, 500, 500)
                 val barcodeEncoder = BarcodeEncoder()
                 val bitmap = barcodeEncoder.createBitmap(bitMatrix)
                 //Suponiendo que tienes un ImageView con el id ivCodigoGenerado
                 toast("Hola")
                 imgQR.setImageBitmap(bitmap)
             }, 10)
     */

    }

    private fun initToolbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.setNavigationIcon(R.drawable.ic_menu)
        setSupportActionBar(toolbar)
        supportActionBar!!.setTitle("QR")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        Tools.setSystemBarColor(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search_setting, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

}