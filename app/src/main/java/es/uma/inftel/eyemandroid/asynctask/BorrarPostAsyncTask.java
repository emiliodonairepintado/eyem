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

import java.util.List;

import es.uma.inftel.eyemandroid.Eyem;
import es.uma.inftel.eyemandroid.R;
import es.uma.inftel.eyemandroid.adapter.EyemBaseAdapter;
import es.uma.inftel.eyemandroid.dialog.DialogManager;
import es.uma.inftel.eyemandroid.entity.Post;

/**
 * Created by Emilio on 23/03/2015.
 */
public class BorrarPostAsyncTask extends AsyncTask<String, Integer, Boolean> {

    private final DialogManager dialogManager;
    private Activity source;
    private Post post;
    private EyemBaseAdapter adapter;
    private int position;

    public BorrarPostAsyncTask(Activity source, DialogManager dialogManager, Post post) {
        this.source = source;
        this.dialogManager = dialogManager;
        this.post = post;
        this.adapter = null;
        this.position = -1;
    }

    public BorrarPostAsyncTask(Activity source, DialogManager dialogManager, Post post, EyemBaseAdapter adapter, int position) {
        this.source = source;
        this.dialogManager = dialogManager;
        this.post = post;
        this.adapter = adapter;
        this.position = position;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialogManager.createDialog(source.getString(R.string.eliminando_post));
        dialogManager.showDialog();
    }

    @Override
    protected Boolean doInBackground(String... urls) {

        Gson gson = new Gson();
        String uri = Eyem.BORRARPOST_REST_URI;
        String postJson = gson.toJson(post, Post.class);

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httpDeletePost = new HttpPost(uri);

        try {
            httpDeletePost.setEntity(new StringEntity(postJson, "UTF-8"));
            httpDeletePost.setHeader("Accept", "application/json");
            httpDeletePost.setHeader(HTTP.CONTENT_TYPE,"application/json");
            Log.d("JSON", postJson);
            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httpDeletePost);
            return response.getStatusLine().getStatusCode() == HttpStatus.SC_OK;
        } catch (Exception e) {
            Log.e("MainActivity", e.getMessage(), e);
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (adapter != null) {
            List data = adapter.getData();
            if (data != null && !data.isEmpty()) {
                data.remove(position);
                adapter.notifyDataSetChanged();
            }
        }
        dialogManager.dismissDialog();
        Toast.makeText(source, source.getString(R.string.post_eliminado), Toast.LENGTH_LONG).show();
    }
}
