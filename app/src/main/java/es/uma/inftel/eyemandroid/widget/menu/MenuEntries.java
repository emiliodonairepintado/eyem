package es.uma.inftel.eyemandroid.widget.menu;

import java.util.ArrayList;

/**
 * Created by miguel on 18/03/15.
 */
public class MenuEntries {

    private ArrayList<MenuEntry> entries;

    public MenuEntries() {
        this.entries = new ArrayList<>();
    }

    public void addEntry(MenuEntry menuEntry) {
        entries.add(menuEntry);
    }

    public ArrayList<MenuEntry> getEntries() {
        return entries;
    }

    public MenuEntry getEntry(int position) {
        return entries.get(position);
    }

    public String[] getEntriesTexts() {
        String[] texts = new String[entries.size()];
        int i = 0;
        for (MenuEntry entry : entries) {
            texts[i++] = entry.getText();
        }
        return texts;
    }
}
