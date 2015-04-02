package es.uma.inftel.eyemandroid.util;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by miguel on 9/03/15.
 */
public class ImageUtils {

    private static final String TAG = ImageUtils.class.getSimpleName();

    public static Bitmap getImageFromUri(Context context, Uri imageUri) {
        ContentResolver contentResolver = context.getContentResolver();
        contentResolver.notifyChange(imageUri, null);
        try {
            return MediaStore.Images.Media.getBitmap(contentResolver, imageUri);
        } catch (Exception e) {
            Log.d(ImageUtils.class.getSimpleName(), "Failed to load", e);
        }
        return null;
    }

    public static Bitmap createScaledBitmap(Bitmap sourceBitmap, float maxWidth, float maxHeight) {
        float width = sourceBitmap.getWidth();
        float height = sourceBitmap.getHeight();

        int scaledWidth;
        int scaledHeight;

        float xRatio = maxWidth / width;
        float yRatio = maxHeight / height;
        if (xRatio > yRatio) {
            scaledWidth = (int) (width * yRatio);
            scaledHeight = (int) (height * yRatio);
        } else {
            scaledWidth = (int) (width * xRatio);
            scaledHeight = (int) (height * xRatio);
        }
        return Bitmap.createScaledBitmap(sourceBitmap, scaledWidth, scaledHeight, true);
    }

    public static File storeImage(Bitmap image, int maxWidth, int maxHeight, File outputFile) {
        Bitmap scaledBitmap = ImageUtils.createScaledBitmap(image, maxWidth, maxHeight);
        return storeImage(scaledBitmap, outputFile);
    }

    public static File storeImage(Bitmap image, File outputFile) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
            image.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.close();
            return outputFile;
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
        return null;
    }
}
