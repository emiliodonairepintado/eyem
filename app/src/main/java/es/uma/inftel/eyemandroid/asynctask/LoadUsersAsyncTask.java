package es.uma.inftel.eyemandroid.asynctask;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import es.uma.inftel.eyemandroid.Eyem;
import es.uma.inftel.eyemandroid.adapter.ListUsersAdapter;
import es.uma.inftel.eyemandroid.entity.Usuario;

import static android.widget.AbsListView.CHOICE_MODE_MULTIPLE;

public class LoadUsersAsyncTask extends AsyncTask<Void, Void, ArrayList<Usuario>> {

    private static final String TAG = LoadUsersAsyncTask.class.getSimpleName();

    private Activity activity;
    private ListView listView;
    private Usuario usuarioRegistrado;
    private List<Usuario> usuariosAntiguos;

    public LoadUsersAsyncTask(Activity activity, ListView listView, Usuario usuarioRegistrado, List<Usuario> usuariosAntiguos){
        this.activity = activity;
        this.listView = listView;
        this.usuarioRegistrado = usuarioRegistrado;
        this.usuariosAntiguos = usuariosAntiguos;
    }

    @Override
    protected ArrayList<Usuario> doInBackground(Void... params){
        Reader reader =  doGetRequest(Eyem.USUARIO_REST_URI);
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Usuario>>() {}.getType();

        return gson.fromJson(reader, type);
    }

    public static Reader doGetRequest(String param){
        InputStream inputStream = null;

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(param);
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
    protected void onPostExecute(ArrayList<Usuario> users) {
        listView.setChoiceMode(CHOICE_MODE_MULTIPLE);
        int i;
        for (i = 0; i<users.size();i++){
            if(usuarioRegistrado.getEmail().compareTo(users.get(i).getEmail())==0){
                users.remove(i);
            }
        }
        listView.setAdapter(new ListUsersAdapter(activity, users, usuarioRegistrado, usuariosAntiguos));


    }
}
