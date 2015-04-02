package es.uma.inftel.eyemandroid.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by miguel on 20/03/15.
 */
public class QRUtils {

    public static final String TAG = QRUtils.class.getSimpleName();
    public static final String TEXT = "TEXT_TYPE";

    public static Bitmap generarQR(String pass, Activity activity, String fileName){
        Display display = ((WindowManager)activity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3/4;

        QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(pass,
                null,
                TEXT,
                BarcodeFormat.QR_CODE.toString(),
                smallerDimension);

        Bitmap bitmapQR = null;

        try {
            // Genera el QR
            bitmapQR = qrCodeEncoder.encodeAsBitmap();

            // Guarda el QR en la memoria del telefono
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmapQR.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
            File file = FileUtils.createFileDCIM(activity, fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bytes.toByteArray());
            fileOutputStream.close();

        } catch (WriterException e) {
            Log.e(TAG, "WriterException", e);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "FileNotFoundException", e);
        } catch (IOException e) {
            Log.e(TAG, "IOException", e);
        }

        return bitmapQR;
    }
}
