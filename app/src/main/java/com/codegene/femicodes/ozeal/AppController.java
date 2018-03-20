package com.codegene.femicodes.ozeal;

import android.app.Application;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import co.paystack.android.PaystackSdk;

/**
 * Created by Aghedo on 4/20/2017.
 */

public class AppController extends Application {
    String public_key = "pk_test_ad0753d2ecbfe2b45c27bc68334191138c172950";//Public Key Gotten From Settings#Developer/Api on Dashboard

    @Override
    public void onCreate() {
        super.onCreate();

        //PayStack Initialization
        PaystackSdk.initialize(getApplicationContext());
        PaystackSdk.setPublicKey(public_key);//Public Key Initialization

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        DatabaseReference Ref = FirebaseDatabase.getInstance().getReference();
        Ref.keepSynced(true);

    }

}
