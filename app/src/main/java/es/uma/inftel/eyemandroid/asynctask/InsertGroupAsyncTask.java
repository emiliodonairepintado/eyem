package es.uma.inftel.eyemandroid.asynctask;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.dropbox.client2.exception.DropboxException;
import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import java.io.File;
import java.io.FileNotFoundException;

import es.uma.inftel.eyemandroid.Eyem;
import es.uma.inftel.eyemandroid.R;
import es.uma.inftel.eyemandroid.dialog.DialogManager;
import es.uma.inftel.eyemandroid.entity.Grupo;
import es.uma.inftel.eyemandroid.util.DropboxManager;

/**
 * Created by maramec on 21/03/2015.
 */
public class InsertGroupAsyncTask extends AsyncTask<Grupo, Integer, Boolean> {

    private static final String TAG = InsertGroupAsyncTask.class.getSimpleName();

    private final Activity activity;
    private final DialogManager dialogManager;
    private final DropboxManager dropboxManager;
    private OperacionGrupo operacion;

    public InsertGroupAsyncTask(Activity source, DialogManager dialogManager) {
        this.activity = source;
        this.dialogManager = dialogManager;
        this.dropboxManager = new DropboxManager();
        this.operacion = OperacionGrupo.INSERCION;
    }

    public InsertGroupAsyncTask(Activity source, DialogManager dialogManager, OperacionGrupo operacion) {
        this.activity = source;
        this.dialogManager = dialogManager;
        this.dropboxManager = new DropboxManager();
        this.operacion = operacion;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (operacion == OperacionGrupo.INSERCION) {
            dialogManager.createDialog();
            dialogManager.showDialog();
        }
    }

    @Override
    protected Boolean doInBackground(Grupo... params) {
        Gson gson = new Gson();

        Grupo grupo = params[0];

        if (grupo.getImagen() != null && !grupo.getImagen().equals(".")) {
            if(!grupo.getImagen().startsWith("pic-")){
                File groupImageFile = new File(grupo.getImagen());
                String imagePathInDropbox = uploadImageToDropbox(groupImageFile);
                grupo.setImagen(imagePathInDropbox);
                groupImageFile.delete();
            }
        }

        if (grupo.getImagenMiniatura() != null && !grupo.getImagenMiniatura().equals(".")) {
            if(!grupo.getImagenMiniatura().startsWith("pic-")) {
                File groupImageFileSmall = new File(grupo.getImagenMiniatura());
                String imageSmallPathInDropbox = uploadImageToDropbox(groupImageFileSmall);
                grupo.setImagenMiniatura(imageSmallPathInDropbox);
                groupImageFileSmall.delete();
            }
        }

        String grupoJson = gson.toJson(grupo, Grupo.class);
        String uri = Eyem.CREAR_GRUPO_REST_URI;

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(uri);

        try {
            httppost.setEntity(new StringEntity(grupoJson, "UTF-8"));
            httppost.setHeader("Accept", "application/json");
            httppost.setHeader(HTTP.CONTENT_TYPE,"application/json");
            Log.d("JSON", grupoJson);
            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            return response.getStatusLine().getStatusCode() == HttpStatus.SC_OK;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return false;
    }

    @Override
    protected void onPostExecute(Boolean resultOk) {
        super.onPostExecute(resultOk);

        dialogManager.dismissDialog();

        int resMsgOk;
        int resMsgError;

        switch (operacion) {
            case INSERCION:
                resMsgOk = R.string.grupo_creado;
                resMsgError = R.string.grupo_no_creado;
                break;
            case EDICION:
                resMsgOk = R.string.grupo_editado;
                resMsgError = R.string.grupo_no_editado;
                break;
            case SALIR:
                resMsgOk = R.string.grupo_abandonado;
                resMsgError = R.string.grupo_no_abandonado;
                break;
            default:
                throw new IllegalArgumentException();
        }

        if (resultOk) {
            Toast.makeText(activity, activity.getString(resMsgError), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(activity, activity.getString(resMsgOk), Toast.LENGTH_LONG).show();
            activity.finish();
        }
    }

    private String uploadImageToDropbox(File imageFile) {
        try {
            return dropboxManager.uploadFile(imageFile, "pic-" + System.nanoTime(), "jpg");
        } catch (FileNotFoundException e) {
            Log.e(TAG, "FileNotFoundException", e);
        } catch (DropboxException e) {
            Log.e(TAG, "DropboxException", e);
        }
        return null;
    }
}
