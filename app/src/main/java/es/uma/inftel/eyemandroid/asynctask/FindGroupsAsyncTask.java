package es.uma.inftel.eyemandroid.asynctask;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import es.uma.inftel.eyemandroid.Eyem;
import es.uma.inftel.eyemandroid.activity.MostrarGrupoActivity;
import es.uma.inftel.eyemandroid.adapter.GruposAdapter;
import es.uma.inftel.eyemandroid.entity.Grupo;
import es.uma.inftel.eyemandroid.entity.Usuario;

/**
 * Created by maramec on 23/03/2015.
 */
public class FindGroupsAsyncTask extends AsyncTask< String, Void, List<Grupo>> {

    private Activity source;
    private SwipeRefreshLayout swipeLayout;
    private ListView listView;

    private final Usuario usuario;

    public FindGroupsAsyncTask(Activity activity, SwipeRefreshLayout swipeLayout, ListView listView, Usuario usuario) {
        this.source = activity;
        this.swipeLayout = swipeLayout;
        this.listView = listView;
        this.usuario = usuario;
    }

    protected List<Grupo> doInBackground(String... params) {

        String email = params[0].replace(".", "_");
        List<Grupo> listaGrupos = requestGrupos(email);
        List<Grupo> grupos = new ArrayList<>();
        if (listaGrupos != null) {
            grupos.addAll(listaGrupos);
        }
        if (grupos.isEmpty()) {
            return null;
        }
        return grupos;
    }

    private List<Grupo> requestGrupos(String email) {
        String uri = Eyem.FIND_USER_GROUPS + "/" + email;

        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(uri);

        try {
            // Execute HTTP Get Request
            Gson gson = new Gson();
            HttpResponse response = httpclient.execute(httpGet);
            boolean ok = response.getStatusLine().getStatusCode() == HttpStatus.SC_OK;
            if (ok) {
                String json = EntityUtils.toString(response.getEntity(), "UTF-8");
                Type listType = new TypeToken<List<Grupo>>() {}.getType();
                List<Grupo> listaGrupos = gson.fromJson(json, listType);
                return listaGrupos;
            }

        } catch (Exception e) {
            Log.e("MainActivity", e.getMessage(), e);
        }

        return null;
    }

    protected void onPostExecute(final List<Grupo> listaGrupos) {
        if (listaGrupos == null) {
            listView.setAdapter(new GruposAdapter(new ArrayList<Grupo>(), usuario, source));
        }
        if(listaGrupos != null && !listaGrupos.isEmpty()) {
            listView.setAdapter(new GruposAdapter(listaGrupos, usuario, source));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(source, MostrarGrupoActivity.class);
                    intent.putExtra("grupoSeleccionado",listaGrupos.get(position));
                    intent.putExtra("usuario", usuario);
                    source.startActivity(intent);
                }

            });

        }
        if (swipeLayout.isRefreshing()) {
            swipeLayout.setRefreshing(false);
        }
    }
}