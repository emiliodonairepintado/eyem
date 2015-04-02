package es.uma.inftel.eyemandroid.adapter;

import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by miguel on 26/03/15.
 */
public abstract class EyemBaseAdapter<T> extends BaseAdapter {

    private List<T> data;

    public EyemBaseAdapter(List<T> data) {
        this.data = data;
    }

    public List<T> getData() {
        return data;
    }
}
