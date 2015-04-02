package es.uma.inftel.eyemandroid.util;

import android.content.Context;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AppKeyPair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by miguel on 9/03/15.
 */
public class DropboxManager {

    private static final String APP_KEY = "j6q5n3blf14n0vl";
    private static final String APP_SECRET = "01q30519n03om71";

    private static  final String OAUTH2_ACCESS_TOKEN = "-rUl939EeUAAAAAAAAAABEkPeOk6YGCGrjIdPU0BCp8fNuK6GAzr1r9f-g4xpMOH";

    private DropboxAPI<AndroidAuthSession> dropboxApi;

    public DropboxManager() {
        AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeys);
        dropboxApi = new DropboxAPI<>(session);
        dropboxApi.getSession().setOAuth2AccessToken(OAUTH2_ACCESS_TOKEN);
    }

    public String uploadFile(File file, String fileName, String fileExtension) throws FileNotFoundException, DropboxException {
        FileInputStream inputStream = new FileInputStream(file);
        String destFilePath = "/" + fileName + "-" + System.nanoTime() + "." + fileExtension;
        DropboxAPI.Entry response = dropboxApi.putFile(destFilePath, inputStream, file.length(), null, null);
        return response.fileName();
    }

    public File downloadFile(Context context, String fileName) throws IOException, DropboxException {
        File file = FileUtils.createFile(context, fileName);
        FileOutputStream outputStream = new FileOutputStream(file);
        dropboxApi.getFile("/" + fileName, null, outputStream, null);
        return file;
    }

}
