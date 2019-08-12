package com.material.components.checador;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.material.components.R;
import com.material.components.drawer.DashboarActivity;
import com.material.components.utils.Tools;

import java.util.Random;

/**
 * @author Abraham
 */
public class GenerarQrJActivity extends AppCompatActivity {

    ImageView imageView;
    String id_empresa = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generar_qr);
        imageView = (ImageView) findViewById(R.id.imgQR);
        SharedPreferences sharedPreference = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE);
        id_empresa = sharedPreference.getString("id_empresa", "");
        ejecutarTarea();
        initToolbar();
    }

    private final int TIEMPO = 2000;
    private static final String TAG = "MainActivity";
    Handler handler = new Handler(); // En esta zona creamos el objeto Handler

    public void ejecutarTarea() {
        handler.postDelayed(new Runnable() {
            public void run() {
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                BitMatrix bitMatrix = null;
                Random r = new Random();
                try {
                    int i1 = r.nextInt(9);
                    String re = String.valueOf(i1);
                    String valor = id_empresa + re;
                    bitMatrix = multiFormatWriter.encode(valor, BarcodeFormat.QR_CODE, 500, 500);
                    String cadena = "";
                    cadena = valor.substring(0, valor.length() - 1);
                  //  Toast.makeText(GenerarQrJActivity.this, cadena, Toast.LENGTH_SHORT).show();
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                    imageView.setImageBitmap(bitmap);
                    handler.postDelayed(this, TIEMPO);
                } catch (WriterException e) {
                    e.printStackTrace();
                }

            }
        }, TIEMPO);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("QR");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            regreso();//poner un intent
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        regreso();
    }

    private void regreso() {
        handler.removeCallbacksAndMessages(null);
        Intent intent = new Intent(this, DashboarActivity.class);
        startActivity(intent);
    }
}