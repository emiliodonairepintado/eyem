package es.uma.inftel.eyemandroid.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * Created by miguel on 9/03/15.
 */
public class FileUtils {

    public static File createTemporaryFile(String filename, String extension) throws Exception {
        File tempDirectory = Environment.getExternalStorageDirectory();
        tempDirectory = new File(tempDirectory.getAbsolutePath() + "/.temp/");

        if(!tempDirectory.exists()) {
            tempDirectory.mkdir();
        }
        return File.createTempFile(filename + ((int) (Math.random() * Integer.MAX_VALUE)), extension, tempDirectory);
    }

    public static File createFile(Context context, String fileName) throws IOException {
        File file = new File(context.getFilesDir().getPath().toString() + "/" + fileName);
        if(!file.exists()) {
            file.mkdirs();
            if(!file.createNewFile()) {
                file.delete();
                file.createNewFile();
            }
        }
        return file;
    }

    public static File createFileDCIM(Context context, String fileName) throws IOException {
        File root = Environment.getExternalStorageDirectory();
        File file = new File(root.getAbsolutePath() + "/DCIM/" + fileName);
        if(!file.exists()) {
            file.mkdirs();
            if(!file.createNewFile()) {
                file.delete();
                file.createNewFile();
            }
        }
        return file;
    }

}
