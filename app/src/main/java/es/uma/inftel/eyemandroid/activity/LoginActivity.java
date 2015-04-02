package es.uma.inftel.eyemandroid.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import es.uma.inftel.eyemandroid.R;
import es.uma.inftel.eyemandroid.asynctask.FindUserByEmailPasswordAsyncTask;
import es.uma.inftel.eyemandroid.asynctask.InsertUserAsyncTask;
import es.uma.inftel.eyemandroid.dialog.DialogManager;
import es.uma.inftel.eyemandroid.entity.Usuario;
import es.uma.inftel.eyemandroid.googleapi.GoogleApiClientManager;
import es.uma.inftel.eyemandroid.util.Services;

public class LoginActivity extends Activity implements GoogleApiClientManager.ConnectionCallback, DialogManager {

    private static final String TAG = "LoginActivity";
    private static final int ACTIVITY_RESULT_QR_DRDROID = 0;

    private Usuario usuario;
    private GoogleApiClientManager googleApiClientManager;
    private ProgressDialog progressDialog;


    /**
     * Called when the activity is starting. Restores the activity state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usuario = new Usuario();
        googleApiClientManager = new GoogleApiClientManager(this, this);
        googleApiClientManager.restoreResolutionState(savedInstanceState);

        //SCAN QR
        final ImageButton button = (ImageButton) findViewById(R.id.button_scan);
        button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent qrDroid = new Intent( Services.SCAN );
                qrDroid.putExtra( Services.COMPLETE , true);
                try {
                    startActivityForResult(qrDroid, ACTIVITY_RESULT_QR_DRDROID);
                } catch (ActivityNotFoundException activity) {
                    Services.qrDroidRequired(LoginActivity.this);
                }
            }
        });

    }

    @Override
    public void onConnected(GoogleApiClient googleApiClient) {
        String email = Plus.AccountApi.getAccountName(googleApiClient);
        Person personLog = Plus.PeopleApi.getCurrentPerson(googleApiClient);
        if (personLog != null) {
            usuario.setNombre(personLog.getDisplayName().toString());
            usuario.setEmail(email);
            usuario.setImagen(personLog.getImage().getUrl());
            if (personLog.hasCover() && personLog.getCover().hasCoverPhoto()) {
                usuario.setImagenCover(personLog.getCover().getCoverPhoto().getUrl());
            }
            new InsertUserAsyncTask(this, googleApiClientManager, this).execute(usuario);
        }
    }

    /**
     * Called when the Activity is made visible.
     * A connection to Play Services need to be initiated as
     * soon as the activity is visible. Registers {@code ConnectionCallbacks}
     * and {@code OnConnectionFailedListener} on the
     * activities itself.
     */
    public void logGoogle(View view) {
        createDialog();
        showDialog();
        googleApiClientManager.createConnection();
    }

    /**
     * Called when activity gets invisible. Connection to Play Services needs to
     * be disconnected as soon as an activity is invisible.
     */
    @Override
    protected void onStop() {
        googleApiClientManager.stopConnection();
        dismissDialog();
        super.onStop();
    }

    /**
     * Saves the resolution state.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        googleApiClientManager.saveResolutionState(outState);
    }

    /**
     * Handles Google Play Services resolution callbacks.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        googleApiClientManager.onResult(requestCode, resultCode, data);

        if( ACTIVITY_RESULT_QR_DRDROID==requestCode && null!=data && data.getExtras()!=null ) {
            //Read result from QR Droid (it's stored in la.droid.qr.result)
            String result = data.getExtras().getString(Services.RESULT);

            String gmailToken = "@gmail.com";
            int index = result.indexOf(gmailToken);
            index += gmailToken.length();

            String email = result.substring(0, index);
            String password = result.substring(index);
            new FindUserByEmailPasswordAsyncTask(this).execute(email, password);
        }
    }

    @Override
    public void createDialog() {
        dismissDialog();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.comprobando_credenciales));
    }

    @Override
    public void createDialog(String message) {
        dismissDialog();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(message);
    }

    @Override
    public void showDialog() {
        if (progressDialog != null) {
            progressDialog.show();
        }
    }

    @Override
    public void hideDialog() {
        if (progressDialog != null) {
            progressDialog.hide();
        }
    }

    @Override
    public void dismissDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    @Override
    public Activity getDialogManagerActivity() {
        return this;
    }

}
