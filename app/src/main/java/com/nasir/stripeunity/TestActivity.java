package com.nasir.stripeunity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.nasir.forstripe.StripePluginInterface;

public class TestActivity extends Activity {
    private static final String TAG = "TestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Button paymentButton = findViewById(R.id.paymentButton);
        paymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPaymentFlow();
            }
        });
    }

    private void startPaymentFlow() {
        Log.d(TAG, "Launching payment flow...");
        // Add your code to start the payment flow here
        StripePluginInterface stripePlugin = new StripePluginInterface();
        stripePlugin.receiveUnityActivity(this); // Assuming you need to set the Unity activity
        stripePlugin.StartPaymentFlow();
    }
}
