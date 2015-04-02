package es.uma.inftel.eyemandroid.asynctask;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

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
import es.uma.inftel.eyemandroid.adapter.PerfilAdapter;
import es.uma.inftel.eyemandroid.entity.Post;

/**
 * Created by miguel on 21/03/15.
 */
public class FindUserPublicPostsAsyncTask extends AsyncTask< String, Void, List<Post>> {

    private Activity source;
    private SwipeRefreshLayout swipeLayout;
    private LinearLayout linearLayout;

    public FindUserPublicPostsAsyncTask(Activity activity, SwipeRefreshLayout swipeLayout, LinearLayout linearLayout) {
        this.source = activity;
        this.swipeLayout = swipeLayout;
        this.linearLayout = linearLayout;
    }

    protected List<Post> doInBackground(String... params) {
        String email = params[0].replace(".", "_");
        List<Post> postsPublicos = requestPostsPublicos(email);
        List<Post> postsReplicados = requestPostsReplicados(email);

        List<Post> posts = new ArrayList<>();
        if (postsPublicos != null) {
            posts.addAll(postsPublicos);
        }
        if (postsReplicados != null) {
            posts.addAll(postsReplicados);
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

    private List<Post> requestPostsPublicos(String email) {
        String uri = Eyem.FIND_USER_POSTS_REST_URI + "/" + email;

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

    private List<Post> requestPostsReplicados(String email) {
        String uri = Eyem.FIND_USER_REPLICATED_POSTS_REST_URI + "/" + email;

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
        if(listapost != null) {
            linearLayout.removeAllViews();

            PerfilAdapter adapter = new PerfilAdapter(source, listapost);
            final int adapterCount = adapter.getCount();
            for (int i = 0; i < adapterCount; i++) {
                View view = adapter.getView(i, null, null);
                final int position = i;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(source, VerPostActivity.class);
                        intent.putExtra("Post", listapost.get(position));
                        source.startActivity(intent);
                    }
                });
                linearLayout.addView(view);
            }
        }
        if (swipeLayout.isRefreshing()) {
            swipeLayout.setRefreshing(false);
        }
    }
}
