package com.nasir.forstripe;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.stripe.android.paymentsheet.*;
import com.stripe.android.PaymentConfiguration;
import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.Handler;

import org.json.JSONException;
import org.json.JSONObject;

public class CheckoutActivity extends AppCompatActivity {
    private static final String TAG = "CheckoutActivity";
    PaymentSheet paymentSheet;
    String paymentIntentClientSecret;
    PaymentSheet.CustomerConfiguration customerConfig;
    private void presentPaymentSheet(JSONObject result) {
        try {
            customerConfig = new PaymentSheet.CustomerConfiguration(
                    result.getString("customer"),
                    result.getString("ephemeralKey")
            );
            paymentIntentClientSecret = result.getString("paymentIntent");
            PaymentConfiguration.init(getApplicationContext(), result.getString("publishableKey"));
            final PaymentSheet.GooglePayConfiguration googlePayConfiguration =
                    new PaymentSheet.GooglePayConfiguration(
                            PaymentSheet.GooglePayConfiguration.Environment.Test,
                            "UK"
                    );
            final PaymentSheet.Configuration configuration = new PaymentSheet.Configuration.Builder("Riwada Limited")
                    .customer(customerConfig)
                    .googlePay(googlePayConfiguration)
                    // Set `allowsDelayedPaymentMethods` to true if your business handles payment methods
                    // delayed notification payment methods like US bank accounts.
                    .allowsDelayedPaymentMethods(false)
                    .build();

            paymentSheet.presentWithPaymentIntent(
                    paymentIntentClientSecret,
                    configuration
            );
        } catch (JSONException e) { /* handle error */
            Log.e(TAG,"presentPaymentSheet:"+e.getMessage());
        }

    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        paymentSheet = new PaymentSheet(this, this::onPaymentSheetResult);
        Fuel.INSTANCE.post("https://outstanding-deploy-a07yl.ampt.app//api/sheet", null).responseString(new Handler<String>() {
            @Override
            public void success(String s) {
                try {
                    Log.d(TAG,"result:"+s);
                    final JSONObject result = new JSONObject(s);
                    presentPaymentSheet(result);
                } catch (JSONException e) { /* handle error */
                    Log.e(TAG,"exception:"+e.getMessage());
                }
            }

            @Override
            public void failure(@NonNull FuelError fuelError) { /* handle error */
             Log.e(TAG,"FuelError:"+fuelError);
            }
        });
    }

    void onPaymentSheetResult(final PaymentSheetResult paymentSheetResult) {
        // implemented in the next steps
        if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            Log.d(TAG, "Canceled");
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            Log.e(TAG, "Got error: ", ((PaymentSheetResult.Failed) paymentSheetResult).getError());
        } else if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            // Display for example, an order confirmation screen
            Log.d(TAG, "Completed");
        }
    }
}