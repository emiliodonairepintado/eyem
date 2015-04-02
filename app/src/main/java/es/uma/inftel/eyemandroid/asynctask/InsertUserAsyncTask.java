package es.uma.inftel.eyemandroid.asynctask;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import es.uma.inftel.eyemandroid.Eyem;
import es.uma.inftel.eyemandroid.activity.MainActivity;
import es.uma.inftel.eyemandroid.dialog.DialogManager;
import es.uma.inftel.eyemandroid.entity.Usuario;
import es.uma.inftel.eyemandroid.util.SnackBarUtils;

/**
 * Created by inftel14 on 10/3/15.
 */
public class InsertUserAsyncTask extends AsyncTask<Usuario, Void, Usuario> {

    private static final String TAG = InsertUserAsyncTask.class.getSimpleName();

    private Activity activity;
    private final AsyncTaskManager asyncTaskManager;
    private final DialogManager dialogManager;

    public InsertUserAsyncTask(Activity activity, AsyncTaskManager asyncTaskManager, DialogManager dialogManager) {
        this.activity = activity;
        this.asyncTaskManager = asyncTaskManager;
        this.dialogManager = dialogManager;
    }

    @Override
    protected Usuario doInBackground(Usuario... params) {
        if (params == null) {
            throw new IllegalArgumentException("params can not be null");
        }
        if (params.length != 1) {
            throw new IllegalArgumentException("params must contain 1 element");
        }
        String uri = Eyem.USUARIO_REST_URI;
        try {
            Gson gson = new Gson();

            Usuario user = params[0];
            String userImage = user.getImagen();
            if (userImage != null) {
                user.setImagen(userImage.replace("sz=50", "sz=128"));
            }

            String json = gson.toJson(params[0], Usuario.class);

            HttpPost httpPost = new HttpPost(uri);
            httpPost.setEntity(new StringEntity(json));
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json;charset=ISO-8859-1");
            HttpResponse response = new DefaultHttpClient().execute(httpPost);
            boolean registered = response.getStatusLine().getStatusCode() == HttpStatus.SC_OK;
            if (registered) {
                String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
                Usuario responseUser = gson.fromJson(responseString, Usuario.class);
                String password = responseUser.getPass();
                user.setPass(password);
                return user;
            }
        } catch (UnsupportedEncodingException e) {
            Log.w(TAG, "UnsupportedEncodingException", e);
        } catch (ClientProtocolException e) {
            Log.w(TAG, "ClientProtocolException", e);
        } catch (IOException e) {
            Log.w(TAG, "IOException", e);
        } catch (Exception e) {
            Log.w(InsertUserAsyncTask.class.getSimpleName(), "Exception", e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Usuario usuario) {
        super.onPostExecute(usuario);

        if (usuario == null) {
            dialogManager.dismissDialog();
            SnackBarUtils.showConnectionErrorSnackBar(activity, asyncTaskManager, dialogManager);
            return;
        }

        Intent intent = new Intent(activity, MainActivity.class);
        intent.putExtra("usuario", usuario);
        activity.startActivity(intent);
        activity.finish();
    }

}


