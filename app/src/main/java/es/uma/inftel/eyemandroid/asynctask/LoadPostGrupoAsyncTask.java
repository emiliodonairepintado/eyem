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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import es.uma.inftel.eyemandroid.Eyem;
import es.uma.inftel.eyemandroid.activity.VerPostActivity;
import es.uma.inftel.eyemandroid.adapter.PostGrupoAdapter;
import es.uma.inftel.eyemandroid.dialog.DialogManager;
import es.uma.inftel.eyemandroid.entity.Post;
import es.uma.inftel.eyemandroid.entity.Usuario;

/**
 * Created by maramec on 24/03/2015.
 */
public class LoadPostGrupoAsyncTask extends AsyncTask< Long, Void, List<Post>> {

    private Activity source;
    private DialogManager dialogManager;
    private SwipeRefreshLayout swipeLayout;
    private ListView listView;
    private Usuario usuario;

    public LoadPostGrupoAsyncTask(Activity activity, DialogManager dialogManager, SwipeRefreshLayout swipeLayout, ListView listView, Usuario usuario) {
        this.source = activity;
        this.dialogManager = dialogManager;
        this.swipeLayout = swipeLayout;
        this.listView = listView;
        this.usuario = usuario;
    }

    protected List<Post> doInBackground(Long... params) {
        Long idGrupo = params[0];
        List<Post> postsGrupo = requestPostsGrupo(idGrupo);

        List<Post> posts = new ArrayList<>();
        if (postsGrupo != null) {
            posts.addAll(postsGrupo);
        }
        if (posts.isEmpty()) {
            return null;
        }

        // Ordenamos por fecha en orden descendente
        Collections.sort(posts, new Comparator<Post>() {
            @Override
            public int compare(Post lhs, Post rhs) {
                return rhs.getIdPost().compareTo(lhs.getIdPost());
            }
        });
        return posts;
    }

    private List<Post> requestPostsGrupo(Long idGrupo) {
        String uri = Eyem.FIND_POST_GROUP_REST_URI + "/" + idGrupo;

        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(uri);

        try {
            // Execute HTTP Get Request
            Gson gson = new Gson();
            HttpResponse response = httpclient.execute(httpGet);
            boolean ok = response.getStatusLine().getStatusCode() == HttpStatus.SC_OK;
            if (ok) {
                String json = EntityUtils.toString(response.getEntity(), "UTF-8");
                Log.d("JSON", json);
                Type listType = new TypeToken<List<Post>>() {}.getType();
                List<Post> listapost = gson.fromJson(json, listType);
                return listapost;
            }

        } catch (Exception e) {
            Log.e("MainActivity", e.getMessage(), e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(final List<Post> listapost) {
        if (listapost != null) {
            listView.setAdapter(new PostGrupoAdapter(source, dialogManager, listapost, usuario));

            //Listview on item click listener
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3) {

                    Intent intent = new Intent(source, VerPostActivity.class);
                    intent.putExtra("Post", listapost.get(position));
                    intent.putExtra("Usuario", usuario);
                    source.startActivity(intent);
                }
            });
        }
        if (swipeLayout.isRefreshing()) {
            swipeLayout.setRefreshing(false);
        }
    }
}
