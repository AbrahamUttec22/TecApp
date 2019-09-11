package com.material.tecgurus.paypal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.JsonObject;
import com.material.tecgurus.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by:
 *
 * @author Abraha Casas Aguilar
 */
public class PayPalDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_pal_details);
        Intent intent = getIntent();
        try {
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("PaymentDetails"));
            showDetails(jsonObject.getJSONObject("response"), intent.getStringExtra("PaymentAmount"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showDetails(JSONObject response, String paymentAmount) {
        try {
            String id_pago = response.getString("id");
            String estatus_pago = response.getString("state");
            Log.w("DETALLESPAO",""+response.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}