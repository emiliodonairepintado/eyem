package es.uma.inftel.eyemandroid.asynctask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;


public class LoadImageAsyncTask extends AsyncTask<String, Void, Bitmap> {

    private ImageView imageView;

    public LoadImageAsyncTask(ImageView imageView){
        this.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        URL url = null;
        try {
            url = new URL(params[0]);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        assert url != null;
        InputStream openUrl = null;
        try {
            openUrl = (InputStream) url.getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return BitmapFactory.decodeStream(openUrl);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        imageView.setImageBitmap(bitmap);
    }
}
