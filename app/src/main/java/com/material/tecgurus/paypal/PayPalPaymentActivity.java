package com.material.tecgurus.paypal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.material.tecgurus.R;
import com.material.tecgurus.activity.payment.PaymentProfile;
import com.material.tecgurus.drawer.DashboarActivity;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

/**
 * Created by:
 *
 * @author Abraham Casas Aguilar
 */
public class PayPalPaymentActivity extends AppCompatActivity {

    //all variables for the paypal
    public static final int PAYPAL_REQUEST_CODE = 7171;

   /* private static PayPalConfiguration config = new PayPalConfiguration().
            environment(PayPalConfiguration.ENVIRONMENT_SANDBOX).//aqui puedes configurar el entorno
            clientId(Config.PAYPAL_CLIENT_ID_SANDBOX);*/

    private static PayPalConfiguration config = new PayPalConfiguration().
            environment(PayPalConfiguration.ENVIRONMENT_PRODUCTION).
            clientId(Config.PAYPAL_CLIENT_ID_PRODUCTION);

    private String costo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_pal_payment);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            costo = extras.getString("costo_mensual");
        }

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);
        processPayment();
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    //in this handler I process the payment
    private void processPayment() {
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(costo), "MXN", "Pago Mensual",
                PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    /**
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    try {
                        String paymentDetails = confirmation.toJSONObject().toString(4);
                        startActivity(new Intent(this, PayPalDetailsKActivity.class)
                                .putExtra("PaymentDetails", paymentDetails)
                                .putExtra("PaymentAmount", costo)
                        );//activity para mostrar si se efectuo el pago
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Cancelado", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, PlanesPagoActivity.class));
                //startActivity(new Intent(this, DashboarActivity.class);
            }
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Toast.makeText(this, "Intenta de nuevo", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        stopService(new Intent(this, PayPalService.class));
        startActivity(new Intent(this, PlanesPagoActivity.class));
        super.onBackPressed();
    }
}