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
import es.uma.inftel.eyemandroid.entity.Post;
import es.uma.inftel.eyemandroid.util.DropboxManager;


/**
 * Created by Emilio on 20/03/2015.
 */
public class CrearPostAsyncTask extends AsyncTask<Post, Integer, Boolean> {

    private static final String TAG = CrearPostAsyncTask.class.getSimpleName();

    private final Activity activity;
    private final DialogManager dialogManager;
    private final DropboxManager dropboxManager;

    public CrearPostAsyncTask(Activity source, DialogManager dialogManager) {
        this.activity = source;
        this.dialogManager = dialogManager;
        this.dropboxManager = new DropboxManager();
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

        if (post.getImagen() != null && !post.getImagen().equals(".")) {
            File postImageFile = new File(post.getImagen());
            String imagePathInDropbox = uploadImageToDropbox(postImageFile);
            post.setImagen(imagePathInDropbox);
            postImageFile.delete();
        }

        String postJson = gson.toJson(post, Post.class);
        String uri = Eyem.CREARPOST_REST_URI;

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(uri);

        try {
            httppost.setEntity(new StringEntity(postJson, "UTF-8"));
            httppost.setHeader("Accept", "application/json");
            httppost.setHeader(HTTP.CONTENT_TYPE,"application/json");
            Log.d("JSON", postJson);
            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            return response.getStatusLine().getStatusCode() == HttpStatus.SC_OK;
        } catch (Exception e) {
            Log.e("MainActivity", e.getMessage(), e);
        }

        return false;
    }

    @Override
    protected void onPostExecute(Boolean postCreated) {
        super.onPostExecute(postCreated);

        dialogManager.dismissDialog();

        if (postCreated) {
            Toast.makeText(activity, activity.getString(R.string.post_no_creado), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(activity, activity.getString(R.string.post_creado), Toast.LENGTH_LONG).show();
            activity.finish();
        }
        activity.finish();
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
