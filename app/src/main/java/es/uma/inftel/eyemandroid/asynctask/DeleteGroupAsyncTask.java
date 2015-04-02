package es.uma.inftel.eyemandroid.asynctask;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import es.uma.inftel.eyemandroid.Eyem;
import es.uma.inftel.eyemandroid.R;
import es.uma.inftel.eyemandroid.dialog.DialogManager;
import es.uma.inftel.eyemandroid.entity.Grupo;

/**
 * Created by maramec on 25/03/2015.
 */
public class DeleteGroupAsyncTask extends AsyncTask<String, Integer, Boolean> {

    private Activity source;
    private DialogManager dialogManager;
    private Grupo grupo;
    private Grupo grupoActualizado;
    private OperacionGrupo operacion;

    public DeleteGroupAsyncTask(Activity source, DialogManager dialogManager, Grupo grupo, Grupo grupoActualizado) {
        this.source = source;
        this.dialogManager = dialogManager;
        this.grupo = grupo;
        this.grupoActualizado = grupoActualizado;
        this.operacion = OperacionGrupo.ELIMINAR;
    }

    public DeleteGroupAsyncTask(Activity source, DialogManager dialogManager, Grupo grupo, Grupo grupoActualizado, OperacionGrupo operacion) {
        this.source = source;
        this.dialogManager = dialogManager;
        this.grupo = grupo;
        this.grupoActualizado = grupoActualizado;
        this.operacion = operacion;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (operacion == OperacionGrupo.SALIR) {
            dialogManager.createDialog(source.getString(R.string.abandonando_grupo));
        } else if (grupoActualizado.getIdGrupo().compareTo(grupo.getIdGrupo()) == 0) {
            dialogManager.createDialog(source.getString(R.string.editando_grupo));
        } else {
            dialogManager.createDialog(source.getString(R.string.eliminando_grupo));
        }
        dialogManager.showDialog();
    }

    @Override
    protected Boolean doInBackground(String... urls) {

        Gson gson = new Gson();
        String uri = Eyem.BORRARGRUPO_REST_URI;
        String postJson = gson.toJson(grupo, Grupo.class);

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httpDeleteGroup = new HttpPost(uri);

        try {
            httpDeleteGroup.setEntity(new StringEntity(postJson, "UTF-8"));
            httpDeleteGroup.setHeader("Accept", "application/json");
            httpDeleteGroup.setHeader(HTTP.CONTENT_TYPE,"application/json");
            Log.d("JSON", postJson);
            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httpDeleteGroup);
            return response.getStatusLine().getStatusCode() == HttpStatus.SC_OK;
        } catch (Exception e) {
            Log.e("MainActivity", e.getMessage(), e);
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean groupCreated) {
        super.onPostExecute(groupCreated);

        if (operacion == OperacionGrupo.SALIR) {
            new InsertGroupAsyncTask(source, dialogManager, OperacionGrupo.SALIR).execute(grupoActualizado);
        } else if (grupoActualizado.getIdGrupo().compareTo(grupo.getIdGrupo()) == 0) {
            new InsertGroupAsyncTask(source, dialogManager, OperacionGrupo.EDICION).execute(grupoActualizado);
        } else {
            if (operacion == OperacionGrupo.SALIR) {
                Toast.makeText(source, source.getString(R.string.grupo_abandonado), Toast.LENGTH_LONG).show();
            } else if (operacion == OperacionGrupo.ELIMINAR) {
                Toast.makeText(source, source.getString(R.string.grupo_eliminado), Toast.LENGTH_LONG).show();
            }
            dialogManager.dismissDialog();
            source.finish();
        }
    }
}
