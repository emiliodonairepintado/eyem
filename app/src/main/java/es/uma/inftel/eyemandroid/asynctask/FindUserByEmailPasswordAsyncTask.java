package es.uma.inftel.eyemandroid.asynctask;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import es.uma.inftel.eyemandroid.Eyem;
import es.uma.inftel.eyemandroid.R;
import es.uma.inftel.eyemandroid.activity.MainActivity;
import es.uma.inftel.eyemandroid.entity.Usuario;

public class FindUserByEmailPasswordAsyncTask extends AsyncTask<String, Void, Usuario> {

    private static final String TAG = FindUserByEmailPasswordAsyncTask.class.getSimpleName();

    private Activity activity;

    public FindUserByEmailPasswordAsyncTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected Usuario doInBackground(String... params){
        if (params == null) {
            throw new IllegalArgumentException("params can not be null");
        }
        if (params.length != 2) {
            throw new IllegalArgumentException("params must contain 2 elements");
        }

        String email = params[0].replace(".", "_");
        String password = params[1];

        Reader reader =  doGetRequest(Eyem.USUARIO_EMAIL_REST_URI, email);
        Gson gson = new Gson();
        Usuario usuario = gson.fromJson(reader, Usuario.class);
        if (usuario != null && usuario.getPass().equals(password)) {
            return usuario;
        }
        return null;
    }

    public static Reader doGetRequest(String uri, String email){
        InputStream inputStream = null;

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(uri + "/" + email);
            httpGet.setHeader("Accept", "application/json");
            httpGet.setHeader("Content-type", "application/json");
            HttpResponse httpResponse = httpclient.execute(httpGet);
            inputStream = httpResponse.getEntity().getContent();
        } catch (Exception e) {
            Log.e(TAG, "InputStream", e);
        }
        return new InputStreamReader(inputStream);
    }

    @Override
    protected void onPostExecute(Usuario usuario) {
        if (usuario == null) {
            Toast.makeText(activity, activity.getResources().getString(R.string.login_error), Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent = new Intent(activity, MainActivity.class);
        intent.putExtra("usuario", usuario);
        activity.startActivity(intent);
        activity.finish();
    }

}
