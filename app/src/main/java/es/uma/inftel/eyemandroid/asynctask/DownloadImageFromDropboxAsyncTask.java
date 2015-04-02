package es.uma.inftel.eyemandroid.asynctask;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.dropbox.client2.exception.DropboxException;

import java.io.File;
import java.io.IOException;

import es.uma.inftel.eyemandroid.util.DropboxManager;
import es.uma.inftel.eyemandroid.util.ImageUtils;

/**
 * Created by miguel on 9/03/15.
 */
public class DownloadImageFromDropboxAsyncTask extends AsyncTask<String, Void, File> {

    private final Activity activity;
    private final ImageView imageView;
    private final DropboxManager dropboxManager;

    public DownloadImageFromDropboxAsyncTask(Activity activity, ImageView bmImage) {
        this.activity = activity;
        this.imageView = bmImage;
        this.dropboxManager = new DropboxManager();
    }

    @Override
    protected File doInBackground(String... params) {
        if (params == null) {
            throw new IllegalArgumentException("params can not be null");
        }
        if (params.length != 1) {
            throw new IllegalArgumentException("params must contain 1 element");
        }
        try {
            return dropboxManager.downloadFile(activity, params[0]);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DropboxException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(File result) {
        if (result == null) {
            return;
        }
        Bitmap bitmap = ImageUtils.getImageFromUri(activity, Uri.fromFile(result));
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }
    }
}
