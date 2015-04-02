package es.uma.inftel.eyemandroid.asynctask;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
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
import es.uma.inftel.eyemandroid.entity.Post;
import es.uma.inftel.eyemandroid.entity.Usuario;


/**
 * Created by Emilio on 20/03/2015.
 */
public class ReplicarPostAsyncTask extends AsyncTask<Post, Integer, Boolean> {

    private static final String TAG = ReplicarPostAsyncTask.class.getSimpleName();

    private final Activity activity;
    private final DialogManager dialogManager;
    private final Usuario usuario;
    private final ImageButton btnReplicar;

    public ReplicarPostAsyncTask(Activity source, DialogManager dialogManager, Usuario usuario, ImageButton btnReplicar) {
        this.activity = source;
        this.dialogManager = dialogManager;
        this.usuario = usuario;
        this.btnReplicar = btnReplicar;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialogManager.createDialog();
        dialogManager.showDialog();
    }

    @Override
    protected Boolean doInBackground(Post... params) {

        Gson gson = new Gson();

        Post post = params[0];

        String postJson = gson.toJson(post, Post.class);
        String uri = Eyem.REPLICAR_POST_REST_URI + "?email=" + usuario.getEmail().replace(".", "_");

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(uri);

        try {
            httppost.setEntity(new StringEntity(postJson, "UTF-8"));
            httppost.setHeader("Accept", "application/json");
            httppost.setHeader(HTTP.CONTENT_TYPE,"application/json");
            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            return response.getStatusLine().getStatusCode() == HttpStatus.SC_OK;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return false;
    }

    @Override
    protected void onPostExecute(Boolean postCreated) {
        super.onPostExecute(postCreated);

        dialogManager.dismissDialog();

        if (postCreated) {
            Toast.makeText(activity, activity.getString(R.string.post_no_replicado), Toast.LENGTH_LONG).show();
        } else {
            btnReplicar.setVisibility(View.GONE);
            Toast.makeText(activity, activity.getString(R.string.post_replicado), Toast.LENGTH_LONG).show();
        }
    }
}
