package es.uma.inftel.eyemandroid.util;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;

import es.uma.inftel.eyemandroid.R;
import es.uma.inftel.eyemandroid.asynctask.AsyncTaskManager;
import es.uma.inftel.eyemandroid.dialog.DialogManager;

/**
 * Created by miguel on 12/03/15.
 */
public class SnackBarUtils {

    public static void showConnectionErrorSnackBar(final Activity activity, final AsyncTaskManager asyncTaskManager, final DialogManager dialogManager) {
        SnackbarManager.show(
                Snackbar.with(activity)
                        .text(activity.getString(R.string.connection_error))
                        .duration(Snackbar.SnackbarDuration.LENGTH_INDEFINITE)
                        .actionLabel(activity.getString(R.string.try_again))
                        .actionListener(new ActionClickListener() {

                            @Override
                            public void onActionClicked(Snackbar snackbar) {
                                modifyMargins(snackbar, false, activity);
                                dialogManager.dismissDialog();
                                dialogManager.createDialog();
                                dialogManager.showDialog();
                                asyncTaskManager.createTask();
                                asyncTaskManager.executeTask();
                            }
                        })
        );
        Snackbar currentSnackbar = SnackbarManager.getCurrentSnackbar();
        modifyMargins(currentSnackbar, true, activity);
    }

    private static void modifyMargins(Snackbar snackbar, boolean big, Activity activity) {
        int height = snackbar.getMinimumHeight();
        int smallMarginDp = 16;
        int smallMarginPixels = (int) UnitsConverter.convertDpToPixel(smallMarginDp, activity);
        int bigMarginPixels = smallMarginPixels + height;
        View view = activity.findViewById(R.id.fabButton);
        if (view != null) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
            layoutParams.setMargins(smallMarginPixels, smallMarginPixels, smallMarginPixels, (big) ? bigMarginPixels : smallMarginPixels);
            view.requestLayout();
        }
    }
}
