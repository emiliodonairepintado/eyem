package es.uma.inftel.eyemandroid.widget.menu;

import android.app.Activity;
import android.graphics.Bitmap;

import es.uma.inftel.eyemandroid.util.ActivityUtils;

/**
 * Created by miguel on 18/03/15.
 */
public class MenuEntry {

    private Bitmap icon;
    private String text;

    public MenuEntry(int textResId, int iconResId, Activity activity) {
        this(activity.getString(textResId), ActivityUtils.loadBitmapFromDrawable(activity, iconResId));
    }

    public MenuEntry(String text, Bitmap icon) {
        this.text = text;
        this.icon = icon;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
