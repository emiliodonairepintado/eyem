package es.uma.inftel.eyemandroid.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import es.uma.inftel.eyemandroid.R;
import es.uma.inftel.eyemandroid.widget.menu.MenuEntries;
import es.uma.inftel.eyemandroid.widget.menu.MenuEntry;

/**
 * Created by miguel on 18/03/15.
 */
public class MenuEntryAdapter extends BaseAdapter {

    private final Activity activity;
    private final LayoutInflater inflater;
    private final MenuEntries menuEntries;
    private final String[] menuEntriesTexts;

    public MenuEntryAdapter(Activity activity) {
        this.activity = activity;

        menuEntries = new MenuEntries();
        menuEntries.addEntry(new MenuEntry(R.string.timeline, R.drawable.ic_action_timeline, activity));
        menuEntries.addEntry(new MenuEntry(R.string.mi_eyem, R.drawable.ic_action_public, activity));
        menuEntries.addEntry(new MenuEntry(R.string.grupos, R.drawable.ic_action_groups, activity));
        menuEntries.addEntry(new MenuEntry(R.string.generar_qr, R.drawable.ic_action_generate_qr, activity));
        menuEntries.addEntry(new MenuEntry(R.string.desconectarse, R.drawable.ic_action_exit, activity));

        menuEntriesTexts = menuEntries.getEntriesTexts();

        inflater = LayoutInflater.from(activity);
    }

    @Override
    public int getCount() {
        return menuEntries.getEntries().size();
    }

    @Override
    public Object getItem(int position) {
        return menuEntriesTexts[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MenuEntryViewHolder viewHolder;

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_menu_entry, null);
            viewHolder = new MenuEntryViewHolder();
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MenuEntryViewHolder) convertView.getTag();
        }

        MenuEntry menuEntry = menuEntries.getEntries().get(position);

        viewHolder.icon = (ImageView) convertView.findViewById(R.id.icon);
        viewHolder.icon.setImageBitmap(menuEntry.getIcon());

        viewHolder.text = (TextView) convertView.findViewById(R.id.text);
        viewHolder.text.setText(menuEntry.getText());

        return convertView;
    }

    private static class MenuEntryViewHolder {

        private ImageView icon;
        private TextView text;
    }
}
