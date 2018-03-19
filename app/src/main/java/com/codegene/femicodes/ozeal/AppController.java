package com.codegene.femicodes.ozeal;

import android.app.Application;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Aghedo on 4/20/2017.
 */

public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        // Toast.makeText(getBaseContext(), "App Controller started", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onCreate: App Controller Started");
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        DatabaseReference Ref = FirebaseDatabase.getInstance().getReference();
        Ref.keepSynced(true);

//        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
//            //All location services are disabled
//            Toast.makeText(getApplicationContext(), "No internet Access", Toast.LENGTH_SHORT).show();
//
//        }else{
//            Toast.makeText(getApplicationContext(), "All Clear boss", Toast.LENGTH_SHORT).show();
//
//        }

    }

}
