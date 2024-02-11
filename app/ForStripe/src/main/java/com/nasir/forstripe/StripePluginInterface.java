package com.nasir.forstripe;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

public class StripePluginInterface {
    public static Activity unityActivity;
    public static String TAG = "PluginInstance";

    public static void receiveUnityActivity(Activity _unityActivity) {
        unityActivity = _unityActivity;
    }

    public int Add(int A, int B) {
        return A + B;
    }

    public void StartPaymentFlow() {
        Log.e(TAG, "Called payment flow... ");
        if (unityActivity != null) {
            Intent intent = new Intent(unityActivity, CheckoutActivity.class);
            unityActivity.startActivity(intent);
        } else {
            Log.e(TAG, "Unity activity is null. Cannot start payment flow.");
        }
    }
}
