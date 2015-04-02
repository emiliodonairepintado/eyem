package es.uma.inftel.eyemandroid.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TabHost;

import es.uma.inftel.eyemandroid.R;

public class Services extends TabActivity {

	//Actions
	public static final String SCAN = "la.droid.qr.scan";
	//SCAN
	public static final String COMPLETE = "la.droid.qr.complete"; //Default: false
	//Result
	public static final String RESULT = "la.droid.qr.result";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        //Recycled objects
	    Resources res = getResources();
	    TabHost tabHost = getTabHost();
	    TabHost.TabSpec spec;
	    Intent intent;
    }

    /**
     * Display a message stating that QR Droid is requiered, and lets the user download it for free
     * @param activity
     */
    public static void qrDroidRequired( final Activity activity ) {
    	//Apparently, QR Droid is not installed, or it's previous to version 3.5
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setMessage( activity.getString(R.string.qrdroid_missing) )
		       .setCancelable(true)
		       .setNegativeButton( activity.getString(R.string.cancel), new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   dialog.cancel();
		           }
		       })
		       .setPositiveButton( activity.getString(R.string.from_market), new DialogInterface.OnClickListener() {
		    	   public void onClick(DialogInterface dialog, int id) {
		    		   activity.startActivity( new Intent( Intent.ACTION_VIEW, Uri.parse( activity.getString(R.string.url_market) ) ) );
		           }
		       });
		builder.create().show();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
    	super.onConfigurationChanged(newConfig);
    	//Nothing
    }
}