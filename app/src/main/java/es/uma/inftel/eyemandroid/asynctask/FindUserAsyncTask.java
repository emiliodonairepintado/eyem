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
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;

import es.uma.inftel.eyemandroid.Eyem;
import es.uma.inftel.eyemandroid.activity.PerfilActivity;
import es.uma.inftel.eyemandroid.adapter.UserFindAdapter;
import es.uma.inftel.eyemandroid.entity.Usuario;

/**
 * Created by inftel21 on 20/3/15.
 */
public class FindUserAsyncTask extends AsyncTask<String, Void, ArrayList<Usuario>> {

    private Activity activity;
    private SwipeRefreshLayout swipeLayout;
    private ListView listView;
    private View vNoResults;

    public FindUserAsyncTask(Activity activity, SwipeRefreshLayout swipeLayout, ListView listaView, View vNoResults) {
        this.activity = activity;
        this.swipeLayout = swipeLayout;
        this.listView = listaView;
        this.vNoResults = vNoResults;
    }

    @Override
    protected ArrayList<Usuario> doInBackground(String... params) {
        String url = Eyem.Usuario_REST_NAME_URI + "/" + params[0].replace(" ", "%20");
        Reader reader = requestGet(url);
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Usuario>>() {}.getType();
        return gson.fromJson(reader, type);
    }

    public static Reader requestGet(String param) {
        InputStream inputStream = null;

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(param);
            httpGet.setHeader("Accept", "application/json");
            httpGet.setHeader("Content-type", "application/json");
            HttpResponse httpResponse = httpclient.execute(httpGet);
            inputStream = httpResponse.getEntity().getContent();
        } catch (Exception e) {
            Log.e(FindUserAsyncTask.class.getSimpleName(), "InputStream", e);
        }
        return new InputStreamReader(inputStream);
    }

    @Override
    protected void onPostExecute(final ArrayList<Usuario> listaUsuarios) {
        if (listaUsuarios != null && !listaUsuarios.isEmpty()) {
            listView.setAdapter(new UserFindAdapter(this.activity, listaUsuarios));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(activity, PerfilActivity.class);
                    intent.putExtra("usuario", listaUsuarios.get(position));
                    activity.startActivity(intent);
                }
            });
            vNoResults.setVisibility(View.GONE);
        } else {
            vNoResults.setVisibility(View.VISIBLE);
        }
        if (swipeLayout.isRefreshing()) {
            swipeLayout.setRefreshing(false);
        }
    }


}
