package es.uma.inftel.eyemandroid.dialog;

import android.app.Activity;

/**
 * Created by miguel on 9/03/15.
 */
public interface DialogManager {

    void createDialog();

    void createDialog(String message);

    void showDialog();

    void hideDialog();

    void dismissDialog();

    Activity getDialogManagerActivity();

}
